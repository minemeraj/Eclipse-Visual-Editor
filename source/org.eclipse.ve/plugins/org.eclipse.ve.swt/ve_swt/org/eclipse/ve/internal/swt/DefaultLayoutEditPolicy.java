/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
import java.util.Collections;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/*
 * $RCSfile: DefaultLayoutEditPolicy.java,v $ $Revision: 1.4 $ $Date: 2005-10-11 21:23:47 $
 */
/**
 * Default layout edit policy for SWT layouts. Allows insertion between components and standard
 * horizontal target feedback when creating or moving components.
 */
public class DefaultLayoutEditPolicy extends FlowLayoutEditPolicy {
	protected LayoutPolicyHelper helper = new UnknownLayoutPolicyHelper();
	/**
	 * Create with the container policy for handling DiagramFigures.
	 */
	public DefaultLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		super(containerPolicy);
		helper.setContainerPolicy(containerPolicy);
	}
	/**
	 * The child editpart is about to be added to the parent.
	 */
	public Command createAddCommand(EditPart childEditPart, EditPart before) {
		// We need to create a constraint and send this and the child over to the container policy.
		Object child = childEditPart.getModel();
		return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(null), before != null ? before
				.getModel() : null).getCommand();
	}
	protected Command getCreateCommand(CreateRequest aRequest) {
		Object child = aRequest.getNewObject();
		EditPart before = getInsertionReference(aRequest);
		return helper.getCreateChildCommand(child, null, before != null ? before.getModel() : null).getCommand();
	}
	/**
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return true;
	}
}
