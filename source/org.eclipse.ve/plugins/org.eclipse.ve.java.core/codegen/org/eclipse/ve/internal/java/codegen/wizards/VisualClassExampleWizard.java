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
package org.eclipse.ve.internal.java.codegen.wizards;
/*
 *  $RCSfile: VisualClassExampleWizard.java,v $
 *  $Revision: 1.16 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

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
	private String fContainerPlugin = null;
	private String fContainerName = null;
	
	public VisualClassExampleWizard() {
		super();
		setDefaultPageImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
		setDialogSettings(JavaVEPlugin.getPlugin().getDialogSettings());
		setWindowTitle(CodegenMessages.VisualClassExampleWizard_title); 
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

		// If present, add the container to the classpath of the project 
		if(fContainerPlugin!=null && fContainerName!=null){
			NewVisualClassCreationWizard.updateProjectClassPath(fContainerPlugin, fContainerName, fPage.getCreatedType().getJavaProject(), monitor);
		}
	}
	
	protected void openResource(IResource resource) {
		NewVisualClassCreationWizard.openResourceJVE(resource);
	}
	
	public void setInitializationData(IConfigurationElement element,String string,Object object){
		if(!"class".equals(string)) //$NON-NLS-1$
			return;
		if ( object instanceof String ) {
			setInitializationData(element.getDeclaringExtension().getNamespace(), (String) object);
		}else if(object instanceof Hashtable){
			Hashtable hash = (Hashtable) object;
			if(hash.containsKey("exampleFile")) //$NON-NLS-1$
				fExampleClassName = (String) hash.get("exampleFile"); //$NON-NLS-1$
			if(hash.containsKey("classpathContainerPlugin")) //$NON-NLS-1$
				fContainerPlugin = (String) hash.get("classpathContainerPlugin"); //$NON-NLS-1$
			if(hash.containsKey("classpathContainerName")) //$NON-NLS-1$
				fContainerName = (String) hash.get("classpathContainerName"); //$NON-NLS-1$
			setInitializationData(element.getDeclaringExtension().getNamespace(), fExampleClassName, fContainerPlugin, fContainerName);
		}
	}
	
	public void setInitializationData(String pluginName, String exampleClassName){
		fPluginName = pluginName;
		fExampleClassName = exampleClassName;
	}

	protected void setInitializationData(String pluginName, String exampleClassName, String containerPlugin, String containerName){
		setInitializationData(pluginName, exampleClassName);
		fContainerPlugin = containerPlugin;
		fContainerName = containerName;
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
