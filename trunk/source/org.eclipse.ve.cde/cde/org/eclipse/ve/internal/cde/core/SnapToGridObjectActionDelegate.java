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
 *  $RCSfile: SnapToGridObjectActionDelegate.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-20 21:53:21 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
 

public class SnapToGridObjectActionDelegate implements IObjectActionDelegate {
	ToggleGridAction snapToGridAction;
	private EditPart fEditPart;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void run(IAction action) {
		if (fEditPart != null && snapToGridAction != null) {
			snapToGridAction.run();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		fEditPart = null; // clear out previous selection
		snapToGridAction = null;
		if (((IStructuredSelection) selection).size() != 1) {
			action.setEnabled(false);
		} else {
			action.setEnabled(true);
			fEditPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
			snapToGridAction = new ToggleGridAction((GraphicalViewer)fEditPart.getRoot().getViewer());
			action.setChecked(snapToGridAction.isChecked());

		}
	}

}
