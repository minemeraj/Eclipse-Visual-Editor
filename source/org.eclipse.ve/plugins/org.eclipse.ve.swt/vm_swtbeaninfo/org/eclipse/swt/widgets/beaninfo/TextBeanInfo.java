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
 *  $RCSfile: TextBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2004-06-03 14:45:34 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class TextBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Text.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "textAlignment" , TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
			    TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Value.Center") , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) , //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Value.Left") , "org.eclipse.swt.SWT.LEFT" ,  new Integer(SWT.LEFT), 				 //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.TextAlignment.Value.Right") , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT)  //$NON-NLS-1$ //$NON-NLS-2$
			} } ,
			{ "readOnly" , TextMessages.getString("TextBeanInfo.StyleBits.ReadOnly.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.ReadOnly.Value.ReadOnly") , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					 //$NON-NLS-1$ //$NON-NLS-2$
			} },
			{ "lines" , TextMessages.getString("TextBeanInfo.StyleBits.Lines.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Lines.Value.Single") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					 //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Lines.Value.Multi") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				 //$NON-NLS-1$ //$NON-NLS-2$
			} },			
			{ "wrap" , TextMessages.getString("TextBeanInfo.StyleBits.Wrap.Name"), Boolean.FALSE , new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				TextMessages.getString("TextBeanInfo.StyleBits.Wrap.Value.Wrap") , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					 //$NON-NLS-1$ //$NON-NLS-2$
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
			SelectionListenerEventSet.getEventSetDescriptor(getBeanClass()),
			VerifyListenerEventSet.getEventSetDescriptor(getBeanClass())
	};
}
	
}
