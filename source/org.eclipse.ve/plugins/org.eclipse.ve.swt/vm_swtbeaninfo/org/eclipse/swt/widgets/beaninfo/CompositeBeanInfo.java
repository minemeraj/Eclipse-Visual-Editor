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
 *  $RCSfile: CompositeBeanInfo.java,v $
 *  $Revision: 1.11 $  $Date: 2005-12-14 21:47:19 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
 
/**
 * 
 * @since 1.0.0
 */
public class CompositeBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Composite.class;
	}	
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "noRadioGroup" , CompositeMessages.getString("CompositeBeanInfo.StyleBits.NoRadioGroup.Name"), Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				CompositeMessages.getString("CompositeBeanInfo.StyleBits.NoRadioGroup.Value.NoRadioGroup") , "org.eclipse.swt.SWT.NO_RADIO_GROUP" , new Integer(SWT.NO_RADIO_GROUP) //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "embedded" , CompositeMessages.getString("CompositeBeanInfo.StyleBits.Embedded.Name"), Boolean.TRUE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				CompositeMessages.getString("CompositeBeanInfo.StyleBits.Embedded.Value.Embedded") , "org.eclipse.swt.SWT.EMBEDDED" , new Integer(SWT.EMBEDDED) //$NON-NLS-1$ //$NON-NLS-2$
			} },
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
			// children
			super.createPropertyDescriptor(getBeanClass(),"children", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CompositeMessages.getString("childrenDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CompositeMessages.getString("childrenSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// layout
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CompositeMessages.getString("layoutDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CompositeMessages.getString("layoutSD"), //$NON-NLS-1$
			}
			),
			// tabList
			super.createPropertyDescriptor(getBeanClass(),"tabList", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CompositeMessages.getString("tabListDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, CompositeMessages.getString("tabListSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// style bit
			super.createPropertyDescriptor(getBeanClass(),"style", new Object[] { //$NON-NLS-1$
				IBaseBeanInfoConstants.FACTORY_CREATION , new Object[] { 
						new Object[] { "org.eclipse.ui.forms.widgets.FormToolkit" , "createComposite" , new Integer(1) ,  //$NON-NLS-1$ //$NON-NLS-2$
								new String[] { "org.eclipse.swt.widgets.Composite" , "int"} } }				 //$NON-NLS-1$ //$NON-NLS-2$
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
