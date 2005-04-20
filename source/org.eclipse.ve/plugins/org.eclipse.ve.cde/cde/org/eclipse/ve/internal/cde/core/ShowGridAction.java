package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ShowGridAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-04-20 14:27:11 $ 
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
	// Bugzilla 91826 - under some conditions the selection is not an edit part
	Object selection = getEditorPart().getSite().getSelectionProvider().getSelection();
	if(!(selection instanceof IStructuredSelection)) return;
	Object selectedObject = ((IStructuredSelection)selection).getFirstElement();	
	if(!(selectedObject instanceof EditPart)) return;
	EditPart ep = (EditPart) selectedObject;
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