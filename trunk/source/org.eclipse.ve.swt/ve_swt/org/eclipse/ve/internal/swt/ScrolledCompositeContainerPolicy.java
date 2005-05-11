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
 *  $Revision: 1.1 $  $Date: 2005-05-11 21:42:58 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;


public class ScrolledCompositeContainerPolicy extends CompositeContainerPolicy {
	
	public ScrolledCompositeContainerPolicy(EditDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_SCROLLEDCOMPOSITE_CONTENT);
	}
	
	/*
	 * Return true if we have no children. A ScrolledComposite can only have one child.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (!super.isValidChild(child, containmentSF))
			return false;
			
		IJavaObjectInstance scrolledCompositeBean = (IJavaObjectInstance) getContainer();
		IJavaInstance content = (IJavaInstance) scrolledCompositeBean.eGet(sfLayoutData);
		return (content == null);
	}

	public Command getCreateCommand(Object child, Object positionBeforeChild) {
		Command setAsContent;
		EObject parent = (EObject)getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.applyAttributeSetting(parent, sfLayoutData, child, positionBeforeChild);
		setAsContent = cBld.getCommand();
		return super.getCreateCommand(child, positionBeforeChild).chain(setAsContent);
	}

	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		Command removeContent;
		EObject parent = (EObject)getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		cBld.applyAttributeSetting(parent,sfLayoutData,null);
		removeContent = cBld.getCommand();
		return removeContent.chain(super.getDeleteDependentCommand(child, containmentSF));
	}
	
}
