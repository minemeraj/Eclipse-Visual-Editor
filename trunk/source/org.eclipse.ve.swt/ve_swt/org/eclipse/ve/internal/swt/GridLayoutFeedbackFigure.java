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
 *  $RCSfile: GridLayoutFeedbackFigure.java,v $
 *  $Revision: 1.5 $  $Date: 2005-07-11 20:03:52 $ 
 */

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

public class GridLayoutFeedbackFigure extends RectangleFigure {

	public GridLayoutFeedbackFigure() {
		super();
		setLineStyle(SWT.LINE_SOLID);
		setLineWidth(2);
	}
	public void fillShape(Graphics g) {
		Rectangle r = getBounds().getCopy();
		r.expand(-4, -4);
		g.setBackgroundColor(ColorConstants.lightGray);
		try {
			g.setAlpha(175);
		} catch (Exception e) {
			// For OS platforms that don't support setAlpha
			g.setXORMode(true);
			g.setBackgroundColor(ColorConstants.darkGray);
		}
		g.fillRectangle(r.x, r.y, r.width, r.height);
	}
	public void outlineShape(Graphics g) {
		Rectangle r = getBounds().getCopy();
		r.expand(-4, -4);
		g.drawRectangle(r.x, r.y, r.width, r.height);
	}
}
