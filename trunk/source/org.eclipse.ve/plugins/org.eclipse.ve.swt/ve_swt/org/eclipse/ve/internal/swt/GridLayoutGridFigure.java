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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: GridLayoutGridFigure.java,v $
 *  $Revision: 1.15 $  $Date: 2005-11-15 17:19:51 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridLayout;

/**
 * Draw grid lines on a GridLayout when dropping a control into a composite.
 * <p>
 * <b>Note:</b> For this to work correctly it is assumed that this figure uses absolute coordinates and
 * that it is within a figure that uses absolute coordinates. If either this figure or the parent uses
 * relative coordinates, the grid figure will fail to work properly.
 * 
 * @since 1.1.0
 */
public class GridLayoutGridFigure extends Figure {
	// number of pixels either side of a row/column that will trigger showing the row/column insertion figure.
	public static final int ROW_COLUMN_SENSITIVITY = 5;
	
	GridLayoutPolicyHelper helper;
	
	// Note: all of these that have "model" in the name positions are relative in the model coordinate system (which may be Right-to-Left and so backwards
	// from the GEF grid figure).
	int[] columnModelPositions, rowModelPositions;
	Point [] rowStartModelPositions, rowEndModelPositions;
	Point [] columnStartModelPositions, columnEndModelPositions;
	int [][] columnModelSegments, rowModelSegments;
	
	int marginWidth, marginHeight, verticalSpacing, horizontalSpacing;

public GridLayoutGridFigure (Rectangle bounds, int [][] layoutDimensions, EObject[][] cellContents, Rectangle spacing, GridLayoutPolicyHelper helper) {
	super();
	setBounds(bounds);
	this.helper = helper;
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
		columnModelSegments = calculateColumnDividers(columnWidths, rowHeights, cellContents);
		rowModelSegments = calculateRowDividers(columnWidths, rowHeights, cellContents);
	}
}

protected void paintFigure(Graphics g){
	g.setForegroundColor(ColorConstants.gray);
	g.setLineStyle(Graphics.LINE_DOT);
	drawColumnDividers(g);
	drawRowDividers(g);
}

/**
 * Helper routine to map model point to figure point (in absolute coordinates).
 * @param point The point to map. It will be modified so make a copy if necessary before calling.
 * @return the point passed in but modified by the mapping.
 * 
 * @since 1.2.0
 */
public Point mapModelToFigure(Point point) {
	Rectangle figureBounds = getBounds();
	return LayoutPolicyHelper.mapModelToFigure(helper.getContainerProxyAdapter(), this, point).translate(figureBounds.x, figureBounds.y);
}

/**
 * Helper routine to map model rectangle to figure rectangle (in absolute coordinates).
 * @param rect The rectto map. It will be modified so make a copy if necessary before calling.
 * @return the rect passed in but modified by the mapping.
 * 
 * @since 1.2.0
 */
public Rectangle mapModelToFigure(Rectangle rect) {
	Rectangle figureBounds = getBounds();
	return LayoutPolicyHelper.mapModelToFigure(helper.getContainerProxyAdapter(), this, rect).translate(figureBounds.x, figureBounds.y);
}

/**
 * Helper routine to map figure point (in absolute coordinates) to model.
 * @param point The point to map. It will be modified so make a copy if necessary before calling.
 * @return the point passed in but modified by the mapping.
 * 
 * @since 1.2.0
 */
public Point mapFigureToModel(Point point) {
	Rectangle figureBounds = getBounds();
	return LayoutPolicyHelper.mapFigureToModel(helper.getContainerProxyAdapter(), this, point.translate(-figureBounds.x, -figureBounds.y));
}

/**
 * Map the x,y in figure absolute coordinates to model coordinates.
 * @param x
 * @param y
 * @return
 * 
 * @since 1.2.0
 */
public Point mapFigureToModel(int x, int y) {
	return mapFigureToModel(new Point(x, y));
}

/**
 * Helper routine to map figure rect (in absolute coordinates) to model.
 * @param rect The rect to map. It will be modified so make a copy if necessary before calling..
 * @return the rect passed in but modified by the mapping.
 * 
 * @since 1.2.0
 */
public Rectangle mapFigureToModel(Rectangle rect) {
	Rectangle figureBounds = getBounds();
	return LayoutPolicyHelper.mapFigureToModel(helper.getContainerProxyAdapter(), this, rect.translate(-figureBounds.x, -figureBounds.y));
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
	if (rowHeights == null || columnWidths == null || helper.getContainerClientArea() == null) return null;
	
	int spacingLeft = (int)Math.ceil((double)horizontalSpacing / 2);
	int spacingRight = (int)Math.floor((double)horizontalSpacing / 2);
	int spacingTop = (int)Math.ceil((double)verticalSpacing / 2);
	int spacingBottom = (int)Math.floor((double)verticalSpacing / 2);
		
	int containerHeight = 0;
	columnModelPositions = new int [columnWidths.length+1];
	columnStartModelPositions = new Point [columnModelPositions.length];
	columnEndModelPositions = new Point [columnModelPositions.length];
	int[][] columnSegments = new int[columnModelPositions.length][];

	for (int i = 0; i < rowHeights.length; i++) {
		containerHeight += rowHeights[i];
	}
	// add height for the spacing
	containerHeight += marginHeight * 2;
	containerHeight += verticalSpacing * (rowHeights.length - 1);
		
	Rectangle clientArea = helper.getContainerClientArea();
	int xPos = clientArea.x;
	int yMin = clientArea.y;
	int yMax = clientArea.y + containerHeight;
	int xMax = xPos+clientArea.width-1;	// This is the far right side, less one so that it draws within the box (otherwise it would be outside the box).
	
	// draw the first divider
	columnModelPositions[0] = xPos;
	columnStartModelPositions[0] = new Point(xPos, yMin);
	columnEndModelPositions[0] = new Point(xPos, yMax);
	columnSegments[0] = new int[] {yMin, yMax};	// The left border will always be one segemnt.
	
	// move up by the initial margin width
	xPos += marginWidth;
	
	int[] colSegs = new int[2+rowHeights.length*2];	// Each entry is a y position. They are in 2-tuples (start,stop) for a segment. For a col, it is max of one segment(or 2 points) + 2 for start of next.	
	// draw the dividers in between and at the end
	for (int i = 1; i < columnModelPositions.length; i++) {
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
		columnModelPositions[i] = xPos;
		columnStartModelPositions[i] = startPoint;
		columnEndModelPositions[i] = new Point(xPos, yMax);
		
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
	if (columnModelSegments == null || columnStartModelPositions == null)
		return;
	Point fromPoint = new Point();
	Point toPoint = new Point();	
	for (int i = 0; i < columnModelSegments.length; i++) {
		int[] colSegs = columnModelSegments[i];
		int xPos = columnStartModelPositions[i].x;
		int j=-1;
		while(++j < colSegs.length) {
			// Map from model to figure coordinates. First map to relative to figure, and then maps to absolute (through the figure bounds upper-left).
			mapModelToFigure(fromPoint.setLocation(xPos, colSegs[j]));
			mapModelToFigure(toPoint.setLocation(xPos, colSegs[++j]));
			g.drawLine(fromPoint, toPoint);
		}
	}
}

/**
 * Draw the row dividers based on the on the GridBagLayout's origin and the row heights.
 * Note: Row heights from GridBagLayout that equal zero indicate there are no components
 * in that specific row.
 */
protected int[][] calculateRowDividers(int[] columnWidths, int[] rowHeights, EObject[][] cellContents) {
	if (columnWidths == null || rowHeights == null || helper.getContainerClientArea() == null) return null;
	
	int spacingLeft = (int)Math.ceil((double)horizontalSpacing / 2);
	int spacingRight = (int)Math.floor((double)horizontalSpacing / 2);
	int spacingTop = (int)Math.ceil((double)verticalSpacing / 2);
	int spacingBottom = (int)Math.floor((double)verticalSpacing / 2);
	
	int containerWidth = 0;
	rowModelPositions = new int [rowHeights.length+1];
	rowStartModelPositions = new Point [rowModelPositions.length];
	rowEndModelPositions = new Point [rowModelPositions.length];
	int[][] rowSegments = new int[rowModelPositions.length][];

	for (int i = 0; i < columnWidths.length; i++) {
		containerWidth += columnWidths[i];
	}
	// add width for the spacing
	containerWidth += marginWidth * 2;
	containerWidth += horizontalSpacing * ( columnWidths.length - 1);
	
	Rectangle clientArea = helper.getContainerClientArea();
	int yPos = clientArea.y;
	int xMin = clientArea.x;
	int xMax = clientArea.x + containerWidth;
	int yMax = yPos+clientArea.height-1;	// This is the bottom side, less one so that it draws within the box (otherwise it would be outside the box).
	
	// draw the first divider
	rowModelPositions[0] = yPos;
	rowStartModelPositions[0] = new Point(xMin, yPos);
	rowEndModelPositions[0] = new Point(xMax, yPos);
	rowSegments[0] = new int[] {xMin, xMax};	// The top border will always be one segemnt.
	
	// move up the initial margin height
	yPos += marginHeight;
	
	int[] rowSegs = new int[2+columnWidths.length*2];	// Each entry is an x position. They are in 2-tuples (start,stop) for a segment. For a row, it is max of one segment(or 2 points) + 2 for start of next.
	// draw the dividers in between and at the end
	for (int i = 1; i < rowModelPositions.length; i++) {
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
		rowModelPositions[i] = yPos;
		rowStartModelPositions[i] = startPoint;
		rowEndModelPositions[i] = endPoint;
		
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
	if (rowModelSegments == null || rowStartModelPositions == null)
		return;
	Point fromPoint = new Point();
	Point toPoint = new Point();	
	for (int i = 0; i < rowModelSegments.length; i++) {
		int[] rowSegs = rowModelSegments[i];
		int yPos = rowStartModelPositions[i].y;
		int j=-1;
		while(++j < rowSegs.length) {
			// Map from model to figure coordinates. First map to relative to figure, and then maps to absolute (through the figure bounds upper-left).
			mapModelToFigure(fromPoint.setLocation(rowSegs[j], yPos));
			mapModelToFigure(toPoint.setLocation(rowSegs[++j], yPos));
			g.drawLine(fromPoint, toPoint);
		}
	}
}

/**
 * Get the cell location (i.e. the grid x/y) that the point (in model coordinates) is within.
 * 
 * @param p point to look for what cell it is in. It is in model coordinates.
 * @return the cell grid x/y as a point. '-1' for a grid means the incoming dimension was not within a cell.
 * 
 * @since 1.2.0
 */
public Point getCellLocation(Point p) {
	return getCellLocation(p.x, p.y);
}

/**
 * Get the cell location (i.e. the grid x/y) that the point (in model coordinates) is within.
 * 
 * @param x x to look for what cell it is in. It is in model coordinates.
 * @param y y to look for what cell it is in. It is in model coordinates.
 * @return the cell grid x/y as a point. '-1' for a grid means the incoming dimension was not within a cell.
 * 
 * @since 1.2.0
 */
public Point getCellLocation(int x, int y) {
	return getCellLocation(x, y, false, false);
}

/**
 * Get the cell location (i.e. the grid x/y) that the point (in model coordinates) is within.
 * @param x x to look for what cell it is in. It is in model coordinates.
 * @param y y to look for what cell it is in. It is in model coordinates.
 * @param includeEmptyColumns
 * @param includeEmptyRows
 * @return the cell grid x/y as a point. '-1' for a grid means the incoming dimension was not within a cell.
 * 
 * @since 1.2.0
 */
public Point getCellLocation(int x, int y, boolean includeEmptyColumns, boolean includeEmptyRows) {
	if (rowModelPositions == null || columnModelPositions == null)
		return new Point(-1,-1);
	
	int gridx = 0, gridy = 0;
	boolean foundx = false, foundy = false;
	for (int i = 0; i < columnModelPositions.length-1; i++) {
		int xpos = columnModelPositions[i];
		if (x >= xpos && x < columnModelPositions[i+1]) {
			gridx = i;
			if (includeEmptyColumns) {
				/*
				 * Since column positions can be equal if there columns that don't contain components,
				 * iterate back throught the columns positions to get the first one with this position.
				 */
				int j;
				for (j = i; j >= 0 && columnModelPositions[i] == columnModelPositions[j]; j--);
				gridx = j + 1;
			}
			foundx = true;
			break;
		}
	} 
	for (int i = 0; i < rowModelPositions.length-1; i++) {
		int ypos = rowModelPositions[i];
		if (y >= ypos && y < rowModelPositions[i+1]) {
			gridy = i;
			if (includeEmptyRows) {
				/*
				 * Since row positions can be equal if there rows that don't contain components,
				 * iterate back throught the rows to get the first one with this position.
				 */
				int j;
				for (j = i; j >= 0 && rowModelPositions[i] == rowModelPositions[j]; j--);
				gridy = j + 1;
			}
			foundy = true;
			break;
		}
	} 
	
	if (!foundx)
		gridx = -1;
	if (!foundy)
		gridy = -1;
		
	return new Point(gridx,gridy);
}


/**
 * Get the cell bounds for the cell that the model position is within.
 * @param pos position in model coordinates
 * @return cell bounds in model coordinates of the cell that the pos is in, or <code>null</code> if not within a cell. It can be modified.
 * 
 * @since 1.2.0
 */
public Rectangle getCellBounds(Point pos) {
	if (rowModelPositions == null || columnModelPositions == null)
		return new Rectangle();
	
	int cellxpos = 0, cellypos = 0, cellwidth = 0, cellheight = 0;
	boolean foundCell = false;
	for (int i = 0; i < columnModelPositions.length-1; i++) {
		int xpos = columnModelPositions[i];
		if (pos.x >= xpos && pos.x < columnModelPositions[i+1]) {
			cellxpos = xpos;
			cellwidth = columnModelPositions[i+1] - xpos;
			foundCell = true;
			break;
		}
	} 
	if (!foundCell)
		return null;
	foundCell = false;
	for (int i = 0; i < rowModelPositions.length-1; i++) {
		int ypos = rowModelPositions[i];
		if (pos.y >= ypos && pos.y < rowModelPositions[i+1]) {
			cellypos = ypos;
			cellheight = rowModelPositions[i+1] - ypos;
			foundCell = true;
			break;
		}
	} 
	return foundCell ? new Rectangle(cellxpos, cellypos, cellwidth, cellheight) : null;
}

/**
 * Get the grid figure rect for the specified cell dimensions.  The cells dimensions
 * are packed into a Rectangle according to the following rules:
 * 
 * rect.x = column position
 * rect.y = row position
 * rect.width = horizontal span
 * rect.height = vertical span
 * 
 * @param cellsBounds  The cell area to calculate
 * @return the rect in model coor. for the grid representing these cells.
 * 
 * @since 1.0.0
 */
public Rectangle getGridBroundsForCellBounds(Rectangle cellsBounds) {
	Rectangle r = new Rectangle();
	if (rowModelPositions != null && columnModelPositions != null && cellsBounds.y <= rowModelPositions.length - 1 &&
			cellsBounds.x <= columnModelPositions.length - 1) {
		r.x = columnModelPositions[cellsBounds.x];
		r.y = rowModelPositions[cellsBounds.y];
		
		if (cellsBounds.x + cellsBounds.width > columnModelPositions.length - 1) {
			r.width = columnModelPositions[columnModelPositions.length - 1];
		} else {
			r.width = columnModelPositions[cellsBounds.x + cellsBounds.width];
		}
		r.width -= r.x;
		
		if (cellsBounds.y + cellsBounds.height > rowModelPositions.length - 1) {
			r.height = rowModelPositions[rowModelPositions.length - 1];
		} else {
			r.height = rowModelPositions[cellsBounds.y + cellsBounds.height];
		}
		r.height -= r.y;
	}
	return r;
	
}

/**
 * Get the rectangle for the column left hand side (in model terms) nearest the x sent it. The x needs
 * to be within row/column sensitivity of the column to find it.
 * 
 * @param x x in model coordinates to look for the column. It must be within {@link #ROW_COLUMN_SENSITIVITY} if outside the column.
 * @return rect (0 width) for the left hand side of the column. This rect can be modified by caller. It will be in model coors.
 * 
 * @since 1.2.0
 */
public Rectangle getColumnRectangle(int x) {
	if (columnStartModelPositions != null) {
		for (int i = 0; i < columnModelPositions.length; i++) {
			int xpos = columnModelPositions[i];
			if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY))
				return new Rectangle(columnStartModelPositions[i], columnEndModelPositions[i]).resize(-1,-1);	// This ctor makes them bigger by 1.
		} 
	}
	return new Rectangle();
}

/**
 * Get column start position in model coordinates.
 * @param x x in model coordinates to look for the column. It must be within {@link #ROW_COLUMN_SENSITIVITY} if outside the column.
 * @return the column start position in model coors. It can be modified.
 * 
 * @since 1.2.0
 */
public Point getColumnStartPosition(int x) {
	if (columnStartModelPositions != null) {
		for (int i = 0; i < columnModelPositions.length; i++) {
			int xpos = columnModelPositions[i];
			if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY)) 
				return columnStartModelPositions[i].getCopy();
		} 
	}
	return new Point(0,0);
}

/**
 * Get column end position in model coordinates.
 * @param x x in model coordinates to look for the column. It must be within {@link #ROW_COLUMN_SENSITIVITY} if outside the column.
 * @return the column end position in model coors. It can be modified.
 * 
 * @since 1.2.0
 */
public Point getColumnEndPosition(int x) {
	if (columnEndModelPositions != null) {
		for (int i = 0; i < columnModelPositions.length; i++) {
			int xpos = columnModelPositions[i];
			if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY)) 
				return columnEndModelPositions[i].getCopy();
		} 
	}
	return new Point(0,0);
}

/**
 * Get the rectangle for the row top hand side (in model terms) nearest the y sent it. The y needs
 * to be within row/column sensitivity of the column to find it.
 * 
 * @param y y in model coordinates to look for the row. It must be within {@link #ROW_COLUMN_SENSITIVITY} if outside the row.
 * @return rect (0 width) for the top side of the row. It will be in model coors. It can be modified.
 * 
 * @since 1.2.0
 */
public Rectangle getRowRectangle(int y) {
	if (rowStartModelPositions != null) {
		for (int i = 0; i < rowModelPositions.length; i++) {
			int ypos = rowModelPositions[i];
			if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY))
				return new Rectangle(rowStartModelPositions[i], rowEndModelPositions[i]).resize(-1,-1);	// This ctor makes them bigger by 1.
		} 
	}
	return new Rectangle();
}

/**
 * Get row start position in model coordinates.
 * @param y y in model coordinates to look for the row. It must be within {@link #ROW_COLUMN_SENSITIVITY} if outside the row.
 * @return the row start position in model coors. It can be modified.
 * @since 1.2.0
 */
public Point getRowStartPosition(int y) {
	if (rowStartModelPositions != null) {
		for (int i = 0; i < rowModelPositions.length; i++) {
			int ypos = rowModelPositions[i];
			if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY)) 
				return rowStartModelPositions[i].getCopy();
		}
	}
	return new Point(0,0);
}

/**
 * Get row end position in model coordinates.
 * @param y y in model coordinates to look for the row. It must be within {@link #ROW_COLUMN_SENSITIVITY} if outside the row.
 * @return the row end position in model coors. It can be modified.
 * 
 * @since 1.2.0
 */

public Point getRowEndPosition(int y) {
	if (rowEndModelPositions != null) {
		for (int i = 0; i < rowModelPositions.length; i++) {
			int ypos = rowModelPositions[i];
			if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY)) 
				return rowEndModelPositions[i].getCopy();
		} 
	}
	return new Point(0,0);
}

/**
 * Is x (in model coor) near a column (i.e. within it or just outside within {@link #ROW_COLUMN_SENSITIVITY}).
 * 
 * @param x x in model coor.
 * @return <code>true</code> if near a column.
 * 
 * @since 1.2.0
 */
public boolean isPointerNearAColumn(int x) {
	if (columnModelPositions == null)
		return false;

	for (int i = 0; i < columnModelPositions.length; i++) {
		int xpos = columnModelPositions[i];
		if ((x >= xpos - ROW_COLUMN_SENSITIVITY) && (x <= xpos + ROW_COLUMN_SENSITIVITY)) 
			return true;
	} 
	return false;
}
/**
 * Is y (in model coor) near a row (i.e. within it or just outside within {@link #ROW_COLUMN_SENSITIVITY}).
 * 
 * @param y y in model coor.
 * @return <code>true</code> if near a row.
 * 
 * @since 1.2.0
 */
public boolean isPointerNearARow(int y) {
	if (rowModelPositions == null)
		return false;

	for (int i = 0; i < rowModelPositions.length; i++) {
		int ypos = rowModelPositions[i];
		if ((y >= ypos - ROW_COLUMN_SENSITIVITY) && (y <= ypos + ROW_COLUMN_SENSITIVITY)) 
			return true;
	} 
	return false;
}

/**
 * Get the nearest row to the incoming y (in model coor).
 * @param y y position (in model coor) to find the nearest row.
 * @return the nearest row to the incoming y.
 * 
 * @since 1.2.0
 */
public int getNearestRow(int y) {
	if (rowModelPositions == null || rowModelPositions.length == 0)
		return 0;

	int row = 0;
	int value = Math.abs(y);
	for (int i = 0; i < rowModelPositions.length; i++) {
		int diff = Math.abs(rowModelPositions[i] - y);
		if (diff < value) {
			row = i;
			value = diff;
		}
	} 
	return row;
}

/**
 * Get the nearest column to the x (in model coor).
 * 
 * @param x x to find nearest column to. It is in model coor.
 * @return the column number that is nearest to the incoming x.
 * 
 * @since 1.2.0
 */
public int getNearestColumn(int x) {
	if (columnModelPositions == null || columnModelPositions.length == 0)
		return 0;

	int column = 0;
	int value = Math.abs(x);
	for (int i = 0; i < columnModelPositions.length; i++) {
		int diff = Math.abs(columnModelPositions[i] - x);
		if (diff < value) {
			column = i;
			value = diff;
		}
	} 
	return column;
}


}
