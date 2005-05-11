/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanPropertyError.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:20 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.IErrorHolder.ErrorType;
import org.eclipse.ve.internal.cde.core.IErrorHolder.PropertyError;
 

/**
 * A property error for beans. It handles the ThrowableProxy to get more info from it.
 * 
 * @since 1.1.0
 */
public class BeanPropertyError extends PropertyError {
	
	/**
	 * @param severity
	 * @param error
	 * @param sf
	 * @param objectInError
	 * 
	 * @since 1.1.0
	 */
	public BeanPropertyError(int severity, ErrorType error, EStructuralFeature sf, Object objectInError) {
		super(severity, error, sf, objectInError);
	}
	
	/**
	 * @param error
	 * @param severity
	 * @param sf
	 * 
	 * @since 1.1.0
	 */
	public BeanPropertyError(Throwable error, int severity, EStructuralFeature sf) {
		this(severity, new BeanExceptionError(error, severity), sf, null);
	}

	/**
	 * @param severity
	 * @param error
	 * @param sf
	 * @param objectInError
	 * 
	 * @since 1.1.0
	 */
	public BeanPropertyError(int severity, Throwable error, EStructuralFeature sf, Object objectInError) {
		this(severity, new BeanExceptionError(error, severity), sf, objectInError);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IErrorHolder.PropertyError#getPropertyName()
	 */
	protected String getPropertyName() {
		PropertyDecorator pd = Utilities.getPropertyDecorator(getFeature());
		return pd != null ? pd.getDisplayName() : getFeature().getName();
	}	
}
