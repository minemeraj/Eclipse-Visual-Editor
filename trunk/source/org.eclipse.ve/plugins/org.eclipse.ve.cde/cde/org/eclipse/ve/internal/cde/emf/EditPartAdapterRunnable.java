/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EditPartAdapterRunnable.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:58 $ 
 */
package org.eclipse.ve.internal.cde.emf;

import org.eclipse.emf.common.notify.*;
import org.eclipse.gef.EditPart;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
 

/**
 * This is a base class to be used for a common EditPart with EMF models notification problem. Notification can
 * come in on any thread, typically, but updates to the edit part need to be done on the ui thread. This class provides
 * a more convienent way of doing this. 
 * <p>
 * It would typically be used in the following manner.
 * 
 * <pre><code>
 * 	private Adapter adapter = new EditPartAdapterRunnable() {
 * 		public void notifyChanged(Notification notification) {
 * 			if (..notification is of interest..)
 * 				queueExec(MyOuterEditpart.this);
 * 		}
 * 
 * 		public void run() {
 * 			refreshChildren();	// This can only be done on ui thread.
 * 		}
 * 	}
 * 
 * 	public void activate() {
 * 		super.activate();
 * 		((Notifier) getModel()).eAdapters().add(adapter);
 * 	} 
 * 
 * 	public void deactivate() {
 * 		super.deactivate();
 * 		((Notifier) getModel()).eAdapters().remove(adapter);
 * 	}
 * </code></pre>
 * 
 * @since 1.0.0
 */
public abstract class EditPartAdapterRunnable implements Adapter, Runnable {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;	// Typically not important
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		// Typically not important
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return false;	// Typically not important
	}

	/**
	 * Queue up this object so that the <code>run</code> method of <code>this</code> will be executed either 
	 * immediately if on the display thread of the viewer associated with the <code>ep</code>,
	 * or if not, then doing an asyncExec on that display.
	 * 
	 * @param ep the EditPart to determine the display to use.
	 * 
	 * @since 1.0.0
	 */
	protected void queueExec(EditPart ep) {
		CDEUtilities.displayExec(ep, this);
	}
	
	/**
	 * Queue up this object so that the <code>run</code> method of the given runnable will be executed either 
	 * immediately if on the display thread of the viewer associated with the <code>ep</code>,
	 * or if not, then doing an asyncExec on that display.
	 * 
	 * @param ep the EditPart to determine the display to use.
	 * @param runnable the runnable to execute. This can be used if another runnable is needed instead of the <code>this</code>
	 * 
	 * @see EditPartAdapterRunnable#queueExec(EditPart)
	 * @since 1.0.0
	 */
	protected void queueExec(EditPart ep, Runnable runnable) {
		CDEUtilities.displayExec(ep, runnable);
	}
}
