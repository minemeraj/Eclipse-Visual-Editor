/*
 * Created on May 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
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
 *  $RCSfile: CreateContentPaneObjectActionDelegate.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:18:06 $ 
 */

import java.util.Iterator;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionDelegate;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * @author richkulp
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CreateContentPaneObjectActionDelegate
	extends ActionDelegate
	implements IObjectActionDelegate {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	IStructuredSelection selection;
	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (!(selection instanceof IStructuredSelection))
			action.setEnabled(false);
		else {
			// Disable if any already have a contentpane.
			// We can assume we are on implementers of RootPaneContainer because that is handled in the plugin.xml.
			this.selection = (IStructuredSelection) selection;
			Iterator itr = this.selection.iterator();
			boolean enabled = false;
			while (itr.hasNext()) {
				EditPart ep = (EditPart) itr.next();
				IJavaObjectInstance model = (IJavaObjectInstance) ep.getModel();
				EStructuralFeature sf_contentPane = ((JavaClass) model.getJavaType()).getEStructuralFeature("contentPane"); //$NON-NLS-1$
				if (sf_contentPane == null || model.eIsSet(sf_contentPane)) {
					// No content pane feature (shouldn't happen), or model has a content pane, so disable the action.
					enabled = false;
					break;
				} else
					enabled = true;
			}
			action.setEnabled(enabled); 
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		EditPart firstEP = (EditPart) selection.getFirstElement();
		EditDomain domain = EditDomain.getEditDomain(firstEP);	
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		
		Iterator eps = selection.iterator();
		while(eps.hasNext()) {
			EditPart ep = (EditPart) eps.next();
			IJavaObjectInstance model = (IJavaObjectInstance) ep.getModel();
			RootPaneCreationPolicy.createContentPane(cb, domain, model);
		}
		
		domain.getCommandStack().execute(cb.getCommand());
	}		

}
