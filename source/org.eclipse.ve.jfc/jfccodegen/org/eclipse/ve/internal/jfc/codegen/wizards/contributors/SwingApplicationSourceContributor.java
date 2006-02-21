/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SwingApplicationSourceContributor.java,v $
 *  $Revision: 1.3 $  $Date: 2006-02-21 17:16:37 $ 
 */

package org.eclipse.ve.internal.jfc.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;

import org.eclipse.ve.internal.jfc.core.JFCVisualPlugin;

public class SwingApplicationSourceContributor implements IVisualClassCreationSourceContributor{

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
	return FileLocator.find(JFCVisualPlugin.getPlugin().getBundle(), new Path("templates/org/eclipse/ve/internal/jfc/codegen/jjet/wizards/contributors/SwingApplicationSourceTemplate.javajet"), null); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getStatus(IResource)()
 */
public IStatus getStatus(IResource resource) {
	return null;
}

}
