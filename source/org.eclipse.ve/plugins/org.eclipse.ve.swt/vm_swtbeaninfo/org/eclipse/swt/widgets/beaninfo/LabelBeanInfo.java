/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LabelBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class LabelBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Label.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "orientation" , "orientation" , Boolean.FALSE ,  new Object[] {
			    "HORIZONTAL" , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL) ,
				"VERTICAL" , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				
			} },
			{ "textAlignment" , "textAlignment", Boolean.FALSE , new Object[] {
				"LEFT" , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,					
				"RIGHT" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) ,
				"CENTER" , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER)				
			} },
			{ "separator" , "separator", Boolean.FALSE , new Object[] {
				"SEPARATOR" , "org.eclipse.swt.SWT.SEPARATOR" , new Integer(SWT.SEPARATOR)					
			} },
			{ "shadow" , "shadow" , Boolean.FALSE , new Object[] {
				"IN" , "org.eclipse.swt.SWT.SHADOW_IN" , new Integer(SWT.SHADOW_IN),
				"OUT" , "org.eclipse.swt.SWT.SHADOW_OUT" , new Integer(SWT.SHADOW_OUT),
				"NONE" , "org.eclipse.swt.SWT.SHADOW_NONE" , new Integer(SWT.SHADOW_NONE)
			} },
			{ "wrap" , "wrap", Boolean.FALSE , new Object[] {
				"WRAP" , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
