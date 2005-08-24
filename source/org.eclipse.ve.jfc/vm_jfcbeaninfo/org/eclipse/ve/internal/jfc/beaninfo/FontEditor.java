/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: FontEditor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.awt.*;
import java.beans.*;

public class FontEditor extends PropertyEditorSupport {
	private static java.util.ResourceBundle resabtedit = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.vceedit");  //$NON-NLS-1$
	FontPropertyEditor customEditor = null;
	public String getAsText() {
	String strStyle;
	Font font = null;
	try{
		font = (Font) getValue();
	} catch (ClassCastException exc){
		System.err.print("Value not a font = " + getValue()); //$NON-NLS-1$
		exc.printStackTrace();
	}
	if (font == null)
		return "";	//$NON-NLS-1$
	if (font.isBold()) {
		strStyle = font.isItalic() ? resabtedit.getString("bolditalic") : resabtedit.getString("bold"); //$NON-NLS-2$ //$NON-NLS-1$
	} else {
		strStyle = font.isItalic() ? resabtedit.getString("italic") : resabtedit.getString("plain"); //$NON-NLS-2$ //$NON-NLS-1$
	}
	return font.getName() + ", " + strStyle + ", " + font.getSize(); //$NON-NLS-2$//$NON-NLS-1$
}
public Component getCustomEditor() {
	if (customEditor == null) {
		Font aFont = (Font)getValue();
		if (aFont == null) {
			customEditor = new FontPropertyEditor();
		} else {
			customEditor = new FontPropertyEditor(aFont);
		}
	}	
	return customEditor;
}
public String getStyleNamedConstant(int style) {
    String constant = String.valueOf( style );
    
    switch (style) {
    	case Font.PLAIN              : constant = "java.awt.Font.PLAIN"; break; //$NON-NLS-1$
    	case Font.BOLD               : constant = "java.awt.Font.BOLD"; break; //$NON-NLS-1$
    	case Font.ITALIC             : constant = "java.awt.Font.ITALIC"; break; //$NON-NLS-1$
    	case Font.BOLD | Font.ITALIC : constant = "java.awt.Font.BOLD | java.awt.Font.ITALIC"; break; //$NON-NLS-1$
    }
    
    return constant;
}
public String getJavaInitializationString() {
	
	Font font = (Font)getValue();
	if (font != null)	
		return "new java.awt.Font(\"" + font.getName() + "\", " +//$NON-NLS-2$//$NON-NLS-1$
			getStyleNamedConstant(font.getStyle()) + ", " + font.getSize() + ")";//$NON-NLS-2$//$NON-NLS-1$
	return "null"; //$NON-NLS-1$
}
public Object getValue() {

	if (customEditor != null) {
		return customEditor.getFontValue();
	} else {
		return super.getValue();
	}	
}
public void setAsText(String text) throws IllegalArgumentException {
	throw new IllegalArgumentException(text);
}
public void setValue(Object newValue) {
	
	if (customEditor != null) {
		customEditor.setFontValue((Font)newValue);
	}
	super.setValue(newValue);	
}
public boolean supportsCustomEditor() {
	return true;
}
}
