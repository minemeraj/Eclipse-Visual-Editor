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
 *  $Revision: 1.1 $  $Date: 2004-03-06 11:30:26 $ 
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
public class ButtonBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Button.class);
	descriptor.setValue(
		"SWEET_STYLEBITS",
	    new Object[] [] {
			{ "style" ,  new Object[] {
			    "push" , "org.eclipse.swt.SWT.PUSH" , new Integer(SWT.PUSH) ,
				"check" , "org.eclipse.swt.SWT.CHECK" ,  new Integer(SWT.CHECK), 				
				"radio" , "org.eclipse.swt.SWT.RADIO" , new Integer(SWT.RADIO) ,
				"arrow" , "org.eclipse.swt.SWT.ARROW" , new Integer(SWT.ARROW) ,
				"toggle" , "org.eclipse.swt.SWT.TOGGLE" , new Integer(SWT.TOGGLE)				
			} } ,
			{ "alignment" , new Object[] {
				"left" , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,					
				"right" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) ,
				"center" , "org.eclipse.swt.SWT.CENTER" , new Integer(SWT.CENTER)				
			} },
			{ "arrowStyle" , new Object[] {
				"up" , "org.eclipse.swt.SWT.UP" , new Integer(SWT.UP) ,					
				"down" , "org.eclipse.swt.SWT.DOWN" , new Integer(SWT.DOWN) ,
				"left" , "org.eclipse.swt.SWT.LEFT" , new Integer(SWT.LEFT) ,
				"right" , "org.eclipse.swt.SWT.RIGHT" , new Integer(SWT.RIGHT) 				
			} }
		}
	);
	return descriptor;
}
	
}
