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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: EMFEditDomainHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
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
