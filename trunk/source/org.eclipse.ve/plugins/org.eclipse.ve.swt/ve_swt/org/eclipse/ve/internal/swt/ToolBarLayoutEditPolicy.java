/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ToolBarLayoutEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2004-08-22 22:42:51 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;

/**
 * 
 * @since 1.0.0
 */
public class ToolBarLayoutEditPolicy extends FlowLayoutEditPolicy {

	public ToolBarLayoutEditPolicy(ToolBarGraphicalEditPart editpart) {
		super(new ToolBarContainerPolicy(EditDomain.getEditDomain(editpart)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return true;
	}

	public EditPolicy createChildEditPolicy(EditPart aChild) {
		return new NonResizableEditPolicy();
	}

}