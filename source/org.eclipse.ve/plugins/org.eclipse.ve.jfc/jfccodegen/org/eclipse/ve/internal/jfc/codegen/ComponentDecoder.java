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
 *  $RCSfile: ComponentDecoder.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-30 23:19:30 $ 
 */


import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;



public class ComponentDecoder extends JFCObjectDecoder {


/**
 *  A JComponent (may be changing to Component) Decoder will also deal with constraints ...
 *  May need to augment it with other features in the future
 */
public ComponentDecoder (CodeExpressionRef expr, IBeanDeclModel model, IDiagramModelInstance cm, BeanPart part) {
	super (expr,model,cm,part) ;
}

public ComponentDecoder() {
	super () ;
}


protected String getMethod() {
      String method=null ;
	  
	  if (fFeatureMapper!= null)
	    if(fFeatureMapper.getDecorator()!= null) 
	       method = fFeatureMapper.getDecorator().getWriteMethod().getName() ;
	    	    
	  if (method == null)
	    method = CodeGenUtil.getWriteMethod(fExpr) ;	  
	    
	 return method ;
}

/**
 *  This is it for now
 */
protected boolean isConstraint() {	
	
	  String method=getMethod() ;
	  
	    
      if (method == null)
      	  return false;
      else
          return method.equals(IJFCFeatureMapper.CONSTRAINT_BOUND) ||
               method.equals(IJFCFeatureMapper.CONSTRAINT_SIZE)	;
	 
} 

protected boolean isLocation() {	
	
	  String method=getMethod() ;
	  
	    
      if (method == null)
      	  return false;
      else
          return method.equals(IJFCFeatureMapper.LOCATION_NAME) ;
	 
} 


/**
 *
 */
protected void initialFeatureMapper(){
               super.initialFeatureMapper() ;                       
}

/**
 *
 */
protected void initialFeatureMapper(EStructuralFeature sf){  
       super.initialFeatureMapper(sf) ;
}
/**
 *
 */
protected void initialDecoderHelper() {

      if (isConstraint())	
	   fhelper = new ConstraintDecoderHelper(fbeanPart, fExpr,  fFeatureMapper,this) ;
	else if (isLocation())
	   fhelper = new PointDecoderHelper(fbeanPart, fExpr,  fFeatureMapper,this) ;
	else
	   super.initialDecoderHelper() ;
}

/**
 *  Get the first level descendents
 */
public List getChildren(IJavaObjectInstance component) {
	// Vanilla Components has no specific children
      return super.getChildren(component) ; 	
}



}