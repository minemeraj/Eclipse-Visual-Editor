package org.eclipse.ve.internal.jfc.codegen;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTabbedPaneFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.ve.internal.java.codegen.java.AbstractFeatureMapper;

/**
 * @version 	1.0
 * @author
 */
public class JTabbedPaneFeatureMapper extends AbstractFeatureMapper {

	/*
	 * @see IJavaFeatureMapper#getFeature(Expression)
	 */
	public EStructuralFeature getFeature (Statement expr) {
	     if (fSF != null) 
	     	return fSF ;
	     if (fRefObj == null || expr==null) 
	     	return null ;
	     getMethodName(expr)  ;
	     fSF = ((EObject)fRefObj).eClass().getEStructuralFeature(JTabbedPaneDecoder.JTABBED_PANE_FEATURE_NAME) ;
	     fSFname = fMethodName ;
	     return fSF ;		
	}
	
	public String getMethodName(Expression expr) {
		String name = super.getMethodName(expr) ;
		if (name == null) 
		   name = JTabbedPaneDecoder.JTABBED_PANE_METHOD ;
		return name ;
	}

	public String getMethodName() {
		String name = super.getMethodName() ;
		if (name == null) 
		   name = JTabbedPaneDecoder.JTABBED_PANE_METHOD;
		return name ;
	}
	public String getIndexMethodName() {
       return JTabbedPaneDecoder.JTABBED_PANE_INDEX_METHOD ;
    }
}


