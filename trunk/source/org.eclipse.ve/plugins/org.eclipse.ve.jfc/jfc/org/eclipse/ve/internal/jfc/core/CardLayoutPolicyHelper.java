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
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.commands.CancelAttributeSettingCommand;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

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

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ILayoutPolicyHelper#getCreateChildCommand(Object, Object, Object)
	 */
	public Command getCreateChildCommand(Object childComponent, Object constraint, Object position) {		
		CommandBuilder cb = new CommandBuilder(""); //$NON-NLS-1$
		Command applyCmd = null;
		EObject constraintComponent = visualFact.create(classConstraintComponent);
		if (constraint != NO_CONSTRAINT_VALUE)
			constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), convertConstraint(constraint));	// Put the constraint into the constraint component.
		else {
			constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), getDefaultStringConstraintObject());	// So no errors while being added because card layout can't handle null constraint and we can't get the name until component construction.
			applyCmd = getConstraintCommand(childComponent, constraintComponent);
		}
		cb.append(policy.getCreateCommand(constraintComponent, childComponent, position));
		cb.append(applyCmd);
		return cb.getCommand();
	}


	public Command getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		CompoundCommand constraintCommands = new CompoundCommand();
		ArrayList componentConstraints = new ArrayList(childrenComponents.size());
		int i = 0;
		Iterator itr = constraints.iterator();
		while (itr.hasNext()) {
			EObject constraintComponent = visualFact.create(classConstraintComponent);
			componentConstraints.add(constraintComponent);
			Object constraint = itr.next();
			if (constraint != NO_CONSTRAINT_VALUE)
				constraintComponent.eSet(sfConstraintConstraint, convertConstraint(constraint));	// Put the constraint into the constraint component.
			else {
				constraintComponent.eSet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT), getDefaultStringConstraintObject());				
				constraintCommands.append(getConstraintCommand(childrenComponents.get(i), constraintComponent));
			}
			i++;
		}
		CommandBuilder cb = new CommandBuilder();
		cb.append(policy.getAddCommand(componentConstraints, childrenComponents, position));
		cb.append(constraintCommands.unwrap());
		return cb.getCommand();
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
		// Check if 'name' is set... which is used by default as the constraint.
		// If not,we need to set a place holder constraint and a special command
		// so that later on when we execute the command, we can derive the constraint
		// name from the annotation composition name and set the name property.
	
		if (!isNameSet((EObject)childComponent)) {
			return new ApplyCardLayoutConstraintCommand(constraintComponent, (EObject)childComponent, policy.getEditDomain());
		}
		
		if (constraintComponent.eIsSet(sfConstraintConstraint)) {
			CancelAttributeSettingCommand cmd = new CancelAttributeSettingCommand();
			cmd.setTarget(constraintComponent);
			cmd.setAttribute(sfConstraintConstraint);
			return cmd;
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
