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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagLayoutGridFigure.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-29 16:01:58 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * Draw grid lines on a GridBagLayout when dropping a component into a container
 */
public class GridBagLayoutGridFigure extends Figure {
	Point layoutOrigin = new Point(0,0);
	Point mousePosition = null;
	int[] columnWidths = null, rowHeights = null, columnPositions = null, rowPositions = null;
	Point [] rowHeaderPositions = null, rowStartPositions = null, rowEndPositions = null;
	Point [] columnHeaderPositions = null, columnStartPositions = null, columnEndPositions = null;
	int highlightColumnStart = 9999;
	int highlightRowStart = 9999;

public GridBagLayoutGridFigure (Rectangle bounds, int [][] layoutDimensions, Point gridBagLayoutOrigin) {
	super();
	setBounds(bounds);
	layoutOrigin = gridBagLayoutOrigin;
	if (layoutDimensions != null) {
		columnWidths = layoutDimensions[0];
		rowHeights = layoutDimensions[1];
		calculateColumnDividers();
		calculateRowDividers();
	}
}
protected void paintFigure(Graphics g){
	Rectangle r = getBounds().getCopy();
	r = new Rectangle(r.x - 15, r.y - 15, r.width + 15, r.height + 30);
	g.setClip(r);
	Color orgColor = g.getForegroundColor();
	g.setForegroundColor(ColorConstants.red);
	g.setLineStyle(Graphics.LINE_DOT);
	drawColumnDividers(g);
	drawRowDividers(g);
	g.setLineStyle(Graphics.LINE_SOLID);
	g.setForegroundColor(orgColor);
	drawColumnHeaders(g);
	drawRowHeaders(g);
}

/**
 * Make sure to repaint area outside border to handle clipping area.
 *
 * @since 1.1
 */
public void erase() {
	if (getParent() == null || !isVisible())
		return;
	
	Rectangle r = getBounds().getCopy();
	r = new Rectangle(r.x - 15, r.y - 15, r.width + 15, r.height + 30);
	getParent().translateToParent(r);
	getParent().repaint(r.x, r.y, r.width, r.height);
}

/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 * Note: Column widths from GridBagLayout that equal zero indicate there are no components
 * in that specific column.
 */
protected void calculateColumnDividers() {
	if (rowHeights == null || columnWidths == null || layoutOrigin == null) return;
	Rectangle r = getBounds();
	int containerHeight = 0;
	columnPositions = new int [columnWidths.length+1];
	columnStartPositions = new Point [columnWidths.length+1];
	columnEndPositions = new Point [columnWidths.length+1];

	for (int i = 0; i < rowHeights.length; i++) {
		containerHeight += rowHeights[i];
	}
	int xPos = r.x+ layoutOrigin.x;
	int yMin = r.y + layoutOrigin.y;
	int yMax = r.y + layoutOrigin.y + containerHeight;
	int colHeaderPos = 0;
	columnHeaderPositions = new Point [columnWidths.length];
	// draw the first divider
	columnPositions[0] = xPos;
	columnStartPositions[0] = new Point(xPos, yMin);
	columnEndPositions[0] = new Point(xPos, yMax);
	// draw the dividers in between and at the end
	for (int i = 0; i < columnWidths.length; i++) {
		if (columnWidths[i] > 0) {
			colHeaderPos = xPos + columnWidths[i]/2;
			// Draw the column header half way between each column
			columnHeaderPositions[i] = new Point(colHeaderPos, Math.max(r.y, yMin-8));
		}
		xPos += columnWidths[i];
		if (xPos >= r.x+r.width)
			xPos = r.x+r.width-1;
		Point startPoint = new Point(xPos, yMin );
		Point endPoint = new Point(xPos, yMax );
		columnPositions[i+1] = xPos;
		columnStartPositions[i+1] = startPoint;
		columnEndPositions[i+1] = endPoint;
	}
}
/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 */
protected void drawColumnDividers(Graphics g) {
	if (columnStartPositions == null && columnEndPositions == null)
		return;
	Rectangle r = getBounds().getCopy();
	int minX = r.x;
	int maxX = r.x + r.width;
	for (int i = 0; i < columnStartPositions.length; i++) 
		if( columnStartPositions[i].x >= minX && columnStartPositions[i].x <= maxX)
			g.drawLine(new Point(columnStartPositions[i].x, r.y), new Point(columnEndPositions[i].x, r.y + r.height));
}

protected void drawColumnHeaders(Graphics g) {
	Color orgColor = g.getBackgroundColor();
	for (int i=0; i < columnHeaderPositions.length; i++) {
		Point p = columnHeaderPositions[i];
		int columnNumber = 0;
		if (i < highlightColumnStart) {
			g.setBackgroundColor(ColorConstants.cyan);
			columnNumber = i;
		} else {
			g.setBackgroundColor(ColorConstants.yellow);
			columnNumber = i+1;
		}
		drawColumnHeader(g, columnNumber, p);
	}
	g.setBackgroundColor(orgColor);
}

protected void drawColumnHeader(Graphics g, int colNumber, Point p) {
	if (p != null) {
		String colname = Integer.toString(colNumber);
		int charwidth = g.getFontMetrics().getAverageCharWidth();
		int charheight = g.getFontMetrics().getHeight();
		Rectangle r = getBounds().getCopy();
		if(p.x+1 >= r.x && p.x + (colname.length()* charwidth)- 1 <= r.x + r.width){
			g.fillRoundRectangle(new Rectangle(p.x-4, p.y-10, (colname.length()* charwidth)+6, charheight+2), 6, 6);
			g.drawRoundRectangle(new Rectangle(p.x-4, p.y-10, (colname.length()* charwidth)+6, charheight+2), 6, 6);
			g.drawText(Integer.toString(colNumber), new Point(p.x, p.y - 9));
		}
	}
}
public void highlightColumnHeadersFromColumn(int colNumber) {
	highlightColumnStart = colNumber;
}
public void resetHighlightedColumnHeaders() {
	highlightColumnStart = 9999;
}

/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 * Note: Row heights from GridBagLayout that equal zero indicate there are no components
 * in that specific row.
 */
protected void calculateRowDividers() {
	if (rowHeights == null || columnWidths == null || layoutOrigin == null) return;
	Rectangle r = getBounds();
	int containerWidth = 0;
	rowPositions = new int [rowHeights.length+1];
	rowStartPositions = new Point [rowHeights.length+1];
	rowEndPositions = new Point [rowHeights.length+1];

	for (int i = 0; i < columnWidths.length; i++) {
		containerWidth += columnWidths[i];
	}
	int yPos = r.y+ layoutOrigin.y;
	int xMin = r.x + layoutOrigin.x;
	int xMax = r.x + layoutOrigin.x + containerWidth;
	int rowHeaderPos = 0;
	rowHeaderPositions = new Point [rowHeights.length];
	// draw the first divider
	rowPositions[0] = yPos;
	rowStartPositions[0] = new Point(xMin, yPos);
	rowEndPositions[0] = new Point(xMax, yPos);
	// draw the dividers in between and at the end
	for (int i = 0; i < rowHeights.length; i++) {
		if (rowHeights[i] > 0) {
			rowHeaderPos = yPos + rowHeights[i]/2;
		// Draw the row header half way between each column
			rowHeaderPositions[i] = new Point(Math.max(xMin-8, r.x), rowHeaderPos-5);
		}
		yPos += rowHeights[i];
		if (yPos >= r.y+r.height) 
			yPos = r.y+r.height-1;
		Point startPoint = new Point(xMin, yPos );
		Point endPoint = new Point(xMax, yPos );
		rowPositions[i+1] = yPos;
		rowStartPositions[i+1] = startPoint;
		rowEndPositions[i+1] = endPoint;
	}
}
/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 */
protected void drawRowDividers(Graphics g) {
	if (rowStartPositions == null && rowEndPositions == null)
		return;
	Rectangle r = getBounds().getCopy();
	int minY = r.y;
	int maxY = r.y + r.height;
	for (int i = 0; i < rowStartPositions.length; i++)
		if(rowStartPositions[i].y >= minY && rowStartPositions[i].y <= maxY)
			g.drawLine(new Point(r.x, rowStartPositions[i].y), new Point(r.x + r.width, rowEndPositions[i].y));
}

protected void drawRowHeaders(Graphics g) {
	Color orgColor = g.getBackgroundColor();
	for (int i=0; i < rowHeaderPositions.length; i++) {
		Point p = rowHeaderPositions[i];
		int rowNumber = 0;
		if (i < highlightRowStart) {
			g.setBackgroundColor(ColorConstants.cyan);
			rowNumber = i;
		} else {
			g.setBackgroundColor(ColorConstants.yellow);
			rowNumber = i+1;
		}
		drawRowHeader(g, rowNumber, p);
	}
	g.setBackgroundColor(orgColor);
}
protected void drawRowHeader(Graphics g, int rowNumber, Point p) {
	if (p != null) {
		String rowname = Integer.toString(rowNumber);
		int charwidth = g.getFontMetrics().getAverageCharWidth();
		int charheight = g.getFontMetrics().getHeight();
		Rectangle r = getBounds().getCopy();
		if(p.y + 5 >= r.y && p.y + charheight - 11 <= r.y + r.height){
			g.fillRoundRectangle(new Rectangle(p.x - 14, p.y-1, (rowname.length()* charwidth)+6, charheight+2), 6, 6);
			g.drawRoundRectangle(new Rectangle(p.x - 14, p.y-1, (rowname.length()* charwidth)+6, charheight+2), 6, 6);
			g.drawText(Integer.toString(rowNumber), new Point(p.x-10, p.y));
		}
	}
}
public void highlightRowHeadersFromRow(int rowNumber) {
	highlightRowStart = rowNumber;
}
public void resetHighlightedRowHeaders() {
	highlightRowStart = 9999;
}
/**
 * Based on specific x,y coorindate, return a Point with the gridx,gridy position
 */
public Point getCellLocation(int x, int y) {
	return getCellLocation(x, y, false, false);
}
/**
 * Based on specific x,y coordindate, return a Point with the gridx,gridy position
 */
public Point getCellLocation(int x, int y, boolean includeEmptyColumns, boolean includeEmptyRows) {
	if (rowPositions == null || columnPositions == null)
		return new Point(0,0);

	int gridx = 0, gridy = 0;
	boolean foundx = false, foundy = false;
	for (int i = 0; i < columnPositions.length-1; i++) {
		int xpos = columnPositions[i];
		if (x >= xpos && x < columnPositions[i+1]) {
			gridx = i;
			if (includeEmptyColumns) {
				/*
				 * Since column positions can be equal if there columns that don't contain components,
				 * iterate back throught the columns positions to get the first one with this position.
				 */
				int j;
				for (j = i; j >= 0 && columnPositions[i] == columnPositions[j]; j--);
				gridx = j + 1;
			}
			foundx = true;
			break;
		}
	} 
	for (int i = 0; i < rowPositions.length-1; i++) {
		int ypos = rowPositions[i];
		if (y >= ypos && y < rowPositions[i+1]) {
			gridy = i;
			if (includeEmptyRows) {
				/*
				 * Since row positions can be equal if there rows that don't contain components,
				 * iterate back throught the rows to get the first one with this position.
				 */
				int j;
				for (j = i; j >= 0 && rowPositions[i] == rowPositions[j]; j--);
				gridy = j + 1;
			}
			foundy = true;
			break;
		}
	} 
	
	if (!foundx && (x >= columnPositions[columnPositions.length-1]))
		// mouse position is beyond the end of the last column
		gridx = columnPositions.length-1;
	if (!foundy && (y >= rowPositions[rowPositions.length-1]))
		// mouse position is beyond the end of the last row
		gridy = rowPositions.length-1;
		
	return new Point(gridx,gridy);
}
/**
 * Return the bounds for the grid bag cell located at position x,y
 */
public Rectangle getCellBounds(int x, int y) {
	if (rowPositions == null || columnPositions == null)
		return new Rectangle();

	int cellxpos = 0, cellypos = 0, cellwidth = 0, cellheight = 0;
	for (int i = 0; i < columnPositions.length-1; i++) {
		int xpos = columnPositions[i];
		if (x >= xpos && x < columnPositions[i+1]) {
			cellxpos = xpos;
			cellwidth = columnPositions[i+1] - xpos;
			break;
		}
	} 
	for (int i = 0; i < rowPositions.length-1; i++) {
		int ypos = rowPositions[i];
		if (y >= ypos && y < rowPositions[i+1]) {
			cellypos = ypos;
			cellheight = rowPositions[i+1] - ypos;
			break;
		}
	} 
	return new Rectangle(cellxpos, cellypos, cellwidth, cellheight);
}
public Point getColumnStartPosition(int x) {
	if (columnStartPositions != null) {
		for (int i = 0; i < columnPositions.length; i++) {
			int xpos = columnPositions[i];
			if ((xpos <= x) && (x <= xpos + 5)) 
				return columnStartPositions[i];
		} 
	}
	return new Point(0,0);
}
public Point getColumnEndPosition(int x) {
	if (columnEndPositions != null) {
		for (int i = 0; i < columnPositions.length; i++) {
			int xpos = columnPositions[i];
			if ((xpos <= x) && (x <= xpos + 5)) 
				return columnEndPositions[i];
		} 
	}
	return new Point(0,0);
}
public Point getRowStartPosition(int y) {
	if (rowStartPositions != null) {
		for (int i = 0; i < rowPositions.length; i++) {
			int ypos = rowPositions[i];
			if ((ypos <= y) && (y < ypos + 5)) 
				return rowStartPositions[i];
		}
	}
	return new Point(0,0);
}
public Point getRowEndPosition(int y) {
	if (rowStartPositions != null) {
		for (int i = 0; i < rowPositions.length; i++) {
			int ypos = rowPositions[i];
			if ((ypos <= y) && (y < ypos + 5)) 
				return rowEndPositions[i];
		} 
	}
	return new Point(0,0);
}
		
public boolean isPointerNearAColumn(int x) {
	if (columnPositions == null)
		return false;

	for (int i = 0; i < columnPositions.length; i++) {
		int xpos = columnPositions[i];
		if ((xpos <= x) && (x <= xpos + 5)) 
			return true;
	} 
	return false;
}
public boolean isPointerNearARow(int y) {
	if (rowPositions == null)
		return false;

	for (int i = 0; i < rowPositions.length; i++) {
		int ypos = rowPositions[i];
		if ((ypos <= y) && (y < ypos + 5)) 
			return true;
	} 
	return false;
}
/*
 * Return true if this is one of the columns that has no components in it which is
 * the case if we don't a column header for it
 */
public boolean isColumnHidden (int x) {
	if (columnHeaderPositions != null && x < columnHeaderPositions.length-1)
		return columnHeaderPositions[x] == null;
	return false;	
}
/*
 * Return true if this is one of the rows that has no components in it which is
 * the case if we don't a row header for it
 */
public boolean isRowHidden (int y) {
	if (rowHeaderPositions != null && y < rowHeaderPositions.length-1)
		return rowHeaderPositions[y] == null;
	return false;	
}

public void setLayoutDimensions(int[][] dim) {
	if (dim == null) return;
	columnWidths = dim[0];
	rowHeights = dim[1];
	calculateColumnDividers();
	calculateRowDividers();
	invalidate();
}
}
