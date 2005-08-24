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
 *  $RCSfile: ListBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:52:53 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ListBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.List.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "selectionStyle" , ListMessages.getString("ListBeanInfo.StyleBits.SelectionStyle.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				ListMessages.getString("ListBeanInfo.StyleBits.SelectionStyle.Value.Single") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				ListMessages.getString("ListBeanInfo.StyleBits.SelectionStyle.Value.Multi") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				 //$NON-NLS-1$ //$NON-NLS-2$
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
			// focusIndex
			super.createPropertyDescriptor(getBeanClass(),"focusIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("focusIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("focusIndexSD"), //$NON-NLS-1$
			}
			),
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// itemHeight
			super.createPropertyDescriptor(getBeanClass(),"itemHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("itemHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("itemHeightSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("itemsSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("selectionSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selectionCount
			super.createPropertyDescriptor(getBeanClass(),"selectionCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("selectionCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("selectionCountSD"), //$NON-NLS-1$
			}
			),
			// selectionIndex
			super.createPropertyDescriptor(getBeanClass(),"selectionIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("selectionIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("selectionIndexSD"), //$NON-NLS-1$
			}
			),
			// selectionIndices
			super.createPropertyDescriptor(getBeanClass(),"selectionIndices", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("selectionIndicesDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("selectionIndicesSD"), //$NON-NLS-1$
			}
			),
			// topIndex
			super.createPropertyDescriptor(getBeanClass(),"topIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ListMessages.getString("topIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ListMessages.getString("topIndexSD"), //$NON-NLS-1$
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
