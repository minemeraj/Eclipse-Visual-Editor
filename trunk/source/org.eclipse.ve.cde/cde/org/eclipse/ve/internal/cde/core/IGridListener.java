package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IGridListener.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
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