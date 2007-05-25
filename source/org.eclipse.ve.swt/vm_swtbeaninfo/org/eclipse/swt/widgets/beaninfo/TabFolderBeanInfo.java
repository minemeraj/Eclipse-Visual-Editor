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
 *  $RCSfile: TabFolderBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2007-05-25 04:20:17 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TabFolderBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.TabFolder.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {				
			{ "tabPosition" , TabFolderMessages.getString("TabFolderBeanInfo.StyleBits.TabPosition.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TabFolderMessages.getString("TabFolderBeanInfo.StyleBits.TabPosition.Value.Top") , "org.eclipse.swt.SWT.TOP" , new Integer(SWT.TOP) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				TabFolderMessages.getString("TabFolderBeanInfo.StyleBits.TabPosition.Value.Bottom") , "org.eclipse.swt.SWT.BOTTOM" , new Integer(SWT.BOTTOM)				 //$NON-NLS-1$ //$NON-NLS-2$
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
			// clientArea
			super.createPropertyDescriptor(getBeanClass(),"clientArea", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabFolderMessages.getString("clientAreaDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabFolderMessages.getString("clientAreaSD"), //$NON-NLS-1$
			}
			),
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabFolderMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabFolderMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabFolderMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabFolderMessages.getString("itemsSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabFolderMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabFolderMessages.getString("selectionSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selectionIndex
			super.createPropertyDescriptor(getBeanClass(),"selectionIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TabFolderMessages.getString("selectionIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TabFolderMessages.getString("selectionIndexSD"), //$NON-NLS-1$
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
	
	replacePropertyDescriptor(newPDs, "layout", null, new Object[] {  //$NON-NLS-1$
		DESIGNTIMEPROPERTY, Boolean.FALSE,
		}
	);

	return newPDs;
}
}
