/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:48 $ 
 */
package org.eclipse.ve.internal.cde.emf;

import org.eclipse.emf.common.notify.*;
import org.eclipse.gef.EditPart;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditPartRunnable;
 

/**
 * This is a base class to be used for a common EditPart with EMF models notification problem. Notification can
 * come in on any thread, typically, but updates to the edit part need to be done on the ui thread. This class provides
 * a more convienent way of doing this. 
 * <p>
 * It would typically be used in the following manner.
 * 
 * <pre><code>
 * 	private Adapter adapter = new EditPartAdapterRunnable(this) {
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
public abstract class EditPartAdapterRunnable extends EditPartRunnable implements Adapter{
	
	public EditPartAdapterRunnable(EditPart editPart) {
		super(editPart);
	}

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
	 * Queue up this object so that the <code>run</code> method of <code>this</code> will be executed at the
	 * end of the next transaction.
	 * 
	 * @param ep the EditPart to determine the display to use.
	 * 
	 * @since 1.0.0
	 */
	protected void queueExec(EditPart ep) {
		CDEUtilities.displayExec(ep, this);
	}
	
	/**
	 * Queue up this object so that the <code>run</code> method of the given runnable will be executed at
	 * the end of the next transaction.
	 * 
	 * @param ep the EditPart to determine the display to use.
	 * @param runnable the runnable to execute. This can be used if another runnable is needed instead of the <code>this</code>
	 * 
	 * @see EditPartAdapterRunnable#queueExec(EditPart)
	 * @since 1.0.0
	 */
	protected void queueExec(EditPart ep, EditPartRunnable runnable) {
		CDEUtilities.displayExec(ep, runnable);
	}
	
	/**
	 * Queue up this object so that the <code>run</code> method of <code>this</code> will be executed at the
	 * end of the next transaction.
	 * 
	 * @param ep the EditPart to determine the display to use.
	 * @param once the key to limit to only one queue up of the run for this editpart.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void queueExec(EditPart ep, Object once) {
		CDEUtilities.displayExec(ep, once, this);
	}

	/**
	 * Queue up the runable will be executed at the
	 * end of the next transaction.
	 * 
	 * @param ep the EditPart to determine the display to use.
	 * @param once the key to limit to only one queue up of the runnable for this editpart.
	 * @param runnable
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void queueExec(EditPart ep, Object once, EditPartRunnable runnable) {
		CDEUtilities.displayExec(ep, once, runnable);
	}
}
