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
/*
 *  $RCSfile: AbstractRCPClassCreationSourceContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:52:57 $ 
 */
package org.eclipse.ve.internal.jface.codegen.wizards.contributors;

import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;
import org.eclipse.ve.internal.java.codegen.wizards.StatusInfo;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.jface.JFaceMessages;
import org.eclipse.ve.internal.swt.SwtPlugin;
 

public abstract class AbstractRCPClassCreationSourceContributor implements IVisualClassCreationSourceContributor{

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#needsFormatting()
	 */
	public boolean needsFormatting() {
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getTemplateLocation()
	 */
	public abstract URL getTemplateLocation() ;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getStatus(IResource)()
	 */
	public IStatus getStatus(IResource resource) {
		IProject project = resource.getProject();
		boolean isError = false;
		if(project == null){
			isError = true;
		} else {
			try {
				IJavaProject javaProject = JavaCore.create(project);
				if(!javaProject.exists()){
					isError = true;
				} else {
					isError = !ProxyPlugin.isPDEProject(javaProject);
				}
			} catch (CoreException e) {
				JavaVEPlugin.log(e, Level.FINEST);
				return new StatusInfo(
						IStatus.ERROR,
						e.getMessage(),
						SwtPlugin.PLUGIN_ID);
			}
		}

		if(isError){ 	
			return new StatusInfo(
					IStatus.ERROR,
					MessageFormat.format(JFaceMessages.RCPViewCreation_RCP_Project_ERROR_, 
					new Object[]{project == null ? null : project.getName()}),
					SwtPlugin.PLUGIN_ID);
		} else {
			return StatusInfo.OK_STATUS;
		}
	}			
		
}
