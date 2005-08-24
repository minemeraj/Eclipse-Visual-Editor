/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet.common.commands;
/*
 *  $RCSfile: CommandWrapper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.gef.commands.Command;

/**
 * A command that wraps another command.
 * All the {@link Command} methods are delegated to the wrapped command.
 *
 * <p>
 * There are two typical usage patterns.  
 * One typical use for this command is to modify the behaviour of a command that you can't subclass, i.e., a decorator pattern:
 *<pre>
 *   Command decoratedCommand =
 *     new CommandWrapper(someOtherCommand)
 *     {
 *       public void execute()
 *       {
 *         doSomethingBeforeExecution();
 *         super.execute();
 *         doSomethingAfterExecution();
 *       }
 *       public Collection getResult()
 *       {
 *         return someOtherResult();
 *       }
 *     };
 *</pre>
 * The other typical use is to act as a proxy for a command who's creation is delayed:
 *<pre>
 *   Command proxyCommand =
 *     new CommandWrapper()
 *     {
 *       public Command createCommand()
 *       {
 *         return createACommandSomehow();
 *       }
 *     };
 *</pre>
 */
public class CommandWrapper extends AbstractCommand {
	/**
	 * The command for which this is a proxy or decorator.
	 */
	protected Command command;

	/** 
	 * Creates a decorator instance with the given label and description for the given command.
	 * @param label the label of the wrapper
	 * @param description the description of the wrapper
	 * @param command the command to wrap.
	 */
	public CommandWrapper(String label, String description, Command command) {
		super(label, description);
		this.command = command;
	}

	/**
	 * Creates a commandless proxy instance.
	 * The wrapped command will be created by a {@link #createCommand} callback.
	 * Since a proxy command like this is pointless unless you override some method, this constructor is protected.
	 */
	protected CommandWrapper() {
		super();
	}

	/**
	 * Creates a commandless proxy instance, with the given label.
	 * The command will be created by a {@link #createCommand} callback.
	 * Since a proxy command like this is pointless unless you override some method, this constructor is protected.
	 * @param label the label of the wrapper
	 */
	protected CommandWrapper(String label) {
		super(label);
	}

	/**
	 * Creates a commandless proxy instance, with the given label and description.
	 * The command will be created by a {@link #createCommand} callback.
	 * Since a proxy command like this is pointless unless you override some method, this constructor is protected.
	 * @param label the label of the wrapper
	 * @param description the description of the wrapper
	 */
	protected CommandWrapper(String label, String description) {
		super(label, description);
	}

	/**
	 * Returns the command for which this is a proxy or decorator.
	 * This may be <code>null</code> before {@link #createCommand} is called.
	 * @return the command for which this is a proxy or decorator.
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * Create the command being proxied.
	 * This implementation just return <code>null</code>.
	 * It is called by {@link #prepare}.
	 * @return the command being proxied.
	 */
	protected Command createCommand() {
		return null;
	}

	/**
	 * Returns whether the command can execute.
	 * This implementation creates the command being proxied using {@link #createCommand},
	 * if the command wasn't given in the constructor.
	 * @return whether the command can execute.
	 */
	protected boolean prepare() {
		if (command == null) {
			command = createCommand();
		}

		boolean result = command.canExecute();
		return result;
	}

	/**
	 * Delegates to the execute method of the command.
	 */
	public void execute() {
		if (command != null) {
			command.execute();
		}
	}

	/**
	 * Delegates to the canUndo method of the command.
	 */
	public boolean canUndo() {
		return command == null || command.canUndo();
	}

	/**
	 * Delegates to the undo method of the command.
	 */
	public void undo() {
		if (command != null)
			command.undo();
	}

	/**
	 * Delegates to the redo method of the command.
	 */
	public void redo() {
		if (command != null) {
			command.redo();
		}
	}

	/**
	 * Delegates to the getLabel method of the command.
	 * @return the label.
	 */
	public String getLabel() {
		return label == null ? command == null ? CommonPlugin.INSTANCE.getString("_UI_CommandWrapper_label") : command.getLabel() : label; //$NON-NLS-1$
	}

	/**
	 * Delegates to the dispose method of the command.
	 */
	public void dispose() {
		if (command != null) {
			command.dispose();
		}
	}

	/*
	 * Javadoc copied from base class.
	 */
	public String toString() {
		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (command: " + command + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		return result.toString();
	}
}
