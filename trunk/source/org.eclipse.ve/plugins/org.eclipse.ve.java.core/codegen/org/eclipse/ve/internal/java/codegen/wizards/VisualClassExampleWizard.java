/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.wizards;
/*
 *  $RCSfile: VisualClassExampleWizard.java,v $
 *  $Revision: 1.10 $  $Date: 2004-11-12 19:45:12 $ 
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
	private String fContainerPlugin = null;
	private String fContainerName = null;
	
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

		// If present, add the container to the classpath of the project 
		if(fContainerPlugin!=null && fContainerName!=null){
			NewVisualClassCreationWizard.updateProjectClassPath(fContainerPlugin, fContainerName, fPage.getCreatedType().getJavaProject(), monitor);
		}
	}
	
	protected void openResource(IResource resource) {
		NewVisualClassCreationWizard.openResourceJVE(resource);
	}
	
	public void setInitializationData(IConfigurationElement element,String string,Object object){
		if(!"class".equals(string))
			return;
		if ( object instanceof String ) {
			setInitializationData(element.getDeclaringExtension().getNamespace(), (String) object);
		}else {
			IConfigurationElement[] wizardChildren = element.getChildren();
			if(wizardChildren!=null && wizardChildren.length>0){
				for (int wcc = 0; wcc < wizardChildren.length; wcc++) {
					if("class".equals(wizardChildren[wcc].getName())){
						IConfigurationElement[] classChildren = wizardChildren[wcc].getChildren();
						if(classChildren!=null && classChildren.length>0){
							String pluginName=element.getDeclaringExtension().getNamespace();
							String exampleClassName=null, containerPlugin=null, containerName=null;
							for (int ccc = 0; ccc < classChildren.length; ccc++) {
								String[] attrNames = classChildren[ccc].getAttributeNames();
								if("exampleFile".equals(attrNames[0])){
									exampleClassName = classChildren[ccc].getAttribute("exampleFile");
								}else if("classpathContainerPlugin".equals(attrNames[0])){
									containerPlugin = classChildren[ccc].getAttribute("classpathContainerPlugin");
								}else if("classpathContainerName".equals(attrNames[0])){
									containerName = classChildren[ccc].getAttribute("classpathContainerName");
								}
							}
							setInitializationData(pluginName, exampleClassName, containerPlugin, containerName);
						}
					}
				}
			}
		}
	}
	
	protected void setInitializationData(String pluginName, String exampleClassName){
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
