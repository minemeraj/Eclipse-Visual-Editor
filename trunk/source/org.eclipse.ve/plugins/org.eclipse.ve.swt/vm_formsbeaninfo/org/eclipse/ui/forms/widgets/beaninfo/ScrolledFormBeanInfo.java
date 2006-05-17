/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ScrolledFormBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2006-05-17 20:15:54 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import org.eclipse.jem.beaninfo.vm.BaseBeanInfo;

import org.eclipse.ve.swt.common.SWTBeanInfoConstants;
 
/**
 * 
 * @since 1.0.0
 */
public class ScrolledFormBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return ScrolledForm.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		return createBeanDescriptor(getBeanClass(), new Object[] {
			BaseBeanInfo.REQUIRED_IMPLICIT_PROPERTIES, "body",		//$NON-NLS-1$
			SWTBeanInfoConstants.DEFAULT_LAYOUT, Boolean.FALSE
		}); 
	}
	
/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrolledFormMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrolledFormMessages.getString("textSD"), //$NON-NLS-1$
			}
			),		
			// image
			super.createPropertyDescriptor(getBeanClass(),"image", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrolledFormMessages.getString("imageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrolledFormMessages.getString("imageSD"), //$NON-NLS-1$
			}
			),					
			// background image
			super.createPropertyDescriptor(getBeanClass(),"backgroundImage", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrolledFormMessages.getString("backgroundImageDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrolledFormMessages.getString("backgroundImageSD"), //$NON-NLS-1$
			}		
			),
			// body
			super.createPropertyDescriptor(getBeanClass(),"body", new Object[] { //$NON-NLS-1$
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
