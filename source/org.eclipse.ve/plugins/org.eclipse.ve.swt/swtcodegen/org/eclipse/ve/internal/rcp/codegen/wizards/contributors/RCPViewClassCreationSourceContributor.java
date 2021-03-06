/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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
 *  $Revision: 1.3 $  $Date: 2006-05-17 20:15:54 $ 
 */
package org.eclipse.ve.internal.rcp.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.swt.SwtPlugin;
 

public class RCPViewClassCreationSourceContributor extends AbstractRCPClassCreationSourceContributor{
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getTemplateLocation()
	 */
	public URL getTemplateLocation() {
		return FileLocator.find(SwtPlugin.getDefault().getBundle(), new Path("templates/org/eclipse/ve/internal/swt/codegen/jjet/wizards/contributors/RCPViewTemplate.javajet"), null); //$NON-NLS-1$
	}
}
