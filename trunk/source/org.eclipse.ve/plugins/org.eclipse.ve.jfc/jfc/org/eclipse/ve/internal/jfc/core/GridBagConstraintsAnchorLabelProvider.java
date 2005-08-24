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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagConstraintsAnchorLabelProvider.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.jface.viewers.LabelProvider;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * Label provider for the GridBagConstraints.anchor  field
 */
public class GridBagConstraintsAnchorLabelProvider extends LabelProvider implements INeedData {
	public static String[] ANCHOR_VALUES = new String[] {
		JavaMessages.CENTER, JavaMessages.NORTH, JavaMessages.NORTHEAST, JavaMessages.EAST,JavaMessages.SOUTHEAST, 
		JavaMessages.SOUTH,JavaMessages.SOUTHWEST,JavaMessages.WEST,JavaMessages.NORTHWEST
	};
	
	protected EditDomain editDomain;

public String getText(Object element){
	IIntegerBeanProxy anchorValueProxy = (IIntegerBeanProxy)BeanProxyUtilities.getBeanProxy((IJavaInstance)element, JavaEditDomainHelper.getResourceSet(editDomain));
	int anchorValue = anchorValueProxy.intValue();
	switch (anchorValue) {
		case GridBagConstraint.CENTER: 
			return JavaMessages.CENTER; 
		case GridBagConstraint.NORTH: 
			return JavaMessages.NORTH; 
		case GridBagConstraint.NORTHEAST: 
			return JavaMessages.NORTHEAST; 
		case GridBagConstraint.EAST: 
			return JavaMessages.EAST; 
		case GridBagConstraint.SOUTHEAST: 
			return JavaMessages.SOUTHEAST; 
		case GridBagConstraint.SOUTH: 
			return JavaMessages.SOUTH; 
		case GridBagConstraint.SOUTHWEST: 
			return JavaMessages.SOUTHWEST; 
		case GridBagConstraint.WEST: 
			return JavaMessages.WEST; 
		case GridBagConstraint.NORTHWEST: 
			return JavaMessages.NORTHWEST; 
		default:
			return JavaMessages.CENTER; 
	}
}
	/**
	 * @see INeedData#setData(Object)
	 */
	public void setData(Object data) {
		editDomain = (EditDomain) data;
	}

}
