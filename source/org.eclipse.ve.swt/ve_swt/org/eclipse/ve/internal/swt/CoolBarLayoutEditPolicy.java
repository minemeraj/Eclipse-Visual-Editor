package org.eclipse.ve.internal.swt;

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
 *  $RCSfile: CoolBarLayoutEditPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2004-08-20 22:39:14 $ 
 */

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;

/**
 * @author ekonstantinov
 * 
 * Layout edit policy for CoolBarGraphicalEditPart We need a FlowLayout so we can insert & add items or actions with the flow layout feedback.
 */
public class CoolBarLayoutEditPolicy extends FlowLayoutEditPolicy {

	/**
	 * Constructor for CoolBarLayoutEditPolicy. Use CoolBarContainerPolicy which allows Components to be added.
	 * 
	 * @param containerPolicy
	 */
	public CoolBarLayoutEditPolicy(CoolBarGraphicalEditPart editpart) {
		super(new CoolBarContainerPolicy(EditDomain.getEditDomain(editpart)));
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

	/**
	 * getMoveChildCommand method comment. We don't perform move/resize since we don't know how.
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return null;
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
	 * A new child is being created in this container.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		Command createContributionCmd = containerPolicy.getCommand(request);
		if (createContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return createContributionCmd;
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

	public void deactivate() {
		super.deactivate();
		containerPolicy.setContainer(null);
	}

	public EditPolicy createChildEditPolicy(EditPart aChild) {
		return new NonResizableEditPolicy();
	}

	public void activate() {
		super.activate();
		containerPolicy.setContainer(getHost().getModel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return true;
	}
}