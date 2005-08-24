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
 *  $RCSfile: IVisualComponentListener.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:50 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.EventListener;

/**
 * Listener for visual component changes.
 * <p>
 * <b>Note:</b> These notifications will not necessarily be on the UI thread, therefor
 * do not assume it is on the UI thread.
 * 
 * @since 1.1.0
 */
public interface IVisualComponentListener extends EventListener {

	/**
	 * The listened component was hidden
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void componentHidden();

	/**
	 * The listened component moved. The point moved to is passed on.
	 * <p>
	 * The origin that the point is relative to is IVisualComponent implementation specific. Listeners will need to know who they are listening to.
	 * For example, the point may be relative to the direct parent, or may be relative to some absolute coordinate.
	 * 
	 * @param x
	 * @param y
	 * 
	 * @since 1.1.0
	 */
	public void componentMoved(int x, int y);

	/**
	 * The listened component has changed in some way, but can't give a specific. So refresh. (an example is the underlying component that the visual
	 * component is monitoring has been replaced, so refresh the bounds).
	 * 
	 * @since 1.1.0
	 */
	public void componentRefreshed();

	/**
	 * The listened component was resized. The new size is passed also.
	 * 
	 * @param width
	 * @param height
	 * 
	 * @since 1.1.0
	 */
	public void componentResized(int width, int height);

	/**
	 * The listened component was shown.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void componentShown();
	
	/**
	 * This is sent when the component was invalidated and then subsequently validated. It is not required that all visual components
	 * can send validated notifications. Also when it is sent depends on the visual component itself. Typically it would be when
	 * a new image would of been required if an images were being listened for.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void componentValidated();
}
