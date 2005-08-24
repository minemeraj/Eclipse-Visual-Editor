/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JTabbedPaneSelectPageObjectAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.gef.EditPart;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

/**
 * This action will select the page given by the index.
 */
public class JTabbedPaneSelectPageObjectAction extends Action {

	protected int page;
	protected JTabbedPaneGraphicalEditPart editPart;

	public JTabbedPaneSelectPageObjectAction(JTabbedPaneGraphicalEditPart editPart, int page) {
		super(getTextForPage(editPart, page));
		this.editPart = editPart;
		this.page = page;
	}

	protected static String getTextForPage(JTabbedPaneGraphicalEditPart editPart, int page) {
		// The text is the tab title. Need to get it.
		IJavaObjectInstance pageObject = (IJavaObjectInstance) ((EditPart) editPart.getChildren().get(page)).getModel();
		EObject tabComponent = InverseMaintenanceAdapter.getFirstReferencedBy(pageObject, JavaInstantiation.getReference(pageObject, JFCConstants.SF_JTABCOMPONENT_COMPONENT));
		IJavaObjectInstance tabTitle =
			(IJavaObjectInstance) tabComponent.eGet(
				JavaInstantiation.getSFeature(pageObject, JFCConstants.SF_JTABCOMPONENT_TITLE));
		if (tabTitle != null) {
			IStringBeanProxy title = (IStringBeanProxy) BeanProxyUtilities.getBeanProxy(tabTitle);
			return title.stringValue();
		}
		return ""; //$NON-NLS-1$
	}

	public void run() {
		editPart.selectPage((EditPart) editPart.getChildren().get(page));
	}

}
