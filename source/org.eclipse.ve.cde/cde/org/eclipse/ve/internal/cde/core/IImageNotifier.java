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
 *  $RCSfile: IImageNotifier.java,v $
 *  $Revision: 1.4 $  $Date: 2005-07-12 21:10:21 $ 
 */
package org.eclipse.ve.internal.cde.core;

/**
 * Image Changed Notifier. It notifies of changes to the image. These can happen because someone requests a new image or because it has been
 * determined by implementers that the image has changed, and so it will request one and the notification will go out that there is a new image.
 * 
 * @since 1.0.0
 */
public interface IImageNotifier {

	/**
	 * Add an image listener. Do not add an image listener except if you need the actual image. This is because
	 * if you are the only listener and you don't need the image it will still cause an image to be captured.
	 * This is a waste. Use {@link IVisualComponentListener#componentValidated()} instead.
	 * 
	 * @param listener
	 * 
	 * @since 1.0.0
	 */
	public void addImageListener(IImageListener listener);

	/**
	 * Is anyone listening?
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public boolean hasImageListeners();

	/**
	 * Invalidate the image. The next time refreshImage is called, if still invalid, it will send out a new image. It will not trigger a new image at
	 * this time.
	 * 
	 * @since 1.0.0
	 */
	public void invalidateImage();

	/**
	 * Refresh the image, get a new one and send notification if image was validated with this request. If the image was already valid, nothing will
	 * happen due to this call.
	 * 
	 * @since 1.1.0
	 */
	public void refreshImage();

	/**
	 * Remove the image listener.
	 * 
	 * @param listener
	 * 
	 * @since 1.1.0
	 */
	public void removeImageListener(IImageListener listener);
}