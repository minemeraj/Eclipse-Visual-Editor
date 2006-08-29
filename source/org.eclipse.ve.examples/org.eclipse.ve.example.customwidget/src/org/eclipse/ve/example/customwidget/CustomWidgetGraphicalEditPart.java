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
package org.eclipse.ve.example.customwidget;

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;

import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;


public class CustomWidgetGraphicalEditPart extends ControlGraphicalEditPart {

	public CustomWidgetGraphicalEditPart(Object model) {
		super(model);
	}
	
	public void performRequest(Request request) {
		if(request.getType() == RequestConstants.REQ_DIRECT_EDIT){
			// do nothing.  Bug introduced because the "text" property has a custom editor, and the BeanDirectEditCellEditorLocator assumes that "text" always has an SWT Text editor
		} else {
			super.performRequest(request);
		}
	}
	

	protected IFigure createFigure() {

		IFigure figure = super.createFigure();
		Label customFigure = new Label("VE Rules",CustomwidgetPlugin.getCustomImage());
		customFigure.setForegroundColor(ColorConstants.red);
		customFigure.setTextPlacement(PositionConstants.SOUTH);
		// ImageFigure has no layout, so we will have to explicitly set the size().
		// To get a prefered size (before we look up the hierarchy), Label will need a Font
		customFigure.setFont(((GraphicalEditPart)getParent()).getFigure().getFont());
		customFigure.setSize(customFigure.getPreferredSize());

		figure.add(customFigure);

		return figure;
	}

}
