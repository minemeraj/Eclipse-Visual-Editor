package org.eclipse.ve.internal.jfc.core;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Draw grid lines on a GridBagLayout when dropping a component into a container
 */
public class GridBagLayoutFeedBack extends Figure {
	public static final String copyright = "(c) Copyright IBM Corporation 2002."; //$NON-NLS-1$
	Point layoutOrigin = new Point(0,0);
	int[] columnWidths = null,
		rowHeights = null;

protected void paintFigure(Graphics g){
	g.setBackgroundColor(ColorConstants.red);
	g.setLineStyle(Graphics.LINE_DOT);
	drawColumnDividers(g);
	drawRowDividers(g);
}
/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 */
protected void drawColumnDividers(Graphics g) {
	if (rowHeights == null || columnWidths == null || layoutOrigin == null) return;
	Rectangle r = getBounds();
	int containerHeight = 0;
	for (int i = 0; i < rowHeights.length; i++) {
		containerHeight += rowHeights[i];
	}
	int xPos = r.x+ layoutOrigin.x;
	int yMin = r.y + layoutOrigin.y;
	int yMax = r.y + layoutOrigin.y + containerHeight;
	// draw the first divider
	g.drawLine(new Point(xPos, yMin ), new Point(xPos, yMax));
	// draw the dividers in between and at the end
	for (int i = 0; i < columnWidths.length; i++) {
		xPos += columnWidths[i];
		if (xPos >= r.x+r.width)
			xPos = r.x+r.width-1;
		Point startPoint = new Point(xPos, yMin );
		Point endPoint = new Point(xPos, yMax );
		g.drawLine(startPoint, endPoint);
	}
}
/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 */
protected void drawRowDividers(Graphics g) {
	if (rowHeights == null || columnWidths == null || layoutOrigin == null) return;
	Rectangle r = getBounds();
	int containerWidth = 0;
	for (int i = 0; i < columnWidths.length; i++) {
		containerWidth += columnWidths[i];
	}
	int yPos = r.y+ layoutOrigin.y;
	int xMin = r.x + layoutOrigin.x;
	int xMax = r.x + layoutOrigin.x + containerWidth;
	// draw the first divider
	g.drawLine(new Point(xMin, yPos ), new Point(xMax, yPos));
	// draw the dividers in between and at the end
	for (int i = 0; i < rowHeights.length; i++) {
		yPos += rowHeights[i];
		if (yPos >= r.y+r.height) 
			yPos = r.y+r.height-1;
		Point startPoint = new Point(xMin, yPos );
		Point endPoint = new Point(xMax, yPos );
		g.drawLine(startPoint, endPoint);
	}
}
public void setLayoutDimensions(int[][] dim) {
	if (dim == null) return;
	columnWidths = dim[0];
	rowHeights = dim[1];
}
public void setLayoutOrigin(Point p) {
	layoutOrigin = p;
}
}