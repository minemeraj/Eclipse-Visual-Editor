package org.eclipse.ve.internal.swt;
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
 *  $RCSfile: CustomizeLayoutObjectActionDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-07 12:46:42 $ 
 */

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.cde.core.EditDomain;

public class CustomizeLayoutObjectActionDelegate implements IObjectActionDelegate {
	protected CustomizeLayoutAction mlAction;
	private EditPart fEditPart;
	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (fEditPart != null) {
			CustomizeLayoutAction mlAction = new CustomizeLayoutAction(EditDomain.getEditDomain(fEditPart).getEditorPart());
			mlAction.run();
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		fEditPart = null;	// clear out previous selection
		if (((IStructuredSelection) selection).size() != 1)
			action.setEnabled(false);
		else {
			fEditPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
			action.setEnabled(true);
		}
	}

}
