package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: HoldProcessingCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */


import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
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
