/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ShellSourceContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-28 23:15:59 $ 
 */
package org.eclipse.ve.internal.java.codegen.wizards.contributors;

import java.net.URL;

import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

/**
 * 
 * @since 1.0.0
 */
public class ShellSourceContributor implements IVisualClassCreationSourceContributor {

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
		return JavaVEPlugin.getPlugin().find(new Path("templates/org/eclipse/ve/internal/java/codegen/jjet/wizards/contributors/ShellSourceTemplate.javajet"));	}
}
