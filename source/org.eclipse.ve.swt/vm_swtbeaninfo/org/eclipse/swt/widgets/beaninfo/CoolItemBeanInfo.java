package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

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
				DISPLAYNAME, CoolItemMessages.getString("null.boundsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("null.boundsSD"), //$NON-NLS-1$
			}
			),
			// control
			super.createPropertyDescriptor(getBeanClass(),"control", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("null.controlDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("null.controlSD"), //$NON-NLS-1$
			}
			),
			// minimumSize
			super.createPropertyDescriptor(getBeanClass(),"minimumSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("null.minimumSizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("null.minimumSizeSD"), //$NON-NLS-1$
			}
			),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("null.parentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("null.parentSD"), //$NON-NLS-1$
			}
			),
			// preferredSize
			super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("null.preferredSizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("null.preferredSizeSD"), //$NON-NLS-1$
			}
			),
			// size
			super.createPropertyDescriptor(getBeanClass(),"size", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CoolItemMessages.getString("null.sizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CoolItemMessages.getString("null.sizeSD"), //$NON-NLS-1$
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
