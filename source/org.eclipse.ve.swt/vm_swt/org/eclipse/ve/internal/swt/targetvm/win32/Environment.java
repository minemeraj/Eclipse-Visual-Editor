/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: Environment.java,v $
 *  $Revision: 1.3 $  $Date: 2009-04-06 09:18:14 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.win32;

import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.jem.internal.proxy.common.IVMServer;
 

/**
 * Environment for Win32.
 * @since 1.1.0
 */
public class Environment extends org.eclipse.ve.internal.swt.targetvm.Environment {

	public Environment(IVMServer vmserver) {
		super(vmserver);
	}

	private static final ShellListener preventShellCloseListener = new ShellAdapter() {
		
		/**
		 * Prevent the closing of the shell when the shellClosed event is received.
		 * @param e the shell event
		 */
		public void shellClosed(ShellEvent e) {
			e.doit = false;
		}
		
		/**
		 * Prevent the minimizing of the shell by un-minimizing whenever the minimization occurs
		 * @param e the shell event
		 */
		public void shellIconified(ShellEvent e) {
			Shell s = (Shell)e.widget;
			s.setMinimized(false);	// Can't stop it, so just restore it.
		}
	};
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.Environment#getPreventShellCloseListener()
	 */
	protected ShellListener getPreventShellCloseListener() {
		return preventShellCloseListener;
	}

}
