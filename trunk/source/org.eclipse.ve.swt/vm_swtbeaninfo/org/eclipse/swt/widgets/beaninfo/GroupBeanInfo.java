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
 *  $RCSfile: GroupBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class GroupBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Group.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {			
			{ "noRadioGroup" , "noRadioGroup" , Boolean.FALSE , new Object[] {
				"NO_RADIO_GROUP" , "org.eclipse.swt.SWT.NO_RADIO_GROUP" , new Integer(SWT.NO_RADIO_GROUP)				
			} },
			{ "shadow" , "shadow" , Boolean.FALSE , new Object[] {
				"IN" , "org.eclipse.swt.SWT.SHADOW_IN" , new Integer(SWT.SHADOW_IN),
				"OUT" , "org.eclipse.swt.SWT.SHADOW_OUT" , new Integer(SWT.SHADOW_OUT),
				"ETCHED IN" , "org.eclipse.swt.SWT.SHADOW_ETCHED_IN" , new Integer(SWT.SHADOW_ETCHED_IN),
				"ETCHED OUT" , "org.eclipse.swt.SWT.SHADOW_ETCHED_OUT" , new Integer(SWT.SHADOW_ETCHED_OUT),
				"NONE" , "org.eclipse.swt.SWT.SHADOW_NONE" , new Integer(SWT.SHADOW_NONE)
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
