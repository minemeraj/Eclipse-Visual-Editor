/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderSelectPageObjectAction.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:55 $ 
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
public class CTabFolderSelectPageObjectAction extends Action {

	protected int page;
	protected CTabFolderGraphicalEditPart editPart;

	public CTabFolderSelectPageObjectAction(CTabFolderGraphicalEditPart editPart, int page) {
		super(getTextForPage(editPart, page));
		this.editPart = editPart;
		this.page = page;
	}

	protected static String getTextForPage(CTabFolderGraphicalEditPart editPart, int page) {
		// The text is the tab title. Need to get it.
		IJavaObjectInstance pageObject = (IJavaObjectInstance) ((EditPart) editPart.getChildren().get(page)).getModel();
		IJavaObjectInstance tabControl = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(pageObject, JavaInstantiation.getReference(pageObject, SWTConstants.SF_CTABITEM_CONTROL));
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
