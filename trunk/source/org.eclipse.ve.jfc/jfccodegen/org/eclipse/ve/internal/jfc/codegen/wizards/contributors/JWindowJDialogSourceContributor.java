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
 *  $RCSfile: JWindowJDialogSourceContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-11-28 23:24:49 $ 
 */
package org.eclipse.ve.internal.jfc.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;

import org.eclipse.ve.internal.jfc.core.JFCVisualPlugin;

public class JWindowJDialogSourceContributor implements IVisualClassCreationSourceContributor {

	public boolean needsFormatting() {
		return true;
	}

	public URL getTemplateLocation() {
		return JFCVisualPlugin.getPlugin().find(
				new Path("templates/org/eclipse/ve/internal/jfc/codegen/jjet/wizards/contributors/JWindowJDialogSourceTemplate.javajet")); //$NON-NLS-1$
	}

	public IStatus getStatus(IResource resource) {
		return null;
	}

}
