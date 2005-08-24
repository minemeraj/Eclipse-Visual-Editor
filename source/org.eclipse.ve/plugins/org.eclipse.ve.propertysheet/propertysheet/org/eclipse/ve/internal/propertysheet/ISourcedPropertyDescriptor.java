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
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: ISourcedPropertyDescriptor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:44:29 $ 
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
	
    /**
     * Returns whether the value of this property for the given source is
     * resettable to a default value.
     * 
     * @param id
     *            the id of the property
     * @return <code>true</code> if the property with the specified id has a
     *         meaningful default value to which it can be resetted, and
     *         <code>false</code> otherwise
     * @see IPropertySource#resetPropertyValue(Object)
     * @see IPropertySource#isPropertySet(Object)
     */
    boolean isPropertyResettable(IPropertySource source);	
}
