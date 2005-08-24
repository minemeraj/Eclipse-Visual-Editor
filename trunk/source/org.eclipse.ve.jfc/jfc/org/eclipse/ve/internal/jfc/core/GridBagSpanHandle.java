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
 *  $RCSfile: GridBagSpanHandle.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.swt.graphics.Color;

public class GridBagSpanHandle extends ResizeHandle {
	public static int HANDLE_SIZE = DEFAULT_HANDLE_SIZE;

	public GridBagSpanHandle(GraphicalEditPart owner, int direction) {
		super(owner, direction);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.handles.SquareHandle#getBorderColor()
	 */
	protected Color getBorderColor() {
		return ColorConstants.darkGray;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.handles.SquareHandle#getFillColor()
	 */
	protected Color getFillColor() {
		return ColorConstants.lightGreen;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	 
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds();
		r.shrink(1,1);
		try {
			g.setBackgroundColor(getFillColor());
//			g.fillOval(r.x, r.y+1, r.width, r.height - 2);
//			g.fillArc(r, 5, 5);
			g.fillRectangle(r.x, r.y, r.width, r.height);
			g.setForegroundColor(getBorderColor());
//			g.drawOval(r.x, r.y+1, r.width, r.height-2); 
//			g.drawArc(r, 1, 8);
			g.drawRectangle(r.x, r.y, r.width, r.height);
		} finally {
			//We don't really own rect 'r', so fix it.
			r.expand(1,1);
		}
	}

}
