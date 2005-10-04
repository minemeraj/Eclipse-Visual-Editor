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
package org.eclipse.ve.internal.propertysheet.command;
/*
 *  $RCSfile: ForwardUndoCompoundCommand.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-04 15:41:49 $ 
 */


import java.util.ListIterator;

import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 *  This command will undo its commands in the same order applied.
 *
 *  However any Chained commands will undo in reverse order since
 *  that can't be controlled.
 */
public class ForwardUndoCompoundCommand extends CompoundCommand {
	public ForwardUndoCompoundCommand() {
		super();
	}

	public ForwardUndoCompoundCommand(String label) {
		super(label);
	}
	
	public void append(Command command) {
		// Can't merge compound commands into forward undo's. It would mess up the ordering. CompoundCommands must be undone
		// properly (in reverse), while sequential compound commands must be undone in the forward order.
		if (command != null)
			commandList.add(command);
	}

	/**
	 * This calls {@link Command#undo} for each command in the list, in forward order.
	 */
	public void undo() {

		for (ListIterator commands = commandList.listIterator(); commands.hasNext();) {
			try {
				Command command = (Command) commands.next();
				command.undo();
			} catch (RuntimeException exception) {

				// Skip over the command that threw the exception.
				//
				commands.previous();

				// Iterate backwards over the undone commands to redo them.
				//
				while (commands.hasPrevious()) {
					Command command = (Command) commands.previous();
					command.redo();
				}

				throw exception;
			}
		}

	}

}
