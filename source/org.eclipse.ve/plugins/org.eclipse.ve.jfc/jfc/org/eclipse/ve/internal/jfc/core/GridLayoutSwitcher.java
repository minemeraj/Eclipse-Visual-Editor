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
 *  $RCSfile: GridLayoutSwitcher.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:38:09 $ 
 */
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * This helper class is used when a container's layout is changed
 * to GridLayout. Each of the constraints for the container's children 
 * need to removed.
 */
public class GridLayoutSwitcher extends LayoutSwitcher {
	protected FlowLayoutPolicyHelper helper;

public GridLayoutSwitcher(VisualContainerPolicy cp) {
	super(cp);
	helper = new FlowLayoutPolicyHelper(cp);
}
/**
 * Return a command to apply the correct number to the property "rows" of a GridLayout. 
 * By explicitly setting the number of rows in a GridLayout, the columns created will default 
 * based on the number of components. For example, if you set the rows=3, and there are 
 * 10 components, the grid will be 3rows X 4columns.
 */
public Command getCommand(EStructuralFeature sf, IJavaObjectInstance newManager) {
	// Calculate the number of rows based on the number of children and create the command
	// to apply it to the layout manager.
	IJavaObjectInstance containerBean = getContainerBean();
	ResourceSet rset = containerBean.eResource().getResourceSet();
	List components = (List) containerBean.eGet(JavaInstantiation.getSFeature(containerBean, JFCConstants.SF_CONTAINER_COMPONENTS));
	int rows = 1;
	// The calculation is such that we want a grid that is somewhat larger horizontally (#columns)
	// than vertically (#rows). Here's an example table that represents the algorithm:
	
	// #components	          #rows    #columns
	//	1		1	1
	//	2		1	2
	//	3		2	2	<-- row increments here
	//	6		2	3
	//	7		3	3	<-- row increments here
	//	12		3	4
	//	13		4	4	<-- row increments here
	//	...		   ...
	for (rows = 1; rows*(rows+1) < components.size(); rows++){};
	
	ApplyAttributeSettingCommand setRowsCmd = new ApplyAttributeSettingCommand();
	setRowsCmd.setTarget(newManager);
	JavaClass GRID_LAYOUT = Utilities.getJavaClass("java.awt.GridLayout",rset); //$NON-NLS-1$
	EStructuralFeature SF_ROWS = GRID_LAYOUT.getEStructuralFeature("rows"); //$NON-NLS-1$
	setRowsCmd.setAttribute(SF_ROWS);
	Object rowsSetting = BeanUtilities.createJavaObject(Utilities.getJavaType("int", rset), rset, String.valueOf(rows)); //$NON-NLS-1$
	setRowsCmd.setAttributeSettingValue(rowsSetting);
	// Now create the commands to remove the constraints from the children and apply 
	// the layout manager to the container.
	return setRowsCmd.chain(super.getCommand(sf, newManager));
}
/**
 * Returns a compound command containing the child constraint commands to position the
 * children within a flow Layout container.
 */
protected Command getChangeConstraintsCommand(List children) {		
	return helper.getChangeConstraintCommand(children, Collections.nCopies(children.size(), null));	// Now let the helper add them back correctly.
}
}
