/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.core.BeanUtilities;
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
	
	
}
