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
 *  $Revision: 1.4 $  $Date: 2004-06-16 12:38:15 $ 
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
}
