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
 *  $Revision: 1.3 $  $Date: 2005-04-05 22:48:22 $ 
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
		return JavaVEPlugin.getPlugin().find(new Path("templates/org/eclipse/ve/internal/java/codegen/jjet/wizards/contributors/CompositeSourceTemplate.javajet"));	} //$NON-NLS-1$
}
