/*****************************************************************************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms
 * of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: ToolBarContainerPolicy.java,v $ $Revision: 1.1 $ $Date: 2004-08-22 22:42:51 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

public class ToolBarContainerPolicy extends CompositeContainerPolicy {

	public ToolBarContainerPolicy(EditDomain domain) {
		super(domain);
		// Override the containment feature from CompositeContainer
		containmentSF = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_TOOLBAR_ITEMS);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#getCreateCommand(java.lang.Object, java.lang.Object)
	 */
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		// TODO Auto-generated method stub
		return super.getCreateCommand(child, positionBeforeChild);
	}
}