package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: MinMaxJavaValidator.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.propertysheet.MinmaxValidator;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.INumberBeanProxy;
/**
 * A min/max validator for Java Numbers (either primitive or class).
 * Null is valid. If null isn't valid, then use another validator to
 * state this.
 */
public class MinMaxJavaValidator extends MinmaxValidator {

	/**
	 * Is the object valid. It will see if it is a number proxy,
	 * and if it is, get the number value and let the super class
	 * handle it. Otherwise it is invalid.
	 */	
	public String isValid(Object value) {
		if (value instanceof IJavaInstance) {
			IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) value, ((EObject) value).eResource().getResourceSet());
			if (proxy instanceof INumberBeanProxy)
				return super.isValid(((INumberBeanProxy) proxy).numberValue());
		} else if (value == null)
			return null;
		
		return sNotNumberError;
	}
}