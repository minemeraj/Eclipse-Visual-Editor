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
package org.eclipse.ve.internal.cde.core;

/*
 *  $RCSfile: CDELayoutEditPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * The layout input policy for forwarding over to the container policy. This means position has nothing to do with mouse position.
 * <p>
 * The feedback for dropping a child (create/add) will be an outline just outside the figure from the host. If something else is desired, this will
 * need to be subclassed and provide the alternate feedback (show and erase target feedback).
 * 
 * Creation date: (06/26/00 12:44:00 PM)
 * 
 * @author: Administrator
 */
public class CDELayoutEditPolicy extends LayoutEditPolicy {

	protected ContainerPolicy containerPolicy;

	/**
	 * Construct the CDELayoutEditPolicy
	 * @param aPolicy
	 * 
	 * @since 1.1.0
	 */
	public CDELayoutEditPolicy(ContainerPolicy aPolicy) {
		containerPolicy = aPolicy;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		containerPolicy.setContainer(getHost().getModel());
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		containerPolicy.setContainer(null);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	public EditPolicy createChildEditPolicy(EditPart aChild) {
		return new NonResizableEditPolicy();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getAddCommand(org.eclipse.gef.Request)
	 */
	protected Command getAddCommand(Request request) {
		Command addContributionCmd = containerPolicy.getCommand(request);
		if (addContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be added.
		return addContributionCmd;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		Command createContributionCmd = containerPolicy.getCommand(request);
		if (createContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return createContributionCmd;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		Command deleteContributionCmd = containerPolicy.getCommand(request);
		if (deleteContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return deleteContributionCmd;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(org.eclipse.gef.Request)
	 */
	protected Command getMoveChildrenCommand(Request request) {
		// We don't perform move/resize since we don't know how.
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getOrphanChildrenCommand(org.eclipse.gef.Request)
	 */
	protected Command getOrphanChildrenCommand(Request request) {
		// To orphan, we remove only the child. We don't delete the child since the part is not going away, just going
		// somewhere else.
		Command orphanContributionCmd = containerPolicy.getCommand(request);
		if (orphanContributionCmd == null)
			return UnexecutableCommand.INSTANCE; // It can't be created.
		return orphanContributionCmd;
	}

	private Figure targetFeedback; // The target feedback.

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#eraseLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void eraseLayoutTargetFeedback(Request request) {
		super.eraseLayoutTargetFeedback(request);
		if (targetFeedback != null) {
			removeFeedback(targetFeedback);
			targetFeedback = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#showLayoutTargetFeedback(org.eclipse.gef.Request)
	 */
	protected void showLayoutTargetFeedback(Request request) {
		if (REQ_ADD.equals(request.getType()) || REQ_CREATE.equals(request.getType())) {
			// provide feedback on the overall target figure
			if (targetFeedback == null) {
				RectangleFigure rf;
				targetFeedback = rf = new RectangleFigure();
				rf.setFill(false);

				Rectangle rect = new Rectangle(getHostFigure().getBounds());
				rf.setBounds(rect.expand(5, 5));
				addFeedback(targetFeedback);
			}
		}
	}

}

