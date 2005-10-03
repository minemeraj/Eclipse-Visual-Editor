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
 *  $$Revision: 1.22 $$  $$Date: 2005-10-03 19:20:48 $$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;

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

	public Command getCreateCommand(Object constraintComponent, Object childComponent, Object position) {
		return null;
	}

	public Command getAddCommand(List componentConstraints, List childrenComponents, Object position) {
		return null;
	}		

}
