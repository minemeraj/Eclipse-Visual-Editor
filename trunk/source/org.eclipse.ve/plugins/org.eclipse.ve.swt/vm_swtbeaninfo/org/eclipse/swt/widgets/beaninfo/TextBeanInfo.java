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
 *  $RCSfile: TextBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TextBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Text.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "textAlignment" , "textAlignment" , Boolean.FALSE ,  new Object[] {
			    "CENTER" , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) ,
				"LEFT" , "org.eclipse.swt.SWT.LEFT" ,  new Integer(SWT.LEFT), 				
				"RIGHT" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) 
			} } ,
			{ "readOnly" , "readOnly", Boolean.FALSE , new Object[] {
				"READ_ONLY" , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					
			} },
			{ "lines" , "lines", Boolean.FALSE , new Object[] {
				"SINGLE" , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					
				"MULTI" , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				
			} },			
			{ "wrap" , "wrap", Boolean.FALSE , new Object[] {
				"WRAP" , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
			ModifyListenerEventSet.getEventSetDescriptor(getBeanClass()),
			SelectionListenerEventSet.getEventSetDescriptor(getBeanClass()),
			VerifyListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}
	
}
