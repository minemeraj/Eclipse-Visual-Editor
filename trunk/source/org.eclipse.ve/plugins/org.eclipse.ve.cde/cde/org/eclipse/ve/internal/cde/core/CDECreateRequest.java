package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDECreateRequest.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.requests.CreateRequest;


/**
 * Create request that allows key value pairs to be put onto the request by the edit policies
 * and used by the tools such as a cursor or more feedback for why the request was rejected
 */
public class CDECreateRequest extends CreateRequest implements CDERequest {
	
	private Map values;
	
	public void put(Object key, Object value){
		if ( values == null ) values = new HashMap(1);
		values.put(key,value);
	}
	public Object get(Object key){
		if ( values == null ) return null;
		return values.get(key);
	}
}
