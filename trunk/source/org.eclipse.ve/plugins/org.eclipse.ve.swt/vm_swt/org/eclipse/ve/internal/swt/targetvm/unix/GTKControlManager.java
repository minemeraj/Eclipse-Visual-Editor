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
 *  $RCSfile: GTKControlManager.java,v $
 *  $Revision: 1.4 $  $Date: 2005-02-15 23:54:57 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.unix;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.ve.internal.swt.targetvm.ControlManager;
import org.eclipse.ve.internal.swt.targetvm.IImageCapture;
 

/**
 * 
 * @since 1.0.0
 */
public class GTKControlManager extends ControlManager {

	/**
	 * A Shell Listener that prevents the target shell from being closed.
	 * It does not prevent it from being minimized as layout and painting
	 * are performed normally on GTK.
	 * 
	 * @since 1.0.0
	 */
	public class PreventShellCloseListener extends ShellAdapter {
		
		/**
		 * Prevent the closing of the shell when the shellClosed event is received.
		 * @param e the shell event
		 */
		public void shellClosed(ShellEvent e) {
			e.doit = false;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.ControlManager#getImageCapturer()
	 */
	public IImageCapture getImageCapturer() {
		return new ImageCapture();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.ControlManager#addShellListener(org.eclipse.swt.widgets.Shell)
	 */
	public void addShellListener(final Shell shell) {
		shell.getDisplay().asyncExec(new Runnable(){
			public void run() {
				shell.addShellListener(new PreventShellCloseListener());
			}
		});	
	}

}
