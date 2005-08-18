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
 *  $RCSfile: MenuProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-18 21:55:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
 

/**
 * swt Menu Proxy Adapter.
 * @since 1.1.0.1
 */
public class MenuProxyAdapter extends WidgetProxyAdapter {

	protected EReference sf_menubar;
	
	public MenuProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sf_menubar = JavaInstantiation.getReference(rset, SWTConstants.SF_DECORATIONS_MENU_BAR);	
		setSingleItemsFeatureName(DEFAULT_ITEMS_NAME);
	}

	protected IBeanProxyHost getParentProxyAdapter() {
		// Only interested in parent setting being "menuBar" because that is the only one that shows visually, so
		// only that setting needs to refreshed.
		IJavaInstance parent = (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sf_menubar);
		if (parent != null)
			return getSettingBeanProxyHost(parent);
		else
			return null;
	}
	
	public void revalidateBeanProxy() {
		// When we invalidate we need to revalidate the parent as well to cause the image to refresh.
		IBeanProxyHost parentProxy = getParentProxyAdapter();
		// If we are on the freeform then container will not be an instance of table
		if (parentProxy != null) {
			parentProxy.revalidateBeanProxy();
		}

	}
}
