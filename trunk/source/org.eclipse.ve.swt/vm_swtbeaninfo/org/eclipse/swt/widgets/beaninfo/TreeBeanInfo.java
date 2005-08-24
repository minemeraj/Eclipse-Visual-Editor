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
 *  $RCSfile: TreeBeanInfo.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:52:54 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TreeBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Tree.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {				
			{ "style" , TreeMessages.getString("TreeBeanInfo.StyleBits.Style.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    TreeMessages.getString("TreeBeanInfo.StyleBits.Style.Value.Check") , "org.eclipse.swt.SWT.CHECK" , new Integer(SWT.CHECK)  //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "selectionStyle" , TreeMessages.getString("TreeBeanInfo.StyleBits.SelectionStyle.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TreeMessages.getString("TreeBeanInfo.StyleBits.SelectionStyle.Value.Single") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				TreeMessages.getString("TreeBeanInfo.StyleBits.SelectionStyle.Value.Multi") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				 //$NON-NLS-1$ //$NON-NLS-2$
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
			TreeListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// itemHeight
			super.createPropertyDescriptor(getBeanClass(),"itemHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("itemHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("itemHeightSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("itemsSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// parentItem
			super.createPropertyDescriptor(getBeanClass(),"parentItem", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("parentItemDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("parentItemSD"), //$NON-NLS-1$
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("selectionSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selectionCount
			super.createPropertyDescriptor(getBeanClass(),"selectionCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("selectionCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("selectionCountSD"), //$NON-NLS-1$
			}
			),
			// topItem
			super.createPropertyDescriptor(getBeanClass(),"topItem", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, TreeMessages.getString("topItemDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, TreeMessages.getString("topItemSD"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// style bit
			super.createPropertyDescriptor(getBeanClass(),"style", new Object[] { //$NON-NLS-1$
				FACTORY_CREATION  , new Object[] { 
						new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createTree" , new Integer(1) , 
								new String[] { "org.eclipse.swt.widgets.Composite" , "int"} } }				
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
	
	replacePropertyDescriptor(newPDs, "layout", null, new Object[] {  //$NON-NLS-1$
		DESIGNTIMEPROPERTY, Boolean.FALSE,
		}
	);

	return newPDs;
}
}
