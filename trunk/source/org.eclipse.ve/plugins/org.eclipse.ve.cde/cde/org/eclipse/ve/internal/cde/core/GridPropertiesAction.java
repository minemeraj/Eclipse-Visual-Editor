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
 *  $RCSfile: GridPropertiesAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-21 21:43:42 $ 
 */



import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorPart;

public class GridPropertiesAction extends EditorPartAction implements IPropertyChangeListener {
	public static final String ACTION_ID = "cde.GRIDPROPERTIES"; //$NON-NLS-1$
	
public GridPropertiesAction(IEditorPart part, ShowGridAction showGridAction) {
	super(part);
	
	setText(CDEMessages.GridPropertiesAction_label); 
	setToolTipText(CDEMessages.GridPropertiesAction_tooltip); 
	setId(ACTION_ID);
	setImageDescriptor(ImageDescriptor.createFromFile(getClass(),CDEMessages.GridPropertiesAction_image)); 
	setEnabled(showGridAction.isChecked());
	showGridAction.addPropertyChangeListener(this);	// It is assumed that both show grid action and gridproperties action go away at the same time so no need to have a removeListener.
}

/*
 * Updates this action when the grid visibility changes
 */
public void propertyChange(PropertyChangeEvent event) {
	if (event.getProperty().equals(ShowGridAction.SHOW_GRID)) {
		setEnabled(((Boolean) event.getNewValue()).booleanValue());
	}
}

/*
 * Get the grid figure decoration, open the GridPropertiesDialog, and if 
 * changes are made, update the grid figure decoration on the edit part.
 */
public void run() {
	GridController gridController = GridController.getGridController(getEditorPart());
	
	GridPropertiesDialog dialog = new GridPropertiesDialog(
		getEditorPart().getSite().getWorkbenchWindow().getShell(), 
		gridController);
	dialog.open();
}
/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return false;
}

}