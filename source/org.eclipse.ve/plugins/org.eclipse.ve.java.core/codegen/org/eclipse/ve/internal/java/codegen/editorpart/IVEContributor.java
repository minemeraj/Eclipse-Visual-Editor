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
 *  $RCSfile: IVEContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-22 23:49:37 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
 
/**
 * Contributor to the Java Visual Editor
 * Note: This is still very experimental. This will change as we enable customization.
 * @since 1.0.0
 */
public interface IVEContributor {
	
	/**
	 * Contribute to the list of categories to put on the palette. The list may be added to, removed from, reordered,
	 * but do not modify any of the categories themselves. This is because they are just references to the originals
	 * and we cannot reconstruct the palette correctly if they are changed.
	 * <p>
	 * This may be called more than once, so do appropriate checks so as not to remodify the list more than necessary.
	 * 
	 * @param currentCategories the list of categories for the palette. The entries must be an instance of <code>org.eclipse.ve.internal.cde.palette.Category</code>
	 * @param rset resource set to use to load categories xmi files into. 
	 * @return <code>true</code> if contributor modified the categories, <code>false</code> if not touched. This is used to determine whether to rebuild palette or not.
	 * 
	 * @since 1.0.0
	 */
	public boolean contributePalleteCats(List currentCategories, ResourceSet rset);
}
