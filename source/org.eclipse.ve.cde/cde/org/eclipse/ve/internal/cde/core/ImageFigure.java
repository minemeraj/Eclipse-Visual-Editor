/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: ImageFigure.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
/**
 * Figure that takes an image and will display it.
 * This figure will not own the image. It is the
 * responsibility of the caller to maintain the image.
 */
public class ImageFigure extends Figure {
	
	protected Image image;
	protected boolean useParentImageFigure = false;
	
	{
		setPreferredSize(10,10);
	}
	
	/**
	 * Set the image this figure is to use.
	 * NOTE: This figure does not own the image. It shares it. The sender
	 * still maintains ownership of the image and must dispose of it when
	 * necessary.
	 */
	public void setImage(Image image) {
		if (image == this.image)
			return;
		this.image = image;
		if (image == null)
			setPreferredSize(10,10);
		else {
			org.eclipse.swt.graphics.Rectangle bounds = image.getBounds();
			setPreferredSize(bounds.width, bounds.height);
		}
		revalidate();
		repaint();	// In case didn't change size, still need to repaint.
	}
	
	/**
	 * Return the image currently being used.
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Set to use the parent's ImageFigure as the image to use.
	 * This figure must a child or grandchild of this
	 * ImageFigure. It will paint its portion of the image
	 * (depending upon its own bounds) of that ImageFigure.
	 * This is to allow the borders to be appropriately clipped
	 * while still using the one master image.
	 */
	public void setUseParentImageFigure(boolean aBool) {
		if (this.useParentImageFigure == aBool)
			return;
		useParentImageFigure = aBool;
		image = null;
		setPreferredSize(10, 10);
		revalidate();
		repaint();	// In case didn't change size, still need to repaint.		
	}
	
	protected void paintClientArea(Graphics g) {
		if (image != null) {
			Rectangle r = getClientArea();
			g.drawImage(image, r.x, r.y);
		} else if (useParentImageFigure) {
			// Get the parent up the chain that has the actual image to use.
			ImageFigure parentImageFigure = null;
			for (IFigure parent = getParent(); parent != null; parent = parent.getParent()) {
				if (parent instanceof ImageFigure && ((ImageFigure)parent).getImage() != null) {
					parentImageFigure = (ImageFigure) parent;
					break;
				}
			}
			if (parentImageFigure != null) {
				Image parentImage = parentImageFigure.getImage();
				if (parentImage != null) {
					Rectangle r = getClientArea();
					Rectangle parentr = parentImageFigure.getClientArea();
					// Need to be careful, could be we could ask for a region outside the parent image too.
					Image pImage = parentImageFigure.getImage();					
					org.eclipse.swt.graphics.Rectangle pBounds = pImage.getBounds();
					// So set the parent size to be no larger than the image size so that we won't ask for outside of it.
					// Image bounds always have x/y of 0. And we know that (0,0) of image is the same as (x,y) of parent rectangle,
					// so only need to worry about size.
					parentr.setSize(Math.min(parentr.width, pBounds.width), Math.min(parentr.height, pBounds.height));				
					// Need to get the intersection of the client area with the 
					// parent's client area in case the parent's boundary is smaller.
					r = r.intersect(parentr);
					g.drawImage(pImage, r.x-parentr.x, r.y-parentr.y, r.width, r.height, r.x, r.y, r.width, r.height);
				}
			}
		}
		super.paintClientArea(g);
	}	
}
