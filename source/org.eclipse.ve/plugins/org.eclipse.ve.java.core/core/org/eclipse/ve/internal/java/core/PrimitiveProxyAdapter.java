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
 *  $RCSfile: PrimitiveProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-12 21:44:10 $ 
 */

import java.util.*;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.util.ListenerList;

import org.eclipse.jem.internal.core.*;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaDataTypeInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * Proxy Host for Primitives (e.g. int, char, etc.).
 * Lightweight proxy host.
 * 
 * @version 	1.0
 * @author
 */
public class PrimitiveProxyAdapter extends AdapterImpl implements IBeanProxyHost {

	private IBeanProxy beanProxy; // It should be accessed only through accessors, even subclasses.
	private boolean ownsProxy;

	// The domain must be given to us so we can create instances
	protected IBeanProxyDomain domain;
	
	// Hold any exception that occurs when we try to create the BeanProxy
	protected Throwable instantiationError;
	protected ListenerList errorListeners;		

	public PrimitiveProxyAdapter(IBeanProxyDomain domain) {
		super();
		this.domain = domain;
	}
	/*
	 * @see IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		if (getBeanProxy() != null && getBeanProxy().isValid()) {
			getBeanProxy().getProxyFactoryRegistry().releaseProxy(getBeanProxy());
			// Give it a chance to clean up
		}
		beanProxy = null; // Now throw it away			
	}

	public void reinstantiateChild(IBeanProxyHost aChildHost) {
	}
	/*
	 * @see IBeanProxyHost#getBeanPropertyValue(EStructuralFeature)
	 */
	public IJavaInstance getBeanPropertyValue(EStructuralFeature aBeanPropertyFeature) {
		// Primitives have no properties.
		return null;
	}

	/*
	 * @see IBeanProxyHost#getBeanProxy()
	 */
	public IBeanProxy getBeanProxy() {
		return beanProxy;
	}

	/*
	 * @see IBeanProxyHost#getOriginalSettingsTable()
	 */
	public Map getOriginalSettingsTable() {
		return Collections.EMPTY_MAP;
	}

	/*
	 * @see IBeanProxyHost#instantiateBeanProxy()
	 */
	public IBeanProxy instantiateBeanProxy() {
		if (beanProxy == null) {
			IJavaDataTypeInstance jTarget = (IJavaDataTypeInstance) target;

			if (jTarget.isSetAllocation()) {
				JavaAllocation allocation = jTarget.getAllocation();
				IAllocationAdapter allocAdapter = (IAllocationAdapter) EcoreUtil.getRegisteredAdapter(allocation, IAllocationAdapter.class);
				if (allocAdapter != null) {
					try {
						ownsProxy = true;
						beanProxy = allocAdapter.allocate(allocation, domain);
					} catch (IAllocationAdapter.AllocationException e) {
						processInstantiationError(e);
					}
					return beanProxy;
				};
			}
			
			String qualifiedClassName = jTarget.getJavaType().getQualifiedNameForReflection();
			IBeanTypeProxy targetClass = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName);
			try {
				beanProxy = targetClass.newInstance();
				ownsProxy = true;
			} catch (ThrowableProxy exc) {
				processInstantiationError(exc);
				JavaVEPlugin.log("Could not instantiate " + qualifiedClassName, MsgLogger.LOG_WARNING); //$NON-NLS-1$
				JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			}
		}

		return beanProxy;
	}
	
	protected void processInstantiationError(Throwable exc){
		instantiationError = exc;
		fireSeverityError(IBeanProxyHost.ERROR_SEVERE);
	}
	
	protected void fireSeverityError(int severity){
		if ( errorListeners != null ) {
			Object[] listeners = errorListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				((IBeanProxyHost.ErrorListener)listeners[i]).errorStatus(severity);
			}
		}
	}		

	/*
	 * @see IBeanProxyHost#instantiateBeanProxy(IBeanProxy)
	 */
	public IBeanProxy instantiateBeanProxy(IBeanProxy proxy) {
		ownsProxy = false;
		beanProxy = proxy;
		return beanProxy;
	}

	/*
	 * @see IBeanProxyHost#isBeanProxyInstantiated()
	 */
	public boolean isBeanProxyInstantiated() {
		return beanProxy != null;
	}

	/*
	 * @see IBeanProxyHost#revalidateBeanProxy()
	 */
	public void revalidateBeanProxy() {
	}

	/*
	 * @see IBeanProxyHost#setBeanProxy(IBeanProxy)
	 */
	public void setBeanProxy(IBeanProxy beanProxy) {
		this.beanProxy = beanProxy;
		ownsProxy = false;
	}

	/*
	 * @see IBeanProxyHost#setOwnsProxy(boolean)
	 */
	public void setOwnsProxy(boolean ownsProxy) {
		this.ownsProxy = ownsProxy;
	}

	/*
	 * @see IBeanProxyHost#getBeanProxyDomain()
	 */
	public IBeanProxyDomain getBeanProxyDomain() {
		return domain;
	}

	/*
	 * @see Adapter#isAdapterForType(Object)
	 */
	public boolean isAdapterForType(Object type) {
		return BEAN_PROXY_TYPE == type || ERROR_NOTIFIER_TYPE == type || ERROR_HOLDER_TYPE == type;
	}
	
	public int getErrorStatus(){
		
		if (instantiationError != null)
			return ERROR_SEVERE;
		else
			return ERROR_NONE;
	}
	
	public List getErrors() {
		if (instantiationError != null)
			return Collections.singletonList(new IErrorNotifier.ExceptionError(instantiationError,ERROR_SEVERE));
		else
			return Collections.EMPTY_LIST;
	}

	public void addErrorListener(ErrorListener aListener) {
		if(errorListeners == null) errorListeners = new ListenerList(2);
		errorListeners.add(aListener);
	}
	
	public void removeErrorListener(ErrorListener aListener) {
		if (errorListeners != null)
			errorListeners.remove(aListener);
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#invalidateBeanProxy()
	 */
	public void invalidateBeanProxy() {
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#validateBeanProxy()
	 */
	public void validateBeanProxy() {
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#getBeanProxyValue(EStructuralFeature)
	 */
	public IBeanProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature) {
		// There are no properties of a primitive.
		return null;
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#applyBeanPropertyProxyValue(EStructuralFeature, IBeanProxy)
	 */
	public void applyBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IBeanProxy aproxy) {
		// There are no properties of a primitive.		
	}

}