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
 *  $RCSfile: UnknownLayoutInputPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.*;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * The layout input policy for java.awt.Containers for 
 * layout managers that we don't know how to handle.
 * It allows add/delete, but add will not have a constraint,
 * and change constraint will be disabled.
 * This is because if we don't know how to handle the layout,
 * we don't know how to handle the constraint.
 *
 * This is true no matter whether it is LayoutManager or LayoutManager2.
 */
public class UnknownLayoutInputPolicy extends LayoutEditPolicy {
	protected VisualContainerPolicy containerPolicy;
	
	protected UnknownLayoutPolicyHelper fLayoutPolicyHelper;	
	
public UnknownLayoutInputPolicy(VisualContainerPolicy aPolicy) {
	containerPolicy = aPolicy;
	fLayoutPolicyHelper = new UnknownLayoutPolicyHelper(aPolicy);
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
 */
protected Command getAddCommand(Request request) {
	ChangeBoundsRequest cbReq = (ChangeBoundsRequest) request;	
	List children = ContainerPolicy.getChildren(cbReq);
	List constraints = Collections.nCopies(children.size(), null);
	return fLayoutPolicyHelper.getAddChildrenCommand(children, constraints, null);
}

/**
 * A new child is being created in this container.
 */
protected Command getCreateCommand(CreateRequest request) {
	// Unknown means no constraint and add at end.
	return fLayoutPolicyHelper.getCreateChildCommand(request.getNewObject(), null, null);
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
	return fLayoutPolicyHelper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) request));
}

}
