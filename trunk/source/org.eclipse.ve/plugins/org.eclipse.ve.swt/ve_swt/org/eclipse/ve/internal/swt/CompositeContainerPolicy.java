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
 *  $$RCSfile: CompositeContainerPolicy.java,v $$
 *  $$Revision: 1.23 $$  $$Date: 2005-10-11 21:23:47 $$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

public class CompositeContainerPolicy extends VisualContainerPolicy {
	
	protected EReference sfLayoutData;
	
	public CompositeContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(
			JavaEditDomainHelper.getResourceSet(domain), 
			SWTConstants.SF_COMPOSITE_CONTROLS), 
			domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
	}
	
	protected Command getOrphanTheChildrenCommand(List children) {
		Command orphanChildrenCommand = super.getOrphanTheChildrenCommand(children);
		// Need to cancel the childrens' layout data because we don't what it will be where we are going.
		RuledCommandBuilder cbld = new RuledCommandBuilder(getEditDomain());
		cbld.cancelGroupAttributeSetting(children, sfLayoutData);
		cbld.append(orphanChildrenCommand);
		return cbld.getCommand();
	}
	
	protected boolean isValidBeanLocation(Object child) {
		return BeanSWTUtilities.isValidBeanLocation(getEditDomain(), (IJavaObjectInstance)child, (EObject) getContainer());
	}

	protected boolean areFieldNamesShared(List children, EStructuralFeature containmentSF){
	
		IJavaInstance swtComposite = (IJavaInstance) getContainer();
		List compositeControls = (List) swtComposite.eGet(containmentSF);
		// 	Turn the list of controls into a their field names
		HashMap controlNames = new HashMap(compositeControls.size());
		Iterator allChildren = compositeControls.iterator();
		while(allChildren.hasNext()){
			IJavaInstance control = (IJavaInstance)allChildren.next();
			String fieldName = BeanUtilities.getBeanName(control,getEditDomain());
			controlNames.put(control,fieldName);
		}
	
		// Walk the children who are asking to be moved and see if any of these share a name with any of the children
		// (including those that aren't being moved)
		Iterator iter = children.iterator();
		while(iter.hasNext()){
			// This is the control asking to be moved
			IJavaInstance control = (IJavaInstance)iter.next();
			String fieldName = BeanUtilities.getBeanName(control,getEditDomain());			
			Iterator controlNamesIter = controlNames.keySet().iterator();
			while(controlNamesIter.hasNext()){
				Object controlToCompare = controlNamesIter.next();
				if(controlToCompare != control && controlNames.get(controlToCompare).equals(fieldName)){
					return true;
				}
			}
		}
		return false;
	}

	protected void getCreateCommand(List constraints, List children, Object position, CommandBuilder cbld) {
		// First create the create commands for the children so that we can let standard verification of the children be done.
		CommandBuilder createBuilder = createCommandBuilder(true); 
		getCreateCommand(children, position, createBuilder);
		if (createBuilder.isDead()) {
			cbld.markDead();
			return;
		}
		// Walk the constraints and apply to the children.
		Iterator constraintsItr = constraints.iterator();
		Iterator childrenItr = children.iterator();
		while(childrenItr.hasNext()) {
			EObject child = (EObject) childrenItr.next();
			Object constraint = constraintsItr.next();
			if (constraint instanceof ConstraintWrapper)
				constraint = ((ConstraintWrapper) constraint).getConstraint();
			if (constraint != null)
				cbld.applyAttributeSetting(child, sfLayoutData, constraint);
		}
		
		// Now apply the create commands. They should be in the list after the apply of the layout data so that
		// when they actually execute the layout data will already be set.
		cbld.append(createBuilder.getCommand());
	}

	protected void getAddCommand(List constraints, List children, Object position, CommandBuilder cbld) {

		// First create the add commands for the children so that we can let standard verification of the children be done.
		CommandBuilder addBuilder = createCommandBuilder(true); 
		getAddCommand(children, position, addBuilder);
		if (addBuilder.isDead()) {
			cbld.markDead();
			return;
		}
		
		// Walk the constraints and apply to the children.
		// The constraints need to be applied through a ruled builder because the children are already in the model for an add
		// so we need to have the constraints handled through rules to get added to the model correctly.
		RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
		Iterator constraintsItr = constraints.iterator();
		Iterator childrenItr = children.iterator();
		while(childrenItr.hasNext()) {
			EObject child = (EObject) childrenItr.next();
			Object constraint = constraintsItr.next();
			if (constraint instanceof ConstraintWrapper)
				constraint = ((ConstraintWrapper) constraint).getConstraint();
			if (constraint != null)
				rcb.applyAttributeSetting(child, sfLayoutData, constraint);
		}
		
		// The add commands should be in the list after the apply of the layout data so that
		// when they actually execute the layout data will already be set.
		cbld.append(rcb.getCommand());
		cbld.append(addBuilder.getCommand());
	}		

}
