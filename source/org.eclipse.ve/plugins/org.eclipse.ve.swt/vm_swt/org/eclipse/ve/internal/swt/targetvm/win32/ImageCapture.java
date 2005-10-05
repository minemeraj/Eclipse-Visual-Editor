/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm.win32;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.widgets.*;

/**
 * Image Capture for Win32 platforms.
 * 
 * @since 1.1.0
 */
public class ImageCapture extends org.eclipse.ve.internal.swt.targetvm.ImageCapture {

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
		Image myImage = getImage(control, maxWidth, maxHeight);
		if (myImage != null) {
			// Get the images of all of the children
			if (includeChildren && control instanceof Composite) {
				Rectangle parentBounds = myImage.getBounds();
				Point clientOrigin = getClientOrigin(control);
				Control[] children = ((Composite) control).getChildren();
				GC myImageGC = new GC(myImage);
				try {
					int i = children.length;
					while (--i >= 0) {
						Control child = children[i];
						// If the child is not visible then don't try and get its image
						// An example of where this would cause a problem is TabFolder where all the controls
						// for each page are children of the TabFolder, but only the visible one is being shown on the active page
						if (!child.isVisible())
							continue;
						Rectangle childBounds = child.getBounds();
						childBounds.x += clientOrigin.x; // Adjust to true loc within the parent.
						childBounds.y += clientOrigin.y;
						if (!parentBounds.intersects(childBounds))
							continue; // Child is completely outside parent.
						Image childImage = getImage(child, parentBounds.width - childBounds.x, parentBounds.height - childBounds.y, true);
						if (childImage != null) {
							try {
								// Paint the child image on top of our one
								// Its location is within our client area origin, so if it is at 10,10 and our client origin
								// is at 5,5 then we draw at 15,15 on our image
								myImageGC.drawImage(childImage, childBounds.x, childBounds.y);
							} finally {
								childImage.dispose();
							}
						}
					}
				} finally {
					myImageGC.dispose();
				}
			}
		}
		return myImage;
	}

	/**
	 * Return the image of the argument. This includes the client and non-client area, but does not include any child controls. To get child control
	 * use {@link ImageCapture#getImage(Control, int, int, boolean)}.
	 * 
	 * @param aControl
	 * @param maxWidth
	 * @param maxHeight
	 * @return image or <code>null</code> if not valid for some reason. (Like not yet sized).
	 * 
	 * @since 1.1.0
	 */
	protected Image getImage(Control aControl, int maxWidth, int maxHeight) {

		Rectangle rect = aControl.getBounds();
		if (rect.width <= 0 || rect.height <= 0)
			return null;

		Image image = new Image(aControl.getDisplay(), Math.min(rect.width, maxWidth), Math.min(rect.height, maxHeight));
		int WM_PRINT = 0x0317;
		// int WM_PRINTCLIENT = 0x0318;
		// int PRF_CHECKVISIBLE = 0x00000001;
		int PRF_NONCLIENT = 0x00000002;
		int PRF_CLIENT = 0x00000004;
		int PRF_ERASEBKGND = 0x00000008;
		int PRF_CHILDREN = 0x00000010;
		// int PRF_OWNED = 0x00000020;
		int print_bits = PRF_NONCLIENT | PRF_CLIENT | PRF_ERASEBKGND;
		// This method does not print immediate children because the z-order doesn't work correctly and needs to be
		// dealt with separately, however Table's TableColumn widgets are children so must be handled differently
		boolean specialClass = aControl instanceof Table || aControl instanceof Browser || aControl instanceof OleFrame || aControl instanceof CCombo;
		try {
			specialClass |= aControl instanceof Spinner;
		} catch (NoClassDefFoundError e) {
		} // might not be on 3.1 of SWT
		if (specialClass) {
			print_bits |= PRF_CHILDREN;
		}
		GC gc = new GC(image);
		
		// Need to handle cases where the GC font isn't automatically set by the control's image (e.g. CLabel)
		// see bug 98830 (https://bugs.eclipse.org/bugs/show_bug.cgi?id=98830)
		Font f = aControl.getFont();
		if (f != null)
			gc.setFont(f);
		
		int hwnd = aControl.handle;
		if (aControl instanceof Tree) {
			int hwndParent = OS.GetParent(hwnd);
			if (hwndParent != aControl.getParent().handle) {
				hwnd = hwndParent;
				print_bits |= PRF_CHILDREN;		
			}
		}
		OS.SendMessage(hwnd, WM_PRINT, gc.handle, print_bits);

		gc.dispose();
		return image;
	}

	/**
	 * Return the location of the controls origin (its (0,0)) wrt/its upper left corner. On most this will be (0,0), but on shell it isn't since (0,0)
	 * on shell actually puts it down and to the left.
	 * 
	 * @param control
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static Point getClientOrigin(Control control) {
		Point controlOrigin = control.toDisplay(0, 0); // Display coor of where the control's (0,0) is.
		Composite parent = control.getParent();
		Point controlCorner = parent != null ? parent.toDisplay(control.getLocation()) : control.getLocation(); // Display coor of control's
																												// upper-left.
		controlOrigin.x -= controlCorner.x;
		controlOrigin.y -= controlCorner.y;
		return controlOrigin;
	}

}
