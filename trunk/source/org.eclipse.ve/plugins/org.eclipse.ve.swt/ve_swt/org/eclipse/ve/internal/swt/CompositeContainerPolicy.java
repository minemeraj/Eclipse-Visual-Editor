/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 *  $$Revision: 1.19 $$  $$Date: 2005-08-18 21:55:55 $$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

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
	
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		Command result = super.getCreateCommand(child, positionBeforeChild);
		if (result.canExecute()) {
			// If we already have a java allocation then check to see whether it is a prototype instance with a 
			// {parentComposite} that needs substituting with the real parent
			if (child instanceof IJavaObjectInstance && ((IJavaObjectInstance) child).getAllocation() != null) {
				Command insertCorrectParentCommand = new EnsureCorrectParentCommand((IJavaObjectInstance) child, (IJavaObjectInstance) getContainer());
				return insertCorrectParentCommand.chain(result);
			} else {
				return createInitStringCommand((IJavaObjectInstance) child).chain(result);
			}
		} else
			return result;
	}
		
	/**
	 * This is a temporary hack to add an initialization string (allocation) to a dropped component
	 * which contain a parsed tree referencing the parent.
	 * 
	 * Rich has not implemented a ref. parsed tree yet, so use this as a deprecated method
	 * 
	 * @param parent
	 * @return
	 * 
	 * @since 1.0.0
	 */
	
	private Command createInitStringCommand(IJavaObjectInstance child) {
					
		// Class Creation tree - new Foo(args[])
		PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation() ;
		ic.setType(child.getJavaType().getJavaName()) ;
		
		// set the arguments
		PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference() ;
		ir.setObject((IJavaObjectInstance)getContainer()) ;	
		PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();	
		PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT") ; //$NON-NLS-1$
		fa.setField("NONE"); //$NON-NLS-1$
		fa.setReceiver(name) ;
		
		
		ic.getArguments().add(ir);
		ic.getArguments().add(fa) ;
		
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(child);
		applyCmd.setAttribute(child.eClass().getEStructuralFeature("allocation")); //$NON-NLS-1$
		applyCmd.setAttributeSettingValue(alloc);	
		
		return applyCmd;		
		
	}
	
	
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		Command command = super.primAddCommand(children, positionBeforeChild, containmentSF);
		if (command.canExecute()) {
			CompoundCommand cmd = new CompoundCommand();
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				try {
					cmd.append(new EnsureCorrectParentCommand((IJavaObjectInstance) iter.next(), (IJavaObjectInstance) getContainer()));
				} catch (ClassCastException e) {
					// OK, just in case not a java object. It should be.
				}
			}

			cmd.append(command);
			return cmd;
		} else
			return command;
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
