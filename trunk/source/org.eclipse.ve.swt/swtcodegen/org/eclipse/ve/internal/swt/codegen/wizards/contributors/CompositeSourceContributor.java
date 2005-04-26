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
 *  $RCSfile: CompositeSourceContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-26 21:39:44 $ 
 */
package org.eclipse.ve.internal.swt.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;

import org.eclipse.ve.internal.swt.SwtPlugin;
 

/**
 * 
 * @since 1.0.0
 */
public class CompositeSourceContributor implements IVisualClassCreationSourceContributor {

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
		return SwtPlugin.getDefault().find(new Path("templates/org/eclipse/ve/internal/swt/codegen/jjet/wizards/contributors/CompositeSourceTemplate.javajet"));	} //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getStatus(IResource)()
     */
	public IStatus getStatus(IResource resource) {
		return null;
	}
}
