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
 *  $Revision: 1.1 $  $Date: 2004-04-26 16:43:44 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ProgressBar;
 

/**
 * 
 * @since 1.0.0
 */
public class ProgressBarBeanInfo extends SimpleBeanInfo {

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(ProgressBar.class);
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
