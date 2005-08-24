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
 *  $RCSfile: ScrollPaneGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
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
