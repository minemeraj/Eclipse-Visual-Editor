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

public class CLabelBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.CLabel.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "border" , CLabelMessages.getString("CLabelBeanInfo.StyleBits.border.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CLabelMessages.getString("CLabelBeanInfo.StyleBits.border.Value.SHADOW_IN") , "org.eclipse.swt.SWT.SHADOW_IN" , new Integer(SWT.SHADOW_IN) , //$NON-NLS-1$ //$NON-NLS-2$
					CLabelMessages.getString("CLabelBeanInfo.StyleBits.border.Value.SHADOW_OUT") , "org.eclipse.swt.SWT.SHADOW_OUT" , new Integer(SWT.SHADOW_OUT) , //$NON-NLS-1$ //$NON-NLS-2$
					CLabelMessages.getString("CLabelBeanInfo.StyleBits.border.Value.SHADOW_NONE") , "org.eclipse.swt.SWT.SHADOW_NONE" , new Integer(SWT.SHADOW_NONE) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "alignment" , CLabelMessages.getString("CLabelBeanInfo.StyleBits.alignment.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CLabelMessages.getString("CLabelBeanInfo.StyleBits.alignment.Value.LEFT") , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) , //$NON-NLS-1$ //$NON-NLS-2$
					CLabelMessages.getString("CLabelBeanInfo.StyleBits.alignment.Value.CENTER") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) , //$NON-NLS-1$ //$NON-NLS-2$
					CLabelMessages.getString("CLabelBeanInfo.StyleBits.alignment.Value.RIGHT") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) , //$NON-NLS-1$ //$NON-NLS-2$
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
				// image
				super.createPropertyDescriptor(getBeanClass(),"image", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CLabelMessages.getString("imageDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CLabelMessages.getString("imageSD"), //$NON-NLS-1$
				}
				),
				// text
				super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CLabelMessages.getString("textDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CLabelMessages.getString("textSD"), //$NON-NLS-1$
				}
				),
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
