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
 *  $RCSfile: ViewFormContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-17 18:10:52 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;


public class ViewFormContainerPolicy extends CompositeContainerPolicy {
	
	EReference sfLeftControl, sfRightControl, sfCenterControl, sfContentControl;
	
	public ViewFormContainerPolicy(EditDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		sfLeftControl = JavaInstantiation.getReference(rset, SWTConstants.SF_VIEWFORM_TOPLEFT);
		sfRightControl = JavaInstantiation.getReference(rset, SWTConstants.SF_VIEWFORM_TOPRIGHT);
		sfCenterControl = JavaInstantiation.getReference(rset, SWTConstants.SF_VIEWFORM_TOPCENTER);
		sfContentControl = JavaInstantiation.getReference(rset, SWTConstants.SF_VIEWFORM_CONTENT);
	}
	
	/*
	 * Return true if we have a free slot. A ViewForm can have four children.
	 */
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (!super.isValidChild(child, containmentSF))
			return false;
			
		IJavaObjectInstance viewFormBean = (IJavaObjectInstance) getContainer();
		IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
		IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
		IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
		return (left == null || right == null || center == null || content == null);
	}

	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		IJavaObjectInstance viewFormBean = (IJavaObjectInstance) getContainer();
		IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
		IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
		IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
		
		Command removeContent;
		EObject parent = (EObject)getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		if(left != null && left.equals(child))
			cBld.applyAttributeSetting(parent,sfLeftControl,null);
		else if(center != null && center.equals(child))
			cBld.applyAttributeSetting(parent,sfCenterControl,null);
		else if(right != null && right.equals(child))
			cBld.applyAttributeSetting(parent,sfRightControl,null);
		else if(content != null && content.equals(child))
			cBld.applyAttributeSetting(parent,sfContentControl,null);
		
		removeContent = cBld.getCommand();
		
		if(removeContent != null)
			return removeContent.chain(super.getDeleteDependentCommand(child, containmentSF));
		
		return super.getDeleteDependentCommand(child, containmentSF);
	}

	public Command getOrphanChildrenCommand(List children) {
		Object child = children.get(0);
		
		IJavaObjectInstance viewFormBean = (IJavaObjectInstance) getContainer();
		IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
		IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
		IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
		
		Command orphanControl;
		EObject parent = (EObject)getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		if(left != null && left.equals(child))
			cBld.applyAttributeSetting(parent,sfLeftControl,null);
		else if(center != null && center.equals(child))
			cBld.applyAttributeSetting(parent,sfCenterControl,null);
		else if(right != null && right.equals(child))
			cBld.applyAttributeSetting(parent,sfRightControl,null);
		else if(content != null && content.equals(child))
			cBld.applyAttributeSetting(parent,sfContentControl,null);
		
		orphanControl = cBld.getCommand();
		
		if(orphanControl != null)
			return orphanControl.chain(super.getOrphanChildrenCommand(children, containmentSF));
		
		return super.getOrphanChildrenCommand(children);
	}
}
