/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: AlignmentAction.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:17:59 $ 
 */



import java.util.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class AlignmentAction extends SelectionAction {
	// The resource prefix and action id to use for each alignment type
	// The alignment types can be found in AlignmentCommandRequest.
	// The following array must match in order the types found
	// in AlignmentCommandRequest so that the labels are correct.
	private final static String[] resPrefix = { "AlignmentAction.left.", //$NON-NLS-1$
		"AlignmentAction.center.", //$NON-NLS-1$
		"AlignmentAction.right.", //$NON-NLS-1$
		"AlignmentAction.top.", //$NON-NLS-1$
		"AlignmentAction.middle.", //$NON-NLS-1$
		"AlignmentAction.bottom.", //$NON-NLS-1$
		"AlignmentAction.width.", //$NON-NLS-1$
		"AlignmentAction.height." }; //$NON-NLS-1$

	protected int fAlignType;

	public AlignmentAction(int alignType) {
		super((IWorkbenchPart) null);	// Actual Workbench part will be assigned later.
		// Default to left alignment if the align type is incorrect
		if (!(alignType >= 0 && alignType < resPrefix.length))
			fAlignType = AlignmentCommandRequest.LEFT_ALIGN;
		else
			fAlignType = alignType;
		String sAlignType = resPrefix[fAlignType];
		setText(CDEMessages.getString(sAlignType+"label")); //$NON-NLS-1$
		setToolTipText(CDEMessages.getString(sAlignType+"tooltip")); //$NON-NLS-1$
		// There are three images, one for full color ( that is the hover one )
		// one for disabled and one for enabled
		String graphicName = CDEMessages.getString(sAlignType + "image"); //$NON-NLS-1$
		// The file structure of these is that they exist in the plugin directory with three folder names, e.g.
		// /icons/full/clc16/alignleft_obj.gif for the color one
		// and elc16 for enabled and dlc16 for disasbled
		setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
		setHoverImageDescriptor(getImageDescriptor());
		setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName));				 //$NON-NLS-1$
		setEnabled(false);
		setId(getActionId(fAlignType));
	}

	/**
	 * Static method that returns the action id based on the alignment type.
	 */
	public static String getActionId(int alignType) {
		return (
			(alignType >= 0 && alignType < resPrefix.length)
				? resPrefix[alignType]
				: resPrefix[AlignmentCommandRequest.LEFT_ALIGN]);
	}

	/**
	 * Creates commands to align the selected objects to an anchor object
	 * @return CompoundCommand with the commands to align the selected objects
	 * to the anchor object. To create the command, there must be
	 * more than one object selected, the selected objects for 
	 * an element viewer must be EditPart items.
	 */
	protected Command createAlignmentCommand(List objects) {
		if (objects.size() < 2)
			return UnexecutableCommand.INSTANCE; // Need to have at least 2 for alignment

		// Need to verify that the editparts are all EditParts, if they aren't then we can't align.
		Iterator itr = objects.iterator();
		while (itr.hasNext())
			if (!(itr.next() instanceof EditPart))
				return UnexecutableCommand.INSTANCE;
			
		AlignmentCommandRequest alignReq = new AlignmentCommandRequest(fAlignType);
		// Of the list of selected editparts the primary select one is the anchor.
		ArrayList selectedParts = new ArrayList(objects.size()-1);
		for (int i = 0; i < objects.size(); i++) {
			EditPart editpart = (EditPart) objects.get(i);
			if (editpart.getSelected() == EditPart.SELECTED_PRIMARY)
				alignReq.setAnchorObject(editpart);
			else
				selectedParts.add(editpart);
		}
		// Create alignment commands to align all the other selected objects to the 
		// anchor editpart.
		CompoundCommand compoundCmd = new CompoundCommand();
		for (int i = 0; i < selectedParts.size(); i++) {
			EditPart editpart = (EditPart) selectedParts.get(i);
			Command cmd = editpart.getCommand(alignReq);
			if (cmd == null)
				return UnexecutableCommand.INSTANCE; // Alignment request not processed, considered not alignable.
			compoundCmd.append(cmd);
		}
		return compoundCmd;
	}

	/**
	 * Creates and executes the aalignment command on the selected objects.
	 */
	public void run() {
		execute(createAlignmentCommand(getSelectedObjects()));
	}
	/**
	 * @see EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		Command cmd = createAlignmentCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}
	public void setWorkbenchPart(IWorkbenchPart part) {
		// To work around GEF's refusal to do the right thing and leave it public.
		super.setWorkbenchPart(part);
	}

}
