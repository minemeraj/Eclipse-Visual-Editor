package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DistributeChildCommandRequest.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */



import org.eclipse.gef.Request;
import org.eclipse.gef.EditPart;
/**
 * Request for Distribute on a a child of this component.
 * Typically this has been rerouted to this parent
 * by the Primary Drag Role Edit Policy,
 * since that policy typically handles move/size requests.
 *
 * Note: Processors of this request must return a
 * command. If no command is returned, this is interpreted
 * as distribute not permitted. So if no change in position
 * was performed, but distribute was permitted, then return
 * NoOpCommand.INSTANCE so that it is known the distribute
 * was allowed. In this manner if no one processes the
 * request then it is assumed it is not distributable (such as if the
 * child is on a Flow Layout, there distribute doesn't make sense).
 *
 */
public class DistributeChildCommandRequest extends Request {
	protected DistributeCommandRequest fDistributeReq;
	protected EditPart fEditPart;
	
public DistributeChildCommandRequest(EditPart childEditPart, DistributeCommandRequest distributeReq) {
	super(RequestConstantsCDE.REQ_DISTRIBUTE_CHILD);
	fEditPart = childEditPart;
	fDistributeReq = distributeReq;
}

public DistributeCommandRequest getDistributeRequest() {
	return fDistributeReq;
}

public EditPart getChildEditPart() {
	return fEditPart;
}
}
