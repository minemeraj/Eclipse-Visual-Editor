package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: ShowGridAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */



import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

public class ShowGridAction extends EditorPartAction {
	public static final String ACTION_ID = "ocm.SHOWGRID"; //$NON-NLS-1$
	public static final String SHOW_GRID = "Show grid";	// event id. //$NON-NLS-1$
	
public ShowGridAction(IEditorPart part) {
	super(part);
	setText(CDEMessages.getString("ShowGridAction.label")); //$NON-NLS-1$
	setToolTipText(CDEMessages.getString("ShowGridAction.tooltip")); //$NON-NLS-1$
	setId(ACTION_ID);
	setImageDescriptor(ImageDescriptor.createFromFile(getClass(),CDEMessages.getString("ShowGridAction.image"))); //$NON-NLS-1$
	setChecked(false);	// It is assumed that the grid controller is initially false.
	setEnabled(true);
}

public void run() {
	// Get the grid controller for the selected container editpart
	EditPart ep = (EditPart) ((IStructuredSelection)getEditorPart().getSite().getSelectionProvider().getSelection()).getFirstElement();
	if (ep == null)
		return;
	GridController gridController = GridController.getGridController(ep);
	if (gridController == null) {
		setEnabled(false);
		return;
	}
	gridController.setGridShowing(!gridController.isGridShowing());
	setChecked(gridController.isGridShowing());
	if (isChecked()) {
		setText(CDEMessages.getString("ShowGridAction.hide.label")); //$NON-NLS-1$
		setToolTipText(CDEMessages.getString("ShowGridAction.hide.tooltip")); //$NON-NLS-1$
	} else {
		setText(CDEMessages.getString("ShowGridAction.label")); //$NON-NLS-1$
		setToolTipText(CDEMessages.getString("ShowGridAction.tooltip")); //$NON-NLS-1$
	}

	// Let other actions know this property has changed.
	firePropertyChange(SHOW_GRID, 
		new Boolean(!gridController.isGridShowing()), 
		new Boolean(gridController.isGridShowing()));
}
/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return false;
}
}