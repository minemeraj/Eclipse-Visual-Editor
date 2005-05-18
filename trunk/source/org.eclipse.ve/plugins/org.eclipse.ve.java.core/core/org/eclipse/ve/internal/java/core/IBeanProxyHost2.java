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
 *  $RCSfile: IBeanProxyHost2.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-18 18:39:19 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.jem.internal.proxy.core.*;

 

/**
 * New bean proxy host interface. It is for working with expressions.
 * @since 1.1.0
 */
public interface IBeanProxyHost2 extends IBeanProxyHost {
	
	/**
	 * This is the NotificationType in a notification that is sent to all EObject's that
	 * reference the IBeanProxyHost2 through an EStructuralFeature setting. It is send
	 * whenever a bean has been released or about to be reinstantiated. It lets all references know that 
	 * there is a new bean and that they should apply the new bean. The notification
	 * will be of type {@link IBeanProxyHost2.NotificationLifeCycle}. (This should
	 * be instanceof tested to be on the safe side in case some other application decides to use this
	 * same value for their notification type).
	 * 
	 * @since 1.1.0 
	 */
	public static final int NOTIFICATION_LIFECYCLE = -3000;
	public static final String BEAN_INSTANTIATION_EXCEPTION = "org.eclipse.ve.internal.java.remotevm.BeanInstantiationException"; //$NON-NLS-1$
	
	/**
	 * Lifecycle notification implementation used by by IBeanProxyHost2. The notification is sent out to all the EMF objects
	 * that have this EMF object as a reference. It will be sent before release and after reinstantiation. 
	 * This will return true for isTouch() since it should normally be thought of as a touch.
	 * <p>
	 * Use {@link Notification#getFeature() getFeature} to get the feature that this setting is on. Use {@link Notification#getPosition() getPosition}
	 * to get the index of the setting if the feature is an isMany. Use {@link Notification#getNewValue() getNewValue()} to get the IJavaInstance
	 * that is being reinstantiated.
	 * <p>
	 * It will be sent before release of the old proxy so that any listeners can do some clean up while it
	 * is still around, if they need to. Use {@link NotificationLifeCycle#isPrerelease() isPrerelease()} to determine if this is a pre-release.
	 * <p>
	 * After reinstantiation a post-reinstantiate notice will be sent. Most listeners (the default proxy adapter does this) will take the new
	 * proxy and reapply it to their proxy so they can pick up the new proxy value.  
	 * @since 1.1.0
	 */
	public interface NotificationLifeCycle extends Notification {
		/**
		 * Get the expression that the new bean was reinstantiated under.
		 * @return the expression or <code>null</code> if the registry is invalid (usually during a release).
		 * 
		 * @since 1.1.0
		 */
		public IExpression getExpression();
		
		/**
		 * Return whether this is a pre-release notice.
		 * @return <code>true</code> if this is a pre-release notice.
		 * 
		 * @since 1.1.0
		 */
		public boolean isPrerelease();
		
		/**
		 * Return whether this is a post reinstantiation notice.
		 * @return <code>true</code> if this is a post-reinstantiate notice.
		 * 
		 * @since 1.1.0
		 */
		public boolean isPostReinstantiation();
	}
	
	/**
	 * Release the bean proxy. The expression can be used for any proxy functions that need to be done for the release. This is
	 * the preferred call to use when releasing more than one bean at a time.
	 * <p>
	 * The expression must be at {@link org.eclipse.jem.internal.proxy.initParser.tree.ForExpression#ROOTEXPRESSION RootExpression}
	 * for release to work correctly and it must be back at RootExpression by the completion of the release call.
	 * @param expression the expression to use. It will be left in a valid state, even if there were errors. It if is <code>null</code> then this
	 * means the registry is not valid and we just need to clean up any variables, but we shouldn't do proxy/expression stuff.
	 * 
	 * @since 1.1.0
	 */
	public void releaseBeanProxy(IExpression expression);

	/**
	 * Instantiate the bean proxy using the expression. Implementations should not
	 * do any proxy calls (other than to get method, field, beantype proxies). It should
	 * not expect the bean proxy to be available during the execution the method. Until
	 * the expression is invoked, nothing is available. That is why it returns an IProxy
	 * instead of an IBeanProxy, so that others can work with it in the expression.
	 * <p>
	 * If the proxy is already instantiated, it will return the true proxy.
	 * <p>
	 * If there where any errors creating the expression, the error status of the bean will be set to ERROR_SEVERE. Errors that occur in the expression while evaluating the expression should be handled
	 * by the expression creation of try/catch's. Implementers of IBeanProxyHost2 must handle such evaluation exceptions that are for instantiation (versus some
	 * error that occured while applying settings) by marking their instantiation error and then throwing a BeanInstantiationException exception (in the expression part)
	 * so that callers of instantiateBeanProxy can catch this and contain it. For example:
	 * <pre><code>
	 *    sample instantiationBeanProxy implementation:
	 * 
	 * instantiateBeanProxy(IExpression) {
	 *   exp.createTry();
	 *     exp.create the instantiate expresions.
	 *   exp.createTryCatch("Exception" e);
	 *     ... mark instantiation exception ...
	 *     exp.createThrow(new BeanInstantiationException());	// throw it so caller knows it did not instantiate.
	 *   exp.createTryEbd();
	 * }
	 * 
	 *    then caller of instantiate would do:
	 * 
	 * exp.createTry();
	 *   child.instantiateBeanProxy(exp);
	 *   apply child as setting.
	 * exp.createTryCatch("BeanInstantiationException" e);
	 *   ... mark child as invalid ...
	 *   ... don't rethrow because error was processed here, unless it is necessary because 
	 *       it can't continue with child being bad ...
	 * exp.createTryEnd(); 
	 * </code></pre> 
	 * <p>
	 * @param expression the expression to use to build up instantiation. It must be in state of {@link org.eclipse.jem.internal.proxy.initParser.tree.ForExpression#ROOTEXPRESSION} as the next expression.
	 * It will be left in a valid state, even if there were errors.
	 * @return an {@link IProxy} if not yet instantiated, or an {@link IBeanProxy} if already instantiated, or <code>null</code> if couldn't for some reason.
	 * 
	 * @since 1.1.0
	 */
	public IProxy instantiateBeanProxy(IExpression expression);
}
