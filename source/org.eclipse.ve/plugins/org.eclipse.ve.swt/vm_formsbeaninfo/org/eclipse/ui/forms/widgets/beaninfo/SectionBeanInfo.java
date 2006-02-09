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
/*
 *  $RCSfile: SectionBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2006-02-09 14:28:18 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.swt.widgets.beaninfo.SweetHelper;
import org.eclipse.ui.forms.widgets.Section;

public class SectionBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return Section.class;
	}	
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
			new Object[] [] {
				{ "description" , SectionMessages.getString("SectionBeanInfo.StyleBits.Description.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					SectionMessages.getString("SectionBeanInfo.StyleBits.Description.Value") , "org.eclipse.ui.forms.widgets.Section.DESCRIPTION" , new Integer(Section.DESCRIPTION) //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
			);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;		
	}
	
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// accessible
				super.createPropertyDescriptor(getBeanClass(),"separatorControl", new Object[] { //$NON-NLS-1$
					HIDDEN, Boolean.TRUE, // The "separatorControl" property should not be on the property sheet
				}
				),	
				super.createPropertyDescriptor(getBeanClass(),"descriptionControl", new Object[] { //$NON-NLS-1$
					HIDDEN, Boolean.TRUE, // The "descriptionControl" property should not be on the property sheet
				}
				),		
				super.createPropertyDescriptor(getBeanClass(),"description", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SectionMessages.getString("descriptionDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SectionMessages.getString("descriptionSD"), //$NON-NLS-1$
				}
				),
				super.createPropertyDescriptor(getBeanClass(),"titleBarBorderColor", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SectionMessages.getString("titleBarBorderColorDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SectionMessages.getString("titleBarBorderColorSD"), //$NON-NLS-1$
				}
				),
				super.createPropertyDescriptor(getBeanClass(),"titleBarBackground", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SectionMessages.getString("titleBarBackgroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SectionMessages.getString("titleBarBackgroundSD"), //$NON-NLS-1$
				}
				),
				super.createPropertyDescriptor(getBeanClass(),"titleBarBackground", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SectionMessages.getString("titleBarBackgroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SectionMessages.getString("titleBarBackgroundSD"), //$NON-NLS-1$
				}
				),
				super.createPropertyDescriptor(getBeanClass(),"titleBarGradientBackground", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SectionMessages.getString("titleBarGradientBackgroundDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SectionMessages.getString("titleBarGradientBackgroundSD"), //$NON-NLS-1$
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
