/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: XYLayoutUtility.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:26 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.geometry.Rectangle;
 

/**
 * Helper for XYLayout constraint control.
 * 
 * @since 1.1.0
 */
public class XYLayoutUtility {
	
	public static final int PREFERRED_SIZE = -1;	// This is hard-code in GEF XYLayout and XYLayoutEditPolicy for default. If changed then we need to change here.
	public static final int PREFERRED_LOC = Integer.MIN_VALUE;
	
	/**
	 * Modify the rectangle and mark the sections desired as preferred.
	 * @param rect rectangle to modify
	 * @param preferredLoc make location preferred
	 * @param preferredWidth make width preferred
	 * @param preferredHeight make height preferred
	 * @return the rectangle modified. (It is the same rectangle instance).
	 * @since 1.1.0
	 */
	public static Rectangle modifyPreferredRectangle(Rectangle rect, boolean preferredLoc, boolean preferredWidth, boolean preferredHeight) {
		if (preferredLoc)
			rect.setLocation(PREFERRED_LOC, PREFERRED_LOC);
		if (preferredWidth)
			rect.width = PREFERRED_SIZE;
		if (preferredHeight)
			rect.height = PREFERRED_SIZE;
		return rect;
	}	

	/**
	 * Using the input bounds create a rectangle and mark the sections desired as preferred. The non-preferred sections are copied as is.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param preferredLoc make location preferred
	 * @param preferredWidth make width preferred
	 * @param preferredHeight make height preferred
	 * @return the rectangle modified. (It is the same rectangle instance).
	 * @since 1.1.0
	 */
	public static Rectangle modifyPreferredRectangle(int x, int y, int width, int height, boolean preferredLoc, boolean preferredWidth, boolean preferredHeight) {
		if (preferredLoc)
			x = y = PREFERRED_LOC;
		if (preferredWidth)
			width = PREFERRED_SIZE;
		if (preferredHeight)
			height = PREFERRED_SIZE;
		return new Rectangle(x, y, width, height);
	}	
	
	/**
	 * Modify the CDM Model rectangle and mark the sections desired as preferred.
	 * @param rect rectangle to modify
	 * @param preferredLoc make location preferred
	 * @param preferredWidth make width preferred
	 * @param preferredHeight make height preferred
	 * @return the rectangle modified. (It is the same rectangle instance).
	 * @since 1.1.0
	 */
	public static org.eclipse.ve.internal.cdm.model.Rectangle modifyPreferredCDMRectangle(org.eclipse.ve.internal.cdm.model.Rectangle rect, boolean preferredLoc, boolean preferredWidth, boolean preferredHeight) {
		if (preferredLoc) 
			rect.x = rect.y = PREFERRED_LOC;
		if (preferredWidth)
			rect.width = PREFERRED_SIZE;
		if (preferredHeight)
			rect.height = PREFERRED_SIZE;
		return rect;
	}

	/**
	 * Query to see if the constraint contains any preferred settings (i.e. size or location should
	 * be set to a preferred/default). It will return false if all settings in the constraint are set.
	 * 
	 * @param constraint
	 * @param moved <code>true</code> if should look at the loc portion of the constraint
	 * @param resized <code>true</code> if should look at the size portion of the constraint.
	 * @return <code>true</code> if any of the constraint settings are asking for preferred according to the moved/resized flags.
	 * 
	 * @since 1.1.0
	 */
	public static boolean constraintContainsPreferredSettings(Rectangle constraint, boolean moved, boolean resized) {
		return (resized && (constraint.width == PREFERRED_SIZE || constraint.height == PREFERRED_SIZE)) || (moved && constraint.x == PREFERRED_LOC && constraint.y == PREFERRED_LOC);
	}
	
	/**
	 * Query to see if the constraint contains any preferred settings (i.e. size or location should
	 * be set to a preferred/default). It will return false if all settings in the constraint are set.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param moved <code>true</code> if should look at the loc portion of the constraint
	 * @param resized <code>true</code> if should look at the size portion of the constraint.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static boolean constraintContainsPreferredSettings(int x, int y, int width, int height, boolean moved, boolean resized) {
		return (resized && (width == PREFERRED_SIZE || height == PREFERRED_SIZE)) || (moved && x == PREFERRED_LOC && y == PREFERRED_LOC);
	}
}
