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
 *  $RCSfile: ImageOverlay.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-15 18:51:51 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

public class ImageOverlay {
	
	public ImageOverlay(ImageData anImageData){
		this(anImageData,anImageData.width,anImageData.height);
	}
	
	public ImageOverlay(ImageData anImageData, int x, int y) {
		imageData = anImageData;
		location = new Point(x,y);
	}
	public ImageData imageData;
	public Point location;

}
