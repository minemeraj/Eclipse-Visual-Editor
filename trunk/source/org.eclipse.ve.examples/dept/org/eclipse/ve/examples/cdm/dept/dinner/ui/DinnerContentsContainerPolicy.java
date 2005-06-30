package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DinnerContentsContainerPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-16 00:30:22 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFContainerPolicy;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.DiagramFigure;
/**
 * Container EditPolicy for dinner.
 */
public class DinnerContentsContainerPolicy extends EMFContainerPolicy {
	
	public DinnerContentsContainerPolicy(EditDomain domain) {
		super(CDMPackage.eINSTANCE.getDiagram_Figures(), domain);
	}
	
	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (child instanceof DiagramFigure) {
			DiagramFigure childFigure = (DiagramFigure) child;
			if (DinnerConstants.ENTREE_CHILD_TYPE.equals(childFigure.getType()))
				return true;
		}
		return false;
	}

}