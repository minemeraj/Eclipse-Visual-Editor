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
 *  $Revision: 1.1 $  $Date: 2005-11-06 23:26:52 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.macosx;

import org.eclipse.swt.events.*;

import org.eclipse.jem.internal.proxy.common.IVMServer;
 

/**
 * Environment for OS X.
 * @since 1.2.0
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
	};
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.Environment#getPreventShellCloseListener()
	 */
	protected ShellListener getPreventShellCloseListener() {
		return preventShellCloseListener;
	}

}
