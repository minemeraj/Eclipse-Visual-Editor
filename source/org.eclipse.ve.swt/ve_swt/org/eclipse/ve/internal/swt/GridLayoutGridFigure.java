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
 *  $Revision: 1.12 $  $Date: 2005-07-12 22:42:52 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridLayout;

/**
 * Draw grid lines on a GridLayout when dropping a control into a composite
 */
public class GridLayoutGridFigure extends Figure {
	// number of pixels either side of a row/column that will trigger showing the row/column insertion figure.
	public static final int ROW_COLUMN_SENSITIVITY = 5;
	
	Rectangle clientArea;
	Point mousePosition;
	int[] columnPositions, rowPositions;
	Point [] rowStartPositions, rowEndPositions;
	Point [] columnStartPositions, columnEndPositions;
	int [][] columnSegments, rowSegments;
	
	int marginWidth, marginHeight, verticalSpacing, horizontalSpacing;

public GridLayoutGridFigure (Rectangle bounds, int [][] layoutDimensions, EObject[][] cellContents, Rectangle spacing, Rectangle clientArea) {
	super();
	setBounds(bounds);
	this.clientArea = clientArea;
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
		int[] columnWidths = layoutDimensions[0];
		int [] rowHeights = layoutDimensions[1];
		columnSegments = calculateColumnDividers(columnWidths, rowHeights, cellContents);
		rowSegments = calculateRowDividers(columnWidths, rowHeights, cellContents);
	}
}

protected void paintFigure(Graphics g){
	g.setForegroundColor(ColorConstants.gray);
	g.setLineStyle(Graphics.LINE_DOT);
	drawColumnDividers(g);
	drawRowDividers(g);
}

/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 * Return the columnSegments array.
 * <p>
 * <b>Note:</b> Column widths from GridBagLayout that equal zero indicate there are no components
 * in that specific column.
 *
 * @param columnWidths
 * @param rowHeights
 * @param cellContents
 * @return columnSegements array. First dimension is for each column. Second dimension is the array of segments to draw
 * for that column. For example, if there are no spaned columns, then the path for that column will be just one segment,
 * top to bottom. The segments will be 2-tuples (startY, endY). It will only be the segments for drawing the non-spanned
 * columns. Spanned columns aren't drawn.
 * 
 * @since 1.1.0
 */
protected int[][] calculateColumnDividers(int[] columnWidths, int[] rowHeights, EObject[][] cellContents) {
	if (rowHeights == null || columnWidths == null || clientArea == null) return null;
	
	int spacingLeft = (int)Math.ceil((double)horizontalSpacing / 2);
	int spacingRight = (int)Math.floor((double)horizontalSpacing / 2);
	int spacingTop = (int)Math.ceil((double)verticalSpacing / 2);
	int spacingBottom = (int)Math.floor((double)verticalSpacing / 2);
		
	Rectangle r = getBounds();
	int containerHeight = 0;
	columnPositions = new int [columnWidths.length+1];
	columnStartPositions = new Point [columnPositions.length];
	columnEndPositions = new Point [columnPositions.length];
	int[][] columnSegments = new int[columnPositions.length][];

	for (int i = 0; i < rowHeights.length; i++) {
		containerHeight += rowHeights[i];
	}
	// add height for the spacing
	containerHeight += marginHeight * 2;
	containerHeight += verticalSpacing * (rowHeights.length - 1);
		
	int xPos = r.x+ clientArea.x;
	int yMin = r.y + clientArea.y;
	int yMax = r.y + clientArea.y + containerHeight;
	int xMax = xPos+clientArea.width-1;	// This is the far right side, less one so that it draws within the box (otherwise it would be outside the box).
	
	// draw the first divider
	columnPositions[0] = xPos;
	columnStartPositions[0] = new Point(xPos, yMin);
	columnEndPositions[0] = new Point(xPos, yMax);
	columnSegments[0] = new int[] {yMin, yMax};	// The left border will always be one segemnt.
	
	// move up by the initial margin width
	xPos += marginWidth;
	
	int[] colSegs = new int[2+rowHeights.length*2];	// Each entry is a y position. They are in 2-tuples (start,stop) for a segment. For a col, it is max of one segment(or 2 points) + 2 for start of next.	
	// draw the dividers in between and at the end
	for (int i = 1; i < columnPositions.length; i++) {
		xPos += columnWidths[i-1];
		
		// Place the position in the middle of the the horizontal spacing gap
		if (i < columnWidths.length) {
			xPos += spacingLeft;
		} else {
			// or after the end margin, if this is the last line
			xPos += marginWidth;
		}
		
		xPos = Math.min(xPos, xMax);
		Point startPoint = new Point(xPos, yMin );
		Point endPoint = new Point(xPos, yMax );
		columnPositions[i] = xPos;
		columnStartPositions[i] = startPoint;
		columnEndPositions[i] = endPoint;
		
		// Now calculate the column segments.
		if (i < columnWidths.length) {
			EObject[] leftColumn = cellContents[i-1];
			EObject[] rightColumn = cellContents[i];
			int yPos = yMin;
			int lastRow = leftColumn.length-1;
			int colSegsNdx = 0;	// This will always point to the start index of the next segment.
			colSegs[colSegsNdx] = yMin;	// Will always start at yMin.
			boolean prevSpan = true;	// Previous was a span. (We treat first one as previous span so as not to close off an empty segment)
			// Walk each row, and compare left and right column to see if spanned.
			for (int j = 0; j < leftColumn.length; j++) {
				int trueRowHeight = rowHeights[j];
				if (j == 0)
					trueRowHeight+=marginHeight;
				else
					trueRowHeight+=spacingTop;
				if (j != lastRow)
					trueRowHeight+=spacingBottom;
				else
					trueRowHeight+=marginHeight;
				EObject leftObject, rightObject;
				if ((leftObject = leftColumn[j]) != GridLayoutPolicyHelper.EMPTY && (rightObject = rightColumn[j]) != GridLayoutPolicyHelper.EMPTY && leftObject == rightObject) {
					// We are spanning, so skip it and move start of segment to next cell.
					if (!prevSpan) {
						// Need to close off previous one (if not first)
						colSegs[++colSegsNdx] = yPos;
						colSegsNdx++;	// Move to start of next.
					}
					yPos+=trueRowHeight;
					colSegs[colSegsNdx] = yPos+1;	// Start of next seg.
					prevSpan = true;
				} else {
					// We are not spanning, continue line through it.
					prevSpan = false;
					yPos+=trueRowHeight;
				}
			}
			if (colSegs[colSegsNdx] < yPos) {
				// We have to something to draw for last segment.
				colSegs[++colSegsNdx] = yMax;
			} else
				colSegsNdx--;	// We had a segment start that is the same as the last stop, so get rid of it.
			columnSegments[i] = new int[++colSegsNdx];
			System.arraycopy(colSegs, 0, columnSegments[i], 0, colSegsNdx);
		} else {
			columnSegments[i] = new int[] {yMin, yMax};	// The right border will always be one segemnt.
		}
		// add the remainder of the spacing
		xPos += spacingRight;
	}
	
	return columnSegments;
}
/**
 * Draw the column dividers based on the GridBagLayout's origin and the column widths.
 */
protected void drawColumnDividers(Graphics g) {
	if (columnSegments == null || columnStartPositions == null)
		return;
	for (int i = 0; i < columnSegments.length; i++) {
		int[] colSegs = columnSegments[i];
		int xPos = columnStartPositions[i].x;
		int j=-1;
		while(++j < colSegs.length) {
			g.drawLine(xPos, colSegs[j], xPos, colSegs[++j]);
		}
	}
}

/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 * Note: Row heights from GridBagLayout that equal zero indicate there are no components
 * in that specific row.
 */
protected int[][] calculateRowDividers(int[] columnWidths, int[] rowHeights, EObject[][] cellContents) {
	if (columnWidths == null || rowHeights == null || clientArea == null) return null;
	
	int spacingLeft = (int)Math.ceil((double)horizontalSpacing / 2);
	int spacingRight = (int)Math.floor((double)horizontalSpacing / 2);
	int spacingTop = (int)Math.ceil((double)verticalSpacing / 2);
	int spacingBottom = (int)Math.floor((double)verticalSpacing / 2);
	
	Rectangle r = getBounds();
	int containerWidth = 0;
	rowPositions = new int [rowHeights.length+1];
	rowStartPositions = new Point [rowPositions.length];
	rowEndPositions = new Point [rowPositions.length];
	int[][] rowSegments = new int[rowPositions.length][];

	for (int i = 0; i < columnWidths.length; i++) {
		containerWidth += columnWidths[i];
	}
	// add width for the spacing
	containerWidth += marginWidth * 2;
	containerWidth += horizontalSpacing * ( columnWidths.length - 1);
	
	int yPos = r.y+ clientArea.y;
	int xMin = r.x + clientArea.x;
	int xMax = r.x + clientArea.x + containerWidth;
	int yMax = yPos+clientArea.height-1;	// This is the bottom side, less one so that it draws within the box (otherwise it would be outside the box).
	
	// draw the first divider
	rowPositions[0] = yPos;
	rowStartPositions[0] = new Point(xMin, yPos);
	rowEndPositions[0] = new Point(xMax, yPos);
	rowSegments[0] = new int[] {xMin, xMax};	// The top border will always be one segemnt.
	
	// move up the initial margin height
	yPos += marginHeight;
	
	int[] rowSegs = new int[2+columnWidths.length*2];	// Each entry is an x position. They are in 2-tuples (start,stop) for a segment. For a row, it is max of one segment(or 2 points) + 2 for start of next.
	// draw the dividers in between and at the end
	for (int i = 1; i < rowPositions.length; i++) {
		yPos += rowHeights[i-1];
	
		// Place the position in the middle of the the vertical spacing gap
		if (i < rowHeights.length) {
			yPos += spacingTop;
		} else {
			// or after the end margin, if this is the last line
			yPos += marginHeight;
		}
		
		yPos = Math.min(yPos, yMax);
		Point startPoint = new Point(xMin, yPos );
		Point endPoint = new Point(xMax, yPos );
		rowPositions[i] = yPos;
		rowStartPositions[i] = startPoint;
		rowEndPositions[i] = endPoint;
		
		// Now calculate the row segments.
		if (i < rowHeights.length) {
			int upperRow = i-1;
			int lowerRow = i;
			int xPos = xMin;
			int lastCol = cellContents.length-1;
			int rowSegsNdx = 0;	// This will always point to the start index of the next segment.
			rowSegs[rowSegsNdx] = xMin;	// Will always start at xMin.
			boolean prevSpan = true;	// Previous was a span. (We treat first one as previous span so as not to close off an empty segment)
			// Walk each column comparing upper row and lower to see if spanned.
			for (int j = 0; j < cellContents.length; j++) {
				int trueColWidth = columnWidths[j];
				if (j == 0)
					trueColWidth+=marginWidth;
				else
					trueColWidth+=spacingLeft;
				if (j != lastCol)
					trueColWidth+=spacingRight;
				else
					trueColWidth+=marginWidth;
				EObject upperObject, lowerObject;
				if ((upperObject = cellContents[j][upperRow]) != GridLayoutPolicyHelper.EMPTY && (lowerObject = cellContents[j][lowerRow]) != GridLayoutPolicyHelper.EMPTY && upperObject == lowerObject) {
					// We are spanning, so skip it and move start of segment to next cell.
					if (!prevSpan) {
						// Need to close off previous one (if not first)
						rowSegs[++rowSegsNdx] = xPos;
						rowSegsNdx++;	// Move to start of next.
					}
					xPos+=trueColWidth;
					rowSegs[rowSegsNdx] = xPos+1;	// Start of next seg.
					prevSpan = true;
				} else {
					// We are not spanning, continue line through it.
					xPos+=trueColWidth;
					prevSpan = false;
				}
			}
			if (rowSegs[rowSegsNdx] < xPos) {
				// We have to something to draw for last segment.
				rowSegs[++rowSegsNdx] = xMax;
			} else
				rowSegsNdx--;	// We had a segment start that is the same as the last stop, so get rid of it.
			rowSegments[i] = new int[++rowSegsNdx];
			System.arraycopy(rowSegs, 0, rowSegments[i], 0, rowSegsNdx);
		} else {
			rowSegments[i] = new int[] {xMin, xMax};	// The bottom border will always be one segemnt.
		}
		
		// add the remainder of the spacing		
		yPos += spacingBottom;
	}
	
	return rowSegments;
}
/**
 * Draw the row dividers based on the GridBagLayout's origin and the row widths.
 */
protected void drawRowDividers(Graphics g) {
	if (rowSegments == null || rowStartPositions == null)
		return;
	for (int i = 0; i < rowSegments.length; i++) {
		int[] rowSegs = rowSegments[i];
		int yPos = rowStartPositions[i].y;
		int j=-1;
		while(++j < rowSegs.length) {
			g.drawLine(rowSegs[j], yPos, rowSegs[++j], yPos);
		}
	}
}

/**
 * Based on specific x,y coorindate, return a Point with the column, row position
 */
public Point getCellLocation(Point p) {
	return getCellLocation(p.x, p.y);
}

/**
 * Based on specific x,y coorindate, return a Point with the column, row position
 */
public Point getCellLocation(int x, int y) {
	return getCellLocation(x, y, false, false);
}
/**
 * Based on specific x,y coordindate, return a Point with the column, row position
 * Return -1 for column and/or row indicates position is beyond column and/or row
 */
public Point getCellLocation(int x, int y, boolean includeEmptyColumns, boolean includeEmptyRows) {
	if (rowPositions == null || columnPositions == null)
		return new Point(-1,-1);

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
		gridx = -1;
	if (!foundy && (y >= rowPositions[rowPositions.length-1]))
		// mouse position is beyond the end of the last row
		gridy = -1;
		
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
			if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY)) 
				return columnStartPositions[i];
		} 
	}
	return new Point(0,0);
}
public Point getColumnEndPosition(int x) {
	if (columnEndPositions != null) {
		for (int i = 0; i < columnPositions.length; i++) {
			int xpos = columnPositions[i];
			if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY)) 
				return columnEndPositions[i];
		} 
	}
	return new Point(0,0);
}
public Point getRowStartPosition(int y) {
	if (rowStartPositions != null) {
		for (int i = 0; i < rowPositions.length; i++) {
			int ypos = rowPositions[i];
			if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY)) 
				return rowStartPositions[i];
		}
	}
	return new Point(0,0);
}
public Point getRowEndPosition(int y) {
	if (rowStartPositions != null) {
		for (int i = 0; i < rowPositions.length; i++) {
			int ypos = rowPositions[i];
			if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY)) 
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
		if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY)) 
			return true;
	} 
	return false;
}
public boolean isPointerNearARow(int y) {
	if (rowPositions == null)
		return false;

	for (int i = 0; i < rowPositions.length; i++) {
		int ypos = rowPositions[i];
		if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY)) 
			return true;
	} 
	return false;
}
public int getNearestRow(int y) {
	if (rowPositions == null || rowPositions.length == 0)
		return 0;

	int row = 0;
	int value = Math.abs(0 - y);
	for (int i = 0; i < rowPositions.length; i++) {
		int diff = Math.abs(rowPositions[i] - y);
		if (diff < value) {
			row = i;
			value = diff;
		}
	} 
	return row;
}
public int getNearestColumn(int x) {
	if (columnPositions == null || columnPositions.length == 0)
		return 0;

	int column = 0;
	int value = Math.abs(0 - x);
	for (int i = 0; i < columnPositions.length; i++) {
		int diff = Math.abs(columnPositions[i] - x);
		if (diff < value) {
			column = i;
			value = diff;
		}
	} 
	return column;
}
}
