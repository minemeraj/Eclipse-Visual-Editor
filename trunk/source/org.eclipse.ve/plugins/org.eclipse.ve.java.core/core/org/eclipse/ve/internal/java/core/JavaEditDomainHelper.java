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
 *  $RCSfile: JavaEditDomainHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
/**
 * This contains keys to the EditDomain in the
 * JBCF case, and helper methods to set it up.
 */
public class JavaEditDomainHelper extends EMFEditDomainHelper {
	
	public static final String
		BEAN_PROXY_DOMAIN_KEY = "org.eclipse.ve.internal.java.core.beanproxydomainkey"; //$NON-NLS-1$
		
		
	/**
	 * The IBeanProxyDomain for the editor. This is the editor specific proxy domain.
	 */
	public static IBeanProxyDomain getBeanProxyDomain(EditDomain dom) {
		return (IBeanProxyDomain) dom.getData(BEAN_PROXY_DOMAIN_KEY);
	}
	
	/**
	 * The IBeanProxyDomain for the editor. Gotton from the IBeanProxyHost of the passed
	 * in IJavaInstance. If the beanproxyhost has not already been added and this
	 * bean is not contained, then domain cannot be found.
	 */
	public static IBeanProxyDomain getBeanProxyDomain(IJavaInstance aBean) {
		IBeanProxyHost phost = BeanProxyUtilities.getBeanProxyHost(aBean);
		return phost != null ? phost.getBeanProxyDomain() : null;
	} 
	
	/**
	 * The BeanProxyAdapterFactory for this editor.
	 */
	public static AdapterFactory getBeanProxyAdapterFactory(EditDomain dom) {
		return EcoreUtil.getAdapterFactory(EMFEditDomainHelper.getResourceSet(dom).getAdapterFactories(), IBeanProxyHost.BEAN_PROXY_TYPE);
	}	

		
	/**
	 * Set the BeanProxyDomain into the domain.
	 */
	public static void setBeanProxyDomain(IBeanProxyDomain proxyDomain, EditDomain dom) {
		dom.setData(BEAN_PROXY_DOMAIN_KEY, proxyDomain);
	}

}