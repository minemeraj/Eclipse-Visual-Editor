package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This is an implementation of JBCF containment that is for a single
 * structural feature. This will be the case the majority of the time.
 */
public class JavaContainerPolicy extends AbstractJavaContainerPolicy {
	
	protected EStructuralFeature containmentSF;
	
	public JavaContainerPolicy(EStructuralFeature containmentSF, EditDomain domain) {
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
	
	public Command getOrphanChildrenCommand(List children) {
		return getOrphanChildrenCommand(children, containmentSF);
	}
}
