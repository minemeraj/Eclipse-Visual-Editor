/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JSplitPaneProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:48 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

public class JSplitPaneProxyAdapter extends ContainerProxyAdapter {

	protected EStructuralFeature sfLeftComponent, sfRightComponent, sfTopComponent, sfBottomComponent, sfDividerLocation;
	protected JSplitPaneManager splitPaneManager;
	protected boolean dividerSet;

	public JSplitPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfLeftComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT);
		sfRightComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT);
		sfBottomComponent =
			JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		sfTopComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT);
		sfDividerLocation = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_DIVIDERLOCATION);	
		
		// Real kludge, we need to force the default divider location to -1. If we don't then when we reset
		// it, it will use the bogus 60 that comes up after construction. This guy gets there because the
		// default buttons that splitpane inserts under the covers forces the divider to have an initial location
		// immediately after construction. And that is the value we see. Not the true default of -1.
		getOriginalSettingsTable().put(sfDividerLocation, domain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-1)		);
			
	}
	
	protected void applied(EStructuralFeature as, Object newValue, int position) {
		super.applied(as, newValue, position);

		if ((as == sfLeftComponent
			|| as == sfRightComponent
			|| as == sfTopComponent
			|| as == sfBottomComponent) &&
			newValue != null) {
			IBeanProxyHost componentProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) newValue);
			IComponentProxyHost componentAdapter = (IComponentProxyHost) componentProxyHost;
			// Now that we've added it, set the parent component.
			componentAdapter.setParentComponentProxyHost((IComponentProxyHost) this);
		}
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#setupBeanProxy(IBeanProxy)
	 */
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		super.setupBeanProxy(beanProxy);
		
		if (beanProxy != null) {
			if (splitPaneManager == null)
				splitPaneManager = new JSplitPaneManager();
			
			splitPaneManager.setJSplitPaneBeanProxy(this, beanProxy);
		}
		
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
		
		if (splitPaneManager != null) {
			splitPaneManager.dispose();
			splitPaneManager = null;
		}			
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applyBeanFeature(EStructuralFeature, PropertyDecorator, BeanFeatureDecorator, IBeanProxy)
	 */
	protected void primApplyBeanFeature(
		EStructuralFeature sf,
		PropertyDecorator propDecor,
		BeanFeatureDecorator featureDecor,
		IBeanProxy settingBeanProxy) throws ThrowableProxy {
		// If it is divider, then set through the manager. 
		if (sf != sfDividerLocation || splitPaneManager == null) 
			super.primApplyBeanFeature(sf, propDecor, featureDecor, settingBeanProxy);
		else {
			splitPaneManager.setDividerLocation(settingBeanProxy);
			dividerSet = ((IIntegerBeanProxy) settingBeanProxy).intValue() != -1;
		}
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#childInvalidated(IComponentProxyHost)
	 */
	public void childInvalidated(IComponentProxyHost childProxy) {
		if (!dividerSet && splitPaneManager != null)
			splitPaneManager.resetToPreferredSizes();
		super.childInvalidated(childProxy);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if ((sf == sfLeftComponent
			|| sf == sfRightComponent
			|| sf == sfTopComponent
			|| sf == sfBottomComponent) &&
			oldValue != null) {
			IBeanProxyHost componentProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) oldValue);
			IComponentProxyHost componentAdapter = (IComponentProxyHost) componentProxyHost;
			// Now that we've added it, set the parent component.
			componentAdapter.setParentComponentProxyHost(null);
			super.canceled(sf, oldValue, position);
			componentProxyHost.releaseBeanProxy();	// Need to release it because stnd canceled won't
		}
		super.canceled(sf, oldValue, position);
	}

}
