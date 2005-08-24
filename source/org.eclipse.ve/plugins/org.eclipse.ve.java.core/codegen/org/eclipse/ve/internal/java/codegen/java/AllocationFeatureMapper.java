/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AllocationFeatureMapper.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class AllocationFeatureMapper extends AbstractFeatureMapper {

	public static final String ALLOCATION_FEATURE = "allocation" ;//$NON-NLS-1$
	public static final String NEW = "new" ;//$NON-NLS-1$
	
	public AllocationFeatureMapper(EObject refObject) {
		fRefObj=(IJavaInstance)refObject;
	}
		/*
	 * @see IJavaFeatureMapper#getFeature(Expression)
	 */
	public EStructuralFeature getFeature (Statement expr) {
        // KLUDGE: For now we need to get the allocation feature each time
        // because if the class goes undefined the allocation feature will be 
        // a physically different feature than when it was defined. (And visa-versa).
		EStructuralFeature sf = ((EObject)fRefObj).eClass().getEStructuralFeature(ALLOCATION_FEATURE) ;
        if (sf != fSF) {
            fSF = sf;
            fSFname = fSF.getName() ;
        }
		return fSF ;		
	}
	
	public String getMethodName(Expression expr) {
		return getMethodName();
	}

	public String getMethodName() {
		return NEW ;
	}
	public String getIndexMethodName() {
		return NEW;
	}


}
