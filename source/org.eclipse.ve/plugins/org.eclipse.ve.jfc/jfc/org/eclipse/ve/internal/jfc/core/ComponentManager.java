/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ComponentManager.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-08 11:52:37 $ 
 */

import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;

import org.eclipse.jem.internal.proxy.awt.IDimensionBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.IVisualComponentListener;
import org.eclipse.ve.internal.cde.core.VisualComponentSupport;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.jfc.common.Common;
/**
 * This is the IDE class that is the callback listener for org.eclipse.ve.internal.jfc.vm.ComponentListener
 * that is running in the target VM
 * Implementors of IVisualComponentListeners can add themselves as listeners to this
 * and they will be notified when the component moves, resizes, or is hidden or shown
 */
public class ComponentManager implements ICallback {
	
	protected VisualComponentSupport vcSupport = new VisualComponentSupport();
	protected IBeanProxy fComponentManagerProxy;
	protected IBeanProxy fComponentBeanProxy;
	private Point fLastSignalledLocation;
	private Dimension fLastSignalledSize;
	
public void addComponentListener(IVisualComponentListener aListener){
	vcSupport.addComponentListener(aListener);
}
public void removeComponentListener(IVisualComponentListener aListener){
	vcSupport.removeComponentListener(aListener);
}
/**
 * Set the bean proxy of the component that we are listening to
 */
public void setComponentBeanProxy(IBeanProxy aComponentBeanProxy){
	// Deregister any listening from the previous non null component bean proxy
	if ( fComponentBeanProxy != null )
		BeanAwtUtilities.invoke_set_ComponentBean_Manager(fComponentManagerProxy, null);
		
	fComponentBeanProxy = aComponentBeanProxy;
	if ( fComponentBeanProxy != null ) {
		try {
			if (fComponentManagerProxy == null) {
				IBeanTypeProxy componentManagerType = fComponentBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ComponentManager"); //$NON-NLS-1$
				fComponentManagerProxy = componentManagerType.newInstance();
				aComponentBeanProxy.getProxyFactoryRegistry().getCallbackRegistry().registerCallback(fComponentManagerProxy,this);
			}
			BeanAwtUtilities.invoke_set_ComponentBean_Manager(fComponentManagerProxy, fComponentBeanProxy);	
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
	}
}

/**
 * Set the relative parent of the component we are hosting.  This is the parent that 
 * we calculate positional offsets from
 */
public void setRelativeParentComponentBeanProxy(IBeanProxy aContainerBeanProxy){
		BeanAwtUtilities.invoke_set_RelativeParent_Manager(fComponentManagerProxy, aContainerBeanProxy);	
}

public Object calledBack(int msgID, Object parm){
	switch ( msgID ) {		
		case Common.CL_HIDDEN :
			componentHidden();
			break;
		case Common.CL_SHOWN :
			componentShown();
			break;
		case Common.CL_REFRESHED :
			Object[] bounds = (Object[]) parm; 
			fLastSignalledLocation = new Point(((Integer)bounds[0]).intValue(),((Integer)bounds[1]).intValue());
			fLastSignalledSize = new Dimension(((Integer)bounds[2]).intValue(),((Integer)bounds[3]).intValue());
			fireComponentRefresh();
			break;
	}
	return null;
}

public Object calledBack(int msgID, IBeanProxy parm){
	throw new RuntimeException("A component listener has been called back incorrectly"); //$NON-NLS-1$
}
public void calledBackStream(int msgID, java.io.InputStream is){
	throw new RuntimeException("A component listener has been called back incorrectly"); //$NON-NLS-1$
}
public Object calledBack(int msgID, Object[] parms){
	switch ( msgID ) {
		case Common.CL_RESIZED : 
			componentResized(
				((IIntegerBeanProxy)parms[0]).intValue(),
				((IIntegerBeanProxy)parms[1]).intValue()				
			);
			break;
		case Common.CL_MOVED :
			componentMoved(
				((IIntegerBeanProxy)parms[0]).intValue(),
				((IIntegerBeanProxy)parms[1]).intValue()
			);
			break;			
	}
	return null;
}
protected void componentResized(int width, int height){
	fLastSignalledSize = new Dimension(width,height);
	vcSupport.fireComponentResized(width, height);
}
protected void componentMoved(int x, int y){
	fLastSignalledLocation = new Point(x,y);
	vcSupport.fireComponentMoved(x, y);
}
protected void componentHidden(){
	vcSupport.fireComponentHidden();
}
protected void componentShown(){
	vcSupport.fireComponentShown();
}
/**
 * fireComponentRefresh. Send out a refresh notification.
 */
public void fireComponentRefresh() {
	vcSupport.fireComponentRefreshed();
}

public void dispose(){
	if ( fComponentManagerProxy != null) {
		if (fComponentManagerProxy.isValid()) {
			// Created and not already released (could of been released due to registry shutdown and is now being GC'd.
			BeanAwtUtilities.invoke_set_ComponentBean_Manager(fComponentManagerProxy, null);
			fComponentManagerProxy.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(fComponentManagerProxy);
			fComponentManagerProxy.getProxyFactoryRegistry().releaseProxy(fComponentManagerProxy);
		}
		fComponentManagerProxy = null;
	}
}

public Rectangle getBounds() {

	if(fLastSignalledLocation != null && fLastSignalledSize != null){
		return new Rectangle(fLastSignalledLocation.x,fLastSignalledLocation.y,fLastSignalledSize.width,fLastSignalledSize.height);
	}
	
	try {
		IArrayBeanProxy boundsProxy = BeanAwtUtilities.invoke_get_Bounds_Manager(fComponentManagerProxy);
		return new Rectangle(
				((IIntegerBeanProxy) boundsProxy.get(0)).intValue(), 
				((IIntegerBeanProxy) boundsProxy.get(1)).intValue(),
				((IIntegerBeanProxy) boundsProxy.get(2)).intValue(),
				((IIntegerBeanProxy) boundsProxy.get(3)).intValue());
	} catch (ThrowableProxy e) {
		return new Rectangle();
	}
	
}

/**
 * Return the location relative to our parent component
 */
public Point getLocation() {
	
	if(fLastSignalledLocation != null){
		return fLastSignalledLocation;
	}
	
	try {
		IArrayBeanProxy locProxy = BeanAwtUtilities.invoke_get_Location_Manager(fComponentManagerProxy);
		return new Point(((IIntegerBeanProxy) locProxy.get(0)).intValue(), ((IIntegerBeanProxy) locProxy.get(1)).intValue());
	} catch (ThrowableProxy e) {
		return new Point();
	}
}

public Dimension getSize() {
	
	if(fLastSignalledSize != null){
		return fLastSignalledSize;
	}
	
	IDimensionBeanProxy sizeProxy = BeanAwtUtilities.invoke_getSize(fComponentBeanProxy);
	return new Dimension(sizeProxy.getWidth(),sizeProxy.getHeight());
}
/**
 * @param visualComponentBeanProxy for the Component itself
 * @param parentContainerBeanProxy for the relative parent container
 */
public void setComponentAndParent(IBeanProxy aComponentBeanProxy, IBeanProxy parentContainerBeanProxy) {
	
//	 Deregister any listening from the previous non null component bean proxy
	if ( fComponentBeanProxy != null )
		BeanAwtUtilities.invoke_set_ComponentBean_Manager(fComponentManagerProxy, null);
		
	fComponentBeanProxy = aComponentBeanProxy;
	if ( fComponentBeanProxy != null ) {
		try {
			if (fComponentManagerProxy == null) {
				IBeanTypeProxy componentManagerType = fComponentBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ComponentManager"); //$NON-NLS-1$
				fComponentManagerProxy = componentManagerType.newInstance();
				aComponentBeanProxy.getProxyFactoryRegistry().getCallbackRegistry().registerCallback(fComponentManagerProxy,this);
				BeanAwtUtilities.invoke_set_ComponentAndParentBean_Manager(fComponentManagerProxy,aComponentBeanProxy,parentContainerBeanProxy);				
			}
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}		
	}
	
}
}
