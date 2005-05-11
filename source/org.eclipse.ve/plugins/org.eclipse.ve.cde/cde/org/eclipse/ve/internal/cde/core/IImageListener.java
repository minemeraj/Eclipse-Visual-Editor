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
 *  $RCSfile: IImageListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-11 19:01:26 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.EventListener;

import org.eclipse.swt.graphics.ImageData;

/**
 * Listener for image changed notifications.
 * 
 * @since 1.1.0
 */
public interface IImageListener extends EventListener {

	/**
	 * The image of this object has changed. The new image data is sent along. If it is null, then there is no image to render. This could happen
	 * because the size was (0,0) for example. In this case the listener would probably want to handle no image.
	 * 
	 * @param imageData
	 * 
	 * @since 1.1.0
	 */
	public void imageChanged(ImageData imageData);
}