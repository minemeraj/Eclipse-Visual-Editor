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
 *  $RCSfile: DefaultComponentEditPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */

import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.gef.requests.GroupRequest;
/**
 * A default ComponentEditPolicy to use because the GEF ComponentEditPolicy 
 * no longer sends the request onto the parent to handle.
 */
public class DefaultComponentEditPolicy extends ComponentEditPolicy {
	
	/**
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(GroupRequest)
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		ForwardedRequest forwardedRequest = new ForwardedRequest(RequestConstants.REQ_DELETE_DEPENDANT, getHost());
		return getHost().getParent().getCommand(forwardedRequest);
	}
}