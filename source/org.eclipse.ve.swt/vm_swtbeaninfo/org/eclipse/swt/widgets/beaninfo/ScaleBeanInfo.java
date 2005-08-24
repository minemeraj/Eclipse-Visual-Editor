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
 *  $RCSfile: ScaleBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:53 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ScaleBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Scale.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "orientation" , ScaleMessages.getString("ScaleBeanInfo.StyleBits.Orientation.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ScaleMessages.getString("ScaleBeanInfo.StyleBits.Orientation.Value.Horizontal") , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL) , //$NON-NLS-1$ //$NON-NLS-2$
				ScaleMessages.getString("ScaleBeanInfo.StyleBits.Orientation.Value.Vertical") , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
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
			// increment
			super.createPropertyDescriptor(getBeanClass(),"increment", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScaleMessages.getString("incrementDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScaleMessages.getString("incrementSD"), //$NON-NLS-1$
			}
			),
			// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScaleMessages.getString("maximumDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScaleMessages.getString("maximumSD"), //$NON-NLS-1$
			}
			),
			// minimum
			super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScaleMessages.getString("minimumDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScaleMessages.getString("minimumSD"), //$NON-NLS-1$
			}
			),
			// pageIncrement
			super.createPropertyDescriptor(getBeanClass(),"pageIncrement", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScaleMessages.getString("pageIncrementDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScaleMessages.getString("pageIncrementSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScaleMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScaleMessages.getString("selectionSD"), //$NON-NLS-1$
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
