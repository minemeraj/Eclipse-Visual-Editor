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
 *  $Revision: 1.11 $  $Date: 2005-11-04 17:30:52 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.core.ContainerPolicy.Result;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * 
 * @since 1.0.0
 */
public abstract class LayoutPolicyHelper implements ILayoutPolicyHelper {
	
	protected VisualContainerPolicy policy;	
	
public LayoutPolicyHelper(VisualContainerPolicy ep){
	setContainerPolicy(ep);
}
public LayoutPolicyHelper(){
}

protected IJavaObjectInstance getContainer() {
	return (IJavaObjectInstance) policy.getContainer();
}
		
public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {
	return policy.getCreateCommand(constraint, childComponent, position);
}

public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
	return policy.getAddCommand(constraints, childrenComponents, position);
}

public Result getOrphanChildrenCommand(List children) {
	
	// Now get the orphan command for the children.
	Result orphanContributionCmd = policy.getOrphanChildrenCommand(children);

	RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
	cb.append(orphanContributionCmd.getCommand());	
	cancelConstraints(cb, children);
	orphanContributionCmd.setCommand(cb.getCommand());
	return orphanContributionCmd;
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
