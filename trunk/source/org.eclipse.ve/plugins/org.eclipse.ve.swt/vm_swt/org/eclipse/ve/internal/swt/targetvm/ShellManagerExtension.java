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
 *  $RCSfile: ShellManagerExtension.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerExtension;
 

/**
 * Shell Manager Extension.
 * @since 1.1.0
 */
public class ShellManagerExtension extends ControlManagerExtension {
		
	/**
	 * Apply the shell title. We need to be able to put in a default title if the
	 * title is not already set, or are applying null or "". This is so that it has a title because the window shows
	 * on the taskbar of the system and without a title people get confused.
	 * <p>
	 * It will return the old title.
	 * 
	 * @param shell
	 * @param title
	 * @param replaceOld Only used if title is empty, then if <code>true</code> should replace old (this would be for a normal set),
	 * <code>false</code> shouldn't replace old if old title is not empty (this would be for a initial setup
	 * where no title was explicitly set on the client side. This allows us to replace an empty old title with the
	 * default string, or not replace it if old title was good).
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static String applyShellTitle(Shell shell, String title, boolean replaceOld) {
		String oldTitle = shell.getText();
		if (title == null || title.length() == 0) {
			if (replaceOld || (oldTitle == null || oldTitle.length() == 0))
				shell.setText(TargetVMMessages.getString("ShellDefaultTitle"));	 //$NON-NLS-1$
		} else
			shell.setText(title);
		
		return oldTitle;
	}
	
	/**
	 * Pack window on any change flag.
	 */
	protected boolean packOnChange;
	
	/**
	 * Set the pack on change flag. 
	 * @param packOnChange <code>true</code> if on any change (invalidate) the Shell should be packed. This is used when no explicit size has
	 * been set and it should float to the packed size.
	 * 
	 * @since 1.1.0
	 */
	public void setPackOnChange(boolean packOnChange) {
		this.packOnChange = packOnChange;
	}
	
	
	protected void aboutToValidate() {
		if (packOnChange)
			((Shell) getControl()).pack(true);
	}
		
	protected void controlSet(Control oldControl, Control newControl) {
		if (oldControl != null)
			((Shell) oldControl).removeShellListener(getControlManager().getFeedbackController().getEnvironment().getPreventShellCloseListener());
		if (newControl != null)
			((Shell) newControl).addShellListener(getControlManager().getFeedbackController().getEnvironment().getPreventShellCloseListener());
		super.controlSet(oldControl, newControl);
	}

}
