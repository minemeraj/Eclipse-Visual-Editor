/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: NullLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:34:48 $ 
 */
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.ve.internal.cde.core.XYLayoutEditPolicy;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.awt.*;
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
	return helper.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(nullconst), null);
}

protected Command getCreateCommand(CreateRequest aRequest) {
	Object child = aRequest.getNewObject();
	Object constraint = translateToModelConstraint(getConstraintFor(aRequest));			
	
	NullLayoutPolicyHelper.NullConstraint nullconst = new NullLayoutPolicyHelper.NullConstraint((Rectangle) constraint, true, true);
	
	return helper.getCreateChildCommand(child, nullconst, null);
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
	return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest));
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

/**
 * A constraint can be used for a bean that indicates that we want to have the size
 * generated from the preferred size.  An example of this is when a bean is dropped on a null
 * layout without a drag rectangle being generated.  In this case the rectangle has an artificial
 * width and height of -1 set because the preferred size is not known at the time of 
 * the command was generated for the drop command request.
 * It is only known once the live java component bean is given its parent and it has access 
 * to information such as its font which it inherits from its parent.
 *
 * We must also handle the case where a user wants to reset the preferred size just for the 
 * width OR just the height.
 *
 * Finally we must handle a constraint whose position contains just a placeholder (Integer.MIN_VALUE
 * for it's x and y values).
 *
 * It will set the bounds directly in the argument bounds .
 * It will return true if it made any changes.
 */
public static boolean adjustForPreferredSizeAndPosition(IBeanProxy childProxy, Rectangle bounds, int marginWidth, int marginHeight) {
	boolean changedit = false;
	// If either the width or height are -1 we need to make them the current preferred size of the live component
	// This would be the state after a drop on the graph viewer with just a point and no drag rectangle,
	// or else a drop onto a tree
	if (bounds.width == -1 || bounds.height == -1) {
		changedit = true;
		IDimensionBeanProxy preferredSize = BeanAwtUtilities.invoke_getPreferredSize(childProxy);
		if (bounds.width == -1)
			bounds.width = Math.max(preferredSize.getWidth(), 10);
		if (bounds.height == -1)
			bounds.height = Math.max(preferredSize.getHeight(), 10);
	}	
	// See if the positions requires calculating.  This would be following a drop of a component onto the tree
	// where the constraint generated is unpositioned and can only be positioned after the live component
	// has been created
	if (bounds.x == Integer.MIN_VALUE && bounds.y == Integer.MIN_VALUE) {
		changedit = true;
		IBeanProxy containerBeanProxy = BeanAwtUtilities.invoke_getParent(childProxy);
		Point position = findNextAvailablePosition(bounds , containerBeanProxy , marginWidth, marginHeight);
		bounds.x = position.x;
		bounds.y = position.y;
	}
	return changedit;
}

public static boolean adjustForPreferredLocation(IBeanProxy childProxy, Point location, int marginWidth, int marginHeight) {
	boolean changedit = false;
	
	// See if the positions requires calculating.  This would be following a drop of a component onto the tree
	// where the constraint generated is unpositioned and can only be positioned after the live component
	// has been created
	if (location.x == Integer.MIN_VALUE && location.y == Integer.MIN_VALUE) {
		changedit = true;
		IBeanProxy containerBeanProxy = BeanAwtUtilities.invoke_getParent(childProxy);
		// Try to figure out where to place this guy.  We need to know his size.
		IDimensionBeanProxy size = BeanAwtUtilities.invoke_getSize(childProxy) ;		
		if (size == null) return false ;
		
		Rectangle bounds = new Rectangle(location.x,location.y,size.getWidth(),size.getHeight()) ;
		Point position = findNextAvailablePosition(bounds , containerBeanProxy , marginWidth, marginHeight);
		location.x = position.x;
		location.y = position.y;
	}
	return changedit;	
}

/**
 * If the width or height is -1 them set them to be the current preferred size.
 * It will set the size directly in the argument size.
 * It will return true if it made any changes.
 */
public static boolean adjustForPreferredSize(IBeanProxy childProxy, Dimension size) {
	// If either the width or height are -1 we need to make them the current preferred size of the live component
	// This would be the state after a drop on the graph viewer with just a point and no drag rectangle,
	// or else a drop onto a tree
	boolean changedit = false;
	if (size.width == -1 || size.height == -1) {
		changedit = true;
		IDimensionBeanProxy preferredSize = BeanAwtUtilities.invoke_getPreferredSize(childProxy);
		if (size.width == -1)
			size.width = Math.max(preferredSize.getWidth(), 10);
		if (size.height == -1)
			size.height = Math.max(preferredSize.getHeight(), 10);
	}
	return changedit;
}

/**
 * Try to find the next available white space for a child within a container's bounding rectangle
 * but outside of the other children. If necessary, go beyound the bounding rectangle.
 * Children whose sizes are 0 or positions are Integer.MIN_VALUE are not to be considered for 
 * calculations since it has not yet been adjusted for size or position.
 */
protected static Point findNextAvailablePosition(Rectangle bounds, IBeanProxy containerBeanProxy, int marginWidth, int marginHeight) {
	int requestedWidth = bounds.width + 2 * marginWidth; // Increase by double margin width (one on each side)
	int requestedHeight = bounds.height + 2 * marginHeight;
	IRectangleBeanProxy boundingRect = BeanAwtUtilities.invoke_getBounds(containerBeanProxy);
	// Gather the bounds for all the children in the container
	IArrayBeanProxy children = BeanAwtUtilities.invoke_getComponents(containerBeanProxy);
	// Collect all rectangles that have non zero widths and height
	ArrayList rects = new ArrayList(children.getLength());
	for (int i = 0; i < children.getLength(); i++) {
		try {
			IRectangleBeanProxy cBounds = BeanAwtUtilities.invoke_getBounds(children.get(i));
			// Exclude the children being added which are unadjusted 
			if (cBounds.getWidth() != -1 && cBounds.getHeight() != -1){
				// Convert the IRectangleBeanProxy to a 2d rectangle that we can work with
				Rectangle lBounds = new Rectangle(cBounds.getX(),cBounds.getY(),cBounds.getWidth(),cBounds.getHeight());
				rects.add(lBounds);
			}
		} catch ( ThrowableProxy exc ) {
			exc.printProxyStackTrace();
		}
	}
	if (requestedWidth < boundingRect.getWidth()) {
		// The requested size is less than the bounding rectangle, see where on the right
		// it can fit, moving down as needed.

		int highestBottom = 0; // How high up to do search. Start at the the top.
		// Perform search by width, then by height. Start at upper left and see if any intersections.
		// If there aren't any, good, we found a spot. If there are, move right beyond all of the
		// intersections and try again. If we get all of the way over, then move back to the highest
		// bottom we found among all of the intersections and to the far left and try again, moving
		// down the page until we find something.
		while (true) {
			int x = 0;
			int y = highestBottom;
			highestBottom = Integer.MAX_VALUE;
			while (x + requestedWidth < boundingRect.getWidth()) {
				boolean intersects = false;
				Rectangle tryRect = new Rectangle(x, y, requestedWidth, requestedHeight);
				for (int i = 0; i < rects.size(); i++) {
					Rectangle rect = (Rectangle) rects.get(i);
					if (rect.intersects(tryRect)) {
						intersects = true;
						x = Math.max(x, rect.x + rect.width);
						highestBottom = Math.min(highestBottom, rect.y + rect.height);
					}
				}
				if (!intersects) {
					// We got a space for it.
					// Move right and down by margin amount.
					Point position = new Point(tryRect.x + marginWidth, tryRect.y + marginHeight);
					return position;
				}
				// There was an intersection, x is now just right of the rightmost intersection.
				// Loop will try searching here now.
			}
			// We gone all of the way to the right and couldn't find anything. Now move down
			// to just below the height intersect bottom. Loop will try searching here.
		}
	} else {
		// The requested width is larger than the bounding area, so go down and find first
		// y that we can use that doesn't intersect anything.
		int y = 0;
		while (true) {
			boolean intersects = false;
			Rectangle tryRect = new Rectangle(0, y, requestedWidth, requestedHeight);
			for (int i = 0; i < rects.size(); i++) {
				Rectangle rect = (Rectangle) rects.get(i);
				if (rect.intersects(tryRect)) {
					intersects = true;
					y = Math.max(y, rect.y + rect.height);
				}
				if (!intersects) {
					// We got a space for it.
					// Move right and down by margin amount.
					Point position = new Point(tryRect.x + marginWidth, tryRect.y + marginHeight);
					return position;
				}
				// It intersects something in this row, move below the lowest of the intersections
				// Loop will try again.
			}
		}
	}
}
}
