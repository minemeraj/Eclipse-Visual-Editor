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
 *  $RCSfile: BeanInstantiationException.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-22 21:05:23 $ 
 */
package org.eclipse.ve.internal.java.remotevm;
 

/**
 * This is thrown by instantiation of a bean in BeanProxyAdapter to let everyone know the bean
 * did not instantiate. The actual error has already been processed. This is to tell the difference
 * between an instantiation error and an apply error.
 * @since 1.1.0
 */
public class BeanInstantiationException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 4141977353006039779L;

	/**
	 * Create.
	 * 
	 * @since 1.1.0
	 */
	public BeanInstantiationException() {
		super();
		// TODO Auto-generated constructor stub
	}
}
