/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JMenuGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:39 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.gef.*;

import org.eclipse.ve.internal.cde.core.CDELayoutEditPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
 

/**
 * JMenu Graphical Edit part. It allows the menu to be seen, and it allows dropping
 * children onto it, but they are not shown graphically. The children will always be added to the end.
 * @since 1.1.0
 */
public class JMenuGraphicalEditPart extends ComponentGraphicalEditPart {

	/**
	 * @param model
	 * 
	 * @since 1.1.0
	 */
	public JMenuGraphicalEditPart(Object model) {
		super(model);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CDELayoutEditPolicy(new JMenuContainerPolicy(EditDomain.getEditDomain(this))));
	}	

}
