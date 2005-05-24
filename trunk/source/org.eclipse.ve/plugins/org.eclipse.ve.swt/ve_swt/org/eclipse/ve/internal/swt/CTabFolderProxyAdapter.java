/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-24 21:47:56 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.core.ModelChangeController;

import org.eclipse.ve.internal.java.core.*;

/**
 * 
 * @since 1.0.0
 */
public class CTabFolderProxyAdapter extends ItemParentProxyAdapter {
	
	private EReference sf_items;
	
	/**
	 * @param domain
	 */
	public CTabFolderProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
  	  super.setTarget(newTarget);
	  if (newTarget != null) {
		sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_CTABFOLDER_ITEMS);
	  }
	}

	/**
	 * Sets the selected CTabItem corresponding to the index for this CTabFolder. This is public and is used by 
	 * the edit parts to bring a selected tab to the front so it can be viewed and edited.
	 */
	public void setSelection(int index) {
		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;
		IIntegerBeanProxy intProxy = getBeanProxy().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(index);
		BeanSWTUtilities.invoke_ctabfolder_setSelection(getBeanProxy(), intProxy);
		revalidateBeanProxy();
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (!isBeanProxyInstantiated())
			return;
		else if (sf == sf_items) {
			IBeanProxyHost oldHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) oldValue);
			if (oldHost != null)
				oldHost.releaseBeanProxy();
		} else {
			super.canceled(sf, oldValue, position);
		}

	}

	/*
	 * Instantiate all the tab items.
	 */
	private void addAllTabItems() {
		List controls = (List) ((IJavaObjectInstance) getTarget()).eGet(sf_items);
		Iterator iter = controls.iterator();
		while (iter.hasNext()) {
			IBeanProxyHost value = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) iter.next());
			if (value != null)
				value.instantiateBeanProxy();
		}
	}

	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		super.applied(sf, newValue, position);
		
		// If a tabitem has been added/moved to a position other than the end, all the tab items
		// need to be removed and re-added in order for the tabFolder to show correctly in the vm.
		if (sf == sf_items && position != -1) {
			if (fControlManager != null) {
				getModelChangeController().execAtEndOfTransaction(new Runnable() {
	
					public void run() {
						if (fControlManager != null) {
							// We were not disposed by the time we got here
							removeAllTabItems();
							addAllTabItems();
						}
					}
				}, new ModelChangeController.HashKey(this, "TabFolder Changed"));
			}
		}
	}

	/*
	 * Remove all tab items. Sometimes needed as a first step when a new tab is inserted between tabs.
	 */
	private void removeAllTabItems() {
		if (isBeanProxyInstantiated()) {
			List controls = (List) ((IJavaObjectInstance) getTarget()).eGet(sf_items);
			Iterator iter = controls.iterator();
			while (iter.hasNext()) {
				IBeanProxyHost value = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) iter.next());
				if (value != null)
					value.releaseBeanProxy();
			}
		}
	
	}
}