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
 *  $$Revision: 1.10 $$  $$Date: 2005-03-28 22:09:51 $$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
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
	/**
	 * Ensure that the child argument belongs to the correct parent for its ParseTreeAllocation
	 * There are two situations where this is required
	 * 1	-	Dropping from the palette for prototype instances such as CheckBox.xmi with {parentComposite} as a PTName to be replaced
	 * 2	-	Moving between parents using GEF such as dragging in the tree or viewer	
	 * 
	 * @since 1.0.0
	 */
	public class EnsureCorrectParentCommand extends AbstractCommand{
		private IJavaObjectInstance javaChild;
		public EnsureCorrectParentCommand(IJavaObjectInstance aChild){
			javaChild = aChild;
		}
		public void execute() {
			if(javaChild.getAllocation() != null){
				IJavaObjectInstance correctParent = (IJavaObjectInstance)getContainer(); 
				if(javaChild.getAllocation() instanceof ParseTreeAllocation){
					PTExpression expression = ((ParseTreeAllocation)javaChild.getAllocation()).getExpression();
					if(expression instanceof PTClassInstanceCreation){
						PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) expression;
						if(classInstanceCreation.getArguments().size() == 2){
							Object firstArgument = classInstanceCreation.getArguments().get(0);
							if(firstArgument instanceof PTName && ((PTName)firstArgument).getName().equals("{parentComposite}")){
								PTInstanceReference parentRef = InstantiationFactory.eINSTANCE.createPTInstanceReference();
								parentRef.setObject(correctParent);
								classInstanceCreation.getArguments().remove(0);
								classInstanceCreation.getArguments().add(0,parentRef);
								// 	ReCreate the allocation feature so that CodeGen will reGenerate the constructor
								ParseTreeAllocation newAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
								newAlloc.setExpression(expression);								
								javaChild.setAllocation(newAlloc);
							} else if (firstArgument instanceof PTInstanceReference){
								PTInstanceReference instanceReference = (PTInstanceReference)firstArgument;
								if(instanceReference.getObject() != correctParent){
									instanceReference.setObject(correctParent);
									// 	ReCreate the allocation feature so that CodeGen will reGenerate the constructor
									ParseTreeAllocation newAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
									newAlloc.setExpression(expression);								
									javaChild.setAllocation(newAlloc);
								}
							}
						}
					} 			
				}
			}
		}
		protected boolean prepare() {
			return true;
		}
	}	
	
	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		Command result = super.getCreateCommand(child, positionBeforeChild);
		final IJavaObjectInstance javaChild = (IJavaObjectInstance)child;
		// If we already have a java allocation then check to see whether it is a prototype instance with a 
		// {parentComposite} that needs substituting with the real parent
		if(javaChild.getAllocation() != null){
			Command insertCorrectParentCommand = new EnsureCorrectParentCommand((IJavaObjectInstance) child);
			return insertCorrectParentCommand.chain(result);
		} else {
			return createInitStringCommand((IJavaObjectInstance)child).chain(result);
		}
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
		PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT") ;
		fa.setField("NONE");
		fa.setReceiver(name) ;
		
		
		ic.getArguments().add(ir);
		ic.getArguments().add(fa) ;
		
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
		ApplyAttributeSettingCommand applyCmd = new ApplyAttributeSettingCommand();
		applyCmd.setTarget(child);
		applyCmd.setAttribute(child.eClass().getEStructuralFeature("allocation"));
		applyCmd.setAttributeSettingValue(alloc);	
		
		return applyCmd;		
		
	}
	
	public Command getOrphanChildrenCommand(List children) {

		CompoundCommand cmd = new CompoundCommand();
		Iterator iter = children.iterator();
		while(iter.hasNext()){
			IJavaObjectInstance child = (IJavaObjectInstance)iter.next();
			cmd.append(new EnsureCorrectParentCommand(child));
		}
		cmd.append(super.getOrphanChildrenCommand(children));
		return cmd;
	}
	
	protected Command primAddCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
		CompoundCommand cmd = new CompoundCommand();
		Iterator iter = children.iterator();
		while(iter.hasNext()){
			IJavaObjectInstance child = (IJavaObjectInstance)iter.next();
			if (!(BeanSWTUtilities.isValidBeanLocation(domain, child, (EObject)container)))
				return UnexecutableCommand.INSTANCE;
			cmd.append(new EnsureCorrectParentCommand(child));
		}
		cmd.append(super.primAddCommand(children, positionBeforeChild, containmentSF));
		return cmd;
	}

	public Command getCreateCommand(Object constraintComponent, Object childComponent, Object position) {
		// TODO Auto-generated method stub
		return null;
	}

	public Command getAddCommand(List componentConstraints, List childrenComponents, Object position) {
		// TODO Auto-generated method stub
		return null;
	}

}
