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
 *  $RCSfile: CBannerContainerPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-24 18:57:12 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

/**
 * ContainerPolicy for a CBanner.  A CBanner may have three children in a left, right,
 * or bottom location.  The control is created as a child and then assigned to a location
 * by the CBanner.
 * 
 * @since 1.1
 */
public class CBannerContainerPolicy extends CompositeContainerPolicy {
	
	EReference sfLeftControl, sfRightControl, sfBottomControl;
	
	public CBannerContainerPolicy(EditDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLeftControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CBANNER_LEFT);
		sfRightControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CBANNER_RIGHT);
		sfBottomControl = JavaInstantiation.getReference(rset, SWTConstants.SF_CBANNER_BOTTOM);
	}
	
	/*
	 * Return true if we have a free slot. A CBanner can have three displayed children.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (!super.isValidChild(child, containmentSF))
			return false;
			
		IJavaObjectInstance cBannerBean = (IJavaObjectInstance) getContainer();
		IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
		IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		return (left == null || right == null || bottom == null);
	}

	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		IJavaObjectInstance cBannerBean = (IJavaObjectInstance) getContainer();
		IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
		IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		
		Command removeContent;
		EObject parent = (EObject)getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		if(left != null && left.equals(child))
			cBld.applyAttributeSetting(parent,sfLeftControl,null);
		else if(right != null && right.equals(child))
			cBld.applyAttributeSetting(parent,sfRightControl,null);
		else if(bottom != null && bottom.equals(child))
			cBld.applyAttributeSetting(parent,sfBottomControl,null);
		
		removeContent = cBld.getCommand();
		
		if(removeContent != null)
			return removeContent.chain(super.getDeleteDependentCommand(child, containmentSF));
		
		return super.getDeleteDependentCommand(child, containmentSF);
	}

	protected Command getOrphanTheChildrenCommand(List children) {
		Command orphanCmd = super.getOrphanTheChildrenCommand(children);
		if (orphanCmd == null || !orphanCmd.canExecute())
			return UnexecutableCommand.INSTANCE;
		
		Object child = children.get(0);
		
		IJavaObjectInstance cBannerBean = (IJavaObjectInstance) getContainer();
		IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
		IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		
		Command orphanControl;
		EObject parent = (EObject)getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		if(left != null && left.equals(child))
			cBld.cancelAttributeSetting(parent,sfLeftControl);
		else if(right != null && right.equals(child))
			cBld.cancelAttributeSetting(parent,sfRightControl);
		else if(bottom != null && bottom.equals(child))
			cBld.cancelAttributeSetting(parent,sfBottomControl);
		
		orphanControl = cBld.getCommand();
		
		if(orphanControl != null)
			return orphanControl.chain(orphanCmd);
		
		return orphanCmd;
	}
}
