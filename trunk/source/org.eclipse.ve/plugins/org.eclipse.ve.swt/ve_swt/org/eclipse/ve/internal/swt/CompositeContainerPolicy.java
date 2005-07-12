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
 *  $$Revision: 1.18 $$  $$Date: 2005-07-12 18:41:15 $$ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.ApplyAttributeSettingCommand;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.BeanUtilities;
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
		private boolean changedAllocation = false;
		IJavaObjectInstance correctParent;
		public EnsureCorrectParentCommand(IJavaObjectInstance aChild){
			javaChild = aChild;
		}
		public void execute() {
			if(javaChild.getAllocation() != null){
				correctParent = (IJavaObjectInstance)getContainer(); 
				if(javaChild.getAllocation() instanceof ParseTreeAllocation){
					PTExpression expression = ((ParseTreeAllocation)javaChild.getAllocation()).getExpression();					
					if(expression instanceof PTClassInstanceCreation){
						visitClassInstanceCreation((PTClassInstanceCreation)expression);
					} else if (expression instanceof PTMethodInvocation){
						visitMethodInvocation((PTMethodInvocation)expression);
					}
					if(changedAllocation){
						// ReCreate the allocation feature so that CodeGen will reGenerate the constructor
						ParseTreeAllocation newAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
						newAlloc.setExpression(expression);								
						javaChild.setAllocation(newAlloc);					
					}					
				}
			}
		}
		private void visitClassInstanceCreation(PTClassInstanceCreation expression){
			visitArguments(expression.getArguments());
		}
		private void visitMethodInvocation(PTMethodInvocation expression){
			visitArguments(expression.getArguments());
		}		
		
		private void visitArguments(List arguments){
			// Find all references to {parentComposite} and swop them with a pointer to the real composite parent
			ArrayList argsCopy = new ArrayList(arguments.size());
			argsCopy.addAll(arguments);	// Use a copy so we don't access and change the same collection in the loop
			for (int i = 0; i < argsCopy.size(); i++) {
				Object argument = argsCopy.get(i);
				if(argument instanceof PTName && SwtPlugin.PARENT_COMPOSITE_TOKEN.equals(((PTName)argument).getName())){ //$NON-NLS-1$
					PTInstanceReference parentRef = InstantiationFactory.eINSTANCE.createPTInstanceReference();
					parentRef.setObject(correctParent);
					arguments.remove(i);
					arguments.add(i,parentRef);
					changedAllocation = true;
				} else if (argument instanceof PTInstanceReference){
					PTInstanceReference instanceReference = (PTInstanceReference)argument;
					if(instanceReference.getObject() != correctParent){
						instanceReference.setObject(correctParent);
						changedAllocation = true;
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
			Command insertCorrectParentCommand = new EnsureCorrectParentCommand(javaChild);
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
	
	protected Command getOrphanTheChildrenCommand(List children) {

		CompoundCommand cmd = new CompoundCommand();
		Iterator iter = children.iterator();
		while(iter.hasNext()){
			IJavaObjectInstance child = (IJavaObjectInstance)iter.next();
			cmd.append(new EnsureCorrectParentCommand(child));
		}
		cmd.append(super.getOrphanTheChildrenCommand(children));
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
	
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		// If two or more of the child conntrols being deleted share the same instance variable do not allow deletion because we
		// cannot correctly update the code yet so it's safer to disallow for now
		List children;
		if(child instanceof List){
			children = (List)child;
		} else {
			children = new ArrayList(1);
			children.add(child);			
		}

		return super.getDeleteDependentCommand(child, containmentSF);
	}
	
	protected Command getMoveChildrenCommand(List children, Object positionBeforeChild, EStructuralFeature containmentSF) {
//		// If two or more of the child conntrols being moved share the same instance variable do not allow movement because we
//		// cannot correctly update the code yet so it's safer to disallow for now
//		if(areFieldNamesShared(children,containmentSF)){
//			return UnexecutableCommand.INSTANCE;
//		} else {
			return super.getMoveChildrenCommand(children, positionBeforeChild,containmentSF);
//		}
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
