package org.eclipse.ve.internal.jfc.core;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.util.Collections;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.GridBagConstraint;

public class GridBagLayoutEditPolicy extends ConstrainedLayoutEditPolicy {
	public static final String copyright = "(c) Copyright IBM Corporation 2002."; //$NON-NLS-1$
	protected GridBagLayoutFeedBack fGridBagLayoutFeedback = null;
	protected GridBagLayoutPolicyHelper helper = new GridBagLayoutPolicyHelper();
	protected ContainerPolicy containerPolicy;		// Handles the containment functions

public GridBagLayoutEditPolicy(ContainerPolicy containerPolicy) {
	this.containerPolicy = containerPolicy;
	helper.setContainerPolicy(containerPolicy);
}
public void activate() {
	super.activate();
	containerPolicy.setContainer(getHost().getModel());
}
public void deactivate() {
	super.deactivate();
	containerPolicy.setContainer(null);
}
protected EditPolicy createChildEditPolicy(EditPart child) {
	return new NonResizableEditPolicy();
}
protected Command getCreateCommand(CreateRequest request) {
	Object child = request.getNewObject();
	
// Eventually we may get a constraint dependent upon drop position, but for now we just do a default.
	GridBagConstraint gridbagconstraint = helper.getDefaultConstraint((IJavaObjectInstance) child);
	
	return helper.getCreateChildCommand(child, gridbagconstraint, null);
}
protected Command getDeleteDependantCommand(Request aRequest) {
	Command deleteContributionCmd = containerPolicy.getCommand(aRequest);
	if ( deleteContributionCmd == null )
		return UnexecutableCommand.INSTANCE;	// It can't be deleted

	// Note: If there is any annotation, that will be deleted too by the
	// container policy, and that will then also delete all of the view info.
	// So we don't need to handle viewinfo here.
		
	return deleteContributionCmd;		
}
protected Command getMoveChildrenCommand(Request p1){
	return null;
}
/**
 * getOrphanChildCommand: About to remove a child from the model
 * so that it can be added someplace else.
 *
 * Remove the constraints since it may not be appropriate in 
 * the new position. We need to use the Helper for this.
 */
protected Command getOrphanChildrenCommand(Request aRequest) {
	return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest));
}
/**
 * The child editpart is about to be added to the parent.
 * The child is an existing child that was orphaned from a previous parent.
 */
protected Command createAddCommand(EditPart childEditPart, Object constraint) {
	// We need to create a constraint and send this and the child over to the container policy.
	Object child = childEditPart.getModel();
// Eventually we may get a constraint dependent upon drop position, but for now we just do a default.
	GridBagConstraint gridbagconst = helper.getDefaultConstraint((IJavaObjectInstance)child);
	return helper.getAddChildrenCommand(Collections.nCopies(1, child), Collections.nCopies(1, gridbagconst), null);
}
protected Command createChangeConstraintCommand(EditPart p1, Object p2){
	return null;
}
protected Object getConstraintFor(Point p1){
	return null;
}
protected Object getConstraintFor(Rectangle p1){
	return null;
}
private IFigure getGridBagLayoutFeedback() {
	if (fGridBagLayoutFeedback == null) {
		GridBagLayoutFeedBack gf = new GridBagLayoutFeedBack();
		gf.setLayoutOrigin(helper.getContainerLayoutOrigin());
		gf.setLayoutDimensions(helper.getContainerLayoutDimensions());
		IFigure f = ((GraphicalEditPart) getHost()).getFigure();
		Rectangle r = f.getBounds().getCopy(); // Don't work with the original, use a copy
		gf.setBounds(r);
		addFeedback(gf);
		fGridBagLayoutFeedback = gf;
	}
	return fGridBagLayoutFeedback;
}
protected void eraseLayoutTargetFeedback(Request request) {
	if (fGridBagLayoutFeedback != null) {
		removeFeedback(fGridBagLayoutFeedback);
		fGridBagLayoutFeedback = null;
	}
}
protected void showLayoutTargetFeedback(Request request) {
	getGridBagLayoutFeedback();
}
}