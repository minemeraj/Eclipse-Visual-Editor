package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualClassExampleWizard.java,v $
 *  $Revision: 1.4 $  $Date: 2004-06-01 13:39:39 $ 
 */

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.wizards.NewClassCreationWizard;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 */
public class VisualClassExampleWizard extends NewClassCreationWizard implements IExecutableExtension  {
	
	private VisualClassExampleWizardPage fPage;	
	// the class name is going to be searched in the Examples directory of the
	// contributing plugin
	private String fExampleClassName;
	private String fPluginName = null ;
	
	public VisualClassExampleWizard() {
		super();
		setDefaultPageImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
		setDialogSettings(JavaVEPlugin.getPlugin().getDialogSettings());
		setWindowTitle(CodegenMessages.getString("VisualClassExampleWizard.title")); //$NON-NLS-1$
	}

	/*
	 * @see Wizard#createPages
	 */	
	public void addPages() {
		fPage= new VisualClassExampleWizardPage();
		addPage(fPage);
		fPage.init(getSelection());
		fPage.setSuperClass("javax.swing.JFrame",false); //$NON-NLS-1$
		fPage.setTypeName(fExampleClassName,false);
		fPage.setPluginName(fPluginName) ;
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		fPage.createType(monitor); // use the full progress monitor
		ICompilationUnit cu= JavaModelUtil.toOriginal(fPage.getCreatedType().getCompilationUnit());
		if (cu != null) {
			IResource resource= cu.getResource();
			selectAndReveal(resource);
			openResource(resource);
		}	
	}
	
	protected void openResource(IResource resource) {
		NewVisualClassCreationWizard.openResourceJVE(resource);
	}
	
	public void setInitializationData(IConfigurationElement element,String string,Object object){
		if ( object instanceof String ) {
			fPluginName = element.getDeclaringExtension().getDeclaringPluginDescriptor().getUniqueIdentifier();
			fExampleClassName = (String) object;
		}			
	}
}