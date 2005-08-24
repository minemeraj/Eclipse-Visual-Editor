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
 *  $RCSfile: AlignmentChildCommandRequest.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:50 $ 
 */



import org.eclipse.gef.EditPart;
/**
 * Request for Alignment on a a child of this component.
 * Typically this has been rerouted to this parent
 * by the Primary Drag Role Edit Policy,
 * since that policy typically handles move/size requests.
 *
 * Note: Processors of this request must return a
 * command. If no command is returned, this is interpreted
 * as alignment not permitted. So if no change in position
 * was performed, but alignment was permitted, then return
 * NoOpCommand.INSTANCE so that it is known the alignment
 * was allowed. In this manner if no one processes the
 * request then it is assumed it is not alignable (such as if the
 * child is on a Flow Layout, there align doesn't make sense).
 *
 */
public class AlignmentChildCommandRequest extends ChildRequest {
	protected AlignmentCommandRequest fAlignRequest;

public AlignmentChildCommandRequest(EditPart editpart, AlignmentCommandRequest alignRequest) {
	super(RequestConstantsCDE.REQ_ALIGNMENT_CHILD, editpart);
	fAlignRequest = alignRequest;
}
public AlignmentCommandRequest getAlignmentRequest() {
	return fAlignRequest;
}
}
