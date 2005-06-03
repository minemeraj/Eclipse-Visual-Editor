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
 *  $RCSfile: RCPEditorClassCreationSourceContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-03 21:33:03 $ 
 */
package org.eclipse.ve.internal.jface.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.swt.SwtPlugin;
 

public class RCPEditorClassCreationSourceContributor extends AbstractRCPClassCreationSourceContributor{

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor#getTemplateLocation()
	 */
	public URL getTemplateLocation() {
		return SwtPlugin.getDefault().find(new Path("templates/org/eclipse/ve/internal/swt/codegen/jjet/wizards/contributors/RCPEditorTemplate.javajet")); //$NON-NLS-1$
	}
}
