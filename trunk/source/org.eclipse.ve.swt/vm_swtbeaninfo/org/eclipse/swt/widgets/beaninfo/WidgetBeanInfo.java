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
 *  $RCSfile: WidgetBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
 

/**
 * @since 1.0.0
 *
 */
public class WidgetBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Widget.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		return descriptor;
	}

	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				DisposeListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}
	
}
