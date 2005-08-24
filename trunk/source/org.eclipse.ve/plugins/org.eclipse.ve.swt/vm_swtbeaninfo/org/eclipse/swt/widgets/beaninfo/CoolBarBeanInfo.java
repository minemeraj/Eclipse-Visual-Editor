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

import java.beans.*;

public class CoolBarBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle CoolBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.coolbar");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.CoolBar.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// itemOrder
			super.createPropertyDescriptor(getBeanClass(),"itemOrder", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("itemOrderDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("itemOrderSD"), //$NON-NLS-1$
			}
			),
			// itemSizes
			super.createPropertyDescriptor(getBeanClass(),"itemSizes", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("itemSizesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("itemSizesSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("itemsSD"),//$NON-NLS-1$ 
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// locked
			super.createPropertyDescriptor(getBeanClass(),"locked", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("lockedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("lockedSD"), //$NON-NLS-1$
			}
			),
			// wrapIndices
			super.createPropertyDescriptor(getBeanClass(),"wrapIndices", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("wrapIndicesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("wrapIndicesSD"), //$NON-NLS-1$
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
