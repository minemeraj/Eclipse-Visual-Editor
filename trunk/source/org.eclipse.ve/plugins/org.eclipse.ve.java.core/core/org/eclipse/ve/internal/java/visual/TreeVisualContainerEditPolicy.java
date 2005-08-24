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
package org.eclipse.ve.internal.java.visual;
/*
 *  $RCSfile: TreeVisualContainerEditPolicy.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:47 $ 
 */

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.*;
import org.eclipse.ui.IActionFilter;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy;
/**
 * Tree ContainerEditPolicy for Visual containers with layout policy helpers on the Tree (Java Beans Viewer).
 * <p>
 * If there is a layout policy helper, then certain commands are forwarded over to
 * the layout policy helper to do processing for the command.
 * <p>
 * If the layout policy helper is also an IActionFilter, then testAttribute requests
 * are forwarded over to it too.
 */
public class TreeVisualContainerEditPolicy extends TreeContainerEditPolicy implements IActionFilter {
	
	protected ILayoutPolicyHelper helper;
	
	public TreeVisualContainerEditPolicy(VisualContainerPolicy policy) {
		super(policy);
	}
	
	public void setPolicyHelper(ILayoutPolicyHelper helper) {
		helper.setContainerPolicy((VisualContainerPolicy) containerPolicy);
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
		
	public boolean testAttribute(Object target, String name, String value) {
		if (helper instanceof IActionFilter) {
			return ((IActionFilter)helper).testAttribute(target, name, value);
		}
		return false;
	}

}
