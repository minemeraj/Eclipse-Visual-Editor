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
 *  $RCSfile: CompositeFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-28 00:47:08 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.ve.internal.java.codegen.java.AbstractFeatureMapper;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class CompositeFeatureMapper extends AbstractFeatureMapper {

	/*
	 * @see IJavaFeatureMapper#getFeature(Expression)
	 */
	public EStructuralFeature getFeature (Statement expr) {
		if (fSF != null) 
			return fSF ;
		if (fRefObj == null || expr==null) 
			return null ;
		getMethodName(expr)  ;
		fSF = ((EObject)fRefObj).eClass().getEStructuralFeature(CompositeDecoder.ADD_METHOD_SF_NAME) ;
		fSFname = fMethodName ;
		return fSF ;		
	}
	
	public String getMethodName(Expression expr) {
		String name = super.getMethodName(expr) ;
		if (name == null) 
			name = "????????" ;
		return name ;
	}

	public String getMethodName() {
		String name = super.getMethodName() ;
		if (name == null) 
			name = CompositeDecoder.ADD_METHOD_PREFIX;
		return name ;
	}
	public String getIndexMethodName() {
		return CompositeDecoder.ADD_METHOD_PREFIX ;
	}

}
