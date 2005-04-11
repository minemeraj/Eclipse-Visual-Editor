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
 *  $Revision: 1.3 $  $Date: 2005-04-11 22:18:10 $ 
 */
package org.eclipse.ve.internal.jface;

import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
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
		return JavaVEPlugin.getPlugin().find(new Path("templates/org/eclipse/ve/internal/java/codegen/jjet/wizards/contributors/RCPViewTemplate.javajet")); //$NON-NLS-1$
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
				isError = !ProxyPlugin.isPDEProject(javaProject);
			} catch (CoreException e) {
				JavaVEPlugin.log(e, Level.FINEST);
				return new IVisualClassCreationSourceContributor.StatusInfo(
						IStatus.ERROR,
						e.getMessage(),
						SwtPlugin.PLUGIN_ID);
			}
		}

		if(isError){
			String errorMessage = "Project must be a Plug-in Project to create a ViewPart";	
			return new IVisualClassCreationSourceContributor.StatusInfo(
					IStatus.ERROR,
					errorMessage,
					SwtPlugin.PLUGIN_ID);
		} else {
			return IVisualClassCreationSourceContributor.OK_STATUS;
		}
	}			
		
}
