/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Feb 28, 2005 by Gili Mendel
 * 
 */
package org.eclipse.ve.example.customwidget;

import org.eclipse.draw2d.*;
import org.eclipse.gef.GraphicalEditPart;

import org.eclipse.ve.internal.cde.core.ImageFigure;

import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;


public class CustomWidgetGraphicalEditPart extends ControlGraphicalEditPart {

	public CustomWidgetGraphicalEditPart(Object model) {
		super(model);
	}

	protected IFigure createFigure() {

		ImageFigure figure = (ImageFigure) super.createFigure();
		Label customFigure = new Label("VE Rules",CustomwidgetPlugin.getCustomImage());
		customFigure.setForegroundColor(ColorConstants.red);
		customFigure.setTextPlacement(PositionConstants.SOUTH);
		figure.add(customFigure);
//		customFigure.setSize(customFigure.getPreferredSize());		

		return figure;
	}

}