package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: GridBagLayoutFeedbackFigure.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-26 16:35:58 $ 
 */

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

public class GridBagLayoutFeedbackFigure extends RectangleFigure {

	public GridBagLayoutFeedbackFigure() {
		super();
		setLineStyle(SWT.LINE_SOLID);
		setLineWidth(2);
	}
	public void fillShape(Graphics g) {
		Rectangle r = getBounds().getCopy();
		r.expand(-2, -2);
		g.setBackgroundColor(ColorConstants.darkGray);
		g.setXORMode(true);
		g.fillRectangle(r.x, r.y, r.width, r.height);
	}
	public void outlineShape(Graphics g) {
		Rectangle r = getBounds().getCopy();
		r.expand(-2, -2);
		g.drawRectangle(r.x, r.y, r.width, r.height);
	}
}