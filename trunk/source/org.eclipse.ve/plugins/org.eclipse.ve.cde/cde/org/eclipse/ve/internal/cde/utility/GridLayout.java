/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridLayout.java,v $
 *  $Revision: 1.1 $  $Date: 2004-07-01 15:53:49 $ 
 */
package org.eclipse.ve.internal.cde.utility;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
 

/**
 * 
 * @since 1.0.0
 */
public class GridLayout extends AbstractLayout {
	
	public int columns = 1;

	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		// TODO Auto-generated method stub
		return null;
	}
	public void layout(IFigure container) {
		// Find how many rows there are
		int nFigures = container.getChildren().size();
		int rows = nFigures/columns;
		int[] colWidths = new int[columns];
		int[] colHeights = new int[rows];
		// Calculate the width of each column, by taking the preferred size of each figure that is within it
		for(int c=0;c<columns;c++){
			// Get the preferred width of each figure in that column
			int maxWidth = 0;
			for(int r=0;r<rows;r++){
				IFigure figure = (IFigure)container.getChildren().get(r * columns + c);
				Dimension preferredSize = figure.getPreferredSize(-1,-1);
				maxWidth = Math.max(maxWidth,preferredSize.width);
				colHeights[r] = Math.max(colHeights[r],preferredSize.height);
			}
			colWidths[c] = Math.max(colWidths[c],maxWidth);
		}
		// Now we know the height and row of each figure, set its bounds
		int x=0;
		int y=0;
		int row=0;
		int col=0;
		// Iterate over the children
		for(int i=0;i<container.getChildren().size();i++){
			IFigure childFigure = (IFigure)container.getChildren().get(i);
			childFigure.setBounds(new Rectangle(x,y,colWidths[col],colHeights[row]));
			x=x+colWidths[col];
//			if()
		}		
	}
}
