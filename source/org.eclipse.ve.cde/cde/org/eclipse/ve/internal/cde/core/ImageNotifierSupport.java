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
 *  $RCSfile: ImageNotifierSupport.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.graphics.ImageData;

/**
 * This is an image notifier support class. It maintains the listeners for an image notifier and does the firing. Implementers of IImageNotifier only
 * need an instance of this class to manage the listener lists and can forward them to here. Use an VisualComponentSupport class for the
 * IVisualComponent non-image portion.
 * 
 * @since 1.0.0
 */
public class ImageNotifierSupport {

	protected ListenerList imageListeners = null;

	/**
	 * Add image listener.
	 * 
	 * @param aListener
	 * 
	 * @since 1.0.0
	 */
	public synchronized void addImageListener(IImageListener aListener) {
		if (imageListeners == null)
			imageListeners = new ListenerList(2);
		imageListeners.add(aListener);
	}

	/**
	 * Fire image changed notification.
	 * 
	 * @param imageData
	 * 
	 * @since 1.0.0
	 */
	public void fireImageChanged(ImageData imageData) {
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
		if (imageListeners != null && !imageListeners.isEmpty()) {
			Object[] listeners = imageListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				((IImageListener) listeners[i]).imageChanged(imageData);
			}
		}
	}

	/**
	 * Is anyone listening?
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public boolean hasImageListeners() {
		return imageListeners != null && !imageListeners.isEmpty();
	}

	/**
	 * Remove listener.
	 * 
	 * @param aListener
	 * 
	 * @since 1.0.0
	 */
	public synchronized void removeImageListener(IImageListener aListener) {
		if (imageListeners != null)
			imageListeners.remove(aListener);
	}

}
