package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: ComponentManager.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-04 21:25:42 $ 
 */

import org.eclipse.draw2d.geometry.*;

import org.eclipse.ve.internal.cde.core.IVisualComponentListener;
import org.eclipse.ve.internal.cde.core.VisualComponentSupport;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.jfc.common.Common;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.awt.IDimensionBeanProxy;
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
			JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
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
	vcSupport.fireComponentResized(width, height);
}
protected void componentMoved(int x, int y){
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
	return new Rectangle(getLocation(), getSize());
}

/**
 * Return the location relative to our parent component
 */
public Point getLocation() {
	try {
		IArrayBeanProxy locProxy = BeanAwtUtilities.invoke_get_Location_Manager(fComponentManagerProxy);
		return new Point(((IIntegerBeanProxy) locProxy.get(0)).intValue(), ((IIntegerBeanProxy) locProxy.get(1)).intValue());
	} catch (ThrowableProxy e) {
		return new Point();
	}
}

public Dimension getSize() {
	IDimensionBeanProxy sizeProxy = BeanAwtUtilities.invoke_getSize(fComponentBeanProxy);
	return new Dimension(sizeProxy.getWidth(),sizeProxy.getHeight());
}
}