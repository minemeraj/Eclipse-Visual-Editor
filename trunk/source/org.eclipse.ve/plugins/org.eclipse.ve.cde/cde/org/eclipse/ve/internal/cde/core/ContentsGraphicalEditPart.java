package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ContentsGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DeselectAllTracker;
/**
 * CDE Contents (Freeform surface) Graphical Edit Part.
 * Override to perform domain specific functions. 
 * This provides the necessary functions for maintaining the
 * freeform surface, such as positioning, zooming, ...
 */

public abstract class ContentsGraphicalEditPart extends AbstractGraphicalEditPart {
		
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

/**
 * Apart from the EditPolicy updates and refreshes of super,
 * this also sets the layout of the CONNECTION_LAYER to
 * reflect the connection updates.
 */
public void activate() {
	// Get the connection layer from the drawing and set its connection 
	// layout manager.  This allows one instance of a connection layout
	// manager to be shared by all connections.
	ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
	// The fan routre will resolve conflicts and provide auto-bending functionality
	// This occurs after manual bending ( handled by the BendableConnectionRouter )
	FanRouter chainRouter = new FanRouter();
	chainRouter.setNextRouter(new BendpointConnectionRouter());
	cLayer.setConnectionRouter(chainRouter);
	super.activate();
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