/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AllocationFeatureMapper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-05 16:13:50 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.Statement;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class AllocationFeatureMapper extends AbstractFeatureMapper {

	public static final String ALLOCATION_FEATURE = "allocation" ;//$NON-NLS-1$
	public static final String NEW = "new" ;//$NON-NLS-1$
	
		/*
	 * @see IJavaFeatureMapper#getFeature(Expression)
	 */
	public EStructuralFeature getFeature (Statement expr) {
		if (fSF != null) 
			return fSF ;
		if (fRefObj == null || expr==null) 
			return null ;
		getMethodName(expr)  ;
		fSF = ((EObject)fRefObj).eClass().getEStructuralFeature(ALLOCATION_FEATURE) ;
		fSFname = fMethodName ;
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
