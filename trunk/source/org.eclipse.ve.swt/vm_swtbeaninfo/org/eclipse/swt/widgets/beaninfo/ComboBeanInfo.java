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
 *  $RCSfile: ComboBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-10-06 15:18:50 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;
 
/**
 * 
 * @since 1.0.0
 */
public class ComboBeanInfo extends IvjBeanInfo {

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Combo.class;
	}	
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "readOnly" , ComboMessages.getString("ComboBeanInfo.StyleBits.Readonly.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				ComboMessages.getString("ComboBeanInfo.StyleBits.Readonly.Value.ReadOnly") , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "style" , ComboMessages.getString("ComboBeanInfo.StyleBits.Style.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				ComboMessages.getString("ComboBeanInfo.StyleBits.Style.Value.DropDown") , "org.eclipse.swt.SWT.DROP_DOWN" , new Integer(SWT.DROP_DOWN), //$NON-NLS-1$ //$NON-NLS-2$
				ComboMessages.getString("ComboBeanInfo.StyleBits.Style.Value.Simple") , "org.eclipse.swt.SWT.SIMPLE" , new Integer(SWT.SIMPLE) //$NON-NLS-1$ //$NON-NLS-2$
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
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("itemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("itemCountSD"), //$NON-NLS-1$
			}
			),
			// itemHeight
			super.createPropertyDescriptor(getBeanClass(),"itemHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("itemHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("itemHeightSD"), //$NON-NLS-1$
			}
			),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("itemsDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("itemsSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("orientationDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("orientationSD"), //$NON-NLS-1$
		      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
						ComboMessages.getString("orientation.left_to_right"), new Integer(org.eclipse.swt.SWT.LEFT_TO_RIGHT), //$NON-NLS-1$
				   		"org.eclipse.swt.SWT.LEFT_TO_RIGHT", //$NON-NLS-1$
				   		ComboMessages.getString("orientation.right_to_left"), new Integer(org.eclipse.swt.SWT.RIGHT_TO_LEFT), //$NON-NLS-1$
				   		"org.eclipse.swt.SWT.RIGHT_TO_LEFT", //$NON-NLS-1$
				},
				EXPERT, Boolean.TRUE,
			}
			),
			// selection
			super.createPropertyDescriptor(getBeanClass(),"selection", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("selectionDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("selectionSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// selectionIndex
			super.createPropertyDescriptor(getBeanClass(),"selectionIndex", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("selectionIndexDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("selectionIndexSD"), //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,
			}
			),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("textDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("textSD"), //$NON-NLS-1$
			}
			),
			// textHeight
			super.createPropertyDescriptor(getBeanClass(),"textHeight", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("textHeightDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("textHeightSD"), //$NON-NLS-1$
			}
			),
			// textLimit
			super.createPropertyDescriptor(getBeanClass(),"textLimit", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("textLimitDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("textLimitSD"), //$NON-NLS-1$
			}
			),
			// visibleItemCount
			super.createPropertyDescriptor(getBeanClass(),"visibleItemCount", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, ComboMessages.getString("visibleItemCountDN"), //$NON-NLS-1$
				SHORTDESCRIPTION, ComboMessages.getString("visibleItemCountSD"), //$NON-NLS-1$
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
