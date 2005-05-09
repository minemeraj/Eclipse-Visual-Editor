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
/*
 *  $RCSfile: SpinnerBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-09 15:38:08 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;


/**
 * @since 1.1
 */
public class SpinnerBeanInfo extends IvjBeanInfo {
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Spinner.class;
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "readOnly" , SpinnerMessages.getString("SpinnerBeanInfo.StyleBits.ReadOnly.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    SpinnerMessages.getString("SpinnerBeanInfo.StyleBits.ReadOnly.Value.ReadOnly") , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY) , //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		return descriptor;
	}	
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}
	
	/**
	 * Return the property descriptors for this bean.
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor aDescriptorList[] = {
				// digits
				super.createPropertyDescriptor(getBeanClass(),"digits", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SpinnerMessages.getString("digitsDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SpinnerMessages.getString("digitsSD"), //$NON-NLS-1$
				}
				),
				// increment
				super.createPropertyDescriptor(getBeanClass(),"increment", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SpinnerMessages.getString("incrementDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SpinnerMessages.getString("incrementSD"), //$NON-NLS-1$
				}
				),
				// maximum
				super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SpinnerMessages.getString("maximumDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SpinnerMessages.getString("maximumSD"), //$NON-NLS-1$
				}
				),
				// minimum
				super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SpinnerMessages.getString("minimumDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SpinnerMessages.getString("minimumSD"), //$NON-NLS-1$
				}
				),
				// pageIncrement
				super.createPropertyDescriptor(getBeanClass(),"pageIncrement", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SpinnerMessages.getString("pageIncrementDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SpinnerMessages.getString("pageIncrementSD"), //$NON-NLS-1$
				}
				),
				// selection
				super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, SpinnerMessages.getString("selectionDN"), //$NON-NLS-1$
					SHORTDESCRIPTION, SpinnerMessages.getString("selectionSD"), //$NON-NLS-1$
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

		// hide layout property
		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
			DESIGNTIMEPROPERTY, Boolean.FALSE,
		}
		);

		return newPDs;
	}
}
