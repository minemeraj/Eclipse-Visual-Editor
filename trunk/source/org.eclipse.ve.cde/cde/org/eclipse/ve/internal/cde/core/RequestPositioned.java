package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RequestPositioned.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */

import org.eclipse.gef.Request;

/**
 * Request to place before a child (position). It must be an actual
 * child, not an Editpart.
 */

public class RequestPositioned extends Request {
	protected Request request;
	protected Object position;
	
	public RequestPositioned(Object type, Request request, Object position) {
		super(type);
		
		this.request = request;
		this.position = position;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public Object getPosition() {
		return position;
	}

}