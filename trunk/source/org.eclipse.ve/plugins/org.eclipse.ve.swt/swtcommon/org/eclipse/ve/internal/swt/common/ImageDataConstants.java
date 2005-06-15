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
 *  $RCSfile: ImageDataConstants.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt.common;

/**
 * This contains constants needed to be shared between the vm and the IDE for ImageData collection.
 */
public interface ImageDataConstants {

	// Completion codes
	/**
	 * Status code: Image was not started and no collection done. Sent through
	 * {@link org.eclipse.ve.internal.swt.ImageDataCollector.DataCollectedRunnable#imageNotCollected(int)}.
	 */
	public final static int IMAGE_NOT_STARTED = 0;

	/**
	 * Status code: The image is complete and successful. Sent through
	 * {@link org.eclipse.ve.internal.swt.ImageDataCollector.DataCollectedRunnable#imageData(ImageData, int)}.
	 */
	public final static int IMAGE_COMPLETE = 1;

	/**
	 * Status code: Image was clipped. Requested max size was smaller than component size. Sent through
	 * {@link org.eclipse.ve.internal.swt.ImageDataCollector.DataCollectedRunnable#imageData(ImageData, int)}.
	 */
	public final static int COMPONENT_IMAGE_CLIPPED = 2;

	/**
	 * Status code: Image was a (0,0) sized image. Nothing sent. Sent through
	 * {@link org.eclipse.ve.internal.swt.ImageDataCollector.DataCollectedRunnable#imageData(ImageData, int)}.
	 */
	public final static int IMAGE_EMPTY = 3;

	/**
	 * Status code: An error was encountered while producing the image. Sent through
	 * {@link org.eclipse.ve.internal.swt.ImageDataCollector.DataCollectedRunnable#imageNotCollected(int)}.
	 */
	public final static int IMAGE_ERROR = 4;

	/**
	 * Status code: The image creation process was deliberately aborted. Sent through
	 * {@link org.eclipse.ve.internal.swt.ImageDataCollector.DataCollectedRunnable#imageNotCollected(int)}.
	 */
	public final static int IMAGE_ABORTED = 5;

	// Folowing are internal codes used within SWT, not for use of clients.

	public final static int IN_PROGRESS = 6, // Consume in progress. This is not sent to image listener.
			NOT_IN_PROGRESS = -1; // Consume not in progress. This is not sent to image listener.

	public final static int IMAGE_DATA_COLLECTION = 1, // MsgId sent back with the image data collection input stream callback.
			IMAGE_HAS_EXCEPTION = 2; // MsgId for image collection has thrown an exception.

	// The stream for feeding image data back will be wrappered into a Data...Stream
	// so that some structured data may be sent.
	public final static byte CMD_DIM = 1, // A new width and height, width/height will follow as integers.
			CMD_DIRECT = 2, // A direct color palette, followed by a byte containing the depth.
			CMD_INDEXED = 3, // An indexed color palette, followed by a byte containing the depth,
			// an int containing the transparent pixel, an int containing the
			// number of RGBs to follow, and array of bytes,
			// representing the RGBs (R,G,B,R,G,B,...).
			CMD_BYTES = 4, // Send bytes for a row. Followed by int y. Following this will be repeat/nonrepeat blocks, until next command.
			CMD_INTS = 5, // Send ints for a row. Followed by int y. Following this will be repeat/nonrepeat blocks, until next command.
			CMD_REPEAT = 6, // One pixel will be repeated. Followed by int numPixels, int/byte pixel (pixel to be repeated, depends on current cmd
			// mode)
			CMD_NOREPEAT = 7, // Pixel group. Followed by int numPixels, int/bype pixels (for numPixels, byte/int depends on current command mode).
			CMD_DONE = 8; // The collection has ended. Followed by int status.

}