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
 *  $RCSfile: WidgetBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-24 14:31:25 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
 

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
	
	/**
	 * Return the property descriptors for this bean.
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// data
				super.createPropertyDescriptor(getBeanClass(),"data", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, WidgetMessages.getString("dataDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, WidgetMessages.getString("dataSD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// display
				super.createPropertyDescriptor(getBeanClass(),"display", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, WidgetMessages.getString("displayDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, WidgetMessages.getString("displaySD"), //$NON-NLS-1$
					EXPERT, Boolean.TRUE,
				}
				),
				// disposed
				super.createPropertyDescriptor(getBeanClass(),"disposed", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, WidgetMessages.getString("disposedDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, WidgetMessages.getString("disposedSD"), //$NON-NLS-1$
				}
				)
			};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}
	
}
