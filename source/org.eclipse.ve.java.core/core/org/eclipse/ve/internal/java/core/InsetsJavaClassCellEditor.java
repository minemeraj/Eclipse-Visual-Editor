package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: InsetsJavaClassCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.StringTokenizer;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.java.impl.JavaClassImpl;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
/**
 * Cell Editor for Insets Beans.
 */
public class InsetsJavaClassCellEditor extends DefaultJavaClassCellEditor {
	
public InsetsJavaClassCellEditor(Composite aComposite){
	super(aComposite);
}

/**
 * Returns the string for the value.
 */
protected String doGetString(Object value) {
	if (isInstance(value)) {
		return InsetsJavaClassLabelProvider.toString((IJavaInstance) value);
	} else
		return null;
}

protected String getJavaInitializationString(String insetsString) {
	// want to make sure nicely formed (i.e. no extra spaces). This assumes the string is valid. This shouldn't be called if it isn't.
	StringTokenizer st = new StringTokenizer(insetsString, ","); //$NON-NLS-1$
	StringBuffer sb = new StringBuffer(insetsString.length());
	sb.append("new java.awt.Insets("); //$NON-NLS-1$
	sb.append(st.nextToken().trim());
	while (st.hasMoreTokens()) {
		sb.append(',');
		sb.append(st.nextToken().trim());
	}
	sb.append(')');
	return (sb.toString());
}

/**
 * Parse the text string to see if it is a valid rectangle, e.g. x,y,width,height
 */
protected String isCorrectString(String text) {
	StringTokenizer st = new StringTokenizer(text, ","); //$NON-NLS-1$
	
	String[] tokenMsgs = new String[] {JavaMessages.getString("CellEditor.Insets.TopErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Insets.LeftErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Insets.BottomErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Insets.RightErrorMsg_ERROR_")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	if (st.countTokens() != tokenMsgs.length)
		return JavaMessages.getString("CellEditor.Insets.ErrorMsg_ERROR_"); //$NON-NLS-1$
	
	for (int i=0; i<tokenMsgs.length; i++) {
		String field = st.nextToken().trim();
		try {
			Integer.decode(field);
		} catch ( NumberFormatException exc ){
			return tokenMsgs[i];
		}
	}
	return null;
}

public void setData(Object data) {
	super.setData(data);
	setJavaType(JavaClassImpl.reflect("java.awt.Insets", JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
}

/**
 * Helper to return a well formed Java Initialization string for an Insets.
 */
public static String getJavaInitializationString(org.eclipse.draw2d.geometry.Insets insets) {
	return getJavaInitializationString(insets.top, insets.left, insets.bottom, insets.right);
}

/**
 * Helper to return a well formed Java Initialization string for a top, left, bottom, right
 */
public static String getJavaInitializationString(int top, int left, int bottom, int right){
	StringBuffer buffer = new StringBuffer();
	buffer.append("new java.awt.Insets("); //$NON-NLS-1$
	buffer.append(String.valueOf(top));
	buffer.append(',');
	buffer.append(String.valueOf(left));
	buffer.append(',');
	buffer.append(String.valueOf(bottom));
	buffer.append(',');
	buffer.append(String.valueOf(right));
	buffer.append(')');
	return buffer.toString();
}
}
