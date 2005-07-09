/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: ComponentDecoder.java,v $
 *  $Revision: 1.9 $  $Date: 2005-07-09 00:02:21 $ 
 */


import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.*;



public class ComponentDecoder extends JFCObjectDecoder {


/**
 *  A JComponent (may be changing to Component) Decoder will also deal with constraints ...
 *  May need to augment it with other features in the future
 */
public ComponentDecoder (CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
	super (expr,model,cm,part) ;
}

public ComponentDecoder() {
	super () ;
}


protected String getMethod() {
      String method=null ;
	  
	  if (fFeatureMapper!= null)
	    if(fFeatureMapper.getDecorator()!= null && fFeatureMapper.getDecorator().getWriteMethod()!=null) 
	       method = fFeatureMapper.getDecorator().getWriteMethod().getName() ;
	    	    
	  if (method == null)
	    method = AbstractFeatureMapper.getWriteMethod(fExpr) ;	  
	    
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
 *  Is this a component add expression
 */
protected boolean isMethod(String mSig, EStructuralFeature sf) {	

	String method=null ;
	
	if (fFeatureMapper!= null)
		if(fFeatureMapper.getDecorator()!= null &&
				fFeatureMapper.getDecorator().getWriteMethod()!=null) 
			method = fFeatureMapper.getDecorator().getWriteMethod().getName() ;
		else 
			if (fFeatureMapper.getFeature(null).equals(sf))
				return true ;
			
	if (method == null)
		method = AbstractFeatureMapper.getWriteMethod(fExpr) ;	  
	return method != null && method.equals(mSig) ;	
} 

protected boolean isJFCAtrribute() {
	for (int i=0;i<AttributeFeatureMapper.hardCodeMethods.length; i++) {
		String method = AttributeFeatureMapper.hardCodeMethods[i];
		EStructuralFeature sf = JavaInstantiation.getSFeature((IJavaObjectInstance)fbeanPart.getEObject(),AttributeFeatureMapper.hardCodedURI[i]);
		if (isMethod(method,sf)) return true;		
	}
	return false;
}

/**
 *
 */
protected void initialFeatureMapper(){
		if (isJFCAtrribute()) 
			fFeatureMapper = new AttributeFeatureMapper();		
        else
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
	   fhelper = new AggregateDecoderHelper(fbeanPart, fExpr,  fFeatureMapper,this, new Class[]{ConstraintDecoderHelper.class, ChildRelationshipDecoderHelper.class}) ;
	else if (isLocation())
	   fhelper = new AggregateDecoderHelper(fbeanPart, fExpr,  fFeatureMapper,this, new Class[]{PointDecoderHelper.class, ChildRelationshipDecoderHelper.class}) ;
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
