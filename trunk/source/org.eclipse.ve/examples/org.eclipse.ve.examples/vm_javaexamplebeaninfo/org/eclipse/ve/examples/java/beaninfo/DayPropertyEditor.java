/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.beaninfo;

import java.awt.*;
import java.beans.*;

/**
 * Property Editor that lets you edit days from 0-6 through
 * their strings
 */
public class DayPropertyEditor extends PropertyEditorSupport {
	protected int fDay;
	protected DayCustomEditor fEditor;
public void setAsText(String text) throws IllegalArgumentException {

	for(int i=0;i<=6;i++){
		if(DayHelper.DAY_NAMES[i].equals(text)){
			setValue(new Integer(i));
			return;
		}
	}
	// The day is not a valid one
	throw new IllegalArgumentException(text + " is not a valid weekday, e.g. Monday, Tuesday, etc...");
}
public Object getValue(){
	return new Integer(fEditor != null ? fEditor.getDay() : fDay);
}
public void setValue(Object aValue){
	fDay = ((Integer)aValue).intValue();
	if (fEditor != null)
		fEditor.setDay(fDay);
}
public String getAsText(){
	int day = ((Integer) getValue()).intValue();
	if ( day >= 0 || day <= 6 ) {
		return DayHelper.DAY_NAMES[day];
	} else {
		return "UNKNOWN";
	}
}
public String getJavaInitializationString(){
	return DayHelper.INIT_STRINGS[((Integer) getValue()).intValue()];
}
public boolean supportsCustomEditor(){
	return true;
}
public Component getCustomEditor(){
	if (fEditor == null)
		fEditor = new DayCustomEditor(fDay);
	return fEditor;
}
}
