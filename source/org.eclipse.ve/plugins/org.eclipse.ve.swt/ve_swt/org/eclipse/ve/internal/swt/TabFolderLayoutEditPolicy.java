/*****************************************************************************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms
 * of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: TabFolderLayoutEditPolicy.java,v $ $Revision: 1.1 $ $Date: 2004-08-19 19:56:20 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;

public class TabFolderLayoutEditPolicy extends LayoutEditPolicy {

	protected TabFolderContainerPolicy containerPolicy;

	public TabFolderLayoutEditPolicy(EditDomain anEditDomain) {
		containerPolicy = new TabFolderContainerPolicy(anEditDomain);
	}

	public void activate() {
		super.activate();
		containerPolicy.setContainer(getHost().getModel());
	}

	public void deactivate() {
		super.deactivate();
		containerPolicy.setContainer(null);
	}

	public EditPolicy createChildEditPolicy(EditPart aChild) {
		return new NonResizableEditPolicy();
	}

	/**
	 * Add of children requested.
	 */
	protected Command getAddCommand(Request request) {
		Command addContributionCmd = containerPolicy.getCommand(request);
		if (addContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be added.
		return addContributionCmd;
	}

	/**
	 * A new child is being created in this container.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		Command createContributionCmd = containerPolicy.getCommand(request);
		if (createContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return createContributionCmd;
	}

	/**
	 * getDeleteDependantCommand method comment.
	 */
	protected Command getDeleteDependantCommand(Request request) {
		Command deleteContributionCmd = containerPolicy.getCommand(request);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return deleteContributionCmd;
	}

	/**
	 * getMoveChildCommand method comment. We don't perform move/resize since we don't know how.
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return null;
	}

	/**
	 * getOrphanChildCommand. To orphan, we delete only the child. We don't delete the subpart since the part is not going away, just somewhere's
	 * else. So the subpart stays.
	 */
	protected Command getOrphanChildrenCommand(Request request) {
		Command orphanContributionCmd = containerPolicy.getCommand(request);
		if (orphanContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return orphanContributionCmd;
	}
}