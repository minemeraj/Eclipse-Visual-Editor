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


import java.beans.PropertyDescriptor;

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
		PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();
		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { DESIGNTIMEPROPERTY, Boolean.FALSE,});
		return newPDs;
	}

}