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
 *  $RCSfile: DummyWizardCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.text.MessageFormat;

import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.internal.WorkbenchMessages;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DummyWizardCommand extends AbstractCommand {

	protected IWizard wizard = null;
	protected Shell shell  = null;
	protected IWorkingCopy workingCopy = null;
	protected IEditorPart editorPart = null;
	
	/**
	 * Constructor for RuledWizardPropertySetCommand.
	 * @param IWizard
	 */
	public DummyWizardCommand(IEditorPart editorPart, IWizard wizard, Shell shell) {
		this.wizard = wizard;
		this.shell = shell;
		this.editorPart = editorPart;
		this.workingCopy = JavaUI.getWorkingCopyManager().getWorkingCopy(editorPart.getEditorInput());
	}

	/**
	 * Constructor for RuledWizardPropertySetCommand.
	 * @param label
	 * @param domain
	 * @param target
	 * @param propertyId
	 * @param propertyValue
	 */
	public DummyWizardCommand(String label, IEditorPart editorPart, IWizard wizard, Shell shell) {
		this(editorPart, wizard, shell);
		setLabel(label);
	}

	/**
	 * @see com.ibm.etools.common.command.Command#execute()
	 */
	public void execute() {
		String message = MessageFormat.format(VisualMessages.getString("DummyWizardCommand.MessageDialog.FileWillBeSaved"), new Object[]{workingCopy.getOriginalElement().getElementName()});  //$NON-NLS-1$
		// Show a dialog.
		String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL};
			MessageDialog d = new MessageDialog(
				shell, WorkbenchMessages.getString("Save_Resource"), //$NON-NLS-1$
				null, message, MessageDialog.QUESTION, buttons, 0);
		int choice = d.open();
	
		// Branch on the user choice.
		// The choice id is based on the order of button labels above.
		switch (choice) {
			case MessageDialog.OK : //OK
				editorPart.doSave(null);
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.open();
				break;
			case MessageDialog.CANCEL : //cancel
				return;
			default :
				return;
		}
	}

	/**
	 * @see com.ibm.etools.common.command.Command#redo()
	 */
	public void redo() {
		execute();
	}

	/**
	 * @see com.ibm.etools.common.command.Command#canExecute()
	 */
	public boolean canExecute() {
		return wizard!=null;
	}

	/**
	 * @see com.ibm.etools.common.command.Command#canUndo()
	 */
	public boolean canUndo() {
		return false;
	}

}
