package org.eclipse.ve.internal.cde.emf;
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
 *  $RCSfile: EMFEditDomainHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * @version 	1.0
 * @author
 */
public class EMFEditDomainHelper {

	public static final String RESOURCE_SET_KEY = "org.eclipse.ve.internal.cde.emf.resourcesetkey"; //$NON-NLS-1$

	/**
	 * The resourceSet for the editor. This is the editor specific resourceSet.
	 */
	public static ResourceSet getResourceSet(EditDomain dom) {
		return (ResourceSet) dom.getData(EMFEditDomainHelper.RESOURCE_SET_KEY);
	}

	/**
	 * Set the resource set into the domain.
	 */
	public static void setResourceSet(ResourceSet rset, EditDomain dom) {
		dom.setData(EMFEditDomainHelper.RESOURCE_SET_KEY, rset);
	}

}
