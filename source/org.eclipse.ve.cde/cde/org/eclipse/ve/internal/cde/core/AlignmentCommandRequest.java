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
 *  $RCSfile: AlignmentCommandRequest.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */



import org.eclipse.gef.Request;
/**
 * Request for Alignment on a specific component.
 * Typically this is rerouted to the parent as an
 * AlignmentChildRequest by the Primary Drag Role Edit Policy,
 * since that policy typically handles move/size requests.
 *
 * Note: Processors of this request must return a
 * command. If no command is returned, this is interpreted
 * as alignment not permitted. So if no change in position
 * was performed, but alignment was permitted, then return
 * NoOpCommand.INSTANCE so that it is known the alignment
 * was allowed. This may be done by the AlignmentChildCommandRequest
 * processing instead. In this manner if no one processes the
 * request then it is assumed it is not alignable (such as if the
 * child is on a Flow Layout, there align doesn't make sense).
 *
 */
public class AlignmentCommandRequest extends Request {
	/**
	 * The alignment types.
	 */
	public final static int
		LEFT_ALIGN = 0,
		CENTER_ALIGN = 1,
		RIGHT_ALIGN = 2, 
		TOP_ALIGN = 3,
		MIDDLE_ALIGN = 4,
		BOTTOM_ALIGN = 5,
		MATCH_WIDTH = 6,
		MATCH_HEIGHT = 7;

	protected int fAlignType;
	protected Object fAnchorObject;

public AlignmentCommandRequest(int alignType) {
	super(RequestConstantsCDE.REQ_ALIGNMENT);
	fAlignType = alignType;
}
public int getAlignType() {
	return fAlignType;
}
public Object getAnchorObject() {
	return fAnchorObject;
}
public void setAnchorObject(Object newAnchor) {
	fAnchorObject = newAnchor;
}
}
