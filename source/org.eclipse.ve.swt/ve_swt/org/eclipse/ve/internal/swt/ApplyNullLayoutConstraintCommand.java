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
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.util.URI;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.XYLayoutUtility;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.*;

public class ApplyNullLayoutConstraintCommand extends NullLayoutConstraintCommand {

	public ApplyNullLayoutConstraintCommand(String label) {
		super(label);
	}
	public ApplyNullLayoutConstraintCommand() {
	}
	/**
	 * Return the location for org.eclipse.swt.graphics.Point
	 */
	protected IJavaInstance createLocationInstance(int x, int y) {
		return BeanUtilities.createJavaObject(
			SWTConstants.POINT_CLASS_NAME, 
			rset,
			PointJavaClassCellEditor.getJavaInitializationString(x, y,SWTConstants.POINT_CLASS_NAME));
	}	
	/**
	 * Return the size for SWT.  This is an org.eclipse.swt.graphics.Point
	 */
	protected IJavaInstance createSizeInstance(int width, int height) {
		return BeanUtilities.createJavaObject(
			SWTConstants.POINT_CLASS_NAME, 
			rset,
			PointJavaClassCellEditor.getJavaInitializationString(width, height,SWTConstants.POINT_CLASS_NAME));						
	}
	/**
	 * Return the bounds for org.eclipse.swt.graphics.Rectangle
	 */
	protected IJavaInstance createBoundsInstance(int x, int y, int width, int height) {
		return BeanUtilities.createJavaObject(
			SWTConstants.RECTANGLE_CLASS_NAME, 
			rset,
			RectangleJavaClassCellEditor.getJavaInitializationString(x, y, width, height,SWTConstants.RECTANGLE_CLASS_NAME));		
	}	
	/**
	 * Return the structural feature URI for SWT control bounds
	 */
	protected URI getSFBounds() {
		return SWTConstants.SF_CONTROL_BOUNDS;
	}	
	/**
	 * Return the structural feature URI for SWT control size
	 */	
	protected URI getSFSize() {
		return SWTConstants.SF_CONTROL_SIZE;		
	}
	/**
	 * Return the structural feature URI for SWT control location
	 */	
	protected URI getSFLocation() {
		return SWTConstants.SF_CONTROL_LOCATION;		
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.NullLayoutConstraintCommand#getPreferredLoc()
	 */
	protected Point getPreferredLoc() {
		IBeanProxy childProxy = BeanProxyUtilities.getBeanProxy(target, true);
		if (childProxy == null)
			return new Point(XYLayoutUtility.PREFERRED_LOC, XYLayoutUtility.PREFERRED_LOC);
		
		// Try to figure out where to place this guy.  We need to know his size.
		IRectangleBeanProxy boundsProxy = BeanSWTUtilities.invoke_getBounds(childProxy) ;		
		if (boundsProxy == null) return new Point(XYLayoutUtility.PREFERRED_LOC, XYLayoutUtility.PREFERRED_LOC) ;
		
		Rectangle bounds = new Rectangle(XYLayoutUtility.PREFERRED_LOC, XYLayoutUtility.PREFERRED_LOC, boundsProxy.getWidth(), boundsProxy.getHeight()) ;
		return findNextAvailablePosition(bounds , BeanSWTUtilities.invoke_getParent(childProxy) , 5, 5);
	}
	
	/**
	 * Try to find the next available position.
	 * @param bounds
	 * @param compositeBeanProxy
	 * @param marginWidth
	 * @param marginHeight
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected static Point findNextAvailablePosition(Rectangle bounds, IBeanProxy compositeBeanProxy, int marginWidth, int marginHeight) {
		int requestedWidth = bounds.width + 2 * marginWidth; // Increase by double margin width (one on each side)
		int requestedHeight = bounds.height + 2 * marginHeight;
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
				JavaVEPlugin.log(exc, Level.WARNING);
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
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.NullLayoutConstraintCommand#getPreferredSize(int, int)
	 */
	protected Dimension getPreferredSize(int width, int height) {
		IBeanProxy childProxy = BeanProxyUtilities.getBeanProxy(target, true);
		if (childProxy != null) {
			IPointBeanProxy preferredSize = BeanSWTUtilities.invoke_computeSize(childProxy, childProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(width), childProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(height));
			if (width == -1)
				width = Math.max(preferredSize.getX(), 10);
			if (height == -1)
				height = Math.max(preferredSize.getY(), 10);
		}
		return new Dimension(width, height);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.NullLayoutConstraintCommand#getPreferredBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	protected Rectangle getPreferredBounds(Rectangle constraint) {
		Rectangle newConstraint = new Rectangle(constraint);
		IBeanProxy childProxy = BeanProxyUtilities.getBeanProxy(target, true);
		if (childProxy != null) {
			// If either the width or height are -1 we need to make them the current preferred size of the live component
			// This would be the state after a drop on the graph viewer with just a point and no drag rectangle,
			// or else a drop onto a tree
			if (newConstraint.width == XYLayoutUtility.PREFERRED_SIZE || newConstraint.height == XYLayoutUtility.PREFERRED_SIZE) {
				IPointBeanProxy preferredSize = BeanSWTUtilities.invoke_computeSize(childProxy, childProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(newConstraint.width), childProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(newConstraint.height));
				if (newConstraint.width == XYLayoutUtility.PREFERRED_SIZE)
					newConstraint.width = Math.max(preferredSize.getX(), 10);
				if (newConstraint.height == XYLayoutUtility.PREFERRED_SIZE)
					newConstraint.height = Math.max(preferredSize.getY(), 10);
			}	
			// See if the positions requires calculating.  This would be following a drop of a component onto the tree
			// where the constraint generated is unpositioned and can only be positioned after the live component
			// has been created
			if (newConstraint.x == XYLayoutUtility.PREFERRED_LOC && newConstraint.y == XYLayoutUtility.PREFERRED_LOC) {
				IBeanProxy containerBeanProxy = BeanSWTUtilities.invoke_getParent(childProxy);
				Point position = findNextAvailablePosition(newConstraint , containerBeanProxy , 5, 5);
				newConstraint.x = position.x;
				newConstraint.y = position.y;
			}
		}
		return newConstraint;
	}	
	
	
	
}
