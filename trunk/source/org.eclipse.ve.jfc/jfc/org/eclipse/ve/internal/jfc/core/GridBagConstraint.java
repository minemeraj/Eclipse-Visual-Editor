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
 *  $RCSfile: GridBagConstraint.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.draw2d.geometry.Insets;
/**
 * Constraint class that emulates the java.awt.GridBagConstraints classs
 * Any interface changes to Sun's GridBagConstraints class need to be made here as well.
 */
public class GridBagConstraint {
	public static final int 
		// fill values
		NONE = 0,
		BOTH = 1,
		HORIZONTAL = 2,
		VERTICAL = 3,
		// anchor values
		CENTER = 10,
		NORTH = 11,
		NORTHEAST = 12,
		EAST = 13,
		SOUTHEAST = 14,
		SOUTH = 15,
		SOUTHWEST = 16,
		WEST = 17,
		NORTHWEST = 18;
		
	public int gridx, gridy, gridwidth, gridheight, anchor, fill, ipadx, ipady;
	public double weightx, weighty;
	public Insets insets;

public GridBagConstraint() {
    gridx = -1;
    gridy = -1;
    gridwidth = 1;
    gridheight = 1;

    weightx = 0;
    weighty = 0;
    anchor = CENTER;
    fill = NONE;

    insets = new Insets(0, 0, 0, 0);
    ipadx = 0;
    ipady = 0;
}
}
