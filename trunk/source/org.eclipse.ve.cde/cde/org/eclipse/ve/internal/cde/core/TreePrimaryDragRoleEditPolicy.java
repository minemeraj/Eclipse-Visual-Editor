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
 *  $RCSfile: TreePrimaryDragRoleEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

/**
 * Default Tree Primary Drag EditPolicy. This is used on components
 * to send move requests up to the parent. It should be installed
 * on all tree components (and containers) except the contents
 * tree edit part. That part doesn't need it.
 */
public class TreePrimaryDragRoleEditPolicy
	extends AbstractEditPolicy
{

public Command getCommand(Request req){
	if (REQ_MOVE.equals(req.getType()))
		return getMoveCommand(req);
	return null;	
}

protected Command getMoveCommand(Request req){
	EditPart parent = getHost().getParent();
	if(parent != null){
		req.setType(REQ_MOVE_CHILDREN);
		Command cmd = parent.getCommand(req);
		req.setType(REQ_MOVE);
		return cmd;
	} else {
		return UnexecutableCommand.INSTANCE;
	}
}

public EditPart getTargetEditPart(Request req){
	return null;
}

}


