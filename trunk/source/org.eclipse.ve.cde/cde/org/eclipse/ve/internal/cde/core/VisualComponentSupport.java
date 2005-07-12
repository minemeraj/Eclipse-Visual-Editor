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
 *  $RCSfile: VisualComponentSupport.java,v $
 *  $Revision: 1.4 $  $Date: 2005-07-12 21:08:25 $ 
 */

import org.eclipse.jface.util.ListenerList;

/**
 * This is a visual component support class. It maintains the listeners for a component and does the firing. Implementers of IVisualComponent only
 * need an instance of this class to manage the listener lists and can forward them to here. Use an ImageNotifierSupport class for the image
 * notification portion.
 */
public class VisualComponentSupport {

	protected ListenerList componentListeners = null; // Listeners for IComponentNotification.

	public boolean hasListeners() {
		return componentListeners != null && !componentListeners.isEmpty();
	}

	/**
	 * addComponentListener.
	 */
	public synchronized void addComponentListener(IVisualComponentListener aListener) {
		if (componentListeners == null)
			componentListeners = new ListenerList(2);
		componentListeners.add(aListener);
	}

	/**
	 * Fire component hidden notification.
	 */
	public void fireComponentHidden() {
		if (componentListeners != null) {
			Object[] lists = null;
			synchronized (this) {
				lists = componentListeners.getListeners();
			}
			for (int i = 0; i < lists.length; i++) {
				IVisualComponentListener listener = (IVisualComponentListener) lists[i];
				listener.componentHidden();
			}
		}
	}
	
	/**
	 * Fire component validated notification.
	 */
	public void fireComponentValidated() {
		if (componentListeners != null) {
			Object[] lists = null;
			synchronized (this) {
				lists = componentListeners.getListeners();
			}
			for (int i = 0; i < lists.length; i++) {
				IVisualComponentListener listener = (IVisualComponentListener) lists[i];
				listener.componentValidated();
			}
		}
	}	

	/**
	 * Fire component moved notification.
	 */
	public void fireComponentMoved(int x, int y) {
		if (componentListeners != null) {
			Object[] lists = null;
			synchronized (this) {
				lists = componentListeners.getListeners();
			}
			for (int i = 0; i < componentListeners.size(); i++) {
				IVisualComponentListener listener = (IVisualComponentListener) lists[i];
				listener.componentMoved(x, y);
			}
		}
	}

	/**
	 * Fire component refreshed notification.
	 */
	public void fireComponentRefreshed() {
		if (componentListeners != null) {
			Object[] lists = null;
			synchronized (this) {
				lists = componentListeners.getListeners();
			}
			for (int i = 0; i < componentListeners.size(); i++) {
				IVisualComponentListener listener = (IVisualComponentListener) lists[i];
				listener.componentRefreshed();
			}
		}
	}

	/**
	 * Fire component resized notification.
	 */
	public void fireComponentResized(int width, int height) {
		if (componentListeners != null) {
			Object[] lists = null;
			synchronized (this) {
				lists = componentListeners.getListeners();
			}
			for (int i = 0; i < componentListeners.size(); i++) {
				IVisualComponentListener listener = (IVisualComponentListener) lists[i];
				listener.componentResized(width, height);
			}
		}
	}

	/**
	 * Fire component shown notification.
	 */
	public void fireComponentShown() {
		if (componentListeners != null) {
			Object[] lists = null;
			synchronized (this) {
				lists = componentListeners.getListeners();
			}
			for (int i = 0; i < componentListeners.size(); i++) {
				IVisualComponentListener listener = (IVisualComponentListener) lists[i];
				listener.componentShown();
			}
		}
	}

	/**
	 * removeComponentListener method..
	 */
	public synchronized void removeComponentListener(IVisualComponentListener aListener) {
		if (componentListeners != null)
			componentListeners.remove(aListener);
	}

}