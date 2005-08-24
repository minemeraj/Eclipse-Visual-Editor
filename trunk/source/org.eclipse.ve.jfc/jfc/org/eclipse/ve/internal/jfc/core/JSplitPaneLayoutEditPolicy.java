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
 *  $RCSfile: JSplitPaneLayoutEditPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;


public class JSplitPaneLayoutEditPolicy extends LayoutEditPolicy {
	protected JSplitPaneContainerPolicy containerPolicy;

public JSplitPaneLayoutEditPolicy(EditDomain anEditDomain) {
	containerPolicy = new JSplitPaneContainerPolicy(anEditDomain);
}

public void activate() {
	super.activate();
	containerPolicy.setContainer(getHost().getModel());
}
public void deactivate() {
	super.deactivate();
	containerPolicy.setContainer(null);
}
public EditPolicy createChildEditPolicy(EditPart aChild){
	return new NonResizableEditPolicy();
}
/**
 * Add of children requested.
 * 
 * See the comments in the getCreateCommand(Request request) method.
 */
protected Command getAddCommand(Request request) {
	Command addContributionCmd = null;
	if (RequestConstants.REQ_ADD.equals(request.getType())) {
		if (containerPolicy.isRightComponentOccupiedAndOnlyComponent())
			addContributionCmd = containerPolicy.getAddCommand(ContainerPolicy.getChildren((GroupRequest) request), getHost().getChildren().get(0));
		else
			addContributionCmd = containerPolicy.getAddCommand(ContainerPolicy.getChildren((GroupRequest) request), null);
	}
	if (addContributionCmd == null)
		return UnexecutableCommand.INSTANCE;	// It can't be added.
	return addContributionCmd;
}

/**
 * A new child is being created in this container.
 * 
 * We need to handle the situation in which the JSplitPane has a right/bottom component
 * occupied and the left/top component is unoccupied.
 * Since the SplitPaneContainerPolicy thinks that a null for the positionBeforeChild object
 * means to put it after or at the end, in the graph viewer it will put the new component in the
 * right/bottom and move the original right/bottom to the left/top position. This is rather
 * annoying and since we know we are in the graph viewer when we get the request, we need
 * to check if this is the case and if so, send the request to the container policy with
 * the right/bottom component as the positionBeforeChild parameter to force it to leave the
 * right/bottom component as is and put the new component in the left/top position.
 */
protected Command getCreateCommand(CreateRequest request) {
	Command createContributionCmd = null;
	if (RequestConstants.REQ_CREATE.equals(request.getType())) {
		if (containerPolicy.isRightComponentOccupiedAndOnlyComponent())
			createContributionCmd = containerPolicy.getCreateCommand(request.getNewObject(), getHost().getChildren().get(0));
		else 
			createContributionCmd = containerPolicy.getCreateCommand(request.getNewObject(), null);
	}
	if (createContributionCmd == null)
		return UnexecutableCommand.INSTANCE;	// It can't be created.
	return createContributionCmd;
}

/**
 * getDeleteDependantCommand method comment.
 */
protected Command getDeleteDependantCommand(Request request) {
	Command deleteContributionCmd = containerPolicy.getCommand(request);
	if (deleteContributionCmd == null)
		return UnexecutableCommand.INSTANCE;	// It can't be created.
	return deleteContributionCmd;
}
/**
 * getMoveChildCommand method comment.
 * We don't perform move/resize since we don't know how.
 */
protected Command getMoveChildrenCommand(Request request) {
	return null;
}
/**
 * getOrphanChildCommand. To orphan, we delete only the child. We don't
 * delete the subpart since the part is not going away, just somewhere's
 * else. So the subpart stays.
 */
protected Command getOrphanChildrenCommand(Request request) {
	Command orphanContributionCmd = containerPolicy.getCommand(request);
	if (orphanContributionCmd == null)
		return UnexecutableCommand.INSTANCE;	// It can't be created.
	return orphanContributionCmd;
}
}
