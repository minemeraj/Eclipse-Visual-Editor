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
 *  $RCSfile: ControlBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-07 14:57:10 $ 
 */
package org.eclipse.swt.widgets.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.SimpleBeanInfo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
 
/**
 * 
 * @since 1.0.0
 */
public class ControlBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Control.class);
	descriptor.setValue(
		"SWEET_STYLEBITS",
	    new Object[] [] {
			{ "border" ,  new Object[] {
			    "BORDER" , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER)				
			} } 
		}
	);
	return descriptor;
}
	
}
