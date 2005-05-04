/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: GridLayoutGridFigure.java,v $
 *  $Revision: 1.7 $  $Date: 2005-05-04 22:12:15 $ 
 */

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;

/**
 * Draw grid lines on a GridLayout when dropping a control into a composite
 */
public class GridLayoutGridFigure extends Figure {
	Rectangle clientArea = new Rectangle();
	Point mousePosition = null;
	int[] columnWidths = null, rowHeights = null, expandableColumns = null, expandableRows = null, columnPositions = null, rowPositions = null;
	Point [] rowStartPositions = null, rowEndPositions = null;
	Point [] columnStartPositions = null, columnEndPositions = null;
	
	int marginWidth, marginHeight, verticalSpacing, horizontalSpacing, bonusWidth = 0, bonusHeight = 0;
	boolean columnsEqualWidth = false;

public GridLayoutGridFigure (Rectangle bounds, int [][] layoutDimensions, int[][] expandableDimensions, boolean columnsEqualWidth, Rectangle spacing, Rectangle clientArea) {
	super();
	setBounds(bounds);
	setForegroundColor(ColorConstants.cyan);
	this.clientArea = clientArea;
	this.columnsEqualWidth = columnsEqualWidth;
	if (spacing == null) {
		// Get the default spacings from GridLayout.
		GridLayout example = new GridLayout();
		marginWidth = example.marginWidth;
		marginHeight = example.marginHeight;
		horizontalSpacing = example.horizontalSpacing;
		verticalSpacing = example.verticalSpacing;
	} else {
		marginWidth = spacing.x;
		marginHeight = spacing.y;
		horizontalSpacing = spacing.width;
		verticalSpacing = spacing.height;
	}
	if (layoutDimensions != null) {
		columnWidths = layoutDimensions[0];
		rowHeights = layoutDimensions[1];
		if (expandableDimensions != null) {
			expandableColumns = expandableDimensions[0];
			expandableRows = expandableDimensions[1];
			calculateBonusSizes();
		}
		calculateColumnDividers();
		calculateRowDividers();
	}
}

protected void paintFigure(Graphics g){
	Color orgColor = g.getForegroundColor();
	
	g.setForegroundColor(ColorConstants.red);
	g.setLineStyle(Graphics.LINE_DOT);
	drawColumnDividers(g);
	drawRowDividers(g);
	
	g.setForegroundColor(orgColor);
}


private void calculateBonusSizes() {
	if (clientArea == null || expandableColumns == null || expandableRows == null) return;
	
	if (expandableColumns.length != 0) {
		int containerWidth = 0;
		for (int i = 0; i < columnWidths.length; i++) {
			containerWidth += columnWidths[i];
		}
		// add width for the spacing
		containerWidth += marginWidth * 2;
		containerWidth += horizontalSpacing * (columnWidths.length - 1);

		// Subtract the total unexpanded width from the client area's width, and divide by the number of
		// columns requesting expansion
		bonusWidth = (clientArea.width - containerWidth) / expandableColumns.length;	
	}
	
	if (expandableRows.length != 0) {
		int containerHeight = 0;
		for (int i = 0; i < rowHeights.length; i++) {
			containerHeight += rowHeights[i];
		}
		// add height for the spacing
		containerHeight += marginHeight * 2;
		containerHeight += verticalSpacing * (rowHeights.length - 1);

		bonusHeight = (clientArea.height - containerHeight) / expandableRows.length;
	}
}

/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 * Note: Column widths from GridBagLayout that equal zero indicate there are no components
 * in that specific column.
 */
protected void calculateColumnDividers() {
	if (rowHeights == null || columnWidths == null || clientArea == null) return;
	
	int spacingTop = (int)Math.ceil((double)horizontalSpacing / 2);
	int spacingBottom = (int)Math.floor((double)horizontalSpacing / 2);
	boolean isExpanding = bonusWidth != 0;
	int equalWidth = 0;
		
	Rectangle r = getBounds();
	int containerHeight = 0;
	columnPositions = new int [columnWidths.length+1];
	columnStartPositions = new Point [columnWidths.length+1];
	columnEndPositions = new Point [columnWidths.length+1];

	if (bonusHeight != 0) {
		containerHeight = clientArea.height;
	} else {
		for (int i = 0; i < rowHeights.length; i++) {
			containerHeight += rowHeights[i];
		}
		// add height for the spacing
		containerHeight += marginHeight * 2;
		containerHeight += verticalSpacing * (rowHeights.length - 1);
	}
		
	int xPos = r.x+ clientArea.x;
	int yMin = r.y + clientArea.y;
	int yMax = r.y + clientArea.y + containerHeight;
	
	// Calculate the width value if the columns are equal width
	if (columnsEqualWidth) {
		int padding = marginWidth * 2 + horizontalSpacing * (columnWidths.length - 1);
		equalWidth = (clientArea.width - padding) / columnWidths.length;
	}
	
	// draw the first divider
	columnPositions[0] = xPos;
	columnStartPositions[0] = new Point(xPos, yMin);
	columnEndPositions[0] = new Point(xPos, yMax);
	
	// move up by the initial margin width
	xPos += marginWidth;
	
	// draw the dividers in between and at the end
	for (int i = 0; i < columnWidths.length; i++) {
		if (columnsEqualWidth) {
			xPos += equalWidth;
		} else {
			xPos += columnWidths[i];
			if (isExpanding && arrayContains(expandableColumns, i)) {
				xPos += bonusWidth;
			}
		}
		
		// Place the position in the middle of the the horizontal spacing gap
		if (i != columnWidths.length - 1) {
			xPos += spacingTop;
		} else {
			// or after the end margin, if this is the last line
			xPos += marginWidth;
		}
		
		if (xPos >= r.x+r.width)
			xPos = r.x+r.width-1;
		Point startPoint = new Point(xPos, yMin );
		Point endPoint = new Point(xPos, yMax );
		columnPositions[i+1] = xPos;
		columnStartPositions[i+1] = startPoint;
		columnEndPositions[i+1] = endPoint;
		// add the remainder of the spacing
		xPos += spacingBottom;
	}
}
/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 */
protected void drawColumnDividers(Graphics g) {
	if (columnStartPositions == null && columnEndPositions == null)
		return;
	for (int i = 0; i < columnStartPositions.length; i++) 
		g.drawLine(columnStartPositions[i], columnEndPositions[i]);
}

/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 * Note: Row heights from GridBagLayout that equal zero indicate there are no components
 * in that specific row.
 */
protected void calculateRowDividers() {
	if (rowHeights == null || columnWidths == null || clientArea == null) return;
	
	int spacingLeft = (int)Math.ceil((double)verticalSpacing / 2);
	int spacingRight = (int)Math.floor((double)verticalSpacing / 2);
	boolean isExpanding = bonusHeight != 0;
	
	Rectangle r = getBounds();
	int containerWidth = 0;
	rowPositions = new int [rowHeights.length+1];
	rowStartPositions = new Point [rowHeights.length+1];
	rowEndPositions = new Point [rowHeights.length+1];

	if (bonusWidth != 0) {
		containerWidth = clientArea.width;
	} else {
		for (int i = 0; i < columnWidths.length; i++) {
			containerWidth += columnWidths[i];
		}
		// add width for the spacing
		containerWidth += marginWidth * 2;
		containerWidth += horizontalSpacing * ( columnWidths.length - 1);
	}
	
	int yPos = r.y+ clientArea.y;
	int xMin = r.x + clientArea.x;
	int xMax = r.x + clientArea.x + containerWidth;
	
	// draw the first divider
	rowPositions[0] = yPos;
	rowStartPositions[0] = new Point(xMin, yPos);
	rowEndPositions[0] = new Point(xMax, yPos);
	
	// move up the initial margin height
	yPos += marginHeight;
	
	// draw the dividers in between and at the end
	for (int i = 0; i < rowHeights.length; i++) {
		yPos += rowHeights[i];
		if (isExpanding && arrayContains(expandableRows, i)) {
			yPos += bonusHeight;
		}
		
		if (yPos >= r.y+r.height) 
			yPos = r.y+r.height-1;

		// Place the position in the middle of the the vertical spacing gap
		if (i != rowHeights.length - 1) {
			yPos += spacingLeft;
		} else {
			// or after the end margin, if this is the last line
			yPos += marginHeight;
		}
		
		Point startPoint = new Point(xMin, yPos );
		Point endPoint = new Point(xMax, yPos );
		rowPositions[i+1] = yPos;
		rowStartPositions[i+1] = startPoint;
		rowEndPositions[i+1] = endPoint;
		// add the remainder of the spacing		
		yPos += spacingRight;
	}
}
/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 */
protected void drawRowDividers(Graphics g) {
	if (rowStartPositions == null && rowEndPositions == null)
		return;
	for (int i = 0; i < rowStartPositions.length; i++) 
		g.drawLine(rowStartPositions[i], rowEndPositions[i]);
}

private boolean arrayContains(int[] array, int value) {
	for (int i = 0; i < array.length; i++) {
		if (array[i] == value) {
			return true;
		}
	}
	return false;
}

public Point getCellLocation(Point p) {
	return getCellLocation(p.x, p.y);
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
		gridx = columnPositions.length-2;
	if (!foundy && (y >= rowPositions[rowPositions.length-1]))
		// mouse position is beyond the end of the last row
		gridy = rowPositions.length-2;
		
	return new Point(gridx,gridy);
}

public int getRowHeight(int row) {
	if (rowHeights == null || row < 0 || row >= rowHeights.length)
		return -1;
	else return rowHeights[row];
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

/**
 * Get the grid point size dimensions for the specified cell dimensions.  The cells dimensions
 * are packed into a Rectangle according to the following rules:
 * 
 * rect.x = column position
 * rect.y = row position
 * rect.width = horizontal span
 * rect.height = vertical span
 * 
 * @param cellsBounds  The cell area to calculate
 * @return the point dimensions for the grid representing these cells.
 * 
 * @since 1.0.0
 */
public Rectangle getGridBroundsForCellBounds(Rectangle cellsBounds) {
	Rectangle r = new Rectangle();
	if (rowPositions != null && columnPositions != null && cellsBounds.y <= rowPositions.length - 1 &&
			cellsBounds.x <= columnPositions.length - 1) {
		r.x = columnPositions[cellsBounds.x];
		r.y = rowPositions[cellsBounds.y];
		
		if (cellsBounds.x + cellsBounds.width > columnPositions.length - 1) {
			r.width = columnPositions[columnPositions.length - 1];
		} else {
			r.width = columnPositions[cellsBounds.x + cellsBounds.width];
		}
		r.width -= r.x;
		
		if (cellsBounds.y + cellsBounds.height > rowPositions.length - 1) {
			r.height = rowPositions[rowPositions.length - 1];
		} else {
			r.height = rowPositions[cellsBounds.y + cellsBounds.height];
		}
		r.height -= r.y;
	}
	return r;
	
}

public Point getColumnStartPosition(int x) {
	if (columnStartPositions != null) {
		for (int i = 0; i < columnPositions.length; i++) {
			int xpos = columnPositions[i];
			if ((xpos <= x) && (x <= xpos + 3)) 
				return columnStartPositions[i];
		} 
	}
	return new Point(0,0);
}
public Point getColumnEndPosition(int x) {
	if (columnEndPositions != null) {
		for (int i = 0; i < columnPositions.length; i++) {
			int xpos = columnPositions[i];
			if ((xpos <= x) && (x <= xpos + 3)) 
				return columnEndPositions[i];
		} 
	}
	return new Point(0,0);
}
public Point getRowStartPosition(int y) {
	if (rowStartPositions != null) {
		for (int i = 0; i < rowPositions.length; i++) {
			int ypos = rowPositions[i];
			if ((ypos <= y) && (y < ypos + 3)) 
				return rowStartPositions[i];
		}
	}
	return new Point(0,0);
}
public Point getRowEndPosition(int y) {
	if (rowStartPositions != null) {
		for (int i = 0; i < rowPositions.length; i++) {
			int ypos = rowPositions[i];
			if ((ypos <= y) && (y < ypos + 3)) 
				return rowEndPositions[i];
		} 
	}
	return new Point(0,0);
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
