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
 *  $RCSfile: FlowLayoutEditPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2005-10-11 21:26:01 $ 
 */

import java.util.Collections;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
/**
 * Common Diagram Layout Edit Policy that uses a CDE ContainerEditPolicy to do the
 * container functions. This allows the ContainerEditPolicy to also be used
 * as in the tree also.
 */
public class FlowLayoutEditPolicy extends org.eclipse.gef.editpolicies.FlowLayoutEditPolicy {
	
		
	protected ContainerPolicy containerPolicy;		// Handles the containment functions
	private IFigure targetFeedback;
	private Boolean horizontal;
	
protected EditPolicy createChildEditPolicy(EditPart child) {
	return new NonResizableEditPolicy();
}
	
/**
 * Set this flag to indicate this is horizontal(true)/vertical(false). If it is <code>null</code>
 * it will do the default, which is to query the layout from the Figure. In that
 * case the layout <b>MUST</b> be a {@link org.eclipse.draw2d.FlowLayout} else
 * there will be a class cast exception. By default this value is <code>null</code>
 * @param horizontal <code>Boolean.TRUE</code> for horizontal, <code>Boolean.FALSE</code> for
 * vertical, <code>null</code> for do default of query the figure's layout (in which
 * case the layout MUST be a {@link org.eclipse.draw2d.FlowLayout}.
 * 
 * @since 1.1.0.1
 */
protected void setHorizontal(Boolean horizontal) {
	this.horizontal = horizontal;
}

protected boolean isHorizontal() {
	return horizontal != null ? horizontal.booleanValue() : super.isHorizontal();
}

/**
 * Create with the container policy for handling children.
 */
public FlowLayoutEditPolicy(ContainerPolicy containerPolicy) {
	this.containerPolicy = containerPolicy;
}

public FlowLayoutEditPolicy(ContainerPolicy containerPolicy, Boolean horizontal) {
	this(containerPolicy);
	setHorizontal(horizontal);
}

public void activate() {
	super.activate();
	containerPolicy.setContainer(getHost().getModel());
}

public void deactivate() {
	super.deactivate();
	containerPolicy.setContainer(null);
}

protected Command createAddCommand(EditPart childEP, EditPart beforeEP) {
	return  containerPolicy.getAddCommand(childEP.getModel(), beforeEP != null ? beforeEP.getModel() : null).getCommand();
}

protected Command getDeleteDependantCommand(Request aRequest) {
	Command cmd = containerPolicy.getCommand(aRequest);
	return cmd != null ? cmd : UnexecutableCommand.INSTANCE;
}

protected Command createMoveChildCommand(EditPart childEP, EditPart beforeEP) {
	return  containerPolicy.getMoveChildrenCommand(Collections.singletonList(childEP.getModel()), beforeEP != null ? beforeEP.getModel() : null);
}

protected Command getOrphanChildrenCommand(Request req) {
	// Now get the orphan command for the child.
	return containerPolicy.getCommand(req);
}

protected Command getCreateCommand(CreateRequest req){
	EditPart before = getInsertionReference(req);
	return containerPolicy.getCreateCommand(req.getNewObject(), before != null ? before.getModel() : null).getCommand();
}
/* (non-Javadoc)
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
 * Provide feedback on the overall parent figure.
 */
protected IFigure getLayoutTargetFeedback(Request request) {
	
	// provide feedback on the overall target figure
	if (targetFeedback == null){
		RectangleFigure rf;
		targetFeedback = rf = new RectangleFigure();
		rf.setFill(false);

		Rectangle rect = new Rectangle(getHostFigure().getBounds());
		rf.setBounds(rect.shrink(4,4));
		addFeedback(targetFeedback);
	}
	return targetFeedback;	
}
/* (non-Javadoc)
 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#showLayoutTargetFeedback(org.eclipse.gef.Request)
 * Overridden here because we want to show some kind of feedback even when dropping on an empty container.
 */
protected void showLayoutTargetFeedback(Request request) {
	super.showLayoutTargetFeedback(request);
	getLayoutTargetFeedback(request);
}

}
