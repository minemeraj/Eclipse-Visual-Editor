/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.7 $  $Date: 2005-06-24 18:57:15 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

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
	
	public Command getCreateCommand(CreateRequest request, Object positionBeforeChild) {	
		return UnexecutableCommand.INSTANCE;	// This should never be called. It should go through the getCreateCommand(constraint, component, position);
	}
	
	/**
	 * Get the create command for the child constraint listed here at the position.
	 */
	public Command getCreateCommand(Object childConstraintComponent, Object childComponent, Object positionBeforeChild) {
		// Verify that the component and the child are valid.
		if (!isValidChild(childConstraintComponent, containmentSF) || !isValidComponent(childComponent) || !isParentAcceptable(childComponent))
			return UnexecutableCommand.INSTANCE;
			
		// Add the child to the component.
		CommandBuilder cb = new CommandBuilder();
		cb.applyAttributeSetting((EObject) childConstraintComponent, sfConstraintComponent, childComponent);
		cb.append(primCreateCommand(childConstraintComponent, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) positionBeforeChild) : null, containmentSF));
		return cb.getCommand();
	}
	
	
	public Command getAddCommand(GroupRequest request, Object position) {
		return UnexecutableCommand.INSTANCE;	// This should never be called. It should go through the getAddCommand(constraints, components, position);
	}

	/**
	 * Get the add constraints and components command for the list and position.
	 */
	public Command getAddCommand(List constraints, List children, Object positionBeforeChild) {
		// We need to create the add children to constraints command first.
		// This is because we can't set the children into the constraints except
		// at command execution time. If we do it now it would immediately
		// make the change and this would strip the child out even if the commands
		// were never executed.
		//
		// It is assumed that both lists are the same length.


		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		cb.setApplyRules(false);	
		Iterator cons = constraints.iterator();
		Iterator chlds = children.iterator();
		while (cons.hasNext()) {
			Object childConstraintComponent = cons.next();
			Object childComponent = chlds.next();
			if (!isValidChild(childConstraintComponent, containmentSF) || !isValidComponent(childComponent) || !isParentAcceptable(childComponent)
					|| !(BeanAwtUtilities.isValidBeanLocation(domain, (EObject) childComponent)))
				return UnexecutableCommand.INSTANCE;
			cb.applyAttributeSetting((EObject) childConstraintComponent, sfConstraintComponent, childComponent);
		}

		// We added the component to the constraint components outside of Ruled control because there is no need for
		// extra preset commands since the setting of constraintComponent itself will be under Ruled control and will handle
		// the component setting automatically.		
		cb.setApplyRules(true);
		cb.applyAttributeSettings((EObject) container, containmentSF, constraints, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) positionBeforeChild) : null);		
		return cb.getCommand();
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

}
