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
 *  $RCSfile: BorderLayoutRegionFeedback.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
/**
 */
public class BorderLayoutRegionFeedback extends RectangleFigure {
	protected String fLabel;
public BorderLayoutRegionFeedback() {
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
