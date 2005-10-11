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
 *  $RCSfile: CardLayoutPolicyHelper.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-11 21:23:50 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * @version 	1.0
 * @author
 */
public class CardLayoutPolicyHelper extends LayoutPolicyHelper {
	protected EStructuralFeature sfName;	

	public CardLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}
	
	public CardLayoutPolicyHelper() {
	}

	/*
	 * @see LayoutPolicyHelper#convertConstraint(Object)
	 */
	protected IJavaObjectInstance convertConstraint(Object constraint) {
		if (constraint instanceof String)
			return BeanUtilities.createString(getContainer().eResource().getResourceSet(), (String) constraint);
		else
			return null;
	}
	/*
	 * @see ILayoutPolicyHelper#getDefaultConstraint(List)
	 */
	public List getDefaultConstraint(List children) {
		return Collections.nCopies(children.size(), NO_CONSTRAINT_VALUE);
	}


	public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {		
		CommandBuilder cb = new CommandBuilder(); 
		EObject constraintComponent = visualFact.create(classConstraintComponent);
		if (constraint != NO_CONSTRAINT_VALUE)
			constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), convertConstraint(constraint));	// Put the constraint into the constraint component.
		else {
			constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), getDefaultStringConstraintObject());	// So no errors while being added because card layout can't handle null constraint and we can't get the name until component construction.
		}
		VisualContainerPolicy.CorelatedResult result = policy.getCreateCommand(constraintComponent, childComponent, position);
		cb.append(result.getCommand());
		if (constraint == NO_CONSTRAINT_VALUE && !result.getChildren().isEmpty())
			cb.append(getConstraintCommand(result.getChildren().get(0), constraintComponent));	// The child was added. Use the true child (from the list, it may of changed).
		result.setCommand(cb.getCommand());
		return result;
	}

	private static class CardConstraintWrapper extends VisualContainerPolicy.ConstraintWrapper {

		public final Object constraint2;

		public CardConstraintWrapper(Object constraintComponent, Object constraint) {
			super(constraintComponent);
			constraint2 = constraint;
		}
		
		
	}
	public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		ArrayList componentConstraints = new ArrayList(childrenComponents.size());
		int i = 0;
		Iterator itr = constraints.iterator();
		while (itr.hasNext()) {
			EObject constraintComponent = visualFact.create(classConstraintComponent);
			Object constraint = itr.next();
			componentConstraints.add(new CardConstraintWrapper(constraintComponent, constraint));
			if (constraint != NO_CONSTRAINT_VALUE)
				constraintComponent.eSet(sfConstraintConstraint, convertConstraint(constraint));	// Put the constraint into the constraint component.
			else {
				constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), getDefaultStringConstraintObject());				
			}
			i++;
		}
		CommandBuilder cb = new CommandBuilder();
		VisualContainerPolicy.CorelatedResult result = policy.getAddCommand(componentConstraints, childrenComponents, position);
		cb.append(result.getCommand());
		itr = result.getChildren().iterator();
		Iterator constraintsItr = result.getCorelatedList().iterator();
		while (itr.hasNext()) {
			Object child = itr.next();
			Object constraint = constraintsItr.next();
			if (((CardConstraintWrapper) constraint).constraint2 == NO_CONSTRAINT_VALUE)
				cb.append(getConstraintCommand(child, (EObject) ((CardConstraintWrapper) constraint).getConstraint()));
		}
		result.setCommand(cb.getCommand());
		return result;
	}
	/**
	 * Check to see if the 'name' attribute is part-of, and set for a given EObject
	 */
	protected boolean isNameSet(EObject obj) {
		// First check to see that 'name' is a valid attribute for this object		
		EClass container = sfName.getEContainingClass();
		if (!container.isInstance(obj)) return false ;
		
		return obj.eIsSet(sfName) ;
	}

	protected Command getConstraintCommand(Object childComponent, EObject constraintComponent) {
		RuledCommandBuilder rcb = new RuledCommandBuilder(policy.getEditDomain());
		// Check if 'name' is set... which is used by default as the constraint.
		// If not,we need to set a place holder constraint and a special command
		// so that later on when we execute the command, we can derive the constraint
		// name from the annotation composition name and set the name property.
	
		if (!isNameSet((EObject)childComponent)) {
			rcb.append(new ApplyCardLayoutConstraintCommand(constraintComponent, (EObject)childComponent, policy.getEditDomain()));
			return rcb.getCommand();
		}
		
		if (constraintComponent.eIsSet(sfConstraintConstraint)) {
			rcb.cancelAttributeSetting(constraintComponent, sfConstraintConstraint);
			return rcb.getCommand();
		}
		
		return null;
		
	}

	/*
	 * Provide a dummy string value for now so the card layout won't throw an exception
	 */	
	protected IJavaObjectInstance getDefaultStringConstraintObject() {
		return BeanUtilities.createString(getContainer().eResource().getResourceSet(), "NO_CONSTRAINT_VALUE"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ILayoutPolicyHelper#setContainerPolicy(ContainerPolicy)
	 */
	public void setContainerPolicy(VisualContainerPolicy policy) {
		super.setContainerPolicy(policy);
		if (policy != null) {
			// Eventually we will be set with a policy. At that time we can compute these.
			ResourceSet rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
			sfName = JavaInstantiation.getSFeature(rset, JFCConstants.SF_COMPONENT_NAME);
		}
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ILayoutPolicyHelper#getChangeConstraintCommand(List, List)
	 */
	public Command getChangeConstraintCommand(List children, List constraints) {
		RuledCommandBuilder cb = new RuledCommandBuilder(policy.getEditDomain());
		
		Iterator childs = children.iterator();
		Iterator consts = constraints.iterator();
		while (childs.hasNext()) {
			EObject child = (EObject) childs.next();
			EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject) policy.getContainer(), sfComponents, sfConstraintComponent, child);
			Object oconstraint = consts.next();
			if (oconstraint != NO_CONSTRAINT_VALUE) {
				IJavaObjectInstance constraint = convertConstraint(oconstraint);
				cb.applyAttributeSetting(constraintComponent, sfConstraintConstraint, constraint);
			} else {
				cb.append(getConstraintCommand(child, constraintComponent));
			}
		}
		return cb.getCommand();
	}

}
