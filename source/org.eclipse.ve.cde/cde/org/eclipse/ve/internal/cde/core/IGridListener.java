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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: IGridListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

import java.util.EventListener;
/**
 * Listener for events relating to the grid.
 */
public interface IGridListener extends EventListener {
	
	/**
	 * Grid Height has changed.
	 */
	public void gridHeightChanged(int gridHeight, int oldGridHeight);

	/**
	 * Grid Width has changed.
	 */
	public void gridWidthChanged(int gridWidth, int oldGridWidth);

	/**
	 * Grid Margin has changed.
	 */
	public void gridMarginChanged(int gridMargin, int oldGridMargin);
	
	/**
	 * Grid Visibility has changed.
	 */
	public void gridVisibilityChanged(boolean isShowing);

}
