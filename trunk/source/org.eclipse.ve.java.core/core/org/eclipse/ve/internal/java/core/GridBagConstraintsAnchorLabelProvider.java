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
 *  $RCSfile: GridBagConstraintsAnchorLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Label provider for the GridBagConstraints.anchor  field
 */
public class GridBagConstraintsAnchorLabelProvider extends LabelProvider implements INeedData {
	public static String[] ANCHOR_VALUES = new String[] {
		JavaMessages.getString("CENTER"), JavaMessages.getString("NORTH"), JavaMessages.getString("NORTHEAST"), JavaMessages.getString("EAST"),JavaMessages.getString("SOUTHEAST"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		JavaMessages.getString("SOUTH"),JavaMessages.getString("SOUTHWEST"),JavaMessages.getString("WEST"),JavaMessages.getString("NORTHWEST") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	};
	
	protected EditDomain editDomain;

public String getText(Object element){
	IIntegerBeanProxy anchorValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)element, JavaEditDomainHelper.getResourceSet(editDomain));
	int anchorValue = anchorValueProxy.intValue();
	switch (anchorValue) {
		case GridBagConstraint.CENTER: 
			return JavaMessages.getString("CENTER"); //$NON-NLS-1$
		case GridBagConstraint.NORTH: 
			return JavaMessages.getString("NORTH"); //$NON-NLS-1$
		case GridBagConstraint.NORTHEAST: 
			return JavaMessages.getString("NORTHEAST"); //$NON-NLS-1$
		case GridBagConstraint.EAST: 
			return JavaMessages.getString("EAST"); //$NON-NLS-1$
		case GridBagConstraint.SOUTHEAST: 
			return JavaMessages.getString("SOUTHEAST"); //$NON-NLS-1$
		case GridBagConstraint.SOUTH: 
			return JavaMessages.getString("SOUTH"); //$NON-NLS-1$
		case GridBagConstraint.SOUTHWEST: 
			return JavaMessages.getString("SOUTHWEST"); //$NON-NLS-1$
		case GridBagConstraint.WEST: 
			return JavaMessages.getString("WEST"); //$NON-NLS-1$
		case GridBagConstraint.NORTHWEST: 
			return JavaMessages.getString("NORTHWEST"); //$NON-NLS-1$
		default:
			return JavaMessages.getString("CENTER"); //$NON-NLS-1$
	}
}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}