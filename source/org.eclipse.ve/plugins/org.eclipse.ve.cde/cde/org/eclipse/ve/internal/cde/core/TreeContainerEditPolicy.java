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
 *  $RCSfile: TreeContainerEditPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-12-01 20:19:41 $ 
 */

import java.util.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
/**
 * Common Diagram Editor TreeContainerEditPolicy. It handles the tree
 * by taking in the requests, determining the positions (which become 
 * indexes) and forwards them over to the ContainerEditPolicy passed in
 * to perform the actions.
 * <p>
 * What it makes different from GEF's {@link org.eclipse.gef.editpolicies.TreeContainerEditPolicy} is
 * that the actual function is shipped over to a {@link org.eclipse.ve.internal.cde.core.ContainerPolicy}, 
 * which handles that actual commands. This is because the commands are common between the GraphicalViewer
 * and the TreeViewer, so why duplicate them in the two edit policies, so use the common class instead.
 */
public class TreeContainerEditPolicy extends org.eclipse.gef.editpolicies.TreeContainerEditPolicy {
	protected ContainerPolicy containerPolicy;
	
public TreeContainerEditPolicy(ContainerPolicy containerPolicy) {
	this.containerPolicy = containerPolicy;
}

public void activate() {
	super.activate();
	containerPolicy.setContainer(getHost().getModel());
}

public void deactivate() {
	super.deactivate();
	containerPolicy.setContainer(null);
}

protected EditPart findBeforePart(int index, List movingparts, List children) {
	// Now need to find who to move before. Find first ep starting
	// at newIndex that is not one of the editparts being moved.
	// This will be the one to go before. Otherwise it goes at the end.
	EditPart beforePart = null;
	if (index >= 0 && index < children.size()) {
		ListIterator lItr = children.listIterator(index);
		beforePart = (EditPart) lItr.next();
		while (movingparts.contains(beforePart)) {
			if (lItr.hasNext())
				beforePart = (EditPart) lItr.next();
			else {
				beforePart = null;
				break;
			}
		}
	}
	return beforePart;
}

public Command getCommand(Request req){
	if (req.getType().equals(REQ_ORPHAN_CHILDREN) || REQ_DELETE.equals(req.getType())) 
		return containerPolicy.getCommand(req);	// Null returns are valid for these requests.
	if (REQ_DELETE_DEPENDANT.equals(req.getType()))
		return getDeleteDependantCommand(req);
	return super.getCommand(req);
}

/**
 * Add the children at the location of the request.
 */
protected Command getAddCommand(ChangeBoundsRequest request) {	
	EditPart beforePart = findBeforePart(findIndexOfTreeItemAt(request.getLocation()), Collections.EMPTY_LIST, getHost().getChildren());
	return  containerPolicy.getAddCommand(ContainerPolicy.getChildren(request), beforePart != null ? beforePart.getModel() : null).getCommand();
}
	
/**
 * Create the child at the location of the request.
 */
protected Command getCreateCommand(CreateRequest request) {	
	EditPart beforePart = findBeforePart(findIndexOfTreeItemAt(request.getLocation()), Collections.EMPTY_LIST, getHost().getChildren());
	return  containerPolicy.getCreateCommand(request.getNewObject(), beforePart != null ? beforePart.getModel() : null).getCommand();
}

/**
 * getDeleteDependantCommand method comment.
 */
protected Command getDeleteDependantCommand(Request request) {
	return containerPolicy.getCommand(request);
}

/**
 * Move the children to the location of the request.
 */
protected Command getMoveChildrenCommand(ChangeBoundsRequest request) {
	// In the tree, if they are all of the same parent, then a move children
	// request is sent to the container edit policy. If they are of different
	// parentage, then it needs to be orphan followed by add.
	
	List editparts = request.getEditParts();
	List children = getHost().getChildren();
	// The request has the new location of where we are to move the children.
	
	int newIndex = findIndexOfTreeItemAt( request.getLocation() );
	EditPart beforePart = findBeforePart(newIndex, editparts, children);
			
	// Iterate through the selected children, and determine if this is a valid
	// location to move each one to.
	// It is invalid all of them have them same index location they had before.
	// This means that nothing actually moved. 
	boolean isChanged = false;	// Flag to indicate something has actually moved.
	int index = newIndex;		// Index of where next child will go.
	for( int i = 0; i < editparts.size(); i++, index++ ){
		EditPart child = (EditPart)editparts.get(i);
		if (!isChanged) {
			int oldIndex = children.indexOf( child );
			if( oldIndex != index && oldIndex + 1 != index )
				isChanged = true;
		}
	}
	
	if (!isChanged)
		return UnexecutableCommand.INSTANCE;
	else
		return getMoveChildrenCommand(ContainerPolicy.getChildren(request), beforePart != null ? beforePart.getModel() : null);
}

/**
 * Subclasses should override to do different processing.
 * @param children
 * @param before
 * @return
 * 
 * @since 1.2.0
 */
protected Command getMoveChildrenCommand(List children, Object before) {
	return containerPolicy.getMoveChildrenCommand(children, before);
}

}
