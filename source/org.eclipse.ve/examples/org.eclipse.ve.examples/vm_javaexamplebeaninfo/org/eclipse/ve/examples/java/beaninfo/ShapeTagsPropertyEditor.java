package org.eclipse.ve.examples.java.beaninfo;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.beans.*;

/**
 * Property editor for shape that illustrates how the customizer goes through the property editor
 * to calculate the initializationString
 */
public class ShapeTagsPropertyEditor extends PropertyEditorSupport {
	protected int fShapeIndex = -1;
public ShapeTagsPropertyEditor() {
	super();
}
public ShapeTagsPropertyEditor(Object source) {
	super(source);
}
public String getAsText() {

	if ( fShapeIndex != -1 ) {
		return ShapeHelper.fShapeNames[fShapeIndex];
	} else {
		return "";
	}

}
	
public String getJavaInitializationString(){

	if ( fShapeIndex != -1 ) {
		return ShapeHelper.fInitStrings[fShapeIndex];
	} else {
		return "";
	}

}
public String[] getTags(){
	return ShapeHelper.fShapeNames;
}
public void setAsText(String text) {

	fShapeIndex = -1;
	fShapeIndex = ShapeHelper.getShapeIndex(text);
	setValue(new Integer(fShapeIndex));

}
	
public void setValue(Object aValue){

	super.setValue(aValue);
	fShapeIndex = ((Integer)aValue).intValue();

}
}
