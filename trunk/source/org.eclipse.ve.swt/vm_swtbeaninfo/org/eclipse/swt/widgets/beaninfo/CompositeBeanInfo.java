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
 *  $RCSfile: CompositeBeanInfo.java,v $
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
public class CompositeBeanInfo extends SimpleBeanInfo {
	
public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor descriptor = new BeanDescriptor(Composite.class);
	descriptor.setValue(
		"SWEET_STYLEBITS",
	    new Object[] [] {			
			{ "noBackground" ,  new Object[] {
			    "NO_BACKGROUND" , "org.eclipse.swt.SWT.NO_BACKGROUND" , new Integer(SWT.NO_BACKGROUND)				
			} } ,
			{ "noFocus" ,  new Object[] {
				"NO_FOCUS" , "org.eclipse.swt.SWT.NO_FOCUS" , new Integer(SWT.NO_FOCUS)				
			} } ,
			{ "noMergePaints" ,  new Object[] {
				"NO_MERGE_PAINTS" , "org.eclipse.swt.SWT.NO_MERGE_PAINTS" , new Integer(SWT.NO_MERGE_PAINTS)				
			} } ,
			{ "noRedrawResize" ,  new Object[] {
				"NO_REDRAW_RESIZE" , "org.eclipse.swt.SWT.NO_REDRAW_RESIZE" , new Integer(SWT.NO_REDRAW_RESIZE)				
			} } ,
			{ "noRadioGroup" ,  new Object[] {
				"NO_RADIO_GROUP" , "org.eclipse.swt.SWT.NO_RADIO_GROUP" , new Integer(SWT.NO_RADIO_GROUP)				
			} } 			
		}
	);
	return descriptor;
}
	
}
