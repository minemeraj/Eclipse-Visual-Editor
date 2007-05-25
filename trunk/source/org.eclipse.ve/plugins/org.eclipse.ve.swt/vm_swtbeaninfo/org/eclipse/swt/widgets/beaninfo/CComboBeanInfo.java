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

import java.beans.*;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;


public class CComboBeanInfo extends IvjBeanInfo {

	public Class getBeanClass() {
		return org.eclipse.swt.custom.CCombo.class;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "border" , CComboMessages.getString("CComboBeanInfo.StyleBits.Border.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CComboMessages.getString("CComboBeanInfo.StyleBits.Border.Value.Border") , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "readOnly" , CComboMessages.getString("CComboBeanInfo.StyleBits.ReadOnly.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CComboMessages.getString("CComboBeanInfo.StyleBits.ReadOnly.Value.ReadOnly") , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "flat" , CComboMessages.getString("CComboBeanInfo.StyleBits.Flat.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CComboMessages.getString("CComboBeanInfo.StyleBits.Flat.Value.Flat") , "org.eclipse.swt.SWT.FLAT" , new Integer(SWT.FLAT) , //$NON-NLS-1$ //$NON-NLS-2$
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
				// editable
				super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CComboMessages.getString("editableDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CComboMessages.getString("editableSD"), //$NON-NLS-1$
				}
				),
				// text
				super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, CComboMessages.getString("textDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, CComboMessages.getString("textSD"), //$NON-NLS-1$
				}
				),
				// text limit
				super.createPropertyDescriptor(getBeanClass(),"textLimit", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CComboMessages.getString("textLimitDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CComboMessages.getString("textLimitSD"), //$NON-NLS-1$
				}
				),
				// visible item count
				super.createPropertyDescriptor(getBeanClass(),"visibleItemCount", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CComboMessages.getString("visibleItemCountDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CComboMessages.getString("visibleItemCountSD"), //$NON-NLS-1$
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
