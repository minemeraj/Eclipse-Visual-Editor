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
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */

import org.eclipse.jface.util.ListenerList;
/**
 * This is a visual component support class. It maintains
 * the listeners for a component and does the firing.
 * Implementers of IVisualComponent only need an instance
 * of this class to manage the listener lists and can forward
 * them to here. Use an ImageNotifierSupport class for 
 * the image notification portion.
 */
public class VisualComponentSupport {
	protected ListenerList componentListeners = null; // Listeners for IComponentNotification.

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
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
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
	 * Fire component moved notification.
	 */
	public void fireComponentMoved(int x, int y) {
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
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
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
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
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
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
		// Probably should make a copy of the notification list to prevent
		// modifications while firing, but we'll see if this gives any problems.
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
		componentListeners.remove(aListener);
	}

}
