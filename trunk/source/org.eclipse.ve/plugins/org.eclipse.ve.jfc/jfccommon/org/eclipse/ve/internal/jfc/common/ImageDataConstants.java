package org.eclipse.ve.internal.jfc.common;
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
 *  $RCSfile: ImageDataConstants.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 23:13:34 $ 
 */

/**
 * This contains constants needed to be shared
 * between the vm and the IDE for ImageData collection.
 */
public interface ImageDataConstants {
	
	public final static int
		IMAGE_DATA_COLLECTION = 1,			// MsgId sent back with the image data collection input stream callback.
		IMAGE_GET_PREFERRED_DEPTH = 2,		// MsgId for requesting preferred depth.
		IMAGE_HAS_BEEN_ABORTED = 3,			// MsgId for image was aborted so ignore rest of data in the pipeline. The beanproxy parm will have the status int to use.
		IMAGE_HAS_STARTED_CALLBACK = 4,		// MsgId for image has been started with the code.
		IMAGE_HAS_EXCEPTION = 5;			// MsgId for image collection has thrown an exception.
		
	// Image Started status codes.
	public final static int	
		IMAGE_NOT_STARTED = -1,				// Image was not started and no collection done.
		IMAGE_STARTED = 0,					// Image collection started ok
		COMPONENT_IMAGE_CLIPPED = 1;		// Component image collection was clipped. Requested max size was smaller then component size.
	
	// The stream for feeding image data back will be wrappered into a Data...Stream
	// so that some structured data may be sent.
	public final static byte
		CMD_DIM = 1,		// A new width and height, width/height will follow as integers.
		CMD_DIRECT = 2,	// A direct color palette, followed by a byte containing the depth.
		CMD_INDEXED = 3,	// An indexed color palette, followed by a byte containing the depth,
					//	an int containing the transparent pixel, an int containing the
					//	number of RGBs to follow, and array of bytes,
					//	representing the RGBs (R,G,B,R,G,B,...).
		CMD_BYTES = 4,		// Send bytes for a row. Followed by int x, int y. Following this will be repeat/nonrepeat blocks, until next command.
		CMD_INTS = 5,		// Send ints for a row. Followed by int x, int y. Following this will be repeat/nonrepeat blocks, until next command.
		CMD_REPEAT = 6,		// One pixel will be repeated. Followed by int numPixels, int/byte pixel (pixel to be repeated, depends on current cmd mode)
		CMD_NOREPEAT = 7,	// Pixel group. Followed by int numPixels, int/bype pixels (for numPixels, byte/int depends on current command mode).
		CMD_DONE = 8;		// The collection has ended. Followed by int status, shown below.
		
		
	// Completion codes
	public final static int
		/**
		 * An error was encountered while producing the image.
		 */
		IMAGE_ERROR = 1,
		
		/**
		 * One frame of the image is complete but there are more frames
		 * to be delivered.
		 */
		SINGLE_FRAME_DONE = 2,
		
		/**
		 * The image is complete and there are no more pixels or frames
		 * to be delivered.
		 */
		STATIC_IMAGE_DONE = 3,
		
		/**
		 * The image creation process was deliberately aborted.
		 */
		IMAGE_ABORTED = 4,
		
		/**
		 * Unknown status code from ImageProducer.
		 */
		UNKNOWN_STATUS = 5,
		
		/**
		 * Consume in progress.
		 */
		IN_PROGRESS = 6,
		
		/**
		 * Consume not in progress.
		 */
		NOT_IN_PROGRESS = -1;
		

}