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
package org.eclipse.ve.internal.java.core;

/*
 *  $RCSfile: BeanProxyAdapterFactory.java,v $
 *  $Revision: 1.12 $  $Date: 2005-08-24 23:30:45 $ 
 */
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;

import org.eclipse.ve.internal.jcm.BeanDecorator;
import org.eclipse.ve.internal.jcm.JCMPackage;

/**
 * Factory for creating the BeanProxyAdaptor.
 * Creation date: (1/10/00 12:28:38 PM)
 * @author: Richard Lee Kulp
 */
public class BeanProxyAdapterFactory
	extends AdapterFactoryImpl
	implements IBeanProxyDomain {

	protected ProxyFactoryRegistry fProxyFactoryRegistry;
	protected EditDomain domain;
	protected IAllocationProcesser allocationProcesser;
	protected String thisTypeName;
	protected static EAttribute BEAN_PROXY_CLASS_NAME;

	/**
	 * Create a BeanProxyAdapterFactory (which is also an IBeanProxyDomain.
	 * 
	 * @param aProxyFactoryRegistry
	 * @param domain
	 * @param allocationProcesser The allocation processer to be owned by this domain. It will have this factory set as its IBeanProxyDomain.
	 * 
	 * @since 1.0.0
	 */
	/**
	 * BeanProxyAdaptorFactory constructor comment.
	 */
	public BeanProxyAdapterFactory(ProxyFactoryRegistry aProxyFactoryRegistry, EditDomain domain, IAllocationProcesser allocationProcesser) {
		super();
		fProxyFactoryRegistry = aProxyFactoryRegistry;
		this.domain = domain;
		this.allocationProcesser = allocationProcesser;
		allocationProcesser.setBeanProxyDomain(this);
		if (domain != null)
			JavaEditDomainHelper.setBeanProxyDomain(this, domain);	// May not be in an editdomain, used when just doing JBCF without editing.
	}

	public ProxyFactoryRegistry getProxyFactoryRegistry() {
		return fProxyFactoryRegistry;
	}
	
	public void setProxyFactoryRegistry(ProxyFactoryRegistry registry) {
		fProxyFactoryRegistry = registry;
	}

	public EditDomain getEditDomain() {
		return domain;
	}

	protected static EAttribute getBeanProxyClassNameFeature() {
		// Lazy initialize the meta class for the BeanDecorator
		if (BEAN_PROXY_CLASS_NAME == null) {
			BEAN_PROXY_CLASS_NAME = JCMPackage.eINSTANCE.getBeanDecorator_BeanProxyClassName();
		}
		return BEAN_PROXY_CLASS_NAME;
	}
	/**
	 * Adapt us based upon whether we have an attribute for "beanProxyAdaptor"
	 * Otherwise use a default adaptor
	 */
	protected Adapter createAdapter(Notifier adaptable) {
		if (!(adaptable instanceof IJavaInstance))
			return null; // Only Beans can be adapted to a beanProxyAdaptor.

		IJavaInstance bean = (IJavaInstance) adaptable;
		// The class of adapter to use comes from the BeanDecorator.  Get the one that has the property set
		BeanDecorator decr =
			(BeanDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				bean.getJavaType(),
				BeanDecorator.class,
				getBeanProxyClassNameFeature());
		
		if(decr == null){
			return new BeanProxyAdapter(this);
		} else {
			return decr.createBeanProxy(adaptable, this);
		}
	}

	public boolean isFactoryForType(Object key) {
		return IBeanProxyHost.BEAN_PROXY_TYPE.equals(key);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyDomain#getAllocationProcesser()
	 */
	public IAllocationProcesser getAllocationProcesser() {
		return allocationProcesser; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyDomain#getThisType()
	 */
	public IBeanTypeProxy getThisType() {
		return thisTypeName != null && getProxyFactoryRegistry() != null ? getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(thisTypeName) : null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyDomain#setThisTypeName(java.lang.String)
	 */
	public void setThisTypeName(String name) {
		this.thisTypeName = name;
	}
}
