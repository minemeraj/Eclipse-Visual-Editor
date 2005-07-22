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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * Get the widths and heights of the columns and rows so that the IDE can show these in its GEF feedback
 * This class works on Eclipse 3.0 because it has access to fields in the GridLayout
 * In 3.1 these were removed so this won't work, however the IDE GridLayoutPolicyHelper instead uses
 * the newer peer named GridLayoutHelper
 */

public class GridLayoutHelper_30 {

	private Composite fComposite;
	public int[] widths;
	public int[] heights;
	private int [] expandableColumns;
	private int [] expandableRows;
	private Field pixelColumnWidthsFieldProxy;
	private Field pixelRowHeightsFieldProxy;
	private Field expandableColumnsFieldProxy;
	private Field expandableRowsFieldProxy;

	public void setComposite(Composite aComposite) {
		fComposite = aComposite;
		computeValues();
	}

	private void computeValues() {
		GridLayout gridLayout = (GridLayout) fComposite.getLayout();
		try {
			if(pixelColumnWidthsFieldProxy == null){
				pixelColumnWidthsFieldProxy = gridLayout.getClass().getDeclaredField("pixelColumnWidths");
				pixelColumnWidthsFieldProxy.setAccessible(true);
			}
			if(pixelRowHeightsFieldProxy == null){
				pixelRowHeightsFieldProxy = gridLayout.getClass().getDeclaredField("pixelRowHeights");
				pixelRowHeightsFieldProxy.setAccessible(true);				
			}			
			if(expandableColumnsFieldProxy == null){
				expandableColumnsFieldProxy = gridLayout.getClass().getDeclaredField("expandableColumns");
				expandableColumnsFieldProxy.setAccessible(true);				
			}			
			if(expandableRowsFieldProxy == null){
				expandableRowsFieldProxy = gridLayout.getClass().getDeclaredField("expandableRows");
				expandableRowsFieldProxy.setAccessible(true);				
			}			
			widths = (int[])pixelColumnWidthsFieldProxy.get(gridLayout);
			heights = (int[])pixelRowHeightsFieldProxy.get(gridLayout);			
			expandableColumns = (int[])expandableColumnsFieldProxy.get(gridLayout);
			expandableRows = (int[])expandableRowsFieldProxy.get(gridLayout);
			if (expandableColumns.length > 0 || expandableRows.length > 0)
				adjustGridDimensions(gridLayout);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

	}

	private void adjustGridDimensions(GridLayout gridLayout) {
		int[] columnWidths;
		int[] rowHeights;
		int compositeWidth, compositeHeight;
		int excessHorizontal, excessVertical;
//		Point extent = computeSize(fComposite, SWT.DEFAULT, SWT.DEFAULT, flushCache);
		Point extent = new Point(0,0);
		columnWidths = new int[gridLayout.numColumns];
		for (int i = 0; i < widths.length; i++) {
			columnWidths[i] = widths[i];
		}
		rowHeights = new int[heights.length];
		for (int i = 0; i < heights.length; i++) {
			rowHeights[i] = heights[i];
		}
		compositeWidth = extent.x;
		compositeHeight = extent.y;

		// Calculate whether or not there is any extra space or not enough space due to a resize 
		// operation.  Then allocate/deallocate the space to columns and rows that are expandable.  
		// If a control grabs excess space, its last column or row will be expandable.
		excessHorizontal = fComposite.getClientArea().width - compositeWidth;
		excessVertical = fComposite.getClientArea().height - compositeHeight;
		if (excessVertical == 0) {};

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
		for (int i = 0; i < widths.length; i++) {
			widths[i] = columnWidths[i];
		}
		// ---> NEED MORE calculations here from the 3.0 GridLayout.layout method <---
		
	}

}