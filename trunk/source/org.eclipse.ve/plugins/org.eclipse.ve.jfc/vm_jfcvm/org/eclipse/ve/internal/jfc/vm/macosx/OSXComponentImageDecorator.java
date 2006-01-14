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
package org.eclipse.ve.internal.jfc.vm.macosx;

/*
 *  $RCSfile: OSXComponentImageDecorator.java,v $
 *  $Revision: 1.1 $  $Date: 2006-01-14 23:34:05 $ 
 */

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * OSXComponentImageDecorator adds Mac OS X specific decorations to the captured images of JFC Components.
 * The primary purpose of this class is to emulate the OS X title bar on Frames and Dialogs that fails to be
 * captured by Component.printAll() on OS X (See bug 120279).
 *
 */
public class OSXComponentImageDecorator {
		
	/*
	 * Cache of DecorationInfo objects
	 * Map is Component -> DecorationInfo
	 */
	private static HashMap decorationInfoCache = null;
	
	/* 
	 * Cache of title bar images
	 * Map is Component -> Image
	 */
	private static HashMap titleBarImageCache = null;
	
	// Title bar image components
	
	// Left border of the title bar, along with the red close button
	private static Image leftRedImage = null;
	
	// Yellow minimize button
	private static Image yellowImage = null;
	
	// Green maximize button
	private static Image greenImage = null;
	
	// Disabled button
	private static Image grayImage = null;
	
	// Image of the blank middle of the title bar that will be streched to the size of the window
	private static Image middleImage = null;
	
	// Right border of the title bar
	private static Image rightImage = null;
	
	// Resize handle shown in the bottom right of a resizable window
	private static Image resizeImage = null;
	
	// Cached sizes of the image files used in drawing
	private static int leftWidth;
	private static int buttonWidth;
	private static int rightWidth;
	private static int resizeWidth;
	private static int resizeHeight;
	private static int titleHeight;
	
	// Font for the title bar text
	private static Font titleBarFont = null;
	
	// Flag to indicate a problem initializing the decorator class (usually failure to load the images)
	private static boolean initializationFailed = false;
	
	// Static init block loads the decoration images
	static {
		try {			
			// Use reflection to call ImageIO.read(InputStream) so that it will fall gracefully on
			// JDK's < 1.4
			Class imageIOClass = Class.forName("javax.imageio.ImageIO"); //$NON-NLS-1$
			Method readMethod = imageIOClass.getMethod("read", new Class[] { InputStream.class }); //$NON-NLS-1$
			if (readMethod != null) {
				Class c = OSXComponentImageDecorator.class;
				InputStream is = c.getResourceAsStream("left-red.png"); //$NON-NLS-1$
				leftRedImage = (Image)readMethod.invoke(null, new Object[] { is });
				is = c.getResourceAsStream("yellow.png"); //$NON-NLS-1$
				yellowImage = (Image)readMethod.invoke(null, new Object[] { is });
				is = c.getResourceAsStream("green.png"); //$NON-NLS-1$
				greenImage = (Image)readMethod.invoke(null, new Object[] { is });
				is = c.getResourceAsStream("gray.png"); //$NON-NLS-1$
				grayImage = (Image)readMethod.invoke(null, new Object[] { is });
				is = c.getResourceAsStream("right.png"); //$NON-NLS-1$
				rightImage = (Image)readMethod.invoke(null, new Object[] { is });
				is = c.getResourceAsStream("middle.png"); //$NON-NLS-1$
				middleImage = (Image)readMethod.invoke(null, new Object[] { is });
				is = c.getResourceAsStream("resize.png"); //$NON-NLS-1$
				resizeImage = (Image)readMethod.invoke(null, new Object[] { is });
				
				leftWidth = leftRedImage.getWidth(null);
				buttonWidth = grayImage.getWidth(null);
				rightWidth = rightImage.getWidth(null);
				titleHeight = leftRedImage.getHeight(null);
				
				resizeWidth = resizeImage.getWidth(null);
				resizeHeight = resizeImage.getHeight(null);
				
				titleBarImageCache = new HashMap();
				decorationInfoCache = new HashMap();
			} else {
				initializationFailed = true;
			}
			
			// Initialize title bar font
			// As defined here: 
			// http://developer.apple.com/documentation/UserExperience/Conceptual/OSXHIGuidelines/XHIGText/chapter_13_section_2.html
			titleBarFont = new Font("Lucida Grande", Font.PLAIN, 13); //$NON-NLS-1$
			
			if (titleBarFont == null)
			{
				// Fall back on the default font
				titleBarFont = new Font(null, Font.PLAIN, 13);
			}
			
		} catch (Exception e) {
			// Problem loading images, or some other problem.  Fail gracefully
			initializationFailed = true;
		}
	}
	
	/**
	 * Add Mac OS X specific decorations to the component image of the given component.
	 * 
	 * @param component the component to decorate
	 * @param componentImage the captured image of the component
	 * @param imageWidth the width of the captured image
	 * @param imageHeight the height of the captured image
	 */
	public static void decorateComponent(Component component, Image componentImage, int imageWidth, int imageHeight) {
		// If there was a problem initializing the Decorator class, jump out immediately
		if (initializationFailed) {
			return;
		}
		
		// Check to see if the component is one of the types we can decorate
		if (!(component instanceof Frame || component instanceof Dialog)) {
			return;
		}
		
		// Gather the decorations info for the given component
		DecorationInfo info = getDecorationInfo(component);
		
		// Check whether the component is marked undecorated
		if (info.isUndecorated) {
			return;
		}
		
		// Check to see whether there's already cached images for this component, and whether
		// any of the decorated properties have changed on the component
		boolean needsNewImage = !matchesCachedDecorations(component, info);
		
		Image titleBarImage = null;
		if (needsNewImage)
		{
			// Create a new title bar image
			titleBarImage = makeTitleBarImage(info, component.getColorModel(), imageWidth);
			if (titleBarImage != null) {
				// Put the title bar image into the cache
				titleBarImageCache.put(component, titleBarImage);
				decorationInfoCache.put(component, info);
			}
		} else {
			// Get the title bar image from the cache
			titleBarImage = (Image)titleBarImageCache.get(component);
		}
		
		if (titleBarImage != null) {
			// Draw the title bar image
			Graphics graphics = componentImage.getGraphics();
			graphics.drawImage(titleBarImage, 0, 0, null);
			
			// Draw the resize pull in the bottom right
			if (info.isResizable) {
				int x = imageWidth - resizeWidth;
				int y = imageHeight - resizeHeight;
				graphics.drawImage(resizeImage, x, y, null);
			}
		}
	}
	
	/**
	 * Clear out this Decorator's caches.
	 *
	 */
	public static void clearCaches() {
		synchronized(decorationInfoCache) {
			decorationInfoCache.clear();	
		}
		synchronized(titleBarImageCache) {
			titleBarImageCache.clear();	
		}
	}
	
	/**
	 * Make a title bar image with the Component's decoration info
	 * 
	 * @param info the decoration info for the target Component
	 * @param cm color model of the Component
	 * @param width width of the title bar image to create
	 * @return the image, null if a problem occurred making the image
	 */
	private static Image makeTitleBarImage(DecorationInfo info, ColorModel cm, int width) {
		
		Image image = new BufferedImage(cm, cm.createCompatibleWritableRaster(width, titleHeight), cm.isAlphaPremultiplied(), null);
		
		// Ensure that we have Graphics2D to work with
		if (!(image.getGraphics() instanceof Graphics2D))
			return null;
		
		Graphics2D graphics = (Graphics2D)image.getGraphics();
		
		// Start x of the bar's middle section 
		int innerX = leftWidth + (buttonWidth * 2);
		// Width of the bar's middle section
		int innerWidth = width - innerX - rightWidth;
		if (innerWidth <= 0)
			innerWidth = 1;
		
		int x = 0;
		
		// Draw left edge and close button
		graphics.drawImage(leftRedImage, x, 0, null);
		x += leftWidth;
		
		// Draw the minimizable button
		if (info.isMinimizable)
		{
			graphics.drawImage(yellowImage, x, 0, null);
		}
		else
		{
			graphics.drawImage(grayImage, x, 0, null);
		}
		x += buttonWidth;
		
		// Draw the resizable button
		if (info.isResizable)
		{
			graphics.drawImage(greenImage, x, 0, null);
		}
		else
		{
			graphics.drawImage(grayImage, x, 0, null);
		}
		x += buttonWidth;
		
		// Draw middle section
		Image scaledMiddle = middleImage.getScaledInstance(innerWidth, titleHeight, Image.SCALE_DEFAULT);
		graphics.drawImage(scaledMiddle, x, 0, null);
		
		// Draw right edge
		graphics.drawImage(rightImage, width - rightWidth, 0, null);
		
		// Add title text
		if (info.title != null && info.title.length() > 0) {			
			graphics.setColor(SystemColor.activeCaptionText);
			graphics.setFont(titleBarFont);
			Rectangle2D fontBounds = titleBarFont.getStringBounds(info.title, graphics.getFontRenderContext());
			int fontX = (int)((width / 2) - fontBounds.getCenterX());
			// Prevent the text from drawing over the buttons
			fontX = Math.max(fontX, innerX);
			int fontY = (int)Math.ceil((double)titleHeight / 2 - fontBounds.getCenterY());
			
			// clip the text to the inner region
			graphics.setClip(innerX, 0, innerWidth, titleHeight);
			graphics.drawString(info.title, fontX, fontY);
		}
		
		return image;
	}
	
	/**
	 * Initializes a new DecorationInfo object for the given Component
	 * @param component the Component
	 * @return the Component's DecorationInfo
	 */
	private static DecorationInfo getDecorationInfo(Component component) {
		DecorationInfo info = new DecorationInfo();
		info.size = component.getSize();
		
		if (component instanceof Frame)
		{
			Frame frame = (Frame)component;
			info.title = frame.getTitle();
			info.isUndecorated = frame.isUndecorated();
			info.isResizable = frame.isResizable();
			info.isMinimizable = true;
		} else if (component instanceof Dialog) {
			Dialog dialog = (Dialog)component;
			info.title = dialog.getTitle();
			info.isUndecorated = dialog.isUndecorated();
			info.isResizable = dialog.isResizable();
			info.isMinimizable = false;
		}
		return info;
	}
	
	/**
	 * Check whether the given DecorationInfo matches the info for the given Component that's 
	 * already in the cache.
	 *
	 * @param component the component to check
	 * @param newInfo the component's info object
	 * @return true if the cached info matches the given info, false if the cache is out of date
	 */
	private static boolean matchesCachedDecorations(Component component, DecorationInfo newInfo)
	{
		DecorationInfo oldInfo = (DecorationInfo)decorationInfoCache.get(component);
		
		return (oldInfo != null && oldInfo.equals(newInfo));
	}
	
	private static class DecorationInfo {
		public String title = null;
		public Dimension size = null;
		public boolean isUndecorated = false;
		public boolean isResizable = true;
		public boolean isMinimizable = true;
		
		public boolean equals(Object o) {
			if (o == null || !(o instanceof DecorationInfo))
				return false;
			
			DecorationInfo i = (DecorationInfo)o;
			
			boolean matches = true;
			
			if (title == null) {
				matches = (i.title == null);
			} else {
				matches = i.title != null && title.equals(i.title);
			}
			
			if (size == null) {
				matches = matches && (i.size == null);
			} else {
				matches = matches && i.size != null && size.equals(i.size);
			}
			
			matches = matches &&
					(isUndecorated == i.isUndecorated) && 
					(isResizable == i.isResizable) &&
					(isMinimizable == i.isMinimizable);
			
			return matches;
		}
	}

}
