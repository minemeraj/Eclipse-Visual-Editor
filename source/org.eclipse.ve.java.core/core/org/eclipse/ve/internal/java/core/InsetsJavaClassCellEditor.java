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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: InsetsJavaClassCellEditor.java,v $
 *  $Revision: 1.12 $  $Date: 2006-05-17 20:14:52 $ 
 */

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaRefFactory;

import com.ibm.icu.util.StringTokenizer;
/**
 * Cell Editor for Insets Beans.
 */
public class InsetsJavaClassCellEditor extends DefaultJavaClassCellEditor implements IJavaCellEditor2 {
	
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
 * Parse the text string to see if it is a valid rectangle, e.g. x,y,width,height
 */
protected String isCorrectString(String text) {
	StringTokenizer st = new StringTokenizer(text, ","); //$NON-NLS-1$
	
	String[] tokenMsgs = new String[] {JavaMessages.CellEditor_Insets_TopErrorMsg_ERROR_, JavaMessages.CellEditor_Insets_LeftErrorMsg_ERROR_, JavaMessages.CellEditor_Insets_BottomErrorMsg_ERROR_, JavaMessages.CellEditor_Insets_RightErrorMsg_ERROR_}; 
	if (st.countTokens() != tokenMsgs.length)
		return JavaMessages.CellEditor_Insets_ErrorMsg_ERROR_; 
	
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
	setJavaType(JavaRefFactory.eINSTANCE.reflectType("java.awt.Insets", JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
}

/**
 * Helper to return a well formed Java Initialization string for an Insets.
 */
public static String getJavaInitializationString(org.eclipse.draw2d.geometry.Insets insets) {
	return getJavaInitializationString(insets.top, insets.left, insets.bottom, insets.right);
}

/**
 * Return initialization as a parse tree allocation.
 * @param top
 * @param left
 * @param bottom
 * @param right
 * @param aRectangleClassName
 * @return
 * 
 * @since 1.2.0
 */
public static ParseTreeAllocation getJavaAllocation(int top, int left, int bottom, int right) {
	PTClassInstanceCreation newclass = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation("java.awt.Insets", null); //$NON-NLS-1$
	newclass.getArguments().add(InstantiationFactory.eINSTANCE.createPTNumberLiteral(Integer.toString(top)));
	newclass.getArguments().add(InstantiationFactory.eINSTANCE.createPTNumberLiteral(Integer.toString(left)));
	newclass.getArguments().add(InstantiationFactory.eINSTANCE.createPTNumberLiteral(Integer.toString(bottom)));
	newclass.getArguments().add(InstantiationFactory.eINSTANCE.createPTNumberLiteral(Integer.toString(right)));
	return InstantiationFactory.eINSTANCE.createParseTreeAllocation(newclass);	
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
