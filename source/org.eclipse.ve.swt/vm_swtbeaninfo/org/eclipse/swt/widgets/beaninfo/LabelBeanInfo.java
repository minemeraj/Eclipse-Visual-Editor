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
 *  $RCSfile: LabelBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-03 14:45:34 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class LabelBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Label.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "orientation" , LabelMessages.getString("LabelBeanInfo.StyleBits.Orientation.Value") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    LabelMessages.getString("LabelBeanInfo.StyleBits.Orientation.Value.Horizontal") , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL) , //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.Orientation.Value.Vertical") , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "textAlignment" , LabelMessages.getString("LabelBeanInfo.StyleBits.TextAlignment.Value"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.TextAlignment.Value.Left") , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.TextAlignment.Value.Right") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) , //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.TextAlignment.Value.Center") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "separator" , LabelMessages.getString("LabelBeanInfo.StyleBits.Separator.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.Separator.Value.Separator") , "org.eclipse.swt.SWT.SEPARATOR" , new Integer(SWT.SEPARATOR)					 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "shadow" , LabelMessages.getString("LabelBeanInfo.StyleBits.Shadow.Name") , Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.Shadow.Value.In") , "org.eclipse.swt.SWT.SHADOW_IN" , new Integer(SWT.SHADOW_IN), //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.Shadow.Value.Out") , "org.eclipse.swt.SWT.SHADOW_OUT" , new Integer(SWT.SHADOW_OUT), //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.Shadow.Value.None") , "org.eclipse.swt.SWT.SHADOW_NONE" , new Integer(SWT.SHADOW_NONE) //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "wrap" , LabelMessages.getString("LabelBeanInfo.StyleBits.Wrap.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				LabelMessages.getString("LabelBeanInfo.StyleBits.Wrap.Value.Wrap") , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					 //$NON-NLS-1$ //$NON-NLS-2$
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
