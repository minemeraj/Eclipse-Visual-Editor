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
 *  $RCSfile: JToolBarProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * @author pwalker
 *
 * Proxy adapter for JToolBar. Allows adding Actions to the JToolBar.
 */
public class JToolBarProxyAdapter extends ComponentProxyAdapter {
	protected EStructuralFeature sfItems;
	protected JavaClass classComponent, classAction;
	protected IMethodProxy fAddActionMethodProxy, fRemoveAllMethodProxy;

	/**
	 * Constructor for JToolBarProxyAdapter.
	 * @param domain
	 */
	public JToolBarProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		classComponent = (JavaClass) Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		classAction = (JavaClass) Utilities.getJavaClass("javax.swing.Action", rset); //$NON-NLS-1$
	}

	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == getSFItems()) {
			// Instantiate just this one. applyItems will only apply those that are instantiated.
			IJavaInstance item = (IJavaInstance) newValue;
			IBeanProxyHost itemBeanProxyHost = BeanProxyUtilities.getBeanProxyHost(item);
			itemBeanProxyHost.instantiateBeanProxy();
			if (itemBeanProxyHost.getErrorStatus() == ERROR_SEVERE)
				processError(getSFItems(), ((ExceptionError) itemBeanProxyHost.getErrors().get(0)).error, item);
			else {						
				removeItems();
				applyItems();
				revalidateBeanProxy();
			}
		} else {
			super.applied(as, newValue, position);
		}
	}

	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == getSFItems()) {
			removeComponent((EObject) oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}

	/** 
	 * Iterate over the items in the JToolBar and add them.
	 */
	protected void applyItems() {

		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;

		EStructuralFeature sfitems = getSFItems();
		List items = (List) ((EObject) getTarget()).eGet(sfitems);
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			IJavaObjectInstance item = (IJavaObjectInstance) iter.next();
			if (!isValidFeature(sfitems, item))
				continue;	// It wasn't valid for some reason.
			// There is a hole here in that we may of changed something and is not valid to apply, but it is difficult if we need
			// to not test for validity. The appliedList(...boolean testValidity) usually handles this, but that only applies
			// to the one setting. So for now once it goes bad it stays bad until reload from scratch.
			IBeanProxyHost itemBeanProxyHost = BeanProxyUtilities.getBeanProxyHost(item);			
			if (itemBeanProxyHost.isBeanProxyInstantiated() && itemBeanProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
				try {
					IBeanProxy itemBeanProxy = itemBeanProxyHost.getBeanProxy();					
					// Invoke a method to add the item (a component or Action) to the JToolBar.
					if (classComponent.isInstance(item)) {
						BeanAwtUtilities.invoke_add_Component(getBeanProxy(), itemBeanProxy);
						// Now that we've added it, set the parent component.
						IComponentProxyHost componentAdapter = (IComponentProxyHost) itemBeanProxyHost;
						componentAdapter.setParentComponentProxyHost((IComponentProxyHost) this);
					} else if (classAction.isInstance(item)) {
						IMethodProxy addActionMethodProxy = getAddActionMethodProxy();
						if (addActionMethodProxy != null) {
							addActionMethodProxy.invoke(getBeanProxy(), itemBeanProxy);
						}
					}
				} catch (ThrowableProxy e) {
					processError(getSFItems(), e, item);
				}
			}
		}
	}

	protected IMethodProxy getAddActionMethodProxy() {
		if (fAddActionMethodProxy == null) {
			fAddActionMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("add", "javax.swing.Action"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return fAddActionMethodProxy;
	}

	/*
	 * Return the "items" structural feature for JToolBar
	 */
	protected EStructuralFeature getSFItems() {
		if (sfItems == null) {
			JavaClass modelType = (JavaClass) ((EObject) getTarget()).eClass();
			sfItems = modelType.getEStructuralFeature("items"); //$NON-NLS-1$
		}
		return sfItems;
	}

	protected IMethodProxy removeAllMethodProxy() {
		if (fRemoveAllMethodProxy == null) {
			fRemoveAllMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("removeAll"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return fRemoveAllMethodProxy;
	}

	protected void removeComponent(EObject aComponent) {
		clearError(getSFItems(), aComponent);

		// The component to actually remove is within the ConstraintComponent too.
		IBeanProxyHost aComponentBeanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) aComponent);

		// It is possible the component or ourselves didn't instantiate.  We then can't cancel it.
		if (aComponentBeanProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE
			&& getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
			if (classComponent.isInstance(aComponent))
				BeanAwtUtilities.invoke_remove_Component(getBeanProxy(), aComponentBeanProxyHost.getBeanProxy());
			else if (classAction.isInstance(aComponent))
				BeanAwtUtilities.invoke_jtoolbar_remove_item_action(
					getBeanProxy(),
					aComponentBeanProxyHost.getBeanProxy());

			aComponentBeanProxyHost.releaseBeanProxy();
			// This is required because AWT will not invalidate and relayout the container
			revalidateBeanProxy();
		}
	}
	/* 
	 * Remove all items
	 */
	protected void removeItems() {
		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;

		removeAllMethodProxy().invokeCatchThrowableExceptions(getBeanProxy());
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
		if (fRemoveAllMethodProxy != null) {
			fRemoveAllMethodProxy.getProxyFactoryRegistry().releaseProxy(fRemoveAllMethodProxy);
			fRemoveAllMethodProxy = null;
		}
		if (fAddActionMethodProxy != null) {
			fAddActionMethodProxy.getProxyFactoryRegistry().releaseProxy(fAddActionMethodProxy);
			fAddActionMethodProxy = null;
		}
		
	}

}
