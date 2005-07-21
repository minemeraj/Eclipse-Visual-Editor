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
import java.util.LinkedList;

import org.eclipse.ve.sweet.controllers.IEditStateController;
import org.eclipse.ve.sweet.objectviewer.IEditStateListener;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.reflect.DuckType;

/**
 * An object that tracks the dirty/committed state of specified IObjectViewers and 
 * enables/disables SWT controls when the edit state changes.  This implementation uses
 * duck typing so that it works with any SWT control that has setEnabled/isEnabled methods.
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
	
	private static class ControlInfo {
		public final IEnableableDuck control;
		public final boolean enableOnDirty;
		public ControlInfo(IEnableableDuck control, boolean dirty) {
			this.control = control;
			enableOnDirty = dirty;
		}
	}

	private HashMap controls = new HashMap();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#addControl(java.lang.Object)
	 */
	public void addControl(Object control, boolean enableOnDirty) {
		if (!DuckType.instanceOf(IEnableableDuck.class, control)) {
			throw new ClassCastException(control.getClass().getName() + " does not implement setEnabled/isEnabled");
		}
		IEnableableDuck enableableControl = (IEnableableDuck)DuckType.implement(IEnableableDuck.class, control);
		controls.put(control, new ControlInfo(enableableControl, enableOnDirty));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#removeControl(java.lang.Object)
	 */
	public void removeControl(Object control) {
		controls.remove(control);
	}
	
	private LinkedList objectViewers = new LinkedList();
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IEditStateController#addObjectViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void addObjectViewer(IObjectViewer objectViewer) {
		objectViewers.add(objectViewer);
		boolean isDirty = objectViewer.isDirty();
		
		if (isDirty && !isDirty()) {
			this.dirty = true;
			updateControlEnablement();
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
		for (Iterator objectViewersIter = objectViewers.iterator(); objectViewersIter.hasNext();) {
			IObjectViewer objectViewer = (IObjectViewer) objectViewersIter.next();
			if (objectViewer.isDirty()) {
				nowIsDirty = true;
				break;
			}
		}
		
		if (isDirty()) {
			if (!nowIsDirty) {
				dirty = nowIsDirty;
				updateControlEnablement();
			}
		} else {
			if (nowIsDirty) {
				dirty = nowIsDirty;
				updateControlEnablement();
			}
		}
	}

	private void updateControlEnablement() {
		for (Iterator controlsIter = controls.keySet().iterator(); controlsIter.hasNext();) {
			ControlInfo controlInfo = (ControlInfo) controls.get(controlsIter.next());
			
			if (controlInfo.control.isDisposed()) {
				continue;
			}
			
			if (controlInfo.enableOnDirty) {
				controlInfo.control.setEnabled(dirty);
			} else {
				controlInfo.control.setEnabled(!dirty);
			}
		}
	}

}
