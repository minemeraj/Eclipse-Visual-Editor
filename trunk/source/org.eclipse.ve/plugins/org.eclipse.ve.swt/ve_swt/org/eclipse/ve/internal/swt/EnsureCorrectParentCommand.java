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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * Ensure that the child argument belongs to the correct parent for its ParseTreeAllocation. There are two situations where this is required:
 * <ol>
 * <li>Dropping from the palette for prototype instances such as CheckBox.xmi with {parentComposite} as a PTName to be replaced
 * <li>Moving between parents using GEF such as dragging in the tree or viewer
 * </ol>
 * 
 * @since 1.1.0.1
 */
public class EnsureCorrectParentCommand extends CommandWrapper {

	private IJavaObjectInstance javaChild;

	private IJavaObjectInstance correctParent;

	public EnsureCorrectParentCommand(IJavaObjectInstance aChild, IJavaObjectInstance correctParent) {
		javaChild = aChild;
		this.correctParent = correctParent;
	}

	public void execute() {
		// The javaChild may itself be wrapped by an object that takes the composite parent as an argument
		// We need to check for this as well as the child itself
		EStructuralFeature veWrapper_SF = javaChild.eClass().getEStructuralFeature("visual_wrapper");
		if(veWrapper_SF != null){
			IJavaObjectInstance wrapperObject = (IJavaObjectInstance) javaChild.eGet(veWrapper_SF);
			if(wrapperObject != null){
				processParseTree(wrapperObject);
			}
		}
		processParseTree(javaChild);
	}
		
	private void processParseTree(IJavaObjectInstance javaChild){
	
		if (javaChild.getAllocation() != null) {
			if (javaChild.getAllocation() instanceof ParseTreeAllocation) {
				CommandBuilder cbldr = new CommandBuilder(false); // Use a forward undo so that the actual apply to new allocation is last.
				PTExpression expression = ((ParseTreeAllocation) javaChild.getAllocation()).getExpression();
				if (expression instanceof PTClassInstanceCreation) {
					visitClassInstanceCreation((PTClassInstanceCreation) expression, cbldr);
				} else if (expression instanceof PTMethodInvocation) {
					visitMethodInvocation((PTMethodInvocation) expression, cbldr);
				}
				if (!cbldr.isEmpty()) {
					// ReCreate the allocation feature so that CodeGen will reGenerate the constructor
					ParseTreeAllocation newAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
					cbldr.applyAttributeSetting(newAlloc, InstantiationPackage.eINSTANCE.getParseTreeAllocation_Expression(), expression);
					cbldr.applyAttributeSetting(javaChild, JavaInstantiation.getAllocationFeature(javaChild), newAlloc);
					command = cbldr.getCommand();
					command.execute();
				}
			}
		}
	}

	private void visitClassInstanceCreation(PTClassInstanceCreation expression, CommandBuilder cbldr) {
		visitArguments(expression, InstantiationPackage.eINSTANCE.getPTClassInstanceCreation_Arguments(), expression.getArguments(), cbldr);
	}

	private void visitMethodInvocation(PTMethodInvocation expression, CommandBuilder cbldr) {
		visitArguments(expression, InstantiationPackage.eINSTANCE.getPTMethodInvocation_Arguments(), expression.getArguments(), cbldr);
	}

	private void visitArguments(EObject expression, EStructuralFeature feature, List arguments, CommandBuilder cbldr) {
		// Find all references to {parentComposite} and swop them with a pointer to the real composite parent
		ArrayList argsCopy = new ArrayList(arguments.size());
		argsCopy.addAll(arguments); // Use a copy so we don't access and change the same collection in the loop
		for (int i = 0; i < argsCopy.size(); i++) {
			Object argument = argsCopy.get(i);
			if (argument instanceof PTName && SwtPlugin.PARENT_COMPOSITE_TOKEN.equals(((PTName) argument).getName())) { //$NON-NLS-1$
				PTInstanceReference parentRef = InstantiationFactory.eINSTANCE.createPTInstanceReference();
				parentRef.setObject(correctParent);
				cbldr.replaceAttributeSetting(expression, feature, parentRef, i);
			} else if (argument instanceof PTInstanceReference) {
				PTInstanceReference instanceReference = (PTInstanceReference) argument;
				if (instanceReference.getObject() != correctParent) {
					cbldr.applyAttributeSetting(instanceReference, InstantiationPackage.eINSTANCE.getPTInstanceReference_Object(), correctParent);
				}
			}
		}
	}

	protected boolean prepare() {
		return true;
	}

}
