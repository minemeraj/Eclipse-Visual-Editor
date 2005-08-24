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
 * Created on Jun 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.jfc.codegen.wizards.contributors;
/*
 *  $RCSfile: ContentPaneSourceContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;

import org.eclipse.ve.internal.jfc.core.JFCVisualPlugin;

public class ContentPaneSourceContributor implements IVisualClassCreationSourceContributor{

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
	return JFCVisualPlugin.getPlugin().find(new Path("templates/org/eclipse/ve/internal/jfc/codegen/jjet/wizards/contributors/JContentPaneSourceTemplate.javajet")); //$NON-NLS-1$
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getStatus(IResource)()
 */
public IStatus getStatus(IResource resource) {
	return null;
}

}
