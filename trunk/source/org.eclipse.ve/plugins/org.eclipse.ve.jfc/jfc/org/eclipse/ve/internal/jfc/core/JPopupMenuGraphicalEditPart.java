/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;

/*
 *  $RCSfile: JPopupMenuGraphicalEditPart.java,v $
 *  $Revision: 1.6 $  $Date: 2005-05-11 19:01:38 $ 
 */

import org.eclipse.gef.EditPolicy;

import org.eclipse.ve.internal.cde.core.CDELayoutEditPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaBeanGraphicalEditPart;

/**
 * @author pwalker
 * 
 * GraphicalEditPart for javax.swing.JPopupMenu's. This is currently just an icon that you can drop children onto it. Though it will always add to the
 * end of the current set of children and you need to use the beans viewer to manipulate them. <package protected> until we can use it.
 */
public class JPopupMenuGraphicalEditPart extends JavaBeanGraphicalEditPart {

	/**
	 * Constructor for JPopupMenuTreeEditPart.
	 * 
	 * @param model
	 */
	public JPopupMenuGraphicalEditPart(Object model) {
		super(model);
	}

	/**
	 * Use a JPopupMenuLayoutPolicy which is a FlowLayout
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CDELayoutEditPolicy(new JMenuContainerPolicy(EditDomain.getEditDomain(this))));
	}
}