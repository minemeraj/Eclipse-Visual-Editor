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
 *  $RCSfile: BeanExceptionError.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-15 20:19:38 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.jem.internal.proxy.core.ThrowableProxy;

import org.eclipse.ve.internal.cde.core.IErrorHolder.ExceptionError;
 

/**
 * An exception error that can handle either a ThrowableProxy or a regular exception. It will return the
 * message from the proxy if it is throwable proxy.
 * @since 1.1.0
 */
public class BeanExceptionError extends ExceptionError {

	/**
	 * @param error
	 * @param severity
	 * 
	 * @since 1.1.0
	 */
	public BeanExceptionError(Throwable error, int severity) {
		super(error, severity);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IErrorHolder.ExceptionError#getExceptionClassname()
	 */
	protected String getExceptionClassname() {
		if (getException() instanceof ThrowableProxy)
			return ((ThrowableProxy) getException()).getTypeProxy().getTypeName();
		else
			return super.getExceptionClassname();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IErrorHolder.ExceptionError#getExceptionMessage()
	 */
	protected String getExceptionMessage() {
		if (getException() instanceof ThrowableProxy)
			return ((ThrowableProxy) getException()).getProxyLocalizedMessage();
		else 
			return super.getExceptionMessage();
	}

}
