/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PreventShellCloseMinimizeListener.java,v $
 *  $Revision: 1.2 $  $Date: 2004-11-02 22:00:15 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
 

/**
 * A Shell Listener that prevents the target shell from being closed.
 * @since 1.0.0
 */
public class PreventShellCloseMinimizeListener extends ShellAdapter {
	
	/**
	 * Prevent the closing of the shell when the shellClosed event is received.
	 * @param e the shell event
	 */
	public void shellClosed(ShellEvent e) {
		e.doit = false;
	}
}
