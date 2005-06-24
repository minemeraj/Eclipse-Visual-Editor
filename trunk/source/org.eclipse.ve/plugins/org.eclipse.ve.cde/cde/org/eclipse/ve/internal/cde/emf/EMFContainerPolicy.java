package org.eclipse.ve.internal.cde.emf;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EMFContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-24 18:57:15 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.gef.commands.Command;

/**
 * This is an implementation of EMF containment that is for a single
 * structural feature. This will be the case the majority of the time.
 */
public abstract class EMFContainerPolicy extends AbstractEMFContainerPolicy {
	
	protected EStructuralFeature containmentSF;
	
	public EMFContainerPolicy(EStructuralFeature containmentSF, EditDomain domain) {
		super(domain);
		this.containmentSF = containmentSF;
	}
	
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		return getCreateCommand(child, positionBeforeChild, containmentSF);
	}
	
	
	public Command getAddCommand(List children, Object positionBeforeChild) {
		return getAddCommand(children, positionBeforeChild, containmentSF);
	}
	
	public Command getDeleteDependentCommand(Object child) {
		return getDeleteDependentCommand(child, containmentSF);
	}
	
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		return getMoveChildrenCommand(children, positionBeforeChild, containmentSF);
	}
	
	protected Command getOrphanTheChildrenCommand(List children) {
		return getOrphanChildrenCommand(children, containmentSF);
	}
}