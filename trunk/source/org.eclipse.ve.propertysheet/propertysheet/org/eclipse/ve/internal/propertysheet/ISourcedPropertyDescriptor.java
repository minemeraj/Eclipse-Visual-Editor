package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ISourcedPropertyDescriptor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:47:33 $ 
 */


import org.eclipse.ui.views.properties.*;

/**
 * This property descriptor is used when the
 * source is not just a straight IPropertySource and
 * the value when querying must be handled specially.
 */
public interface ISourcedPropertyDescriptor extends IPropertyDescriptor {
	/**
	 * Query the value from this descriptor for the
	 * given source.
	 * Creation date: (6/9/00 2:42:36 PM)
	 * @return java.lang.Object
	 * @param source java.lang.Object
	 */
	public Object getValue(IPropertySource source);
	
	/**
	 * Return whether this value is set or not for the
	 * given source using this descriptor.
	 */
	public boolean isSet(IPropertySource source);
}
