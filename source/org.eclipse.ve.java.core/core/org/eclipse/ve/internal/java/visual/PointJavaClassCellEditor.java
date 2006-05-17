/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $RCSfile: PointJavaClassCellEditor.java,v $
 *  $Revision: 1.11 $  $Date: 2006-05-17 20:14:53 $ 
 */


import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.core.*;

import com.ibm.icu.util.StringTokenizer;
/**
 * Cell Editor for Point instances
 * The actual point class itself is provided as an initialization string so the same class can be used for 
 * different toolkits, i.e. AWT or SWT
 */
public class PointJavaClassCellEditor extends DefaultJavaClassCellEditor implements IExecutableExtension, IJavaCellEditor2 {
	
	private String pointClassName;
	
public PointJavaClassCellEditor(Composite aComposite){
	super(aComposite);
}

/**
 * Returns the string for the value.
 */
protected String doGetString(Object value) {
	if (isInstance(value)) {
		return PointJavaClassLabelProvider.toString((IJavaInstance) value);
	} else
		return null;
}

protected String getJavaInitializationString(String dimString) {
	// want to make sure nicely formed (i.e. no extra spaces). This assumes the string is valid. This shouldn't be called if it isn't.
	StringTokenizer st = new StringTokenizer(dimString, ","); //$NON-NLS-1$
	StringBuffer sb = new StringBuffer(dimString.length());
	sb.append("new "); //$NON-NLS-1$
	sb.append(pointClassName);
	sb.append('(');
	sb.append(st.nextToken().trim());
	while (st.hasMoreTokens()) {
		sb.append(',');
		sb.append(st.nextToken().trim());
	}
	sb.append(')');
	return (sb.toString());
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.core.DefaultJavaClassCellEditor#getJavaAllocation(java.lang.String)
 */
protected JavaAllocation getJavaAllocation(String value) {
	return BeanPropertyDescriptorAdapter.createAllocation(getJavaInitializationString(value), fEditDomain);
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.core.IJavaCellEditor2#getJavaAllocation()
 */
public JavaAllocation getJavaAllocation() {
	return BeanPropertyDescriptorAdapter.createAllocation(getJavaInitializationString(), fEditDomain);
}

/**
 * Parse the text string to see if it is a valid dimension, e.g. width,height
 */
protected String isCorrectString(String text) {
	StringTokenizer st = new StringTokenizer(text, ","); //$NON-NLS-1$
	
	String[] tokenMsgs = new String[] {JavaMessages.CellEditor_Point_XErrorMsg_ERROR_, JavaMessages.CellEditor_Point_YErrorMsg_ERROR_}; 
	if (st.countTokens() != tokenMsgs.length)
		return JavaMessages.CellEditor_Point_ErrorMsg_ERROR_; 
	
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
	setJavaType(JavaRefFactory.eINSTANCE.reflectType(pointClassName, JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
}

/**
 * Helper to return a well formed Java Initialization string for an width and height
 */
public static String getJavaInitializationString(int x, int y, String aPointClassName){
	StringBuffer buffer = new StringBuffer();
	buffer.append("new "); //$NON-NLS-1$
	buffer.append(aPointClassName);
	buffer.append('('); 
	buffer.append(String.valueOf(x));
	buffer.append(',');
	buffer.append(String.valueOf(y));
	buffer.append(')');
	return buffer.toString();
}

/**
 * Return initialization as a parse tree allocation.
 * @param x
 * @param y
 * @param aPointClassName
 * @return
 * 
 * @since 1.2.0
 */
public static ParseTreeAllocation getJavaAllocation(int x, int y, String aPointClassName) {
	PTClassInstanceCreation newclass = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(aPointClassName, null);
	newclass.getArguments().add(InstantiationFactory.eINSTANCE.createPTNumberLiteral(Integer.toString(x)));
	newclass.getArguments().add(InstantiationFactory.eINSTANCE.createPTNumberLiteral(Integer.toString(y)));
	return InstantiationFactory.eINSTANCE.createParseTreeAllocation(newclass);
}

/**
 * The point class name is a contained in the initialization data to allow this class to be configurable
 */
public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
	if (initData instanceof String){
		pointClassName = (String)initData;
	}				
}
}

