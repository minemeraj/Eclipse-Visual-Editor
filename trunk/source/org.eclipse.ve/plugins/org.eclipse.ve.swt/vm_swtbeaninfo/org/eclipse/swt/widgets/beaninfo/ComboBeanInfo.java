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
 *  $Revision: 1.2 $  $Date: 2004-06-01 18:04:09 $ 
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
			{ "readOnly" , "readOnly", Boolean.FALSE , new Object[] {
				"READ_ONLY" , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					
			} },
			{ "style" , "style", Boolean.FALSE , new Object[] {
				"DROP DOWN" , "org.eclipse.swt.SWT.DROP_DOWN" , new Integer(SWT.DROP_DOWN),
				"SIMPLE" , "org.eclipse.swt.SWT.SIMPLE" , new Integer(SWT.SIMPLE)
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
