package org.eclipse.ve.internal.swt;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EnumeratedIntValueLabelProvider.java,v $
 *  $Revision: 1.4 $  $Date: 2004-03-12 18:50:31 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaDataTypeInstance;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
/**
 * Label provider for the int Orientation field
 */
public class EnumeratedIntValueLabelProvider extends LabelProvider  {

	protected EditDomain editDomain;
	protected String[] FILL_NAMES;
	protected Integer[] FILL_VALUES;
	
public EnumeratedIntValueLabelProvider(String[] NAMES, Integer[] VALUES){
	FILL_NAMES = NAMES;
	FILL_VALUES = VALUES;
}

public String getText(Object element){
	int intValue = 0;
	if(element instanceof IJavaDataTypeInstance){
		intValue = ((IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance)element)).intValue();
	} else {
		intValue = ((Integer)element).intValue();		
	}
	// Match against the int value
	for (int i=0; i<FILL_VALUES.length; i++){
		if(FILL_VALUES[i].intValue() == intValue){
			return FILL_NAMES[i];
		}
	}
	return "";
}
}