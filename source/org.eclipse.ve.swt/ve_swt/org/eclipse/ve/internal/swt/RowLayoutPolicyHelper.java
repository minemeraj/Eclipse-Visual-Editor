/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: RowLayoutPolicyHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-11 14:57:12 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
 
/**
 * 
 * @since 1.0.0
 */
public class RowLayoutPolicyHelper extends LayoutPolicyHelper {


public RowLayoutPolicyHelper(VisualContainerPolicy ep) {
	super(ep);
}
public RowLayoutPolicyHelper(){
}

public Command getCreateChildCommand(Object childComponent, Object constraint, Object position) {

	Command createContributionCmd = policy.getCreateCommand(childComponent, position);
	if (createContributionCmd == null || !createContributionCmd.canExecute())
		return UnexecutableCommand.INSTANCE;	// It can't be created
		
	CompoundCommand command = new CompoundCommand("");		 //$NON-NLS-1$
	// TODO Add layoutData here to the compound command
	command.append(createContributionCmd);

	return command.unwrap();
}

protected void cancelConstraints(CommandBuilder commandBuilder, List children) {
	// TODO Auto-generated method stub
}

public Command getChangeConstraintCommand(List children, List constraints) {
	// TODO Auto-generated method stub
	return null;
}

public List getDefaultConstraint(List children) {
	// TODO Auto-generated method stub
	return null;
}

}
