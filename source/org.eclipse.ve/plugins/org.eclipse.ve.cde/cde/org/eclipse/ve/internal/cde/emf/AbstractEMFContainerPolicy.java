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
 *  $RCSfile: AbstractEMFContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-18 21:54:35 $ 
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

	/**
	 * Construct with just domain. This would be used if there can be more
	 * than one containment feature or if the feature will be determined later
	 * when the container has been set.
	 * @param domain
	 * 
	 * @since 1.1.0.1
	 */
	public AbstractEMFContainerPolicy(EditDomain domain) {
		this(null, domain);
	}
	
	/**
	 * Construct with a single containment feature.
	 * @param feature
	 * @param domain
	 * 
	 * @since 1.1.0.1
	 */
	public AbstractEMFContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(domain);
		setContainerFeature(feature);
	}
	
	protected static final int 
		ADD_REQ = 0,	// The request for the containment feature is an add request. Children are currently not contained in this container.
		CREATE_REQ = 1,	// The request for the containment feature is a create request. Child is currently not contained in this container.		
		MOVE_REQ = 2,	// The request for the containment feature is a move request. Children are currently contained in this container.
		DELETE_REQ = 3,	// The request for the containment feature is a delete request. Child is currently contained in this container.
		ORPHAN_REQ = 4;	// The request for the containment feature is an orphan request. Children are currently contained in this container.

	
	/**
	 * Used by default as the containment feature. Subclasses should override
	 * {@link #getContainmentSF(List, int)} and {@link #getContainmentSF(Object, int)}
	 * to return a different feature if the container can have multiple containment features.
	 * By default the getContainmentSF methods will return this setting.
	 * <p>
	 * Use {@link #setContainerFeature(EStructuralFeature)} to changed it.
	 */
	protected EStructuralFeature containmentSF;
	
	/**
	 * Call this to set the containment feature if it is to be changed after construction.
	 * <p>
	 * <b>Note:</b> This is not meant to be dynamic in that as the commands are requested that
	 * the containment feature is to be changed. Instead override the {@link #getContainmentSF(Object, int)}
	 * and {@link #getContainmentSF(List, int)} methods instead if it is dynamic. This method is used
	 * for when the containment feature depends on the container type itself and can't be known until
	 * {@link ContainerPolicy#setContainer(Object)} is called. Then the override class can also
	 * call this method to change the containment feature.
	 * @param containmentSF
	 * 
	 * @since 1.1.0.1
	 */
	protected void setContainerFeature(EStructuralFeature containmentSF) {
		this.containmentSF = containmentSF;
	}
	/**
	 * Return the appropriate structural feature for this child.
	 * Sent with CREATE_REQ and DELETE_REQ.
	 * <p>
	 * By default it is the containmentSF set by {@link #setContainerFeature(EStructuralFeature)}.
	 * 
	 * @param child
	 * @param positionBeforeChild the child (if not null) to position before. This may determine the feature type. This
	 * may be null if position before not known, or if add to end, or if not an add/create type request.
	 * @param requestType The request type constant. 
	 * @return The containment Structural feature for the child. null if not valid for containment.
	 */
	protected EStructuralFeature getContainmentSF(Object child, Object positionBeforeChild, int requestType) {
		return containmentSF;
	}
	
	/**
	 * Return the appropriate structural feature for these children.
	 * Sent with ADD_REQ, MOVE_REQ, ORPHAN_REQ.
	 * <p>
	 * By default it is the containmentSF set by {@link #setContainerFeature(EStructuralFeature)}.
	 * 
	 * @param children
	 * @param positionBeforeChild the child (if not null) to position before. This may determine the feature type.
	 * @param requestType The request type constant
	 * @return The containment Structural feature for the children. null if not valid for containment.
	 */
	protected EStructuralFeature getContainmentSF(List children, Object positionBeforeChild, int requestType) {
		return containmentSF;
	}


	/**
	 * Is this a valid child of this container. By default it checks that the child
	 * is a instanceof the EType of the feature.
	 * @param child
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		// By default it is the type of the structural feature. 
		return containmentSF.getEType().isInstance(child);
	}

	/**
	 * Is parent acceptable. Sometimes even though the parent may accept
	 * a child, the child may not accept the parent.
	 * @param child
	 * @return
	 * 
	 * @since 1.1.0.1
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
	
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(child, positionBeforeChild, CREATE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;
		return getCreateCommand(child, positionBeforeChild, containmentSF);
	}
	
	
	public Command getAddCommand(List children, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(children, positionBeforeChild, ADD_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getAddCommand(children, positionBeforeChild, containmentSF);
	}
	
	public Command getDeleteDependentCommand(Object child) {
		EStructuralFeature containmentSF = getContainmentSF(child, null, DELETE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getDeleteDependentCommand(child, containmentSF);
	}
	
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(children, positionBeforeChild, MOVE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getMoveChildrenCommand(children, positionBeforeChild, containmentSF);
	}
	
	protected Command getOrphanTheChildrenCommand(List children) {
		EStructuralFeature containmentSF = getContainmentSF(children, null, ORPHAN_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getOrphanChildrenCommand(children, containmentSF);
	}
	

	/**
	 * Create a  child. It tests validity too. 
	 * <p>
	 * Subclasses should override {@link #primCreateCommand(Object, Object, EStructuralFeature)} to change
	 * actual create processing.
	 * @param child
	 * @param positionBeforeChild
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected Command getCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!isValidChild(child, containmentSF) || !isParentAcceptable(child))
			return UnexecutableCommand.INSTANCE;
		return primCreateCommand(child, positionBeforeChild, containmentSF);
	}

	/**
	 * Internal create a child where we have the child has already been tested for validity. This should
	 * be overridden by subclasses to do something different.
	 * @param child
	 * @param positionBeforeChild
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
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
	 * Add children. It tests validity of the children too.
	 * <p>
	 * Subclasses should override {@link #primAddCommand(List, Object, EStructuralFeature)} to do something different.
	 * @param children
	 * @param positionBeforeChild
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
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
	 * <p>
	 * Subclasses should override this to do something different.
	 * @param children
	 * @param positionBeforeChild
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && (((EObject) container).eIsSet(containmentSF) || children.size() > 1))
			return UnexecutableCommand.INSTANCE;	// This is a single valued feature, and it is already set or there is more than one child being added.

		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
		return cBld.getCommand();
	}

	/**
	 * Delete a child.
	 * @param child
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
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
	 * Orphan children.
	 * @param children
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected Command getOrphanChildrenCommand(List children, EStructuralFeature containmentSF) {
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.cancelAttributeSettings((EObject) container, containmentSF, children);
		return cBld.getCommand();
	}

	/**
	 * Move children.
	 * @param children
	 * @param positionBeforeChild
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
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
