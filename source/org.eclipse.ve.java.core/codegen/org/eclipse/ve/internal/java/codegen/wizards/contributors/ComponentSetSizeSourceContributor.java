/*
 * Created on Jun 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.wizards.contributors;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComponentSetSizeSourceContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.net.URL;

import org.eclipse.core.runtime.Path;


import org.eclipse.ve.internal.java.codegen.wizards.IVisualClassCreationSourceContributor;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


/**
 * @author sri
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComponentSetSizeSourceContributor implements IVisualClassCreationSourceContributor{

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.core.codegen.wizards.IVisualClassCreationSourceContributor#needsFormatting()
 */
public boolean needsFormatting() {
	return true;
}

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.java.core.codegen.wizards.IVisualClassCreationSourceContributor#getTemplateLocation()
 */
public URL getTemplateLocation() {
	return JavaVEPlugin.getPlugin().find(new Path("templates/org/eclipse/ve/internal/java/codegen/jjet/wizards/contributors/ComponentSetSizeSourceTemplate.javajet"));
}


}
