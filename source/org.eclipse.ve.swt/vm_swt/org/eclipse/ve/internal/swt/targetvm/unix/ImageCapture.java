/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *  $Revision: 1.6 $  $Date: 2005-02-15 23:54:57 $ 
 */
package org.eclipse.ve.internal.swt.targetvm.unix;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.swt.targetvm.IImageCapture;
 

/**
 * 
 * @since 1.0.0
 */
public class ImageCapture implements IImageCapture{

	static{
		try{
			System.loadLibrary("swt-gtk-print");
		} catch (UnsatisfiedLinkError error){
			error.printStackTrace();
		}
	}
	static final int OBSCURED = 1<<6; // Must be the same value as Widget.OBSCURED
	static final String FIELD_STATE_NAME = "state"; 
	
	private native int[] getPixels(int handle, int includeChildren, int arg2, int arg3, int arg4);
	
	protected Point getTopLeftOfClientare(Decorations decorations){
		Point trim = decorations.toControl(decorations.getLocation());
		trim.x = -trim.x;
		trim.y = -trim.y;
		if(decorations.getMenuBar()!=null){
			Menu menu = decorations.getMenuBar();
			try{
				Class osClass = getClass().getClassLoader().loadClass("org.eclipse.swt.internal.gtk.OS");
				Method method = osClass.getMethod("GTK_WIDGET_HEIGHT", new Class[]{int.class});
				Object ret = method.invoke(menu, new Object[]{new Integer(menu.handle)});
				if(ret!=null){
					int menuBarHeight = ((Integer)ret).intValue();
					trim.y -= menuBarHeight;
				}
			}catch(Throwable t){}
		}
		return new Point(trim.x, trim.y);
	}
	
	protected Image getImageOfControl(Control control, int includeChildren){
		Image image = null;
		if (control instanceof Shell) {
			Shell shell = (Shell) control;
			int handle = readIntFieldValue(shell.getClass(), shell, "shellHandle");
			if(handle>0){
				image = getImageOfHandle(handle, shell.getDisplay(), includeChildren);
			}
		}
		if(image==null){
			image = getImageOfHandle(control.handle, control.getDisplay(), includeChildren);
		}
		if (control instanceof Decorations) {
			Decorations decorations = (Decorations) control;
			Rectangle shellBounds = decorations.getBounds();
			Point topLeft = getTopLeftOfClientare(decorations);
			Image realShellImage = new Image(decorations.getDisplay(), shellBounds.width, shellBounds.height);
			simulateDecoration(decorations, realShellImage, decorations.getBounds(), decorations.getClientArea(), topLeft);
			GC gc = new GC(realShellImage);
			gc.drawImage(image, topLeft.x, topLeft.y);
			gc.dispose();
			image.dispose();
			image = realShellImage;
		}
		return image;
	}
	
	protected Image getImageOfHandle(int handle, Display display, int includeChildren){
		int[] tcData = getPixels(handle, includeChildren,0,0,0);
		int depth = display.getDepth();
		if(depth==15)
			depth=16; // SWT cant handle depth of 15. Similar to 16
		if(depth>24)
			depth=24;
		if(tcData!=null){
			int tcWidth = tcData[0];
			int tcHeight = tcData[1];
			int type = tcData[2];
			if(type==1){
				// Direct RGB values
				int red_mask = tcData[3]==-1 ? 0x00FF:tcData[3];
				int green_mask = tcData[4]==-1 ? 0x00FF00:tcData[4];
				int blue_mask = tcData[5]==-1 ? 0x00FF0000:tcData[5];
				//System.err.println("Masks: "+Integer.toHexString(red_mask)+","+Integer.toHexString(green_mask)+","+Integer.toHexString(blue_mask));
				int[] tcPixels = new int[tcData.length-6];
				System.arraycopy(tcData, 6, tcPixels,0,tcPixels.length);
				ImageData tcImageData = new ImageData(tcWidth, tcHeight,depth,new PaletteData(red_mask, green_mask, blue_mask));
				tcImageData.setPixels(0,0,tcPixels.length,tcPixels,0);
				Image tcImage = new Image(display, tcImageData);
				return tcImage;
			}else if(type==2){
				// Indexed values
				int numColors = tcData[3];
				RGB[] rgb = new RGB[numColors];
				//System.err.println("### Num colors = "+numColors);
				for (int colCount = 0; colCount < numColors; colCount++) {
					int r = tcData[4+(colCount*3)+0];
					int g = tcData[4+(colCount*3)+1];
					int b = tcData[4+(colCount*3)+2];
					rgb[colCount] = new RGB(r,g,b);
				}
				PaletteData pd = new PaletteData(rgb);
				ImageData id = new ImageData(tcWidth, tcHeight, depth, pd);
				int offset = (4+(rgb.length*3));
				int pixels[] = new int[tcData.length-offset];
				System.arraycopy(tcData, offset, pixels, 0, pixels.length);
				id.setPixels(0,0,pixels.length,pixels,0);
				Image tcImage = new Image(display, id);
				//System.err.println("### returning image");
				return tcImage;
			}else{
				System.err.println("JNI Returned unknown image type");
			}
		}
		return null;
	}
	

	/**
	 * @param decoration
	 * @param realShellImage
	 * @param bounds
	 * @param clientArea
	 * @param topLeft
	 * 
	 * @since 1.0.0
	 */
	private void simulateDecoration(Decorations decoration, Image realShellImage, Rectangle bounds, Rectangle clientArea, Point topLeft) {
		GC gc = new GC(realShellImage);
		gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(0,0,bounds.width, bounds.height);
		gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		gc.drawRectangle(topLeft.x-1,topLeft.y-1,clientArea.width+2, clientArea.height+2);
		
		// little squares at bottom corners
		gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
		gc.fillRectangle(0,bounds.height-topLeft.y, topLeft.y, bounds.height);
		gc.fillRectangle(bounds.width-topLeft.y,bounds.height-topLeft.y, bounds.width, bounds.height);
		
		// title bar
		if((decoration.getStyle()&(SWT.TITLE|SWT.CLOSE|SWT.MAX|SWT.MIN))!=0 && topLeft.y>2){
			int barHeight = topLeft.y - 2;
			// There will be a title bar - draw the text
			gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
			gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
			gc.fillGradientRectangle(0,0,bounds.width, barHeight,false);
			gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
			gc.drawText(decoration.getText(), topLeft.y, 2, true);
			if(decoration.getImage()!=null && !decoration.getImage().isDisposed()) {
				Rectangle imageBounds = decoration.getImage().getBounds();
				if (imageBounds.height <= barHeight) {
					gc.drawImage(decoration.getImage(), 0,0);
				} else {
					ImageData imageData = decoration.getImage().getImageData();
					double factor = (double)barHeight / (double)imageBounds.height;
					int newWidth = (int)((double)imageBounds.width * factor);
					imageData = imageData.scaledTo(newWidth, barHeight);
					Image newImage = new Image(decoration.getDisplay(), imageData);
					gc.drawImage(newImage, 0, 0);
					newImage.dispose();
				}
			}			
			
			int rightx = bounds.width-topLeft.y;

			// title bar buttons
			if((decoration.getStyle()&SWT.CLOSE)!=0){
				gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				gc.fillRectangle(rightx,0,topLeft.y,topLeft.y);
				gc.setLineWidth(1);
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
				gc.drawRectangle(rightx,0,topLeft.y,topLeft.y);
				int lineWidth = topLeft.y/6;
				if(lineWidth<1)
					lineWidth = 1;
				gc.setLineWidth(lineWidth);
				lineWidth=lineWidth*2;
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER));
				gc.drawLine(rightx+lineWidth,lineWidth,rightx+topLeft.y-lineWidth,topLeft.y-lineWidth);
				gc.drawLine(rightx+lineWidth,topLeft.y-lineWidth,rightx+topLeft.y-lineWidth,lineWidth);
				rightx -= topLeft.y;
			}
			if((decoration.getStyle()&SWT.MAX)!=0){
				gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				gc.fillRectangle(rightx,0,topLeft.y,topLeft.y);
				gc.setLineWidth(1);
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
				gc.drawRectangle(rightx,0,topLeft.y,topLeft.y);
				int lineWidth = topLeft.y/6;
				if(lineWidth<1)
					lineWidth = 1;
				gc.setLineWidth(lineWidth);
				lineWidth=lineWidth*2;
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER));
				gc.drawRectangle(rightx+lineWidth,lineWidth,topLeft.y-(2*lineWidth),topLeft.y-(2*lineWidth));
				rightx -= topLeft.y;
			}
			if((decoration.getStyle()&SWT.MIN)!=0){
				gc.setLineWidth(1);
				gc.setBackground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				gc.fillRectangle(rightx,0,topLeft.y-1,topLeft.y-1);
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
				gc.drawRectangle(rightx,0,topLeft.y-1,topLeft.y-1);
				int lineWidth = topLeft.y/6;
				if(lineWidth<1)
					lineWidth = 1;
				gc.setLineWidth(lineWidth);
				lineWidth=lineWidth*2;
				gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER));
				gc.drawLine(rightx+lineWidth,topLeft.y-lineWidth,rightx+topLeft.y-lineWidth,topLeft.y-lineWidth);
				rightx -= topLeft.y;
			}

			gc.setLineWidth(1);
			gc.setForeground(decoration.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
			gc.drawLine(0, topLeft.y-1, bounds.width, topLeft.y-1);
		}
		
		gc.dispose();
	}
	
	/**
	 * @param object
	 * @param fieldName
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private int readIntFieldValue(Class klass, Object object, String fieldName) {
		try {
			Field field = klass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.getInt(object);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return -1;
	}
	
	/**
	 * @param object
	 * @param fieldName
	 * @return
	 * 
	 * @since 1.0.0
	 */
	private void writeIntFieldValue(Class klass, Object object, String fieldName, int newInt) {
		try {
			Field field = klass.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.setInt(object, newInt);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	/**
	 * @param shell
	 * @param selection
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public Image getImage(Control control, boolean includeChildren) {
		int ic = includeChildren?1:0;
		Map map = new HashMap();
		changeObscured(control, map, false);
		Image image = null;
		try {
			image = getImageOfControl(control, ic);
		} finally {
			changeObscured(control, map, true);
		}
		return image;
	}

	/**
	 * @param control
	 * @param map
	 * 
	 * @since 1.0.0
	 */
	private void changeObscured(Control control, Map map, boolean on) {
		if(on){
			// restoring the obscured flags
			if(map.containsKey(control)){
				// control had obscured flag changed - reset it
				Integer originalValue = (Integer) map.get(control);
				writeIntFieldValue(Widget.class, control, FIELD_STATE_NAME, originalValue.intValue());
			}
		}else{
			// disabling the obscure flags
			int stateValue = readIntFieldValue(Widget.class, control, FIELD_STATE_NAME);
			if((stateValue & OBSCURED)!=0){
				// obscured - disable flag and remember
				map.put(control, new Integer(stateValue));
				stateValue &= ~OBSCURED;
				writeIntFieldValue(Widget.class, control, FIELD_STATE_NAME, stateValue);
			}
		}
		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			Control[] children = composite.getChildren();
			for (int cc = 0; children!=null && cc < children.length; cc++) {
				changeObscured(children[cc], map, on);
			}
		}
	}

}
