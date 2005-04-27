/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementationf
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class GridLayoutHelper {

	private Composite fComposite;
	private GridLayout gridLayout;
	public int[] columnWidths;
	public int[] rowHeights;
	public int[] expandableColumns;
	public int[] expandableRows;
	private GridLayout fGridLayout;
	private Field cacheWidthField;
	private Field cacheHeightField;
	private java.util.List grid = new ArrayList();

	public void setComposite(Composite aComposite) {

		fComposite = aComposite;
		fGridLayout = (GridLayout) aComposite.getLayout();
		computeValues();

	}

	private int getCacheWidth(GridData aGridData) {
		if (cacheWidthField == null) {
			try {
				cacheWidthField = GridData.class.getDeclaredField("cacheWidth");
				cacheWidthField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Integer cacheWidth = (Integer) cacheWidthField.get(aGridData);
			return cacheWidth.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private int getCacheHeight(GridData aGridData) {
		if (cacheHeightField == null) {
			try {
				cacheHeightField = GridData.class.getDeclaredField("cacheHeight");
				cacheHeightField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Integer cacheWidth = (Integer) cacheHeightField.get(aGridData);
			return cacheWidth.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void setCacheWidth(GridData aGridData, int aWidth) {

	}	
	
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		Control[] children = composite.getChildren();
		int numChildren = children.length;

		if (numChildren == 0) return new Point(0,0);

		if (flushCache) {
			// Cause the grid and its related information to be calculated
			// again.
			grid.clear();
		}
		return computeLayoutSize(composite, wHint, hHint, flushCache);
	}	
	
	GridData[] emptyRow() {
		GridData[] row = new GridData[fGridLayout.numColumns];
		for (int i = 0; i < fGridLayout.numColumns; i++) {
			row[i] = null;}
		return row;
	}
	
	Point getFirstEmptyCell(int row, int column) {
		GridData[] rowData = (GridData[]) grid.get(row);
		while (column < fGridLayout.numColumns && rowData[column] != null) {
			column++;
		}
		if (column == fGridLayout.numColumns) {
			row++;
			column = 0;
			if (row  == grid.size()) {
				grid.add(emptyRow());
			}
			return getFirstEmptyCell(row, column);
		}
		return new Point(row, column);
	}
	Point getLastEmptyCell(int row, int column) {
		GridData[] rowData = (GridData[])grid.get(row);
		while (column < fGridLayout.numColumns && rowData[column] == null ) {
			column++;
		}
		return new Point(row, column - 1);
	}	
	
	Point getCell(int row, int column, int width, int height) {
		Point start = getFirstEmptyCell(row, column);
		Point end = getLastEmptyCell(start.x, start.y);
		if (end.y + 1 - start.y >= width) return start;
		GridData[] rowData = (GridData[]) grid.get(start.x);
		for (int j = start.y; j < end.y + 1; j++) {
			GridData spacerSpec = new GridData();
			rowData[j] = spacerSpec;
		}
		return getCell(end.x, end.y, width, height);
	}	
	
	private HashMap dataIndexes = new HashMap();
	void putChildIndex(GridData aData, int index){
		dataIndexes.put(aData,new Integer(index));
	}
	int getChildIndex(GridData aData){
		Integer index = (Integer)dataIndexes.get(aData);
		return index == null ? 0 : index.intValue();
	}
	
	private HashMap itemData = new HashMap();
	void putItemData(GridData aData, boolean isItemData){
		itemData.put(aData,isItemData ? Boolean.TRUE : Boolean.FALSE);
	}
	boolean isItemData(GridData aData){
		Boolean isItemData = (Boolean)itemData.get(aData);
		return isItemData == null ? true : isItemData.booleanValue();
	}
	
	private HashMap childGridData = new HashMap();
	void putChildGridData(Control child,GridData data){
		childGridData.put(child,data);
	}
	
	void createGrid(Composite composite) {
		int row, column, rowFill, columnFill;
		Control[] children;
		GridData spacerSpec;

		// 
		children = composite.getChildren();

		// 
		grid.add(emptyRow());
		row = 0;
		column = 0;

		// Loop through the children and place their associated layout specs in the
		// grid.  Placement occurs left to right, top to bottom (i.e., by row).
		for (int i = 0; i < children.length; i++) {
			// Find the first available spot in the grid.
			Control child = children[i];
			GridData spec = (GridData) child.getLayoutData();
			if (spec == null) {
				spec = new GridData();
			} 
			putChildGridData(child,spec);
			spec.horizontalSpan = Math.min(spec.horizontalSpan, fGridLayout.numColumns);
			Point p = getCell(row, column, spec.horizontalSpan, spec.verticalSpan);
			row = p.x; column = p.y;

			// The vertical span for the item will be at least 1.  If it is > 1,
			// add other rows to the grid.
			for (int j = 2; j <= spec.verticalSpan; j++) {
				if (row + j > grid.size()) {
					grid.add(emptyRow());
				}
			}

			// Store the layout spec.  Also cache the childIndex.  NOTE: That we assume the children of a
			// composite are maintained in the order in which they are created and added to the composite.
			((GridData[]) grid.get(row))[column] = spec;
			putChildIndex(spec,i);

			// Put spacers in the grid to account for the item's vertical and horizontal
			// span.
			rowFill = spec.verticalSpan - 1;
			columnFill = spec.horizontalSpan - 1;
			for (int r = 1; r <= rowFill; r++) {
				for (int c = 0; c < spec.horizontalSpan; c++) {
					spacerSpec = new GridData();
					((GridData[]) grid.get(row + r))[column + c] = spacerSpec;
				}
			}
			for (int c = 1; c <= columnFill; c++) {
				for (int r = 0; r < spec.verticalSpan; r++) {
					spacerSpec = new GridData();
					putItemData(spacerSpec,false);
					((GridData[]) grid.get(row + r))[column + c] = spacerSpec;
				}
			}
			column = column + spec.horizontalSpan - 1;
		}

		// Fill out empty grid cells with spacers.
		for (int r = row; r < grid.size(); r++) {
			GridData[] rowData = (GridData[]) grid.get(r);
			for (int c = 0; c < fGridLayout.numColumns; c++) {
				if (rowData[c] == null) {
					spacerSpec = new GridData();
					putItemData(spacerSpec,false);
					rowData[c] = spacerSpec;
				}
			}
		}
	}
	
	void calculateGridDimensions(Composite composite, boolean flushCache) {
		int maxWidth, childWidth, maxHeight, childHeight;
		
		//
		Control[] children = composite.getChildren();
		Point[] childSizes = new Point[children.length];
		columnWidths = new int[fGridLayout.numColumns];
		rowHeights = new int[grid.size()];
		
		// Loop through the grid by column to get the width that each column needs to be.
		// Each column will be as wide as its widest control.
		for (int column = 0; column < fGridLayout.numColumns; column++) {
			maxWidth = 0;
			for (int row = 0; row < grid.size(); row++) {
				GridData spec = ((GridData[]) grid.get(row))[column];
				if (isItemData(spec)) {
					Control child = children[getChildIndex(spec)];
					childSizes[getChildIndex(spec)] = child.computeSize(spec.widthHint, spec.heightHint, flushCache);
					childWidth = childSizes[getChildIndex(spec)].x + spec.horizontalIndent;
					if (spec.horizontalSpan == 1) {
						maxWidth = Math.max(maxWidth, childWidth);
					}
				}
			}
			// Cache the values for later use.
			columnWidths[column] = maxWidth;
		}

		// 
		if (fGridLayout.makeColumnsEqualWidth) {
			maxWidth = 0;
			// Find the largest column size that is necessary and make each column that size.
			for (int i = 0; i < fGridLayout.numColumns; i++) {
				maxWidth = Math.max(maxWidth,columnWidths[i]);
			}
			maxWidth += fGridLayout.horizontalSpacing;
			for (int i = 0; i < fGridLayout.numColumns; i++) {
				columnWidths[i] = maxWidth;
			}
		}

		// Loop through the grid by row to get the height that each row needs to be.
		// Each row will be as high as its tallest control.
		for (int row = 0; row < grid.size(); row++) {
			maxHeight = 0;
			for (int column = 0; column < fGridLayout.numColumns; column++) {
				GridData spec = ((GridData[]) grid.get(row))[column];
				if (isItemData(spec)) {
					childHeight = childSizes[getChildIndex(spec)].y;
					if (spec.verticalSpan == 1) {
						maxHeight = Math.max(maxHeight, childHeight);
					}
				}
			}
			// Cache the values for later use.
			rowHeights[row] = maxHeight;
		}
	}	
	
	void computeExpandableCells() {
		// If a control grabs excess horizontal space, the last column that the control spans
		// will be expandable.  Similarly, if a control grabs excess vertical space, the 
		// last row that the control spans will be expandable.
		Hashtable growColumns = new Hashtable();
		Hashtable growRows = new Hashtable();
		for (int col = 0; col < fGridLayout.numColumns; col++) {
			for (int row = 0; row < grid.size(); row++) {
				GridData spec = ((GridData[]) grid.get(row))[col];
				if (spec.grabExcessHorizontalSpace) {
					growColumns.put(new Integer(col + spec.horizontalSpan - 1), new Object());
				}
				if (spec.grabExcessVerticalSpace) {
					growRows.put(new Integer(row + spec.verticalSpan - 1), new Object());
				}
			}
		}

		// Cache the values.  These values are used later during children layout.
		int i = 0;
		Enumeration enumeration = growColumns.keys();
		expandableColumns = new int[growColumns.size()];
		while (enumeration.hasMoreElements()) {
			expandableColumns[i] = ((Integer)enumeration.nextElement()).intValue();
			i = i + 1;
		}
		i = 0;
		enumeration = growRows.keys();
		expandableRows = new int[growRows.size()];
		while (enumeration.hasMoreElements()) {
			expandableRows[i] = ((Integer)enumeration.nextElement()).intValue();
			i = i + 1;
		}
	}
	
	void adjustGridDimensions(Composite composite, boolean flushCache) {
		// Ensure that controls that span more than one row or column have enough space.
		for (int row = 0; row < grid.size(); row++) {
			for (int column = 0; column < fGridLayout.numColumns; column++) {
				GridData spec = ((GridData[]) grid.get(row))[column];
				if (isItemData(spec)) {
					// Widgets spanning columns.
					if (spec.horizontalSpan > 1) {
						Control child = composite.getChildren()[getChildIndex(spec)];
						Point extent = child.computeSize(spec.widthHint, spec.heightHint, flushCache);

						// Calculate the size of the control's spanned columns.
						int lastSpanIndex = column + spec.horizontalSpan;
						int spannedSize = 0;
						for (int c = column; c < lastSpanIndex; c++) {
							spannedSize = spannedSize + columnWidths[c] + fGridLayout.horizontalSpacing;
						}
						spannedSize = spannedSize - fGridLayout.horizontalSpacing;

						// If the spanned columns are not large enough to display the control, adjust the column
						// sizes to account for the extra space that is needed.
						if (extent.x + spec.horizontalIndent > spannedSize) {
							int extraSpaceNeeded = extent.x + spec.horizontalIndent - spannedSize;
							int lastColumn = column + spec.horizontalSpan - 1;
							int colWidth;
							if (fGridLayout.makeColumnsEqualWidth) {
								// Evenly distribute the extra space amongst all of the columns.
								int columnExtra = extraSpaceNeeded / fGridLayout.numColumns;
								int columnRemainder = extraSpaceNeeded % fGridLayout.numColumns;
								for (int i = 0; i < columnWidths.length; i++) {
									colWidth = columnWidths[i] + columnExtra;
									columnWidths[i] = colWidth;
								}
								colWidth = columnWidths[lastColumn] + columnRemainder;
								columnWidths[lastColumn] = colWidth;
							} else {
								Vector localExpandableColumns = new Vector();
								for (int i = column; i <= lastColumn; i++) {
									for (int j = 0; j < expandableColumns.length; j++) {
										if (expandableColumns[j] == i) {
											localExpandableColumns.addElement(new Integer(i));
										}
									}
								}
								if (localExpandableColumns.size() > 0) {
									// If any of the control's columns grab excess space, allocate the space amongst those columns.
									int columnExtra = extraSpaceNeeded / localExpandableColumns.size();
									int columnRemainder = extraSpaceNeeded % localExpandableColumns.size();
									for (int i = 0; i < localExpandableColumns.size(); i++) {
										int expandableCol = ((Integer) localExpandableColumns.elementAt(i)).intValue();
										colWidth = columnWidths[expandableCol] + columnExtra;
										columnWidths[expandableCol] = colWidth;
									}
									colWidth = columnWidths[lastColumn] + columnRemainder;
									columnWidths[lastColumn] = colWidth;
								} else {
									// Add the extra space to the control's last column if none of its columns grab excess space.
									colWidth = columnWidths[lastColumn] + extraSpaceNeeded;
									columnWidths[lastColumn] = colWidth;
								}
							}
						}
					}

					// Widgets spanning rows.
					if (spec.verticalSpan > 1) {
						Control child = composite.getChildren()[getChildIndex(spec)];
						Point extent = child.computeSize(spec.widthHint, spec.heightHint, flushCache);

						// Calculate the size of the control's spanned rows.
						int lastSpanIndex = row + spec.verticalSpan;
						int spannedSize = 0;
						for (int r = row; r < lastSpanIndex; r++) {
							spannedSize = spannedSize + rowHeights[r] + fGridLayout.verticalSpacing;
						}
						spannedSize = spannedSize - fGridLayout.verticalSpacing;
						// If the spanned rows are not large enough to display the control, adjust the row
						// sizes to account for the extra space that is needed.
						if (extent.y > spannedSize) {
							int extraSpaceNeeded = extent.y - spannedSize;
							int lastRow = row + spec.verticalSpan - 1;
							int rowHeight;
							Vector localExpandableRows = new Vector();
							for (int i = row; i <= lastRow; i++) {
								for (int j = 0; j < expandableRows.length; j++) {
									if (expandableRows[j] == i) {
										localExpandableRows.addElement(new Integer(i));
									}
								}
							}
							if (localExpandableRows.size() > 0) {
								// If any of the control's rows grab excess space, allocate the space amongst those rows.
								int rowExtra = extraSpaceNeeded / localExpandableRows.size();
								int rowRemainder = extraSpaceNeeded % localExpandableRows.size();
								for (int i = 0; i < localExpandableRows.size(); i++) {
									int expandableRow = ((Integer) localExpandableRows.elementAt(i)).intValue();
									rowHeight = rowHeights[expandableRow] + rowExtra;
									rowHeights[expandableRow] = rowHeight;
								}
								rowHeight = rowHeights[lastRow] + rowRemainder;
								rowHeights[lastRow] = rowHeight;
							} else {
								// Add the extra space to the control's last row if no rows grab excess space.
								rowHeight = rowHeights[lastRow] + extraSpaceNeeded;
								rowHeights[lastRow] = rowHeight;
							}
						}
					}
				}
			}
		}
	}	
	
	Point computeLayoutSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		int totalMarginHeight, totalMarginWidth;
		int totalWidth, totalHeight;
		int cols, rows;

		// Initialize the grid and other cached information that help with the grid layout.
		if (grid.size() == 0) {
			createGrid(composite);
			calculateGridDimensions(composite, flushCache);
			computeExpandableCells();
			adjustGridDimensions(composite, flushCache);
		}

		//
		cols = fGridLayout.numColumns;
		rows = grid.size();
		totalMarginHeight = fGridLayout.marginHeight;
		totalMarginWidth = fGridLayout.marginWidth;

		// The total width is the margin plus border width plus space between each column, 
		// plus the width of each column.
		totalWidth = (totalMarginWidth * 2) + ((cols - 1) * fGridLayout.horizontalSpacing);

		//Add up the width of each column. 
		for (int i = 0; i < columnWidths.length; i++) {
			totalWidth = totalWidth + columnWidths[i];
		}

		// The total height is the margin plus border height, plus space between each row, 
		// plus the height of the tallest child in each row.
		totalHeight = (totalMarginHeight * 2) + ((rows - 1) * fGridLayout.verticalSpacing);

		//Add up the height of each row. 
		for (int i = 0; i < rowHeights.length; i++) {
			totalHeight = totalHeight + rowHeights[i];
		}

		if (wHint != SWT.DEFAULT) totalWidth = wHint;
		if (hHint != SWT.DEFAULT) totalHeight = hHint;
		// The preferred extent is the width and height that will accomodate the grid's controls.
		return new Point(totalWidth, totalHeight);
	}	
	
	protected void computeValues() {
		int[] columnWidths;
		int[] rowHeights;
		int rowSize, rowY, columnX;
		int compositeWidth, compositeHeight;
		int excessHorizontal, excessVertical;
		Control[] children;
		grid.clear();
		children = fComposite.getChildren();
		if (children.length == 0)
			return;

		Point extent = computeSize(fComposite, SWT.DEFAULT, SWT.DEFAULT, true);
		columnWidths = new int[fGridLayout.numColumns];
		for (int i = 0; i < columnWidths.length; i++) {
			columnWidths[i] = columnWidths[i];
		}
		rowHeights = new int[grid.size()];
		for (int i = 0; i < rowHeights.length; i++) {
			rowHeights[i] = rowHeights[i];
		}
		int columnWidth = 0;
		rowSize = Math.max(1, grid.size());

		// 
		compositeWidth = extent.x;
		compositeHeight = extent.y;

		// Calculate whether or not there is any extra space or not enough space due to a resize 
		// operation.  Then allocate/deallocate the space to columns and rows that are expandable.  
		// If a control grabs excess space, its last column or row will be expandable.
		excessHorizontal = fComposite.getClientArea().width - compositeWidth;
		excessVertical = fComposite.getClientArea().height - compositeHeight;

		// Allocate/deallocate horizontal space.
		if (expandableColumns.length != 0) {
			int excess, remainder, last;
			int colWidth;
			excess = excessHorizontal / expandableColumns.length;
			remainder = excessHorizontal % expandableColumns.length;
			last = 0;
			for (int i = 0; i < expandableColumns.length; i++) {
				int expandableCol = expandableColumns[i];
				colWidth = columnWidths[expandableCol];
				colWidth = colWidth + excess;
				columnWidths[expandableCol] = colWidth;
				last = Math.max(last, expandableCol);
			}
			colWidth = columnWidths[last];
			colWidth = colWidth + remainder;
			columnWidths[last] = colWidth;
		}

		// Go through all specs in each expandable column and get the maximum specified
		// widthHint.  Use this as the minimumWidth for the column.
		for (int i = 0; i < expandableColumns.length; i++) {
			int expandableCol = expandableColumns[i];
			int colWidth = columnWidths[expandableCol];
			int minWidth = 0;
			for (int j = 0; j < grid.size(); j++) {
				GridData[] row = (GridData[]) grid.get(j);
				GridData spec = row[expandableCol];
				if (spec.horizontalSpan == 1) {
					minWidth = Math.max(minWidth, spec.widthHint);
				}
			}
			columnWidths[expandableCol] = Math.max(colWidth, minWidth);
		}
		// Allocate/deallocate vertical space.
		if (expandableRows.length != 0) {
			int excess, remainder, last;
			int rowHeight;
			excess = excessVertical / expandableRows.length;
			remainder = excessVertical % expandableRows.length;
			last = 0;
			for (int i = 0; i < expandableRows.length; i++) {
				int expandableRow = expandableRows[i];
				rowHeight = rowHeights[expandableRow];
				rowHeight = rowHeight + excess;
				rowHeights[expandableRow] = rowHeight;
				last = Math.max(last, expandableRow);
			}
			rowHeight = rowHeights[last];
			rowHeight = rowHeight + remainder;
			rowHeights[last] = rowHeight;
		}
		// Go through all specs in each expandable row and get the maximum specified
		// heightHint.  Use this as the minimumHeight for the row.
		for (int i = 0; i < expandableRows.length; i++) {
			int expandableRow = expandableRows[i];
			int rowHeight = rowHeights[expandableRow];
			int minHeight = 0;
			GridData[] row = (GridData[]) grid.get(expandableRow);
			for (int j = 0; j < fGridLayout.numColumns; j++) {
				GridData spec = row[j];
				if (spec.verticalSpan == 1) {
					minHeight = Math.max(minHeight, spec.heightHint);
				}
			}
			rowHeights[expandableRow] = Math.max(rowHeight, minHeight);
		}

		// Get the starting x and y.
		columnX = fGridLayout.marginWidth + fComposite.getClientArea().x;
		rowY = fGridLayout.marginHeight + fComposite.getClientArea().y;

		// Layout the control left to right, top to bottom.
		for (int r = 0; r < rowSize; r++) {
			int rowHeight = rowHeights[r];
			GridData[] row = (GridData[]) grid.get(r);

			// 
			for (int c = 0; c < row.length; c++) {
				int spannedWidth = 0, spannedHeight = 0;
				int hAlign = 0, vAlign = 0;
				int widgetX = 0, widgetY = 0;
				int widgetW = 0, widgetH = 0;

				//
				GridData spec = (GridData) row[c];
				if (fGridLayout.makeColumnsEqualWidth) {
					columnWidth = fComposite.getClientArea().width - 2 * (fGridLayout.marginWidth)  - ((fGridLayout.numColumns - 1) * fGridLayout.horizontalSpacing);
					columnWidth = columnWidth / fGridLayout.numColumns;
					for (int i = 0; i < columnWidths.length; i++) {
						columnWidths[i] = columnWidth;
					}
				} else {
					columnWidth = columnWidths[c];
				}

				//
				spannedWidth = columnWidth;
				for (int k = 1; k < spec.horizontalSpan; k++) {
					if ((c + k) <= fGridLayout.numColumns) {
						if (!fGridLayout.makeColumnsEqualWidth) {
							columnWidth = columnWidths[c + k];
						}
						spannedWidth = spannedWidth + columnWidth + fGridLayout.horizontalSpacing;
					}
				}

				//
				spannedHeight = rowHeight;
				for (int k = 1; k < spec.verticalSpan; k++) {
					if ((r + k) <= grid.size()) {
						spannedHeight = spannedHeight + rowHeights[r + k] + fGridLayout.verticalSpacing;
					}
				}

				//
				if (isItemData(spec)) {
					Control child = children[getChildIndex(spec)];
					Point childExtent = child.computeSize(spec.widthHint, spec.heightHint, true);
					hAlign = spec.horizontalAlignment;
					widgetX = columnX;

					// Calculate the x and width values for the control.
					if (hAlign == GridData.CENTER || hAlign == SWT.CENTER) {
						widgetX = widgetX + (spannedWidth / 2) - (childExtent.x / 2);
					} else
						if (hAlign == GridData.END || hAlign == SWT.END || hAlign == SWT.RIGHT) {
							widgetX = widgetX + spannedWidth - childExtent.x - spec.horizontalIndent;
						} else {
							widgetX = widgetX + spec.horizontalIndent;
						}
					if (hAlign == GridData.FILL) {
						widgetW = spannedWidth - spec.horizontalIndent;
						widgetX = columnX + spec.horizontalIndent;
					} else {
						widgetW = childExtent.x;
					}

					// Calculate the y and height values for the control.
					vAlign = spec.verticalAlignment;
					widgetY = rowY;
					if (vAlign == GridData.CENTER || vAlign == SWT.CENTER) {
						widgetY = widgetY + (spannedHeight / 2) - (childExtent.y / 2);
					} else
						if (vAlign == GridData.END || vAlign == SWT.END || vAlign == SWT.BOTTOM) {
							widgetY = widgetY + spannedHeight - childExtent.y;
						}
					if (vAlign == GridData.FILL) {
						widgetH = spannedHeight;
						widgetY = rowY;
					} else {
						widgetH = childExtent.y;
					}
				}
				// Update the starting x value.
				columnX = columnX + columnWidths[c] + fGridLayout.horizontalSpacing;
			}
			// Update the starting y value and since we're starting a new row, reset the starting x value.
			rowY = rowY + rowHeights[r] + fGridLayout.verticalSpacing;
			columnX = fGridLayout.marginWidth + fComposite.getClientArea().x;
		}
	}
}
