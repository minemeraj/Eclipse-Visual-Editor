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
 *  $RCSfile: ShowGridObjectActionDelegate.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-26 16:35:58 $ 
 */

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ShowGridAction;

public class ShowGridObjectActionDelegate implements IObjectActionDelegate {
	protected ShowGridAction sgAction;
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
			ShowGridAction sgAction = new ShowGridAction(EditDomain.getEditDomain(fEditPart).getEditorPart());
			sgAction.run();
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
			action.setEnabled(true);
			fEditPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
		}
	}

}
