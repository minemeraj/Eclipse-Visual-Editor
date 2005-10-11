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
 *  $RCSfile: ScrolledCompositeContainerPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-11 21:23:47 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;


public class ScrolledCompositeContainerPolicy extends CompositeContainerPolicy {
	
	protected EStructuralFeature sfContent;
	
	public ScrolledCompositeContainerPolicy(EditDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfContent = JavaInstantiation.getReference(rset, SWTConstants.SF_SCROLLEDCOMPOSITE_CONTENT);
	}
	
	/*
	 * Return true if we have no children. A ScrolledComposite can only have one child.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (!super.isValidChild(child, containmentSF))
			return false;
			
		IJavaObjectInstance scrolledCompositeBean = (IJavaObjectInstance) getContainer();
		IJavaInstance content = (IJavaInstance) scrolledCompositeBean.eGet(sfContent);
		return (content == null);
	}

	protected Command primCreateCommand(Object child, Object positionBeforeChild, EStructuralFeature containmentSF) {
		EObject parent = (EObject)getContainer();
		// We apply to parent AFTER creating child so that at the time the parent receives the content setting the child is
		// a true child of the ScrolledComposite.
		CommandBuilder cBld = new CommandBuilder();
		cBld.append(super.primCreateCommand(child, positionBeforeChild, containmentSF));
		cBld.applyAttributeSetting(parent, sfContent, child);
		return cBld.getCommand();
	}
	
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		EObject parent = (EObject)getContainer();
		// We remove the content setting BEFORE we remove the child so that the parent doesn't have an invalid child as a content setting.
		CommandBuilder cBld = new CommandBuilder();
		cBld.cancelAttributeSetting(parent,sfContent);
		cBld.append(super.getDeleteDependentCommand(child, containmentSF));
		return cBld.getCommand();
	}
	
	protected Command getOrphanTheChildrenCommand(List children) {
		EObject parent = (EObject)getContainer();
		// Need to cancel the content because it won't be the content when the chilren are moved. (Technically there is only one child!).
		CommandBuilder cbld = new CommandBuilder();
		cbld.cancelAttributeSetting(parent, sfContent);
		cbld.append(super.getOrphanTheChildrenCommand(children));
		return cbld.getCommand();
	}
	
}
