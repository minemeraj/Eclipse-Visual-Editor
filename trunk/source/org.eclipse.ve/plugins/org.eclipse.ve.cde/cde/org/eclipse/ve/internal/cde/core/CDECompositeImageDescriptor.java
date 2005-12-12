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
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;


/**
 * A CompositeImageDescriptor that takes {@link ImageOverlay} objects to overlay the base image.
 * 
 * @since 1.2.0
 */
public class CDECompositeImageDescriptor extends CompositeImageDescriptor {
	
	private ImageData fBaseImage;
	private List imageOverlays;

	/**
	 * Construct with the base image.
	 * @param baseImage
	 * 
	 * @since 1.2.0
	 */
	public CDECompositeImageDescriptor(ImageData baseImage){
		fBaseImage = baseImage;
	}

	protected void drawCompositeImage(int width, int height) {
		drawImage(fBaseImage, 0, 0);
		Point size = getSize();
		
		if(imageOverlays != null){
			Iterator iter = imageOverlays.iterator();
			ImageOverlay overlay = (ImageOverlay)iter.next();
			Point location = overlay.location;
			drawImage(overlay.imageData, size.x - location.x, size.y - location.y);
		}
	}
	
	public void addOverlay(ImageOverlay anOverlay){
		if (anOverlay != null) {
			if (imageOverlays == null) {
				imageOverlays = new ArrayList(1);
			}
			imageOverlays.add(anOverlay);
		}
	}

	protected Point getSize() {
		return new Point(fBaseImage.width, fBaseImage.height);
	}

}
