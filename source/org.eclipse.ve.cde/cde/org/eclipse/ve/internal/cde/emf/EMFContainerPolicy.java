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
 *  $RCSfile: EMFContainerPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This is an implementation of EMF containment that is for a single
 * structural feature. This will be the case the majority of the time.
 * @deprecated Just use AbstractEMFContainerPolicy directly as the supertype.
 */
public abstract class EMFContainerPolicy extends AbstractEMFContainerPolicy {
	
	
	public EMFContainerPolicy(EStructuralFeature containmentSF, EditDomain domain) {
		super(containmentSF, domain);
	}
	
}
