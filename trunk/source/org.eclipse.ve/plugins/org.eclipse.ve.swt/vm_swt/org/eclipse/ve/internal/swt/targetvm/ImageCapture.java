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
 *  $Revision: 1.8 $  $Date: 2010-05-14 14:31:53 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jem.internal.proxy.common.*;

import org.eclipse.ve.internal.swt.common.ImageDataConstants;
 

/**
 * Capture and transmit the image back. There are platform specific subclasses
 * to handle the grab of the image from a control. 
 * 
 * @since 1.1.0
 */
public abstract class ImageCapture implements ICallback {
	
	protected IVMCallbackServer vmServer;
	protected int callbackID;
	protected DataOutputStream os;

	public void initializeCallback(IVMCallbackServer vmServer, int callbackID) {
		this.vmServer = vmServer;
		this.callbackID = callbackID;
	}
	
	/**
	 * Grab the image for the given control.
	 * @param control
	 * @param maxWidth
	 * @param maxHeighth
	 * @param includeChildren
	 * @return image. Do not return null except if the image is empty for some reason. If there was an error, throw RuntimeException with a msg. If it was caused by some non-runtime exception, then
	 * use new RuntimeException(exception). We use that as flag to actually return the real exception.
	 * 
	 * @since 1.1.0
	 */
	protected abstract Image getImage(Control control, int maxWidth, int maxHeight, boolean includeChildren);
	
	// The current status. Access to this must be through sync(this).
	private int status = ImageDataConstants.NOT_IN_PROGRESS;
	
	/**
	 * Get the current status.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected synchronized int getStatus() {
		return status;
	}
	
	/**
	 * Set the status.
	 * @param status
	 * 
	 * @since 1.1.0
	 */
	protected synchronized void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Start the capture of the image. This will not wait for the capture to complete but
	 * return immediately. The actual capture will occur on another thread. If there
	 * is a capture already in progress this return false and no capture will occur.
	 * @param control
	 * @param maxWidth
	 * @param maxHeighth
	 * @param abortAndWait <code>true</code> if should abort a current one and wait for it to finish. <code>false</code> don't if busy, just return.
	 * @return <code>true</code> if capture proceeded, <code>false</code> if busy and not asked to wait or if wait timed out or if control is invalid.
	 * @since 1.1.0
	 */
	public boolean captureImage(final Control control, final int maxWidth, final int maxHeight, boolean abortAndWait) {
		if (control.isDisposed())
			return false;
		synchronized (this) {
			if (abortAndWait) {
				if (!abortImage(true))
					return false;
			} else if (status != ImageDataConstants.NOT_IN_PROGRESS)
				return false;
			
			try {
				os = new DataOutputStream(vmServer.requestStream(callbackID, ImageDataConstants.IMAGE_DATA_COLLECTION));
			} catch (CommandException e1) {
				e1.printStackTrace();
				return false;
			}
			
			status = ImageDataConstants.IN_PROGRESS;
			
			try {
				control.getDisplay().asyncExec(new Runnable() {

					public void run() {
						if (testForAbort())
							return;

						try {
							Point size = control.getSize();
							if (size.x == 0 || size.y == 0) {
								imageComplete(ImageDataConstants.IMAGE_EMPTY);
								return;
							}
							boolean clipped = false;
							if (size.x > maxWidth) {
								clipped = true;
								size.x = maxWidth;
							}
							if (size.y > maxHeight) {
								clipped = true;
								size.y = maxHeight;
							}

							if (clipped)
								setStatus(ImageDataConstants.COMPONENT_IMAGE_CLIPPED);

							Image image = getImage(control, size.x, size.y, true);
							if (image != null) {
								ImageData imageData = image.getImageData();
								image.dispose();
								startProcessingThread(imageData);
							} else
								imageComplete(ImageDataConstants.IMAGE_EMPTY);
						} catch (Throwable e) {
							processTerminatingException(e);
							if (e instanceof Error)
								throw (Error) e; // Make sure it goes on out
						}
					}
				});
			} catch (SWTException e) {
				processTerminatingException(e);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Terminating due to an exception. 
	 * @param e
	 * 
	 * @since 1.1.0
	 */
	private void processTerminatingException(final Throwable e) {
		e.printStackTrace();
		try {
			vmServer.doCallback(new ICallbackRunnable() {
				public Object run(ICallbackHandler handler) throws CommandException {
					return handler.callbackWithParms(callbackID, ImageDataConstants.IMAGE_HAS_EXCEPTION, new Object[] {e});
				}
			});
		} catch (CommandException e1) {
			e1.printStackTrace();
		}						
		imageComplete(ImageDataConstants.IMAGE_ERROR);
	}
	
	private void sendPalette(ImageData imageData) { 
		if (imageData.palette.isDirect) {
			try {
				writeByte(ImageDataConstants.CMD_DIRECT);
				writeByte((byte) imageData.depth);
				writeInt(imageData.palette.redMask);
				writeInt(imageData.palette.greenMask);
				writeInt(imageData.palette.blueMask);
			} catch (IOException e) {
				processTerminatingException(e);
				return;
			}
		} else {
			try {
				writeByte(ImageDataConstants.CMD_INDEXED);
				writeByte((byte) imageData.depth);
				writeInt(imageData.transparentPixel);
				RGB[] rgbs = imageData.palette.getRGBs();
				writeInt(rgbs.length);
				for (int i = 0; i < rgbs.length; i++) {
					RGB rgb = rgbs[i];
					writeByte(rgb.red);
					writeByte(rgb.green);
					writeByte(rgb.blue);
				}
			} catch (IOException e) {
				processTerminatingException(e);
				return;
			}

		}
	}
	
	
	/**
	 * Start the thread that actually takes the image data ships to the client.
	 * @param imageData
	 * 
	 * @since 1.1.0
	 */
	private void startProcessingThread(final ImageData imageData) {
		if (testForAbort())
			return;
		Thread t = new Thread() {
			public void run() {
				if (testForAbort())
					return;
				try {
					// Write the dimensions, the palette, and then the data.
					writeByte(ImageDataConstants.CMD_DIM);
					writeInt(imageData.width);
					writeInt(imageData.height);

					// Send the palette
					sendPalette(imageData);
					if (imageData.depth <= 8)
						sendByteData(imageData);
					else
						sendIntData(imageData);
					if (testForAbort())
						return;
					int lstatus;
					synchronized (ImageCapture.this) {
						if (status == ImageDataConstants.IN_PROGRESS)
							lstatus = ImageDataConstants.IMAGE_COMPLETE;
						else
							lstatus = status;
					}
					imageComplete(lstatus);
				} catch (IOException e) {
					processTerminatingException(e);
					return;					
				} finally {
					boolean notcomplete;
					synchronized (ImageCapture.this) {
						notcomplete = status != ImageDataConstants.NOT_IN_PROGRESS;
					}
					if (notcomplete)
						imageComplete(ImageDataConstants.IMAGE_ERROR);	// For some reason it did not complete.
				}
				
			}
		};
		t.start();
	}

	private synchronized void writeByte(int b) throws IOException {
		if (os != null) {
			os.writeByte(b);
		}
	}

	private synchronized void writeInt(int i) throws IOException {
		if (os != null) {
			os.writeInt(i);
		}
	}

	private synchronized void sendByteData(ImageData imageData) {
		byte[] pixels = new byte[imageData.width];
		try {
			for (int row = 0; row < imageData.height; row++) {
				if (testForAbort())
					return;
				imageData.getPixels(0, row, pixels.length, pixels, 0);
				writeByte(ImageDataConstants.CMD_BYTES);
				writeInt(row);

				// Send with simple compression.
				int col = 0;
				int startNonDup = col;
				int startDup = col;
				byte startDupPixel = pixels[col];
				int stop = pixels.length;
				byte pixel;
				for (; col < stop; col++) {
					if ((pixel = pixels[col]) == startDupPixel)
						continue;
					int dupCnt = col - startDup;
					if (dupCnt < 3) {
						// Need at least 3 dups before it is economical to use a dup group.
						// Move new dup start to current
						startDup = col;
						startDupPixel = pixel;
						continue;
					}
					// We have a nondup group and complete dup group, write them out.
					if (startNonDup < startDup) {
						writeByte(ImageDataConstants.CMD_NOREPEAT);
						writeInt(startDup - startNonDup);
						while (startNonDup < startDup)
							writeByte(pixels[startNonDup++]);
					}

					writeByte(ImageDataConstants.CMD_REPEAT);
					writeInt(dupCnt);
					writeByte(startDupPixel);

					// Start search with current loc.
					startDup = startNonDup = col;
					startDupPixel = pixel;
				}

				// Now write out the trailing groups.
				int dupCnt = col - startDup;
				if (dupCnt < 3) {
					// Need at least 3 dups before it is economical to use a dup group.
					// Move new dup start to current
					startDup = col;
					dupCnt = 0;
				}
				// We have a nondup group and a possible complete dup group, write them out.
				if (startNonDup < startDup) {
					writeByte(ImageDataConstants.CMD_NOREPEAT);
					writeInt(startDup - startNonDup);
					while (startNonDup < startDup)
						writeByte(pixels[startNonDup++]);
				}

				if (dupCnt > 0) {
					writeByte(ImageDataConstants.CMD_REPEAT);
					writeInt(dupCnt);
					writeByte(startDupPixel);
				}
			}
		} catch (IOException e) {
			processTerminatingException(e);
			return;
		}

	}

	private synchronized void sendIntData(ImageData imageData) {
		int[] pixels = new int[imageData.width];
		try {
			for (int row = 0; row < imageData.height; row++) {
				if (testForAbort())
					return;
				imageData.getPixels(0, row, pixels.length, pixels, 0);
				writeByte(ImageDataConstants.CMD_INTS);
				writeInt(row);

				// Send with simple compression.
				int col = 0;
				int startNonDup = col;
				int startDup = col;
				int startDupPixel = pixels[col];
				int stop = pixels.length;
				int pixel;
				for (; col < stop; col++) {
					if ((pixel = pixels[col]) == startDupPixel)
						continue;
					int dupCnt = col - startDup;
					if (dupCnt < 3) {
						// Need at least 3 dups before it is economical to use a dup group.
						// Move new dup start to current
						startDup = col;
						startDupPixel = pixel;
						continue;
					}
					// We have a nondup group and complete dup group, write them out.
					if (startNonDup < startDup) {
						writeByte(ImageDataConstants.CMD_NOREPEAT);
						writeInt(startDup - startNonDup);
						while (startNonDup < startDup)
							writeInt(pixels[startNonDup++]);
					}

					writeByte(ImageDataConstants.CMD_REPEAT);
					writeInt(dupCnt);
					writeInt(startDupPixel);

					// Start search with current loc.
					startDup = startNonDup = col;
					startDupPixel = pixel;
				}

				// Now write out the trailing groups.
				int dupCnt = col - startDup;
				if (dupCnt < 3) {
					// Need at least 3 dups before it is economical to use a dup group.
					// Move new dup start to current
					startDup = col;
					dupCnt = 0;
				}
				// We have a nondup group and a possible complete dup group, write them out.
				if (startNonDup < startDup) {
					writeByte(ImageDataConstants.CMD_NOREPEAT);
					writeInt(startDup - startNonDup);
					while (startNonDup < startDup)
						writeInt(pixels[startNonDup++]);
				}

				if (dupCnt > 0) {
					writeByte(ImageDataConstants.CMD_REPEAT);
					writeInt(dupCnt);
					writeInt(startDupPixel);
				}
			}
		} catch (IOException e) {
			processTerminatingException(e);
			return;
		}

	}
	
	/**
	 * Tell any currently running capture to abort.
	 * 
	 * @param wait
	 *            <code>true</code> if it should wait until aborted.
	 * @return <code>true</code> if wait is <code>false</code>. If wait is <code>true</code>, then returns <code>true</code> if not busy or
	 *         abort has completed. <code>false</code> if abort timed out.
	 * @since 1.1.0
	 */
	public synchronized boolean abortImage(boolean wait) {
		if (status != ImageDataConstants.NOT_IN_PROGRESS) {
			status = ImageDataConstants.IMAGE_ABORTED;
			if (wait) {
				while(true) {
					try {
						wait(15000);
						return status == ImageDataConstants.NOT_IN_PROGRESS;
					} catch (InterruptedException e) {
					}
				}
			} 
		}
		return true;
	}
	
	/**
	 * Send image completion status to the host.
	 * @param status
	 * 
	 * @since 1.1.0
	 */
	protected synchronized void imageComplete(int status) {
		if (os != null) {
			// Not yet completed.
			try {
				os.writeByte(ImageDataConstants.CMD_DONE);
				os.writeInt(status);
				os.close();
				os = null;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close(); // We had an error while terminating. Just close it to be safe.
					} catch (IOException e1) {
					}
				}
			}
		}
		
		synchronized (this) {
			this.status = ImageDataConstants.NOT_IN_PROGRESS;
			notifyAll();
		}
	}

	/**
	 * Test for an abort. If abort requested, it will terminate
	 * the transmission and return true. 
	 * @return <code>true</code> if abort was requested, in this case must not continue. <code>false</code> abort not requested, can continue,
	 * @since 1.1.0
	 */
	protected boolean testForAbort() {
		int s;
		synchronized (ImageCapture.this) {
			s = status;
		}
		if (s == ImageDataConstants.NOT_IN_PROGRESS)
			return true;	// We had terminated already.
		if (s == ImageDataConstants.IMAGE_ABORTED) {
			if (os != null)
				imageComplete(s);	// We haven't yet aborted.
			return true;
		} else
			return false;
	}
}
