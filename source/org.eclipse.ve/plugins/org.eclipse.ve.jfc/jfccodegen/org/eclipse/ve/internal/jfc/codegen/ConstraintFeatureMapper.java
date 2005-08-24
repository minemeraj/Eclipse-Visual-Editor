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
 *  $RCSfile: ConstraintFeatureMapper.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:12 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.java.AbstractFeatureMapper;

/**
 *  For compnents , e.g., add(foo) 
 */
public class ConstraintFeatureMapper extends AbstractFeatureMapper {

public EStructuralFeature getFeature (Statement expr) {
	
	
     if (fSF != null) return fSF ;
     
     if (fRefObj == null || expr==null) return null ;
     	    
     getMethodName(expr)  ;
     StringBuffer  sb = new StringBuffer() ;
     
         
     fSF =((EObject)fRefObj).eClass().getEStructuralFeature(IJFCFeatureMapper.CONSTRAINT_FEATURE_NAME) ;  
     sb.append(Character.toLowerCase(fMethodName.charAt(3))) ;
     for (int i = 4;  i< fMethodName.length(); i++) {
    	    sb.append(fMethodName.charAt(i)) ;
     }
     fSFname = sb.toString() ;
                	
  
     return fSF ;		
}


}


