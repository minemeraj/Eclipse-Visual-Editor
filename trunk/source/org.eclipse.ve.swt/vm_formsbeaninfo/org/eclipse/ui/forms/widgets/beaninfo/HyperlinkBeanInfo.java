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
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.PropertyDescriptor;

import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.ui.forms.widgets.Hyperlink;


public class HyperlinkBeanInfo extends IvjBeanInfo {

	public Class getBeanClass() {
		return Hyperlink.class;
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
				super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, HyperlinkMessages.getString("textDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, HyperlinkMessages.getString("textSD"), //$NON-NLS-1$
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
		PropertyDescriptor[] newPDs = pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}
}
