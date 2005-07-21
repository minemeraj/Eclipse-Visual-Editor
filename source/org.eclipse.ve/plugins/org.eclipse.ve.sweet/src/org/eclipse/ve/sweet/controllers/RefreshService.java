/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.controllers;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.ve.sweet.objectviewer.IEditStateListener;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;

/**
 * Forwards edit state change events to a global set of listeners so that 
 * they can refresh themselves properly when objects commit changes.
 * IObjectViewers are not automatically registered with the RefreshService.
 * that must be done as is appropriate for each application.<p>
 * 
 * The RefreshService is normally used by adding the RefreshService 
 * singleton as an IEditStateListener to each IObjectViewer that needs
 * to broadcast edit state changes.<p>
 * 
 * Clients then register themselves using addEditStateListener and
 * removeEditStateListener in the usual manner.<p>
 * 
 * Alternately, clients could create multiple instances of the RefreshService
 * object and use them as desired.
 * 
 * @author djo
 */
public class RefreshService implements IEditStateListener {
	private static RefreshService singleton = null;
	
	/**
	 * Method getDefault.  Return the RefreshService singleton.
	 * 
	 * @return The global RefreshService.
	 */
	public static RefreshService getDefault() {
		if (singleton == null) {
			singleton = new RefreshService();
		}
		return singleton;
	}

	/** (non-API)
	 * @see org.eclipse.ve.sweet.objectviewer.IEditStateListener#stateChanged(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void stateChanged(IObjectViewer sender) {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			IEditStateListener listener = (IEditStateListener) i.next();
			listener.stateChanged(sender);
		}
	}
	
	private LinkedList listeners = new LinkedList();
	
	/**
	 * Method addEditStateListener.  Add the specified IEditStateListener to the
	 * set of listeners that will be notified of edit state changes on all
	 * IObjectViewers with which this RefreshService is registered.
	 * 
	 * @param listener The IEditStateListener to add.
	 */
	public void addEditStateListener(IEditStateListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Method removeEditStateListener.  Removes the specified IEditStateListener 
	 * from the set of listeners that will be notified of edit state changes on all
	 * IObjectViewers with which this RefreshService is registered.
	 * 
	 * @param listener Teh IEditStateListener to remove.
	 */
	public void removeEditStateListener(IEditStateListener listener) {
		listeners.remove(listener);
	}

}
