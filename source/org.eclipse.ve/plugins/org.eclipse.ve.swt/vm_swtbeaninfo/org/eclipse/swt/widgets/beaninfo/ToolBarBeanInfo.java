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

public class ToolBarBeanInfo extends IvjBeanInfo {

	private static java.util.ResourceBundle ToolBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.toolbar"); //$NON-NLS-1$

	/**
	 * Gets the bean class.
	 * 
	 * @return java.lang.Class
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.ToolBar.class;
	}

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "flat" , ToolBarMessages.getString("ToolBarBeanInfo.StyleBits.Flat.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ToolBarMessages.getString("ToolBarBeanInfo.StyleBits.Flat.Value.Flat") , "org.eclipse.swt.SWT.FLAT" , new Integer(SWT.FLAT) , //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
			// itemCount
					super.createPropertyDescriptor(getBeanClass(), "itemCount", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, ToolBarMessages.getString("itemCountDN"), //$NON-NLS-1$
									SHORTDESCRIPTION, ToolBarMessages.getString("itemCountSD"), //$NON-NLS-1$
							}),
					// rowCount
					super.createPropertyDescriptor(getBeanClass(), "rowCount", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, ToolBarMessages.getString("rowCountDN"), //$NON-NLS-1$
									SHORTDESCRIPTION, ToolBarMessages.getString("rowCountSD"), //$NON-NLS-1$
							}),};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		}
		;
		return null;
	}

	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = pds.clone();
		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { DESIGNTIMEPROPERTY, Boolean.FALSE,}); //$NON-NLS-1$
		return newPDs;
	}

}
