package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaMultiFeatureContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-24 18:57:14 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This is an implementation of JBCF containment that can have more than one
 * structural feature for a child. It is used when the type of the child can
 * be used to determine the appropriate feature. If it requires some other criteria,
 * such as where it was dropped, that needs to be handled in a different kind of policy.
 */
public abstract class JavaMultiFeatureContainerPolicy extends AbstractJavaContainerPolicy {
	
	public JavaMultiFeatureContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	protected static final int 
		ADD_REQ = 0,	// The request for the containment feature is an add request. Children are currently not contained in this container.
		CREATE_REQ = 1,	// The request for the containment feature is a create request. Child is currently not contained in this container.		
	    MOVE_REQ = 2,	// The request for the containment feature is a move request. Children are currently contained in this container.
		DELETE_REQ = 3,	// The request for the containment feature is a delete request. Child is currently contained in this container.
		ORPHAN_REQ = 4;	// The request for the containment feature is an orphan request. Children are currently contained in this container.
	
	/**
	 * Return the appropriate structural feature for this child.
	 * Sent with CREATE_REQ and DELETE_REQ
	 * 
	 * @param child
	 * @param requestType The request type constant. 
	 * @return The containment Structural feature for the child. null if not valid for containment.
	 */
	protected abstract EStructuralFeature getContainmentSF(Object child, int requestType);
	
	/**
	 * Return the appropriate structural feature for these children.
	 * Sent with ADD_REQ, MOVE_REQ, ORPHAN_REQ
	 * 
	 * @param children
	 * @param requestType The request type constant
	 * @return The containment Structural feature for the children. null if not valid for containment.
	 */
	protected abstract EStructuralFeature getContainmentSF(List children, int requestType);
	
	
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(child, CREATE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;
		return getCreateCommand(child, positionBeforeChild, containmentSF);
	}
	
	
	public Command getAddCommand(List children, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(children, ADD_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getAddCommand(children, positionBeforeChild, containmentSF);
	}
	
	public Command getDeleteDependentCommand(Object child) {
		EStructuralFeature containmentSF = getContainmentSF(child, DELETE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getDeleteDependentCommand(child, containmentSF);
	}
	
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(children, MOVE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getMoveChildrenCommand(children, positionBeforeChild, containmentSF);
	}
	
	protected Command getOrphanTheChildrenCommand(List children) {
		EStructuralFeature containmentSF = getContainmentSF(children, ORPHAN_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getOrphanChildrenCommand(children, containmentSF);
	}

}
