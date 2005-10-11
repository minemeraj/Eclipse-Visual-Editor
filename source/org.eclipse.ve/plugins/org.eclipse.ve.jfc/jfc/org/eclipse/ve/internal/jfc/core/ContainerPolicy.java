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
 *  $RCSfile: ContainerPolicy.java,v $
 *  $Revision: 1.12 $  $Date: 2005-10-11 21:23:50 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
/**
 * AWT Container policy for handling children in an AWT container.
 * This is special in that none of the add/create commands
 * come through the standard methods. This is because they are
 * special in Containers. The children are ConstraintComponents but
 * the model object of the child editpart is awt.Component. So the
 * requests wouldn't have the ConstraintComponent in them.
 * So the layout and tree editpolicies will cast directly to
 * ContainerEditPolicy and use the special methods listed here.
 *
 * The delete, move, and orphan commands can come through the
 * standard way because no pre-processing is needed in the
 * layout/tree policies to strip out the components and constraints.
 */
public class ContainerPolicy extends VisualContainerPolicy {
	
	protected EReference sfConstraintComponent;
	protected EClass 
		classConstraintComponent,
		classComponent;
	
	public ContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_CONTAINER_COMPONENTS), domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		classConstraintComponent = (EClass) rset.getEObject(JFCConstants.CLASS_CONTAINER_CONSTRAINTCOMPONENT, true);
		classComponent = (EClass) sfConstraintComponent.getEType();
	}
	
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		return classConstraintComponent.isInstance(child);
	}
	
	protected boolean isValidComponent(Object component) {
		// Also need to verify that the component is a valid component
		return classComponent.isInstance(component);
	}
		
		
	/**
	 * Delete the dependent. The child is the component, not the constraintComponent.
	 */
	public Command getDeleteDependentCommand(Object child) {
		return super.getDeleteDependentCommand(InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) child));
	}
	
	
	/**
	 * Get the move children command for the list. The children
	 * are the components, not the constraintComponents.
	 */	
	public Command getMoveChildrenCommand(List children, Object positionBeforeChild) {
		// We need to convert to the constraintComponents, not the components.
		List constraints = new ArrayList(children.size());
		Iterator itr = children.iterator();
		while(itr.hasNext()) {
			constraints.add(InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) itr.next()));
		}
		return super.getMoveChildrenCommand(constraints, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) positionBeforeChild) : null); 
	}
		

	/**
	 * Get the orphan command for the list. The children
	 * are the components, not the constraintComponents.
	 */
	protected Command getOrphanTheChildrenCommand(List children) {
		// We need to unset the components from the constraints after
		// orphaning the constraints so that they are free of any
		// containment when they are added to their new parent. If we
		// didn't unset the components, then upon undo the component
		// would not be in the constraint when it is added back in because
		// the old parent would of gotton lost.
		//
		// It is required that when orphaning, the contraintComponents themselves
		// are not reused on the subsequent add. They will be thrown away.
				
		List constraints = new ArrayList(children.size());
		Iterator itr = children.iterator();
		while(itr.hasNext()) {
			EObject constraint = InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) itr.next());
			constraints.add(constraint);
		}
		
		// The order of below will result in:
		//   1) Remove all of the constraints from the container.
		//   2) Remove all of the components from the constraints.
		//   3) Post set will handle the constraints that were removed (but since by this time the components have been remove
		//      from the constraints, they won't be processed.
		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		cb.cancelAttributeSettings((EObject) container, containmentSF, constraints); // Delete the constraint components under rule control so that they will go away.
		cb.setApplyRules(false);
		cb.cancelGroupAttributeSetting(constraints, sfConstraintComponent);	// Cancel out all of the component settings not under rule control since we are keeping them.
		return cb.getCommand();
	}

	protected void getCreateCommand(List constraints, List children, Object position, CommandBuilder cbld) {
		// For AWT Container, add and create are the same because add will still handle correctly adding the child annotation if needed
		// through the rule builder.
		getAddCommand(constraints, children, position, cbld);
	}

	protected void getAddCommand(List constraints, List children, Object position, CommandBuilder cbld) {
		RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
		Iterator childrenItr = children.iterator();
		Iterator constraintsItr = constraints.iterator();
		List componentConstraints = new ArrayList(constraints.size());
		// First we go through and add the children to the constraint components. This is done without rules because no need
		// to use rules yet since the constraint components are not yet in the model.
		// Then we add the constraint components to the container. This time with rules. 
		rcb.setApplyRules(false);
		while (childrenItr.hasNext()) {
			Object childConstraintComponent = constraintsItr.next();
			if (childConstraintComponent instanceof ConstraintWrapper)
				childConstraintComponent = ((ConstraintWrapper) childConstraintComponent).getConstraint();
			componentConstraints.add(childConstraintComponent);
			if (!isValidChild(childConstraintComponent, sfConstraintComponent)) {
				cbld.markDead();
				return;
			}
			Object childComponent = childrenItr.next();
			if (!isValidComponent(childComponent)) {
				cbld.markDead();
				return;
			}
			rcb.applyAttributeSetting((EObject) childConstraintComponent, sfConstraintComponent, childComponent);
		}
		
		// We added the component to the constraint components outside of Ruled control because there is no need for
		// extra preset commands since the setting of constraintComponents themselves will be under Ruled control and will handle
		// the component setting automatically. But now we apply rules so that the add of the constraint component walks through and
		// gets all children.
		rcb.setApplyRules(true);
		rcb.applyAttributeSettings((EObject) container, containmentSF, componentConstraints, position!= null ? InverseMaintenanceAdapter
				.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent,
						(EObject) position) : null);
		cbld.append(rcb.getCommand());
	}

}
