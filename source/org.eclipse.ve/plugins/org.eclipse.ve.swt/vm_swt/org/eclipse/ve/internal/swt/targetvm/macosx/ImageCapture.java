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
 *  $Revision: 1.3 $  $Date: 2005-11-14 04:04:54 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.macosx;

import java.lang.reflect.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Image Capture for Mac OS X platforms.
 * 
 * @since 1.2.0
 */

public class ImageCapture extends org.eclipse.ve.internal.swt.targetvm.ImageCapture {
	
	private static Field shellHandleField = null;
	private static Method HIViewGetRootMethod = null;
	
	static {
		
		System.loadLibrary("swt-carbon-print");
		
		try {
			shellHandleField = Shell.class.getDeclaredField("shellHandle");
			shellHandleField.setAccessible(true);
			
			Class osClass = Class.forName("org.eclipse.swt.internal.carbon.OS"); //$NON-NLS-1$
			HIViewGetRootMethod = osClass.getMethod("HIViewGetRoot", new Class[]{int.class}); //$NON-NLS-1$

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private native int captureImage(int controlHandle, int shellHandle);

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
		
		Rectangle rect = control.getBounds();
		if (rect.width <= 0 || rect.height <= 0)
			return null;
		
//		int width = Math.min(rect.width, maxWidth);
//		int height = Math.min(rect.height, maxHeight);
		
		Image image = null;
		
		int controlHandle = -1;
		int shellHandle = -1;
		
		
		if (control instanceof Shell)
		{
			try {
				shellHandle = shellHandleField.getInt(control);
				if (shellHandle != -1)
				{
					Integer result = (Integer)HIViewGetRootMethod.invoke(null, new Object[] { new Integer(shellHandle) }); 
					if (result != null)
					{
						controlHandle = result.intValue();
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			controlHandle = control.handle;
 /*			try {
				shellHandle = shellHandleField.getInt(control.getShell());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
		int imageHandle = captureImage(controlHandle, shellHandle);
		
		if (imageHandle != 0)
		{
			image = Image.carbon_new(control.getDisplay(), SWT.BITMAP, imageHandle, 0);
		}
				
		return image;
	}
}
