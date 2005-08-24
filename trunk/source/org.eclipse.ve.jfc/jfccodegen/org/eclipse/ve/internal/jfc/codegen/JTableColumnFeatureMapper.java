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
package org.eclipse.ve.internal.jfc.codegen;
/*
 *  $RCSfile: JTableColumnFeatureMapper.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:38:12 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.java.AbstractFeatureMapper;

/**
 * @version 	1.0
 * @author
 */
public class JTableColumnFeatureMapper extends AbstractFeatureMapper {

	/*
	 * @see IJavaFeatureMapper#getFeature(Expression)
	 */
	public EStructuralFeature getFeature (Statement expr) {
	     if (fSF != null) 
	     	return fSF ;
	     if (fRefObj == null || expr==null) 
	     	return null ;
	     getMethodName(expr)  ;
	     fSF = ((EObject)fRefObj).eClass().getEStructuralFeature(JTableDecoder.JTABLE_COLUMNS_FEATURE_NAME) ;
	     fSFname = fMethodName ;
	     return fSF ;		
	}
	
	public String getMethodName(Statement expr) {
		String name = super.getMethodName(expr) ;
		if (name == null) 
		   name = JTableDecoder.JTABLE_ADDCOLUMN_METHOD;
		return name ;
	}

	public String getMethodName() {
		String name = super.getMethodName() ;
		if (name == null) 
		   name = JTableDecoder.JTABLE_ADDCOLUMN_METHOD;
		return name ;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getFeaturePriority(java.lang.String)
	 */
	public int getFeaturePriority(String methodType) {
		if (methodType.equals(JTableDecoder.JTABLE_ADDCOLUMN_METHOD))
		    return IJFCFeatureMapper.PRIORITY_JTABLECOLUMN_ADDCOLUMN; // a bit more than a regular add
		else
			return super.getFeaturePriority(methodType);
	}
}
