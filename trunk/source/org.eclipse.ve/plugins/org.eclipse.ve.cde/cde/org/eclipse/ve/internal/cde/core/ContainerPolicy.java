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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: ContainerPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-04 17:30:49 $ 
 */

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.*;

import org.eclipse.ve.internal.cdm.VisualInfo;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;

import org.eclipse.ve.internal.cde.commands.CancelVisualConstraintCommand;

/**
 * A standard Container Policy for the Common Dialog Editor.
 * The purpose of this policy is to handle containment of model
 * operations. For example, add, created, deleteDependent, etc. Typically
 * this policy has no knowledge of the type of view it is contained within,
 * it only works on the containment operations.
 *
 * It is normally wrappered within a LayoutEditPolicy and a TreeEditPolicy. Those
 * other EditPolicies handle the constraint or tree while this policy
 * handles the children and the model. That way the code can easily be
 * shared between the tree and graph view.
 *
 * It can be used to process certain specific requests, or it can
 * be called directly without using the requests/getCommand method.
 *
 * This is an abstract class meant to be subclassed for each type
 * of model container.
 *
 * NOTE: It is very important that Annotations are handled correctly.
 * Each abstract method below gives a description of how the implementation
 * should handle Annotations. If they are not handled as described, they
 * may not be not be added or they be left dangling.
 */
public abstract class ContainerPolicy {

	protected Object container;
	protected EditDomain domain;
	
public ContainerPolicy(EditDomain domain) {
	this.domain = domain;	// All subclass policies will need a domain so that annotations can be handled.
}
	
/**
 * Answer the container this policy is working on.
 */
public Object getContainer() {
	return container;
}

/**
 * Return the domain.
 */
public EditDomain getEditDomain() {
	return domain;
}

/**
 * Call to set the container this policy is working on.
 * It will be set to null if the editpolicy using this policy is deactivated.
 */
public void setContainer(Object container) {
	this.container= container;
}

/**
 * The result of the getAdd/Create commands. It contains the command that results and
 * the true list of children that were added/created. This may be the same as the original list
 * or it may be different. It depends on the policy implementation. The policy may decide to 
 * change the children that actually get added/created.
 * 
 * @since 1.2.0
 */
public static class Result {
	protected Command command;
	protected List children;
	
	public Result(List children) {
		this.children = children;
	}
	
	/**
	 * Get the command that is to be executed.
	 * @return the command or <code>null</code> if no command.
	 * 
	 * @since 1.2.0
	 */
	public final Command getCommand() {
		return command;
	}
	
	/**
	 * The list of children that were actually created/added.
	 * @return the list of children actually created/added.
	 * 
	 * @since 1.2.0
	 */
	public final List getChildren() {
		return children;
	}
	
	/**
	 * Set the command. Not meant to used by clients. It is here for ContainerPolicy implementers
	 * to set the command.
	 * @param command
	 * 
	 * @since 1.2.0
	 */
	public void setCommand(Command command) {
		this.command = command;
	}
	
	/**
	 * Set the children result list. Not meant to used by clients. It is here for ContainerPolicy implementers
	 * to set the children result list.
	 * @param children
	 * 
	 * @since 1.2.0
	 */
	public void setChildren(List children) {
		this.children = children;
	}
}

/**
 * Return the command to add the children in the request. Insert them before
 * the child specified by position. If position is null, then add them
 * all to the end of the list of children. Add means the children were in the model
 * (i.e. they were orphaned) or are in the model in the case this is a shared
 * relationship (in other words this child is a logical child and is a physical
 * child somewhere else).
 * <p>
 * It is possible that some specialized container may decide to actually change the set of children being
 * added. The result list could have a different set of
 * children, different number of children, or even be empty. If of interest the resulting list can be
 * retrieved from the result (@link Result#getChildren()}).
 * <p>
 * <B>Note:</b> If the children CANNOT be added, then the command returned should probably
 * be the UnexecutableCommand.INSTANCE so that none of the other parts of the
 * add request (such as constraints being set or annotations added) should be 
 * performed. If it returns null instead, these other parts would still be added,
 * and this could be an error in the DiagramData. It is up to the developer to
 * decide if null is valid return value.
 * <p>
 * <b>Note:</b> Annotations don't need to be handled for add because "add" means the
 * model objects are either still in the model or where orphaned. In either
 * case the annotations are left in the DiagramData.
 * @param children children to add. The list may be modified, so don't use an unmodifiable list.
 * @param positionBeforeChild
 * @return
 * 
 * @since 1.2.0
 */
public abstract Result getAddCommand(List children, Object positionBeforeChild);

/**
 * A convenience method to add one child. 
 * @param child
 * @param positionBeforeChild
 * @return
 * 
 * @since 1.2.0
 */
public Result getAddCommand(Object child, Object positionBeforeChild) {
	return getAddCommand(Collections.singletonList(child), positionBeforeChild);
}


/**
 * A convenience method to create one child.
 * @param child
 * @param positionBeforeChild
 * @return
 * 
 * @see #getCreateCommand(List, Object, List)
 * @since 1.2.0
 */
public Result getCreateCommand(Object child, Object positionBeforeChild) {
	return getCreateCommand(Collections.singletonList(child), positionBeforeChild);
}

/**
 * Return the command to create the children in the request. Insert them before
 * the child specified by position. If position is null, then add 
 * all to the end of the list of children. Create means these are brand new children
 * never before in the model.
 * <p>
 * It is possible that some specialized container may decide to actually change the set of children being
 * created. The result list could have a different set of
 * children, different number of children, or even be empty. If of interest the resulting list can be
 * retrieved from the result (@link Result#getChildren()}).
 * <p>
 * <b>Note:</b> If a child CANNOT be added, then the command returned should probably
 * be the UnexecutableCommand.INSTANCE so that none of the other parts of the
 * add request (such as constraints being set or annotations added) should be 
 * performed. If it returns null instead, these other parts would still be added,
 * and this could be an error in the DiagramData. It is up to the developer to
 * decide if null is valid return value.
 * <p>
 * <b>Note:</b>Annotations need to be handled here. After creating the create command
 * for the model objects, pass this command, along with the rest of the required
 * parameters, to the AnnotationPolicy.getAddAllAnnotationsCommand(...). And then return
 * the returned command. It will contain the create command passed in at the
 * appropriate position within the returned command. It is assumed
 * that the annotations for the model object have already been created and attached
 * to the model object through the AnnotationLinkagePolicy. It will detect them
 * through this linkage.
 * <p>
 * <b>Note:</b> Implementations must not modify the children list directly.
 * @param children children to create. May be an unmodifiable list. The resulting children will be in the result if needed.
 * @param positionBeforeChild
 * @return
 * 
 * @see #getCreateCommand(List, Object)
 * @since 1.2.0
 */
public abstract Result getCreateCommand(List children, Object positionBeforeChild);


/**
 * A convenience method for delete of one child.
 * @param child
 * @return
 * 
 * @since 1.2.0
 */
public Result getDeleteDependentCommand(Object child) {
	return getDeleteDependentCommand(Collections.singletonList(child));
}

/**
 * Return the command to delete the children in the request. Delete means the child is
 * being removed entirely from the model. (In some cases, the relationship may be
 * a shared relationship (see "add" above) and is only being deleted from this relationship, in that
 * case the delete request should be treated as an orphan request. Only the implementer
 * knows if this is the case).
 *
 * Note: If the child CANNOT be deleted, then the command returned should probably
 * be the UnexecutableCommand.INSTANCE so that none of the other parts of the
 * delete request (such as constraints being set or annotations delete) should be 
 * performed. If it returns null instead, these other parts would still be deleted,
 * and this could be an error in the DiagramData. It is up to the developer to
 * decide if null is valid return value.
 *
 * Note: Annotations need to be handled here. If this is being treated as an orphan
 * request, then annotations don't need to be handled. If this is being treated as
 * a delete request, then after creating the delete command for the model object,
 * pass this command, along with the rest of the requited parameters, to the 
 * AnnotationPolicy.deleteAnnotationsCommand(...). And the return the returned command.
 * It will contain the delete command passed in at the appropriate posistion within
 * the returned command. It will detect the annotations through the standard linkage
 * by AnnotationLinkagePolicy.
 */
public abstract Result getDeleteDependentCommand(List children);

/**
 * Return the command to orphan the child in the request. Orphan means the children
 * are being removed from this specified relationship, but is not permanently being
 * removed from the model, it will be added back somewhere else, or contained already
 * somewhere else in the model.
 *
 * Note: If the child CANNOT be orphaned, then the command returned should probably
 * be the UnexecutableCommand.INSTANCE so that none of the other parts of the
 * orphan request (such as constraints being unset or annotations removed) should be 
 * performed. If it returns null instead, these other parts would still be orphaned,
 * and this could be an error in the DiagramData. It is up to the developer to
 * decide if null is valid return value.
 *
 * Note: Annotations need not be handled in this case because the model object is
 * not being permanently removed from the model, so the annotations will stay.
 */
protected abstract Result getOrphanTheChildrenCommand(List children);


/**
 * Return the command to orphan the child in the request. Orphan means the children
 * are being removed from this specified relationship, but is not permanently being
 * removed from the model, it will be added back somewhere else, or contained already
 * somewhere else in the model.
 * <p>
 * <b>Note</b>: If the child CANNOT be orphaned, then the command returned will probably
 * be the UnexecutableCommand.INSTANCE so that none of the other parts of the
 * orphan request (such as constraints being unset or annotations removed) should be 
 * performed. If it returns null instead, these other parts would still be orphaned,
 * and this could be an error in the DiagramData. It is up to the developer to
 * decide if null is valid return value.
 * <p>
 * <b>Note</b>: Annotations, except for visual infos, will not be handled in this case because the model object is
 * not being permanently removed from the model, so the annotations will stay. Visual infos
 * for all attached diagrams will be canceled because the child is being orphaned then
 * the visual info wouldn't make sense because it will be somewhere else. Because of this
 * all diagrams need to be able to handle not having a visual info for a child.
 * @param children
 * @return
 * 
 * @since 1.1.0
 */
public final Result getOrphanChildrenCommand(List children) {
	Result result = getOrphanTheChildrenCommand(children);
	Command cmd = result.getCommand();
	if (cmd.canExecute()) {
		Iterator childrenItr = children.iterator();
		while (childrenItr.hasNext()) {
			Object child = childrenItr.next();
			List vis = VisualInfoPolicy.getAllVisualInfos(child, getEditDomain());
			if (!vis.isEmpty()) {
				for (Iterator viItr = vis.iterator(); viItr.hasNext();) {
					VisualInfo vi = (VisualInfo) viItr.next();
					if (vi.getKeyedValues().containsKey(CDMModelConstants.VISUAL_CONSTRAINT_KEY)) {
						// Delete the visual constraint, if any. Leave the VI there even if empty.
						CancelVisualConstraintCommand delCommand = new CancelVisualConstraintCommand();
						delCommand.setTarget(vi);
						cmd = cmd.chain(delCommand);
					}
				}
			}
			IConstraintHandler constraintHandler = getChildConstraintHandler(child);
			if (constraintHandler != null) {
				cmd = cmd.chain(constraintHandler.contributeOrphanChildCommand());
			}
		}
		result.setCommand(cmd);
	}
	return result;
}

protected IConstraintHandler getChildConstraintHandler(Object child) {
	IModelAdapterFactory factory = CDEUtilities.getModelAdapterFactory(getEditDomain());
	return factory != null ? (IConstraintHandler) factory.getAdapter(child, IConstraintHandler.class) : null;
}

/**
 * Return the command to move the children in the request. Insert them before
 * the child specified by position. If position is null, then move them to the
 * end of the list.
 *
 * Note: If the children CANNOT be moved, then the command returned should probably
 * be the UnexecutableCommand.INSTANCE so that none of the other parts of the
 * move children request (such as constraints being unset or annotations removed) should be 
 * performed. If it returns null instead, these other parts would still be changed,
 * and this could be an error in the DiagramData. It is up to the developer to
 * decide if null is valid return value.
 *
 * Note: Annotations need not be handled by this command because the children are
 * staying in the model, so the annotations should too.
 */
public abstract Command getMoveChildrenCommand(List children, Object positionBeforeChild);

/**
 * A static helper method to get the children out of a group request
 * and into a list. The children are the model objects.
 */
public static List getChildren(GroupRequest request) {
	List cEP = request.getEditParts();
	List children = new ArrayList(cEP.size());
	Iterator itr = cEP.iterator();
	while (itr.hasNext()) {
		Object child = ((EditPart) itr.next()).getModel();
		children.add(child);
	}
	return children;
}

/**
 * Return the appropriate command for the given request.
 */
public Command getCommand(Request request){
	
	if (container == null)
		return null;
		
	if (RequestConstants.REQ_DELETE_DEPENDANT.equals(request.getType())) {
		return getDeleteDependentCommand(((ForwardedRequest) request).getSender().getModel()).getCommand();
	}
	
	if (RequestConstants.REQ_CREATE.equals(request.getType())) {
		return getCreateCommand(((CreateRequest) request).getNewObject(), null).getCommand();
	}
	
	if (RequestConstants.REQ_ADD.equals(request.getType())) {
		return getAddCommand(getChildren((GroupRequest) request), null).getCommand();
	}
			
	if (RequestConstants.REQ_ORPHAN_CHILDREN.equals(request.getType()))
		return getOrphanChildrenCommand(getChildren((GroupRequest) request)).getCommand();
		
	return null;
}

}


