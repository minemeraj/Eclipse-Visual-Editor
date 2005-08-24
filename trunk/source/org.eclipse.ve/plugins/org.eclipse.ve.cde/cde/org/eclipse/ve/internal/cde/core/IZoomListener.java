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
 *  $RCSfile: IZoomListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

import java.util.EventListener;
/**
 * This listener listens for changes to the zoom setting.
 * A listener must add itself to the listener list of the
 * ZoomController. See ZoomController to find out how to
 * find the ZoomController.
 */
public interface IZoomListener extends EventListener {

	/**
	 * The zoom has changed. The value is the percentage of the zoom.
	 * For example, 100 = normal size, 1000 = 10 times larger, 10 = 1/10 size
	 */
	public void zoomChanged(int newZoom, int oldZoom);
}
