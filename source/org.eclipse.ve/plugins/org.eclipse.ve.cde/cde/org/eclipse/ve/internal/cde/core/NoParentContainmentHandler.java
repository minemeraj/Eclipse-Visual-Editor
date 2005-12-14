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
 *  $RCSfile: NoParentContainmentHandler.java,v $
 *  $Revision: 1.2 $  $Date: 2005-12-14 21:27:11 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
 

/**
 * This is generic handler to prevent any containment. This is for those classes which should never be dropped or moved around. They
 * are for classes that are explicitly handled/moved/created through code and not through palette or choose class.
 *  
 * @since 1.2.0
 */
public class NoParentContainmentHandler implements IContainmentHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#contributeToDropRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation,
			EditDomain domain) throws StopRequestException {
		throw new StopRequestException(CDEMessages.NoParentContainmentHandler_StopRequest_DropNotAllowed_Msg);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#contributeToRemoveRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToRemoveRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean orphan,
			EditDomain domain) throws StopRequestException {
		// Allow normal delete to proceed.
		return child;
	}

}
