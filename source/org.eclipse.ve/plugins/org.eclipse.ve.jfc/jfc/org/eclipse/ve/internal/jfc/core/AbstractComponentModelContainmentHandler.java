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
 *  $RCSfile: AbstractComponentModelContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-11-04 17:30:48 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;
 

/**
 * Default abstract ComponentModel Containment Handler. It implements the
 * default do nothing {@link IContainmentHandler#contributeToRemoveRequest(Object, Object, CommandBuilder, CommandBuilder, boolean, EditDomain)}.
 * @since 1.2.0
 */
public abstract class AbstractComponentModelContainmentHandler extends ComponentModelAdapter implements IContainmentHandler {

	/**
	 * @param component
	 * 
	 * @since 1.2.0
	 */
	public AbstractComponentModelContainmentHandler(Object component) {
		super(component);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#contributeToRemoveRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToRemoveRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean orphan, EditDomain domain) throws StopRequestException {
		return child;
	}

}
