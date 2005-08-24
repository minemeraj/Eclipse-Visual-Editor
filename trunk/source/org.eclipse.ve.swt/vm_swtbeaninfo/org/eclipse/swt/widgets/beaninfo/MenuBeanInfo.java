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

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;

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
				DISPLAYNAME, MenuMessages.getString("defaultItemDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("defaultItemSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("enabledSD"), //$NON-NLS-1$
			}
			),
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// visible
			super.createPropertyDescriptor(getBeanClass(),"visible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("visibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("visibleSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuMessages.getString("itemsSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "style" , MenuMessages.getString("MenuBeanInfo.StyleBits.Style.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					MenuMessages.getString("MenuBeanInfo.StyleBits.Style.Value.Bar") , "org.eclipse.swt.SWT.BAR" , new Integer(SWT.BAR),	 //$NON-NLS-1$ //$NON-NLS-2$
					MenuMessages.getString("MenuBeanInfo.StyleBits.Style.Value.Popup") , "org.eclipse.swt.SWT.POP_UP" , new Integer(SWT.POP_UP),	 //$NON-NLS-1$ //$NON-NLS-2$
					MenuMessages.getString("MenuBeanInfo.StyleBits.Style.Value.Dropdown") , "org.eclipse.swt.SWT.DROP_DOWN" , new Integer(SWT.DROP_DOWN)	 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

}
