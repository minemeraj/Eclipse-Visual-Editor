package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventCallback.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.ve.internal.jcm.*;

/**
 * This is used by the JavaBeans tree view model to show an event callback
 */
public class EventCallback {
	
	private Callback callback;
	private AbstractEventInvocation eventInvocation;
	
	public EventCallback(AbstractEventInvocation anEventInvocation, Callback aCallback){
		callback = aCallback;
		eventInvocation = anEventInvocation;
	}
	public Callback getCallback() {
		return callback;
	}
	public AbstractEventInvocation getEventInvocation() {
		return eventInvocation;
	}
	

}
