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
 *  $RCSfile: TableColumnGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-08 15:03:04 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.ve.internal.cde.core.OutlineBorder;
 

/**
 * org.eclipse.swt.widgets.TableColumn does not inherit from org.eclipse.swt.widgets.Control
 * The edit part should create a figure that has the height of the parent Table
 * the width of getWidth() from the TableColumn
 * and the x based on the width of all the preceeding columns
 * @since 1.0.0
 */
public class TableColumnGraphicalEditPart extends AbstractGraphicalEditPart {

	protected IFigure createFigure() {
		Figure figure = new Figure();
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.cyan);
		figure.setBorder(new OutlineBorder());
		figure.setSize(30,30);
		Rectangle parentBounds = ((GraphicalEditPart)getParent()).getFigure().getBounds();
		figure.setLocation(new Point(parentBounds.x,parentBounds.y));
		figure.setSize(parentBounds.width/2,parentBounds.height);
		return figure;
	}
	
	
	public void refresh(){
		super.refresh();
		Rectangle parentBounds = ((GraphicalEditPart)getParent()).getFigure().getBounds();
		getFigure().setLocation(new Point(parentBounds.x,parentBounds.y));
		getFigure().setSize(parentBounds.width/2,parentBounds.height);
	}

	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}
}
