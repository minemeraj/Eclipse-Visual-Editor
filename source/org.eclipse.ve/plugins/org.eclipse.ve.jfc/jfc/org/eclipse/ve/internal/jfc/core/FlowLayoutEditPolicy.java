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
 *  $RCSfile: FlowLayoutEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.util.Collections;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Layout input policiy for hanlding the Flow layout of a Container.
 */
public class FlowLayoutEditPolicy extends org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy {
	protected FlowLayoutPolicyHelper helper = new FlowLayoutPolicyHelper();

	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public FlowLayoutEditPolicy(ContainerPolicy containerPolicy) {
		super(containerPolicy);
		helper.setContainerPolicy(containerPolicy);
	}
	/**
	 * The child editpart is about to be added to the parent.
	 */
	public Command createAddCommand(EditPart childEditPart, EditPart before) {

		// We need to create a constraint and send this and the child over to the container policy.
		Object child = childEditPart.getModel();
		return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(null), before != null ? before.getModel() : null);
	}
	protected Command getCreateCommand(CreateRequest aRequest) {
		Object child = aRequest.getNewObject();
		EditPart before = getInsertionReference(aRequest);
		return helper.getCreateChildCommand(child, null, before != null ? before.getModel() : null);
	}
	/**
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 * Override because the super (GEF) looks to the host figure's layout manager which is an XYLayout usually
	 * which throws a ClassCastException
	 */
	protected boolean isHorizontal() {
		return true;
	}
}
