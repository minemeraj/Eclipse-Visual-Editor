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
package org.eclipse.ve.examples.cdm.dept.dinner.ui;
/*
 *  $RCSfile: DinnerContentsContainerPolicy.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-06 23:38:35 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.DiagramFigure;
/**
 * Container EditPolicy for dinner.
 */
public class DinnerContentsContainerPolicy extends AbstractEMFContainerPolicy {
	
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
