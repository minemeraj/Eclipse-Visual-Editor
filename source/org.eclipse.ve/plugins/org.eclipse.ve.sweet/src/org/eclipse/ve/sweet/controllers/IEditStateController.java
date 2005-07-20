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

import org.eclipse.ve.sweet.objectviewer.IObjectViewer;

/**
 * An interface for objects that can enable/disable UI objects automatically when any of several
 * IObjectViewers becomes dirty or is saved.
 * 
 * @author djo
 */
public interface IEditStateController {

	/**
	 * Method addControl.  Add a control to be managed by this IEditStateController.  When
	 * any IObjectViewer managed by this IEditStateController becomes dirty, all managed
	 * controls will be enabled/disabled in the user interface as specified by their
	 * enableOnDirty flag.
	 * 
	 * @param control The control to manager.
	 * @param enableOnDirty If the control should be enabled when editors are dirty.
	 */
	void addControl(Object control, boolean enableOnDirty);

	/**
	 * Method removeControl.  Removes a control from being managed by this IEditStateController.
	 * 
	 * @param control The control to remove.
	 */
	void removeControl(Object control);

	/**
	 * Method addObjectViewer.  Add an IObjectViewer from which to receive dirty/committed
	 * events.  When any managed IObjectViewer becomes dirty, all managed controls will be
	 * disabled.  When all managed IObjectViewers are committed, then the managed controls
	 * will be reenabled.
	 * 
	 * @param objectViewer The IObjectViewer to manage.
	 */
	void addObjectViewer(IObjectViewer objectViewer);

	/**
	 * Method removeObjectViewer.  Remove an IObjectViewer from being managed by this 
	 * IEditStateController.
	 * 
	 * @param objectViewer The IObjectViewer to remove.
	 */
	void removeObjectViewer(IObjectViewer objectViewer);
	
	/**
	 * Method isDirty.  Indicates if the entire group of IObjectViewer objects is dirty or
	 * not.  Specifically: isDirty returns false iff all managed IObjectViewers are not dirty.
	 * isDirty returns true otherwise.
	 * 
	 * @return true if any managed IObjectViewer isDirty(); false otherwise.
	 */
	boolean isDirty();

}