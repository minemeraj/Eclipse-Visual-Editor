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
 *  $RCSfile: ImageNotifierSupport.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import java.util.*;
import org.eclipse.swt.graphics.ImageData;
/**
 * This is an image notifier support class. It maintains
 * the listeners for an image notifier and does the firing.
 * Implementers of IImageNotifier only need an instance
 * of this class to manage the listener lists and can forward
 * them to here. Use an VisualComponentSupport class for 
 * the IVisualComponent non-image portion.
 */
public class ImageNotifierSupport {
	protected List imageListeners = null;	// Listeners for IComponentNotification.

	/**
	 * addImageListener.
	 */
	public synchronized void addImageListener(IImageListener aListener) {
		if (imageListeners == null)
			imageListeners = new ArrayList(2);
		imageListeners.add(aListener);
	}
	
	/**
	 * Fire image changed notification.
	 */
	public void fireImageChanged(ImageData imageData) {
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
		if (imageListeners != null) {
			for (int i=0; i<imageListeners.size(); i++) {
				IImageListener listener = (IImageListener) imageListeners.get(i);
				listener.imageChanged(imageData);
			}
		}
	}
	
	/**
	 * hasListeners method..
	 */
	public boolean hasImageListeners() {
		return imageListeners != null && !imageListeners.isEmpty();
	}
	
	/**
	 * removeImageListener method.
	 */
	public synchronized void removeImageListener(IImageListener aListener) {
		imageListeners.remove(aListener);
	}

}