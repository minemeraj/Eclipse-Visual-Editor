package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JMenuBarLayoutEditPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.FlowLayoutEditPolicy;

/**
 * @author pwalker
 *
 * Layout edit policy for JmenuBarGraphicalEditPart
 * We need a FlowLayout so we can insert & add menus with the flow layout feedback
 */
public class JMenuBarLayoutEditPolicy extends FlowLayoutEditPolicy {

	/**
	 * Constructor for JMenuBarLayoutEditPolicy.
	 * Use JMenuBarContainerPolicy which allows only JMenu's to be added.
	 * @param containerPolicy
	 */
	public JMenuBarLayoutEditPolicy(JMenuBarGraphicalEditPart editpart) {
		super(new JMenuBarContainerPolicy(EditDomain.getEditDomain(editpart)));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
	 */
	protected boolean isHorizontal() {
		return true;
	}

}
