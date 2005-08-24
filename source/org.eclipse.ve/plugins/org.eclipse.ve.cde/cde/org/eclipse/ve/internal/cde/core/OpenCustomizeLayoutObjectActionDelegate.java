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
 *  $RCSfile: OpenCustomizeLayoutObjectActionDelegate.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:49 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;
 

/**
 * @since 1.0.0
 *
 */
public class OpenCustomizeLayoutObjectActionDelegate implements IObjectActionDelegate {

	protected CustomizeLayoutWindowAction currentLayoutAction;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		IAction customizeLayoutAction = null;
		if (targetPart instanceof IEditorPart)
			customizeLayoutAction = ((IEditorPart) targetPart).getEditorSite().getActionBars().getGlobalActionHandler(CustomizeLayoutWindowAction.ACTION_ID);
		else if (targetPart instanceof PageBookView) {
			IPage page = ((PageBookView) targetPart).getCurrentPage();
			if (page instanceof IPageBookViewPage)
				customizeLayoutAction = ((IPageBookViewPage) page).getSite().getActionBars().getGlobalActionHandler(CustomizeLayoutWindowAction.ACTION_ID);
		} else if (targetPart instanceof IViewPart)
			customizeLayoutAction = ((IViewPart) targetPart).getViewSite().getActionBars().getGlobalActionHandler(CustomizeLayoutWindowAction.ACTION_ID);
		if (customizeLayoutAction instanceof CustomizeLayoutWindowAction)
			currentLayoutAction = (CustomizeLayoutWindowAction) customizeLayoutAction;
		else
			currentLayoutAction = null;
		action.setEnabled(currentLayoutAction != null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		currentLayoutAction.showCustomizeLayoutWindow();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
}
