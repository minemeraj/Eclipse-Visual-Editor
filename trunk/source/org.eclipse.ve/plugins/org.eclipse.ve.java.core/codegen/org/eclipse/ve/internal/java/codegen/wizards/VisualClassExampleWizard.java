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
 *  $Revision: 1.8 $  $Date: 2004-08-02 19:36:03 $ 
 */

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.actions.WorkbenchRunnableAdapter;
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
		// TODO What does this do (the toOriginal)? Does it need to be called here?
		JavaModelUtil.toOriginal(fPage.getCreatedType().getCompilationUnit());
	}
	
	protected void openResource(IResource resource) {
		NewVisualClassCreationWizard.openResourceJVE(resource);
	}
	
	public void setInitializationData(IConfigurationElement element,String string,Object object){
		if ( object instanceof String ) {
			setInitializationData(element.getDeclaringExtension().getNamespace(), (String) object);
		}			
	}
	
	public void setInitializationData(String pluginName, String exampleClassName){
		fPluginName = pluginName;
		fExampleClassName = exampleClassName;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#canRunForked()
	 */
	protected boolean canRunForked() {
		return !fPage.isEnclosingTypeSelected();
	}
	/*
	 * This method is an exact copy of super.super.performFinish() API.
	 * The reason for copying is that one cannot call that API without
	 * hitting a NPE.
	 */
	public boolean performFinishNewElement() {
		IWorkspaceRunnable op= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
				try {
					finishPage(monitor);
				} catch (InterruptedException e) {
					throw new OperationCanceledException(e.getMessage());
				}
			}
		};
		try {
			getContainer().run(canRunForked(), true, new WorkbenchRunnableAdapter(op, getSchedulingRule()));
		} catch (InvocationTargetException e) {
			handleFinishException(getShell(), e);
			return false;
		} catch  (InterruptedException e) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		warnAboutTypeCommentDeprecation();
		boolean res= performFinishNewElement();
		if (res) {
			IResource resource= fPage.getModifiedResource();
			if (resource != null) {
				selectAndReveal(resource);
				openResource(resource);
			}	
		}
		return res;
	}
}