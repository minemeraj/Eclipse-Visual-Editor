package org.eclipse.ve.internal.cde.emf;
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
 *  $RCSfile: AbstractEMFContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
/**
 * Container Policy for EMF (MOF) containment relationship.
 * The structural feature is to be passed in from subclasses.
 * This allows the subclass to determine the feature to use, in cases
 * where it may change depending upon conditions.
 */
public abstract class AbstractEMFContainerPolicy extends ContainerPolicy {

	public AbstractEMFContainerPolicy(EditDomain domain) {
		super(domain);
	}

	/**
	 * Is this a valid child of this container.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		// By default it is the type of the structural feature. 
		return containmentSF.getEType().isInstance(child);
	}
	
	/**
	 * Is parent acceptable. Sometimes even though the parent may accept
	 * a child, the child may not accept the parent.
	 */
	protected boolean isParentAcceptable(Object child) {
		IModelAdapterFactory fact = CDEUtilities.getModelAdapterFactory(domain);
		if (fact != null) {
			IContainmentHandler handler = (IContainmentHandler) fact.getAdapter(child, IContainmentHandler.class);
			if (handler != null)
				return handler.isParentValid(container);
		}
		return true;
	}

	/**
	 * Create a  child.
	 * NOTE: Should not be getting create request on a non-composite containment.
	 * That doesn't make sense, it should be an add instead. So we don't test for that.
	 */
	protected Command getCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!isValidChild(child, containmentSF) || !isParentAcceptable(child))
			return UnexecutableCommand.INSTANCE;
		return primCreateCommand(child, positionBeforeChild, containmentSF);
	}

	/**
	 * Internal create a child where we have the child has already been tested for validity.
	 */
	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && ((EObject) container).eIsSet(containmentSF))
			return UnexecutableCommand.INSTANCE; // This is a single valued feature, and it is already set.

		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.applyAttributeSetting((EObject) container, containmentSF, child, positionBeforeChild);
		List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy());
		return AnnotationPolicy.getCreateRequestCommand(annotations, cBld.getCommand(), domain);
	}

	/**
	 * Add a  child.
	 */
	protected Command getAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			if (!isValidChild(child, containmentSF) || !isParentAcceptable(child))
				return UnexecutableCommand.INSTANCE;
		}

		return primAddCommand(children, positionBeforeChild, containmentSF);
	}

	/**
	 * Internal add children where we have the children already tested for validity..
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && (((EObject) container).eIsSet(containmentSF) || children.size() > 1))
			return UnexecutableCommand.INSTANCE;	// This is a single valued feature, and it is already set or there is more than one child being added.

		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
		return cBld.getCommand();
	}

	/**
	 * Delete a  child.
	 */
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.cancelAttributeSetting((EObject) container, containmentSF, child);
		if (containmentSF instanceof EReference && ((EReference) containmentSF).isContainment()) {
			// A containment feature means we have annotations to worry about.
			List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy());
			return AnnotationPolicy.getDeleteDependentCommand(annotations, cBld.getCommand(), domain.getDiagramData());
		}
		return cBld.getCommand(); // No annotations if not composite because the child is owned by someone else and is not going away.
	}

	/**
	 * Orphan  children.
	 */
	protected Command getOrphanChildrenCommand(List children, EStructuralFeature containmentSF) {
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.cancelAttributeSettings((EObject) container, containmentSF, children);
		return cBld.getCommand();
	}

	/**
	 * Move the  children.
	 */
	protected Command getMoveChildrenCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		if (children.contains(positionBeforeChild))
			return UnexecutableCommand.INSTANCE;
		// We clone the list because cancelAttributeSettings fools around with the list itself.
		cBld.cancelAttributeSettings((EObject) container, containmentSF, new ArrayList(children));
		cBld.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
		return cBld.getCommand();
	}

}
