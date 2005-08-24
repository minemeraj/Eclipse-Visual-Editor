/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LayoutPolicyHelper.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:52:55 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
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

protected IJavaObjectInstance getContainer() {
	return (IJavaObjectInstance) policy.getContainer();
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

/*
 *  (non-Javadoc)
 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getOrphanConstraintsCommand(java.util.List)
 * 
 * Forward to subclasses to remove any specific contraints, properties, etc. (e.g. null layout to remove bounds, size, location).
 * Then remove the layout data of each child since it's not applicable to what layout we've switched to.
 */
public Command getOrphanConstraintsCommand(List children) {
	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cancelConstraints(cb, children);
	// We need to cancel the layout data for each of the controls
	IJavaObjectInstance parent = (IJavaObjectInstance) policy.getContainer();
	EStructuralFeature sf_layoutData = JavaInstantiation.getSFeature(parent, SWTConstants.SF_CONTROL_LAYOUTDATA);
	cb.cancelGroupAttributeSetting(children, sf_layoutData);
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
	return NoOpCommand.INSTANCE;
}

	
}
