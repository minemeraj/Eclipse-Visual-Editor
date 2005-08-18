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
 *  Created Aug 15, 2005 by Gili Mendel
 * 
 *  $RCSfile: MenuItemTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-18 21:55:55 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

/**
 * MenuItem Tree Editpart.
 * 
 * @since 1.1.0.1
 */
public class MenuItemTreeEditPart extends ItemTreeEditPart {

	protected EReference sf_menu;
	
	public MenuItemTreeEditPart(Object model) {
		super(model);
	}
	
	public void setModel(Object model) {
		super.setModel(model);

		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_menu = JavaInstantiation.getReference(rset, SWTConstants.SF_MENUITEM_MENU);
	}	
	
	protected List getChildJavaBeans() {
		if (((EObject) getModel()).eIsSet(sf_menu)) {
			return Collections.singletonList (((EObject) getModel()).eGet(sf_menu));
		} else 	
			return Collections.EMPTY_LIST;
	}
	
	protected Adapter menuItemAdapter = new EditPartAdapterRunnable(this) {
		protected void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_menu)
				queueExec(MenuItemTreeEditPart.this, "MENU"); //$NON-NLS-1$
		}
	};

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(menuItemAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(menuItemAdapter);
	}
	

}
