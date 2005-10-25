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
/*
 *  $RCSfile: ComboGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-25 19:12:43 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;
 

/**
 * Graphical editpart for a SWT Combo.
 * 
 * @since 1.2.0
 */
public class ComboGraphicalEditPart extends ControlGraphicalEditPart {

	public ComboGraphicalEditPart(Object model) {
		super(model);
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new FlowLayoutEditPolicy(getContainerPolicy()));
	}
	
	protected ContainerPolicy getContainerPolicy() {
		return new SWTWidgetContainerPolicy(getEditDomain());
	}
}
