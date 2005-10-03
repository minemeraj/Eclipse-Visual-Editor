/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: AbstractEMFContainerPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2005-10-03 19:21:04 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.NoAddException;
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
	 * Create a command builder.
	 * <p>
	 * Subclasses may override to provide a different command builder.
	 * 
	 * @param regCmd
	 * @return a new command builder to use
	 * 
	 * @since 1.2.0
	 */
	protected CommandBuilder createCommandBuilder(boolean regCmd) {
		return new CommandBuilder(regCmd);
	}
	
	private IModelAdapterFactory modelAdapterFactory;
	private boolean retrievedModelAdapterFactory;
	
	/**
	 * Get the model adapter factory.
	 * @return model adapter factory, or <code>null</code> if there isn't one.
	 * 
	 * @since 1.2.0
	 */
	protected final IModelAdapterFactory getModelAdapterFactory() {
		if (!retrievedModelAdapterFactory) {
			modelAdapterFactory = CDEUtilities.getModelAdapterFactory(domain);
			retrievedModelAdapterFactory = true;
		}
		return modelAdapterFactory;
	}

	/**
	 * Return the adjusted true child if {@link IContainmentHandler} decides for a different one.
	 * @param child child to send in
	 * @param creation <code>true</code> if this is for a creation, <code>false</code> if this for an add.
	 * @param preCmds CommandBuilder for commands to be executed before the adds
	 * @param postCmds CommandBuilder for commands to be executed after the adds
	 * @return the child to add  (maybe different than the one sent it) or <code>null</code> if handler handled child
	 * @throws NoAddException thrown if the handler determines that the parent is not valid for this child.
	 * 
	 * @since 1.2.0
	 */
	protected Object getTrueChild(Object child, boolean creation, CommandBuilder preCmds, CommandBuilder postCmds) throws NoAddException {
		// Go to the IContainmentHandler to decide what to do with the child.
		IModelAdapterFactory fact = getModelAdapterFactory();
		if (fact != null) {
			IContainmentHandler handler = (IContainmentHandler) fact.getAdapter(child, IContainmentHandler.class);
			if (handler != null) {
				child = handler.contributeToDropRequest(container, child, preCmds, postCmds, creation, getEditDomain());
			}
		}
		return child;
	}
	
	public final Command getCreateCommand(Object child, Object positionBeforeChild) {
		CommandBuilder preCmds = createCommandBuilder(true), postCmds = createCommandBuilder(true);
		try {
			child = getTrueChild(child, true, preCmds, postCmds);
		} catch (NoAddException e) {
			return UnexecutableCommand.INSTANCE;
		}

		if (preCmds.isDead() || postCmds.isDead())
			return UnexecutableCommand.INSTANCE;
		
		if (child != null) {
			getCreateCommand(child, positionBeforeChild, preCmds);
		}

		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			return UnexecutableCommand.INSTANCE;
		else
			return preCmds.getCommand();
	}

	protected void getCreateCommand(Object child, Object positionBeforeChild, CommandBuilder cbldr) {
		EStructuralFeature containmentSF = getContainmentSF(child, positionBeforeChild, CREATE_REQ);
		if (containmentSF == null)
			cbldr.markDead();
		else
			cbldr.append(getCreateCommand(child, positionBeforeChild, containmentSF));
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
		if (!isValidChild(child, containmentSF))
			return UnexecutableCommand.INSTANCE;
		else
			return primCreateCommand(child, positionBeforeChild, containmentSF);
	}

	/**
	 * Internal create a child where we have the child has already been tested for validity. This should
	 * be overridden by subclasses to do something different.
	 * @param child
	 * @param positionBeforeChild
	 * @param containmentSF
	 * 
	 * @since 1.1.0.1
	 */
	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && ((EObject) container).eIsSet(containmentSF))
			return UnexecutableCommand.INSTANCE; // This is a single valued feature, and it is already set.
		else {
			CommandBuilder cBld = createCommandBuilder(true); //$NON-NLS-1$
			cBld.applyAttributeSetting((EObject) container, containmentSF, child, positionBeforeChild);
			List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), child, domain.getAnnotationLinkagePolicy());
			return AnnotationPolicy.getCreateRequestCommand(annotations, cBld.getCommand(), domain);
		}
	}
	
	
	public final Command getAddCommand(List children, Object positionBeforeChild) {
		CommandBuilder preCmds = createCommandBuilder(true), postCmds = createCommandBuilder(true);
		// We may be modifying the list, so we'll do a copy of it. Even if we
		// modify it we still should to make the coding simpler.
		int origSize = children.size();
		children = new ArrayList(children);
		for (ListIterator childrenItr = children.listIterator(); childrenItr.hasNext();) {
			Object child = childrenItr.next();
			try {
				Object newChild = getTrueChild(child, false, preCmds, postCmds);
				if (newChild != child)
					if (newChild == null)
						childrenItr.remove();	// Said don't add this child.
					else
						childrenItr.set(newChild);	// Said change the child.
			} catch (NoAddException e) {
				return UnexecutableCommand.INSTANCE;
			}
		}
		
		if (preCmds.isDead() || postCmds.isDead())
			return UnexecutableCommand.INSTANCE;
				
		if (!children.isEmpty() || origSize == 0) {
			// Either there are childre.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getAddCommand(children, positionBeforeChild, preCmds);
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			return UnexecutableCommand.INSTANCE;
		else
			return preCmds.getCommand();
		
	}

	protected void getAddCommand(List children, Object positionBeforeChild, CommandBuilder cbldr) {
		EStructuralFeature containmentSF = getContainmentSF(children, positionBeforeChild, ADD_REQ);
		if (containmentSF == null)
			cbldr.markDead();		
		cbldr.append(getAddCommand(children, positionBeforeChild, containmentSF));
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
			if (!isValidChild(child, containmentSF)) {
				return UnexecutableCommand.INSTANCE;
			}
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
	 * @param cbldr
	 * 
	 * @since 1.1.0.1
	 */
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && (((EObject) container).eIsSet(containmentSF) || children.size() > 1)) {
			return UnexecutableCommand.INSTANCE;	// This is a single valued feature, and it is already set or there is more than one child being added.
		} else {
			CommandBuilder cbldr = createCommandBuilder(true);
			cbldr.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
			return cbldr.getCommand();
		}
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
	 * Delete a child.
	 * @param child
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		CommandBuilder cBld = createCommandBuilder(true); //$NON-NLS-1$
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
		CommandBuilder cBld = createCommandBuilder(true); //$NON-NLS-1$
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
		CommandBuilder cBld = createCommandBuilder(true); //$NON-NLS-1$
		if (children.contains(positionBeforeChild))
			return UnexecutableCommand.INSTANCE;
		// We clone the list because cancelAttributeSettings fools around with the list itself.
		cBld.cancelAttributeSettings((EObject) container, containmentSF, new ArrayList(children));
		cBld.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
		return cBld.getCommand();
	}

}
