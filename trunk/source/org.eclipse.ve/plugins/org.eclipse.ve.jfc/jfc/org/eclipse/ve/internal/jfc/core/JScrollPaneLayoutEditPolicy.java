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
 *  $RCSfile: JScrollPaneLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */


import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;
/**
 * The layout edit policy for javax.swing.JScrollPane
 */
public class JScrollPaneLayoutEditPolicy extends LayoutEditPolicy {
	protected JScrollPaneContainerPolicy containerPolicy;
	
public JScrollPaneLayoutEditPolicy(EditDomain anEditDomain) {
	containerPolicy = new JScrollPaneContainerPolicy(anEditDomain);
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
	Command addContributionCmd = containerPolicy.getCommand(request);
	if (addContributionCmd == null)
		return UnexecutableCommand.INSTANCE;	// It can't be added.
	return addContributionCmd;
}

/**
 * A new child is being created in this container.
 */
protected Command getCreateCommand(CreateRequest request) {
	Command createContributionCmd = containerPolicy.getCommand(request);
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


