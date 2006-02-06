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
 *  $RCSfile: ExpandableCompositeTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:41 $ 
 */
package org.eclipse.ve.internal.forms;

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.swt.CompositeTreeEditPart;
 

/**
 * TreeEditPart for Expandable Composite.
 * @since 1.2.0
 */
public class ExpandableCompositeTreeEditPart extends CompositeTreeEditPart {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public ExpandableCompositeTreeEditPart(Object model) {
		super(model);
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new org.eclipse.ve.internal.cde.core.TreeContainerEditPolicy(new ExpandableCompositeContainerPolicy(
				EditDomain.getEditDomain(this))));
	}


}
