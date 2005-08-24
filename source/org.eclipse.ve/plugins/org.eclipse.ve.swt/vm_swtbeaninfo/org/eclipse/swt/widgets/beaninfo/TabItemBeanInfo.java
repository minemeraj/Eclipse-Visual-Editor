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

public class TabItemBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle TabItemMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.tabitem");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.TabItem.class;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// control
			super.createPropertyDescriptor(getBeanClass(),"control", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabItemMessages.getString("controlDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabItemMessages.getString("controlSD"), //$NON-NLS-1$
			}
			),
			// toolTipText
			super.createPropertyDescriptor(getBeanClass(),"toolTipText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabItemMessages.getString("toolTipTextDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabItemMessages.getString("toolTipTextSD"), //$NON-NLS-1$
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
