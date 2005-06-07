/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabFolderSelectPageObjectAction.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-07 19:22:42 $ 
 */
package org.eclipse.ve.internal.swt;
 
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * This action will select the page given by the index.
 */
public class TabFolderSelectPageObjectAction extends Action {

	protected int page;
	protected TabFolderGraphicalEditPart editPart;

	public TabFolderSelectPageObjectAction(TabFolderGraphicalEditPart editPart, int page) {
		super(getTextForPage(editPart, page));
		this.editPart = editPart;
		this.page = page;
	}

	protected static String getTextForPage(TabFolderGraphicalEditPart editPart, int page) {
		// The text is the tab title. Need to get it.
		IJavaObjectInstance pageObject = (IJavaObjectInstance) ((EditPart) editPart.getChildren().get(page)).getModel();
		IJavaObjectInstance tabControl = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(pageObject, JavaInstantiation.getReference(pageObject, SWTConstants.SF_TABITEM_CONTROL));
		IJavaObjectInstance tabTitle =
			(IJavaObjectInstance) tabControl.eGet(
				JavaInstantiation.getSFeature(tabControl, "text")); //$NON-NLS-1$
		if (tabTitle != null) {
			IStringBeanProxy title = (IStringBeanProxy) BeanProxyUtilities.getBeanProxy(tabTitle);
			return title.stringValue();
		}
		return ""; //$NON-NLS-1$
	}

	public void run() {
		editPart.pageSelected((EditPart) editPart.getChildren().get(page));
	}

}