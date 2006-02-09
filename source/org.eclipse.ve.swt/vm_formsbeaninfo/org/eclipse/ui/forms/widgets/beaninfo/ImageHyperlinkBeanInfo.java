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
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.swt.widgets.beaninfo.SweetHelper;
import org.eclipse.ui.forms.widgets.ImageHyperlink;


public class ImageHyperlinkBeanInfo extends IvjBeanInfo {

	public Class getBeanClass() {
		return ImageHyperlink.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
			new Object[] [] {
				{ "vertical alignment" , ImageHyperlinkMessages.getString("ImageHyperlinkBeanInfo.StyleBits.VerticalAlignment.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ImageHyperlinkMessages.getString("ImageHyperlinkBeanInfo.StyleBits.VerticalAlignment.Value.Top") , "org.eclipse.swt.SWT.TOP" , new Integer(SWT.TOP) , //$NON-NLS-1$ //$NON-NLS-2$
					ImageHyperlinkMessages.getString("ImageHyperlinkBeanInfo.StyleBits.VerticalAlignment.Value.Bottom") , "org.eclipse.swt.SWT.BOTTOM" , new Integer(SWT.BOTTOM) , //$NON-NLS-1$ //$NON-NLS-2$
					ImageHyperlinkMessages.getString("ImageHyperlinkBeanInfo.StyleBits.VerticalAlignment.Value.Centre") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) //$NON-NLS-1$ //$NON-NLS-2$					
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
				// text
				super.createPropertyDescriptor(getBeanClass(),"image", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ImageHyperlinkMessages.getString("imageDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ImageHyperlinkMessages.getString("imageSD"), //$NON-NLS-1$
				}
				),
				super.createPropertyDescriptor(getBeanClass(),"activeImage", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ImageHyperlinkMessages.getString("activeImageDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ImageHyperlinkMessages.getString("activeImageSD"), //$NON-NLS-1$
				}
				),
				super.createPropertyDescriptor(getBeanClass(),"hover", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, ImageHyperlinkMessages.getString("hoverImageDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, ImageHyperlinkMessages.getString("hoverImageSD"), //$NON-NLS-1$
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
