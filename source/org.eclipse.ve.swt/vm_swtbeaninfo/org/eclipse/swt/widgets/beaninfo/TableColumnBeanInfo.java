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

public class TableColumnBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle TableColumnMessages = java.util.ResourceBundle.getBundle("org.eclipse.swt.widgets.beaninfo.tablecolumn");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return org.eclipse.swt.widgets.TableColumn.class;
}

public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "alignment" , TableColumnMessages.getString("alignmentDN") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableColumnMessages.getString("StyleBits.Alignment.Value.Center") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) , //$NON-NLS-1$ //$NON-NLS-2$
				TableColumnMessages.getString("StyleBits.Alignment.Value.Left") , "org.eclipse.swt.SWT.LEFT" ,  new Integer(SWT.LEFT), 				 //$NON-NLS-1$ //$NON-NLS-2$
				TableColumnMessages.getString("StyleBits.Alignment.Value.Right") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT)  //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
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
