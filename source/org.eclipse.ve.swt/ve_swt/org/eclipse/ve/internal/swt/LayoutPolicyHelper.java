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
 *  $RCSfile: LayoutPolicyHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-04 02:13:17 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.ContainerPolicy;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

import org.eclipse.gef.commands.UnexecutableCommand;
/**
 * 
 * @since 1.0.0
 */
public abstract class LayoutPolicyHelper implements ILayoutPolicyHelper {
	
	protected ContainerPolicy policy;	
	
public LayoutPolicyHelper(VisualContainerPolicy ep){
	setContainerPolicy(ep);
}
public LayoutPolicyHelper(){
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

public Command getAddChildrenCommand(List childrenComponents, List constraints, Object position) {

	Command addContributionCmd = policy.getAddCommand(childrenComponents, position);
	if (addContributionCmd == null || !addContributionCmd.canExecute())
		return UnexecutableCommand.INSTANCE;	// It can't be added.
		
	CompoundCommand command = new CompoundCommand("");		 //$NON-NLS-1$
	command.append(getChangeConstraintCommand(childrenComponents, constraints));
	command.append(addContributionCmd);

	return command.unwrap();
}

public Command getOrphanChildrenCommand(List children) {
	
	// Now get the orphan command for the children.
	Command orphanContributionCmd = policy.getOrphanChildrenCommand(children);
	if (orphanContributionCmd == null || !orphanContributionCmd.canExecute())
		return UnexecutableCommand.INSTANCE;	// It can't be orphaned		

	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cb.append(orphanContributionCmd);	
	cancelConstraints(cb, children);
	return cb.getCommand();

}
protected abstract void cancelConstraints(CommandBuilder commandBuilder, List children);

public Command getOrphanConstraintsCommand(List children) {
	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cancelConstraints(cb, children);
	return cb.getCommand();
}


public void setContainerPolicy(VisualContainerPolicy policy) {
	this.policy = policy;
}		

/**
 * Apply the corresponding constraint to each child.
 * The child is the Control itself
 */
public Command getChangeConstraintCommand(List children, List constraints) {
	
	// TODO Need to figure out what we do here - JRW
	return UnexecutableCommand.INSTANCE;
}

	
}
