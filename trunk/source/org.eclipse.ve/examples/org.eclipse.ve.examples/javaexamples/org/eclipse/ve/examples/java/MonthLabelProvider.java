/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java;
/*
 *  $RCSfile: MonthLabelProvider.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
/**
 * This is an example of a LabelProvider that is described directly on the 
 * Month Feature of Area.  It shows how to write a LabelProvider that does
 * rendering of the IJavaObjectInstance directly on the IDE side rather than
 * BeanCellRenderer which hosts a java.beans.PropertyEditor and involes
 * doing a setValue(Object) and getAsText()
 */
public class MonthLabelProvider extends LabelProvider {
	
	public static String[] MONTHS = new String[] {
		ExampleMessages.getString("LabelProvider.Month.January"),ExampleMessages.getString("LabelProvider.Month.February"),ExampleMessages.getString("LabelProvider.Month.March"),ExampleMessages.getString("LabelProvider.Month.April"),ExampleMessages.getString("LabelProvider.Month.May"),ExampleMessages.getString("LabelProvider.Month.June"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		ExampleMessages.getString("LabelProvider.Month.July"),ExampleMessages.getString("LabelProvider.Month.August"),ExampleMessages.getString("LabelProvider.Month.September"),ExampleMessages.getString("LabelProvider.Month.October"),ExampleMessages.getString("LabelProvider.Month.November"),ExampleMessages.getString("LabelProvider.Month.December") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	};

public String getText(Object element){

	IIntegerBeanProxy monthProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)element);
	try {
		// The month is zero indexed array but I think 1 represents January better than 0
		return MONTHS[monthProxy.intValue() - 1];
	} catch ( ArrayIndexOutOfBoundsException exc ){
		return ""; //$NON-NLS-1$
	}
	
}
}
