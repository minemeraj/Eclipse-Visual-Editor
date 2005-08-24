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
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: CursorFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:45 $ 
 */

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

public class CursorFactory {

	static ImageDescriptor source;
	static ImageDescriptor mask;
	static {
		source = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dnd/stack_source.bmp"); //$NON-NLS-1$
		mask = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dnd/stack_mask.bmp"); //$NON-NLS-1$
	}

	/**
	 *  // Image - Mask
	 *	// 1 , 0 = transparent
	 *	// 0 , 0 = black
	 *	// 1 , 1 = black
	 *	// 0 , 1 = white
	 */
	public static Cursor createCursor(Display display, int xPos, int yPos) {

		ImageData sourceData = source.getImageData();
		ImageData maskData = mask.getImageData();
		Font smallFont = new Font(display, "Arial", 6, SWT.NORMAL); //$NON-NLS-1$
		Font normalFont = new Font(display, "Arial", 8, SWT.NORMAL); //$NON-NLS-1$

		Image image1 = new Image(display, 32, 32);
		GC gc = new GC(image1);
		String xText = new Integer(xPos).toString();
		if (gc.stringExtent(xText).x > 13) {
			gc.setFont(smallFont);
			gc.drawText(xText, 2, 19);
		} else {
			gc.setFont(normalFont);
			gc.drawText(xText, 3, 17);
		}
		gc.setFont(normalFont);
		String yText = new Integer(yPos).toString();
		if (gc.stringExtent(yText).x > 13) {
			gc.setFont(smallFont);
			gc.drawText(yText, 16, 19);
		} else {
			gc.setFont(normalFont);
			gc.drawText(yText, 17, 17);
		}
		normalFont.dispose();
		smallFont.dispose();
		gc.dispose();
		ImageData image1Data = image1.getImageData();
		// 1 is background, 0 is foreground
		// Find all foreground pixels, and for each one set the corresponing pixel white
		// which is 0 for image and 1 for mask
		for (int x = 0; x < 32; x++) {
			for (int y = 0; y < 32; y++) {
				int pixel = image1Data.getPixel(x, y);
				if (pixel == 0) {
					sourceData.setPixel(x, y, 0);
					maskData.setPixel(x, y, 0);
				}
			}
		}

		return new Cursor(display, sourceData, maskData, 0, 0);
	}
}
