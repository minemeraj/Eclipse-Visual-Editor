/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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
 *  $Revision: 1.3 $  $Date: 2006-05-17 20:14:59 $ 
 */
package org.eclipse.ve.internal.jfc.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;

import org.eclipse.ve.internal.jfc.core.JFCVisualPlugin;

public class JWindowJDialogSourceContributor implements IVisualClassCreationSourceContributor {

	public boolean needsFormatting() {
		return true;
	}

	public URL getTemplateLocation() {
		return FileLocator.find(JFCVisualPlugin.getPlugin().getBundle(),
				new Path("templates/org/eclipse/ve/internal/jfc/codegen/jjet/wizards/contributors/JWindowJDialogSourceTemplate.javajet"), null); //$NON-NLS-1$
	}

	public IStatus getStatus(IResource resource) {
		return null;
	}

}
