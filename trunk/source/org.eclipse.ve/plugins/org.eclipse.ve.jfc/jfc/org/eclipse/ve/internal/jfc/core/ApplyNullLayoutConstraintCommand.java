/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: ApplyNullLayoutConstraintCommand.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.emf.common.util.URI;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.*;
 
/**
 * Command to apply the Null Layout constraint(s) for AWT components
 */
public class ApplyNullLayoutConstraintCommand extends NullLayoutConstraintCommand {
	
	public ApplyNullLayoutConstraintCommand(String label) {
		super(label);
	}

	public ApplyNullLayoutConstraintCommand() {
	}
	/**
	 * Return the location for java.awt.Dimension
	 */
	protected IJavaInstance createLocationInstance(int x, int y) {
		return BeanUtilities.createJavaObject(
			JFCConstants.POINT_CLASS_NAME,
			rset,
			PointJavaClassCellEditor.getJavaInitializationString(x, y, JFCConstants.POINT_CLASS_NAME));
	}	
	/**
	 * Return the size for java.awt.Dimension
	 */
	protected IJavaInstance createSizeInstance(int width, int height) {
		return BeanUtilities.createJavaObject(
			JFCConstants.DIMENSION_CLASS_NAME,
			rset,
			DimensionJavaClassCellEditor.getJavaInitializationString(width, height, JFCConstants.DIMENSION_CLASS_NAME));						
	}
	/**
	 * Return the bounds for java.awt.Rectangle
	 */
	protected IJavaInstance createBoundsInstance(int x, int y, int width, int height) {
		return BeanUtilities.createJavaObject(
			JFCConstants.RECTANGLE_CLASS_NAME,
			rset,
			RectangleJavaClassCellEditor.getJavaInitializationString(x, y, width, height,JFCConstants.RECTANGLE_CLASS_NAME));		
	}	
	/**
	 * Return the structural feature URI for AWT component bounds
	 */
	protected URI getSFBounds() {
		return JFCConstants.SF_COMPONENT_BOUNDS;
	}	
	/**
	 * Return the structural feature URI for AWT component size
	 */	
	protected URI getSFSize() {
		return JFCConstants.SF_COMPONENT_SIZE;		
	}
	/**
	 * Return the structural feature URI for AWT component location
	 */	
	protected URI getSFLocation() {
		return JFCConstants.SF_COMPONENT_LOCATION;		
	}	
	

}
