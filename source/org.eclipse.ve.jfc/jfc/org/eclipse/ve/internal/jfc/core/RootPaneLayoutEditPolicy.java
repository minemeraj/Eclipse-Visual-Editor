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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: RootPaneLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class RootPaneLayoutEditPolicy extends LayoutEditPolicy {

	/**
	 * Constructor for RootPaneLayoutEditPolicy.
	 */
	public RootPaneLayoutEditPolicy() {
		super();
	}

	/**
	 * @see LayoutEditPolicy#createChildEditPolicy(EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NonResizableEditPolicy();
	}

	/**
	 * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * @see LayoutEditPolicy#getDeleteDependantCommand(Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * @see LayoutEditPolicy#getMoveChildrenCommand(Request)
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return null;
	}
	
	protected Command getOrphanChildrenCommand(Request request) {
		return UnexecutableCommand.INSTANCE;
	}

}
