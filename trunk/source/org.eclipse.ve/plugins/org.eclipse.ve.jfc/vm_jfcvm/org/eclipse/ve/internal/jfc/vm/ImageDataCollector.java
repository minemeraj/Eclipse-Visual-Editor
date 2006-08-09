/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: ImageDataCollector.java,v $
 *  $Revision: 1.13 $  $Date: 2006-08-09 15:42:28 $ 
 */

import java.awt.*;
import java.awt.image.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.jem.internal.proxy.common.*;
import org.eclipse.jem.internal.proxy.common.remote.CommandErrorException;
import org.eclipse.jem.internal.proxy.common.remote.Commands;

import org.eclipse.ve.internal.jfc.common.ImageDataConstants;
import org.eclipse.ve.internal.jfc.vm.macosx.OSXComponentImageDecorator;

/**
 * An AWT Image Consumer which will consume an AWT image
 * and write the image back to the client (using the callback
 * output stream). It will transmit commands to tell
 * the client how to build the image on the client side.
 * This is necessary because we need to get it into a
 * format that can easily be converted into an SWT image.
 *
 * The output stream will end with a CMD_DONE and the status
 * code for completion. It will then be closed.
 * 
 * A simple compression algorithm will be used. If there are
 * duplicate pixels, only the count of the number of dups, plus
 * the pixel will be sent. Otherwise the entire group of non-dups
 * will be sent. This should help some because most images for
 * components will have many dups along a row. They don't usually
 * have a different pixel right next to each other on a continued
 * basis.
 */
public class ImageDataCollector implements ImageConsumer, ICallback {
	
	protected IVMCallbackServer fVMServer;
	protected int fCallbackID;
	protected DataOutputStream fos;
	protected int fWidth, fHeight;
	protected IndexColorModel fStartingIndexModel;
	protected boolean fIndex;
	protected int fStatus = ImageDataConstants.NOT_IN_PROGRESS;
	
	protected static final int NO_MODEL = -1;
	protected int fDepth = NO_MODEL;
	
	// fProducer and fEndProductionRequested can only be referenced/changed under synchronized (this) blocks.
	protected ImageProducer fProducer = null;
	protected boolean fEndProductionRequested = false;	// There's no way to stop production, so we need to indicate that
														// production end was requested so that we can ignore further
														// data until the end is normally reached and then return the abort flag.
	
	private static final boolean isMacOSX = System.getProperty("os.name").equalsIgnoreCase("Mac OS X"); //$NON-NLS-1$ //$NON-NLS-2$
	
	public ImageDataCollector() {
	}
	
	public void initializeCallback(IVMCallbackServer vmServer, int callbackID) {
		fVMServer = vmServer;
		fCallbackID = callbackID;
	}	
		
	/**
	 * Start collecting on a specific image.
	 */
	public boolean start(Image image) throws IllegalAccessException, IllegalArgumentException, CommandException {
		return start(image, ImageDataConstants.IMAGE_NOT_STARTED);
	}
	
	protected boolean start(Image image, int startStatus) throws IllegalAccessException, IllegalArgumentException, CommandException {
		return start(image.getSource(), startStatus);
	}
	
	/**
	 * Start collecting the image of a component.
	 * Return whether collection has started or not.
	 * It is possible that collection didn't start but this is not
	 * an error. For example the component has a width or height of 0.
	 */
	public boolean start(final Component component, final int maxWidth, final int maxHeight) throws IllegalAccessException, IllegalArgumentException, CommandException {
		// Need to queue the printall off to the UI thread because there could be problems in some
		// versions of the JDK if paint and print are done at the same time.
		// Also we now want (as of 1.1.0) the validate() to be done in UI thread too so that any
		// bounds changes will be batched together and sent in one transaction instead of each
		// individually.
		synchronized (this) {
			if (fStatus != ImageDataConstants.NOT_IN_PROGRESS)
				return false;	// Already in progress. We only got this far because two requests were submitted before we got this far to mark one as started.
			fStatus = ImageDataConstants.IN_PROGRESS;	// Need to mark in progress because we have started
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Now validate the component so that we have a good image to capture.
				// We need to go to top level component so that all validations are done,
				// if on a component, validate only validates that component, so it won't
				// get relayed out. We have to check for Window because we don't want to 
				// go higher than a Window. A Window can have a parent, but that is another
				// window, and validating that window will not validate child windows (this
				// relationship downward is ownership and not child containment).
				Container parent = component.getParent();			
				while (parent != null && !(parent instanceof Window) && parent.getParent() != null)
					parent = parent.getParent();
				if (parent == null)
					component.validate();	// There is no parent at all, so validate this one only.
				else
					parent.validate();
				

				// Get the AWT image of the component
				if (component.getWidth() == 0 || component.getHeight() == 0) {
					imageComplete(ImageDataConstants.IMAGE_EMPTY);
					synchronized(ImageDataCollector.this) {
						fProducer = null;
						fStatus = ImageDataConstants.NOT_IN_PROGRESS;			
						ImageDataCollector.this.notifyAll();	// Let anyone waiting know we are really done.
						return;
					}								
				}

				boolean clipped = false;
				int w = component.getWidth();
				int h = component.getHeight();
				if (w > maxWidth) {
					w = maxWidth;
					clipped = true;
				}
				if (h > maxHeight) {
					h = maxHeight;
					clipped = true;
				}		
				final int iWidth = w;
				final int iHeight = h;	
				ColorModel cm = component.getColorModel();
				final Image componentImage = new BufferedImage(cm, cm.createCompatibleWritableRaster(iWidth, iHeight), cm.isAlphaPremultiplied(), null);
				if (componentImage == null) {
					// This could happen if we asked for an image of component not attached to a window, it was
					// attached to a window when requested but it was detached by the time the event queue got to process this request.
					imageComplete(IMAGEERROR);
					synchronized(ImageDataCollector.this) {
						fProducer = null;
						fStatus = ImageDataConstants.NOT_IN_PROGRESS;			
						ImageDataCollector.this.notifyAll();	// Let anyone waiting know we are really done.
						return;
					}
				}
				final int startStatus = !clipped ? ImageDataConstants.IMAGE_NOT_STARTED : ImageDataConstants.COMPONENT_IMAGE_CLIPPED;
				
				// We have a component image. We may not if it wasn't yet visible.
				Graphics graphics = null;			
				try {
					try {
						graphics = componentImage.getGraphics();
						graphics.setClip(0, 0, iWidth, iHeight);
						component.printAll(graphics);
						
						if (isMacOSX)
						{
							OSXComponentImageDecorator.decorateComponent(component, componentImage, iWidth, iHeight);
						}
					} finally {
						if (graphics != null)
							graphics.dispose(); // Clear out the resources.
						synchronized(ImageDataCollector.this) {
							if (fEndProductionRequested) {
								// End requested while retrieving image, means don't bother producing.
								fProducer = null;
								fStatus = ImageDataConstants.NOT_IN_PROGRESS;			
								ImageDataCollector.this.notifyAll();	// Let anyone waiting know we are really done.
								return;
							}
						}								
					}
					if (!start(componentImage, startStatus)) {
						imageComplete(IMAGEERROR);
						synchronized(ImageDataCollector.this) {
							fProducer = null;
							fStatus = ImageDataConstants.NOT_IN_PROGRESS;			
							ImageDataCollector.this.notifyAll();	// Let anyone waiting know we are really done.
							return;
						}								
					}
				} catch(final Throwable e) {
					// If this is a callback not registered, don't bother with printing anything. This just means this request came
					// in after a close of the component was started. So we don't care.
					if (!(e instanceof CommandErrorException) || ((CommandErrorException) e).getErrorCode() != Commands.CALLBACK_NOT_REGISTERED)
						e.printStackTrace();
					try {
						fVMServer.doCallback(new ICallbackRunnable() {
							public Object run(ICallbackHandler handler) throws CommandException {
								return handler.callbackWithParms(fCallbackID, ImageDataConstants.IMAGE_HAS_EXCEPTION, new Object[] {e});
							}
						});
					} catch (CommandErrorException e1) {
						// If this is a callback not registered, don't bother with printing anything. This just means this request came
						// in after a close of the component was started. So we don't care.						
						if (e1.getErrorCode() != Commands.CALLBACK_NOT_REGISTERED)
							e.printStackTrace();
					} catch (CommandException e1) {
						e1.printStackTrace();
					}						
					imageComplete(IMAGEERROR);
					synchronized(ImageDataCollector.this) {
						fProducer = null;
						fStatus = ImageDataConstants.NOT_IN_PROGRESS;			
						ImageDataCollector.this.notifyAll();	// Let anyone waiting know we are really done.
						return;
					}								
					
				}
			}
		});
		return true;
	}		
	
	
	/**
	 * Start production with the producer.
	 */
	public boolean start(ImageProducer producer) throws IllegalAccessException, IllegalArgumentException, CommandException {
		return start(producer, ImageDataConstants.IMAGE_NOT_STARTED);
	}
	
	protected boolean start(final ImageProducer producer, final int initialStartStatus) throws IllegalAccessException, IllegalArgumentException, CommandException {
		synchronized (this) {
			if (fProducer != null)
				throw new IllegalAccessException("Image collection already in progress."); //$NON-NLS-1$
			if (producer == null)
				throw new IllegalArgumentException("ImageProducer is null"); //$NON-NLS-1$
				
			fStartingIndexModel = null;
			fIndex = false;
			fWidth = fHeight = -1;
			fDepth = NO_MODEL;
			fStatus = ImageDataConstants.IN_PROGRESS;
			fProducer = producer;
			fEndProductionRequested = false;
			fos = null;			
			try {
				fos = new DataOutputStream(fVMServer.requestStream(fCallbackID, ImageDataConstants.IMAGE_DATA_COLLECTION));				
			} finally {
				if (fos == null) {
					fProducer = null;	// Clear up so we don't think we are still producing.
					fEndProductionRequested = true;
					fStatus = ImageDataConstants.UNKNOWN_STATUS;
				}
			} 
		}
		
		if (fos == null)
			return false;	// Shouldn't of gotton here, but just to be safe.
			
		new Thread(new Runnable() {
			public void run() {
				try {
					final int startStatus = initialStartStatus == ImageDataConstants.IMAGE_NOT_STARTED ? ImageDataConstants.IMAGE_STARTED : initialStartStatus;
					try {
						fVMServer.doCallback(new ICallbackRunnable() {
							public Object run(ICallbackHandler handler) throws CommandException {
								return handler.callbackWithParms(fCallbackID, ImageDataConstants.IMAGE_HAS_STARTED_CALLBACK, new Object[] {new Integer(startStatus)});
							}
						});
					} catch (CommandException e) {
						e.printStackTrace();
					}
					producer.startProduction(ImageDataCollector.this);
				} catch (final Exception e) {
					e.printStackTrace();
					try {
						fVMServer.doCallback(new ICallbackRunnable() {
							public Object run(ICallbackHandler handler) throws CommandException {
								return handler.callbackWithParms(fCallbackID, ImageDataConstants.IMAGE_HAS_EXCEPTION, new Object[] {e});
							}
						});
					} catch (CommandException e1) {
						e1.printStackTrace();
					}					
				} finally {
					try {
						// In case it did not finish correctly, we need to finish it.
						boolean notFinished = false;
						synchronized(ImageDataCollector.this) {							
							notFinished = (fos != null);
						}
						
						if (notFinished) {
							// It failed and didn't call imageComplete, so simulate imageComplete with error.
							imageComplete(IMAGEERROR);
						}
					} finally {
						synchronized(ImageDataCollector.this) {
							fProducer = null;
							fStatus = ImageDataConstants.NOT_IN_PROGRESS;			
							ImageDataCollector.this.notifyAll();	// Let anyone waiting know we are really done.
						}						
					}
				}
			}
		}).start();
				
		return true;
	}
	
	public void setDimensions(int width, int height){
		synchronized(this) {
			if (fEndProductionRequested)
				return;	// We don't want to bother sending the data
		}
		
		if (width != fWidth || height != fHeight) {
			// Need to tell client a new width and height.
			try {
				fos.writeByte(ImageDataConstants.CMD_DIM);
				fos.writeInt(width);
				fos.writeInt(height);
			} catch (IOException e) {
				e.printStackTrace();
				requestTerminate(IMAGEERROR);
				return;
			}
			fWidth = width;
			fHeight = height;
		}
	}
	
	public void setProperties(Hashtable properties){
		// We do nothing with properties
	}
	
	/**
	 * set the color model to use.
	 *
	 * Send the color model to use over to the client.
	 */
	public void setColorModel(ColorModel model) {
		synchronized(this) {
			if (fEndProductionRequested)
				return;	// We don't want to bother sending the data
		}
		
		if (!(model instanceof IndexColorModel)) {
			// Not an index model.
			if (fIndex) {
				// We have an existing index model, and going to not index. Need to convert image to direct
				sendClientDirectColorModel(model.getPixelSize());
			} else {
				// Tell client of model at the same depth as the incoming model.
				// We will use direct model on the client side since none of the others are known in SWT.
				int newDepth = model.getPixelSize();
				if (newDepth == 15) {
					newDepth = 16;	// SWT can't handle 15 as a depth, but this
							// is equivalent to 16 bit. And since we
							// use the java.awt.ColorModel to get the pixels
							// out of the word, it gets them out in the
							// necessary 16 bit format (i.e. 8 bits per color,
							// which we shift down to 5 bits required by depth 16).
				}
				if (fDepth != newDepth)
					sendClientDirectColorModel(newDepth);
			}
		} else {
			// It is an index model, see if different model.
			if (model != fStartingIndexModel) 
				if (fDepth == NO_MODEL) {
					// We have no model yet. Use this one.
					sendClientIndexColorModel((IndexColorModel) model);
				} else if(fIndex) {
					// The current model is an index but not the current index, see if we can use
					// If the current model was direct, then use it as is and ignore this setting. We won't convert back to indexed.

					// See if they are compatible, it so use as is.
					boolean compatible = false;
					if (fStartingIndexModel.getPixelSize() == model.getPixelSize()) {
						int newLength = ((IndexColorModel) model).getMapSize();
						int oldLength = fStartingIndexModel.getMapSize();
						if (newLength == oldLength) {
							int[] oldRGBs = new int[oldLength];
							int[] newRGBs = new int[newLength];
							fStartingIndexModel.getRGBs(oldRGBs);
							((IndexColorModel) model).getRGBs(newRGBs);
							compatible = java.util.Arrays.equals(oldRGBs, newRGBs);
						}
					}
					
					if (!compatible) 
						sendClientDirectColorModel(0);	// Not compatible, convert to the direct model on the client.
				}
		}
	}
	
	protected void sendClientDirectColorModel(int depth) {
		// Tell the client to use a direct color palette of the specified depth.
		switch (depth) {
			case 0:
				// Need to get the preferred depth from the client.
				try {
					depth = ((Integer) fVMServer.doCallback(new ICallbackRunnable() {
						public Object run(ICallbackHandler handler) throws CommandException {
							return handler.callbackWithParms(fCallbackID, ImageDataConstants.IMAGE_GET_PREFERRED_DEPTH, null);
						}
					})).intValue();
				} catch (CommandException e) {
					e.printStackTrace();
					depth = 24;
				}
				break;
			case 16:
			case 24:
				break;	// Currently can only handle 16 and 24 (on Windows).
			default:
				depth = 24;	// Anything else we will use depth of 24 (32 is valid, but there is a bug that it won't accept the palette correctly).
		}
		fDepth = depth;
		fStartingIndexModel = null;
		fIndex = false;
		try {
			fos.writeByte(ImageDataConstants.CMD_DIRECT);
			fos.writeByte((byte) depth);
		} catch (IOException e) {
			e.printStackTrace();
			requestTerminate(IMAGEERROR);
			return;
		}
	}
	
	protected void sendClientIndexColorModel(IndexColorModel model) {
		// Tell the client to use an indexed color palette.
		fStartingIndexModel = model;
		fIndex = true;
		fDepth = model.getPixelSize();
		int length = model.getMapSize();
		int transparentPixel = model.getTransparentPixel();
		byte[] reds = new byte[length];
		byte[] greens = new byte[length];
		byte[] blues = new byte[length];
		model.getReds(reds);
		model.getGreens(greens);
		model.getBlues(blues);
		try {
			fos.writeByte(ImageDataConstants.CMD_INDEXED);
			fos.writeByte((byte) fDepth);
			fos.writeInt(transparentPixel);
			fos.writeInt(length);
			for (int i=0; i<length; i++) {
				fos.writeByte(reds[i]);
				fos.writeByte(greens[i]);
				fos.writeByte(blues[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			requestTerminate(IMAGEERROR);
			return;
		}
	}
		
	public void setHints(int hintsFlags){
		// We do nothing with hints
	}
	
	/**
	 * setPixels: Set the pixels. The pixels can fit into a byte. Test for sure, but
	 * if it fits into a byte, it will be a color index model.
	 */
	public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {	
		synchronized(this) {
			if (fEndProductionRequested)
				return;	// We don't want to bother sending the data
		}
		
		if (fIndex) {
			// Current palette is an index model, so see if compatible
			if (fStartingIndexModel != model) {
				sendClientDirectColorModel(0); // Convert over to direct model, easiest way to handle model change.
				setPixels(x, y, w, h, model, pixels, off, scansize); // Now try with the new palette
			} else {
				// If I'm in bytes, then depth must be 1, 4, or 8. ImageData can handle those.
				// Depth greater than 8 is impossible anyway.
				for (int rowOffset = 0, row = y; rowOffset < h; rowOffset++, row++) {
					// Send each row separately.
					try {
						fos.writeByte(ImageDataConstants.CMD_BYTES);
						fos.writeInt(x);
						fos.writeInt(row);
						
						// Send with simple compression.
						int javaIndex = off + (scansize * rowOffset);
						int startNonDup = javaIndex;
						int startDup = javaIndex;
						byte startDupPixel = pixels[javaIndex];
						int stop = javaIndex+w;
						byte pixel;
						for (; javaIndex < stop; javaIndex++) {
							if ((pixel = pixels[javaIndex]) == startDupPixel)
								continue;
							int dupCnt = javaIndex-startDup;
							if (dupCnt < 3) {
								// Need at least 3 dups before it is economical to use a dup group.
								// Move new dup start to current
								startDup = javaIndex;
								startDupPixel = pixel;
								continue;
							}
							// We have a nondup group and complete dup group, write them out.
							if (startNonDup < startDup) {
								fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
								fos.writeInt(startDup - startNonDup);
								while(startNonDup<startDup)
									fos.writeByte(pixels[startNonDup++]);
							}
							
							fos.writeByte(ImageDataConstants.CMD_REPEAT);
							fos.writeInt(dupCnt);
							fos.writeByte(startDupPixel);
							
							// Start search with current loc.
							startDup = startNonDup = javaIndex;
							startDupPixel = pixel;
						}
	
						// Now write out the trailing groups.					
						int dupCnt = javaIndex-startDup;
						if (dupCnt < 3) {
							// Need at least 3 dups before it is economical to use a dup group.
							// Move new dup start to current
							startDup = javaIndex;
							dupCnt = 0;
						}
						// We have a nondup group and a possible complete dup group, write them out.
						if (startNonDup < startDup) {
							fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
							fos.writeInt(startDup - startNonDup);
							while(startNonDup<startDup)
								fos.writeByte(pixels[startNonDup++]);
						}
						
						if (dupCnt > 0) {
							fos.writeByte(ImageDataConstants.CMD_REPEAT);
							fos.writeInt(dupCnt);
							fos.writeByte(startDupPixel);
						}
					} catch (IOException e) {
						e.printStackTrace();
						requestTerminate(IMAGEERROR);
						return;
					}
				}
			}
		} else {
			if (w <= 0)
				return;	// Sanity check. If the width is zero, don't go one.
			// Currently a direct palette, so get the RGB's and place into the data. 
			for (int rowOffset = 0, row = y; rowOffset < h; rowOffset++, row++) {
				try {
					fos.writeByte(ImageDataConstants.CMD_INTS);
					fos.writeInt(x);
					fos.writeInt(row);
					
					// Send with simple compression.
					int javaIndex = off + (scansize * rowOffset);
					int startNonDup = javaIndex;
					int startDup = javaIndex;
					byte startDupPixel = pixels[javaIndex];
					int stop = javaIndex+w;
					byte pixel;
					for (; javaIndex < stop; javaIndex++) {
						if ((pixel = pixels[javaIndex]) == startDupPixel)
							continue;
						int dupCnt = javaIndex-startDup;
						if (dupCnt < 3) {
							// Need at least 3 dups before it is economical to use a dup group.
							// Move new dup start to current
							startDup = javaIndex;
							startDupPixel = pixel;
							continue;
						}
						// We have a nondup group and complete dup group, write them out.
						if (startNonDup < startDup) {
							fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
							fos.writeInt(startDup - startNonDup);
							while(startNonDup<startDup)
								writeIntPixel(model, pixels[startNonDup++]);
						}
						
						fos.writeByte(ImageDataConstants.CMD_REPEAT);
						fos.writeInt(dupCnt);
						writeIntPixel(model, startDupPixel);
						
						// Start search with current loc.
						startDup = startNonDup = javaIndex;
						startDupPixel = pixel;
					}

					// Now write out the trailing groups.					
					int dupCnt = javaIndex-startDup;
					if (dupCnt < 3) {
						// Need at least 3 dups before it is economical to use a dup group.
						// Move new dup start to current
						startDup = javaIndex;
						dupCnt = 0;
					}
					// We have a nondup group and a possible complete dup group, write them out.
					if (startNonDup < startDup) {
						fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
						fos.writeInt(startDup - startNonDup);
						while(startNonDup<startDup)
							writeIntPixel(model, pixels[startNonDup++]);
					}
					
					if (dupCnt > 0) {
						fos.writeByte(ImageDataConstants.CMD_REPEAT);
						fos.writeInt(dupCnt);
						writeIntPixel(model, startDupPixel);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
					requestTerminate(IMAGEERROR);
					return;
				}
			}
		}
	}

	private void writeIntPixel(ColorModel model, int pixel) throws IOException {
		// Convert each incoming pixel into RGB for setting into the data. Need to do this
		// because Java RGB model doesn't match SWT RGB model.		
		
		// Currently only handle direct of 16 and 24.
		if (fDepth == 16) {
			// The AWT model scales the colors to 0-255, even when in depth 16, so we need
			// to scale back to 5 bits.
			int red = (model.getRed(pixel) >> 3) & 0x1F;
			int green = (model.getGreen(pixel) >> 3) & 0x1F;
			int blue = (model.getBlue(pixel) >> 3) & 0x1F;
			
			fos.writeInt(red << 10 | green << 5 | blue);
		} else {
			fos.writeInt((model.getBlue(pixel) & 0xFF) << 16 | (model.getGreen(pixel) & 0xFF) << 8 | model.getRed(pixel) & 0xFF);
		}
	}
	
	public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize){
		synchronized(this) {
			if (fEndProductionRequested)
				return;	// We don't want to bother sending the data
		}
				
		if (fIndex) {
			// Current palette is an index model, so see if compatible
			if (fStartingIndexModel != model) {
				sendClientDirectColorModel(0); // Convert over to direct model, easiest way to handle model change.
				setPixels(x, y, w, h, model, pixels, off, scansize); // Now try with the new palette
			} else {
				for (int rowOffset = 0, row = y; rowOffset < h; rowOffset++, row++) {
					// Send each row separately.
					try {				
					fos.writeByte(ImageDataConstants.CMD_INTS);
					fos.writeInt(x);
					fos.writeInt(row);
					
					// Send with simple compression.
					int javaIndex = off + (scansize * rowOffset);
					int startNonDup = javaIndex;
					int startDup = javaIndex;
					int startDupPixel = pixels[javaIndex];
					int stop = javaIndex+w;
					int pixel;
					for (; javaIndex < stop; javaIndex++) {
						if ((pixel = pixels[javaIndex]) == startDupPixel)
							continue;
						int dupCnt = javaIndex-startDup;
						if (dupCnt < 3) {
							// Need at least 3 dups before it is economical to use a dup group.
							// Move new dup start to current
							startDup = javaIndex;
							startDupPixel = pixel;
							continue;
						}
						// We have a nondup group and complete dup group, write them out.
						if (startNonDup < startDup) {
							fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
							fos.writeInt(startDup - startNonDup);
							while(startNonDup<startDup)
								fos.writeInt(pixels[startNonDup++]);
						}
						
						fos.writeByte(ImageDataConstants.CMD_REPEAT);
						fos.writeInt(dupCnt);
						fos.writeInt(startDupPixel);
						
						// Start search with current loc.
						startDup = startNonDup = javaIndex;
						startDupPixel = pixel;
					}

					// Now write out the trailing groups.					
					int dupCnt = javaIndex-startDup;
					if (dupCnt < 3) {
						// Need at least 3 dups before it is economical to use a dup group.
						// Move new dup start to current
						startDup = javaIndex;
						dupCnt = 0;
					}
					// We have a nondup group and a possible complete dup group, write them out.
					if (startNonDup < startDup) {
						fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
						fos.writeInt(startDup - startNonDup);
						while(startNonDup<startDup)
							fos.writeInt(pixels[startNonDup++]);
					}
					
					if (dupCnt > 0) {
						fos.writeByte(ImageDataConstants.CMD_REPEAT);
						fos.writeInt(dupCnt);
						fos.writeInt(startDupPixel);
					}
					
				} catch (IOException e) {
						e.printStackTrace();
						requestTerminate(IMAGEERROR);
						return;
					}
				}
			}
		} else {
			// Currently a direct palette, so get the RGB's and place into the data.
			// Convert each incoming pixel into RGB for setting into the data. Need to do this
			// because Java RGB model doesn't match SWT RGB model. 
			for (int rowOffset = 0, row = y; rowOffset < h; rowOffset++, row++) {
				try {				
					fos.writeByte(ImageDataConstants.CMD_INTS);
					fos.writeInt(x);
					fos.writeInt(row);
					
					// Send with simple compression.
					int javaIndex = off + (scansize * rowOffset);
					int startNonDup = javaIndex;
					int startDup = javaIndex;
					int startDupPixel = pixels[javaIndex];
					int stop = javaIndex+w;
					int pixel;
					for (; javaIndex < stop; javaIndex++) {
						if ((pixel = pixels[javaIndex]) == startDupPixel)
							continue;
						int dupCnt = javaIndex-startDup;
						if (dupCnt < 3) {
							// Need at least 3 dups before it is economical to use a dup group.
							// Move new dup start to current
							startDup = javaIndex;
							startDupPixel = pixel;
							continue;
						}
						// We have a nondup group and complete dup group, write them out.
						if (startNonDup < startDup) {
							fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
							fos.writeInt(startDup - startNonDup);
							while(startNonDup<startDup)
								writeIntPixel(model, pixels[startNonDup++]);
						}
						
						fos.writeByte(ImageDataConstants.CMD_REPEAT);
						fos.writeInt(dupCnt);
						writeIntPixel(model, startDupPixel);
						
						// Start search with current loc.
						startDup = startNonDup = javaIndex;
						startDupPixel = pixel;
					}

					// Now write out the trailing groups.					
					int dupCnt = javaIndex-startDup;
					if (dupCnt < 3) {
						// Need at least 3 dups before it is economical to use a dup group.
						// Move new dup start to current
						startDup = javaIndex;
						dupCnt = 0;
					}
					// We have a nondup group and a possible complete dup group, write them out.
					if (startNonDup < startDup) {
						fos.writeByte(ImageDataConstants.CMD_NOREPEAT);
						fos.writeInt(startDup - startNonDup);
						while(startNonDup<startDup)
							writeIntPixel(model, pixels[startNonDup++]);
					}
					
					if (dupCnt > 0) {
						fos.writeByte(ImageDataConstants.CMD_REPEAT);
						fos.writeInt(dupCnt);
						writeIntPixel(model, startDupPixel);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
					requestTerminate(IMAGEERROR);
					return;
				}
			}
		}
	
	}
	
	public void imageComplete(int status){
		try {
			ImageProducer producer = null;
			DataOutputStream os	= null;
			boolean justClose = false;
			synchronized(this) {				
				if (fProducer == null)
					return;
				producer = fProducer;
				os = fos;
				// If end requested, then we already status set.
				if (!fEndProductionRequested) {
					// We don't want to expose awt constants on the client, so we will convert
					// status to common constants in ImageDataConstants.
					switch (status) {
						case IMAGEERROR:
							fStatus = ImageDataConstants.IMAGE_ERROR;
							break;
						case SINGLEFRAMEDONE:
							fStatus = ImageDataConstants.SINGLE_FRAME_DONE;
							break;
						case STATICIMAGEDONE:
							fStatus = ImageDataConstants.STATIC_IMAGE_DONE;
							break;
						case IMAGEABORTED:
							fStatus = ImageDataConstants.IMAGE_ABORTED;
							break;
						case ImageDataConstants.IMAGE_EMPTY:
							fStatus = ImageDataConstants.IMAGE_EMPTY;
							break;
						default:
							fStatus = ImageDataConstants.UNKNOWN_STATUS;
							break;
					}
				} else
					justClose = true;
				fEndProductionRequested = false;
				fos = null;	// This being set to null indicates that we have gone through a complete. If production ended due to some error 
						// and it didn't go through imageComplete, then this will be non-null and it knows to complete it.																							
			}			
			producer.removeConsumer(this);
			try {
				if (!justClose) {
					os.writeByte(ImageDataConstants.CMD_DONE);
					os.writeInt(fStatus);
				}
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					os.close();	// We had an error while terminating. Just close it to be safe.
				} catch (IOException e1) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void waitForCompletion() {		
		if (fStatus == ImageDataConstants.NOT_IN_PROGRESS)
			return;
		while (true) {
			try {
				wait();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized boolean isCollecting() {
		return fProducer != null;
	}
	
	public void abort() {
		requestTerminate(IMAGEABORTED);
	}
	
	protected void requestTerminate(final int status) {
		boolean sendNotice = false;
		synchronized (this) {
			if (!fEndProductionRequested && fStatus == ImageDataConstants.IN_PROGRESS) {
				// We're in progress and we haven't already asked for a termination.
				fEndProductionRequested = true;
				fStatus = status;
				sendNotice = true;						
			}
		}
		if (sendNotice) {
			// Let the client know that it has been terminated.
			try {
				fVMServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, ImageDataConstants.IMAGE_HAS_BEEN_ABORTED, new Object[] {new Integer(status)});
					}
				});
			} catch (CommandException e) {
				e.printStackTrace();
			}
		}
	}

}
