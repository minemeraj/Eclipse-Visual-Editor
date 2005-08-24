/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: RestorePreferredSizeAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

public class RestorePreferredSizeAction extends SelectionAction {
	public static final String ACTION_ID = "cde.RESTOREPREFERREDSIZE"; //$NON-NLS-1$

	public RestorePreferredSizeAction() {
		super(null);
		setText(CDEMessages.RestorePreferredSizeAction_label); 
		setToolTipText(CDEMessages.RestorePreferredSizeAction_tooltip); 
		setId(ACTION_ID);

		String graphicName = "resetsize_obj.gif"; //$NON-NLS-1$
		setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
		setHoverImageDescriptor(getImageDescriptor());
		setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
	}
	
	protected Command createResetToPreferredSizeCommand(List objects) {
		if (objects.size() < 1) {
			return UnexecutableCommand.INSTANCE;
		}
		
		Request resizeRequest = new Request(RequestConstantsCDE.REQ_RESTORE_PREFERRED_SIZE);
		
		CompoundCommand compoundCmd = new CompoundCommand();
		Iterator itr = objects.iterator();
		while (itr.hasNext()) {
			Object next = itr.next();
			if (next instanceof EditPart) {
				Command cmd = ((EditPart)next).getCommand(resizeRequest);
				if (cmd != null) {
					compoundCmd.append(cmd);
				}
			}
		}
		
		return compoundCmd;
	}
	
	/**
	 * Creates and executes the alignment command on the selected objects.
	 */
	public void run() {
		execute(createResetToPreferredSizeCommand(getSelectedObjects()));
	}
	
	/**
	 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		Command cmd = createResetToPreferredSizeCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}
	
	public void setWorkbenchPart(IWorkbenchPart part) {
		// To work around GEF's refusal to do the right thing and leave it public.
		super.setWorkbenchPart(part);	
	}	
}
