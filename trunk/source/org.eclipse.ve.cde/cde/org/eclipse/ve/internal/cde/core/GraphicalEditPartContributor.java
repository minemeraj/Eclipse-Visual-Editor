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
 *  $RCSfile: GraphicalEditPartContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-20 22:30:51 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;

/**
 * Graphical Editpart Contributor inteface.
 * 
 * @since 1.2.0
 */
public interface GraphicalEditPartContributor extends EditPartContributor {

	/*
	 * Returns a tool tip processor that can be added to the tool tip of the graphical editpart
	 */
	public ToolTipProcessor getHoverOverLay();

	/*
	 * Returns an image figure or label that can be added to the graphical editpart's main figure
	 */
	public IFigure getFigureOverLay();

	/*
	 * Returns the children to be used to populate the the action bar.
	 * In order to maintain a fairly small action bar, the figures should be small and icons 
	 * that represent them no larger than 16x16 pixels. 
	 */
	public GraphicalEditPart[] getActionBarChildren();
}
