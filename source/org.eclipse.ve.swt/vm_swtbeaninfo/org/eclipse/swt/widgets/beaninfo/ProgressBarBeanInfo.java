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
 *  $RCSfile: ProgressBarBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-03 14:45:34 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
 

/**
 * 
 * @since 1.0.0
 */
public class ProgressBarBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.ProgressBar.class;
	}

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "smooth" , ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Smooth.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Smooth.Value.Smooth") , "org.eclipse.swt.SWT.SMOOTH" , new Integer(SWT.SMOOTH) //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "indeterminate", ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Intermediate.Name"), Boolean.FALSE, new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Intermediate.Value.Intermediate") , "org.eclipse.swt.SWT.INDETERMINATE" ,  new Integer(SWT.INDETERMINATE)				 //$NON-NLS-1$ //$NON-NLS-2$
				} } ,
				{ "orientation" , ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Orientation.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Orientation.Value.Horizontal") , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL), //$NON-NLS-1$ //$NON-NLS-2$
					ProgressBarMessages.getString("ProgressBarBeanInfo.StyleBits.Orientation.Value.Vertical") , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				 //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}
	
}
