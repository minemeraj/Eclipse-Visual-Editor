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
 *  $Revision: 1.11 $  $Date: 2005-10-03 19:21:01 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.NoAddException;
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
	 * Get the create command for the child constraint listed here at the position.
	 */
	public Command getCreateCommand(Object childConstraintComponent, Object childComponent, Object positionBeforeChild) {
		CommandBuilder preCmds = createCommandBuilder(true);
		CommandBuilder postCmds = createCommandBuilder(true);
		try {
			childComponent = getTrueChild(childComponent, true, preCmds, postCmds);
			if (childComponent != null)
				if (!isValidComponent(childComponent))
					return UnexecutableCommand.INSTANCE;
		} catch (NoAddException e) {
			return UnexecutableCommand.INSTANCE;
		}
		
		if (preCmds.isDead() || postCmds.isDead())
			return UnexecutableCommand.INSTANCE;
		
		if (childComponent != null) {
			// Add the child to the component.
			preCmds.applyAttributeSetting((EObject) childConstraintComponent, sfConstraintComponent, childComponent);
			getCreateCommand(childConstraintComponent, positionBeforeChild != null ? InverseMaintenanceAdapter.getIntermediateReference(
					(EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) positionBeforeChild) : null, preCmds);
			
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			return UnexecutableCommand.INSTANCE;
		else
			return preCmds.getCommand();

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

		CommandBuilder preCmds = createCommandBuilder(true);
		CommandBuilder postCmds = createCommandBuilder(true);

		RuledCommandBuilder cb = new RuledCommandBuilder(domain);
		cb.setApplyRules(false);
		constraints = new ArrayList(constraints);	// We'll be possibly modifying the list, so make a copy. Our contract doesn't state we can change the original.
		ListIterator cons = constraints.listIterator();
		Iterator chlds = children.iterator();
		while (cons.hasNext()) {
			Object childConstraintComponent = cons.next();
			if (!isValidChild(childConstraintComponent, sfConstraintComponent))
				return UnexecutableCommand.INSTANCE;
			Object childComponent = chlds.next();
			try {
				childComponent = getTrueChild(childComponent, false, preCmds, postCmds);
				if (childComponent != null)
					if (!isValidComponent(childComponent))
						return UnexecutableCommand.INSTANCE;
					else
						;
				else {
					cons.remove();	// Remove the constraint since handler said no child for this one.
					continue;	// Handler set skip this one.
				}
			} catch (NoAddException e) {
				return UnexecutableCommand.INSTANCE;
			}			
			cb.applyAttributeSetting((EObject) childConstraintComponent, sfConstraintComponent, childComponent);
		}

		if (preCmds.isDead() || postCmds.isDead())
			return UnexecutableCommand.INSTANCE;

		if (!constraints.isEmpty()) {
			// We added the component to the constraint components outside of Ruled control because there is no need for
			// extra preset commands since the setting of constraintComponent itself will be under Ruled control and will handle
			// the component setting automatically.		
			cb.setApplyRules(true);
			cb.applyAttributeSettings((EObject) container, containmentSF, constraints, positionBeforeChild != null ? InverseMaintenanceAdapter
					.getIntermediateReference((EObject) container, (EReference) containmentSF, sfConstraintComponent, (EObject) positionBeforeChild)
					: null);
			preCmds.append(cb.getCommand());
		}
		
		preCmds.append(postCmds.getCommand());
		if (preCmds.isEmpty() || preCmds.isDead())
			return UnexecutableCommand.INSTANCE;
		else
			return preCmds.getCommand();
		
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
