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
 *  $RCSfile: ScrollableBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ScrollableBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Scrollable.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {							
			{ "horizontalScroll" , "horizontalScroll" , Boolean.FALSE , new Object[] {
			    "H_SCROLL" , "org.eclipse.swt.SWT.H_SCROLL" , new Integer(SWT.H_SCROLL)				
			} } ,
			{ "verticalScroll" , "verticalScroll" , Boolean.FALSE , new Object[] {
				"V_SCROLL" , "org.eclipse.swt.SWT.V_SCROLL" , new Integer(SWT.V_SCROLL)				
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
