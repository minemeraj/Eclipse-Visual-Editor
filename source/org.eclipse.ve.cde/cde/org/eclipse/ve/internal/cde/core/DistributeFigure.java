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
 *  $RCSfile: DistributeFigure.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */



import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/*
 * This figure is used to draw a boundary box around the selected editparts
 * so they can be distributed within it's surface area. It has selection 
 * handles (see DistributeHandle) in each corner and in between so allow
 * resizing the bounding area.
 */
public class DistributeFigure extends Polyline implements MouseMotionListener {
	protected Rectangle fBounds = new Rectangle();

public DistributeFigure () {
	
	setBackgroundColor(ColorConstants.blue);
	setLineStyle(Graphics.LINE_DASH);
	setForegroundColor(ColorConstants.white);
	
	setPointsBasedOnRectangle();
}

/**
 * Change the bounding box.
 */
public void setBoundingBox(Rectangle rect) {
	fBounds.setBounds(rect);
	setPointsBasedOnRectangle();
}

/*
 * One of the handles is moving... adjust the size of this figure
 * according to the mouse location.
 */
protected void handleDrag(MouseEvent me) {
	int locType = ((DistributeHandle)me.getSource()).getLocatorType();
	Point p = me.getLocation();
	switch (locType) {
		case (PositionConstants.NORTH) :
			{
				fBounds.height += (fBounds.y - p.y);
				fBounds.y = p.y;
				break;
			}
		case (PositionConstants.SOUTH) :
			{
				fBounds.height = p.y - fBounds.y;
				break;
			}
		case (PositionConstants.EAST) :
			{
				fBounds.width = p.x - fBounds.x;
				break;
			}
		case (PositionConstants.WEST) :
			{
				fBounds.width += (fBounds.x - p.x);
				fBounds.x = p.x;
				break;
			}
		case (PositionConstants.NORTH_WEST) :
			{
				fBounds.width += (fBounds.x - p.x);
				fBounds.x = p.x;
				fBounds.height += (fBounds.y - p.y);
				fBounds.y = p.y;
				break;
			}
		case (PositionConstants.NORTH_EAST) :
			{
				fBounds.width = p.x - fBounds.x;
				fBounds.height += (fBounds.y - p.y);
				fBounds.y = p.y;
				break;
			}
		case (PositionConstants.SOUTH_WEST) :
			{
				fBounds.width += (fBounds.x - p.x);
				fBounds.x = p.x;
				fBounds.height = p.y - fBounds.y;
				break;
			}
		case (PositionConstants.SOUTH_EAST) :
			{
				fBounds.width = p.x - fBounds.x;
				fBounds.height = p.y - fBounds.y;
				break;
			}
		default :
			{
				// default is the NORTH location.
				fBounds.height += (fBounds.y - p.y);
				fBounds.y = p.y;
				break;
			}
	}
	setPointsBasedOnRectangle();
}
public void mouseHover(MouseEvent me){};
public void mouseMoved(MouseEvent me) {};
public void mouseEntered(MouseEvent me){};
public void mouseExited(MouseEvent me){};
public void mouseDragged(MouseEvent me){
	handleDrag(me);
}
protected void setPointsBasedOnRectangle() {
	PointList pl = new PointList(5);
	
	pl.addPoint(fBounds.getTopLeft());
	pl.addPoint(fBounds.getTopRight());
	pl.addPoint(fBounds.getBottomRight());
	pl.addPoint(fBounds.getBottomLeft());
	pl.addPoint(fBounds.getTopLeft());
	setPoints(pl);
	fireFigureMoved();	// Need to do this because handles are listening for changes and setting points doesn't fire move.
	if (isCoordinateSystem())
		fireCoordinateSystemChanged();	// ? if moved and has local coordinate system, fire this too. Don't understand why, it's just the way GEF works.
}
/* (non-Javadoc)
 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
 */
protected void outlineShape(Graphics g) {
	g.setXORMode(true);
	super.outlineShape(g);
}
}
