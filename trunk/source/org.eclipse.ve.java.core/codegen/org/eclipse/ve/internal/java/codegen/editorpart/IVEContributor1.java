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
 *  $RCSfile: IVEContributor1.java,v $
 *  $Revision: 1.1 $  $Date: 2004-11-02 19:13:53 $ 
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
	 * Contribute to the list of categories to put on the palette. The list should only be added to. Do not modify, move, or remove any
	 * other category in the list.
	 * <p>
	 * This may be called more than once, so do appropriate checks so as not to remodify the list more than necessary.
	 * <p>
	 * This is pass 1. It will be called first for all contributors to allow all cats to be added. Pass 2 will allow
	 * the cats to be reordered or deleted. 
	 * 
	 * @param currentCategories the list of categories for the palette. The entries must be an instance of <code>org.eclipse.ve.internal.cde.palette.Category</code>
	 * @param rset resource set to use to load categories xmi files into. 
	 * @return <code>true</code> if contributor modified the categories, <code>false</code> if not touched. This is used to determine whether to rebuild palette or not.
	 * 
	 * @since 1.0.2
	 */
	public boolean contributePalleteCats(List currentCategories, ResourceSet rset);

	/**
	 * Modify the list of palette categories.
	 * <p>
	 * This is called in pass 2. At this point in time all of the categories have been added. The method implementation may then reorder or remove
	 * categories.
	 * <p>
	 * Note: Do not modify any of the categories themselves. This is because they are just references to the originals
	 * and we cannot reconstruct the palette correctly if they are changed.
	 * 
	 * @param currentCategories the list of categories for the palette. The entries must be an instance of <code>org.eclipse.ve.internal.cde.palette.Category</code>
	 * @return <code>true</code> if contributor modified the categories, <code>false</code> if not touched. This is used to determine whether to rebuild palette or not.
	 * 
	 * @since 1.0.2
	 */
	public boolean modifyPaletteCatsList(List currentCategories);
}
