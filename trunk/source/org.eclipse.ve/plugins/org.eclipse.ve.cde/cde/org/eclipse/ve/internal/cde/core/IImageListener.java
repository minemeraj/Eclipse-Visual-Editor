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
 *  $RCSfile: IImageListener.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */



import java.util.EventListener;

import org.eclipse.swt.graphics.ImageData;
/**
 * Proxy listener for Image Changed Listener,
 * including size/position of image.
 * Creation date: (3/3/00 5:10:37 PM)
 * @author: Administrator
 */
public interface IImageListener extends EventListener {
	/**
	 * The image of this object has changed.
	 * The new image data is sent along.
	 * If it is null, then there is no image
	 * to render. This could happen because
	 * the size was (0,0) for example. In this
	 * case the listener would probably want to 
	 * handle no image.
	 * Creation date: (3/3/00 5:11:51 PM)
	 */
	public void imageChanged(ImageData imageData);
}
