package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class ItemBeanInfoX extends IvjBeanInfo {

private static java.util.ResourceBundle ItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.item");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.Item.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// image
			super.createPropertyDescriptor(getBeanClass(),"image", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ItemMessages.getString("imageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ItemMessages.getString("imageSD"), //$NON-NLS-1$
			}
			),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ItemMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ItemMessages.getString("textSD"), //$NON-NLS-1$
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
