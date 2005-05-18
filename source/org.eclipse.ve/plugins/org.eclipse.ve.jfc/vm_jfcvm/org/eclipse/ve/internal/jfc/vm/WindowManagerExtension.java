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
 *  $RCSfile: WindowManagerExtension.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-18 22:53:55 $ 
 */
package org.eclipse.ve.internal.jfc.vm;

import java.awt.EventQueue;
import java.awt.Window;
 

/**
 * Manager for awt.Windows. This extends ComponentManager because it needs state to
 * be saved. So it can't be a static helper class.
 * @since 1.1.0
 */
public class WindowManagerExtension extends ComponentManager.ComponentManagerExtension {
	
	/**
	 * Some bug in window component peer (for a Dialog) throws an exception
	 * when disposing of the second dialog when we had more than one up. 
	 * Can't narrow it down to more than this. So this will try to handle it.
	 * @param window
	 * 
	 * @since 1.1.0
	 */
	public static void disposeWindow(final Window window) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.dispose();
				} catch (NullPointerException e) {
					// Second dialog often get's NPE on dispose. Don't know why.
					// It isn't anything we are doing that we can tell.
					// So we just catch it and ignore it. 
					window.dispose();	// Try it again to try to complete the cleanup/dispose. The second one usually works.
				}
			}
		});
	}
	
	/**
	 * Pack window on any change flag.
	 */
	protected boolean packOnChange;
	
	/**
	 * Set the pack on change flag. 
	 * @param packOnChange <code>true</code> if on any change (invalidate) the Window should be packed. This is used when no explicit size has
	 * been set and it should float to the packed size.
	 * 
	 * @since 1.1.0
	 */
	public void setPackOnChange(boolean packOnChange) {
		this.packOnChange = packOnChange;
	}
	
	
	protected void invalidated() {
		if (packOnChange)
			((Window) getComponent()).pack();
	}

	
}
