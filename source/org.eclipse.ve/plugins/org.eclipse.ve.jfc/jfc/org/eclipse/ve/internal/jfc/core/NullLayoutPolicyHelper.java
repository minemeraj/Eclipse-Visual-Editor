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
 *  $RCSfile: NullLayoutPolicyHelper.java,v $
 *  $Revision: 1.11 $  $Date: 2005-12-01 20:19:40 $ 
 */

import java.util.*;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.XYLayoutUtility;
import org.eclipse.ve.internal.cde.core.ContainerPolicy.Result;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
/**
 * Null layout policy helper.
 *
 * Note: Constraints should be of the type NullConstraint declared
 * in here. It will be converted properly later.
 * Creation date: (11/10/00 11:55:27 AM)
 * @author: Peter Walker
 */
public class NullLayoutPolicyHelper implements ILayoutPolicyHelper, IActionFilter {
	
	protected VisualContainerPolicy policy;
	
/**
 * Constraint object to be passed into this class.
 * rect: the constraint rectangle
 * moved: True if the location portion of the rect should be applied
 * resized: True if the size portion of the rect should be applied.
 */
public static class NullConstraint {
	public Rectangle rect;
	public boolean moved, resized;

	public NullConstraint(Rectangle rect, boolean moved, boolean resized) {
		this.rect = rect;
		this.moved = moved;
		this.resized = resized;
	}
}

public NullLayoutPolicyHelper(VisualContainerPolicy ep) {
	setContainerPolicy(ep);
}

public NullLayoutPolicyHelper() {
}

public void setContainerPolicy(VisualContainerPolicy policy) {
	this.policy = policy;
}

public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {
	EClass constraintComponentClass = (EClass) ((EObject) policy.getContainer()).eResource().getResourceSet().getEObject(JFCConstants.CLASS_CONTAINER_CONSTRAINTCOMPONENT, true);
	EFactory visualFact = JFCConstants.getFactory(constraintComponentClass);
	
	EObject constraintComponent = visualFact.create(constraintComponentClass);
	constraintComponent.eSet(JavaInstantiation.getSFeature((IJavaObjectInstance) policy.getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT),  null);	// Put the null into the constraint component so that nothing is added in the add
	
	VisualContainerPolicy.CorelatedResult result = policy.getCreateCommand(constraintComponent, childComponent, position);
	CommandBuilder cbld = new CommandBuilder();
	if (!result.getChildren().isEmpty()) {
		cbld.append(createChangeConstraintCommand((IJavaObjectInstance) result.getChildren().get(0), (NullConstraint) constraint));
	}
	cbld.append(result.getCommand());
	result.setCommand(cbld.getCommand());
	return result;
}

private static class NullConstraintWrapper extends VisualContainerPolicy.ConstraintWrapper {

	public final NullConstraint nullConstraint;

	public NullConstraintWrapper(Object constraintComponent, NullConstraint nullConstraint) {
		super(constraintComponent);
		this.nullConstraint = nullConstraint;
	}
	
}
public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
	EClass constraintComponentClass = (EClass) ((EObject) policy.getContainer()).eResource().getResourceSet().getEObject(JFCConstants.CLASS_CONTAINER_CONSTRAINTCOMPONENT, true);
	EFactory visualFact = JFCConstants.getFactory(constraintComponentClass);
	ArrayList componentConstraints = new ArrayList(childrenComponents.size());
	for (int i=0; i<childrenComponents.size(); i++) {
		EObject constraintComponent = visualFact.create(constraintComponentClass);
		constraintComponent.eSet(JavaInstantiation.getSFeature((IJavaObjectInstance) policy.getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT),  null);	// Put the null into the constraint component so that nothing is added in the add
		componentConstraints.add(new NullConstraintWrapper(constraintComponent, (NullConstraint) constraints.get(i)));
	}
	
	VisualContainerPolicy.CorelatedResult result = policy.getAddCommand(componentConstraints, childrenComponents, position);		
	CompoundCommand command = new CompoundCommand();
	command.append(getChangeConstraintCommand(result.getChildren(), result.getCorelatedList()));
	command.append(result.getCommand());
	result.setCommand(command.unwrap());
	return result;
}

public Result getOrphanChildrenCommand(List children) {
	
	// Now get the orphan command for the children.
	Result orphanContribution = policy.getOrphanChildrenCommand(children);
	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cb.append(orphanContribution.getCommand());	
	cancelConstraints(cb, children);
	orphanContribution.setCommand(cb.getCommand());
	return orphanContribution;

}

protected void cancelConstraints(CommandBuilder cb, List children) {
	IJavaObjectInstance parent = (IJavaObjectInstance) policy.getContainer();
	EStructuralFeature
		sfComponentLocation = JavaInstantiation.getSFeature(parent, JFCConstants.SF_COMPONENT_LOCATION),
		sfComponentSize = JavaInstantiation.getSFeature(parent, JFCConstants.SF_COMPONENT_SIZE),
		sfComponentBounds = JavaInstantiation.getSFeature(parent, JFCConstants.SF_COMPONENT_BOUNDS);	

	Iterator childrenItr = children.iterator();
	while(childrenItr.hasNext()) {
		EObject child = (EObject) childrenItr.next();
		if (child.eIsSet(sfComponentBounds))
			cb.cancelAttributeSetting(child, sfComponentBounds);
		if (child.eIsSet(sfComponentSize))
			cb.cancelAttributeSetting(child, sfComponentSize);
		if (child.eIsSet(sfComponentLocation))
			cb.cancelAttributeSetting(child, sfComponentLocation);

	}
}
/**
 * Determine what the default constraint(s) are for this layout manager
 * and assign a constraint to each child.
 * Return a List with a constraint for each child.
 */
public List getDefaultConstraint(List children) {

	// We are going to return a array of constraints that is marked as everything is preferred, location, width, and height.
	// It will turn those settings into the calculated preferred settings at execution time.
	return Collections.nCopies(children.size(), new NullConstraint(XYLayoutUtility.modifyPreferredRectangle(new Rectangle(), true, true, true), true, true));
}

public Command getChangeConstraintCommand(List children, List constraints) {
	Iterator childItr = children.iterator();
	Iterator conItr = constraints.iterator();
	CompoundCommand cmd = new CompoundCommand();
	while (childItr.hasNext()) {
		Object constraint = conItr.next();
		// It may of been passed in from getAdd which would of had constraint wrapper instead.
		if (constraint instanceof NullConstraintWrapper)
			constraint = ((NullConstraintWrapper) constraint).nullConstraint;
		cmd.append(createChangeConstraintCommand((IJavaObjectInstance) childItr.next(), (NullConstraint) constraint));
	}
	
	return !cmd.isEmpty() ? cmd.unwrap() : null;
}

protected Command createChangeConstraintCommand(IJavaObjectInstance child, NullConstraint constraint) {
	ApplyNullLayoutConstraintCommand cmd = new ApplyNullLayoutConstraintCommand();
	cmd.setTarget(child);
	cmd.setDomain(policy.getEditDomain());
	if (constraint.moved || constraint.resized)
		cmd.setConstraint(constraint.rect, constraint.moved, constraint.resized);
	else
		return null;
	return cmd;
}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ILayoutPolicyHelper#getOrphanConstraintsCommand(List)
	 */
	public Command getOrphanConstraintsCommand(List children) {
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		cancelConstraints(cb, children);
		return cb.getCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getDeleteDependentCommand(java.util.List)
	 */
	public Command getDeleteDependentCommand(List children) {
		return policy.getDeleteDependentCommand(children).getCommand();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getDeleteDependentCommand(java.lang.Object)
	 */
	public Command getDeleteDependentCommand(Object child) {
		return policy.getDeleteDependentCommand(child).getCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 * Enable the Show/Hide Grid action on the Beans viewer depending on the layout EditPolicy
	 * on the graphical viewer side.
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (target instanceof EditPart) {
			EditDomain ed = EditDomain.getEditDomain((EditPart) target);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(((EditPart) target).getModel());
				if (ep != null && ep.getEditPolicy(EditPolicy.LAYOUT_ROLE) instanceof IActionFilter) {
					return ((IActionFilter) ep.getEditPolicy(EditPolicy.LAYOUT_ROLE)).testAttribute(target, name, value);
				}
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getMoveChildrenCommand(java.util.List, java.lang.Object)
	 */
	public Command getMoveChildrenCommand(List children, Object beforeChild) {
		return policy.getMoveChildrenCommand(children, beforeChild);
	}
}
