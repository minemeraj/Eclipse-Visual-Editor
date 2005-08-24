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

import java.beans.PropertyEditorSupport;
/**
 * Property Editor that lets you edit days from 0-6 through
 * their strings
 */
public class MonthPropertyEditor extends PropertyEditorSupport {
	protected int fMonth;
	protected static String[] MONTHS = new String[] {
		"January","February","March","April","May","June","July","August","September","October","November","December"};
public void setAsText(String text) throws IllegalArgumentException {

	if(text != null) {
		for(int i=0;i<MONTHS.length;i++){
			if(MONTHS[i].toLowerCase().equals(text.toLowerCase())){
				fMonth = i;
				return;
			}
		}
	}
	// The month is not a valid one
	throw new IllegalArgumentException(text + " is not a valid month, e.g. January, February, etc...");
}
public Object getValue(){
	return new Integer(fMonth);
}
public void setValue(Object aValue){
	fMonth = ((Integer)aValue).intValue();
}
public String getAsText(){
	return MONTHS[fMonth];
}
public String getJavaInitializationString(){
	return String.valueOf(fMonth);
}
}
