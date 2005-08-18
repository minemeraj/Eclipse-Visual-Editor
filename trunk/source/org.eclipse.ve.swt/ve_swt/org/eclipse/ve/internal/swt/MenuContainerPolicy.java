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
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
 

/**
 * 
 * @since 1.1.0.1
 */
public class MenuContainerPolicy extends SWTWidgetContainerPolicy {

	public MenuContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_MENU_ITEMS), domain);
	}

}
