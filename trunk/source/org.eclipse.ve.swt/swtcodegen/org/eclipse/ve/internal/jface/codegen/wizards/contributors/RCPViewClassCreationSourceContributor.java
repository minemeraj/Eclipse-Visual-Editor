/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RCPViewClassCreationSourceContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-18 16:44:52 $ 
 */
package org.eclipse.ve.internal.jface.codegen.wizards.contributors;

import java.net.URL;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;

import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;
import org.eclipse.ve.internal.java.codegen.wizards.StatusInfo;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.jface.JFaceMessages;
import org.eclipse.ve.internal.swt.SwtPlugin;
 

public class RCPViewClassCreationSourceContributor implements IVisualClassCreationSourceContributor{

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#needsFormatting()
	 */
	public boolean needsFormatting() {
		return true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getTemplateLocation()
	 */
	public URL getTemplateLocation() {
		return SwtPlugin.getDefault().find(new Path("templates/org/eclipse/ve/internal/swt/codegen/jjet/wizards/contributors/RCPViewTemplate.javajet")); //$NON-NLS-1$
	}

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
				JavaProject javaProject = (JavaProject) project.getNature(JavaCore.NATURE_ID);
				if(javaProject == null){
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
					JFaceMessages.getFormattedString(
							"RCPViewCreation.RCP_Project_ERROR_", //$NON-NLS-1$
							project == null ? null : project.getName()),
					SwtPlugin.PLUGIN_ID);
		} else {
			return StatusInfo.OK_STATUS;
		}
	}			
		
}
