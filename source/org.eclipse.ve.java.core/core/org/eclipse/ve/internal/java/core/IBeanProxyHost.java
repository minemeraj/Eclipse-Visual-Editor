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
 *  $Revision: 1.3 $  $Date: 2005-03-28 22:15:55 $ 
 */

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

/**
 * This is the adapter used to access/hold the bean associated
 * with a MOF JavaBean.
 *
 * Note: It is important that any implementers of this interface
 * MUST have a constructor that takes one argument of type IBeanProxyDomain.
 * It won't be constructed otherwise.
 *
 * Creation date: (1/10/00 12:01:54 PM)
 * @author: Richard Lee Kulp
 */
public interface IBeanProxyHost extends Adapter, IErrorNotifier {
	public final static Class BEAN_PROXY_TYPE = IBeanProxyHost.class;

/**
 * This method is called if we want the bean proxy to be
 * released. This would be used to recreate the bean proxy
 * if necessary.
 * Creation date: (1/27/00 10:06:46 AM)
 */
void releaseBeanProxy();
/* Get the attribute value from the bean.
 * The value returned WILL NOT be contained in any EMF document,
 *
 * NOTE: Any implementers of this method must
 * set the implicit property to true on any IBean that
 * it returns. This is a required function for the VCE
 * to work correctly.
 */
public IJavaInstance getBeanPropertyValue(EStructuralFeature aBeanPropertyFeature);
/* Get the attribute value as a beanproxy from the bean.
 */
public IBeanProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature);
/* Return the bean proxy object itself
 */
public IBeanProxy getBeanProxy();
/**
 * Return the bean Original Settings Hashtable.
 *
 * Warning: This should not normally be called.
 * It is here so that bean customizer support can
 * access it.
 */
public Map getOriginalSettingsTable();
/**
 * Apply the BeanProxy directly as a property.
 * 
 * Warning: This should not normally be called.
 * It is here so that the bean customizer support
 * can access it. This will not save original values,
 * or do any testing of validity.
 */
public void applyBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IBeanProxy aproxy);
/**
 * Instantiate the bean proxy, if not already instantiated.
 */
public IBeanProxy instantiateBeanProxy(); 
/**
 * Instantiate using the given bean proxy if not already instantiated.
 *
 * NOTE: This should normally not be called. It is here
 * so that "implicit" property support works correctly.
 *
 * Warning: See setOwnsProxy for further important information. 
 */
public IBeanProxy instantiateBeanProxy(IBeanProxy proxy);
/* Answer whether of not the bean proxy has been instantiated
 */
public boolean isBeanProxyInstantiated();

/**
 * Allow the bean proxy to be put back into a valid state if required
 * An example is where the bean was changed by a customizer.  This method will be called if the customizer
 * fires a propertyChangeEvent signalling the VCE to refresh itself. This method is also called
 * whenever any property has been set.
 * 
 * This is implemented as a call to invalidateBeanProxy followed by a call to validateBeanProxy.
 * 
 * NOTE that customizer hosting is quite involved as the customizer changes the bean
 * and the VCE needs to work out what attributes have changed so the MOF model is updated
 * and code generation works correctly.  Look at BeanCustomizer and the different
 * customizers for the example classes Area and ButtonBar as examples
 */
public void revalidateBeanProxy();

/**
 * Invalidate the bean proxy. Typically called by revalidateBeanProxy.
 */
public void invalidateBeanProxy();

/**
 * Validate the bean proxy. Typically called by revalidateBeanProxy.
 */
public void validateBeanProxy();


/**
 * Set a bean proxy into this adaptor, if not already set.
 * Warning: This is a dangerous method because it could
 * set a proxy that is not consistent with the target MOF object.
 *
 * Warning: See setOwnsProxy for further important information.
 */
public void setBeanProxy(IBeanProxy beanProxy);
/**
 * Set whether this proxy host owns the proxy. If it owns the proxy, that
 * means when the this host goes away, (either through GC or was removed from
 * an IJavaInstance), it should release the proxy from the remote vm.
 * If it doesn't own it, then that means some other host may own the
 * proxy and they will control releasing it.
 *
 * By default, if the proxy was created through instantiate() (no parms),
 * then the value will be true, this owns it. If set through setBeanProxy
 * or through instantiate(IBeanProxy), then the value will be set to false.
 *
 * This is a very dangerous setting, but is needed because sometimes we need
 * to create a host for an existing proxy. Sometimes the host will own it,
 * such as the value of property sheet entry, other times it won't, such as
 * an implicit setting. In the case of the implicit setting, if no one else
 * owns it then the proxy will be GC'd and cleaned up ok.
 */
public void setOwnsProxy(boolean ownsProxy);

/**
 * Return IBeanProxyDomain for this BeanProxyHost.
 */
public IBeanProxyDomain getBeanProxyDomain();
/**
 * A child wishes to be re-created. It determined that it can't do it itself.
 */
public void reinstantiateChild(IBeanProxyHost aChildProxyHost);

int getErrorStatus();

public List getErrors();

}
