package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class TableColumnBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle TableColumnMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.tablecolumn");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.TableColumn.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// alignment
			super.createPropertyDescriptor(getBeanClass(),"alignment", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableColumnMessages.getString("alignmentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableColumnMessages.getString("alignmentSD"), //$NON-NLS-1$
			}
			),
			// resizable
			super.createPropertyDescriptor(getBeanClass(),"resizable", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableColumnMessages.getString("resizableDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableColumnMessages.getString("resizableSD"), //$NON-NLS-1$
			}
			),
			// width
			super.createPropertyDescriptor(getBeanClass(),"width", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TableColumnMessages.getString("widthDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TableColumnMessages.getString("widthSD"), //$NON-NLS-1$
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
