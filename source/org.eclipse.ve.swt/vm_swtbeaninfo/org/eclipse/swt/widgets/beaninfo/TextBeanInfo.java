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
 *  $Revision: 1.1 $  $Date: 2004-03-11 01:47:55 $ 
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
public class TextBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Text.class);
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "textAlignment" , "textAlignment" , Boolean.FALSE ,  new Object[] {
			    "CENTER" , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER) ,
				"LEFT" , "org.eclipse.swt.SWT.LEFT" ,  new Integer(SWT.LEFT), 				
				"RIGHT" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) 
			} } ,
			{ "readOnly" , "readOnly", Boolean.FALSE , new Object[] {
				"READ_ONLY" , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					
			} },
			{ "lines" , "lines", Boolean.FALSE , new Object[] {
				"SINGLE" , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					
				"MULTI" , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				
			} },			
			{ "wrap" , "wrap", Boolean.FALSE , new Object[] {
				"WRAP" , "org.eclipse.swt.SWT.WRAP" , new Integer(SWT.WRAP)					
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
