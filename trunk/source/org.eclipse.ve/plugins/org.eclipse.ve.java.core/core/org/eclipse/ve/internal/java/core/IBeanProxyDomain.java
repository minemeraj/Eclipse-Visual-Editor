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
 *  $RCSfile: IBeanProxyDomain.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-19 22:50:27 $ 
 */

import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;
import org.eclipse.ve.internal.cde.core.EditDomain;
/**
 * The interface for the BeanProxyDomain. It is
 * placed into each BeanProxyAdapter and used to access certain
 * data.
 */
public interface IBeanProxyDomain {
	
	/**
	 * Return the proxy factory registry to be used for this domain.
	 * 
	 * @return Proxy factory registry.
	 * 
	 * @since 1.0.0
	 */
	public ProxyFactoryRegistry getProxyFactoryRegistry();
	
	/**
	 * Return the CDE EditDomain for this proxy domain.
	 * 
	 * @return The edit domain.
	 * 
	 * @since 1.0.0
	 */
	public EditDomain getEditDomain();
	
	/**
	 * The allocation processor for this domain.
	 * @return The allocation processor.
	 * 
	 * @since 1.0.0
	 */
	public IAllocationProcesser getAllocationProcesser();
}