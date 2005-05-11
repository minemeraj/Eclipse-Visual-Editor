/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabFolderProxyAdapter.java,v $
 *  $Revision: 1.6 $  $Date: 2005-05-11 19:01:30 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.java.core.*;

/**
 * 
 * @since 1.0.0
 */
public class TabFolderProxyAdapter extends ItemParentProxyAdapter {
	private EReference sf_items;
	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public TabFolderProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
  	  super.setTarget(newTarget);
	  if (newTarget != null) {
		sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_TABFOLDER_ITEMS);
	  }
	}

	/**
	 * Sets the selected TabItem corresponding to the index for this TabFolder. This is public and is used by 
	 * the edit parts to bring a selected tab to the front so it can be viewed and edited.
	 */
	public void setSelection(int index) {
		if (!isBeanProxyInstantiated())
			return;
		IIntegerBeanProxy intProxy = getBeanProxy().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(index);
		BeanSWTUtilities.invoke_tabfolder_setSelection(getBeanProxy(), intProxy);
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
}