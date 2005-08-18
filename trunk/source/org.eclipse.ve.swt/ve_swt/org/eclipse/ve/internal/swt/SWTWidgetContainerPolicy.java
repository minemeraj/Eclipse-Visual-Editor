/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

 

/**
 * This is a standard SWT Widget container policy that is for 
 * a SWT Widget that takes children, but IS not for standard
 * Composites (i.e. not using the "controls" feature and not
 * having layout data). Such as for Table. 
 * <p>
 * This is used where the children have a constructor of the 
 * form <code>childWidget(Widget parentWidget, ...)</code>. It makes
 * sure that the parent is in the child's allocation.
 * @since 1.1.0.1
 */
public class SWTWidgetContainerPolicy extends BaseJavaContainerPolicy {

	public SWTWidgetContainerPolicy(EStructuralFeature feature, EditDomain domain) {
		super(feature, domain);
	}

	public SWTWidgetContainerPolicy(EditDomain domain) {
		super(domain);
	}
	
	public Command getCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		Command result = super.getCreateCommand(child, positionBeforeChild, containmentSF);
		final IJavaObjectInstance javaChild = (IJavaObjectInstance)child;
		// If we already have a java allocation then check to see whether it is a prototype instance with a 
		// {parentComposite} that needs substituting with the real parent
		if(javaChild.getAllocation() != null){
			Command insertCorrectParentCommand = new EnsureCorrectParentCommand(javaChild, (IJavaObjectInstance) getContainer());
			return insertCorrectParentCommand.chain(result);
		} else {
			return createInitStringCommand((IJavaObjectInstance)child).chain(result);
		}
	}
	
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		CompoundCommand cmd = new CompoundCommand();
		Iterator iter = children.iterator();
		while(iter.hasNext()){
			IJavaObjectInstance child = (IJavaObjectInstance)iter.next();
			cmd.append(new EnsureCorrectParentCommand(child, (IJavaObjectInstance) getContainer()));
		}
		cmd.append(super.primAddCommand(children, positionBeforeChild, containmentSF));
		return cmd;
	}
	
	protected boolean isValidBeanLocation(Object child) {
		return BeanSWTUtilities.isValidBeanLocation(getEditDomain(), (IJavaObjectInstance)child, (EObject) getContainer());
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
	
	

}
