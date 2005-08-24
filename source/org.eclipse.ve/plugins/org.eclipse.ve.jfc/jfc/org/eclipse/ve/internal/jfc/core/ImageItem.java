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
 *  $RCSfile: ImageItem.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * This is a convenience class that acts like a wrapper around an Image.
 * It holds on to an image's label, tooltip, etc.which are used by the
 * IconScreenDisplay.  While this class seems to hold on to an Image,
 * it actually does not.  It holds on to the ImageData for an Image,
 * and when asked for the Image, it returns a new Image created from
 * the ImageData (hence, it becomes the client's responsibility to dispose
 * that image properly).  This is helpful if you have lots of ImageItems
 * hanging around, as they will not all take up image handles in the OS.
 */
public class ImageItem {

private String label = ""; //$NON-NLS-1$
private String tooltipText = ""; //$NON-NLS-1$
private ImageData id;
private int width, height; 		//image width and height 

/**
 * Creates a new instance of ImageItem
 */
public ImageItem(String filename, String label) throws FileNotFoundException{
    this.label = label;
    try{
        InputStream fis = new FileInputStream( filename );
        id = new ImageData( fis );
        height = id.height;
        width = id.width;
    } finally{}
}

/**
 * Creates a new instance of ImageItem
 */
public ImageItem(String filename, String text, String toolTipText) 
        throws FileNotFoundException {
    this( filename,text );
    this.tooltipText = toolTipText;
}

/**
 * gets the height of the image
 */
public int getHeight() {
	return height;
}

/**
 * Gets the image.
 */
public Image getImage( Display screen ) {
    return new Image( screen, id );
}

/**
 * Gets the label
 */
public String getLabel () {
	return label;
}

/**
 * Gets the ToolTip text
 */
public String getTooltipText() {
	return tooltipText;
}

/**
 * Gets the width of the image
 */
public int getWidth() {
	return width;
}

/**
 * Sets the label for the image
 */
public void setLabel(String label) {
	this.label = label;
}

/**
 * Sets the ToolTip text
 */
public void setTooltipText(String newTooltipText) {
	tooltipText = newTooltipText;
}

}
