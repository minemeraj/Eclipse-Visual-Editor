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
 *  $RCSfile: GridFigure.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:50 $ 
 */



import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;
import java.util.*;

/**
 * Grid figure draws a grid.  The grid parameters are listened for from
 * the grid controller, which it is created with. To figure the childRectangles
 * to avoid drawing in, it uses the parent and gets the siblings of this
 * figure, and uses the children of those figures. 
 *
 * It expects that:
 *   1) The layoutEditPolicy upon activation should add the grid figure to the EditPart's contentpane,
 *      remove it upon deactivation, and it should listen on the GridController and call repaint()
 *      on this GridFigure when anything changes so that it may redraw the new grid. 
 *      It is the layoutEditPolicy that controls all of this because only it knows 
 *      if it can handle a grid, and only it knows when it is active and inactive.
 *
 * This figure will ignore the parent layout manager and force itself to be the same
 * size and position as the parent pane.
 */
public class GridFigure extends Figure {
	protected static Color GridColor = ColorConstants.black;
	protected ZoomController zoomController;	// If this grid responds to zoom.
	protected GridController gridController;
	protected FigureListener figListener = new FigureListener() {
		public void figureMoved(IFigure source) {
			Rectangle rect = source.getBounds().getCopy();
			primSetBounds(rect);
		}
	};
	private GridData gridData = new GridData();	// Always use this one to save on creation/deletion. Painting is done only be one thread so it is safe.
	
	/**
	 * This is the appropriate grid data adjusted for
	 * zoom, if any.
	 */
	public static class GridData {
		Rectangle gridBorder;	// This is the grid border in terms of the content pane (it includes the content pane margin AND the grid margin).
		double gridWidth, gridHeight;	// Real ones with full precision
		int gridMargin;
		GridController gridController;	// The controller this data was created from.
	}

	
	public GridFigure(GridController gridController) {
		this.gridController = gridController;
	}
	
	public GridFigure(GridController gridController, ZoomController zoomController) {
		this(gridController);
		this.zoomController = zoomController;
	}
	
	/**
	 * Being added to a figure, start listening for changes to its position/size
	 * so that we are the same.
	 */
	public void addNotify() {
		super.addNotify();
		getParent().addFigureListener(figListener);
		figListener.figureMoved(getParent());	// Set its initial size/position
	}

	/**
	 * Being removed from a figure, stop listening for changes to its position/size
	 */
	public void removeNotify() {
		super.removeNotify();
		getParent().removeFigureListener(figListener);
	}
	
	/**
	 * Hide setBounds because we don't want to be whatever the
	 * parent's layout manager thought we should be.
	 */
	public void setBounds(Rectangle rect) {
	}
	
	/**
	 * Internal version so that we can set it.
	 */
	protected void primSetBounds(Rectangle rect) {
		super.setBounds(rect);
	}
	
	private Rectangle clipRect = new Rectangle();	// Always use this one to paint. Painting is done only be one thread so it is safe.
	/**
	 * Get the figure's size and draw a grid on it
	 */
	public void paintFigure(Graphics g){
	
		getGridData(gridData);
		// Only if we have not zoomed it to less than 2 pixel per grid in both directions, can we do a grid.
		if (gridData.gridWidth >= 2.0 && gridData.gridHeight >= 2.0) {
			Color oldForeground = g.getForegroundColor();
			g.setForegroundColor(GridColor);

			g.getClip(clipRect);	// Get the clip rectangle so that we only draw within this and not waste time.

			// We need to get all of the rectangles for all of the component figures that are children
			// of the siblings so that we don't draw the grid in them
			Rectangle[] childRectanglesArray = getChildRectangles();
		
			double highestX = Math.min(gridData.gridBorder.x + gridData.gridBorder.width, clipRect.x+clipRect.width);
			double highestY = Math.min(gridData.gridBorder.y + gridData.gridBorder.height, clipRect.y+clipRect.height);
				
			// Loop through each coordinate drawing a small circle
			double startY = gridData.gridBorder.y;
			if (gridData.gridBorder.y < clipRect.y) {
				// Need to start at first grid just outside of the clip rect (this is an easier calculation), it will just do one extra grid point
				int gridNum = (int) Math.round((clipRect.y-gridData.gridBorder.y)/gridData.gridHeight);
				startY = gridData.gridBorder.y + gridNum*gridData.gridHeight;
			}
			double startX = gridData.gridBorder.x;
			if (gridData.gridBorder.x < clipRect.x) {
				// Need to start at first grid just outside of the clip rect (this is an easier calculation), it will just do one extra grid point
				int gridNum = (int) Math.round((clipRect.x-gridData.gridBorder.x)/gridData.gridWidth);
				startX = gridData.gridBorder.x + gridNum*gridData.gridWidth;
			}
			for (double j = startY; j <= highestY ; j += gridData.gridHeight){
				int y = (int) Math.round(j);				
				nextGridPoint : for (double i = startX; i <= highestX ; i += gridData.gridWidth ){
					// Draw a point.  This is a circle with a diameter of 21
					// Make sure that the point we are going to draw the grid within is not inside one of the chidren
					int x = (int) Math.round(i);
					for ( int n = 0; n < childRectanglesArray.length ; n++){
						if ( childRectanglesArray[n].contains( x , y ) ) {
							continue nextGridPoint;
						}
					}
					// If we are here then we do not lie within any component
					g.drawPoint( x , y);
				}
			}
			g.setForegroundColor(oldForeground);
		}
	}
	
	/**
	 * Get the box for every child rectangles of the siblings
	 */
	protected Rectangle[] getChildRectangles(){
	
		ArrayList childRectangles = new ArrayList(10);
		IFigure parent = getParent();
		Iterator siblings = null;
		if (parent instanceof ContentPaneFigure)
			siblings = ((ContentPaneFigure)parent).getContentPane().getChildren().iterator();
		else
			siblings = parent.getChildren().iterator();
		while (siblings.hasNext()) {
			IFigure sibling = (Figure) siblings.next();
			if (sibling != this && sibling.isVisible())
				childRectangles.add(sibling.getBounds());
		}
		
		// Get the box of each child.  This is so that children are not decorated
		// Turn the list into an array for the call
		Rectangle[] childRectanglesArray = new Rectangle[childRectangles.size()];
		childRectanglesArray = (Rectangle[]) childRectangles.toArray(childRectanglesArray);
		return childRectanglesArray;	
	}
	
	/**
	 * Get the grid data, adjusted for zoom.
	 */
	public void getGridData(GridData gd) {
		gd.gridWidth = gridController.getGridWidth();
		gd.gridHeight = gridController.getGridHeight();
		gd.gridMargin = gridController.getGridMargin();
		if (zoomController != null) {
			gd.gridWidth = zoomController.zoomCoordinateReal(gridController.getGridWidth());
			gd.gridHeight = zoomController.zoomCoordinateReal(gridController.getGridHeight());
			gd.gridMargin = zoomController.zoomCoordinate(gd.gridMargin);
		}
		// set the initial values for the loops
		gd.gridBorder = getParent().getClientArea();
		gd.gridBorder.shrink(gd.gridMargin, gd.gridMargin);	// Inset it by the margin.
		gd.gridController = gridController;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
	 * 
	 * Need to return false so hit test doesn't apply to this figure.
	 */
	public boolean containsPoint(int x, int y) {
		return false;
	}
	
}
