package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class ToolBarBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle ToolBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.toolbar");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.ToolBar.class;
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
				DISPLAYNAME, ToolBarMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolBarMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolBarMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolBarMessages.getString("itemsSD"), //$NON-NLS-1$
			}
			),
			// rowCount
			super.createPropertyDescriptor(getBeanClass(),"rowCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolBarMessages.getString("rowCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolBarMessages.getString("rowCountSD"), //$NON-NLS-1$
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
