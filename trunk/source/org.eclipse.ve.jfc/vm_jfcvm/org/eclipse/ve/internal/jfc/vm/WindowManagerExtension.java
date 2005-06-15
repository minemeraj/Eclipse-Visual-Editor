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
 *  $Revision: 1.2 $  $Date: 2005-06-15 20:19:27 $ 
 */
package org.eclipse.ve.internal.jfc.vm;

import java.awt.*;
 

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
	 * Apply the frame title. We need to be able to put in a default title if the
	 * title is not already set, or are applying null or "". This is so that it has a title because the window shows
	 * on the taskbar of the system and without a title people get confused.
	 * <p>
	 * It will return the old title.
	 * 
	 * @param frame
	 * @param title
	 * @param replaceOld Only used if title is empty, then if <code>true</code> should replace old (this would be for a normal set),
	 * <code>false</code> shouldn't replace old if old title is not empty (this would be for a initial setup
	 * where no title was explicitly set on the client side. This allows us to replace an empty old title with the
	 * default string, or not replace it if old title was good).
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static String applyFrameTitle(Frame frame, String title, boolean replaceOld) {
		String oldTitle = frame.getTitle();
		if (title == null || title.length() == 0) {
			if (replaceOld || (oldTitle == null || oldTitle.length() == 0))
				frame.setTitle(VisualVMMessages.getString("FrameDefaultTitle"));	 //$NON-NLS-1$
		} else
			frame.setTitle(title);
		
		return oldTitle;
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
