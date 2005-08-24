/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: DistributeCommandRequest.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */



import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
/**
 * Request for Distribute on a specific component.
 * Typically this is rerouted to the parent as an
 * DistributeChildCommandRequest by the Primary Drag Role Edit Policy,
 * since that policy typically handles move/size requests.
 *
 * Note: Processors of this request must return a
 * command. If no command is returned, this is interpreted
 * as distribute not permitted. So if no change in position
 * was performed, but distribute was permitted, then return
 * NoOpCommand.INSTANCE so that it is known the distribute
 * was allowed. This may be done by the DistributeChildCommandRequest
 * processing instead. In this manner if no one processes the
 * request then it is assumed it is not distributable (such as if the
 * child is on a Flow Layout, there distribute doesn't make sense).
 *
 */
public class DistributeCommandRequest extends Request {
	/*
	 * The distribute types.
	 */
	public final static int 
		HORIZONTAL = 0,
		VERTICAL = 1;
		
	protected Rectangle bounds;	// Bounds of the figure of this request
	
public DistributeCommandRequest() {
	super(RequestConstantsCDE.REQ_DISTRIBUTE);
}


/**
 * The bounds to distriubte too.
 */
public Rectangle getBounds() {
	return bounds;
}

public void setBounds(Rectangle rect) {
	bounds = rect;
}
}
