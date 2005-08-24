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
 *  $RCSfile: FlowLayoutEditPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.Collections;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Layout input policiy for hanlding the Flow layout of a Container.
 */
public class FlowLayoutEditPolicy extends org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy {
	protected FlowLayoutPolicyHelper helper = new FlowLayoutPolicyHelper();

	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public FlowLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
		setHorizontal(Boolean.TRUE);
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
}
