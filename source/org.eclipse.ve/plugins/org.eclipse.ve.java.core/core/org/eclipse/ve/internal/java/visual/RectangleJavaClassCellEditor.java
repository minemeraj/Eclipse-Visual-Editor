/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: RectangleJavaClassCellEditor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-21 22:53:48 $ 
 */

import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.core.*;
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
	
	String[] tokenMsgs = new String[] {JavaMessages.CellEditor_Rectangle_XErrorMsg_ERROR_, JavaMessages.CellEditor_Rectangle_YErrorMsg_ERROR_, JavaMessages.CellEditor_Rectangle_WidthErrorMsg_ERROR_, JavaMessages.CellEditor_Rectangle_HeightErrorMsg_ERROR_}; 
	if (st.countTokens() != tokenMsgs.length)
		return JavaMessages.CellEditor_Rectangle_ErrorMsg_ERROR_; 
	
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
	setJavaType(JavaRefFactory.eINSTANCE.reflectType(rectangleClassName, JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
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
