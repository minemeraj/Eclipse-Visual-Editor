/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * @author richkulp
 *
 * This is an base class for container policies within Java VE. What it does special is
 * to handle using the appropriate rules for creating/deleting children.
 */
public class BaseJavaContainerPolicy extends AbstractEMFContainerPolicy {

	/**
	 * Construct with feature and domain. Used when this should be a single feature container.
	 * @param feature
	 * @param domain
	 * 
	 * @since 1.1.0.1
	 */
	public BaseJavaContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature, domain);
	}

	/**
	 * Constructor for BaseJavaContainerPolicy.
	 * @param domain
	 */
	public BaseJavaContainerPolicy(EditDomain domain) {
		super(domain);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getAddCommand(java.util.List, java.lang.Object, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command getAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		// Test done here and not in isValidChild because bean location only makes sense for add, not create.
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!isValidBeanLocation(child))
				return UnexecutableCommand.INSTANCE;
		}
		return super.getAddCommand(children, positionBeforeChild, containmentSF);
	}
	
	/**
	 * Is the child in valid bean location for the add. For example, if the child is located at LOCAL, can the
	 * child be validly added to this container. The default is true, but subclasses can override to provide
	 * their own tests.
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected boolean isValidBeanLocation (Object child) {
		return true;
	}
	
	/**
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getDeleteDependentCommand(Object, EStructuralFeature)
	 */
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		RuledCommandBuilder cBld = new RuledCommandBuilder(domain);
		cBld.setPropertyRule(false);	// Doing child right now.
		cBld.cancelAttributeSetting((EObject) container, containmentSF, child);
		return cBld.getCommand(); // No annotations if not composite because the child is owned by someone else and is not going away.
	}

	/**
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#primCreateCommand(Object, Object, EStructuralFeature)
	 */
	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && ((EObject) container).eIsSet(containmentSF))
			return UnexecutableCommand.INSTANCE; // This is a single valued feature, and it is already set.

		RuledCommandBuilder cBld = new RuledCommandBuilder(domain); 
		cBld.setPropertyRule(false);
		if (!containmentSF.isMany())
			cBld.applyAttributeSetting((EObject) container, containmentSF, child);
		else
			cBld.applyAttributeSetting((EObject) container, containmentSF, child, positionBeforeChild);
			
		return cBld.getCommand();
	}

}
