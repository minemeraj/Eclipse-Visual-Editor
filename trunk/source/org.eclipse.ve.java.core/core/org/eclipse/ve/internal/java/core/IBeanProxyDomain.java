/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: IBeanProxyDomain.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:10 $ 
 */

import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
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
	
	/**
	 * Set the name of the "this" type.
	 * @param name
	 * 
	 * @since 1.0.0
	 */
	public void setThisTypeName(String name);
	
	/**
	 * Return the "this" type for this domain.
	 * @return the "this" type for this domain, or <code>null</code> if not retrievable for some reason.
	 * 
	 * @since 1.0.0
	 */
	public IBeanTypeProxy getThisType();
}
