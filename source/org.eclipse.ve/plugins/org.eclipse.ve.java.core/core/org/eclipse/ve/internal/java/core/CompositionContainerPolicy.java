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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFContainerPolicy;
import org.eclipse.ve.internal.jcm.JCMPackage;
/**
 * Container Edit Policy for Bean Compositions.
 */
public class CompositionContainerPolicy extends EMFContainerPolicy {
	
	public CompositionContainerPolicy(EditDomain domain) {
		super(JCMPackage.eINSTANCE.getBeanComposition_Components(), domain);
	}

}