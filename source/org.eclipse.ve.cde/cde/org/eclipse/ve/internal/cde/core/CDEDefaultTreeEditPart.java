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
 *  $RCSfile: CDEDefaultTreeEditPart.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-15 20:19:34 $ 
 */

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
/**
 * A simple tree editpart for a non-EMF object. It is simply
 * an icon (somepart.gif) with a label which is the EMF ID of the
 * object. This can be used as a default for any object that don't
 * know what to do with.
 */
public class CDEDefaultTreeEditPart extends AbstractTreeEditPart {

	public CDEDefaultTreeEditPart(Object model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new TreePrimaryDragRoleEditPolicy());
	}


	/**
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
	 */
	protected Image getImage() {
		return CDEDefaultGraphicalEditPart.DEFAULT_IMAGE;
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
	 */
	protected String getText() {
		return getModel() != null ? getModel().toString() : ""+null; //$NON-NLS-1$
	}

}
