package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: CDERequest.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

/**
 * Interface to allow GEF requests to have arbitary key value pairs
 * This allows a level of communication between the tool and the edit policy so that things
 * such as cursors and error messages can be passed back and forth
 */
public interface CDERequest {
	
	void put(Object key, Object value);
	Object get(Object key);

}
