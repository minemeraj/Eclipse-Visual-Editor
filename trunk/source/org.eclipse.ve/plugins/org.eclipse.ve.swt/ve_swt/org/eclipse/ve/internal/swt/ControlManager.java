/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.io.DataInputStream;
import java.util.logging.Level;

import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.IVisualComponentListener;
import org.eclipse.ve.internal.cde.core.VisualComponentSupport;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
/**
 * This is the IDE class that is the callback listener for ComponentListener
 * that is running in the target VM
 * Implementors of IVisualComponentListeners can add themselves as listeners to this
 * and they will be notified when the component moves, resizes, or is hidden or shown
 */
public class ControlManager implements ICallback {
	
//	private IMethodProxy fLocationMethodProxy;
	private IMethodProxy fClientBoxMethodProxy;	
	private IMethodProxy fControlManagerBoundsMethodProxy;		
	public static final int 
			CO_RESIZED = 1,
			CO_MOVED = 2,
			CO_REFRESHED = 3,
			IMAGE_INITIAL_LENGTH = 4,
			IMAGE_FINISHED = 5,
			IMAGE_COLOR_MASKS = 6;	
	protected VisualComponentSupport vcSupport = new VisualComponentSupport();
	protected IBeanProxy fControlManagerProxy;
	protected IBeanProxy fControlBeanProxy;
	protected int fWidth, fHeight, fX, fY;	
	
	class DataCollector{
		int fPointer , fWidth, fHeight;
		int[] data;
		boolean isComplete;
		ImageData fImageData;
		int depth, redMask, greenMask, blueMask;	
		public void setLength(int length){
			data = new int[length];
		}
		public void setSize(int width, int height){
			fWidth = width;
			fHeight = height;
		}
		public void write(int i) {
				data[fPointer] = i;
				fPointer++;
		}
		public void setComplete(){
			isComplete = true;
		}
		public ImageData getImageData(){
			if(isComplete && fImageData == null){
				fImageData = new ImageData(fWidth,fHeight,depth,new PaletteData(redMask,greenMask,blueMask));
				// Convert the int[] to a byte[]
				byte[] bytes = new byte[data.length];
				for (int i = 0; i < data.length; i++) {
					bytes[i] = (byte) data[i];
				}
				fImageData.data = bytes;
			}
			return fImageData;
		}
	}
	private DataCollector fDataCollector;
	
public void addComponentListener(IVisualComponentListener aListener){
	vcSupport.addComponentListener(aListener);
}
public void removeComponentListener(IVisualComponentListener aListener){
	vcSupport.removeComponentListener(aListener);
}

	public void release() {
		if (fControlManagerProxy != null && fControlManagerProxy.isValid()) {
			if (fControlBeanProxy != null) {
				invoke_setControlManager(fControlManagerProxy, null);
			}
			fControlManagerProxy.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(fControlManagerProxy);
		}
	}

	/**
 * Set the bean proxy of the component that we are listening to
 */
public void setControlBeanProxy(IBeanProxy aControlBeanProxy){

	// Deregister any listening from the previous non null control bean proxy
	if ( fControlBeanProxy != null ) {
		invoke_setControlManager(fControlManagerProxy, null);			
	}
		
	fControlBeanProxy = aControlBeanProxy;
	
	if ( fControlBeanProxy != null ) {
 		try {
			if (fControlManagerProxy == null) {
				// Get a new instance of a control manager proxy on the target VM				
				IBeanTypeProxy componentManagerType = null;
				if(Platform.OS_WIN32.equals(Platform.getOS()))
					componentManagerType = fControlBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.win32.Win32ControlManager"); //$NON-NLS-1$
				else if(Platform.WS_GTK.equals(Platform.getWS()))
					componentManagerType = fControlBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.unix.GTKControlManager"); //$NON-NLS-1$
				fControlManagerProxy = componentManagerType.newInstance();
				// Register a callback link between the target VM and us so that we get called back by it
				// when the control moves or is resized
				aControlBeanProxy.getProxyFactoryRegistry().getCallbackRegistry().registerCallback(fControlManagerProxy,this);
			}
			invoke_setControlManager(fControlManagerProxy, fControlBeanProxy);	
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
	}
}

private void invoke_setControlManager(IBeanProxy controlManagerProxy, IBeanProxy controlBeanProxy) {
	IMethodProxy setControlMethodProxy = controlManagerProxy.getTypeProxy().getMethodProxy("setControl","org.eclipse.swt.widgets.Control");
	setControlMethodProxy.invokeCatchThrowableExceptions(controlManagerProxy, controlBeanProxy);
}

public Object calledBack(int msgID, IBeanProxy parm){
	switch ( msgID ) {		
		case IMAGE_FINISHED :
			fDataCollector.setComplete();			
	}
	return null;
}
public Object calledBack(int msgID, Object parm){
	return null;
}
public void calledBackStream(int msgID, java.io.InputStream is){
	
	switch (msgID) {
		case 0:
			DataInputStream dataInput = new DataInputStream(is); 
			try{
				int nextInt = dataInput.read();
				while(nextInt != -1){
					// Each int is really four bytes - split it into these
					fDataCollector.write(nextInt);
					nextInt = dataInput.read();		
				}
			} catch (Exception exc) {
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			break;
		default:
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
				JavaVEPlugin.log("Invalid callback in ImageDataCollector="+msgID, Level.WARNING);	//$NON-NLS-1$
	}	
}
public ImageData getImageData(){
	return fDataCollector == null ? null : fDataCollector.getImageData();
}
public Object calledBack(int msgID, Object[] parms){
	switch ( msgID ) {
		case CO_RESIZED : 
			componentResized(
				((IIntegerBeanProxy)parms[0]).intValue(),
				((IIntegerBeanProxy)parms[1]).intValue()				
			);
			break;
		case CO_MOVED :
			componentMoved(
				((IIntegerBeanProxy)parms[0]).intValue(),
				((IIntegerBeanProxy)parms[1]).intValue()
			);
			break;	
		case CO_REFRESHED :
		    fX = ((IIntegerBeanProxy)parms[0]).intValue();
		    fY = ((IIntegerBeanProxy)parms[1]).intValue();
		    fWidth = ((IIntegerBeanProxy)parms[2]).intValue();
		    fHeight = ((IIntegerBeanProxy)parms[3]).intValue();
		    fireControlRefreshed();
		    break;			
		case IMAGE_INITIAL_LENGTH :
			fDataCollector = new DataCollector();
			fDataCollector.setLength(((IIntegerBeanProxy)parms[0]).intValue());
			fDataCollector.setSize(((IIntegerBeanProxy)parms[1]).intValue(),((IIntegerBeanProxy)parms[2]).intValue());
			break;
		case IMAGE_COLOR_MASKS :
			fDataCollector.depth = (((IIntegerBeanProxy)parms[0]).intValue());			
			fDataCollector.redMask = (((IIntegerBeanProxy)parms[1]).intValue());
			fDataCollector.greenMask = (((IIntegerBeanProxy)parms[2]).intValue());
			fDataCollector.blueMask = (((IIntegerBeanProxy)parms[3]).intValue());
			break;
	}
	return null;
}
protected void componentResized(int width, int height){
	vcSupport.fireComponentResized(width, height);
	fWidth = width;
	fHeight = height;
}
protected void componentMoved(int x, int y){
	vcSupport.fireComponentMoved(x, y);
	fX = x;
	fY = y;
}
protected void componentHidden(){
	vcSupport.fireComponentHidden();
}
protected void componentShown(){
	vcSupport.fireComponentShown();
}
public Dimension getSize(){
	
	return new Dimension(fWidth,fHeight);
}
public Point getLocation(){
	return new Point(fX,fY);
}
public Rectangle getBounds(){
    
    return new Rectangle(fX,fY,fWidth,fHeight);
    
}
/**
 * Send out a refresh notification - to avoid deadlocks do this asynchronously so that this method
 * can return
 */
public void fireControlRefreshed() {
	Display.getDefault().asyncExec(new Runnable(){
		public void run(){
			vcSupport.fireComponentRefreshed();			
		}
	});
}

public void dispose(){
	if ( fControlManagerProxy != null) {
		if (fControlManagerProxy.isValid()) {
			// Created and not already released (could of been released due to registry shutdown and is now being GC'd.
			invoke_setControlManager(fControlManagerProxy, null);
			fControlManagerProxy.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(fControlManagerProxy);
			fControlManagerProxy.getProxyFactoryRegistry().releaseProxy(fControlManagerProxy);
		}
		fControlManagerProxy = null;
	}
}
//private IMethodProxy getComponentManagerLocationMethodProxy(){
//	if(fLocationMethodProxy == null){
//		fLocationMethodProxy = fControlManagerProxy.getTypeProxy().getMethodProxy("getLocation");
//	}
//	return fLocationMethodProxy;	
//}
private IMethodProxy getControlManagerBoundsMethodProxy(){
	if(fControlManagerBoundsMethodProxy == null){
		fControlManagerBoundsMethodProxy = fControlManagerProxy.getTypeProxy().getMethodProxy("getBounds");
	}
	return fControlManagerBoundsMethodProxy;	
}
/**
 * Return the rectangle for the clientArea as defined as a box within the bounds, so the top left corner
 * is inset by the trim
 */
public Rectangle getClientBox() {
	if (fControlManagerProxy != null && fControlBeanProxy != null) {
		if (fClientBoxMethodProxy == null) {
			fClientBoxMethodProxy = fControlManagerProxy.getTypeProxy().getMethodProxy("getClientBox");
		}
		IRectangleBeanProxy fClientBoxBeanProxy = (IRectangleBeanProxy) fClientBoxMethodProxy.invokeCatchThrowableExceptions(fControlManagerProxy);
		return fClientBoxBeanProxy != null ? new Rectangle(fClientBoxBeanProxy.getX(), fClientBoxBeanProxy.getY(), fClientBoxBeanProxy.getWidth(), fClientBoxBeanProxy
				.getHeight()) : null;
	}
	return null;
}
public void captureImage(){
	if (fControlManagerProxy != null && fControlBeanProxy != null) {
		IMethodProxy collectImageMethodProxy = fControlManagerProxy.getTypeProxy().getMethodProxy("captureImage");
		collectImageMethodProxy.invokeCatchThrowableExceptions(fControlManagerProxy);
	}
}
}