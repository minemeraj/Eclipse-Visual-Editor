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
 *  $RCSfile: ImageCapture.java,v $
 *  $Revision: 1.2 $  $Date: 2005-11-12 14:32:19 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.macosx;

import java.lang.reflect.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Image Capture for Mac OS X platforms.
 * 
 * @since 1.2.0
 */

public class ImageCapture extends org.eclipse.ve.internal.swt.targetvm.ImageCapture {
	
	private static Field shellHandleField = null;
	
	static {
		
		try {
			shellHandleField = Shell.class.getDeclaredField("shellHandle");
			shellHandleField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
		
		Rectangle rect = control.getBounds();
		if (rect.width <= 0 || rect.height <= 0)
			return null;
		
		// int width = Math.min(rect.width, maxWidth);
		// int height = Math.min(rect.height, maxHeight);
				
		int controlHandle = -1;
		int shellHandle = -1;
		
		if (control instanceof Shell)
		{
			try {
				shellHandle = shellHandleField.getInt(control);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (shellHandle != -1)
			{
				controlHandle = OS.HIViewGetRoot(shellHandle);
			}
		}
		else
		{
			controlHandle = control.handle;
			try {
				shellHandle = shellHandleField.getInt(control.getShell());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Get the current active window
		int activeWindow = OS.ActiveNonFloatingWindow();
		
		
		// Activate the shell
		OS.ShowWindow(shellHandle);
				
		CGRect bounds = new CGRect();
		int[] imageHandle = new int[1];
		OS.HIViewCreateOffscreenImage(controlHandle, 0, bounds, imageHandle);
		control.redraw();
	
		// Restore the active window
		OS.ShowWindow(activeWindow);
		
		Image resultImage = null;
		if (imageHandle[0] > 0)
		{
			resultImage = Image.carbon_new(control.getDisplay(), SWT.BITMAP, imageHandle[0], 0);
		}
		
		return resultImage;
	}

}
