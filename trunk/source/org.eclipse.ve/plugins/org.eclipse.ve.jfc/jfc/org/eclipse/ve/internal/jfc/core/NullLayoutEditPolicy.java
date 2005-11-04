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
 *  $RCSfile: NullLayoutEditPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2005-11-04 17:30:48 $ 
 */
import java.util.Collections;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.core.XYLayoutEditPolicy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * Layout Input policy for when a java.awt.Container's layout manager
 * is null (i.e. not set)
 */
public class NullLayoutEditPolicy extends XYLayoutEditPolicy {
	protected VisualContainerPolicy containerPolicy;		// Handles the containment functions
	protected NullLayoutPolicyHelper helper = new NullLayoutPolicyHelper();
	
/**
 * Create with the container policy for handling DiagramFigures.
 */
public NullLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
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

/**
 * The child editpart is about to be added to the parent.
 * The child is an existing child that was orphaned from a previous parent.
 */
protected Command createAddCommand(EditPart childEditPart, Object constraint) {
	// We need to create a constraint and send this and the child over to the container policy.
	Object child = childEditPart.getModel();
	NullLayoutPolicyHelper.NullConstraint nullconst = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, true, true);	
	return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(nullconst), null).getCommand();
}

protected Command getCreateCommand(CreateRequest aRequest) {
	Object child = aRequest.getNewObject();
	Object constraint = translateToModelConstraint(getConstraintFor(aRequest));			
	
	NullLayoutPolicyHelper.NullConstraint nullconst = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, true, true);
	
	return helper.getCreateChildCommand(child, nullconst, null).getCommand();
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

/**
 * getOrphanChildCommand: About to remove a child from the model
 * so that it can be added someplace else.
 *
 * Remove the constraints since it may not be appropriate in 
 * the new position. We need to use the Helper for this.
 */
protected Command getOrphanChildrenCommand(Request aRequest) {
	return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest)).getCommand();
}

protected Command createChangeConstraintCommand(EditPart childEditPart, Object constraint, boolean moved, boolean resize) {
	return createChangeConstraintCommand(childEditPart.getModel(), constraint, moved, resize);
}

/**
 * Get the child constraint. Even though the model constraint is really a java object,
 * we are treating the model constraint as being a Rectangle in this policy.
 * It not converted to the appropriate object until absolutely needed.
 */
protected Object getChildConstraint(EditPart child) {
	IJavaObjectInstance childObject = (IJavaObjectInstance) child.getModel();
	IJavaObjectInstance constraint = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, JFCConstants.SF_COMPONENT_BOUNDS));
	if (constraint != null) {
		IRectangleBeanProxy rect = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(constraint);
		return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	// So, size/location should be set. Use those instead.
	Rectangle rect = new Rectangle();
	IJavaObjectInstance loc = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, JFCConstants.SF_COMPONENT_LOCATION));
	if (loc != null) {
		IPointBeanProxy point = (IPointBeanProxy) BeanProxyUtilities.getBeanProxy(loc);
		rect.setLocation(point.getX(), point.getY());
	}
	IJavaObjectInstance size = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, JFCConstants.SF_COMPONENT_LOCATION));
	if (size != null) {
		IDimensionBeanProxy sz = (IDimensionBeanProxy) BeanProxyUtilities.getBeanProxy(size);
		rect.setSize(sz.getWidth(), sz.getHeight());
	}
	return rect;	
}

/**
 * primChangeConstraintCommand: Create the command to change the constraint
 * to this new value. For null layout, the constraints are stored in the
 * bounds, or size/location. It will try to be smart about choosing what to 
 * update.
 */
protected Command createChangeConstraintCommand(Object child, Object constraint, boolean moved, boolean resize) {
	NullLayoutPolicyHelper.NullConstraint nullConstraint = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, moved, resize);
	return helper.getChangeConstraintCommand(Collections.singletonList(child), Collections.singletonList(nullConstraint));
}

/**
 * Convert layout constraint (container child positioning data)
 * to a model constraint. We're using Rectangle as the model constraint
 * too here. It will be converted to the correct type when the command
 * itself is executed.
 */
protected Object translateToModelConstraint(Object figureConstraint) {
	return figureConstraint;
}

protected Object modelToFigureConstraint(Object modelConstraint) {
	if (modelConstraint instanceof IJavaInstance) {
		IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) modelConstraint);
		if (proxy instanceof IRectangleBeanProxy) {
			IRectangleBeanProxy rect = (IRectangleBeanProxy) modelConstraint;
			return new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}
	}
	
	// Don't know what it is, so what do we do. Returning null just makes it not participate.
	return null;
}

protected void setConstraintToFigure(EditPart child, Rectangle figureConstraint) {
	// This is only called if zoom changed, but we are doing zoom, so this is never called.
}


protected boolean isChildResizeable(EditPart aChild) {
	return true;
}
}
