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
 *  $RCSfile: AbstractJavaContainerPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */

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
 * This is an abstract class for container policies within Java VE. What it does special is
 * to handle using the appropriate rules for creating/deleting children.
 */
public abstract class AbstractJavaContainerPolicy extends AbstractEMFContainerPolicy {

	/**
	 * Constructor for AbstractJavaContainerPolicy.
	 * @param domain
	 */
	public AbstractJavaContainerPolicy(EditDomain domain) {
		super(domain);
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
