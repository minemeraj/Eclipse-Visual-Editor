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
 *  $RCSfile: IVEContributor1.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
 

/**
 * Contributor to the Java Visual Editor
 * Note: This is still very experimental. This will change as we enable customization.
 * 
 * @since 1.0.2
 */
public interface IVEContributor1 {

	/**
	 * Contribute to the list of containers to put on the palette. The list should only be added to. Do not modify, move, or remove any
	 * other container in the list.
	 * <p>
	 * This may be called more than once, so do appropriate checks so as not to remodify the list more than necessary.
	 * <p>
	 * This is pass 1. It will be called first for all contributors to allow all containers to be added. Pass 2 will allow
	 * the containers to be reordered or deleted. 
	 * 
	 * @param currentContainers the list of containers for the palette. The entries must be an instance of {@link org.eclipse.ve.internal.cde.palette.Container}
	 * @param rset resource set to use to load container xmi files into. 
	 * @return <code>true</code> if contributor modified the containers list, <code>false</code> if not touched. This is used to determine whether to rebuild palette or not.
	 * 
	 * @since 1.0.2
	 */
	public boolean contributePalleteCats(List currentContainers, ResourceSet rset);

	/**
	 * Modify the list of palette containers.
	 * <p>
	 * This is called in pass 2. At this point in time all of the containers have been added. The method implementation may then reorder or remove
	 * containers.
	 * <p>
	 * Note: Do not modify any of the containers themselves. This is because they are just references to the originals
	 * and we cannot reconstruct the palette correctly if they are changed.
	 * 
	 * @param currentContainers the list of containers for the palette. The entries must be an instance of {@link org.eclipse.ve.internal.cde.palette.Container}.
	 * @return <code>true</code> if contributor modified the categories, <code>false</code> if not touched. This is used to determine whether to rebuild palette or not.
	 * 
	 * @since 1.0.2
	 */
	public boolean modifyPaletteCatsList(List currentContainers);
}
