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
 *  $RCSfile: ScrollPaneGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.gef.EditPolicy;

/**
 * Graphical edit part for handling java.awt.ScrollPane in the Graph viewer.
 * Note: ScrollPane can only have one child.
 */
public class ScrollPaneGraphicalEditPart extends ContainerGraphicalEditPart {

	public ScrollPaneGraphicalEditPart(Object model) {
		super(model);
	}

	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy(new ScrollPaneContainerPolicy(EditDomain.getEditDomain(this))));
	}
}