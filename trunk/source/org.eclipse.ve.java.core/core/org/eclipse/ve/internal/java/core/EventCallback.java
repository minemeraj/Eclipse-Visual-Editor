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
 *  $RCSfile: EventCallback.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
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
