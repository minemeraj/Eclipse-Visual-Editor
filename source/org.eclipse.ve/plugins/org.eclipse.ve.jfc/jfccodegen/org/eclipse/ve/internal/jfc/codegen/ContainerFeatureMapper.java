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
 *  $RCSfile: ContainerFeatureMapper.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:13 $ 
 */


import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.java.AbstractFeatureMapper;


public class ContainerFeatureMapper extends AbstractFeatureMapper implements IJFCFeatureMapper  {

public EStructuralFeature getFeature (Statement expr) {
	
	
     if (fSF != null) return fSF ;
     
     if (fRefObj == null || expr==null) return null ;
     	    
     getMethodName(expr)  ;
              
     fSF = ((EObject)fRefObj).eClass().getEStructuralFeature(COMPONENT_FEATURE_NAME) ;  
     fSFname = fMethodName ;
                	  
     return fSF ;		
}

protected String getMethodName(Statement expr) {
	String name = super.getMethodName(expr) ;
	if (name == null) 
	   name = ContainerDecoder.ADD_METHOD  ;
	return name ;
}

public String getMethodName() {
	String name = super.getMethodName() ;
	if (name == null) 
	   name = ContainerDecoder.ADD_METHOD  ;
	return name ;
}

}


