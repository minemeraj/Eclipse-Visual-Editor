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
 *  $RCSfile: TreeBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;

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
		"SWEET_STYLEBITS",
	    new Object[] [] {				
			{ "style" , "style" , Boolean.FALSE , new Object[] {
			    "CHECK" , "org.eclipse.swt.SWT.CHECK" , new Integer(SWT.CHECK) 
			} } ,
			{ "selectionStyle" , "selectionStyle" , Boolean.FALSE , new Object[] {
				"SINGLE" , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					
				"MULTI" , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				
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
}
