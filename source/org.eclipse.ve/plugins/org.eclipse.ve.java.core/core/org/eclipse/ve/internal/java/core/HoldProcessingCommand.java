/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: HoldProcessingCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:23:54 $ 
 */


import org.eclipse.gef.commands.Command;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * This command will execute the passed in command while processing
 * is held on the object (it must have a IBeanProxyHost that implements
 * IHoldProcessing). When the submitted command is completed it will
 * resume processing.
 */
public class HoldProcessingCommand extends AbstractCommand {

	protected Command cmdToExecute;
	protected IJavaInstance target;
	
	/**
	 * Constructor for HoldProcessingCommand.
	 */
	public HoldProcessingCommand(Command cmdToExecute, IJavaInstance target) {
		this.cmdToExecute = cmdToExecute;
		this.target = target;
	}

	/**
	 * Constructor for HoldProcessingCommand.
	 * @param label
	 */
	public HoldProcessingCommand(Command cmdToExecute, IJavaInstance target, String label) {
		super(label);
		this.cmdToExecute = cmdToExecute;
		this.target = target;
	}

	/**
	 * Constructor for HoldProcessingCommand.
	 * @param label
	 * @param description
	 */
	public HoldProcessingCommand(Command cmdToExecute, IJavaInstance target, String label, String description) {
		super(label, description);
		this.cmdToExecute = cmdToExecute;
		this.target = target;
	}

	/**
	 * @see com.ibm.etools.common.command.Command#redo()
	 */
	public void redo() {
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(target);
		if (h instanceof IHoldProcessing) {
			((IHoldProcessing) h).holdProcessing();
			try {
				cmdToExecute.redo();
			} finally {
				((IHoldProcessing) h).resumeProcessing();
			}
		} else
			cmdToExecute.redo();		
	}

	/**
	 * @see com.ibm.etools.common.command.Command#execute()
	 */
	public void execute() {
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(target);
		if (h instanceof IHoldProcessing) {
			((IHoldProcessing) h).holdProcessing();
			try {
				cmdToExecute.execute();
			} finally {
				((IHoldProcessing) h).resumeProcessing();
			}
		} else
			cmdToExecute.execute();				
	}
	
	/**
	 * @see com.ibm.etools.common.command.Command#undo()
	 */
	public void undo() {
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(target);
		if (h instanceof IHoldProcessing) {
			((IHoldProcessing) h).holdProcessing();
			try {
				cmdToExecute.undo();
			} finally {
				((IHoldProcessing) h).resumeProcessing();
			}
		} else
			cmdToExecute.undo();				
	}	

	/**
	 * @see com.ibm.etools.common.command.AbstractCommand#prepare()
	 */
	protected boolean prepare() {
		return cmdToExecute != null && target != null;
	}

}
