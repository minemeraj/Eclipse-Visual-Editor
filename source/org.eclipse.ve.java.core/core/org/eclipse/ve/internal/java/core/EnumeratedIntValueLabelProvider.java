/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: EnumeratedIntValueLabelProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaDataTypeInstance;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

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
