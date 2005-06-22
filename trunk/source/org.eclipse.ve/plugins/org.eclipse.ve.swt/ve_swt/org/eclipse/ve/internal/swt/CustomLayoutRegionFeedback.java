package org.eclipse.ve.internal.swt;
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
 *  $RCSfile: CustomLayoutRegionFeedback.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-22 16:22:09 $ 
 */

import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * Provides shaded rectangles that will show the available regions of the CBanner.
 */
public class CustomLayoutRegionFeedback extends RectangleFigure {
	protected String fLabel;
public CustomLayoutRegionFeedback() {
	super();
	setLineStyle(SWT.LINE_SOLID);
}
public void fillShape(Graphics g) {
	Rectangle r = getBounds().getCopy();
	r.expand(-2,-2);
	g.fillRectangle(r.x, r.y, r.width, r.height);
	g.drawString(getLabel(), r.x + 10, r.y + 10);
}
public void outlineShape(Graphics g) {
	Rectangle r = getBounds().getCopy();
	r.expand(-2,-2);
	g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
}
public void setLabel(String aString){
	fLabel = aString;
}
public String getLabel(){
	return fLabel;
}
}
