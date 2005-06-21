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
 *  $RCSfile: SnapToGridAction.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-21 21:43:42 $ 
 */



import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class SnapToGridAction extends SelectionAction implements IPropertyChangeListener {
	public static final String ACTION_ID = "cde.SNAPTOGRID"; //$NON-NLS-1$

	public SnapToGridAction(IEditorPart part, ShowGridAction showGridAction) {
		super(part);
		setText(CDEMessages.SnapToGridAction_label); 
		setToolTipText(CDEMessages.SnapToGridAction_tooltip); 
		setId(ACTION_ID);
		setImageDescriptor(
			ImageDescriptor.createFromFile(getClass(),  CDEMessages.SnapToGridAction_image)); 
		setEnabled(false);
		showGridAction.addPropertyChangeListener(this);
		// It is assumed that both show grid action and snaptogrid action go away at the same time so no need to have a removeListener.
	}
	/**
	 * Create a command to move the selected objects to the nearest coordinates in the grid.
	 */
	protected Command createMoveCommand(List objects) {

		ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
		request.setEditParts(objects);
		request.setMoveDelta(new Point(0, 0));
		request.setLocation(new Point(0, 0));

		// Need to verify that the editparts are all EditParts, if they aren't then we can't align.
		Iterator itr = objects.iterator();
		while (itr.hasNext())
			if (!(itr.next() instanceof EditPart))
				return UnexecutableCommand.INSTANCE;
			
		CompoundCommand compoundCmd =
			new CompoundCommand(""); //$NON-NLS-1$
		for (int i = 0; i < objects.size(); i++) {
			EditPart object = (EditPart) objects.get(i);
			Command cmd = object.getCommand(request);
			if (cmd != null)
				compoundCmd.append(cmd);
		}
		return compoundCmd.isEmpty() ? null : compoundCmd.unwrap();
		// It is ok to not have something move (i.e. no commands)
	}
	/**
	 * @see EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		List editParts = getSelectedObjects();
		boolean enabled = false;
		if (editParts.size() > 0) {
			Iterator itr = editParts.iterator();
			while (itr.hasNext()) {
				Object ep = itr.next();
				if (ep instanceof EditPart) {
					GridController gridController = GridController.getGridController(((EditPart)ep).getParent());
					if (gridController != null && gridController.isGridShowing())
						enabled = true;
				}
				enabled = false;
				break;
			}
		}

		return enabled;
	}
	/*
	 * Updates this action when the grid visibility changes
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(ShowGridAction.SHOW_GRID)) {
			refresh();
		}
	}

	/**
	 * Creates and executes the move command on the selected objects.
	 */
	public void run() {
		execute(createMoveCommand(getSelectedObjects()));
	}
}