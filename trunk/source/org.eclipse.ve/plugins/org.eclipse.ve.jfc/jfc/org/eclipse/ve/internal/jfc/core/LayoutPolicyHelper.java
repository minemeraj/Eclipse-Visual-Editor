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
 *  $RCSfile: LayoutPolicyHelper.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-11 21:23:50 $ 
 */


import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.*;
/**
 * Layout Policy helper. This can be used by all layout types if they wish.
 * Creation date: (11/10/00 11:55:27 AM)
 * @author: Peter Walker
 */
public abstract class LayoutPolicyHelper implements ILayoutPolicyHelper {
	protected VisualContainerPolicy policy;

	protected EReference sfConstraintComponent, sfConstraintConstraint, sfComponents;
	protected EClass classConstraintComponent;
	protected EFactory visualFact;
	
	public LayoutPolicyHelper(VisualContainerPolicy ep) {
		setContainerPolicy(ep);
	}
	
	public LayoutPolicyHelper() {
	}
	
	public void setContainerPolicy(VisualContainerPolicy policy) {
		this.policy = policy;
		if (policy != null) {
			// Eventually we will be set with a policy. At that time we can compute these.
			ResourceSet rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
			sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
			sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
			sfComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
			
			classConstraintComponent = (EClass) rset.getEObject(JFCConstants.CLASS_CONTAINER_CONSTRAINTCOMPONENT, true);		
			visualFact = JFCConstants.getFactory(classConstraintComponent);		
		}
	}
	
	protected IJavaObjectInstance getContainer() {
		return (IJavaObjectInstance) policy.getContainer();
	}
	
	public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {		
		EObject constraintComponent = visualFact.create(classConstraintComponent);
		if (constraint != NO_CONSTRAINT_VALUE)
			constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), constraint != null ? convertConstraint(constraint) : null);	// Put the constraint into the constraint component.
		return policy.getCreateCommand(constraintComponent, childComponent, position);
	}
	
	public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		ArrayList componentConstraints = new ArrayList(childrenComponents.size());
		Iterator itr = constraints.iterator();
		while (itr.hasNext()) {
			EObject constraintComponent = visualFact.create(classConstraintComponent);
			componentConstraints.add(constraintComponent);
			Object constraint = itr.next();
			if (constraint != NO_CONSTRAINT_VALUE)
				constraintComponent.eSet(sfConstraintConstraint, constraint != null ? convertConstraint(constraint) : null);	// Put the constraint into the constraint component.
		}
		
		return policy.getAddCommand(componentConstraints, childrenComponents, position);
	}
	
	public Command getOrphanChildrenCommand(List children) {	
		// Now get the orphan command for the children.
		return policy.getOrphanChildrenCommand(children);
	}
	
	/**
	 * Apply the corresponding constraint to each child.
	 * The child is the component itself, it must be inside a ConstraintComponent.
	 */
	public Command getChangeConstraintCommand(List children, List constraints) {
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		
		Iterator childs = children.iterator();
		Iterator consts = constraints.iterator();
		while (childs.hasNext()) {
			EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) policy.getContainer(), sfComponents, sfConstraintComponent, (EObject) childs.next());
			Object oconstraint = consts.next();
			if (oconstraint != NO_CONSTRAINT_VALUE) {
				IJavaObjectInstance constraint = oconstraint != null ? convertConstraint(oconstraint) : null;
				cb.applyAttributeSetting(constraintComponent, sfConstraintConstraint, constraint);
			} else
				cb.cancelAttributeSetting(constraintComponent, sfConstraintConstraint);
		}
		return cb.getCommand();
	}
	
	/*
	 * Convert the incoming constraint to the actual constraint.
	 * This is necessary because often the incoming constraint is not a java object.
	 * It does this for convienence. But we need to java object.
	 */
	protected abstract IJavaObjectInstance convertConstraint(Object constraint);
	
	/**
	 * @see org.eclipse.ve.internal.jfc.core.ILayoutPolicyHelper#getOrphanConstraintsCommand(List)
	 */
	public Command getOrphanConstraintsCommand(List constraintComponents) {
		return null;
	}

}


