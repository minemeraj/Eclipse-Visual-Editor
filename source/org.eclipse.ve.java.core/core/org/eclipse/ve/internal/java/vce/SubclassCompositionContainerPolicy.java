/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.5 $  $Date: 2005-06-24 18:57:14 $ 
 */
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

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
	
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		if (child == getComposition().getThisPart())
			return UnexecutableCommand.INSTANCE;	// Can't delete the this part.
		// The superclass will create a command that will delete the annotation
		return super.getDeleteDependentCommand(child,containmentSF);	
	}
	
	public Command getMoveChildrenCommand(List children, Object position) {
		Object thisPart = getComposition().getThisPart();
		if (thisPart != null) {
			if (children.contains(thisPart))
				return UnexecutableCommand.INSTANCE;	// Can't move the this part.
		}
		return super.getMoveChildrenCommand(children, position);
	}
	
	protected Command getOrphanTheChildrenCommand(List children) {	
		Object thisPart = getComposition().getThisPart();
		if (thisPart != null) {
			if (children.contains(thisPart))
				return UnexecutableCommand.INSTANCE;	// Can't orphan the this part.
		}
		return super.getOrphanTheChildrenCommand(children);
	}
}
