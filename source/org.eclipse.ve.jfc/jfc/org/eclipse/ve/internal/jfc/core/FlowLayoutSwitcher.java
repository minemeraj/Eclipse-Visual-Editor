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
 *  $RCSfile: FlowLayoutSwitcher.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-02 20:49:10 $ 
 */


import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * This helper class is used when a container's layout is changed
 * to an Flow type.
 * There is no constraint that we can handle, so we add back in 
 * the same order with no constraint.
 *
 * Creation date: (10/10/00 3:50:16 PM)
 * @author: Peter Walker
 */
public class FlowLayoutSwitcher extends LayoutSwitcher {
	protected FlowLayoutPolicyHelper helper;

	public FlowLayoutSwitcher(VisualContainerPolicy cp) {
		super(cp);
		helper = new FlowLayoutPolicyHelper(cp);
	}

	/**
	 * Returns a compound command containing the child constraint commands to position the
	 * children within a flow Layout container.
	 */
	protected Command getChangeConstraintsCommand(List children) {		
		return helper.getChangeConstraintCommand(children, Collections.nCopies(children.size(), null));	// Now let the helper change them back correctly.
	}

}


