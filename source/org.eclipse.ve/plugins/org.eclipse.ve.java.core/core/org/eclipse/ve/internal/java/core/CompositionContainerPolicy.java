package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: CompositionContainerPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-26 23:08:01 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.jcm.JCMPackage;
/**
 * Container Edit Policy for Bean Compositions.
 */
public class CompositionContainerPolicy extends JavaContainerPolicy {
	
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