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
 *  $RCSfile: TableBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-11 01:47:55 $ 
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
public class TableBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Table.class);
	descriptor.setValue(
		"SWEET_STYLEBITS",
	    new Object[] [] {				
			{ "style" , "style" , Boolean.FALSE , new Object[] {
			    "CHECK" , "org.eclipse.swt.SWT.CHECK" , new Integer(SWT.CHECK) 
			} } ,
			{ "selection" , "selection" , Boolean.FALSE , new Object[] {
				"SINGLE" , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					
				"MULTI" , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				
			} },
			{ "fullSelection" , "fullSelection" , Boolean.FALSE , new Object[] {
				"FULL_SELECTION" , "org.eclipse.swt.FULL_SELECTION" , new Integer(SWT.FULL_SELECTION) 					
			} } ,
			{ "hideSelection" , "hideSelection" , Boolean.FALSE , new Object[] {
				"HIDE_SELECTION" , "org.eclipse.swt.HIDE_SELECTION" , new Integer(SWT.HIDE_SELECTION) 					
			} }			
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
