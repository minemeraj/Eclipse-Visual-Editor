package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class MenuItemBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle MenuItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.menuitem");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.MenuItem.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// accelerator
			super.createPropertyDescriptor(getBeanClass(),"accelerator", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("null.acceleratorDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("null.acceleratorSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("null.enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("null.enabledSD"), //$NON-NLS-1$
			}
			),
			// menu
			super.createPropertyDescriptor(getBeanClass(),"menu", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("null.menuDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("null.menuSD"), //$NON-NLS-1$
			}
			),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("null.parentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("null.parentSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("null.selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("null.selectionSD"), //$NON-NLS-1$
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
