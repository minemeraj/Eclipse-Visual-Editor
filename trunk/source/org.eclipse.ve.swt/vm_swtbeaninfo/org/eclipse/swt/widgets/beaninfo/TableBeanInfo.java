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
 *  $RCSfile: TableBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2004-06-15 22:16:27 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TableBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Table.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {				
			{ "style" , TableMessages.getString("TableBeanInfo.StyleBits.Style.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    TableMessages.getString("TableBeanInfo.StyleBits.Style.Value.Check") , "org.eclipse.swt.SWT.CHECK" , new Integer(SWT.CHECK)  //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "selectionStyle" , TableMessages.getString("TableBeanInfo.StyleBits.SelectionStyle.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.SelectionStyle.Value.Single") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.SelectionStyle.Value.Multi") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "fullSelection" , TableMessages.getString("TableBeanInfo.StyleBits.FullSelection.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.FullSelection.Value.FullSelection") , "org.eclipse.swt.FULL_SELECTION" , new Integer(SWT.FULL_SELECTION) 					 //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "hideSelection" , TableMessages.getString("TableBeanInfo.StyleBits.HideSelection.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TableMessages.getString("TableBeanInfo.StyleBits.HideSelection.Value.HideSelection") , "org.eclipse.swt.HIDE_SELECTION" , new Integer(SWT.HIDE_SELECTION) 					 //$NON-NLS-1$ //$NON-NLS-2$
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
}
