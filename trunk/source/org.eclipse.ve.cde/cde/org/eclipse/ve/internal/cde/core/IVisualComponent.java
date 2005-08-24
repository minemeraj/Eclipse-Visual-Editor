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
 *  $RCSfile: IVisualComponent.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:50 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.geometry.*;

/**
 * VisualComponent interface. A visual component is an interface to visual components. It notifies if the image has changed, and if the size and
 * position have changed.
 */
public interface IVisualComponent extends IImageNotifier {

	/**
	 * Return the bounds of the live object.
	 * <p>
	 * The origin that the bounds is relative to is IVisualComponent implementation specific. Users will need to know who they are listening to. For
	 * example, the point may be relative to the direct parent, or may be relative to some absolute coordinate.
	 * <p>
	 * <b>Note:</b> This is called often, so it must be cached in the component.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Rectangle getBounds();

	/**
	 * Return the position of the live object.
	 * <p>
	 * The origin that the point is relative to is IVisualComponent implementation specific. Users will need to know who they are listening to. For
	 * example, the point may be relative to the direct parent, or may be relative to some absolute coordinate.
	 * <p>
	 * <b>Note:</b> This is called often, so it must be cached in the component.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getLocation();
	
	/**
	 * Return the absolute location of the live object.
	 * <p>
	 * <b>Note:</b> This is called often, so it must be cached in the component.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getAbsoluteLocation();

	/**
	 * Return the size of the live object.
	 * <p>
	 * <b>Note:</b> This is called often, so it must be cached in the component.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Dimension getSize();

	/**
	 * Add the argument as a listener for when the component moves, resized or is shown
	 * 
	 * @param aListener
	 * 
	 * @since 1.1.0
	 */
	public void addComponentListener(IVisualComponentListener aListener);

	/**
	 * Remove the argument as a listener for when the component moves, resized or is shown
	 * 
	 * @param aListener
	 * 
	 * @since 1.1.0
	 */
	public void removeComponentListener(IVisualComponentListener aListener);
}
