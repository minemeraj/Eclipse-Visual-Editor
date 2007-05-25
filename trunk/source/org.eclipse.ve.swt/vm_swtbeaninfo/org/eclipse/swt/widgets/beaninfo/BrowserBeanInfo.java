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
 *  $RCSfile: BrowserBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2007-05-25 04:20:17 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
 
/**
 * 
 * @since 1.0.0
 */
public class BrowserBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.browser.Browser.class;
	}
	
/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
			CloseWindowListenerEventSet.getEventSetDescriptor(getBeanClass()),
			LocationListenerEventSet.getEventSetDescriptor(getBeanClass()),
			OpenWindowListenerEventSet.getEventSetDescriptor(getBeanClass()),
			ProgressListenerEventSet.getEventSetDescriptor(getBeanClass()),
			StatusTextListenerEventSet.getEventSetDescriptor(getBeanClass()),
			TitleListenerEventSet.getEventSetDescriptor(getBeanClass()),
			VisibilityWindowListenerEventSet.getEventSetDescriptor(getBeanClass()),
	};
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// url property descriptor is special, as its set method returns a value.
			super.createPropertyDescriptor(getBeanClass(),"url", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, BrowserMessages.getString("urlDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, BrowserMessages.getString("urlSD"), //$NON-NLS-1$
			})
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
	PropertyDescriptor[] newPDs = pds.clone();
	
	replacePropertyDescriptor(newPDs, "layout", null, new Object[] {  //$NON-NLS-1$
		DESIGNTIMEPROPERTY, Boolean.FALSE,
		}
	);

	return newPDs;
}
}
