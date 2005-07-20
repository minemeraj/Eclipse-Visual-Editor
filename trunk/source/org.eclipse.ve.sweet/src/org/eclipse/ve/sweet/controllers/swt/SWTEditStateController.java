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
package org.eclipse.ve.sweet.controllers.swt;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.ve.sweet.controllers.IEditStateController;
import org.eclipse.ve.sweet.objectviewer.IEditStateListener;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.reflect.DuckType;

/**
 * An object that tracks the dirty/committed state of specified IObjectViewers and 
 * enables/disables SWT controls when the edit state changes.
 * 
 * @author djo
 */
public class SWTEditStateController implements IEditStateController {
	private boolean dirty = false;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#isDirty()
	 */
	public boolean isDirty() {
		return dirty;
	}

	private HashMap controls = new HashMap();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#addControl(java.lang.Object)
	 */
	public void addControl(Object control) {
		if (!DuckType.instanceOf(IEnableableDuck.class, control)) {
			throw new ClassCastException(control.getClass().getName() + " does not implement setEnabled/isEnabled");
		}
		IEnableableDuck enableableControl = (IEnableableDuck)DuckType.implement(IEnableableDuck.class, control);
		controls.put(control, enableableControl);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#removeControl(java.lang.Object)
	 */
	public void removeControl(Object control) {
		controls.remove(control);
	}
	
	private HashMap objectViewers = new HashMap();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#addObjectViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void addObjectViewer(IObjectViewer objectViewer) {
		boolean isDirty = objectViewer.isDirty();
		objectViewers.put(objectViewer, new Boolean(isDirty));
		
		if (isDirty && !isDirty()) {
			this.dirty = true;
			disableControls();
		}
		
		objectViewer.addObjectListener(editStateListener);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#removeObjectViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void removeObjectViewer(IObjectViewer objectViewer) {
		objectViewer.removeObjectListener(editStateListener);
		objectViewers.remove(objectViewer);
		refreshDirtyState();
	}

	private IEditStateListener editStateListener = new IEditStateListener() {
		public void stateChanged(IObjectViewer sender) {
			refreshDirtyState();
		}
	};

	private void refreshDirtyState() {
		boolean nowIsDirty = false;
		for (Iterator objectViewersIter = objectViewers.keySet().iterator(); objectViewersIter.hasNext();) {
			IObjectViewer objectViewer = (IObjectViewer) objectViewers.get(objectViewersIter.next());
			if (objectViewer.isDirty()) {
				nowIsDirty = true;
				break;
			}
		}
		
		if (isDirty()) {
			if (!nowIsDirty) {
				dirty = nowIsDirty;
				enableControls();
			}
		} else {
			if (nowIsDirty) {
				dirty = nowIsDirty;
				disableControls();
			}
		}
	}

	private void disableControls() {
		for (Iterator controlsIter = controls.keySet().iterator(); controlsIter.hasNext();) {
			IEnableableDuck control = (IEnableableDuck) controls.get(controlsIter.next());
			control.setEnabled(false);
		}
	}

	private void enableControls() {
		for (Iterator controlsIter = controls.keySet().iterator(); controlsIter.hasNext();) {
			IEnableableDuck control = (IEnableableDuck) controls.get(controlsIter.next());
			control.setEnabled(true);
		}
	}

}
