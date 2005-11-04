/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.draw2d.geometry.Point;
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
	protected ContainerPolicy containerPolicy;		// Handles the containment functions
	protected NullLayoutPolicyHelper helper;	
	protected Point originOffset;

/**
 * Create with the container policy for handling DiagramFigures.
 */
public NullLayoutEditPolicy(VisualContainerPolicy containerPolicy, Point originOffset) {
	this.containerPolicy = containerPolicy;
	helper = new NullLayoutPolicyHelper(containerPolicy);	
	this.originOffset = originOffset;
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
	IJavaObjectInstance constraint = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, SWTConstants.SF_CONTROL_BOUNDS));
	if (constraint != null) {
		IBeanProxy rect = BeanProxyUtilities.getBeanProxy(constraint);
		return new Rectangle(getX(rect), getY(rect), getWidth(rect), getHeight(rect));
	}
	
	// So, size/location should be set. Use those instead.
	Rectangle rect = new Rectangle();
	IJavaObjectInstance loc = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, SWTConstants.SF_CONTROL_LOCATION));
	if (loc != null) {
		IBeanProxy point = BeanProxyUtilities.getBeanProxy(loc);
		rect.setLocation(getX(point), getY(point));
	}
	IJavaObjectInstance size = (IJavaObjectInstance) childObject.eGet(JavaInstantiation.getSFeature(childObject, SWTConstants.SF_CONTROL_SIZE));
	if (size != null) {
		IBeanProxy sz = BeanProxyUtilities.getBeanProxy(size);
		rect.setSize(getX(sz), getY(sz));
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
 * The figure constraint is relative to the bounds of the composite, however the model constraint is relative to the origin of the
 * client area.  Therefore we need to subtract from the corner the origin of the client area 
 */
protected Object translateToModelConstraint(Object aFigureConstraint) {
	
	Rectangle figureConstraint = (Rectangle)aFigureConstraint;
	Rectangle result = new Rectangle(
		figureConstraint.x - originOffset.x,
		figureConstraint.y - originOffset.y,
		figureConstraint.width,
		figureConstraint.height);
	return result;
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
private int getX(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("x"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}
private int getY(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("y"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}
private int getWidth(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("width"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}
private int getHeight(IBeanProxy anSWTRectangleBeanProxy){
	IFieldProxy xFieldProxy = anSWTRectangleBeanProxy.getTypeProxy().getFieldProxy("height"); //$NON-NLS-1$
	try{
		IIntegerBeanProxy xProxy = (IIntegerBeanProxy) getGetFieldMethodProxy(anSWTRectangleBeanProxy.getProxyFactoryRegistry()).invoke(xFieldProxy,anSWTRectangleBeanProxy);
		return xProxy.intValue();			
	} catch (ThrowableProxy exc) {
		exc.printProxyStackTrace();
		return 0;
	}
}	
private IBeanTypeProxy environmentBeanTypeProxy;
private IMethodProxy getFieldMethodProxy; 
protected final IBeanTypeProxy getEnvironmentBeanTypeProxy(ProxyFactoryRegistry aProxyFactoryRegistry){
	if(environmentBeanTypeProxy == null){	
		environmentBeanTypeProxy = aProxyFactoryRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.Environment"); //$NON-NLS-1$		
	}
	return environmentBeanTypeProxy;
}
protected final IMethodProxy getGetFieldMethodProxy(ProxyFactoryRegistry aProxyFactoryRegistry){
	if(getFieldMethodProxy == null){	
		getFieldMethodProxy = getEnvironmentBeanTypeProxy(aProxyFactoryRegistry).getMethodProxy("java.lang.reflect.field","java.lang.Object");		 //$NON-NLS-1$ //$NON-NLS-2$
	}
	return getFieldMethodProxy;
}

public static boolean adjustForPreferredSizeAndPosition(IBeanProxy childProxy, IJavaObjectInstance parentComposite, Rectangle bounds, int marginWidth, int marginHeight, IBeanProxy defaultX, IBeanProxy defaultY) {
	boolean changedit = false;
	// If either the width or height are -1 we need to make them the current preferred size of the live component
	// This would be the state after a drop on the graph viewer with just a point and no drag rectangle,
	// or else a drop onto a tree
	if (bounds.width == -1 || bounds.height == -1) {
		changedit = true;
		IPointBeanProxy preferredSize = BeanSWTUtilities.invoke_computeSize(childProxy,defaultX,defaultY);
		if (bounds.width == -1)
			bounds.width = Math.max(preferredSize.getX(), marginWidth);
		if (bounds.height == -1)
			bounds.height = Math.max(preferredSize.getY(), marginHeight);
	}	
	if (bounds.x == Integer.MIN_VALUE && bounds.y == Integer.MIN_VALUE) {
		changedit = true;
		//IPointBeanProxy point = BeanSWTUtilities.invoke_getLocation(childProxy);
		
		//IBeanProxy containerBeanProxy = BeanAwtUtilities.invoke_getParent(childProxy);
		Point position = findNextAvailablePosition(bounds , parentComposite , marginWidth, marginHeight);
		bounds.x = position.x;
		bounds.y = position.y;
	}
	return changedit;
}
protected static Point findNextAvailablePosition(Rectangle bounds, IJavaObjectInstance parentComposite, int marginWidth, int marginHeight) {
	int requestedWidth = bounds.width + 2 * marginWidth; // Increase by double margin width (one on each side)
	int requestedHeight = bounds.height + 2 * marginHeight;
	IBeanProxy compositeBeanProxy = BeanProxyUtilities.getBeanProxy(parentComposite);
	IRectangleBeanProxy boundingRect = BeanSWTUtilities.invoke_getBounds(compositeBeanProxy);
	// Gather the bounds for all the children in the container
	
	IArrayBeanProxy children = BeanSWTUtilities.invoke_getChildren(compositeBeanProxy);
	// Collect all rectangles that have non zero widths and height
	ArrayList rects = new ArrayList(children.getLength());
	for (int i = 0; i < children.getLength(); i++) {
		try {
			IRectangleBeanProxy cBounds = BeanSWTUtilities.invoke_getBounds(children.get(i));
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
