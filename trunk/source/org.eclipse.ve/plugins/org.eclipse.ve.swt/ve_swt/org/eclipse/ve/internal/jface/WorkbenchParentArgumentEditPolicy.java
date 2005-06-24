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
 *  $RCSfile: WorkbenchParentArgumentEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-24 20:44:24 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
 

/**
 * WorkbenchPart "parent" argument container editpolicy.
 * <p>
 * This is used by Workbench editparts to not allow children of the "parent" editpart to be added or created. This is the 
 * composite sent in as an argument. It will display a message that the context menu should be used instead. It will
 * allow child to be orphaned or deleted.
 * <p>
 * @since 1.1.0
 */
public class WorkbenchParentArgumentEditPolicy extends ContainerEditPolicy {

	protected Command getAddCommand(GroupRequest request) {
		return UnexecutableCommand.INSTANCE;	// Can't add through dropping on it.
	}
	
	protected Command getCreateCommand(CreateRequest request) {
		return UnexecutableCommand.INSTANCE;	// Can't create through dropping on it.
	}
	
}
