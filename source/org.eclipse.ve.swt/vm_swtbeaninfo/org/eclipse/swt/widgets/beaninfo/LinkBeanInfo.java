/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LinkBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2005-07-13 14:27:05 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;

/**
 * @since 1.1
 */
public class LinkBeanInfo extends IvjBeanInfo {
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Link.class;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}

	/**
	 * Return the property descriptors for this bean.
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// text
				super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, LinkMessages.getString("textDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, LinkMessages.getString("textSD"), //$NON-NLS-1$
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
