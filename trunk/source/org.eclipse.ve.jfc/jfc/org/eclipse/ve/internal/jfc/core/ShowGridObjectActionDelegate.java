package org.eclipse.ve.internal.jfc.core;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2003 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ShowGridAction;

/**
 * @author pwalker
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
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
		fEditPart = null; // clear out previous selection
		if (((IStructuredSelection) selection).size() != 1)
			action.setEnabled(false);
		else {
			action.setEnabled(true);
			fEditPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
		}
	}

}
