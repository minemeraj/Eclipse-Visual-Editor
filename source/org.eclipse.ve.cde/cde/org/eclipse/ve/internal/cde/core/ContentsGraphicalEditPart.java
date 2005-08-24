/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: ContentsGraphicalEditPart.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DeselectAllTracker;
/**
 * CDE Contents (Freeform surface) Graphical Edit Part.
 * Override to perform domain specific functions. 
 * This provides the necessary functions for maintaining the
 * freeform surface, such as positioning, zooming, ...
 */

public abstract class ContentsGraphicalEditPart extends AbstractGraphicalEditPart implements IFreeFormRoot {
		
/**
 * Set the figure's layout manager to be an XYFlowLayout.
 */
protected IFigure createFigure() {
	IFigure contentPane = new FreeformLayer();
	contentPane.setLayoutManager(new FreeformXYFlowLayout());	
	contentPane.setBackgroundColor(ColorConstants.white);
	contentPane.setOpaque(true);
	return contentPane;
}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		// Need to return a de-selection tracker for the contents edit part
		// so the free form doesn't scroll to position 0,0
		return new DeselectAllTracker(this);
	}

}
