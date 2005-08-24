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
 *  $RCSfile: DistributeHandle.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */



import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/*
 * A selection handle used to select and resize a DistributionFigure.
 */
public class DistributeHandle extends Figure implements FigureListener {
protected Cursor cursor;
protected Locator locator;
protected DistributeFigure owner;
protected int fLocatorType;

protected final static int DEFAULT_HANDLE_SIZE = 7;

/**
 * Creates a handle for the owner using the given Locator.
 * The locatorType are PositionConstants.
 */
public DistributeHandle(DistributeFigure fig, int locatorType) {
	owner = fig;
	fLocatorType = locatorType;
	createRelativeLocator();
	setDragCursor(Cursors.getDirectionalCursor(locatorType));
	setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE, DEFAULT_HANDLE_SIZE));
}

/**
 * Adds this as a {@link FigureListener} to the owner's {@link Figure}.
 */
public void addNotify() {
	// Listen to the owner figure so the handle moves when the
	// figure moves.
	getOwner().addFigureListener(this);
	// Also have the owner listen when this is dragged.
	addMouseMotionListener(getOwner());
}
/**
 * Create a RelativeLocator based on the locator type.
 */
protected void createRelativeLocator() {
	switch (fLocatorType) {
		case (PositionConstants.NORTH) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.SOUTH) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.EAST) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.WEST) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.NORTH_WEST) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.NORTH_EAST) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.SOUTH_WEST) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		case (PositionConstants.SOUTH_EAST) :
			{
				locator = new RelativeLocator(owner, fLocatorType);
				break;
			}
		default :
			{
				// default to NORTH location
				locator = new RelativeLocator(owner, PositionConstants.NORTH);
				break;
			}
	}
}
/**
 * Updates this handle's position when the parent figure moves.
 *
 * @param source The parent figure.
 */
public void figureMoved(IFigure source) {
	revalidate();
}
/**
 * Returns the color of the handle's border.
 */
protected Color getBorderColor() {
	return ColorConstants.blue; 
}
/**
 * Returns the color of the handle.
 */
protected Color getFillColor() {
	return ColorConstants.blue;
}
/**
 * Returns the Cursor that appears over the handle.
 */
public Cursor getCursor() {
	return cursor;
}
/**
 * Returns the Locator associated with this handle.
 */
public Locator getLocator() {
	return locator;
}
/**
 * Returns the Locator type associated with this handle.
 * i.e. NORTH, SOUTH, EAST, WEST, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST
 */
public int getLocatorType() {
	return fLocatorType;
}
/**
 * Returns the owner associated with this handle.
 */
public DistributeFigure getOwner() {
	return owner;
}
public void handleMousePressed(MouseEvent e){
	super.handleMousePressed(e);
	e.consume();
}
public void handleMouseReleased(MouseEvent e){
	super.handleMouseReleased(e);
	e.consume();
}
public void handleMouseMoved(MouseEvent e){
	super.handleMouseMoved(e);
	e.consume();
}
protected boolean isMouseEventTarget() {return true;}
public void removeNotify() {
	getOwner().removeFigureListener(this);
}
public void handleMouseDragged(MouseEvent e){
	super.handleMouseDragged(e);
	e.consume();
}
/**
 * Draws the handle with fill color and outline color.
 *
 * @param g The graphics used to paint the figure.
 */
public void paintFigure(Graphics g) {
	Rectangle r = getBounds();
	r.shrink(1,1);
	try {
		g.setBackgroundColor(getFillColor());
		g.fillRectangle(r.x, r.y, r.width, r.height);
		g.setForegroundColor(getBorderColor()); 
		g.drawRectangle(r.x, r.y, r.width, r.height);
	} finally {
		//We don't really own rect 'r', so fix it.
		r.expand(1,1);
	}
}
public void setDragCursor(Cursor c) {
	cursor = c;
}
public void validate() {
	getLocator().relocate(this);
	super.validate();
}
}
