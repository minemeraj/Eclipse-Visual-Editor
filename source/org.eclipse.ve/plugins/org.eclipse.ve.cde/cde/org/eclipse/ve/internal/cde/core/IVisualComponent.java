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
 *  $RCSfile: IVisualComponent.java,v $
 *  $Revision: 1.3 $  $Date: 2005-03-29 02:41:12 $ 
 */



import org.eclipse.draw2d.geometry.*;
/**
 * VisualComponent interface. A visual component is an interface
 * to visual components. It notifies if the image has changed,
 * and if the size and position have changed.
 */
public interface IVisualComponent extends IImageNotifier {
	/**
	 * Return the bounds of the live object.
	 */
	public Rectangle getBounds();
	
	/**
	 * Return the position of the live object.
	 */
	public Point getLocation();
	
	/**
	 * Return the size of the live object.
	 */
	public Dimension getSize();
	
	/**
	 * Add the argument as a listener for when the component
	 * moves, resized or is shown
	 */
	public void addComponentListener(IVisualComponentListener aListener);
	
	/**
	 * Remove the argument as a listener
	 * for component changes.
	 */
	public void removeComponentListener(IVisualComponentListener aListener);
	
	public boolean hasImageListeners();	
}
