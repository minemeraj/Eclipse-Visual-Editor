/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.vce;
/*
 *  $RCSfile: SubclassCompositionContainerPolicy.java,v $
 *  $Revision: 1.9 $  $Date: 2005-12-01 20:19:39 $ 
 */
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.StopRequestException;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.CompositionContainerPolicy;
/**
 * Container Policy for Bean Subclass Compositions.
 */
public class SubclassCompositionContainerPolicy extends CompositionContainerPolicy {
	
	public SubclassCompositionContainerPolicy(EditDomain domain) {
		super(JCMPackage.eINSTANCE.getBeanComposition_Components(), domain);
	}
	
	protected BeanSubclassComposition getComposition() {
		return (BeanSubclassComposition) container;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy#getTrueChild(java.lang.Object, int, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder)
	 */
	public Object getTrueChild(Object child, int reqType, CommandBuilder preCmds, CommandBuilder postCmds) throws StopRequestException {
		child = super.getTrueChild(child, reqType, preCmds, postCmds);
		if (child == getComposition().getThisPart())
			throw new StopRequestException("The this part is not a valid child to be added/removed.");
		return child;
	}
	
	public Command getMoveChildrenCommand(List children, Object position) {
		Object thisPart = getComposition().getThisPart();
		if (thisPart != null) {
			if (children.contains(thisPart))
				return UnexecutableCommand.INSTANCE;	// Can't move the this part.
		}
		return super.getMoveChildrenCommand(children, position);
	}
		
	protected boolean isValidBeanLocation(Object child) {
		return child instanceof EObject && BeanUtilities.isValidBeanLocation(domain, (EObject)child);
	}
}
