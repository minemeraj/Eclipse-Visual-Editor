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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagConstraintsFillLabelProvider.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-21 22:53:45 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Label provider for the GridBagConstraints.fill  field
 */
public class GridBagConstraintsFillLabelProvider extends LabelProvider implements INeedData {
	public static String[] FILL_VALUES = new String[] {
		JavaMessages.NONE,JavaMessages.HORIZONTAL,JavaMessages.VERTICAL,JavaMessages.BOTH
	};
	
	protected EditDomain editDomain;

public String getText(Object element){
	IIntegerBeanProxy fillValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)element, JavaEditDomainHelper.getResourceSet(editDomain));
	int fillValue = fillValueProxy.intValue();
	switch (fillValue) {
		case GridBagConstraint.NONE: 
			return JavaMessages.NONE; 
		case GridBagConstraint.HORIZONTAL: 
			return JavaMessages.HORIZONTAL; 
		case GridBagConstraint.VERTICAL: 
			return JavaMessages.VERTICAL; 
		case GridBagConstraint.BOTH: 
			return JavaMessages.BOTH; 
		default:
			return JavaMessages.NONE; 
	}
}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}
