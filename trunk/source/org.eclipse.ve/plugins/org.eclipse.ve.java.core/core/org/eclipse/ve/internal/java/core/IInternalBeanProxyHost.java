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
 *  $RCSfile: IInternalBeanProxyHost.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-22 21:05:23 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
 

/**
 * This is an internal internal to be used only by the ones that really need it.
 * They are delicate and can break the bean proxy host if not used correctly.
 * @since 1.1.0
 */
public interface IInternalBeanProxyHost extends IBeanProxyHost {

	/**
	 * Lifecycle notification implementation used by by IBeanProxyHost2. The notification is sent out to all the EMF objects that have this EMF object
	 * as a reference. It will be sent before release and after reinstantiation. This will return true for isTouch() since it should normally be
	 * thought of as a touch.
	 * <p>
	 * Use {@link Notification#getFeature() getFeature} to get the feature that this setting is on. Use {@link Notification#getPosition() getPosition}
	 * to get the index of the setting if the feature is an isMany. Use {@link Notification#getNewValue() getNewValue()} to get the IJavaInstance that
	 * is being reinstantiated.
	 * <p>
	 * It will be sent before release of the old proxy so that any listeners can do some clean up while it is still around, if they need to. Use
	 * {@link NotificationLifeCycle#isPrerelease() isPrerelease()} to determine if this is a pre-release.
	 * <p>
	 * After reinstantiation a post-reinstantiate notice will be sent. Most listeners (the default proxy adapter does this) will take the new proxy
	 * and reapply it to their proxy so they can pick up the new proxy value.
	 * 
	 * @since 1.1.0
	 */
	public interface NotificationLifeCycle extends Notification {
	
		/**
		 * Get the expression that the new bean was reinstantiated under.
		 * 
		 * @return the expression or <code>null</code> if the registry is invalid (usually during a release).
		 * 
		 * @since 1.1.0
		 */
		public IExpression getExpression();
	
		/**
		 * Return whether this is a pre-release notice.
		 * 
		 * @return <code>true</code> if this is a pre-release notice.
		 * 
		 * @since 1.1.0
		 */
		public boolean isPrerelease();
	
		/**
		 * Return whether this is a post reinstantiation notice.
		 * 
		 * @return <code>true</code> if this is a post-reinstantiate notice.
		 * 
		 * @since 1.1.0
		 */
		public boolean isPostReinstantiation();
	}


	/**
	 * This is the NotificationType in a notification that is sent to all EObject's that reference the IBeanProxyHost through an EStructuralFeature
	 * setting. It is send whenever a bean has been released or about to be reinstantiated. It lets all references know that there is a new bean and
	 * that they should apply the new bean. The notification will be of type {@link IInternalBeanProxyHost.NotificationLifeCycle}. (This should be instanceof
	 * tested to be on the safe side in case some other application decides to use this same value for their notification type).
	 * 
	 * @since 1.1.0
	 */
	public static final int NOTIFICATION_LIFECYCLE = -3000;


	/**
	 * Return the bean Original Settings Hashtable.
	 * <p>
	 * Warning: This should not normally be called.
	 * It is here so that bean customizer support can
	 * access it.

	 * @return map of features mapped to IBeanProxy original value.
	 * 
	 * @since 1.1.0
	 */
	public Map getOriginalSettingsTable();
	
	/**
	 * Return the proxy for the bean proxy. It could be either an IBeanProxy if already instantiated or an ExpressionProxy if in instantiation.
	 * It is here for cross-package, but basically internal usage.
	 * 
	 * @return the proxy or <code>null</code> if not instantiated or being instantiated.
	 * 
	 * @since 1.1.0
	 */
	public IProxy getProxy();

	/**
	 * Return whether the original settings table already has the feature set.
	 * @param feature
	 * @return <code>true</code> if the original setting has already been set.
	 * 
	 * @since 1.1.0
	 */
	public boolean isSettingInOriginalSettingsTable(EStructuralFeature feature);

	/**
	 * Apply the BeanProxy directly as a property. In general it will not go through
	 * specializations that normal applies through the IJavaInstance would of gone through.
	 * It will typically do only the straight apply that the property decorator for the
	 * feature indicates should be done.
	 * <p>
	 * Warning: This should not normally be called.
	 * It is here so that the bean customizer support
	 * can access it. This will not save original values,
	 * or do any testing of validity.

	 * @param aBeanPropertyFeature
	 * @param aproxy
	 * 
	 * @since 1.1.0
	 */
	public void applyBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IBeanProxy aproxy);

	/**
	 * Get the property value as a expression proxy. It is here only for implicit allocations. It needs to get the value at instantiation through
	 * an expression.
	 * @param aBeanPropertyFeature
	 * @param expression
	 * @param forExpression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IExpression expression, ForExpression forExpression);

	/**
	 * Set a bean proxy into this adaptor, if not already set.
	 * <p>
	 * Warning: This is a dangerous method because it could
	 * set a proxy that is not consistent with the target MOF object.
	 * 
	 * @see IInternalBeanProxyHost#setOwnsProxy(boolean) for further important information.
	 * @param beanProxy
	 * 
	 * @since 1.1.0
	 */
	public void setBeanProxy(IBeanProxy beanProxy);

	/**
	 * Set whether this proxy host owns the proxy. If it owns the proxy, that
	 * means when the this host goes away, (either through GC or was removed from
	 * an IJavaInstance), it should release the proxy from the remote vm.
	 * If it doesn't own it, then that means some other host may own the
	 * proxy and they will control releasing it.
	 * <p>
	 * By default, if the proxy was created through instantiate() (no parms),
	 * then the value will be true, this owns it. If set through setBeanProxy
	 * or through instantiate(IBeanProxy), then the value will be set to false.
	 * <p>
	 * This is a very dangerous setting, but is needed because sometimes we need
	 * to create a host for an existing proxy. Sometimes the host will own it,
	 * such as the value of property sheet entry, other times it won't, such as
	 * an implicit setting. In the case of the implicit setting, if no one else
	 * owns it then the proxy will be GC'd and cleaned up ok.
	 * @param ownsProxy
	 * 
	 * @since 1.1.0
	 */
	public void setOwnsProxy(boolean ownsProxy);
	
	/**
	 * Return whether in instantiation or not.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean inInstantiation();
	
	/**
	 * Answers whether there were any instantiation errors or not.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean hasInstantiationErrors();

	
	/**
	 * Return the instantiation errors. There can be more than one.
	 * @return the instantiation errors if there are any or <code>null</code> if no instantiation error.
	 * 
	 * @since 1.1.0
	 */
	public List getInstantiationError();
	

	/**
	 * Being added to the FreeForm. The bean should save this indication and do what it needs to do on the next instantiation
	 * to be on the freeform. 
	 * <p>
	 * This is called whenever the bean is being added to the freeform (either "components" or "thisPart").
	 * At the time it is called the bean should of been released. Before being called the bean will be released. And then after the
	 * call the bean will be instantiated. 
	 * 
	 * @param compositionAdapter the proxy adapter for the composition. This allows them to get a hold of the appropriate freeform hosts.
	 * 
	 * @since 1.1.0
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter);
	
	/**
	 * Being removed from the FreeForm. The bean should remove the indication. The bean will alwayb have been released before the remove is
	 * called. 
	 * 
	 * @see IInternalBeanProxyHost2#addToFreeForm(CompositionProxyAdapter)
	 * @since 1.1.0
	 */
	public void removeFromFreeForm();
	
}
