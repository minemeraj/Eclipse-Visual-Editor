/*
 * Created on Jun 12, 2003
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
 *  $RCSfile: CheatSheetNewVisualClassAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.java.codegen.wizards.VisualClassExampleWizard;


/**
 * @author sri
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CheatSheetNewVisualClassAction extends AbstractWizardInvocationAction{

	protected IWizard createWizard() {
		VisualClassExampleWizard wizard = new VisualClassExampleWizard();
		StructuredSelection selection = null;
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(CreateNewWebProjectAction.WEB_PROJECT_NAME);
		if(project!=null && project.exists())
			selection = new StructuredSelection(project);
		else
			selection = new StructuredSelection();
		wizard.init(PlatformUI.getWorkbench(), selection);
		wizard.setInitializationData(null, null, "CheatSheetJTableExample1");
		return wizard;
	}
}
