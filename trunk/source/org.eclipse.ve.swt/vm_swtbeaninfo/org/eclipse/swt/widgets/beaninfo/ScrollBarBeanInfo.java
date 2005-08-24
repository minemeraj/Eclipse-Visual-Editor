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
 *  $RCSfile: ScrollBarBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:52:54 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ScrollBarBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.ScrollBar.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "orientation" , ScrollBarMessages.getString("ScrollBarBeanInfo.StyleBits.Orientation.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ScrollBarMessages.getString("ScrollBarBeanInfo.StyleBits.Orientation.Value.Horizontal") , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL) , //$NON-NLS-1$ //$NON-NLS-2$
				ScrollBarMessages.getString("ScrollBarBeanInfo.StyleBits.Orientation.Value.Vertical") , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				 //$NON-NLS-1$ //$NON-NLS-2$
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
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("enabledDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("enabledSD"), //$NON-NLS-1$
			}
			),
			// increment
			super.createPropertyDescriptor(getBeanClass(),"increment", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("incrementDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("incrementSD"), //$NON-NLS-1$
			}
			),
			// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("maximumDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("maximumSD"), //$NON-NLS-1$
			}
			),
			// minimum
			super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("minimumDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("minimumSD"), //$NON-NLS-1$
			}
			),
			// pageIncrement
			super.createPropertyDescriptor(getBeanClass(),"pageIncrement", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("pageIncrementDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("pageIncrementSD"), //$NON-NLS-1$
			}
			),
			// parent
			super.createPropertyDescriptor(getBeanClass(),"parent", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("parentDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("parentSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("selectionSD"), //$NON-NLS-1$
			}
			),
			// size
			super.createPropertyDescriptor(getBeanClass(),"size", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("sizeDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("sizeSD"), //$NON-NLS-1$
			}
			),
			// thumb
			super.createPropertyDescriptor(getBeanClass(),"thumb", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("thumbDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("thumbSD"), //$NON-NLS-1$
			}
			),
			// visible
			super.createPropertyDescriptor(getBeanClass(),"visible", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollBarMessages.getString("visibleDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollBarMessages.getString("visibleSD"), //$NON-NLS-1$
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
