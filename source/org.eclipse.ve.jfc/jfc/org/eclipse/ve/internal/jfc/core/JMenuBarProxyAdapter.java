package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JMenuBarProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;

/**
 * @author pwalker
 *
 * Proxy adapter for JMenuBar. Adds and removes JMenus.
 */
public class JMenuBarProxyAdapter extends ComponentProxyAdapter {
	protected EStructuralFeature sfMenus;

	/**
	 * Constructor for JMenuBarProxyAdapter.
	 * @param domain
	 */
	public JMenuBarProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfMenus = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JMENUBAR_MENUS);
	}
	
	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == sfMenus) {
			addComponent((EObject) newValue, position);
		} else {
			super.applied(as, newValue, position);
		}
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == sfMenus) {
			removeComponent((EObject) oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}
	private void addComponent(EObject aComponent,int position) {
		IBeanProxyHost componentProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) aComponent);
		// Ensure the bean is instantiated
		IComponentProxyHost componentAdapter = (IComponentProxyHost) componentProxyHost;
		componentProxyHost.instantiateBeanProxy();
		// It is possible the component didn't instantiate.  We then can't apply it
		// and we should flag it as having a warning error
		if (componentProxyHost.getErrorStatus() == IBeanProxyHost.ERROR_SEVERE || getErrorStatus() == IBeanProxyHost.ERROR_SEVERE) {
			// Either we or the setting could not be instantiated, so don't add.
			// Need to get the ability to add an error for a structural feature against a single attribute setting
			return;
		}
		IBeanProxy componentBeanProxy = componentProxyHost.getBeanProxy();
		IBeanProxy beforeBeanProxy = null;	// The beanproxy to go before.

		if (position != -1)
			beforeBeanProxy = getBeanProxyAt(position+1);	// Need to do +1 because we (componentBeanProxy) are already at that position in the EMF list. So we want to go before next guy.

		try {
			// Invoke a method to add the menu to the menubar.
			if (beforeBeanProxy != null) {
				BeanAwtUtilities.invoke_add_Component_before(getBeanProxy(), componentBeanProxy, beforeBeanProxy);
			} else {
				BeanAwtUtilities.invoke_add_Component(getBeanProxy(), componentBeanProxy);
			}
		} catch (ThrowableProxy e) {
			processError(sfMenus, e, aComponent);
		}
		
		// Now that we've added it, set the parent component.
		componentAdapter.setParentComponentProxyHost((IComponentProxyHost) this);
		
		revalidateBeanProxy();
		
		clearError(sfMenus, aComponent);		

	}
	/*
	 * Return the first instantiated menu bean proxy at or after the given index.
	 * It is assumed that tests for the menubar being instantiated has already been done.
	 * Return null if not found.
	 */
	protected IBeanProxy getBeanProxyAt(int position) {
		List menus = (List) ((EObject) getTarget()).eGet(sfMenus);
		for (int i=position; i<menus.size(); i++) {
			EObject menu = (EObject) menus.get(i);
			IBeanProxyHost componentProxyHost =	BeanProxyUtilities.getBeanProxyHost((IJavaInstance) menu);
			if (componentProxyHost.isBeanProxyInstantiated())
				return componentProxyHost.getBeanProxy();
		}
		
		return null;
	}
	protected void removeComponent(EObject aComponent) {
		clearError(sfMenus, aComponent);		
		// The component to actually remove is within the ConstraintComponent too.
		IBeanProxyHost aComponentBeanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) aComponent);

		// It is possible the component or ourselves didn't instantiate.  We then can't cancel it.
		if (aComponentBeanProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE && getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
			BeanAwtUtilities.invoke_remove_Component(getBeanProxy(), aComponentBeanProxyHost.getBeanProxy());
			aComponentBeanProxyHost.releaseBeanProxy();
			// This is required because AWT will not invalidate and relayout the container
			revalidateBeanProxy();
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#reinstantiateChild(IBeanProxyHost)
	 */
	public void reinstantiateChild(IBeanProxyHost aChildProxyHost) {
		IJavaObjectInstance component = (IJavaObjectInstance) aChildProxyHost.getTarget();
		removeComponent(component);
		super.reinstantiateChild(aChildProxyHost);
	}

}
