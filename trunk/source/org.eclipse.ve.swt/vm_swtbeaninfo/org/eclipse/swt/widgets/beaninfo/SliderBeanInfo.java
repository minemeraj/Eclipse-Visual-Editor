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
 *  $RCSfile: SliderBeanInfo.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-01 18:04:09 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;
import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
 
/**
 * 
 * @since 1.0.0
 */
public class SliderBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Slider.class);
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
	    new Object[] [] {
			{ "orientation" , "orientation" , Boolean.FALSE ,  new Object[] {
			    "HORIZONTAL" , "org.eclipse.swt.SWT.HORIZONTAL" , new Integer(SWT.HORIZONTAL) ,
				"VERTICAL" , "org.eclipse.swt.SWT.VERTICAL" ,  new Integer(SWT.VERTICAL) 				
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
			SelectionListenerEventSet.getEventSetDescriptor(Slider.class)
	};
}
}
