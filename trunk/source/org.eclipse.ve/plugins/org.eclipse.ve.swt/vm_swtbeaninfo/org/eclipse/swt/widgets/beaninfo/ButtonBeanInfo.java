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
 *  $RCSfile: ButtonBeanInfo.java,v $
 *  $Revision: 1.5 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;

import org.eclipse.swt.SWT;
 
/**
 * 
 * @since 1.0.0
 */
public class ButtonBeanInfo extends IvjBeanInfo {
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.widgets.Button.class;
	}
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "style" , "style" , Boolean.FALSE ,  new Object[] {
			    "PUSH" , "org.eclipse.swt.SWT.PUSH" , new Integer(SWT.PUSH) ,
				"CHECK" , "org.eclipse.swt.SWT.CHECK" ,  new Integer(SWT.CHECK), 				
				"RADIO" , "org.eclipse.swt.SWT.RADIO" , new Integer(SWT.RADIO) ,
				"ARROW" , "org.eclipse.swt.SWT.ARROW" , new Integer(SWT.ARROW) ,
				"TOGGLE" , "org.eclipse.swt.SWT.TOGGLE" , new Integer(SWT.TOGGLE)				
			} } ,
			{ "textAlignment" , "textAlignment", Boolean.FALSE , new Object[] {
				"LEFT" , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,					
				"RIGHT" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) ,
				"CENTER" , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER)				
			} },
			{ "arrowStyle" , "arrowStyle", Boolean.FALSE , new Object[] {
				"UP" , "org.eclipse.swt.SWT.UP" , new Integer(SWT.UP) ,					
				"DOWN" , "org.eclipse.swt.SWT.DOWN" , new Integer(SWT.DOWN) ,
				"LEFT" , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,
				"RIGHT" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) 				
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
