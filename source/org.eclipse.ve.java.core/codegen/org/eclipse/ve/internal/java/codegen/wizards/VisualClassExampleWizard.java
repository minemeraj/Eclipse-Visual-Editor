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
 *  $RCSfile: VisualClassExampleWizard.java,v $
 *  $Revision: 1.18 $  $Date: 2006-02-21 17:16:35 $ 
 */
package org.eclipse.ve.internal.java.codegen.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


 

public class VisualClassExampleWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	protected VisualClassExampleWizardPage page = null;
	protected IStructuredSelection selection = null;
	protected IWorkbench workbench = null;
	
	// the class name is going to be searched in the Examples directory of the
	// contributing plugin
	private String exampleClassName;
	private String pluginName = null ;
	private String containerPlugin = null;
	private String containerName = null;

	public VisualClassExampleWizard(){
		super();
		setDefaultPageImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
		setDialogSettings(JavaVEPlugin.getPlugin().getDialogSettings());
		setWindowTitle(CodegenMessages.VisualClassExampleWizard_title); 
	}
	
	public void addPages() {
		page = new VisualClassExampleWizardPage();
		addPage(page);
		page.init(selection);
		page.setSuperClass("javax.swing.JFrame", false); //$NON-NLS-1$
		page.setPluginName(pluginName);
		page.setTypeName(exampleClassName, false);
	}
	
	public boolean performFinish() {
		boolean res= performFinishNewElement();
		if (res) {
			IResource resource= page.getModifiedResource();
			if (resource != null) {
				BasicNewResourceWizard.selectAndReveal(resource, workbench.getActiveWorkbenchWindow());
				NewVisualClassCreationWizard.openResourceJVE(resource);
			}	
		}
		return res;
	}

	/*
	 * This method is an exact copy of super.super.performFinish() API.
	 * The reason for copying is that one cannot call that API without
	 * hitting a NPE.
	 */
	public boolean performFinishNewElement() {
		boolean result = true;
		final IWorkspaceRunnable op= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException, OperationCanceledException {
				try {
					finishPage(monitor);
				} catch (InterruptedException e) {
					throw new OperationCanceledException(e.getMessage());
				}
			}
		};
		try {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						JavaCore.run(op, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
				}
			};
			getContainer().run(true, true, runnable);
		} catch (InvocationTargetException e) {
			JavaVEPlugin.log(e.getCause());
			result = false;
		} catch  (InterruptedException e) {
			result = false;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#finishPage(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		page.createType(monitor); // use the full progress monitor

		// If present, add the container to the classpath of the project 
		if(containerPlugin!=null && containerName!=null){
			NewVisualClassCreationWizard.updateProjectClassPath(containerPlugin, containerName, page.getCreatedType().getJavaProject(), monitor);
		}
	}


	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	public void setInitializationData(String pluginName, String exampleClassName){
		this.pluginName = pluginName;
		this.exampleClassName = exampleClassName;
	}
	
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if(!"class".equals(propertyName)) //$NON-NLS-1$
			return;
		if ( data instanceof String ) {
			setInitializationData(config.getDeclaringExtension().getContributor().getName(), (String) data);
		}else if(data instanceof Hashtable){
			Hashtable hash = (Hashtable) data;
			if(hash.containsKey("exampleFile")) //$NON-NLS-1$
				exampleClassName = (String) hash.get("exampleFile"); //$NON-NLS-1$
			if(hash.containsKey("classpathContainerPlugin")) //$NON-NLS-1$
				containerPlugin = (String) hash.get("classpathContainerPlugin"); //$NON-NLS-1$
			if(hash.containsKey("classpathContainerName")) //$NON-NLS-1$
				containerName = (String) hash.get("classpathContainerName"); //$NON-NLS-1$
			setInitializationData(config.getDeclaringExtension().getContributor().getName(), exampleClassName, containerPlugin, containerName);
		}
	}
	
	protected void setInitializationData(String pluginName, String exampleClassName, String containerPlugin, String containerName){
		setInitializationData(pluginName, exampleClassName);
		this.containerPlugin = containerPlugin;
		this.containerName = containerName;
	}

}
