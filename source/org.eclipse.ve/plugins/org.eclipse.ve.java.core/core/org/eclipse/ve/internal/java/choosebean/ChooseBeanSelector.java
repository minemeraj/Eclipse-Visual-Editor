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
package org.eclipse.ve.internal.java.choosebean;

/*
 * $RCSfile: ChooseBeanSelector.java,v $ $Revision: 1.12 $ $Date: 2005-08-24 23:30:47 $
 */

import java.util.logging.Level;

import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.window.Window;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.palette.SelectionCreationToolEntry;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class ChooseBeanSelector implements SelectionCreationToolEntry.ISelector {

	public ChooseBeanSelector() {
	}

	public Object[] getNewObjectAndType(CreationTool creationTool, org.eclipse.gef.EditDomain domain) {
		EditDomain ed = (EditDomain) domain;
		ChooseBeanDialog dialog = new ChooseBeanDialog(ed.getEditorPart().getSite().getShell(), ed, null, -1, false);
		if (dialog.open() == Window.OK) {
			try {
				Object[] results = dialog.getResult();
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log(ChooseBeanMessages.ToolSelector_SelectionLogMessage + results[0], Level.FINE); 
				return results;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

}
