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
 *  $RCSfile: EnsureOrphanFromParentCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2005-11-04 17:30:52 $ 
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
 * Ensure that the child argument has the parent orphaned correctly for its ParseTreeAllocation. This happens when a part
 * is moved between two parents. This makes sure that the parent references are changed into {@link SwtPlugin#PARENT_COMPOSITE_TOKEN}.
 * 
 * @since 1.1.0.1
 */
public class EnsureOrphanFromParentCommand extends CommandWrapper {

	private IJavaObjectInstance javaChild;

	private IJavaObjectInstance oldParent;

	public EnsureOrphanFromParentCommand(IJavaObjectInstance aChild, IJavaObjectInstance oldParent) {
		javaChild = aChild;
		this.oldParent = oldParent;
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

				public boolean visit(PTInstanceReference node) {
					if (node.getReference() == oldParent) {
						PTName parentRef = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.PARENT_COMPOSITE_TOKEN);
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
