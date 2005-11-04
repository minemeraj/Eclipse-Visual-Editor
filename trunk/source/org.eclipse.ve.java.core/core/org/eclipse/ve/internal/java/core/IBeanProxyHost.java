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
/*
 *  $RCSfile: IBeanProxyHost.java,v $
 *  $Revision: 1.7 $  $Date: 2005-11-04 00:08:57 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.IErrorNotifier;

/**
 * This is the adapter used to access/hold the bean associated with a MOF JavaBean.
 * <p>
 * <b>Note:</b> It is important that any implementers of this interface MUST have a constructor that takes one argument of type IBeanProxyDomain. It won't be
 * constructed otherwise. They must also implement {@link org.eclipse.ve.internal.java.core.IInternalBeanProxyHost}.
 * <p>
 * <b>Note:</b> Instead of implementing this interface, it would be better to subclass {@link org.eclipse.ve.internal.java.core.BeanProxyAdapter}.
 * 
 * @since 1.0.0
 */
public interface IBeanProxyHost extends Adapter, IErrorNotifier {

	public final static Class BEAN_PROXY_TYPE = IBeanProxyHost.class;

	/**
	 * This method is called if we want the bean proxy to be released. This would be used to recreate the bean proxy if necessary.
	 * <p>
	 * This should only be used when there is only one bean being released. If it is known that there are more than one bean being released, the
	 * {@link IBeanProxyHost#releaseBeanProxy(IExpression)} is the preferred way of doing the release.
	 */
	void releaseBeanProxy();

	/**
	 * Get the attribute value from the bean. The value returned WILL NOT be contained in any EMF document. This should not be called
	 * if the feature is set. It will not return the setting, it will instead return the original setting.
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
	 * Get the property value as a bean proxy. This should not be called
	 * if the feature is set. It will not return the setting, it will instead return the original setting.
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
	 * <p>
	 * This should only be used when there is only one bean being instantiated. If it is known that there are more than one bean being instantiated,
	 * the {@link IBeanProxyHost#instantiateBeanProxy(IExpression)} is the preferred way of doing the instantiate.
	 * 
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
	 * <b>NOTE:</b> The customizer hosting is quite involved as the customizer changes the bean and the VCE needs to work out what attributes have
	 * changed so the EMF model is updated and code generation works correctly. Look at BeanCustomizer and the different customizers for the example
	 * classes Area and ButtonBar as examples
	 */
	public void revalidateBeanProxy();

	/**
	 * Return IBeanProxyDomain for this BeanProxyHost.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public IBeanProxyDomain getBeanProxyDomain();

	/**
	 * The proxy exception thrown when a bean instantiation failed. This is caught through expressions when instantiating a setting that is about to
	 * be applied. This way the expression can try to instantiate and then try to apply, and then catch this exception and any other exception. If
	 * this exception is caught then it knows that it was the instantiation that failed and not the apply.
	 * 
	 * @since 1.1.0
	 */
	public static final String BEAN_INSTANTIATION_EXCEPTION = "org.eclipse.ve.internal.java.remotevm.BeanInstantiationException"; //$NON-NLS-1$

	/**
	 * Release the bean proxy. The expression can be used for any proxy functions that need to be done for the release. This is the preferred call to
	 * use when releasing more than one bean at a time.
	 * <p>
	 * The expression must be at {@link org.eclipse.jem.internal.proxy.initParser.tree.ForExpression#ROOTEXPRESSION RootExpression} for release to
	 * work correctly and it must be back at RootExpression by the completion of the release call.
	 * 
	 * @param expression
	 *            the expression to use. It will be left in a valid state, even if there were errors. It if is <code>null</code> then this means the
	 *            registry is not valid and we just need to clean up any variables, but we shouldn't do proxy/expression stuff.
	 * 
	 * @since 1.1.0
	 */
	public void releaseBeanProxy(IExpression expression);

	/**
	 * Instantiate the bean proxy using the expression. Implementations should not do any proxy calls (other than to get method, field, beantype
	 * proxies). It should not expect the bean proxy to be available during the execution the method. Until the expression is invoked, nothing is
	 * available. That is why it returns an IProxy instead of an IBeanProxy, so that others can work with it in the expression.
	 * <p>
	 * If the proxy is already instantiated, it will return the true proxy.
	 * <p>
	 * If there where any errors creating the expression, the error status of the bean will be set to ERROR_SEVERE. Errors that occur in the
	 * expression while evaluating the expression should be handled by the expression creation of try/catch's. Implementers of IBeanProxyHost2 must
	 * handle such evaluation exceptions that are for instantiation (versus some error that occured while applying settings) by marking their
	 * instantiation error and then throwing a BeanInstantiationException exception (in the expression part) so that callers of instantiateBeanProxy
	 * can catch this and contain it. For example:
	 * 
	 * <pre><code>
	 * 
	 *     sample instantiationBeanProxy implementation:
	 *  
	 *  instantiateBeanProxy(IExpression) {
	 *    exp.createTry();
	 *      exp.create the instantiate expresions.
	 *    exp.createTryCatch(&quot;Exception&quot; e);
	 *      ... mark instantiation exception ...
	 *      exp.createThrow(new BeanInstantiationException());	// throw it so caller knows it did not instantiate.
	 *    exp.createTryEbd();
	 *  }
	 *  
	 *     then caller of instantiate would do:
	 *  
	 *  exp.createTry();
	 *    child.instantiateBeanProxy(exp);
	 *    apply child as setting.
	 *  exp.createTryCatch(&quot;BeanInstantiationException&quot; e);
	 *    ... mark child as invalid ...
	 *    ... don't rethrow because error was processed here, unless it is necessary because 
	 *        it can't continue with child being bad ...
	 *  exp.createTryEnd(); 
	 *  
	 * </code></pre>
	 * 
	 * <p>
	 * 
	 * @param expression
	 *            the expression to use to build up instantiation. It must be in state of
	 *            {@link org.eclipse.jem.internal.proxy.initParser.tree.ForExpression#ROOTEXPRESSION} as the next expression. It will be left in a
	 *            valid state, even if there were errors.
	 * @return an {@link IProxy} if not yet instantiated, or an {@link IBeanProxy} if already instantiated, or <code>null</code> if couldn't for
	 *         some reason.
	 * 
	 * @since 1.1.0
	 */
	public IProxy instantiateBeanProxy(IExpression expression);
}
