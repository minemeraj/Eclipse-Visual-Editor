package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: TreeContainerEditPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-02 20:49:10 $ 
 */

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.*;
import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.java.visual.*;
/**
 * Tree ContainerEditPolicy for awt Containers.
 */
public class TreeContainerEditPolicy extends org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy implements IActionFilter {
	
	ILayoutPolicyHelper helper;
	
	public TreeContainerEditPolicy(ContainerPolicy policy) {
		super(policy);
	}
	
	public void setPolicyHelper(ILayoutPolicyHelper helper) {
		helper.setContainerPolicy((ContainerPolicy) containerPolicy);
		this.helper = helper;
	}
	
	public Command getCommand(Request req){
		if (req.getType().equals(RequestConstants.REQ_ORPHAN_CHILDREN))
			return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) req));
		return super.getCommand(req);
	}
	
	protected Command getAddCommand(ChangeBoundsRequest request) {	
		EditPart beforePart = findBeforePart(findIndexOfTreeItemAt(request.getLocation()), Collections.EMPTY_LIST, getHost().getChildren());
		
		List childsEP = request.getEditParts();
		ArrayList children = new ArrayList(childsEP.size());
		Iterator itr = childsEP.iterator();
		while(itr.hasNext()) {
			children.add(((EditPart) itr.next()).getModel());
		}
		
		List constraints = helper.getDefaultConstraint(children);
		return helper.getAddChildrenCommand(children, constraints, beforePart != null ? beforePart.getModel() : null);
	}
	
	protected Command getCreateCommand(CreateRequest request) {
		EditPart beforePart = findBeforePart(findIndexOfTreeItemAt(request.getLocation()), Collections.EMPTY_LIST, getHost().getChildren());
		Object child = request.getNewObject();
		List constraints = helper.getDefaultConstraint(Collections.singletonList(child));
		return helper.getCreateChildCommand(child, constraints.get(0), beforePart != null ? beforePart.getModel() : null);
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (helper != null && helper instanceof IActionFilter) {
			return ((IActionFilter)helper).testAttribute(target, name, value);
		}
		return false;
	}

}