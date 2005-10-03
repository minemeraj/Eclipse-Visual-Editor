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
 *  $RCSfile: TreeViewerContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-03 19:20:48 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;

import org.eclipse.ve.internal.swt.WidgetContainmentHandler;
 

/**
 * TreeViewer containment handler. This handles dropping a tree viewer onto a Composite or a Tree.
 * In case of a composite it creates a TreeViewer and an implicit tree. In case of a Tree, it
 * creates a TreeViewer with the explicit tree as its tree setting.
 * @since 1.2.0
 */
public class TreeViewerContainmentHandler implements IContainmentHandler {
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws NoAddException {
		// TODO Assume creation for now. We'll see how to handle add later, this may change for that.
		if (creation) {
			if (parent instanceof IJavaObjectInstance) {
				IJavaObjectInstance pjo = (IJavaObjectInstance) parent;
				IJavaObjectInstance cjo = (IJavaObjectInstance) child;	// Assuming it is a treeviewer because this handler is being called.
				// Need to check for tree before composite because tree is an instance of composite.
				ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
				JavaClass treeClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Tree", rset);
				if (treeClass.isInstance(pjo)) {
					// Dropping tree viewer onto a tree.
				} else {
					JavaHelpers compositeClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Composite", rset);
					if (compositeClass.isInstance(pjo)) {
						// Dropping tree viewer onto a composite.
						return dropOnComposite(pjo, cjo, preCmds, treeClass, rset);
					}
				}
			}
		}
		throw new NoAddException("Parent not valid for a TreeViewer");
	}

	/*
	 * Dropping on a composite.
	 */
	private Object dropOnComposite(IJavaObjectInstance parent, IJavaObjectInstance treeViewer, CommandBuilder preCmds, JavaClass treeClass, ResourceSet rset) {
		// Set the allocation to the parent.
		WidgetContainmentHandler.processAllocation(parent, treeViewer, preCmds);
		// Create a tree with implicit allocation to the tree viewer, and set it into the tree feature.
		EStructuralFeature treeFeature = treeViewer.eClass().getEStructuralFeature("tree");
		ImplicitAllocation allocation = InstantiationFactory.eINSTANCE.createImplicitAllocation(treeViewer, treeFeature);
		IJavaInstance tree = BeanUtilities.createJavaObject(treeClass, rset, allocation);
		preCmds.applyAttributeSetting(treeViewer, treeFeature, tree);
		return tree;
	}

}
