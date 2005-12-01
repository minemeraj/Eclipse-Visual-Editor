/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LayoutPolicyHelper.java,v $
 *  $Revision: 1.13 $  $Date: 2005-12-01 20:19:43 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.ContainerPolicy.Result;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * 
 * @since 1.0.0
 */
public abstract class LayoutPolicyHelper implements ILayoutPolicyHelper {
	
	protected VisualContainerPolicy policy;
	
public LayoutPolicyHelper(VisualContainerPolicy ep){
	setContainerPolicy(ep);
}
public LayoutPolicyHelper(){
}

/**
 * This will map the rectangle (in figure coordinates relative to the composite parent) into
 * model coordinates. This will take origin offset and right-to-left style into consideration.
 * 
 * @param composite the parent composite proxy adapter.
 * @param parentFigure the parent figure
 * @param rect the rectangle in figure coordinates relative to the parent figure. It will be set with the composite model coordinates on return.
 * @return the same rect that is passed in but modified as needed. This is a convienence for usage. It is not a new rect.
 * @since 1.2.0
 */
public static Rectangle mapFigureToModel(CompositeProxyAdapter composite, IFigure parentFigure, Rectangle rect) {
	/**
	 * Convert layout constraint (container child positioning data)
	 * to a model constraint.
	 * First we need to take into account if right-to-left. If it is, then we need to flip it correctly.
	 * 
	 * The figure constraint is relative to the bounds of the composite, however the model constraint is relative to the origin of the
	 * client area.  Therefore we need to then subtract from the corner the origin of the client area.
	 * 
	 * But we first need to take into account if the control is right-to-left. If it is, we need to convert the figure coordinates
	 * to control coordinates by first flipping the x depending on the size.
	 *  
	 */	
	if (composite.isRightToLeft()) {
		// Need to flip to account for right to left orientation.
		// This is done by subtracting the figure upper-left from parent upper-left.
		// Since in rtl the Y direction isn't flipped, we don't need to change the y.
		// Since the figureConstraint is relative to the parent, we don't need the parent's x/y, just the width.
		Rectangle parentBounds = parentFigure.getBounds();
		rect.x = parentBounds.width-(rect.x+rect.width);
	}
	
	// Now adjust for the origin offset in the control's system of coor.
	Point originOffset = composite.getOriginOffset();
	rect.x -= originOffset.x;
	rect.y -= originOffset.y;
	return rect;
}

/**
 * This will map the point (in figure coordinates relative to the composite parent) into
 * model coordinates. This will take origin offset and right-to-left style into consideration.
 * <p>
 * If it is really a rect that needs to be mapped, use {@link #mapFigureToModel(CompositeProxyAdapter, IFigure, Rectangle)} instead.
 * In a Right to Left situation, taking the x/y and converting it and then just use the original width/height will not give a correct
 * rectangle in the model coordinates. The mapping that takes a rectangle will take this into account correctly.
 * 
 * @param composite the parent composite proxy adapter.
 * @param parentFigure the parent figure
 * @param point the point in figure coordinates relative to the parent figure. It will be set with the composite model coordinates on return.
 * @return the same point that is passed in but modified as needed. This is a convienence for usage. It is not a new point.
 * @since 1.2.0
 */
public static Point mapFigureToModel(CompositeProxyAdapter composite, IFigure parentFigure, Point point) {

	/**
	 * Convert layout constraint (container child positioning data)
	 * to a model constraint.
	 * First we need to take into account if right-to-left. If it is, then we need to flip it correctly.
	 * 
	 * The figure constraint is relative to the bounds of the composite, however the model constraint is relative to the origin of the
	 * client area.  Therefore we need to then subtract from the corner the origin of the client area.
	 * 
	 * But we first need to take into account if the control is right-to-left. If it is, we need to convert the figure coordinates
	 * to control coordinates by first flipping the x depending on the size.
	 *  
	 */	
	if (composite.isRightToLeft()) {
		// Need to flip to account for right to left orientation.
		// This is done by subtracting the figure upper-left from parent upper-left.
		// Since in rtl the Y direction isn't flipped, we don't need to change the y.
		// Since the figureConstraint is relative to the parent, we don't need the parent's x/y, just the width.
		Rectangle parentBounds = parentFigure.getBounds();
		point.x = parentBounds.width-point.x;
	}
	
	// Now adjust for the origin offset in the control's system of coor.
	Point originOffset = composite.getOriginOffset();
	point.x -= originOffset.x;
	point.y -= originOffset.y;
	return point;
}


/**
 * This will map the rectangle (in model coordinates) into
 * figure coordinates relative to the parent figure. This will take origin offset and right-to-left style into consideration.
 * 
 * @param composite the parent composite proxy adapter.
 * @param parentFigure the parent figure
 * @param rect the rectangle in model coordinates. It will be set with the figure coordinates relative to the parent figure on return.
 * @return the same rect that is passed in but modified as needed. This is a convienence for usage. It is not a new rect.
 * @since 1.2.0
 */
public static Rectangle mapModelToFigure(CompositeProxyAdapter composite, IFigure parentFigure, Rectangle rect) {
	// Adjust for the origin offset in the control's system of coor.
	Point originOffset = composite.getOriginOffset();
	rect.x += originOffset.x;
	rect.y += originOffset.y;

	if (composite.isRightToLeft()) {
		// Need to flip to account for right to left orientation.
		// This is done by subtracting the figure upper-left from parent upper-left.
		// Since in rtl the Y direction isn't flipped, we don't need to change the y.
		// Since the figureConstraint is relative to the parent, we don't need the parent's x/y, just the width.
		Rectangle parentBounds = parentFigure.getBounds();
		rect.x = parentBounds.width-(rect.x+rect.width);
	}
	
	return rect;
}

/**
 * This will map the point (in model coor) into
 * figure coordinates relative to the parent figure. This will take origin offset and right-to-left style into consideration.
 * <p>
 * If it is really a rect that needs to be mapped, use {@link #mapModelToFigure(CompositeProxyAdapter, IFigure, Rectangle)} instead.
 * In a Right to Left situation, taking the x/y and converting it and then just use the original width/height will not give a correct
 * rectangle in the model coordinates. The mapping that takes a rectangle will take this into account correctly.
 * 
 * @param composite the parent composite proxy adapter.
 * @param parentFigure the parent figure
 * @param point the point in model coors. It will be set with the figure coordinates relative to the parent figure on return.
 * @return the same point that is passed in but modified as needed. This is a convienence for usage. It is not a new point.
 * @since 1.2.0
 */
public static Point mapModelToFigure(CompositeProxyAdapter composite, IFigure parentFigure, Point point) {
	
	// Adjust for the origin offset in the control's system of coor.
	Point originOffset = composite.getOriginOffset();
	point.x += originOffset.x;
	point.y += originOffset.y;

	if (composite.isRightToLeft()) {
		// Need to flip to account for right to left orientation.
		// This is done by subtracting the figure upper-left from parent upper-left.
		// Since in rtl the Y direction isn't flipped, we don't need to change the y.
		// Since the figureConstraint is relative to the parent, we don't need the parent's x/y, just the width.
		Rectangle parentBounds = parentFigure.getBounds();
		point.x = parentBounds.width-point.x;
	}
	
	return point;
}

protected IJavaObjectInstance getContainer() {
	return (IJavaObjectInstance) policy.getContainer();
}
		
public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {
	return policy.getCreateCommand(constraint, childComponent, position);
}

public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
	return policy.getAddCommand(constraints, childrenComponents, position);
}

public Result getOrphanChildrenCommand(List children) {
	
	// Now get the orphan command for the children.
	Result orphanContributionCmd = policy.getOrphanChildrenCommand(children);

	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cb.append(orphanContributionCmd.getCommand());	
	cancelConstraints(cb, children);
	orphanContributionCmd.setCommand(cb.getCommand());
	return orphanContributionCmd;
}

/**
 * Called to cancel any non-layoutdata constraints. Remove any specific contraints, properties, etc. (e.g. null layout to remove bounds, size, location).
 * Subclasses should not remove the "layoutData" constraint itself. That is already taken care of.
 * 
 * @param commandBuilder
 * @param children
 * 
 * @since 1.2.0
 */
protected abstract void cancelConstraints(CommandBuilder commandBuilder, List children);

/*
 *  (non-Javadoc)
 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getOrphanConstraintsCommand(java.util.List)
 * 
 * Forward to subclasses to remove any specific contraints, properties, etc. (e.g. null layout to remove bounds, size, location).
 * Then remove the layout data of each child since it's not applicable to what layout we've switched to.
 */
public Command getOrphanConstraintsCommand(List children) {
	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cancelConstraints(cb, children);
	// We need to cancel the layout data for each of the controls
	IJavaObjectInstance parent = (IJavaObjectInstance) policy.getContainer();
	EStructuralFeature sf_layoutData = JavaInstantiation.getSFeature(parent, SWTConstants.SF_CONTROL_LAYOUTDATA);
	cb.cancelGroupAttributeSetting(children, sf_layoutData);
	return cb.getCommand();
}


public void setContainerPolicy(VisualContainerPolicy policy) {
	this.policy = policy;
}		

/**
 * Apply the corresponding constraint to each child.
 * The child is the Control itself
 */
public Command getChangeConstraintCommand(List children, List constraints) {
	return NoOpCommand.INSTANCE;
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
 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getMoveChildrenCommand(java.util.List, java.lang.Object)
 */
public Command getMoveChildrenCommand(List children, Object beforeChild) {
	return policy.getMoveChildrenCommand(children, beforeChild);
}

}
