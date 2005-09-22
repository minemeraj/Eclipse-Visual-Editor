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
 *  $RCSfile: AdaptableContributorFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2005-09-22 12:55:53 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;

/**
 * Factory for Adaptable Contributors. It is contributed through the "org.eclipse.ve.contributor" extension point.
 * <p>
 * There will only be one instance per editor, so it should not maintain state. 
 * It will not be told when it is no longer of use so any state may hold onto system resoures by accident.
 * <p>
 * The contributors themselves should be individual and one per editpart and may maintain state. They will be told when to dispose.
 * <p>
 * The factory will be selected via the filter mechanism in the extension point.
 * @since 1.2.0
 */
public interface AdaptableContributorFactory {

	/**
	 * Return the tree edit part contributor or null if it will not be contributing.
	 * @param treeEditPart
	 * @return tree edit part contributor or <code>null</code> if not contributing.
	 * 
	 * @since 1.2.0
	 */
	TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart);

	/**
	 * Return the graphical edit part contributor or null if it will not be contributing.
	 * @param graphicalEditPart
	 * @return graphical edit part contributor or <code>null</code> if not contributing.
	 * 
	 * @since 1.2.0
	 */
	GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart);
	
}
