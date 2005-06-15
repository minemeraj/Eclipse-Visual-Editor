package org.eclipse.ve.internal.jfc.common;
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
 *  $RCSfile: Common.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-15 20:19:27 $ 
 */

/**
 * This interface contains common statics that are shared 
 * between the remote vm and the ide.
 */

public interface Common {
	/**
	 * ComponentListener message ids.
	 *
	 * The format of the data that is returned in
	 * callback will be:
	 * <pre>
	 *   hidden: null
	 *   shown: null
	 *   resized: int[2] {width, height}
	 *   moved: int[2] {x, y}
	 *   refreshed: null
	 *   transactions: 3-tuple's of componentManager-sending-transaction,  callbackID(hidden, etc), [parameters to transaction], ...
	 * </pre>
	 */
	public static final int 
		CL_HIDDEN = 0,
		CL_SHOWN = 1,
		CL_RESIZED = 2,
		CL_MOVED = 3,
		CL_REFRESHED = 4,
		CL_TRANSACTIONS = 5,
		CL_IMAGEINVALID = 6;
		
}