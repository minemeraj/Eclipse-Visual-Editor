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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;

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
		processParseTree(javaChild);
	}

	private void processParseTree(IJavaInstance javaChild) {

		if (javaChild.isParseTreeAllocation()) {
			final CommandBuilder cbldr = new CommandBuilder(false); // Use a forward undo so that the actual apply to new allocation is last.
			PTExpression expression = ((ParseTreeAllocation) javaChild.getAllocation()).getExpression();
			// Create a visitor to find all references to parent composite flag and convert to parent reference.
			ParseVisitor visitor = new ParseVisitor() {

				public boolean visit(PTName node) {
					if (SwtPlugin.PARENT_COMPOSITE_TOKEN.equals(node.getName())) {
						PTInstanceReference parentRef = InstantiationFactory.eINSTANCE.createPTInstanceReference();
						parentRef.setReference(correctParent);
						EObject container = node.eContainer();
						EStructuralFeature containingFeature = node.eContainingFeature();
						if (containingFeature.isMany())
							cbldr.replaceAttributeSetting(container, containingFeature, parentRef, ((List) container.eGet(containingFeature))
									.indexOf(node));
						else
							cbldr.applyAttributeSetting(container, containingFeature, parentRef);
					}
					return true;
				}

			};
			expression.accept(visitor);

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

	protected boolean prepare() {
		return true;
	}

}
