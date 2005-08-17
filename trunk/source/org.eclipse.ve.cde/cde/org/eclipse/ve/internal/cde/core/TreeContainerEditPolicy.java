package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: TreeContainerEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-17 18:36:50 $ 
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
	return  containerPolicy.getAddCommand(ContainerPolicy.getChildren(request), beforePart != null ? beforePart.getModel() : null);
}
	
/**
 * Create the child at the location of the request.
 */
protected Command getCreateCommand(CreateRequest request) {	
	EditPart beforePart = findBeforePart(findIndexOfTreeItemAt(request.getLocation()), Collections.EMPTY_LIST, getHost().getChildren());
	return  containerPolicy.getCreateCommand(request.getNewObject(), beforePart != null ? beforePart.getModel() : null);
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
	EditPart parent = getHost();
	// The request has the new location of where we are to move the children.
	
	int newIndex = findIndexOfTreeItemAt( request.getLocation() );
	EditPart beforePart = findBeforePart(newIndex, editparts, children);
			
	// Iterate through the selected children, and determine if this is a valid
	// location to move each one to.
	// It is invalid if it is a move and all of them have them same index location.
	// This means that nothing actually moved. If this is an orphan, then
	// this can never be bad because at least one will change position.
	// TODO Doesn't quite work. Need to think to determine if anything moved or not. Don't quire have new location correct for the test.
	boolean isMove = true;
	boolean isChanged = false;	// Flag to indicate something has actually moved.
	int index = newIndex;		// Index of where next child will go.
	for( int i = 0; i < editparts.size(); i++, index++ ){
		EditPart child = (EditPart)editparts.get(i);
		if (isMove && child.getParent() != parent)
			isMove = false;	// It is now orphan/add
		if (isMove && !isChanged) {
			int oldIndex = children.indexOf( child );
			if( oldIndex != index && oldIndex + 1 != index )
				isChanged = true;
		}
	}
	
	if (isMove && !isChanged)
		return UnexecutableCommand.INSTANCE;

	if (isMove) {
		return containerPolicy.getMoveChildrenCommand(ContainerPolicy.getChildren(request), beforePart != null ? beforePart.getModel() : null);
	} else {
		// Need to do it as Orphan/Add
		List childrenModels = ContainerPolicy.getChildren(request);
		Command cmd = containerPolicy.getOrphanChildrenCommand(childrenModels);
		if (cmd != null) {
			Command cmd1 = containerPolicy.getAddCommand(childrenModels, beforePart != null ? beforePart.getModel() : null);
			if (cmd1 != null)
				cmd = cmd.chain(cmd1);
			else
				return UnexecutableCommand.INSTANCE;
		} else
			return UnexecutableCommand.INSTANCE;
		return cmd;
	}
}

}