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
 *  $Revision: 1.9 $  $Date: 2005-12-05 23:13:07 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.StopRequestException;
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
	
	public static final int 
		ADD_REQ = 0,	// The request for the containment feature is an add request. Children are currently not contained in this container.
		CREATE_REQ = 1,	// The request for the containment feature is a create request. Child is currently not contained in this container.		
		MOVE_REQ = 2,	// The request for the containment feature is a move request. Children are currently contained in this container.
		DELETE_REQ = 3,	// The request for the containment feature is a delete request. Child is currently contained in this container.
		ORPHAN_REQ = 4;	// The request for the containment feature is an orphan request. Children are currently contained in this container.

	
	/**
	 * Result returned when corelated objects list are available. {@link AbstractEMFContainerPolicy} doesn't
	 * return this by default, but subclasses may.
	 * 
	 * @since 1.2.0
	 */
	public static class CorelatedResult extends Result {

		protected List corelatedList;

		/**
		 * @param children
		 * @param corelatedList
		 * 
		 * @since 1.2.0
		 */
		public CorelatedResult(List children, List corelatedList) {
			super(children);
			this.corelatedList = corelatedList;
		}
		
		/**
		 * Create with only children, no corelated objects.
		 * @param children
		 * 
		 * @since 1.2.0
		 */
		public CorelatedResult(List children) {
			super(children);
		}
		
		/**
		 * Get the result list of corelated objects, if any. <code>null</code> if there aren't any.
		 * @return result list of corelated objects or <code>null</code> if none were sent in.
		 * 
		 * @since 1.2.0
		 */
		public final List getCorelatedList() {
			return corelatedList;
		}
		
		/**
		 * Set the corelated. Not meant to used by clients. It is here for ContainerPolicy implementers
		 * to set the corelated list.
		 * @param corelatedList
		 * 
		 * @since 1.2.0
		 */
		public void setCorelatedList(List corelatedList) {
			this.corelatedList = corelatedList;
		}
	}
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
	 * Get the policy's request type from the {@link org.eclipse.gef.RequestConstants} request types.
	 * @param requestConstantsRequestType
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public int getPolicyRequestType(Object requestConstantsRequestType) {
		if (RequestConstants.REQ_ADD.equals(requestConstantsRequestType))
			return ADD_REQ;
		else if (RequestConstants.REQ_CREATE.equals(requestConstantsRequestType))
			return CREATE_REQ;
		else if (RequestConstants.REQ_ORPHAN_CHILDREN.equals(requestConstantsRequestType))
			return ORPHAN_REQ;
		else if (RequestConstants.REQ_DELETE_DEPENDANT.equals(requestConstantsRequestType))
			return DELETE_REQ;
		else if (RequestConstants.REQ_MOVE_CHILDREN.equals(requestConstantsRequestType))
			return MOVE_REQ;
		else
			return -1;	// Unknown.
	}
	
	/**
	 * Return the adjusted true child if {@link IContainmentHandler} decides for a different one.
	 * @param child child to send in
	 * @param reqType request type. {@link #ADD_REQ}, etc.
	 * @param preCmds CommandBuilder for commands to be executed before the adds
	 * @param postCmds CommandBuilder for commands to be executed after the adds
	 * @return the child to add  (maybe different than the one sent it) or <code>null</code> if handler handled child
	 * @throws StopRequestException thrown if the handler determines that the parent is not valid for this child.
	 * 
	 * @since 1.2.0
	 */
	public Object getTrueChild(Object child, int reqType, CommandBuilder preCmds, CommandBuilder postCmds) throws StopRequestException {
		// Go to the IContainmentHandler to decide what to do with the child.
		IModelAdapterFactory fact = getModelAdapterFactory();
		if (fact != null) {
			IContainmentHandler handler = (IContainmentHandler) fact.getAdapter(child, IContainmentHandler.class);
			if (handler != null) {
				switch (reqType) {
					case ADD_REQ:
						child = handler.contributeToDropRequest(container, child, preCmds, postCmds, false, getEditDomain());
						break;
					case CREATE_REQ:
						child = handler.contributeToDropRequest(container, child, preCmds, postCmds, true, getEditDomain());
						break;
					case ORPHAN_REQ:
						child = handler.contributeToRemoveRequest(container, child, preCmds, postCmds, true, getEditDomain());
						break;
					case DELETE_REQ:
						child = handler.contributeToRemoveRequest(container, child, preCmds, postCmds, false, getEditDomain());
						break;
				}
				
			}
		}
		return child;
	}
	
	/**
	 * Process a list of children and possible list of co-related objects to get the true list children and co-related objects.
	 * <p>
	 * Most of the time there is no list of co-related objects, but sometimes there may be. For example in the AWT world there
	 * could be a correlated list of constraints that go with each child. Now if a child was removed from the child list, then
	 * the entry from the corelatedObjects list must also be removed, otherwise they would get out of sync. If the list is <code>null</code>
	 * then no working of the corelated objects is performed.
	 * <p>
	 * The incoming lists will not be modified. If there are any changes then new lists will be created and placed into the result instead.
	 *
	 * @param corelatedResult incoming result containing the children and the corelated list, if any. If no corelated list is <code>null</code>, then
	 * corelated objects checking will be done.
	 * @param reqType request type. {@link #ADD_REQ}, etc.
	 * @param preCmds
	 * @param postCmds
	 * @throws StopRequestException
	 * 
	 * @since 1.2.0
	 */
	public void getTrueChildren(CorelatedResult corelatedResult, int reqType, CommandBuilder preCmds, CommandBuilder postCmds) throws StopRequestException {
		ListIterator corelatedItr = null;
		boolean changedChildrenList = false;
		for (ListIterator childrenItr = corelatedResult.getChildren().listIterator(); childrenItr.hasNext();) {
			Object child = childrenItr.next();
			if (corelatedItr != null)
				corelatedItr.next();
			Object newChild = getTrueChild(child, reqType, preCmds, postCmds);
			if (newChild != child) {
				if (!changedChildrenList) {
					// We will be modifying, so make a copy of the list, and create an iterator starting at where we currently are.
					List newChildren = new ArrayList(corelatedResult.getChildren());
					corelatedResult.setChildren(newChildren);
					childrenItr = newChildren.listIterator(childrenItr.previousIndex());
					childrenItr.next();	
					changedChildrenList = true;
				}
				if (newChild == null) {
					if (corelatedResult.getCorelatedList() != null) {
						if (corelatedItr == null) {
							// We will be modifying, so make a copy of the list, and create an iterator starting at where we currently are.
							List newCo = new ArrayList(corelatedResult.getCorelatedList());
							corelatedResult.setCorelatedList(newCo);
							corelatedItr = newCo.listIterator(childrenItr.previousIndex());
							corelatedItr.next();
						}
						corelatedItr.remove();	// Remove the corresponding correlated object.
					}
					childrenItr.remove();	// Said don't add this child.
				} else
					childrenItr.set(newChild);	// Said change the child.
			}
		}
	}

	public final Result getCreateCommand(List children, Object positionBeforeChild) {
		CorelatedResult result = new CorelatedResult(children);
		CommandBuilder preCmds = createCommandBuilder(true), postCmds = createCommandBuilder(true);
		int origSize = children.size();
		try {
			getTrueChildren(result, CREATE_REQ, preCmds, postCmds);
		} catch (StopRequestException e) {
			preCmds.markDead();
		}
		
		if (preCmds.isDead() || postCmds.isDead()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		}
		
		if (!result.getChildren().isEmpty() || origSize == 0) {
			// Either there are children.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getCreateCommand(result.getChildren(), positionBeforeChild, preCmds);
		}

		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			result.setCommand(UnexecutableCommand.INSTANCE);
		else
			result.setCommand(preCmds.getCommand());
		return result;
	}
	
	/**
	 * Process the finalized list of children for create.
	 * <p>
	 * By default this just calls {@link #getCreateCommand(Object, Object, CommandBuilder)}
	 * for each child. Subclasses can override if they want to handle the list all at once.
	 * The overrides can call {@link #getMultipleCreateCommand(List, Object, CommandBuilder)} instead
	 * of this super method to do the same thing but do them all at once.
	 * 
	 * @param children
	 * @param positionBeforeChild
	 * @param cbldr
	 * 
	 * @since 1.2.0
	 */
	protected void getCreateCommand(List children, Object positionBeforeChild, CommandBuilder cbldr) {
		for (Iterator itr = children.iterator(); itr.hasNext();) {
			getCreateCommand(itr.next(), positionBeforeChild, cbldr);
		}
	}

	/**
	 * Handle one child for create. 
	 * <p>This is the default create, called once for each child on the create list. It gets the the structural feature and
	 * then calls {@link #getCreateCommand(Object, Object, EStructuralFeature)}.
	 * <p>
	 * Subclasses should normally override {@link #primCreateCommand(Object, Object, EStructuralFeature)} to change
	 * actual create processing.
	 * @param child
	 * @param positionBeforeChild
	 * @param cbldr
	 * 
	 * @since 1.2.0
	 */
	protected void getCreateCommand(Object child, Object positionBeforeChild, CommandBuilder cbldr) {
		EStructuralFeature containmentSF = getContainmentSF(child, positionBeforeChild, CREATE_REQ);
		if (containmentSF == null)
			cbldr.markDead();
		else
			cbldr.append(getCreateCommand(child, positionBeforeChild, containmentSF));
	}
	
	/**
	 * A helper to handle creating for multiple children at once. It is not normally called. It can be
	 * called by overrides to {@link #getCreateCommand(List, Object, CommandBuilder)} instead if desire
	 * to use this.
	 * @param children
	 * @param positionBeforeChild
	 * @param cbldr
	 * 
	 * @since 1.2.0
	 */
	protected void getMultipleCreateCommand(List children, Object positionBeforeChild, CommandBuilder cbldr) {
		EStructuralFeature containmentSF = getContainmentSF(children, positionBeforeChild, CREATE_REQ);
		if (containmentSF == null)
			cbldr.markDead();
		else {
			Iterator itr = children.iterator();
			while (itr.hasNext()) {
				Object child = itr.next();
				if (!isValidChild(child, containmentSF)) {
					cbldr.markDead();
					return;
				}
			}
			cbldr.append(primCreateCommand(children, positionBeforeChild, containmentSF));
		}
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
	
	/**
	 * A helper to be able handle create at all once for multiple children. Not normally called, but subclasses can
	 * decide to call it if a simple create All at once it sufficent.
	 * @param children
	 * @param positionBeforeChild
	 * @param containmentSF
	 * @return
	 * 
	 * @since 1.2.0
	 */
	protected Command primCreateCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		if (!containmentSF.isMany() && ((EObject) container).eIsSet(containmentSF))
			return UnexecutableCommand.INSTANCE; // This is a single valued feature, and it is already set.
		else {
			CommandBuilder cBld = createCommandBuilder(true); //$NON-NLS-1$
			cBld.applyAttributeSettings((EObject) container, containmentSF, children, positionBeforeChild);
			List annotations = AnnotationPolicy.getAllAnnotations(new ArrayList(), children, domain.getAnnotationLinkagePolicy());
			return AnnotationPolicy.getCreateRequestCommand(annotations, cBld.getCommand(), domain);
		}
	}
	
	
	public final Result getAddCommand(List children, Object positionBeforeChild) {
		CorelatedResult result = new CorelatedResult(children);
		CommandBuilder preCmds = createCommandBuilder(true), postCmds = createCommandBuilder(true);
		int origSize = children.size();
		try {
			getTrueChildren(result, ADD_REQ, preCmds, postCmds);
		} catch (StopRequestException e) {
			preCmds.markDead();
		}
		
		if (preCmds.isDead() || postCmds.isDead()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		}
				
		if (!result.getChildren().isEmpty() || origSize == 0) {
			// Either there are children.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getAddCommand(result.getChildren(), positionBeforeChild, preCmds);
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			result.setCommand(UnexecutableCommand.INSTANCE);
		else
			result.setCommand(preCmds.getCommand());
		return result;
		
	}

	/**
	 * Add the children and put the commands into the command builder.
	 * <p>
	 * The default implementation will get the containment feature and then call {@link #getAddCommand(List, Object, EStructuralFeature)}.
	 * Subclasses should not normally override this method or {@link #getAddCommand(List, Object, EStructuralFeature)}, instead they should
	 * normally override {@link #primAddCommand(List, Object, EStructuralFeature)}.
	 * @param children
	 * @param positionBeforeChild
	 * @param cbldr
	 * 
	 * @since 1.2.0
	 */
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
	
	
	public final Result getDeleteDependentCommand(List children) {

		CorelatedResult result = new CorelatedResult(children);
		CommandBuilder preCmds = createCommandBuilder(true), postCmds = createCommandBuilder(true);
		int origSize = children.size();
		try {
			getTrueChildren(result, DELETE_REQ, preCmds, postCmds);
		} catch (StopRequestException e) {
			preCmds.markDead();
		}
		
		if (preCmds.isDead() || postCmds.isDead()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		}
		
		if (!result.getChildren().isEmpty() || origSize == 0) {
			// Either there are children.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getDeleteDependentCommand(result.getChildren(), preCmds);
		}

		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			result.setCommand(UnexecutableCommand.INSTANCE);
		else
			result.setCommand(preCmds.getCommand());
		return result;
	}
	
	/**
	 * Process the finalized list of children for delete.
	 * <p>
	 * By default this just calls {@link #getDeleteDependentCommand(Object, CommandBuilder)}
	 * for each child. Subclasses can override if they want to handle the list all at once.
	 * @param children
	 * @param positionBeforeChild
	 * @param cbldr
	 * 
	 * @since 1.2.0
	 */
	protected void getDeleteDependentCommand(List children, CommandBuilder cbldr) {
		for (Iterator itr = children.iterator(); itr.hasNext();) {
			getDeleteDependentCommand(itr.next(), cbldr);
		}
	}
	
	/**
	 * Delete an individual child.
	 * @param child
	 * @param cbldr
	 * 
	 * @since 1.2.0
	 */
	protected void getDeleteDependentCommand(Object child, CommandBuilder cbldr) {
		EStructuralFeature containmentSF = getContainmentSF(child, null, DELETE_REQ);
		if (containmentSF == null)
			cbldr.markDead();
		else
			cbldr.append(getDeleteDependentCommand(child, containmentSF));
	}
	
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		EStructuralFeature containmentSF = getContainmentSF(children, positionBeforeChild, MOVE_REQ);
		if (containmentSF == null)
			return UnexecutableCommand.INSTANCE;		
		return getMoveChildrenCommand(children, positionBeforeChild, containmentSF);
	}
	
	protected void getOrphanTheChildrenCommand(List children, CommandBuilder cbldr) {
		EStructuralFeature containmentSF = getContainmentSF(children, null, ORPHAN_REQ);
		if (containmentSF == null)
			cbldr.markDead();
		else
			cbldr.append(getOrphanChildrenCommand(children, containmentSF));
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

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.ContainerPolicy#getOrphanTheChildrenCommand(java.util.List)
	 */
	protected final Result getOrphanTheChildrenCommand(List children) {

		CorelatedResult result = new CorelatedResult(children);
		CommandBuilder preCmds = createCommandBuilder(true), postCmds = createCommandBuilder(true);
		int origSize = children.size();
		try {
			getTrueChildren(result, ORPHAN_REQ, preCmds, postCmds);
		} catch (StopRequestException e) {
			preCmds.markDead();
		}
		
		if (preCmds.isDead() || postCmds.isDead()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		}
				
		if (!result.getChildren().isEmpty() || origSize == 0) {
			// Either there are children.
			// Or it  could be that the handlers simply removed them all. If they did, then this is valid and we shouldn't go on to getAddCommand.
			// This is because if we sent an empty list into getAddCommand some subclasses treat this as invalid. But the handlers have determined
			// that it is valid. If children is empty and original is empty then the handlers didn't do it, so let normal processing handle it.
			getOrphanTheChildrenCommand(result.getChildren(), preCmds);
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			result.setCommand(UnexecutableCommand.INSTANCE);
		else
			result.setCommand(preCmds.getCommand());
		return result;
		
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
