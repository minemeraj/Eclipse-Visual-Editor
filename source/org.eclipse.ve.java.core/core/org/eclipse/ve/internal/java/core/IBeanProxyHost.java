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
 *  $RCSfile: IBeanProxyHost.java,v $
 *  $Revision: 1.4 $  $Date: 2005-05-11 19:01:20 $ 
 */

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.IErrorNotifier;

/**
 * This is the adapter used to access/hold the bean associated with a MOF JavaBean.
 * <p>
 * Note: It is important that any implementers of this interface MUST have a constructor that takes one argument of type IBeanProxyDomain. It won't be
 * constructed otherwise.
 * 
 * @since 1.0.0
 */
public interface IBeanProxyHost extends Adapter, IErrorNotifier {

	public final static Class BEAN_PROXY_TYPE = IBeanProxyHost.class;

	/**
	 * This method is called if we want the bean proxy to be released. This would be used to recreate the bean proxy if necessary.
	 * <p>
	 * This should only be used when there is only one bean being released. If it is known that there are more than one
	 * bean being released, the {@link IBeanProxyHost2#releaseBeanProxy(IExpression) releaseWithExpression} is the preferred way
	 * of doing the release.
	 */
	void releaseBeanProxy();

	/**
	 * Get the attribute value from the bean. The value returned WILL NOT be contained in any EMF document,
	 * <p>
	 * NOTE: Any implementers of this method must have an ImplicitAllocation. This is a required function for the VCE to work correctly.
	 * 
	 * @param aBeanPropertyFeature
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public IJavaInstance getBeanPropertyValue(EStructuralFeature aBeanPropertyFeature);

	/**
	 * Get the property value as a bean proxy.
	 * 
	 * @param aBeanPropertyFeature
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public IBeanProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature);

	/**
	 * Return the bean proxy this host is wrappering.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public IBeanProxy getBeanProxy();

	/**
	 * Instantiate the bean proxy, if not already instantiated.
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public IBeanProxy instantiateBeanProxy();

	/**
	 * Answer whether the bean is instantiated or not.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public boolean isBeanProxyInstantiated();

	/**
	 * Allow the bean proxy to be put back into a valid state if required An example is where the bean was changed by a customizer. This method will
	 * be called if the customizer fires a propertyChangeEvent signalling the VCE to refresh itself.
	 * <p>
	 * <b>NOTE:</b> The customizer hosting is quite involved as the customizer changes the bean and the VCE needs to work out what attributes have changed so
	 * the EMF model is updated and code generation works correctly. Look at BeanCustomizer and the different customizers for the example classes Area
	 * and ButtonBar as examples
	 */
	public void revalidateBeanProxy();

	/**
	 * Invalidate the bean proxy. 
	 * @deprecated does nothing now.
	 */
	public void invalidateBeanProxy();

	/**
	 * Validate the bean proxy. 
	 * @deprecated does nothing now.
	 */
	public void validateBeanProxy();

	/**
	 * Return IBeanProxyDomain for this BeanProxyHost.
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public IBeanProxyDomain getBeanProxyDomain();

	/**
	 * A child wishes to be re-created. It determined that it can't do it itself.
	 * @deprecated this should not be implemented in a general case. Use the new notifier mechanism of BeanProxyAdapter2.
	 */
	public void reinstantiateChild(IBeanProxyHost aChildProxyHost);
}