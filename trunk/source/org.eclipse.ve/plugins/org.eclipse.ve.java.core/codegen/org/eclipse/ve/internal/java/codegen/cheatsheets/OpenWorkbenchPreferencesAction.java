/*
 * Created on Jun 21, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.cheatsheets;
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
 *  $RCSfile: OpenWorkbenchPreferencesAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;

/**
 * @author sri
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OpenWorkbenchPreferencesAction extends Action {

	public class CheatSheetWorkbenchPreferenceDialog extends WorkbenchPreferenceDialog{
		public CheatSheetWorkbenchPreferenceDialog(
			Shell parentShell,
			PreferenceManager manager,
			String pageID) {
			super(parentShell, manager);
			setSelectedNodePreference(pageID);
		}
	}

	/*
	 *  Code should mirror org.eclipse.ui.internal.OpenPreferencesAction.run()
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		PreferenceManager pm = WorkbenchPlugin.getDefault().getPreferenceManager();
		 if (pm != null){
		 	IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		 	String pageID = "org.eclipse.ui.preferencePages.Workbench";
			PreferenceDialog d = ((PreferenceDialog) (new CheatSheetWorkbenchPreferenceDialog(window.getShell(), pm, pageID)));
			((Window) d).create();
			WorkbenchHelp.setHelp(d.getShell(), IHelpContextIds.PREFERENCE_DIALOG);
			((Window) d).open();
		 }
	}

}
