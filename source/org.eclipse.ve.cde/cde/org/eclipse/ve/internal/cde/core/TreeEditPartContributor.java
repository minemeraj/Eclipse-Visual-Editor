/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TreeEditPartContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-12-02 21:17:46 $ 
 */
package org.eclipse.ve.internal.cde.core;

/**
 * Editpart contributor for Tree Editparts.
 * 
 * @since 1.2.0
 */
public interface TreeEditPartContributor extends EditPartContributor {

	/**
	 * Get the image overlay, if any.
	 * @return image overlay or <code>null</code> if none.
	 * 
	 * @since 1.2.0
	 */
	ImageOverlay getImageOverlay();

	/**
	 * Modify the text to be displayed.
	 * @param text
	 * @return
	 * 
	 * @since 1.2.0
	 */
	String modifyText(String text);
	
}
