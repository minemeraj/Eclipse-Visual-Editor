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
 *  $RCSfile: IImageNotifier.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */



/**
 * Image Changed Notifier.
 * Creation date: (3/3/00 5:14:19 PM)
 * @author: Administrator
 */
public interface IImageNotifier {
	/**
	 * Add the image listener.
	 * Creation date: (3/3/00 5:15:47 PM)
	 * @param listener IImageListener
	 */
	public void addImageListener(IImageListener listener);
	
	/**
	 * Answer if anyone is listening.
	 * Creation date: (3/15/00 5:57:45 PM)
	 * @return boolean
	 */
	public boolean hasImageListeners();
	
	/**
	 * Invalidate the image. The next time
	 * refreshImage is called, if still invalid,
	 * it will send out a new image.
	 * Creation date: (3/6/00 5:39:58 PM)
	 * @return com.ibm.swt.image.ImageData
	 */
	public void invalidateImage();
	
	/**
	 * Refresh the image, get a new one and
	 * send notification if image was validated
	 * with this request.
	 * Creation date: (3/6/00 5:39:58 PM)
	 * @return com.ibm.swt.image.ImageData
	 */
	public void refreshImage();
	
	/**
	 * Remove the image listener.
	 * Creation date: (3/3/00 5:15:47 PM)
	 * @param listener com.ibm.vce.java.awt.IImageListener
	 */
	public void removeImageListener(IImageListener listener);
}
