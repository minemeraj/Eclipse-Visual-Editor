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
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:20 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;
 

/**
 * This is an internal internal to be used only by the ones that really need it.
 * They are delicate and can break the bean proxy host if not used correctly.
 * @since 1.1.0
 */
public interface IInternalBeanProxyHost extends IBeanProxyHost {

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
	 * Return the instantiation errors. There can be more than one.
	 * @return the instantiation errors if there are any or <code>null</code> if no instantiation error.
	 * 
	 * @since 1.1.0
	 */
	public List getInstantiationError();
}
