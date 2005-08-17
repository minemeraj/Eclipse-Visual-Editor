/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
				DISPLAYNAME, MenuItemMessages.getString("acceleratorDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("acceleratorSD"), //$NON-NLS-1$
			}
			),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("enabledSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("selectionSD"), //$NON-NLS-1$
			}
			),
			// menu
			super.createPropertyDescriptor(getBeanClass(),"menu", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, MenuItemMessages.getString("menuDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, MenuItemMessages.getString("menuSD"), //$NON-NLS-1$
			}
			),

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
			{ "style" , MenuItemMessages.getString("MenuItemBeanInfo.StyleBits.Style.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    MenuItemMessages.getString("MenuItemBeanInfo.StyleBits.Style.Value.Push") , "org.eclipse.swt.SWT.PUSH" , new Integer(SWT.PUSH) , //$NON-NLS-1$ //$NON-NLS-2$
			    MenuItemMessages.getString("MenuItemBeanInfo.StyleBits.Style.Value.Check") , "org.eclipse.swt.SWT.CHECK" ,  new Integer(SWT.CHECK), 	 //$NON-NLS-1$ //$NON-NLS-2$
			    MenuItemMessages.getString("MenuItemBeanInfo.StyleBits.Style.Value.Radio") , "org.eclipse.swt.SWT.RADIO" , new Integer(SWT.RADIO) , //$NON-NLS-1$ //$NON-NLS-2$
			    MenuItemMessages.getString("MenuItemBeanInfo.StyleBits.Style.Value.Separator") , "org.eclipse.swt.SWT.SEPARATOR" , new Integer(SWT.SEPARATOR) , //$NON-NLS-1$ //$NON-NLS-2$
			    MenuItemMessages.getString("MenuItemBeanInfo.StyleBits.Style.Value.Cascade") , "org.eclipse.swt.SWT.CASCADE" , new Integer(SWT.CASCADE)	 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

}
