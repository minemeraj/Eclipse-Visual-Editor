package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
				DISPLAYNAME, CoolBarMessages.getString("null.itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("null.itemCountSD"), //$NON-NLS-1$
			}
			),
			// itemOrder
			super.createPropertyDescriptor(getBeanClass(),"itemOrder", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("null.itemOrderDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("null.itemOrderSD"), //$NON-NLS-1$
			}
			),
			// itemSizes
			super.createPropertyDescriptor(getBeanClass(),"itemSizes", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("null.itemSizesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("null.itemSizesSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("null.itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("null.itemsSD"), //$NON-NLS-1$
			}
			),
			// locked
			super.createPropertyDescriptor(getBeanClass(),"locked", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("null.lockedDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("null.lockedSD"), //$NON-NLS-1$
			}
			),
			// wrapIndices
			super.createPropertyDescriptor(getBeanClass(),"wrapIndices", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolBarMessages.getString("null.wrapIndicesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolBarMessages.getString("null.wrapIndicesSD"), //$NON-NLS-1$
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
