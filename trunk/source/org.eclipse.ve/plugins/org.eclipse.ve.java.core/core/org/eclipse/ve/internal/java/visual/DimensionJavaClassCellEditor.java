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
 *  $RCSfile: DimensionJavaClassCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-15 23:23:54 $ 
 */

import java.util.StringTokenizer;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.core.*;
/**
 * Cell Editor for Dimension Beans.
 */
public class DimensionJavaClassCellEditor extends DefaultJavaClassCellEditor implements IExecutableExtension {
	
	private String dimensionClassName;
	
public DimensionJavaClassCellEditor(Composite aComposite){
	super(aComposite);
}

/**
 * Returns the string for the value.
 */
protected String doGetString(Object value) {
	if (isInstance(value)) {
		return DimensionJavaClassLabelProvider.toString((IJavaInstance) value);
	} else
		return null;
}

protected String getJavaInitializationString(String dimString) {
	// want to make sure nicely formed (i.e. no extra spaces). This assumes the string is valid. This shouldn't be called if it isn't.
	StringTokenizer st = new StringTokenizer(dimString, ","); //$NON-NLS-1$
	StringBuffer sb = new StringBuffer(dimString.length());
	sb.append("new ");	//$NON-NLS-1$
	sb.append(dimensionClassName);
	sb.append('('); //$NON-NLS-1$
	sb.append(st.nextToken().trim());
	while (st.hasMoreTokens()) {
		sb.append(',');
		sb.append(st.nextToken().trim());
	}
	sb.append(')');
	return (sb.toString());
}

/**
 * Parse the text string to see if it is a valid dimension, e.g. width,height
 */
protected String isCorrectString(String text) {
	StringTokenizer st = new StringTokenizer(text, ","); //$NON-NLS-1$
	
	String[] tokenMsgs = new String[] {JavaMessages.getString("CellEditor.Dimension.WidthErrorMsg_ERROR_"), JavaMessages.getString("CellEditor.Dimension.HeightErrorMsg_ERROR_")}; //$NON-NLS-1$ //$NON-NLS-2$
	if (st.countTokens() != tokenMsgs.length)
		return JavaMessages.getString("CellEditor.Dimension.ErrorMsg_ERROR_"); //$NON-NLS-1$
	
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
	setJavaType(JavaRefFactory.eINSTANCE.reflectType(dimensionClassName, JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
}

/**
 * Helper to return a well formed Java Initialization string for an width and height
 */
public static String getJavaInitializationString(int width, int height, String aDimensionClassName){
	StringBuffer buffer = new StringBuffer();
	buffer.append("new "); //$NON-NLS-1$
	buffer.append(aDimensionClassName);
	buffer.append('(');
	buffer.append(String.valueOf(width));
	buffer.append(',');
	buffer.append(String.valueOf(height));
	buffer.append(')');
	return buffer.toString();
}

/**
 * The dimension class name is a contained in the initialization data to allow this class to be configurable
 */
public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
	if (initData instanceof String){
		dimensionClassName = (String)initData;
	}				
}
}
