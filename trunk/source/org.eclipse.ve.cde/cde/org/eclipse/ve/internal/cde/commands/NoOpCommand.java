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
package org.eclipse.ve.internal.cde.commands;
/*
 *  $RCSfile: NoOpCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * This is a No Operation command. There are times when a command must
 * be returned and none being returned is considered an error. This can
 * be used in those situations where nothing is to be be done.
 *
 * Use the INSTANCE static field for all usages. There is no need for more
 * than one.
 */

public class NoOpCommand extends AbstractCommand {
	
	public static final NoOpCommand INSTANCE = new NoOpCommand();
	
	private NoOpCommand() {
		super();
		setDescription(getLabel());
	}
	
	public boolean canExecute() {
		return true;
	}
	
	public void execute() {
	}
	
	public void redo() {
	}
	
	public boolean canUndo() {
		return true;
	}
	
	public void undo() {
	}

}
