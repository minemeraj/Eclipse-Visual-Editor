/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: CompositionContainerPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-18 21:54:34 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.jcm.JCMPackage;
/**
 * Container Edit Policy for Bean Compositions.
 */
public class CompositionContainerPolicy extends BaseJavaContainerPolicy {
	
	public CompositionContainerPolicy(EditDomain domain) {
		super(JCMPackage.eINSTANCE.getBeanComposition_Components(), domain);
	}
	
	/**
	 * @param containmentSF
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	protected CompositionContainerPolicy(EStructuralFeature containmentSF, EditDomain domain) {
		super(containmentSF, domain);
	}
}
