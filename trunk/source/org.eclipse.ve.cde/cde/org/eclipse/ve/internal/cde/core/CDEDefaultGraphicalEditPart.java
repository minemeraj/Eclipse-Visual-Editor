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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CDEDefaultGraphicalEditPart.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
/**
 * A simple graphical editpart for any kind of object. It is simply
 * an icon (somepart.gif) with a label which is the toString of the object.
 * This can be used as a default for any object that don't
 * know what to do with. 
 */

public class CDEDefaultGraphicalEditPart extends AbstractGraphicalEditPart {
	
	protected static final Image DEFAULT_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "images/somepart.gif"); //$NON-NLS-1$

	public CDEDefaultGraphicalEditPart(Object model) {
		setModel(model);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
	}

	protected IFigure createFigure() {
		Label label = new Label("?"); //$NON-NLS-1$
		label.setTextPlacement(PositionConstants.SOUTH);
		return label;
	}

	protected void refreshVisuals() {
		Label fig = (Label) getFigure();
		fig.setIcon(DEFAULT_IMAGE);
		fig.setText(getModel() != null ? getModel().toString() : ""+null); //$NON-NLS-1$
	}
}
