/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GroupBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:52:53 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

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
			{ "noRadioGroup" , GroupMessages.getString("GroupBeanInfo.StyleBits.NoRadioGroup.Name") , Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
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

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// clientArea
			super.createPropertyDescriptor(getBeanClass(),"clientArea", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, GroupMessages.getString("clientAreaDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, GroupMessages.getString("clientAreaSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, GroupMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, GroupMessages.getString("textSD"), //$NON-NLS-1$
			}
			),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
	
}
