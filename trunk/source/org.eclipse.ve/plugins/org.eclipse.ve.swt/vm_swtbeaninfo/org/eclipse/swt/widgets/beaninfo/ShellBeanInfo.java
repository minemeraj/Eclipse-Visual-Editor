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
 *  $RCSfile: ShellBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;
 

/**
 * @since 1.0.0
 *
 */
public class ShellBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Shell.class;
	}

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "modality" , "modality" , Boolean.TRUE , new Object[] {
				    "APPLICATION_MODAL" , "org.eclipse.swt.SWT.APPLICATION_MODAL" , new Integer(SWT.APPLICATION_MODAL) ,				
				    "MODELESS" , "org.eclipse.swt.SWT.MODELESS" , new Integer(SWT.MODELESS) ,
					"PRIMARY_MODAL" , "org.eclipse.swt.SWT.PRIMARY_MODAL" , new Integer(SWT.PRIMARY_MODAL),
					"SYSTEM_MODAL" , "org.eclipse.swt.SWT.SYSTEM_MODAL" , new Integer(SWT.SYSTEM_MODAL)
				} }
			}
		);
		// Do not inherit from Composite otherwise we will pick up things like noMergePaintEvents and noBackground
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				ShellListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}
}
