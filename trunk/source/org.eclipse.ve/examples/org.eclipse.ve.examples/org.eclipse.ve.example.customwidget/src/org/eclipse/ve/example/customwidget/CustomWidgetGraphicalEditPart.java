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

import org.eclipse.ve.internal.cde.core.ImageFigure;

import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;


public class CustomWidgetGraphicalEditPart extends ControlGraphicalEditPart {

	public CustomWidgetGraphicalEditPart(Object model) {
		super(model);
	}

	protected IFigure createFigure() {

		ImageFigure figure = (ImageFigure) super.createFigure();

		Figure childFigure = new Figure() {

			public void paint(Graphics graphics) {
				super.paintFigure(graphics);
				graphics.drawImage(CustomwidgetPlugin.getSmileyFace(), getLocation().x, getLocation().y);
			}
		};

		figure.add(childFigure);

		return figure;
	}

}