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
public class TableBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Table.class);
	descriptor.setValue(
		"SWEET_STYLEBITS",
	    new Object[] [] {				
			{ "style" ,  new Object[] {
			    "CHECK" , "org.eclipse.swt.SWT.CHECK" , new Integer(SWT.CHECK) 
			} } ,
			{ "selection" , new Object[] {
				"SINGLE" , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) ,					
				"MULTI" , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI)				
			} },
			{ "fullSelection" , new Object[] {
				"FULL_SELECTION" , "org.eclipse.swt.FULL_SELECTION" , new Integer(SWT.FULL_SELECTION) 					
			} } ,
			{ "hideSelection" , new Object[] {
				"HIDE_SELECTION" , "org.eclipse.swt.HIDE_SELECTION" , new Integer(SWT.HIDE_SELECTION) 					
			} }			
		}
	);
	return descriptor;
}
	
}
