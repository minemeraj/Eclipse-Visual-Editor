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
 *  $RCSfile: ProgressBarBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
 

/**
 * 
 * @since 1.0.0
 */
public class ProgressBarBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.ProgressBar.class;
	}

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "smooth" , "smooth" , Boolean.FALSE ,  new Object[] {
				    "SMOOTH" , "org.eclipse.swt.SWT.SMOOTH" , new Integer(SWT.SMOOTH)
				} },
				{ "indeterminate", "indeterminate", Boolean.FALSE, new Object[] {
					"INDETERMINATE" , "org.eclipse.swt.SWT.INDETERMINATE" ,  new Integer(SWT.INDETERMINATE)				
				} } ,
				{ "orientation" , "orientation" , Boolean.FALSE ,  new Object[] {
				    "HORIZONTAL" , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL),
					"VERTICAL" , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				
				} }
			}
		);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}
	
}
