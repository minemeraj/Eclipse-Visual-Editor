package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: IVisualComponentListener.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */



/**
 * Listener for visual component changes
 */
public interface IVisualComponentListener extends java.util.EventListener {
	/**
	 * The listened component was hidden
	 */
	public void componentHidden();
	
	/**
	 * The listened component moved, the point moved to is passed on.
	 */
	public void componentMoved(int x, int y);
	
	/**
	 * The listened component has changed in some way,
	 * but can't give a specific. So refresh.
	 * (an example is the underlying component
	 * that the visual component is
	 * monitoring has been replaced, so refresh the bounds).
	 */
	public void componentRefreshed();
	
	/**
	 * The listened component was resized. The new size is passed also.
	 */
	public void componentResized(int width, int height);
	
	/**
	 * The listened component was shown
	 */
	public void componentShown();
}
