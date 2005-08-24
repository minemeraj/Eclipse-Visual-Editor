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
 *  $RCSfile: CopyAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ve.internal.cde.core.EditDomain;

public class CopyAction extends SelectionAction {
	
	public static final String REQ_COPY = "VE_REQ_COPY"; //$NON-NLS-1$
	private Command copyCommand;
	private EditDomain editDomain;

	public CopyAction(IWorkbenchPart part) {
		super(part);
		setId(ActionFactory.COPY.getId());		
	}

	protected boolean calculateEnabled() {
		if(getSelectedObjects().size() != 1) return false;
		Iterator iter = getSelectedObjects().iterator();
		while(iter.hasNext()){
			Object selectedObject = iter.next();
			if(selectedObject instanceof EditPart){
				EditPart selectedEditPart = (EditPart)selectedObject;
				editDomain = EditDomain.getEditDomain(selectedEditPart);
				// Get a request from the object to create a copy of itself
				Request copyRequest = new Request(REQ_COPY);
				copyCommand = selectedEditPart.getCommand(copyRequest);
				if (copyCommand != null){
					return copyCommand.canExecute();
				}
			}
		}
		return false;
	}
	
	public void run() {
		editDomain.getCommandStack().execute(copyCommand);
	}	
	
}
