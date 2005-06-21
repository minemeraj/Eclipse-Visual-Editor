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
 *  $RCSfile: IVEContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-21 19:53:10 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
 
/**
 * Contributor to the Java Visual Editor
 * Note: This is still very experimental. This will change as we enable customization.
 * 
 * @deprecated Use IVEContributor1 instead. This interface will be removed in VE 1.1
 * 
 * @see org.eclipse.ve.internal.java.codegen.editorpart.IVEContributor1
 * @since 1.0.0
 */
public interface IVEContributor {
	
	/**
	 * Contribute to the list of containers to put on the palette. The list may be added to, removed from, reordered,
	 * but do not modify any of the containers themselves. This is because they are just references to the originals
	 * and we cannot reconstruct the palette correctly if they are changed.
	 * <p>
	 * This may be called more than once, so do appropriate checks so as not to remodify the list more than necessary.
	 * 
	 * @param currentContainers the list of categories for the palette. The entries must be an instance of {@link org.eclipse.ve.internal.cde.palette.Container}.
	 * @param rset resource set to use to load containers xmi files into. 
	 * @return <code>true</code> if contributor modified the containers, <code>false</code> if not touched. This is used to determine whether to rebuild palette or not.
	 * 
	 * @since 1.0.0
	 */
	public boolean contributePalleteCats(List currentContainers, ResourceSet rset);
}
