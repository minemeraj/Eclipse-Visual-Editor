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
 *  $RCSfile: GridBagLayoutSwitcher.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.List;

import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * This helper class is used when a container's layout is changed
 * to a GridBagLayout. Each of the constraints for the container's children 
 * constraints need to be assigned a GridBagConstaints.
 *
 */
public class GridBagLayoutSwitcher extends LayoutSwitcher {
	GridBagLayoutPolicyHelper helper;

public GridBagLayoutSwitcher(VisualContainerPolicy cp) {
	super(cp);
	helper = new GridBagLayoutPolicyHelper(cp);
}
/**
 * Returns a compound command containing the child constraint commands to position the
 * children within a GridBagLayout container.
 * Since this is a GridBagLayout, it doesn't matter what the previous constraints
 * were for the children since we're going to create GridBagConstraints constraints.
 * In order to do this we must get the position coordinates from the live bean
 * and create setting commands with a Bean rectangle constraint.
 */
protected Command getChangeConstraintsCommand(List children) {
	List constraints = helper.getGridBagConstraints(children);
	if (!children.isEmpty() && !constraints.isEmpty()) {
		return helper.getChangeConstraintCommand(children, constraints);	// Now let the helper add them back correctly.
	}
	return null;
}

}
