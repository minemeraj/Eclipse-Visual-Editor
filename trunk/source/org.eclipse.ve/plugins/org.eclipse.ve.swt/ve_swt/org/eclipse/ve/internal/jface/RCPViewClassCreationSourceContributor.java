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
 *  $RCSfile: RCPViewClassCreationSourceContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-04-05 20:11:46 $ 
 */
package org.eclipse.ve.internal.jface;

import java.net.URL;

import org.eclipse.core.runtime.Path;

import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

public class RCPViewClassCreationSourceContributor implements IVisualClassCreationSourceContributor {

	public boolean needsFormatting() {
		return true;
	}

	public URL getTemplateLocation() {
		return JavaVEPlugin.getPlugin().find(new Path("templates/org/eclipse/ve/internal/java/codegen/jjet/wizards/contributors/RCPViewTemplate.javajet")); //$NON-NLS-1$
	}

}
