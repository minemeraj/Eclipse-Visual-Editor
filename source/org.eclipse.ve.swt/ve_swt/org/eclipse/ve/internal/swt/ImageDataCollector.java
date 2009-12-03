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
 *  $RCSfile: ImageDataCollector.java,v $
 *  $Revision: 1.5 $  $Date: 2009-12-03 04:10:42 $ 
 */
package org.eclipse.ve.internal.swt;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.*;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

import org.eclipse.ve.internal.swt.common.ImageDataConstants;

/**
 * DataCollector to collect the image data from an SWT image/imagedata on a proxy and place it into an SWT ImageData.
 * 
 * There can only be one started collection at a time per instance. If this ImageDataCollector is already processing an image, then the previous one
 * will be aborted.
 */
public class ImageDataCollector implements ICallback {

	/**
	 * When starting an image, the actual completion will be at a later time in a separate thread. So this interface is used in the start methods to
	 * notify when the image has been completed.
	 */
	public interface DataCollectedRunnable {

		/**
		 * Called when successfully collected. The status will be one of the good status.
		 * 
		 * @see ImageDataConstants#IMAGE_COMPLETE
		 * @see ImageDataConstants#COMPONENT_IMAGE_CLIPPED
		 * @see ImageDataConstants#IMAGE_EMPTY
		 */
		public void imageData(ImageData data, int status);

		/**
		 * Called if unsuccessful. The status codes are in ImageDataConstants
		 */
		public void imageNotCollected(int status);

		/**
		 * Called if an Exception was thrown during image collection. The Throwable proxy is passed in.
		 */
		public void imageException(ThrowableProxy exception);
	}

	protected IBeanProxy fDataCollectorProxy; // The data collector proxy.

	protected DataCollectedRunnable fDataCollectedRunnable;

	/**
	 * Create the callback and register the collector proxy.
	 */
	public ImageDataCollector(final ProxyFactoryRegistry registry) {
		try {
			IBeanTypeProxy dataCollectorType = null;
			if (Platform.OS_WIN32.equals(Platform.getOS()))
				dataCollectorType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.win32.ImageCapture"); //$NON-NLS-1$
			else if (Platform.WS_GTK.equals(Platform.getWS())){
				if(Platform.ARCH_IA64.equals(Platform.getOSArch()) || Platform.ARCH_X86_64.equals(Platform.getOSArch()))
					dataCollectorType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.unix.bits64.ImageCapture"); //$NON-NLS-1$
				else
					dataCollectorType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.unix.ImageCapture"); //$NON-NLS-1$
			}
			else if (Platform.OS_MACOSX.equals(Platform.getOS())) {
				if (Platform.WS_COCOA.equals(Platform.getWS()))
					dataCollectorType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.macosx.cocoa.ImageCapture"); //$NON-NLS-1$
				else if (Platform.WS_CARBON.equals(Platform.getWS()))
					dataCollectorType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.macosx.ImageCapture"); //$NON-NLS-1$

			}
		
			if (dataCollectorType != null) {
				fDataCollectorProxy = dataCollectorType.newInstance(); //$NON-NLS-1$
				registry.getCallbackRegistry().registerCallback(fDataCollectorProxy, this);
			}
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
			return; // This is an invalid collector.
		}
	}

	/**
	 * Release this data collector, no longer needed.
	 */
	public void release() {
		if (fDataCollectorProxy != null && fDataCollectorProxy.isValid()) {
			if (isCollectingData()) {
				// Busy, let's stop it and wait so that we can safely clean up.
				abort(true);
			}
			fDataCollectorProxy.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(fDataCollectorProxy);
			fDataCollectorProxy.getProxyFactoryRegistry().getBeanProxyFactory().releaseProxy(fDataCollectorProxy);
		}
		fDataCollectorProxy = null;
		fDataCollectedRunnable = null;
	}

	/**
	 * Abort and wait if requested.
	 * 
	 * @param wait
	 * @return <code>true</code> if not waiting, (means abort request submitted). If waiting <code>true</code> means abort has completed.
	 *         <code>false</code> means abort was not successful.
	 * @since 1.1.0
	 */
	public boolean abort(boolean wait) {
		if (isCollectingData())
			return BeanSWTUtilities.invoke_imageCaptureAbort(fDataCollectorProxy, wait);
		else
			return true;
	}
	
	/**
	 * Are we collecting data?
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean isCollectingData() {
		return fDataCollectedRunnable != null;
	}

	/**
	 * Answer the data collector proxy.
	 */
	public IBeanProxy getDataCollectorProxy() {
		return fDataCollectorProxy;
	}

	/**
	 * Start an image collection from the control. Collects the image from the control (controlProxy must be instanceof org.eclipse.swt.widgets.Control). If
	 * collection could not be started, then call back to dc with null data. It may of not started, but this isn't an error per-se. For example there
	 * is no image to collect. In that case there will be no callbacks.
	 */
	public void startComponent(IBeanProxy aComponentProxy, DataCollectedRunnable dc) throws ThrowableProxy {
		fDataCollectedRunnable = dc;
		IBooleanBeanProxy r = BeanSWTUtilities.invoke_imageCaptureStartCapture(fDataCollectorProxy, aComponentProxy, VCEPreferences.getPlugin()
				.getPluginPreferences().getInt(VCEPreferences.MAX_AWT_COMPONENT_IMAGE_WIDTH), VCEPreferences.getPlugin().getPluginPreferences()
				.getInt(VCEPreferences.MAX_AWT_COMPONENT_IMAGE_HEIGHT), true);
		if (r == null || !r.booleanValue()) {
			dc.imageNotCollected(ImageDataConstants.IMAGE_NOT_STARTED);
			fDataCollectedRunnable = null;
		}
	}

	public Object calledBack(int msgId, IBeanProxy p2) {
		switch (msgId) {
			case ImageDataConstants.IMAGE_HAS_EXCEPTION:
				fDataCollectedRunnable.imageException((ThrowableProxy) p2);
				fDataCollectedRunnable = null;
				break;
		}
		return null;
	}

	/*
	 * This isn't called for ImageDataCollector.
	 */
	public Object calledBack(int p1, Object[] p2) {
		return null;
	}

	/*
	 * This isn't called for ImageDataCollector.
	 */
	public Object calledBack(int p1, Object p2) {
		return null;
	}

	/*
	 * This is called when the data stream is ready from the remote vm.
	 */
	public void calledBackStream(int msgID, InputStream is) {
		switch (msgID) {
			case ImageDataConstants.IMAGE_DATA_COLLECTION:
				createImageData(new DataInputStream(is));
				break;
			default:
				if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
					JavaVEPlugin.log("Invalid callback in ImageDataCollector=" + msgID, Level.WARNING); //$NON-NLS-1$
		}
	}

	private void createImageData(DataInputStream is) {
		int width = -1, height = -1, depth = -1, transparentPixel = -1;
		PaletteData palette = null;
		ImageData imageData = null;
		byte[] bytes = null;
		int[] ints = null;
		int y = 0; // Current y when processing a row through repeated commands of REPEAT and NONREPEAT.
		int rowCnt = 0; // Number of read pixels for the row.
		boolean byteMode = false; // Current cmd mode, byte/int.

		// Read in the stream.
		try {
			while (true) {
				if (fDataCollectedRunnable == null)
					break; // We terminated for some other reason.
				byte cmd = is.readByte();
				switch (cmd) {
					case ImageDataConstants.CMD_DIM:
						width = is.readInt();
						height = is.readInt();
						break;
					case ImageDataConstants.CMD_DIRECT:
						depth = is.readByte();
						int rmask = is.readInt();
						int gmask = is.readInt();
						int bmask = is.readInt();
						palette = new PaletteData(rmask, gmask, bmask);
						break;
					case ImageDataConstants.CMD_INDEXED:
						depth = is.readByte();
						transparentPixel = is.readInt();
						int rgbCnt = is.readInt();
						RGB[] rgbs = new RGB[rgbCnt];
						for (int i = 0; i < rgbCnt; i++) {
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
						if (rowCnt > 0)
							imageData.setPixels(0, y, rowCnt, bytes, 0);

						y = is.readInt();
						rowCnt = 0;
						byteMode = true;
						break;
					case ImageDataConstants.CMD_INTS:
						if (imageData == null) {
							imageData = new ImageData(width, height, depth, palette);
							if (transparentPixel != -1)
								imageData.transparentPixel = transparentPixel;
						}
						if (ints == null)
							ints = new int[width];
						if (rowCnt > 0)
							imageData.setPixels(0, y, rowCnt, ints, 0);
						y = is.readInt();
						rowCnt = 0;
						byteMode = false;
						break;
					case ImageDataConstants.CMD_NOREPEAT:
						int count = is.readInt();
						if (byteMode) {
							is.readFully(bytes, rowCnt, count);
						} else {
							count+=rowCnt;
							for (int i = rowCnt; i < count; i++)
								ints[i] = is.readInt();
						}
						rowCnt = count;
						break;
					case ImageDataConstants.CMD_REPEAT:
						count = is.readInt();
						count+=rowCnt;
						if (byteMode) {
							Arrays.fill(bytes, rowCnt, count, is.readByte());
						} else {
							Arrays.fill(ints, rowCnt, count, is.readInt());
						}
						rowCnt = count;
						break;
					case ImageDataConstants.CMD_DONE:
						// We're done for some reason.
						int status = is.readInt();
						switch (status) {
							case ImageDataConstants.IMAGE_COMPLETE:
							case ImageDataConstants.COMPONENT_IMAGE_CLIPPED:
								if (imageData == null) {
									// We got no bit data but we got a complete. So just create an empty image.
									imageData = new ImageData(width, height, depth, palette);
									if (transparentPixel != -1)
										imageData.transparentPixel = transparentPixel;
								} else {
									// Put last row into the data.
									if (rowCnt > 0)
										if (byteMode)
											imageData.setPixels(0, y, rowCnt, bytes, 0);
										else
											imageData.setPixels(0, y, rowCnt, ints, 0);
								}
								fDataCollectedRunnable.imageData(imageData, status); // It was good.
								fDataCollectedRunnable = null;
								break;
							case ImageDataConstants.IMAGE_EMPTY:
								fDataCollectedRunnable.imageData(null, status); // We have an empty.
								fDataCollectedRunnable = null;
								break;
							default:
								// Could be called twice.
								if (fDataCollectedRunnable != null) {
									// It was not good.
									fDataCollectedRunnable.imageNotCollected(status);
									fDataCollectedRunnable = null;
								}
								break;
						}
						return; // We're done, leave the loop and return the stream.
					default:
						// This shouldn't occur. We will close hard.
						throw new IOException("Invalid cmd=" + cmd); //$NON-NLS-1$
				}

			}
		} catch (EOFException e) {
			if (isCollectingData()) {
				// We reached the end but still have a runnable. This is wrong.
				fDataCollectedRunnable.imageNotCollected(ImageDataConstants.IMAGE_ERROR);
				fDataCollectedRunnable = null;
			}
		} catch (IOException e) {
			JavaVEPlugin.log(e, Level.WARNING);
			// We will close hard.
			fDataCollectedRunnable.imageNotCollected(ImageDataConstants.IMAGE_ERROR);
			fDataCollectedRunnable = null;
		}
	}
}
