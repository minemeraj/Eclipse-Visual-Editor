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
 *  $RCSfile: ComboBeanInfo.java,v $
 *  $Revision: 1.1 $  $Date: 2004-04-26 16:43:44 $ 
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
public class ComboBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Combo.class);
	descriptor.setValue(
		SweetHelper.STYLE_BITS_ID,
		new Object[] [] {
			{ "readOnly" , "readOnly", Boolean.FALSE , new Object[] {
				"READ_ONLY" , "org.eclipse.swt.SWT.READ_ONLY" , new Integer(SWT.READ_ONLY)					
			} },
			{ "style" , "style", Boolean.FALSE , new Object[] {
				"DROP DOWN" , "org.eclipse.swt.SWT.DROP_DOWN" , new Integer(SWT.DROP_DOWN),
				"SIMPLE" , "org.eclipse.swt.SWT.SIMPLE" , new Integer(SWT.SIMPLE)
			} }
		}
	);
	SweetHelper.mergeSuperclassStyleBits(descriptor);
	return descriptor;
}
	
}
