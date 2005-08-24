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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JToolBarLayoutEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;

/**
 * @author pwalker
 *
 * Layout edit policy for JToolBarGraphicalEditPart
 * We need a FlowLayout so we can insert & add items or actions with the flow layout feedback.
 */
public class JToolBarLayoutEditPolicy extends FlowLayoutEditPolicy {

	/**
	 * Constructor for JToolBarLayoutEditPolicy.
	 * Use JToolBarContainerPolicy which allows Components or Actions to be added.
	 * @param containerPolicy
	 */
	public JToolBarLayoutEditPolicy(JToolBarGraphicalEditPart editpart) {
		super(new JToolBarContainerPolicy(EditDomain.getEditDomain(editpart)));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return true;
	}

}
