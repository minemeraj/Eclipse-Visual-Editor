/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComboBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-03 14:45:34 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 
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

/* (non-Javadoc)
 * @see java.beans.BeanInfo#getEventSetDescriptors()
 */
public EventSetDescriptor[] getEventSetDescriptors() {
	return new EventSetDescriptor[] {
			ModifyListenerEventSet.getEventSetDescriptor(getBeanClass()),
			SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}

}
