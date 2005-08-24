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
 *  $RCSfile: FormTextBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:57 $ 
 */
package org.eclipse.ui.forms.widgets.beaninfo;

import java.beans.BeanDescriptor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.beaninfo.*;
import org.eclipse.swt.widgets.beaninfo.IvjBeanInfo;
import org.eclipse.ui.forms.widgets.FormText;
 
/**
 * 
 * @since 1.0.0
 */
public class FormTextBeanInfo extends IvjBeanInfo {
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "wrap" , LabelMessages.getString("LabelBeanInfo.StyleBits.Wrap.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
					LabelMessages.getString("LabelBeanInfo.StyleBits.Wrap.Value.Wrap") , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					 //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		SweetHelper.mergeSuperclassStyleBits(descriptor);
		return descriptor;
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return FormText.class;
	}

	
	
}
