package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class MenuBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle MenuMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.menu");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.Menu.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// defaultItem
			super.createPropertyDescriptor(getBeanClass(),"defaultItem", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.defaultItemDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.defaultItemSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.enabledSD"), //$NON-NLS-1$
			}
			),
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.itemCountSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.itemsSD"), //$NON-NLS-1$
			}
			),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.parentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.parentSD"), //$NON-NLS-1$
			}
			),
			// parentItem
			super.createPropertyDescriptor(getBeanClass(),"parentItem", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.parentItemDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.parentItemSD"), //$NON-NLS-1$
			}
			),
			// parentMenu
			super.createPropertyDescriptor(getBeanClass(),"parentMenu", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.parentMenuDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.parentMenuSD"), //$NON-NLS-1$
			}
			),
			// shell
			super.createPropertyDescriptor(getBeanClass(),"shell", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.shellDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.shellSD"), //$NON-NLS-1$
			}
			),
			// visible
			super.createPropertyDescriptor(getBeanClass(),"visible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("null.visibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("null.visibleSD"), //$NON-NLS-1$
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
