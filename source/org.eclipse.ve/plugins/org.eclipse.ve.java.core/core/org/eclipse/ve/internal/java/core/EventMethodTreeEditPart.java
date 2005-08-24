/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: EventMethodTreeEditPart.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:46 $ 
 */

import java.util.Iterator;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.beaninfo.*;
import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.jcm.*;

/**
 */
public class EventMethodTreeEditPart extends AbstractTreeEditPart implements CallbackEditPart {
	
	protected Callback callback;
	protected AbstractEventInvocation eventInvocation;
	protected boolean isMethod;
	
	public EventMethodTreeEditPart(EventCallback anEventCallback){
		this(anEventCallback.getCallback(),anEventCallback.getEventInvocation());
	}
	
	/**
	 * Treat this as a child of the parent so that delete requests are forwarded to the parent
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy(){
				protected Command createDeleteCommand(GroupRequest deleteRequest) {
					ForwardedRequest forwardedRequest = new ForwardedRequest(JavaBeanEventUtilities.REQ_DELETE_EVENT_DEPENDANT, getHost());
					return getHost().getParent().getCommand(forwardedRequest);
				}			
			});
	}	
	
	public EventMethodTreeEditPart(Callback aCallback, AbstractEventInvocation anEventInvocation){
		super(aCallback);
		callback = aCallback;		
		eventInvocation = anEventInvocation;
	}
	public String getText(){
		// Get the method name - For NLS we need to ensure it's the one from the BeanInfo MethodDescriptor
		// There is no direct way in the model to that right now, so we have to get the EventDescriptor's methods
		// and iterate over them finding the one for our method and then using the decorator from this
		
		
		BeanEvent beanEvent = ((EventInvocation)callback.eContainer()).getEvent();
		EventSetDecorator eventSetDecor = Utilities.getEventSetDecorator(beanEvent);
		Iterator methods = eventSetDecor.getListenerMethods().iterator();
		while(methods.hasNext()){
			MethodProxy methodProxy = (MethodProxy)methods.next();
			if(methodProxy.getMethod() == callback.getMethod()) {
				MethodDecorator methodDecor = Utilities.getMethodDecorator(methodProxy);
				return methodDecor.getDisplayName();				
			}
			
		}
		// This should not occur, but in case it does we need to return just the basic method name	
		return callback.getMethod().getName();			
	}
	public Image getImage(){
		// Return the method image
		if (callback.isSharedScope())
		   return JavaBeanEventUtilities.getEventSharedArrowImage();
		else
		   return JavaBeanEventUtilities.getEventArrowImage();			
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IJavaElement.class) {
			// KLUDGE: Need to go to the Composition EP to get the JE. If we don't do this then the 
			// JavaBrowsing perspective will go blank when this EP is selected. This is annoying.
			return getRoot().getContents().getAdapter(key);
		}
		return super.getAdapter(key);
	}

	/* (non-Javadoc)
	 * Select and return true if we are for the argument
	 */
	public boolean selectCallback(Callback aCallback) {
		if(aCallback == callback){
			// Select us
			getViewer().flush();
			getViewer().select(this);
			return true;
		} else {
			return false;
		}
	}
	
}
