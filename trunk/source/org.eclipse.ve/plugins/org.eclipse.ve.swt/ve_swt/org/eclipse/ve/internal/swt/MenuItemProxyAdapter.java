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
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EReference;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
 

/**
 * 
 * @since 1.1.0.1
 */
public class MenuItemProxyAdapter extends ItemProxyAdapter {

	protected EReference sf_menu;
	
	public MenuItemProxyAdapter(IBeanProxyDomain domain) {
		super(domain, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(domain.getEditDomain()), SWTConstants.SF_MENU_ITEMS));
		sf_menu = JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(domain.getEditDomain()), SWTConstants.SF_MENUITEM_MENU);
	}
	
	protected void primPrimReleaseBeanProxy(IExpression expression) {
		boolean wasInstantiated = isBeanProxyInstantiated();
		super.primPrimReleaseBeanProxy(expression);
		if (wasInstantiated && getJavaObject().eIsSet(sf_menu)) {
			// Need to release the menu. This is because it was implicitly disposed anyway when the
			// widget dispose was called because the target VM will dispose it as a child.
			getSettingBeanProxyHost((IJavaInstance) getJavaObject().eGet(sf_menu)).releaseBeanProxy(expression);
		}
	}

}
