package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class ToolItemBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle ToolItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.toolitem");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.ToolItem.class;
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
				DISPLAYNAME, ToolItemMessages.getString("null.boundsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.boundsSD"), //$NON-NLS-1$
			}
			),
			// control
			super.createPropertyDescriptor(getBeanClass(),"control", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.controlDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.controlSD"), //$NON-NLS-1$
			}
			),
			// disabledImage
			super.createPropertyDescriptor(getBeanClass(),"disabledImage", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.disabledImageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.disabledImageSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.enabledSD"), //$NON-NLS-1$
			}
			),
			// hotImage
			super.createPropertyDescriptor(getBeanClass(),"hotImage", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.hotImageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.hotImageSD"), //$NON-NLS-1$
			}
			),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.parentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.parentSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.selectionSD"), //$NON-NLS-1$
			}
			),
			// toolTipText
			super.createPropertyDescriptor(getBeanClass(),"toolTipText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.toolTipTextDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.toolTipTextSD"), //$NON-NLS-1$
			}
			),
			// width
			super.createPropertyDescriptor(getBeanClass(),"width", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ToolItemMessages.getString("null.widthDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ToolItemMessages.getString("null.widthSD"), //$NON-NLS-1$
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
