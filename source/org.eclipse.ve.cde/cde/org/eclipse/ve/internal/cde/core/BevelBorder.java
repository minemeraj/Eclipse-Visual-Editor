package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BevelBorder.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;

public class BevelBorder extends AbstractBorder{

protected int width = 1;
protected Color color1 = ColorConstants.black;
protected Color color2 = ColorConstants.darkGray;
protected Color color3 = ColorConstants.gray;
protected Color color4 = ColorConstants.lightGray;

public Insets getInsets(IFigure figure){
	return new Insets(width);
}
public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics graphics, Insets insets){
	tempRect.setBounds(getPaintRectangle(figure, insets));
	if (width%2 == 1){
		tempRect.width--;
		tempRect.height--;
	}
	tempRect.shrink(width/2,width/2);
	graphics.setLineWidth(width);
	graphics.setForegroundColor(color2);
	// Top and left outer
	graphics.drawLine(tempRect.x,tempRect.y,tempRect.x,tempRect.y+tempRect.height);
	graphics.drawLine(tempRect.x,tempRect.y,tempRect.x+tempRect.width,tempRect.y);
	// Bottom and right outer
	graphics.setForegroundColor(color4);
	graphics.drawLine(tempRect.x+1,tempRect.y+tempRect.width,tempRect.x+tempRect.width,tempRect.y+tempRect.width);
	graphics.drawLine(tempRect.x+tempRect.width,tempRect.y+tempRect.height,tempRect.x+tempRect.width,tempRect.y);
	// Top and left inner
	graphics.setForegroundColor(color1);
	graphics.drawLine(tempRect.x+1,tempRect.y+1,tempRect.x+tempRect.width-2,tempRect.y+1);
	graphics.drawLine(tempRect.x+1,tempRect.y+1,tempRect.x+1,tempRect.y+tempRect.height-2);
	// Botton and right inner
	graphics.setForegroundColor(color3);
	graphics.drawLine(tempRect.x+1,tempRect.y+tempRect.height-1,tempRect.x+tempRect.width-1,tempRect.y+tempRect.height-1);
	graphics.drawLine(tempRect.x+tempRect.width-1,tempRect.y+tempRect.height-1,tempRect.x+tempRect.width-1,tempRect.y+1);
}
}