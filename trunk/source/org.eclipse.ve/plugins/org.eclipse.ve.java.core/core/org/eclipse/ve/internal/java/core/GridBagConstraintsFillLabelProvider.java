package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: GridBagConstraintsFillLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Label provider for the GridBagConstraints.fill  field
 */
public class GridBagConstraintsFillLabelProvider extends LabelProvider implements INeedData {
	public static String[] FILL_VALUES = new String[] {
		JavaMessages.getString("NONE"),JavaMessages.getString("HORIZONTAL"),JavaMessages.getString("VERTICAL"),JavaMessages.getString("BOTH") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	};
	
	protected EditDomain editDomain;

public String getText(Object element){
	IIntegerBeanProxy fillValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)element, JavaEditDomainHelper.getResourceSet(editDomain));
	int fillValue = fillValueProxy.intValue();
	switch (fillValue) {
		case GridBagConstraint.NONE: 
			return JavaMessages.getString("NONE"); //$NON-NLS-1$
		case GridBagConstraint.HORIZONTAL: 
			return JavaMessages.getString("HORIZONTAL"); //$NON-NLS-1$
		case GridBagConstraint.VERTICAL: 
			return JavaMessages.getString("VERTICAL"); //$NON-NLS-1$
		case GridBagConstraint.BOTH: 
			return JavaMessages.getString("BOTH"); //$NON-NLS-1$
		default:
			return JavaMessages.getString("NONE"); //$NON-NLS-1$
	}
}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}