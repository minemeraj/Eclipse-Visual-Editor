/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: UnknownLayoutSwitcher.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:50 $ 
 */

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * This helper class is used when a container's layout is changed
 * to an unknown type.
 * There is no constraint that we can handle, so we add back in 
 * the same order with no constraint.
 *
 * Creation date: (10/10/00 3:50:16 PM)
 * @author: Peter Walker
 */
public class UnknownLayoutSwitcher extends LayoutSwitcher {
	protected UnknownLayoutPolicyHelper helper;

	public UnknownLayoutSwitcher(VisualContainerPolicy cp) {
		super(cp);
		helper = new UnknownLayoutPolicyHelper(cp);
	}

	/**
	 * Returns a compound command containing the child constraint commands to position the
	 * children within the unknown Layout container. Since we don't know the layout, we can't
	 * have a constraint. Hopefully the layout can handle having a null constraint.
	 */
	protected Command getChangeConstraintsCommand(List children) {		
		return helper.getChangeConstraintCommand(children, Collections.nCopies(children.size(), null));	// Now let the helper add them back correctly.
	}

}


