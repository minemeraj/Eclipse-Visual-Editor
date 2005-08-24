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


public class ViewFormBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.ViewForm.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "border" , ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Border.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Border.Value.Border") , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "flat" , ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Flat.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ViewFormMessages.getString("ViewFormBeanInfo.StyleBits.Flat.Value.Flat") , "org.eclipse.swt.SWT.FLAT" , new Integer(SWT.FLAT) , //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
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
				// topCenterSeparate
				super.createPropertyDescriptor(getBeanClass(),"topCenterSeparate", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ViewFormMessages.getString("topCenterSeparateDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ViewFormMessages.getString("topCenterSeparateSD"), //$NON-NLS-1$
				}),
				// topLeft
				super.createPropertyDescriptor(getBeanClass(),"topLeft", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ViewFormMessages.getString("topLeftDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ViewFormMessages.getString("topLeftSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				}),
				// topRight
				super.createPropertyDescriptor(getBeanClass(),"topRight", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ViewFormMessages.getString("topRightDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ViewFormMessages.getString("topRightSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				}),
				// topCenter
				super.createPropertyDescriptor(getBeanClass(),"topCenter", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ViewFormMessages.getString("topCenterDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ViewFormMessages.getString("topCenterSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				}),
				// content
				super.createPropertyDescriptor(getBeanClass(),"content", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ViewFormMessages.getString("contentDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ViewFormMessages.getString("contentSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				}),
			};
			return aDescriptorList;
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}
	
	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}
}
