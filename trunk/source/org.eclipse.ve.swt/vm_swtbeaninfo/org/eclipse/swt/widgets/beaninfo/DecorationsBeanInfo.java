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
 *  $RCSfile: DecorationsBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-09 00:07:48 $ 
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
public class DecorationsBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Decorations.class);
	descriptor.setValue("SWEET_STYLEBITS",
	    new Object[] [] {
			{ "trim" , "trim" , Boolean.FALSE , new Object[] {
			    "SHELL_TRIM" , "org.eclipse.swt.SWT.SHELL_TRIM" , new Integer(SWT.SHELL_TRIM) ,				
			    "DIALOG_TRIM" , "org.eclipse.swt.SWT.DIALOG_TRIM" , new Integer(SWT.DIALOG_TRIM) ,
				"NO_TRIM" , "org.eclipse.swt.SWT.NO_TRIM" , new Integer(SWT.NO_TRIM)								
			} } ,
			{ "on_top" , "on_top" , Boolean.FALSE , new Object[] {
				"ON_TOP" , "org.eclipse.swt.SWT.ON_TOP" , new Integer(SWT.ON_TOP)				
			} } ,			
			{ "close" , "close" , Boolean.TRUE , new Object[] {
			    "CLOSE" , "org.eclipse.swt.SWT.CLOSE" , new Integer(SWT.CLOSE)				
			} } ,
			{ "min" , "min" , Boolean.TRUE , new Object[] {
				"MIN" , "org.eclipse.swt.SWT.MIN" , new Integer(SWT.MIN)				
			} } ,
			{ "max" , "max" , Boolean.TRUE, new Object[] {
				"MAX" , "org.eclipse.swt.SWT.MAX" , new Integer(SWT.MAX)				
			} } ,
			{ "resize" , "resize" , Boolean.TRUE, new Object[] {
				"RESIZE" , "org.eclipse.swt.SWT.RESIZE" , new Integer(SWT.RESIZE)				
			} } ,
			{ "title" , "title" , Boolean.TRUE, new Object[] {
				"TITLE" , "org.eclipse.swt.SWT.TITLE" , new Integer(SWT.TITLE)				
			} } 
		}
	);
	// Do not inherit from Composite otherwise we will pick up things like noMergePaintEvents and noBackground
	SweetHelper.mergeStyleBits(descriptor, new Class[] {Scrollable.class,});
	return descriptor;
}
	
}
