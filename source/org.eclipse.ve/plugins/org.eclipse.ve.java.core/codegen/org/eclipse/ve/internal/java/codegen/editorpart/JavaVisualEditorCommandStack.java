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
package org.eclipse.ve.internal.java.codegen.editorpart;
/*
 *  $RCSfile: JavaVisualEditorCommandStack.java,v $
 *  $Revision: 1.9 $  $Date: 2005-09-19 20:37:49 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.gef.commands.*;

import org.eclipse.ve.internal.cde.core.ModelChangeController;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/*
 * This is a dummy command stack that executes commands but does not actually keep a stack
 * It is used by the JVE because all undo and redo of the JVE is deferred through
 * to the JavaEditor that undoes the source changes.
 * This makes for a more consistent user experiences where GUI changes and also source changes
 * are all undone together
 *
 * It will use the passed in ModelChangeController to process the commands.
 * 
 * <package> protected because only the JavaVisualEditorPart should use it. 
 * 
 * @since 1.0.0
 */
class JavaVisualEditorCommandStack extends CommandStack {

	protected List fCommandStackListeners;
	protected ModelChangeController modelChangeController;

	public JavaVisualEditorCommandStack(ModelChangeController modelChangeController) {
		this.modelChangeController = modelChangeController;
	}

	public void execute(final Command aCommand) {
		if (aCommand == null || !aCommand.canExecute())
			return;
		try {
			modelChangeController.doModelChanges(new Runnable() {

				public void run() {
					aCommand.execute();
				}
			}, false);
		} catch (RuntimeException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} finally {
			notifyListeners();
			aCommand.dispose();
		}
	}

	protected synchronized void notifyListeners() {
		if (fCommandStackListeners != null) {
			EventObject event = new EventObject(this);
			for (int i = 0; i < fCommandStackListeners.size(); i++)
				 ((CommandStackListener) fCommandStackListeners.get(i)).commandStackChanged(event);
		}
	}

	public void redo() {
		// Do nothing - we should never be asked to redo
		throw new RuntimeException("All redo should be directed to the JavaEditor's command stack");	//$NON-NLS-1$
	}

	public void undo() {
		// Do nothing - we should never be asked to undo
		throw new RuntimeException("All undo should be directed to the JavaEditor's command stack");	//$NON-NLS-1$
	}

	// These are the traditional listeners that just know after something has executed
	public synchronized void addCommandStackListener(CommandStackListener aListener) {
		if (fCommandStackListeners == null)
			fCommandStackListeners = new ArrayList(1);
		fCommandStackListeners.add(aListener);
	}
	public synchronized void removeCommandStackListener(CommandStackListener aListener) {
		if (fCommandStackListeners == null)
			fCommandStackListeners = new ArrayList(1);
		fCommandStackListeners.remove(aListener);

	}
	public boolean canRedo() {
		return false;
	}
	public boolean canUndo() {
		return false;
	}
	public void flush() {
		// Do nothing
	}
	public Command getMostRecentCommand() {
		return null;
	}
	public Command getRedoCommand() {
		return null;
	}
	public Command getUndoCommand() {
		return null;
	}
}
