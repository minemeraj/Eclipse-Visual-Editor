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
 *  $RCSfile: JMenuProxyAdapter.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:38:10 $ 
 */

package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.*;

/**
 * @author pwalker
 * 
 * Proxy adapter for JMenu. The correct structural feature is retrieved based on "items" SF for the specific type. see getSFItems(). This is a
 * superclass for JPopupMenuProxyAdapter, so need to make sure nothing breaks that one.
 *  
 */
public class JMenuProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfItems;

	/**
	 * Constructor for JMenuProxyAdapter.
	 * 
	 * @param domain
	 */
	public JMenuProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean,
	 *      org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void applied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == getSFItems()) {
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
		if (feature == getSFItems())
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
		if (feature == getSFItems()) {
			removeComponent((EObject) oldValue, expression);
		} else
			super.cancelSetting(feature, oldValue, index, expression);
	}

	private void addComponent(EObject aComponent, int position, IExpression expression) {
		IJavaInstance component = (IJavaInstance) aComponent;
		IInternalBeanProxyHost componentProxyHost = getSettingBeanProxyHost(component);
		IProxy componentProxy = instantiateSettingBean(componentProxyHost, expression, getSFItems(), aComponent);
		if (componentProxy == null)
			return; // It failed creation, don't go any further.

		IProxy beforeBeanProxy; // The beanproxy to go before, if any.
		if (position != Notification.NO_INDEX) {
			beforeBeanProxy = getProxyAt(position + 1, getSFItems()); 
			// Need to do +1 because we (componentBeanProxy) are already at that position in the EMF list.
			// So we want to go before next guy.
		} else
			beforeBeanProxy = null;

		BeanAwtUtilities.invoke_JMenu_addComponent(getProxy(), componentProxy, beforeBeanProxy, expression);
	}

	/*
	 * Return the "items" structural feature for this component which could be a JMenu or JPopupMenu. Can't use hardcoded "items" url because it is
	 * physically a different feature between JMenu and JPopupMenu.
	 */
	protected EStructuralFeature getSFItems() {
		if (sfItems == null) {
			JavaClass modelType = (JavaClass) ((EObject) getTarget()).eClass();
			sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
		}
		return sfItems;
	}

	/*
	 * Remove the component. @param aComponent @param expression
	 * 
	 * @since 1.1.0
	 */
	private void removeComponent(EObject aComponent, IExpression expression) {
		IBeanProxyHost componentProxyHost = getSettingBeanProxyHost((IJavaInstance) aComponent);
		// Note: We shouldn't be called during an instantiation of any kind, so we should have a straight instantiated bean.
		if (componentProxyHost != null && componentProxyHost.isBeanProxyInstantiated()) {
			BeanAwtUtilities.invoke_JMenu_removeComponent(getProxy(), componentProxyHost.getBeanProxy(), expression);
		}
	}
}
