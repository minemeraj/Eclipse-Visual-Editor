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
 *  $Revision: 1.4 $  $Date: 2004-06-03 14:45:34 $ 
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
			{ "noRadioGroup" , GroupMessages.getString("GroupBeanInfo.StyleBits.NoRadioGroup.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				GroupMessages.getString("GroupBeanInfo.StyleBits.NoRadioGroup.Value.NoRadioGroup") , "org.eclipse.swt.SWT.NO_RADIO_GROUP" , new Integer(SWT.NO_RADIO_GROUP)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "shadow" , GroupMessages.getString("GroupBeanInfo.StyleBits.Shadow.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				GroupMessages.getString("GroupBeanInfo.StyleBits.Shadow.Value.In") , "org.eclipse.swt.SWT.SHADOW_IN" , new Integer(SWT.SHADOW_IN), //$NON-NLS-1$ //$NON-NLS-2$
				GroupMessages.getString("GroupBeanInfo.StyleBits.Shadow.Value.Out") , "org.eclipse.swt.SWT.SHADOW_OUT" , new Integer(SWT.SHADOW_OUT), //$NON-NLS-1$ //$NON-NLS-2$
				GroupMessages.getString("GroupBeanInfo.StyleBits.Shadow.Value.EtchedIn") , "org.eclipse.swt.SWT.SHADOW_ETCHED_IN" , new Integer(SWT.SHADOW_ETCHED_IN), //$NON-NLS-1$ //$NON-NLS-2$
				GroupMessages.getString("GroupBeanInfo.StyleBits.Shadow.Value.EtchedOut") , "org.eclipse.swt.SWT.SHADOW_ETCHED_OUT" , new Integer(SWT.SHADOW_ETCHED_OUT), //$NON-NLS-1$ //$NON-NLS-2$
				GroupMessages.getString("GroupBeanInfo.StyleBits.Shadow.Value.None") , "org.eclipse.swt.SWT.SHADOW_NONE" , new Integer(SWT.SHADOW_NONE) //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
