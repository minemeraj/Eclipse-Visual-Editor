/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $RCSfile: EntreeContainerPolicy.java,v $
 *  $Revision: 1.5 $  $Date: 2006-05-17 20:14:42 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.cdm.DiagramFigure;
/**
 * Container Policy for Entree.
 */
public class EntreeContainerPolicy extends AbstractEMFContainerPolicy {

	public EntreeContainerPolicy(EditDomain domain) {
		super(CDMPackage.eINSTANCE.getDiagramFigure_ChildFigures(), domain);
	}

	protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
		if (child instanceof DiagramFigure) {
			DiagramFigure childFigure = (DiagramFigure) child;
			if (DinnerConstants.EMPLOYEE_CHILD_TYPE.equals(childFigure.getType()))
				return true;
		}
		return false;
	}

}
