package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: ApplyNullLayoutConstraintCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2004-01-12 21:44:36 $ 
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