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
 *  $RCSfile: ControlBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2004-06-03 14:45:34 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ControlBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Control.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "border" , ControlMessages.getString("ControlBeanInfo.StyleBits.Border.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ControlMessages.getString("ControlBeanInfo.StyleBits.Border.Value.Border") , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } 
		}
	);
	return descriptor;
}

/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
		ControlListenerEventSet.getEventSetDescriptor(getBeanClass()),
		FocusListenerEventSet.getEventSetDescriptor(getBeanClass()),
		HelpListenerEventSet.getEventSetDescriptor(getBeanClass()),
		KeyListenerEventSet.getEventSetDescriptor(getBeanClass()),
		MouseListenerEventSet.getEventSetDescriptor(getBeanClass()),
		MouseMoveListenerEventSet.getEventSetDescriptor(getBeanClass()),
		MouseTrackListenerEventSet.getEventSetDescriptor(getBeanClass()),
		PaintListenerEventSet.getEventSetDescriptor(getBeanClass()),
		TraverseListenerEventSet.getEventSetDescriptor(getBeanClass()),
	};
}
	
}
