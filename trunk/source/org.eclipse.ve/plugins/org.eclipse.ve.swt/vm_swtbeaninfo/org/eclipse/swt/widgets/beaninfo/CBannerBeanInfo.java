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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;

import org.eclipse.swt.SWT;

public class CBannerBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.CBanner.class;
	}

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			ArrayList descriptorList = new ArrayList();
			
			if(SWT.getVersion() >= 3100){
				descriptorList.add(
					// right minimum size
					super.createPropertyDescriptor(getBeanClass(), "rightMinimumSize", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CBannerMessages.getString("rightMinimumSizeDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CBannerMessages.getString("rightMinimumSizeSD"), //$NON-NLS-1$
					})	
				);
			}
			descriptorList.add(
				// simple
				super.createPropertyDescriptor(getBeanClass(), "simple", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("simpleDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("simpleSD"), //$NON-NLS-1$
				})
			);
			descriptorList.add(
				// left
				super.createPropertyDescriptor(getBeanClass(),"left", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("leftDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("leftSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				})
			);
			descriptorList.add(
				// right
				super.createPropertyDescriptor(getBeanClass(),"right", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("rightDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("rightSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				})
			);
			descriptorList.add(
				// bottom
				super.createPropertyDescriptor(getBeanClass(),"bottom", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("bottomDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("bottomSD"), //$NON-NLS-1$
					DESIGNTIMEPROPERTY, Boolean.FALSE,
				})
			);
			descriptorList.add(
				// right width
				super.createPropertyDescriptor(getBeanClass(), "rightWidth", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CBannerMessages.getString("rightWidthDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CBannerMessages.getString("rightWidthSD"), //$NON-NLS-1$
				})
			);
				
			PropertyDescriptor aDescriptorList[] = new PropertyDescriptor[descriptorList.size()];
			descriptorList.toArray(aDescriptorList);
			
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}

	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}

}
