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
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;

public class CoolItemBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle CoolItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.coolitem");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.CoolItem.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// bounds
			super.createPropertyDescriptor(getBeanClass(),"bounds", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("boundsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("boundsSD"), //$NON-NLS-1$
			}
			),
			// control
			super.createPropertyDescriptor(getBeanClass(),"control", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("controlDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("controlSD"), //$NON-NLS-1$
			}
			),
			// minimumSize
			super.createPropertyDescriptor(getBeanClass(),"minimumSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("minimumSizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("minimumSizeSD"), //$NON-NLS-1$
			}
			),
			// preferredSize
			super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("preferredSizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("preferredSizeSD"), //$NON-NLS-1$
			}
			),
			// size
			super.createPropertyDescriptor(getBeanClass(),"size", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("sizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("sizeSD"), //$NON-NLS-1$
			}
			),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "style" , CoolItemMessages.getString("CoolItemBeanInfo.StyleBits.Style.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					CoolItemMessages.getString("CoolItemBeanInfo.StyleBits.Style.Value.Dropdown") , "org.eclipse.swt.SWT.DROP_DOWN" , new Integer(SWT.DROP_DOWN)	 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

}
