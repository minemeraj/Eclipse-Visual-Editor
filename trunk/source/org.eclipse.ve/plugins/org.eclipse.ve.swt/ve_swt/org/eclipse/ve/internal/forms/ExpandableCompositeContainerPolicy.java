/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ExpandableCompositeContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:41 $ 
 */
package org.eclipse.ve.internal.forms;

import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

import org.eclipse.ve.internal.swt.CompositeContainerPolicy;

/**
 * Container policy for Expandable composite. It will handle that the "client" setting must also be set if the child is added. It will also be added
 * as the "controls" setting too.
 * 
 * @since 1.2.0
 */
public class ExpandableCompositeContainerPolicy extends CompositeContainerPolicy {

	private EReference sf_ecClient;

	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public ExpandableCompositeContainerPolicy(EditDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sf_ecClient = JavaInstantiation.getReference(rset, FormsConstants.SF_EXPANDABLECOMPOSITE_CLIENT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#isValidChild(java.lang.Object, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		// If valid by parent, we can't add if we already have the "client" set. You shouldn't have more than one child of an ExpandableComposite,
		// and it should be the client.
		return super.isValidChild(child, containmentSF) && !((EObject) getContainer()).eIsSet(sf_ecClient);
	}

	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		CommandBuilder cbld = new CommandBuilder();
		cbld.append(super.primCreateCommand(child, positionBeforeChild, containmentSF));
		cbld.applyAttributeSetting((EObject) getContainer(), sf_ecClient, child);
		return cbld.getCommand();
	}

	protected void getDeleteDependentCommand(Object child, CommandBuilder cbldr) {
		// Also need to unset the client setting. Don't need to worry about rulesbuilder because the super.delete will handle the child as a rule.
		cbldr.cancelAttributeSetting((EObject) getContainer(), sf_ecClient);
		super.getDeleteDependentCommand(child, cbldr);
	}

	protected void getOrphanTheChildrenCommand(List children, CommandBuilder cbldr) {
		// Also need to unset the client setting. Don't need to worry about rulesbuilder because the super.orphan will handle the child as a rule.
		cbldr.cancelAttributeSetting((EObject) getContainer(), sf_ecClient);
		super.getOrphanTheChildrenCommand(children, cbldr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#primAddCommand(java.util.List, java.lang.Object,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		CommandBuilder cbld = new CommandBuilder();
		cbld.append(super.primAddCommand(children, positionBeforeChild, containmentSF));
		cbld.applyAttributeSetting((EObject) getContainer(), sf_ecClient, children.get(0)); // We can be assured there is only one. We already tested
																							// for that.
		return cbld.getCommand();
	}

}
