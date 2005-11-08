package org.eclipse.ve.internal.swt.common;
/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
 *  $Revision: 1.2 $  $Date: 2005-11-08 22:33:17 $ 
 */

/**
 * This interface contains common statics that are shared 
 * between the remote vm and the ide.
 */

public interface Common {
	/**
	 * ControlListener message ids.
	 *
	 * The format of the data that is returned in
	 * callback will be:
	 * <pre>
	 *   resized: int[2] {width, height}
	 *   moved: int[2] {x, y}
	 *   refreshed: null
	 *   transactions: 3-tuple's of controlManager-sending-transaction,  callbackID(resized, etc), [parameters to transaction], ...
	 * </pre>
	 */
	public static final int 
		CL_RESIZED = 1,
		CL_MOVED = 2,
		CL_REFRESHED = 3,
		CL_TRANSACTIONS = 4,
		CL_IMAGEINVALID = 5;
	
	/**
	 * CompositeManagerExtension Listener message ids;
	 * The format of the data that is returned in
	 * callback will be:
	 * <pre>
	 *   invalid layout: IBeanProxy[n] controls_with_invalid_data
	 *   client area changed: int[6] {originOffset.x, originOffset.y, clientArea.x, clientArea.y, clientArea.width, clientArea.height}
	 * </pre>
	 */
	public static final int
		CMPL_INVALID_LAYOUT = 1,
		CMPL_CLIENTAREA_CHANGED = 2;
		
}