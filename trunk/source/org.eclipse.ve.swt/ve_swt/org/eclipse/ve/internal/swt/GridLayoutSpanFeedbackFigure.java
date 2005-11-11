/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Used for spanning, adds in a little extra dashed line so we can see the column/row going to.
 * 
 * @since 1.1.0
 */
public class GridLayoutSpanFeedbackFigure extends Figure {

	private final int spanDirection;

	private GridLayoutFeedbackFigure layoutFeedbackFigure;

	public GridLayoutSpanFeedbackFigure(int spanDirection) {
		super();
		this.spanDirection = spanDirection;
		add(layoutFeedbackFigure = new GridLayoutFeedbackFigure());
	}

	public void setLayoutFigureBounds(Rectangle bounds) {
		layoutFeedbackFigure.setBounds(bounds);
		// Now increase this slight for our bounds and the line we want to draw.
		// The"8" makes the end line 8 pixels out on each side. The "1" is necessary because we want to be over
		// the next cell border and our bounds are just inside the cell border.
		bounds = spanDirection == PositionConstants.EAST || spanDirection == PositionConstants.WEST ? bounds.getExpanded(1, 8) : bounds.getExpanded(
				8, 1);
		setBounds(bounds);
	}

	protected void paintClientArea(Graphics graphics) {
		super.paintClientArea(graphics);

		// Paint span line.
		graphics.setForegroundColor(ColorConstants.darkGray);
		Rectangle bounds = getBounds();
		switch (spanDirection) {
			case PositionConstants.EAST:
				int x = bounds.x + bounds.width - 1;
				graphics.drawLine(x, bounds.y, x, bounds.y + bounds.height - 1);
				break;
			case PositionConstants.WEST:
				x = bounds.x;
				graphics.drawLine(x, bounds.y, x, bounds.y + bounds.height - 1);
				break;
			case PositionConstants.SOUTH:
				int y = bounds.y + bounds.height - 1;
				graphics.drawLine(bounds.x, y, bounds.x + bounds.width - 1, y);
				break;
		}
	}

}
