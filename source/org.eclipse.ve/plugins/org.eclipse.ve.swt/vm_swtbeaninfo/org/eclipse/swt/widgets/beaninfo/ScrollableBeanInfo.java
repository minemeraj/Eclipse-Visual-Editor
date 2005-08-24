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
 *  $RCSfile: ScrollableBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:52:53 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ScrollableBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Scrollable.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {							
			{ "horizontalScroll" , ScrollableMessages.getString("ScrollableBeanInfo.StyleBits.HorizontalScroll.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    ScrollableMessages.getString("ScrollableBeanInfo.StyleBits.HorizontalScroll.Value.HScroll") , "org.eclipse.swt.SWT.H_SCROLL" , new Integer(SWT.H_SCROLL)				 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "verticalScroll" , ScrollableMessages.getString("ScrollableBeanInfo.StyleBits.VerticalScroll.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				ScrollableMessages.getString("ScrollableBeanInfo.StyleBits.VerticalScroll.Value.VScroll") , "org.eclipse.swt.SWT.V_SCROLL" , new Integer(SWT.V_SCROLL)				 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// clientArea
			super.createPropertyDescriptor(getBeanClass(),"clientArea", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollableMessages.getString("clientAreaDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollableMessages.getString("clientAreaSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// horizontalBar
			super.createPropertyDescriptor(getBeanClass(),"horizontalBar", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollableMessages.getString("horizontalBarDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollableMessages.getString("horizontalBarSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// verticalBar
			super.createPropertyDescriptor(getBeanClass(),"verticalBar", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ScrollableMessages.getString("verticalBarDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ScrollableMessages.getString("verticalBarSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
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
