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
 *  $RCSfile: NoChildrenVisualContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2006-05-17 22:29:56 $ 
 */
package org.eclipse.ve.internal.java.visual;

import java.util.List;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * This is visual container policy for a parent that doesn't allow children to be dropped/removed/added. It controls
 * the children entirely. An example is swt.Form. It has a body as a child. It controls it. This child cannot be
 * added or removed or moved.
 * @since 1.2.0
 */
public class NoChildrenVisualContainerPolicy extends VisualContainerPolicy {

	/**
	 * @param domain
	 * 
	 * @since 1.2.0
	 */
	public NoChildrenVisualContainerPolicy(EditDomain domain) {
		super(domain);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.VisualContainerPolicy#getAddCommand(java.util.List, java.util.List, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder)
	 */
	protected void getAddCommand(List constraints, List children, Object position, CommandBuilder cbld) {
		cbld.markDead();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.visual.VisualContainerPolicy#getCreateCommand(java.util.List, java.util.List, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder)
	 */
	protected void getCreateCommand(List constraints, List children, Object position, CommandBuilder cbld) {
		cbld.markDead();
	}

}
