package org.eclipse.swt.widgets.beaninfo;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2004 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

public class StackLayoutBeanInfo extends IvjBeanInfo {


/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.custom.StackLayout.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

}
