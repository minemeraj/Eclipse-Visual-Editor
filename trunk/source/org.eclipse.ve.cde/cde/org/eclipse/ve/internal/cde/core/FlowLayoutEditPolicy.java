package org.eclipse.ve.internal.cde.core;
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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
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
	
protected EditPolicy createChildEditPolicy(EditPart child) {
	return new NonResizableEditPolicy();
}
	
/**
 * Create with the container policy for handling children.
 */
public FlowLayoutEditPolicy(ContainerPolicy containerPolicy) {
	this.containerPolicy = containerPolicy;
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
	return  containerPolicy.getAddCommand(Collections.singletonList(childEP.getModel()), beforeEP != null ? beforeEP.getModel() : null);
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
	return containerPolicy.getCreateCommand(req.getNewObject(), before != null ? before.getModel() : null);
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