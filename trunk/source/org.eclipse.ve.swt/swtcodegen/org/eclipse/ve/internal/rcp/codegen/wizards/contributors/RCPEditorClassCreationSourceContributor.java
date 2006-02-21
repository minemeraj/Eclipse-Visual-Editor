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
 *  $RCSfile: RCPEditorClassCreationSourceContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2006-02-21 17:16:40 $ 
 */
package org.eclipse.ve.internal.rcp.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.swt.SwtPlugin;
 

public class RCPEditorClassCreationSourceContributor extends AbstractRCPClassCreationSourceContributor{

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getTemplateLocation()
	 */
	public URL getTemplateLocation() {
		return FileLocator.find(SwtPlugin.getDefault().getBundle(), new Path("templates/org/eclipse/ve/internal/swt/codegen/jjet/wizards/contributors/RCPEditorTemplate.javajet"), null); //$NON-NLS-1$
	}
}
