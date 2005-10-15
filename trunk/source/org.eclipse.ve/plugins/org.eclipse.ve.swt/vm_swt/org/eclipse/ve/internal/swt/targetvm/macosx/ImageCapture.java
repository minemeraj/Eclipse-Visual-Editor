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
 *  $Revision: 1.1 $  $Date: 2005-10-15 03:12:38 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.macosx;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Control;

/**
 * Image Capture for Mac OS X platforms.
 * 
 * @since 1.2.0
 */

public class ImageCapture extends org.eclipse.ve.internal.swt.targetvm.ImageCapture {

	protected Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren) {
		
		Rectangle rect = control.getBounds();
		if (rect.width <= 0 || rect.height <= 0)
			return null;
		
		Image image = new Image(control.getDisplay(), Math.min(rect.width, maxWidth), Math.min(rect.height, maxHeight));
		
		GC gc = new GC(control);
		gc.copyArea(image, 0, 0);
		gc.dispose();
		
		return image;
	}

}
