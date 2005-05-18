/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComponentCopyEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-18 16:36:07 $ 
 */

package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.DefaultCopyEditPolicy;

public class ComponentCopyEditPolicy extends DefaultCopyEditPolicy {

	private Copier copier;

	protected void cleanup(IJavaInstance javaBeanToCopy, Copier aCopier) {

		super.cleanup(javaBeanToCopy, copier);
		copier = aCopier;
		// Get the ConstraintComponent object that points to us and strip our constraint
		EObject constraintComponent = InverseMaintenanceAdapter.getFirstReferencedBy(
				javaBeanToCopy,JavaInstantiation.getReference(
						javaBeanToCopy.eResource().getResourceSet(),JFCConstants.SF_CONSTRAINT_COMPONENT));		
		if(constraintComponent != null){
			removeReferenceTo(constraintComponent,"constraint",aCopier); //$NON-NLS-1$
		}
		// Strip bounds, size and location
		removeReferenceTo(javaBeanToCopy,"bounds",aCopier); //$NON-NLS-1$
		removeReferenceTo(javaBeanToCopy,"size",aCopier); //$NON-NLS-1$
		removeReferenceTo(javaBeanToCopy,"location",aCopier);		 //$NON-NLS-1$
	}
	
}
