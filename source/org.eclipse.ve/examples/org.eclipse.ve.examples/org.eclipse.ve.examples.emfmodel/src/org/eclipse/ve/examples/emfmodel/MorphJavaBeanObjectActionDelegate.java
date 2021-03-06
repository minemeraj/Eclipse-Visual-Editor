/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.emfmodel;

import org.eclipse.gef.EditPart;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class MorphJavaBeanObjectActionDelegate implements IObjectActionDelegate , IMenuCreator {

	private EditPart javaBeanEditPart;
	private Shell shell;
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	public void run(IAction action) {
		MorphDialog dialog = new MorphDialog(shell);
		dialog.setJavaBean((IJavaInstance) javaBeanEditPart.getModel());
		dialog.open();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// Enable for one edit part only
		try{
			javaBeanEditPart = (EditPart) ((IStructuredSelection)selection).getFirstElement();
		} catch (ClassCastException e){
			// Do nothing
		}
	}

	public void dispose() {}

	public Menu getMenu(Control parent) {
		return null;
	}
	public Menu getMenu(Menu parent) {
		return null;
	}
}
