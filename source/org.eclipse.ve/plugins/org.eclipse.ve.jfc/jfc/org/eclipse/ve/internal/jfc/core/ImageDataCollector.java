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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ImageDataCollector.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

import org.eclipse.ve.internal.jfc.common.ImageDataConstants;

/**
 * DataCollector to collect the image data from
 * an AWT image and place it into an SWT ImageData.
 *
 * There can only be one started collection at a time
 * per instance. If this ImageDataCollector is already 
 * processing an image, then an exception will be thrown
 * in the start call.
 */
public class ImageDataCollector implements ICallback {
	
	/**
	 * When starting an image, the actual completion will be at a later time in a separate thread.
	 * So this interface is used in the start methods to notify when the
	 * image has been completed.
	 */
	public interface DataCollectedRunnable {
		
		/**
		 * Called when started to give the result of the start.
		 * @param start status, see ImageDataConstants.
		 */
		public void imageStarted(int startStatus);
		
		/**
		 * Called when successfully collected.
		 */
		public void imageData(ImageData data);
		
		/**
		 * Called if unsuccessful. The status
		 * codes are in ImageDataConstants
		 */
		public void imageNotCollected(int status);
		
		/**
		 * Called if an Exception was thrown during
		 * image collection. The Throwable proxy is
		 * passed in.
		 */
		public void imageException(ThrowableProxy exception);
	}
	
	
	protected IBeanProxy fDataCollectorProxy;	// The data collector proxy.
	protected DataCollectedRunnable fDataCollectedRunnable;
	
	// It was aborted for some reason, so stop processing any more data that is in the pipeline and
	// just flush it out until the terminate request comes through. It will be the status to use if
	// aborted. The not-aborted status will be UNKNOWN_STATUS;
	// NOTE: This flag is under sync(this) control.
	protected int fAbortedStatus = ImageDataConstants.UNKNOWN_STATUS;	
	
	
	/**
	 * Create the callback and register the collector proxy.
	 */
	public ImageDataCollector(final ProxyFactoryRegistry registry) {
		try {
			fDataCollectorProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").newInstance(); //$NON-NLS-1$
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
			return;	// This is an invalid collector.
		}
		registry.getCallbackRegistry().registerCallback(fDataCollectorProxy, this);
		// SWT works best if we use same depth as the display.
	}
	
	/**
	 * Release this data collector, no longer needed.
	 */
	public void release() {
		if (fDataCollectorProxy != null && fDataCollectorProxy.isValid()) {
			if (isCollectingData()) {
				// Busy, let's stop it and wait so that we can safely clean up.
				abort();
				waitForCompletion();
			}
			fDataCollectorProxy.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(fDataCollectorProxy);
			fDataCollectorProxy.getProxyFactoryRegistry().getBeanProxyFactory().releaseProxy(fDataCollectorProxy);
		}
		fDataCollectorProxy = null;
	}
	
	/**
	 * Abort any current collection.
	 */
	public void abort() {
		JFCAwtConstants c = JFCAwtConstants.getConstants(fDataCollectorProxy.getProxyFactoryRegistry());
		c.getDataCollectorAbort().invokeCatchThrowableExceptions(fDataCollectorProxy);
	}
	
	/**
	 * Test if busy collecting data.
	 */
	public boolean isCollectingData() {
		JFCAwtConstants c = JFCAwtConstants.getConstants(fDataCollectorProxy.getProxyFactoryRegistry());
		return ((IBooleanBeanProxy) c.getDataCollectorBusy().invokeCatchThrowableExceptions(fDataCollectorProxy)).booleanValue();
	}

	/**
	 * Wait for completion if the image is being collected, else return immediately
	 */
	public void waitForCompletion() {
		JFCAwtConstants c = JFCAwtConstants.getConstants(fDataCollectorProxy.getProxyFactoryRegistry());
		c.getDataCollectorWait().invokeCatchThrowableExceptions(fDataCollectorProxy);
	}
				
	
	/**
	 * Answer the data collector proxy.
	 */
	public IBeanProxy getDataCollectorProxy() {
		return fDataCollectorProxy;
	}
		
	/**
	 * Start an image collection.
	 * Collects the image from the image proxy (anImageProxy must be instanceof java.awt.Image).
	 * If collection could not be started, then call back to dc with null data. It may of not started, but this isn't an
	 * error per-se. For example there is no image to collect. In that case there will be
	 * no callbacks.
	 */
	public void startImage(IBeanProxy anImageProxy, DataCollectedRunnable dc) throws ThrowableProxy {
		JFCAwtConstants c = JFCAwtConstants.getConstants(fDataCollectorProxy.getProxyFactoryRegistry());
		fDataCollectedRunnable = dc;
		IBooleanBeanProxy r = (IBooleanBeanProxy) c.getDataCollectorStartImage().invoke(fDataCollectorProxy, new IBeanProxy[] {anImageProxy});
		if (r == null || !r.booleanValue()) {
			dc.imageStarted(ImageDataConstants.IMAGE_NOT_STARTED);
			dc.imageData(null);
		}
	}
	
	/**
	 * Start an image collection from the component.
	 * Collects the image from the component (aComponentProxy must be instanceof java.awt.Component).
	 * If collection could not be started, then call back to dc with null data. It may of not started, but this isn't an
	 * error per-se. For example there is no image to collect. In that case there will be
	 * no callbacks.
	 */
	public void startComponent(IBeanProxy aComponentProxy, DataCollectedRunnable dc) throws ThrowableProxy {
		ProxyFactoryRegistry registry = fDataCollectorProxy.getProxyFactoryRegistry();
		JFCAwtConstants c = JFCAwtConstants.getConstants(registry);
		fDataCollectedRunnable = dc;
		IBeanProxy maxWidth = registry.getBeanProxyFactory().createBeanProxyWith(VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.MAX_AWT_COMPONENT_IMAGE_WIDTH));
		IBeanProxy maxHeight = registry.getBeanProxyFactory().createBeanProxyWith(VCEPreferences.getPlugin().getPluginPreferences().getInt(VCEPreferences.MAX_AWT_COMPONENT_IMAGE_HEIGHT));
		IBooleanBeanProxy r = (IBooleanBeanProxy) c.getDataCollectorStartComponent().invoke(fDataCollectorProxy, new IBeanProxy[] {aComponentProxy, maxWidth, maxHeight});
		if (r == null || !r.booleanValue()) {
			dc.imageStarted(ImageDataConstants.IMAGE_NOT_STARTED);
			dc.imageData(null);
		}
	}
	
	/**
	 * Start an image collection from the ImageProducer.
	 * Collects the image from the producer (aProducerProxy must be instanceof java.awt.image.ImageProducer).
	 * If collection could not be started, then call back to dc with null data. It may of not started, but this isn't an
	 * error per-se. For example there is no image to collect. In that case there will be
	 * no callbacks.
	 */
	public void startProducer(IBeanProxy aProducerProxy, DataCollectedRunnable dc) throws ThrowableProxy {
		JFCAwtConstants c = JFCAwtConstants.getConstants(fDataCollectorProxy.getProxyFactoryRegistry());
		fDataCollectedRunnable = dc;		
		IBooleanBeanProxy r = (IBooleanBeanProxy) c.getDataCollectorStartProducer().invoke(fDataCollectorProxy, new IBeanProxy[] {aProducerProxy});
		if (r == null || !r.booleanValue()) {
			dc.imageStarted(ImageDataConstants.IMAGE_NOT_STARTED);
			dc.imageData(null);
		}
	}
	
	
	/*
	 * This is called when the preferred depth is being requested from the remote vm.
	 */ 
	public Object calledBack(int msgId, IBeanProxy p2){
		switch (msgId) {
			case ImageDataConstants.IMAGE_GET_PREFERRED_DEPTH:
				final int[] depth = new int[1];
				Display dsp = Display.getCurrent();
				if (dsp == null)
					dsp = Display.getDefault();
				dsp.syncExec(new Runnable() {
					public void run() {
						// This method can only be called from display thread
						depth[0] = Display.getCurrent().getDepth();
					}
				});
				
				return new Integer(depth[0]);
			case ImageDataConstants.IMAGE_HAS_BEEN_ABORTED:
				synchronized (this) {
					fAbortedStatus = ((IIntegerBeanProxy) p2).intValue();
				}
				break;
			case ImageDataConstants.IMAGE_HAS_STARTED_CALLBACK:
				fDataCollectedRunnable.imageStarted(((IIntegerBeanProxy) p2).intValue());
				break;
			case ImageDataConstants.IMAGE_HAS_EXCEPTION:
				fDataCollectedRunnable.imageException((ThrowableProxy) p2);
				break;
		}
		return null;
	}
	
	/*
	 * This isn't called for ImageDataCollector.
	 */ 
	public Object calledBack(int p1, Object[] p2){
		return null;
	}

	/*
	 * This isn't called for ImageDataCollector.
	 */ 
	public Object calledBack(int p1, Object p2){
		return null;
	}
	
	/*
	 * This is called when the data stream is ready
	 * from the remote vm.
	 */
	public void calledBackStream(int msgID, InputStream is) {
		switch (msgID) {
			case ImageDataConstants.IMAGE_DATA_COLLECTION:
				createImageData(new DataInputStream(is));
				break;
			default:
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
					JavaVEPlugin.log("Invalid callback in ImageDataCollector="+msgID, Level.WARNING);	//$NON-NLS-1$
		}
	}
	
	protected void createImageData(DataInputStream is) {
		int width = -1, height = -1, depth = -1, transparentPixel = -1;
		PaletteData palette = null;
		ImageData imageData = null;
		byte[] bytes = null;
		int[] ints = null;
		int x=0, y=0;	// Current x and y when processing a row through repeated commands of REPEAT and NONREPEAT.
		boolean byteMode = false;	// Current cmd mode, byte/int.
		
		int abortStatus = ImageDataConstants.UNKNOWN_STATUS;		
		synchronized (this) {
			fAbortedStatus = ImageDataConstants.UNKNOWN_STATUS;
		}
		// Read in the stream.
		try {
			while (true) {
				byte cmd = is.readByte();
				synchronized(this) {
					if (fAbortedStatus != ImageDataConstants.UNKNOWN_STATUS) {
						abortStatus = fAbortedStatus;
						fAbortedStatus = ImageDataConstants.UNKNOWN_STATUS;
					}
				}
				if (abortStatus != ImageDataConstants.UNKNOWN_STATUS) {
					// An abort was requested, just send the aborted status to the runnable and return because we don't care about the rest.
					fDataCollectedRunnable.imageNotCollected(abortStatus);
					break;
				}
				switch (cmd) {
					case ImageDataConstants.CMD_DIM:
						width = is.readInt();
						height = is.readInt();
						break;
					case ImageDataConstants.CMD_DIRECT:
						depth = is.readByte();
						if (depth == 16)
							palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
						else {
							// Currently only handle 16 and 24 direct. (32 bit has a bug in OTI).
							palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
						}						
						if (imageData != null && !imageData.palette.isDirect) {
							imageData = convertToDirect(depth, imageData, palette);
							bytes = null;
							ints = new int[imageData.width];	// Because we will send at most one row at a time.
						}
						break;
					case ImageDataConstants.CMD_INDEXED:
						depth = is.readByte();
						transparentPixel = is.readInt();
						int rgbCnt = is.readInt();
						RGB[] rgbs = new RGB[rgbCnt];
						for (int i=0; i<rgbCnt; i++) {
							// The bytes need to be unsigned, but java doesn't have that, so we need to do it
							// RGB takes ints range 0-255, not -128 to 127
							rgbs[i] = new RGB(is.readByte() & 0x000000ff, is.readByte() & 0x000000ff, is.readByte() & 0x000000ff);
						}
						palette = new PaletteData(rgbs);
						break;
					case ImageDataConstants.CMD_BYTES:
						if (imageData == null) {
							imageData = new ImageData(width, height, depth, palette);
							if (transparentPixel != -1)
								imageData.transparentPixel = transparentPixel;
						}
						if (bytes == null)
							bytes = new byte[width];
						x = is.readInt();
						y = is.readInt();
						byteMode = true;
						break;
					case ImageDataConstants.CMD_INTS:
						if (imageData == null)
							imageData = new ImageData(width, height, depth, palette);
						if (ints == null)
							ints = new int[width];
						x = is.readInt();
						y = is.readInt();
						byteMode = false;
						break;
					case ImageDataConstants.CMD_NOREPEAT:
						int count = is.readInt();
						if (byteMode) {
							is.readFully(bytes, 0, count);
							imageData.setPixels(x, y, count, bytes, 0);						
						} else {
							for (int i=0; i<count; i++)
								ints[i] = is.readInt();
							imageData.setPixels(x, y, count, ints, 0);
						}
						x+=count;
						break;
					case ImageDataConstants.CMD_REPEAT:
						count = is.readInt();
						if (byteMode) {
							Arrays.fill(bytes, 0, count, is.readByte());
							imageData.setPixels(x, y, count, bytes, 0);							
						} else {
							Arrays.fill(ints, 0, count, is.readInt());
							imageData.setPixels(x, y, count, ints, 0);
						}
						x+=count;
						break;
					case ImageDataConstants.CMD_DONE:
						// We're done for some reason.
						int status = is.readInt();
						switch (status) {
							case ImageDataConstants.SINGLE_FRAME_DONE:
							case ImageDataConstants.STATIC_IMAGE_DONE:
								if (imageData == null) {
									// We got no bit data but we got a complete. So just create an empty image.							
									imageData = new ImageData(width, height, depth, palette);
									if (transparentPixel != -1)
										imageData.transparentPixel = transparentPixel;
								}																
								fDataCollectedRunnable.imageData(imageData);	// It was good.
								break;
							case ImageDataConstants.IMAGE_EMPTY:
								fDataCollectedRunnable.imageData(null);	// We have an empty.
								break;
							default:
								// It was not good.
								fDataCollectedRunnable.imageNotCollected(status);
								break;
						}						
						return;	// We're done, leave the loop and return the stream.
					default:
						// This shouldn't occur. We will close hard.
						throw new IOException("Invalid cmd="+cmd);	//$NON-NLS-1$
				}
							
			}
		} catch (EOFException e) {
				synchronized(this) {
					if (fAbortedStatus != ImageDataConstants.UNKNOWN_STATUS) {
						abortStatus = fAbortedStatus;
						fAbortedStatus = ImageDataConstants.UNKNOWN_STATUS;
					}
				}
				if (abortStatus != ImageDataConstants.UNKNOWN_STATUS) {
					// An abort was requested and we got an EOF, just send the aborted status to the runnable and return because we don't care about the rest.
					fDataCollectedRunnable.imageNotCollected(abortStatus);
				} else {
					// We ended for some other reason, report the error.
					JavaVEPlugin.log(e, Level.FINE);
					// We will close hard.
					fDataCollectedRunnable.imageNotCollected(ImageDataConstants.IMAGE_ERROR);
				}
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.WARNING);
			// We will close hard.
			fDataCollectedRunnable.imageNotCollected(ImageDataConstants.IMAGE_ERROR);
		}
	}
	
	protected ImageData convertToDirect(int depth, ImageData oldImageData, PaletteData palette) {
		// Procedure is to get the old data, row by row into an int array. Then use
		// the old color model to take the int's and convert them to RGB and place them
		// into the new imageData.
		// We have something to convert
		ImageData newImageData = new ImageData(oldImageData.width, oldImageData.height, depth, palette);
		// Convert each old pixel into RGB for setting into the data. 
		int[] pixelRow = new int[oldImageData.width];
		RGB[] oldColorModel = oldImageData.palette.getRGBs();
		for (int row = 0; row < oldImageData.height; row++) {
			oldImageData.getPixels(0, row, oldImageData.width, pixelRow, 0); // Get one row from old
			for (int col = 0; col < oldImageData.width; col++) {
				int pixel = pixelRow[col];
				int red = oldColorModel[pixel].red;
				int green = oldColorModel[pixel].green;
				int blue = oldColorModel[pixel].blue;

				if (depth == 16) {
					// They are stored in reverse-byte order as if stored in an integer of RGB in the imageData.					
					// The RGB's are stored as 256 range, so we need to shift down to 5 bits.
					red = (red >> 3) & 0x1F;
					green = (green >> 3) & 0x1F;
					blue = (blue >> 3) & 0x1F;
					pixelRow[col] = (red << 10) | (green << 5) | blue;
				} else {
					pixelRow[col] = (blue & 0xFF) << 16 | (green & 0xFF) << 8 | (red & 0xFF);
				}
			}
			newImageData.setPixels(0, row, newImageData.width, pixelRow, 0);
		}
		
		return newImageData;
	}		
	
}
