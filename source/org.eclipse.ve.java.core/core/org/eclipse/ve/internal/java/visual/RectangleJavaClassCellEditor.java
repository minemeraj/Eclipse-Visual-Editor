package org.eclipse.ve.internal.java.visual;
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
 *  $RCSfile: RectangleJavaClassCellEditor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:16:38 $ 
 */

import java.util.StringTokenizer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.geometry.Rectangle;
/**
 * Cell Editor for Rectangle Beans.
 */
public class RectangleJavaClassCellEditor extends DefaultJavaClassCellEditor implements IExecutableExtension {
	
	private String rectangleClassName;
	
public RectangleJavaClassCellEditor(Composite aComposite){
	super(aComposite);
}

/**
 * Returns the string for the value.
 */
protected String doGetString(Object value) {
	if (isInstance(value)) {
		return RectangleJavaClassLabelProvider.toString((IJavaInstance) value);
	} else
		return null;
}

protected String getJavaInitializationString(String rectString) {
	// want to make sure nicely formed (i.e. no extra spaces). This assumes the string is valid. This shouldn't be called if it isn't.
	StringTokenizer st = new StringTokenizer(rectString, ","); //$NON-NLS-1$
	StringBuffer sb = new StringBuffer(rectString.length());
	sb.append("new "); //$NON-NLS-1$
	sb.append(rectangleClassName);
	sb.append('(');
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
	
	String[] tokenMsgs = new String[] {JavaMessages.getString("CellEditor.Rectangle.XErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Rectangle.YErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Rectangle.WidthErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Rectangle.HeightErrorMsg_ERROR_")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	if (st.countTokens() != tokenMsgs.length)
		return JavaMessages.getString("CellEditor.Rectangle.ErrorMsg_ERROR_"); //$NON-NLS-1$
	
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
	setJavaType(JavaClassImpl.reflect(rectangleClassName, JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
}

/**
 * Helper to return a well formed Java Initialization string for an Rectangle.
 */
public static String getJavaInitializationString(Rectangle rect,String aRectangleClassName) {
	return getJavaInitializationString(rect.x, rect.y, rect.width, rect.height,aRectangleClassName);
}

/**
 * Helper to return a well formed Java Initialization string for an x, y, width, and height.
 */
public static String getJavaInitializationString(int x, int y, int width, int height,String aRectangleClassName){
	StringBuffer buffer = new StringBuffer();
	buffer.append("new "); //$NON-NLS-1$
	buffer.append(aRectangleClassName);
	buffer.append('(');
	buffer.append(String.valueOf(x));
	buffer.append(',');
	buffer.append(String.valueOf(y));
	buffer.append(',');	
	buffer.append(String.valueOf(width));
	buffer.append(',');
	buffer.append(String.valueOf(height));
	buffer.append(')');
	return buffer.toString();
}
/**
 * The rectangle class name is a contained in the initialization data to allow this class to be configurable
 */
public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
	if (initData instanceof String){
		rectangleClassName = (String)initData;
	}				
}

}
