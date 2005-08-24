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
package org.eclipse.ve.internal.java.codegen.wizards;
/*
 *  $RCSfile: IVisualClassCreationSourceContributor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;

public interface IVisualClassCreationSourceContributor {
	
	public boolean needsFormatting();
	public URL getTemplateLocation();
	/** 
	 * @param resource Representing the container that the template is requesting to create itself into
	 * @return String.  If the contributor is valid then return null, otherwise a status reason as to why it is invalid
	 * This could be because the project build path is incorrect or the wrong type
	 */
	public IStatus getStatus(IResource resource);
}
