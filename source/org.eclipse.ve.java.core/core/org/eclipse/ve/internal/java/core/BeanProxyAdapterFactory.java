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
 *  $RCSfile: BeanProxyAdapterFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2004-02-20 00:44:29 $ 
 */
import java.lang.reflect.Constructor;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
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
		Adapter adapter = null;

		IJavaInstance bean = (IJavaInstance) adaptable;
		// The class of adapter to use comes from the BeanDecorator.  Get the one that has the property set
		BeanDecorator decr =
			(BeanDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				(EClassifier) bean.getJavaType(),
				BeanDecorator.class,
				getBeanProxyClassNameFeature());
		String adapterClassName = decr != null ? decr.getBeanProxyClassName() : null;

		if (adapterClassName != null) {
			Class adapterClass = null;
			try {
				// If the class is not the default we need to load it using the correct
				// class lodaed.  CDEPlugin can do this for us but if the name is BeanProxyAdapter or PrimitiveBeanProxyAdapter we can
				// just get the default class faster with a .class reference here
				if (adapterClassName.equals("org.eclipse.ve.java.core/org.eclipse.ve.internal.java.core.BeanProxyAdapter")) //$NON-NLS-1$
					adapter = new BeanProxyAdapter(this);
				else if (adapterClassName.equals("org.eclipse.ve.java.core/org.eclipse.ve.internal.java.core.PrimitiveProxyAdapter")) //$NON-NLS-1$
					adapter = new PrimitiveProxyAdapter(this);
				else
					adapterClass = CDEPlugin.getClassFromString(adapterClassName);
			} catch (ClassNotFoundException e) {
				adapterClass = null;
				JavaVEPlugin.log(e, Level.WARNING);
			}
			if (adapterClass != null)
				try {
					// There must be a constructor that takes an argument with the IBeanProxyDomain.
					Constructor constructor = adapterClass.getConstructor(new Class[] { IBeanProxyDomain.class });
					adapter = (Adapter) constructor.newInstance(new Object[] { this });
				} catch (Exception e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
		}

		return adapter != null ? adapter : new BeanProxyAdapter(this);
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

}