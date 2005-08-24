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
/*
 * $RCSfile: WindowGraphicalEditPart.java,v $ $Revision: 1.5 $ $Date: 2005-08-24 23:38:09 $
 */

package org.eclipse.ve.internal.jfc.core;

import org.eclipse.draw2d.geometry.Point;

/**
 * Window graphical edit parts have special logic for visibility. When instantiated on the tree form they do not apply the visibility changes so that
 * they always remain visible so we can get their graphic and listen to them
 */
public class WindowGraphicalEditPart extends ContainerGraphicalEditPart {
	public static final Point OFF_SCREEN_LOCATION = new Point(-10000, -10000);

	//	protected IOrderManager fTabOrderManager;
	//	protected Rectangle fLightenedArea;
	public WindowGraphicalEditPart(Object model) {
		super(model);
	}
}
