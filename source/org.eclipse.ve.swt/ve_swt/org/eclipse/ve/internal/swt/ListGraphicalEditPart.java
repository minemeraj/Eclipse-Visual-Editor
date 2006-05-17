/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ListGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2006-05-17 20:15:53 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;
 

/**
 * Graphical editpart for a SWT List.
 * 
 * @since 1.2.0
 */
public class ListGraphicalEditPart extends ControlGraphicalEditPart {

	public ListGraphicalEditPart(Object model) {
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
