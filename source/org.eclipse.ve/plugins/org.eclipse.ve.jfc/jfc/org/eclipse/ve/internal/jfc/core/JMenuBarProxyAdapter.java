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
 *  $RCSfile: JMenuBarProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-22 21:05:25 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

import org.eclipse.ve.internal.java.core.*;

/**
 * @author pwalker
 * 
 * Proxy adapter for JMenuBar. Adds and removes JMenus.
 */
public class JMenuBarProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfMenus;

	/**
	 * Constructor for JMenuBarProxyAdapter.
	 * 
	 * @param domain
	 */
	public JMenuBarProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfMenus = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JMENUBAR_MENUS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean,
	 *      org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void applied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sfMenus) {
			if (isTouch)
				return; // Don't want to apply if all we did was touch.
		}
		super.applied(feature, value, index, isTouch, expression, testValidity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applySetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sfMenus)
			addComponent((EObject) value, index, expression);
		else
			super.applySetting(feature, value, index, expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#cancelSetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		if (feature == sfMenus) {
			removeComponent((EObject) oldValue, expression);
		}
		super.cancelSetting(feature, oldValue, index, expression);
	}

	private void addComponent(EObject aComponent, int position, IExpression expression) {
		IJavaInstance component = (IJavaInstance) aComponent;
		IInternalBeanProxyHost componentProxyHost = getSettingBeanProxyHost(component);
		IProxy componentProxy = instantiateSettingBean(componentProxyHost, expression, sfMenus, aComponent);
		if (componentProxy == null)
			return; // It failed creation, don't go any further.

		IProxy beforeBeanProxy; // The beanproxy to go before, if any.
		if (position != Notification.NO_INDEX)
			beforeBeanProxy = getProxyAt(position + 1, sfMenus); // Need to do +1 because we (componentBeanProxy) are already at that position in the EMF list.
		// So we want to go before next guy.
		else
			beforeBeanProxy = null;

		BeanAwtUtilities.invoke_addComponent(getProxy(), componentProxy, beforeBeanProxy, false, expression);
	}

	/*
	 * Remove the component from the container on the vm. @param aComponent @param expression
	 * 
	 * @since 1.1.0
	 */
	private void removeComponent(EObject aComponent, IExpression expression) {
		ComponentProxyAdapter componentProxyHost = (ComponentProxyAdapter) getSettingBeanProxyHost((IJavaInstance) aComponent);
		// Note: We shouldn't be called during an instantiation of any kind, so we should have a straight instantiated bean.
		if (componentProxyHost != null && componentProxyHost.isBeanProxyInstantiated()) {
			BeanAwtUtilities.invoke_removeComponent(getProxy(), componentProxyHost.getBeanProxy(), expression);
		}
	}

}