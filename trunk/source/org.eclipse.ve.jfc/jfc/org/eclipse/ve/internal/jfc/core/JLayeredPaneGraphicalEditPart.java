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
 *  $RCSfile: JLayeredPaneGraphicalEditPart.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:09 $ 
 */
 
import java.util.List;

import org.eclipse.ve.internal.cde.core.ImageFigure;
import org.eclipse.gef.EditPart;
 
/**
 * GraphicalEditPart for JLayeredPane.
 * 
 * @author richkulp
 */
public class JLayeredPaneGraphicalEditPart extends ContainerGraphicalEditPart {

	/**
	 * Constructor for JLayeredPaneGraphicalEditPart.
	 * @param model
	 */
	public JLayeredPaneGraphicalEditPart(Object model) {
		super(model);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(Object)
	 */
	protected EditPart createChild(Object model) {
		// All of the children of a JLayeredGraphicalEditPart need 
		// to have this component's ImageFigure made the parent image figure so
		// that they will draw in the correct order and any overlaying bounds will be clipped
		// correctly.
		EditPart ep = super.createChild(model);
		((ImageFigure) ((ComponentGraphicalEditPart) ep).getContentPane()).setUseParentImageFigure(true);
		return ep;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		// Bit of a kludge, but we need to get the order that the JLayeredPane has them. They may
		// be a different order because it all depends on the layer/order they were added, or in
		// the case of JDesktopPane, which one was selected. Thus, the hit test order could be
		// different than the order in the MOF model.
		return ((JLayeredPaneProxyAdapter) getComponentProxy()).getCurrentOrder();
	}

}
