/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: PrimitiveProxyAdapter.java,v $
 *  $Revision: 1.13 $  $Date: 2005-08-24 20:29:12 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.util.ListenerList;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaDataTypeInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * Proxy Host for Primitives (e.g. int, char, etc.).
 * Lightweight proxy host.
 * 
 * @version 	1.0
 * @author
 */
public class PrimitiveProxyAdapter extends AdapterImpl implements IInternalBeanProxyHost {

	private IProxy beanProxy; // It should be accessed only through accessors, even subclasses.
	private boolean ownsProxy;

	// The domain must be given to us so we can create instances
	protected IBeanProxyDomain domain;
	
	// Hold any exception that occurs when we try to create the BeanProxy
	protected ErrorType instantiationError;
	protected ListenerList errorListeners;		

	public PrimitiveProxyAdapter(IBeanProxyDomain domain) {
		super();
		this.domain = domain;
	}
	/*
	 * @see IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		if (ownsProxy && getBeanProxy() != null && getBeanProxy().isValid()) {
			getBeanProxy().getProxyFactoryRegistry().releaseProxy(getBeanProxy());
			// Give it a chance to clean up
		}
		beanProxy = null; // Now throw it away			
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2#releaseBeanProxy(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public void releaseBeanProxy(IExpression expression) {
		releaseBeanProxy();	// Being in an expression is not of much use to primitives for releasing.
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
		return (IBeanProxy) (beanProxy != null && beanProxy.isBeanProxy() ? beanProxy : null);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#getProxy()
	 */
	public IProxy getProxy() {
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
			instantiationError = null;
			fireSeverityError(ERROR_NONE);
			IJavaDataTypeInstance jTarget = (IJavaDataTypeInstance) target;

			if (jTarget.isSetAllocation()) {
				JavaAllocation allocation = jTarget.getAllocation();
				ownsProxy = true;
				try {
					beanProxy = getBeanProxyDomain().getAllocationProcesser().allocate(allocation);
				} catch (IAllocationProcesser.AllocationException e) {
					processInstantiationError(new ExceptionError(e.getCause(), ERROR_INFO));
				}
				return (IBeanProxy) beanProxy;
			}
			
			String qualifiedClassName = jTarget.getJavaType().getQualifiedNameForReflection();
			IBeanTypeProxy targetClass = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName);
			try {
				beanProxy = targetClass.newInstance();
				ownsProxy = true;
			} catch (ThrowableProxy exc) {
				processInstantiationError(new BeanExceptionError(exc, ERROR_SEVERE));
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
					JavaVEPlugin.log("Could not instantiate " + qualifiedClassName, Level.WARNING); //$NON-NLS-1$
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			}
			return (IBeanProxy) beanProxy;
		} else if (beanProxy.isExpressionProxy())
			return null;	// We are evaluating, can't return anything while doing that.
		else
			return (IBeanProxy) beanProxy;
	}
	
	protected void processInstantiationError(ErrorType error){
		instantiationError = error;
		fireSeverityError(error.getSeverity());
	}
	
	public boolean hasInstantiationErrors() {
		return instantiationError != null;
	}
	
	protected void fireSeverityError(int severity){
		if ( errorListeners != null ) {
			Object[] listeners = errorListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				((IBeanProxyHost.ErrorListener)listeners[i]).errorStatusChanged();
			}
		}
	}		

	/*
	 * @see IBeanProxyHost#instantiateBeanProxy(IBeanProxy)
	 */
	public IBeanProxy instantiateBeanProxy(IBeanProxy proxy) {
		ownsProxy = false;
		beanProxy = proxy;
		instantiationError = null;
		fireSeverityError(ERROR_NONE);
		return (IBeanProxy) beanProxy;
	}

	/*
	 * @see IBeanProxyHost#isBeanProxyInstantiated()
	 */
	public boolean isBeanProxyInstantiated() {
		return beanProxy != null && beanProxy.isBeanProxy();
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
			return instantiationError.getSeverity();
		else
			return ERROR_NONE;
	}
	
	public List getErrors() {
		if (instantiationError != null)
			return Collections.singletonList(instantiationError);
		else
			return Collections.EMPTY_LIST;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#getInstantiationError()
	 */
	public List getInstantiationError() {
		return getErrors();
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
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2#instantiateBeanProxy(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public IProxy instantiateBeanProxy(IExpression expression) {
		IJavaDataTypeInstance jTarget = (IJavaDataTypeInstance) target;
		instantiationError = null;
		ownsProxy = true;
		fireSeverityError(ERROR_NONE);
		if (jTarget.isSetAllocation()) {
			JavaAllocation allocation = jTarget.getAllocation();
			try {
				beanProxy = getBeanProxyDomain().getAllocationProcesser().allocate(allocation, expression);
			} catch (AllocationException e) {
				processInstantiationError(new ExceptionError(e.getCause(), ERROR_INFO));
				JavaVEPlugin.log(e, Level.WARNING);
				return null;
			}
		} else {
			// otherwise just create it using the default value for it.
			beanProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			switch (jTarget.getJavaType().getPrimitiveID()) {
				case JavaHelpers.PRIM_BOOLEAN_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, false);
					break;
				case JavaHelpers.PRIM_BYTE_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, (byte) 0);
					break;
				case JavaHelpers.PRIM_CHARACTER_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, (char) 0);
					break;
				case JavaHelpers.PRIM_SHORT_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, (short) 0);
					break;
				case JavaHelpers.PRIM_INTEGER_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, 0);
					break;
				case JavaHelpers.PRIM_LONG_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, 0l);
					break;
				case JavaHelpers.PRIM_FLOAT_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, 0f);
					break;
				case JavaHelpers.PRIM_DOUBLE_ID:
					expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, 0d);
					break;
			}
		}
		if (beanProxy.isExpressionProxy()) {
			((ExpressionProxy) beanProxy).addProxyListener(new ExpressionProxy.ProxyListener() {
				public void proxyResolved(ProxyEvent event) {
					beanProxy = event.getProxy();	// We have it! So save it.
				}

				public void proxyNotResolved(ProxyEvent event) {
					beanProxy = null;
				}

				public void proxyVoid(ProxyEvent event) {
					beanProxy = null;
				}
				
			});
		}

		return beanProxy;
	
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#getBeanPropertyProxyValue(org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.jem.internal.proxy.core.IExpression, org.eclipse.jem.internal.proxy.initParser.tree.ForExpression)
	 */
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IExpression expression, ForExpression forExpression) {
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#addToFreeForm(org.eclipse.ve.internal.java.core.CompositionProxyAdapter)
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#removeFromFreeForm()
	 */
	public void removeFromFreeForm() {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#isSettingInOriginalSettingsTable(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean isSettingInOriginalSettingsTable(EStructuralFeature feature) {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#inInstantiation()
	 */
	public boolean inInstantiation() {
		return beanProxy != null && beanProxy.isExpressionProxy();
	}

}
