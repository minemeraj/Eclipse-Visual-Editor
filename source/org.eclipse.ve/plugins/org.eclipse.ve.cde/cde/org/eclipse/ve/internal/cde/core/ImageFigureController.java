package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImageFigureController.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 16:17:52 $ 
 */

import java.util.*;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
/**
 * This is a controller for an ImageFigure that
 * is related to a IImageNotifier.
 *
 * It creates the figure, listens for image
 * changes and modifies the figure appropriately.
 *
 * This controller will listen for image changes.
 *
 * To activate it first set in the figure, and then
 * set in the imageNotifier. Setting the figure only
 * needs to be done once. But the imageNotifier
 * must be done on each activation or change in
 * imageNotifier. It is released upon deactivation.
 *
 * Note: It is very important that this controller
 * is deactivated when the editpart is deactivated.
 * This is because it listens on the image notifier
 * and we don't want the listener to hold onto
 * the notifier when it is not active.
 */

public class ImageFigureController {
	
	protected ImageFigure figure;
	protected IImageNotifier imageNotifier;
	protected Image image;	// If a modified image is in use, this will be non-null.
	private List lightenFigures;	// The GraphicalEditParts that make up the reqion to be lightened.
	private ImageData imageData;	// The current image data. Only set if we have a region to lighten.
	private byte refreshState = NO_REFRESH_PENDING;	// Flag for refresh state
	private static final byte
		NO_REFRESH_PENDING = 0x0,	// No refresh pending.
		RECREATE_IMAGE = 0x1,	// Recreate the image
		FIGURE_MOVED = 0x2;		// figure has moved, refresh if lightening
	
	// This is a private class for listening so as not to pollute the public interface of this class.
	private class Listeners implements IImageListener, FigureListener {
		public void imageChanged(ImageData data) {
			synchronized (ImageFigureController.this) {
				imageData = data;
				scheduleRefresh(RECREATE_IMAGE);
			}
		}
		
		public void figureMoved(IFigure figure) {
			scheduleRefresh(FIGURE_MOVED);
		}
	}
	
	private Listeners listener;
	
	public ImageFigureController() {
	}
	
	public void finalize() {
		// Just to be on safe side if we aren't deactivated and just thrown away.
		deactivate();
		// Our Tool Tip points to CodeGen/Editor Part
		// Finalizer hold on to the ImageFigureController that points to the Figure->ToolTip
		// Can not putthis in the dispose image, because it is called many times in the life cycle
		// of the image, in which case we will loose the ToolTip
		if (figure != null)
		   figure.setToolTip(null) ;
	}
	
	/**
	 * Return the figure the controller is managing.
	 */
	public IFigure getFigure() {
		return figure;
	}
	
	/**
	 * Set the ImageFigure that this controller is managing.
	 */
	public void setImageFigure(ImageFigure figure) {
		this.figure = figure;
	}
	
	/**
	 * Deactivate, clean up.
	 * This should be called by the editpart deactivate method.
	 */
	public void deactivate() {
		if (imageNotifier != null) {
			imageNotifier.removeImageListener(getListener());
			imageNotifier = null;
		}
		
		synchronized (this) {
			if (lightenFigures != null) {
				Iterator itr = lightenFigures.iterator();
				while (itr.hasNext()) {
					IFigure fig = (IFigure) itr.next();
					fig.removeFigureListener(getListener());
				}
				lightenFigures = null;
			}
			refreshState = NO_REFRESH_PENDING;
		}
		
		disposeImage();		
	}
	
	protected void disposeImage() {
		figure.setImage(null);
		if (image != null) {
			image.dispose();
			image = null;
		}
		imageData = null;
	}
	
	protected Listeners getListener() {
		if (listener == null)
			listener = new Listeners();
		return listener;
	}
	
	/**
	 * Set the image notifier to listen on.
	 */
	public void setImageNotifier(IImageNotifier notifier) {
		deactivate();
		if (notifier != null) {
			imageNotifier = notifier;
			imageNotifier.addImageListener(getListener());
			imageNotifier.invalidateImage();
			imageNotifier.refreshImage();
		}
	}
	
	/**
	 * Schedule a refresh. We will
	 * queue these up, but the first one that runs will
	 * actually do the refresh. The rest will do nothing.
	 * this is because many figures could change at one
	 * time.
	 */
	protected synchronized void scheduleRefresh(byte refreshType) {
		if (refreshType == FIGURE_MOVED && lightenFigures == null)
			return;	// Don't schedule anything, move w/o lighten is normal and image change is not needed.
		refreshState |= refreshType;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				synchronized (ImageFigureController.this) {
					if (refreshState != NO_REFRESH_PENDING) {
						if (lightenFigures != null) {
							refreshState = NO_REFRESH_PENDING;	// setAndLighten will recreate the image too
							setAndLightenImageData();
						}
						
						if ((refreshState&RECREATE_IMAGE) != 0) {
							refreshState = NO_REFRESH_PENDING;
							recreateImage();
						}
					}
				}
			}
			
			/**
			 * set new and Lighten ImageData into the image.
			 * Since this is within the runnable, it is within the display thread.
			 * It is also automatically synced with ImageFigureController because it is
			 * called only from the sync block within run(). 
			 */
			private void setAndLightenImageData() {
				Image newImage = null;
				if (imageData != null) {
					// We have an image to lighten
					// We want to lighten a region, so let's get the lighten image data, use an alpha of .5 and a background color of white.
					ImageData lightenImageData = null;	// Working copy, we keep the original this.imageData so that we always have the unadulterated imagedata.
					Rectangle myRect = figure.getBounds();
					int myOriginX = myRect.x;
					int myOriginY = myRect.y;
					Region region = new Region();
					try {
						Iterator itr = lightenFigures.iterator();
						while (itr.hasNext()) {
							IFigure fig = (IFigure) itr.next();
							Rectangle figRect = fig.getBounds();
							region.add(new org.eclipse.swt.graphics.Rectangle(figRect.x - myOriginX, figRect.y - myOriginY, figRect.width, figRect.height));
						}
						lightenImageData = ImageDataHelper.mixAlphaWithinRegion(imageData, region, .5, new RGB(255, 255, 255));
					} finally {
						region.dispose();
					}
			
					newImage = new Image(Display.getCurrent(), lightenImageData);
				}
				
				figure.setImage(newImage);
				if (image != null)
					image.dispose();	// Get rid of the old one.
				image = newImage;
			}
			
			/**
			 * recreate the image from the imageData.
			 * Since this is within the runnable, it is within the display thread.
			 * It is also automatically synced with ImageFigureController because it is
			 * called only from the sync block within run(). 
			 */
			private void recreateImage() {
				Image newImage = null;
				if (imageData != null)
					newImage = new Image(Display.getCurrent(), imageData);
				figure.setImage(newImage);
				if (image != null)
					image.dispose();	// Get rid of the old one.
				image = newImage;
			}

		});
	}
	
	
	/**
	 * Add a Figure to the lighten region. There area occuppied by the
	 * figure will be lightened.
	 */
	public void addLightenFigure(IFigure fig) {
		synchronized (this) {
			if (lightenFigures == null)
				lightenFigures = new ArrayList(2);
			lightenFigures.add(fig); // Add to our watched list
			if (fig != figure) {
				// If the fig is this same fig, no need to listen because we already
				// are notified of changes. Don't want double notification.
				fig.addFigureListener(getListener()); // Listen for changes
			}
		}
		if (imageNotifier != null)
			scheduleRefresh(FIGURE_MOVED); // Refresh with the new region, use Figure_moved to refresh the images.
	}
	
	/**
	 * Remove a Figure from the lighten region.
	 */
	public synchronized void removeLightenFigure(IFigure fig) {
		if (lightenFigures != null) {
			if (lightenFigures.remove(fig)) {
				// This figure was one in the list, so we need to refresh the image.
				if (fig != figure)
					fig.removeFigureListener(getListener());

				if (lightenFigures.isEmpty()) {
					// Now empty, so clean up.
					lightenFigures = null;
					scheduleRefresh(RECREATE_IMAGE);	// Recreate the image.
				} else
					scheduleRefresh(FIGURE_MOVED);	// Refresh with the new reqion, use Figure_moved to refresh the images.
			}
		}
	}	
}