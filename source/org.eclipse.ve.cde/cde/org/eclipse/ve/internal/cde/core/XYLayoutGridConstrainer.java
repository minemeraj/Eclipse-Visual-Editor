package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: XYLayoutGridConstrainer.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


import org.eclipse.draw2d.geometry.*;
/**
 * This is used with XYLayout to constrain objects to the grid, i.e.
 * you can only drop to the grid.
 */

public class XYLayoutGridConstrainer {
	protected GridFigure gridFigure;
	
	public XYLayoutGridConstrainer(GridFigure gridFigure) {
		this.gridFigure = gridFigure;
	}
	
	/**
	 * The constraint is a rectangle.  Snap its position to the grid.
	 */
	public Rectangle adjustConstraintFor(Rectangle constraint){
	
		GridFigure.GridData gd = new GridFigure.GridData();
		gridFigure.getGridData(gd);
		// The gridData is adjusted for zooming, however, our constraint coming in
		// is in constraint format, i.e. it is not zoomed, and its coordinates do not take into account.
		// So we use only the gridWidth/gridHeight and gridBorder to calculate the maxGridNumbers
		// in each direction. The rest of the calculations will be done on non-zoomed coordinates.
		
		Rectangle result = constraint.getCopy();

		int gridMargin = gd.gridController.getGridMargin();
		if (constraint.x < gridMargin)
			result.x = gridMargin;	// Before start of grid, snap to first grid
		else {
			int maxGridNum = (int) Math.round(gd.gridBorder.width / gd.gridWidth);	// Rightmost grid point as a number.
			int gridNum = (int) Math.round((result.x - gridMargin)/((double) gd.gridController.getGridWidth()));	// So that it rounds to nearest grid point
			if (gridNum > maxGridNum)
				gridNum = maxGridNum;
			result.x = gridNum*gd.gridController.getGridWidth() + gridMargin;
		}
		
		if (constraint.y < gridMargin)
			result.y = gridMargin;	// Before start of grid, snap to first grid
		else {
			int maxGridNum = (int) Math.round(gd.gridBorder.height / gd.gridHeight);	// Bottommost grid point as a number.
			int gridNum = (int) Math.round((result.y - gridMargin)/((double) gd.gridController.getGridHeight()));	// So that it rounds to nearest grid point
			if (gridNum > maxGridNum)
				gridNum = maxGridNum;
			result.y = gridNum*gd.gridController.getGridHeight() + gridMargin;
		}

		return result;		
	}
}