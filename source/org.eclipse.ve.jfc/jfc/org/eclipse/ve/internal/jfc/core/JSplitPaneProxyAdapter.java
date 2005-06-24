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
/*
 *  $RCSfile: JSplitPaneProxyAdapter.java,v $
 *  $Revision: 1.9 $  $Date: 2005-06-24 16:45:10 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * JSplitPane Proxy Adapter.
 * 
 * @since 1.1.0
 */
public class JSplitPaneProxyAdapter extends ContainerProxyAdapter {

	protected EStructuralFeature sfLeftComponent, sfRightComponent, sfTopComponent, sfBottomComponent, sfDividerLocation;

	protected JSplitPaneManagerExtension splitPaneManager;

	protected boolean dividerSet;

	public JSplitPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfLeftComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT);
		sfRightComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT);
		sfBottomComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		sfTopComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT);
		sfDividerLocation = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_DIVIDERLOCATION);

		// KLUDGE Real kludge, we need to force the default divider location to -1. If we don't then when we reset
		// it, it will use the bogus 60 that comes up after construction. This guy gets there because the
		// default buttons that splitpane inserts under the covers forces the divider to have an initial location
		// immediately after construction. And that is the value we see. Not the true default of -1.
		setOriginalValue(sfDividerLocation, domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-1));

	}

	protected ComponentManager createComponentManager() {
		ComponentManager cm = super.createComponentManager();
		splitPaneManager = new JSplitPaneManagerExtension();
		cm.addComponentExtension(splitPaneManager, null);
		return cm;
	}

	protected IProxy applyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue)
			throws NoSuchMethodException, NoSuchFieldException {
		if (propertyDecorator.getEModelElement() == sfDividerLocation)
			return splitPaneManager.setDividerLocation(settingProxy, getOriginalValue, expression);
		else
			return super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}
}
