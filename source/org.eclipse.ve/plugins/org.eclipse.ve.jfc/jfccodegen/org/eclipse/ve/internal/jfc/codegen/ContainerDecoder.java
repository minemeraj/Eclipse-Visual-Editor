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
 *  $RCSfile: ContainerDecoder.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;


public class ContainerDecoder extends ComponentDecoder {
	
protected final static  String ADD_METHOD = "add" ;	 //$NON-NLS-1$
protected final static  String DEFAULT_CONSTRAINT = ".getName()" ; //$NON-NLS-1$
protected final static  String LAYOUT_METHOD = "setLayout" ; //$NON-NLS-1$
protected final static  String LAYOUT_FEATURE = "layout" ; //$NON-NLS-1$

/**
 *  A JPanel (maybe changing to Container ) Decoder will also add  ...
 *  May need to augment it with other features in the future
 */

public ContainerDecoder (CodeExpressionRef expr, IBeanDeclModel model, IVEModelInstance cm, BeanPart part) {
	super (expr,model,cm,part) ;
}

public ContainerDecoder() {	
	super () ;
}


/**
 *  Is this a component add expression
 */
protected boolean isMethod(String mSig) {	

	  return isMethod(mSig,CodeGenUtil.getComponentFeature(fbeanPart.getEObject())) ;
} 


/**
 *  Which FeatureMapper to use
 */
protected void initialFeatureMapper(){
	
	  if (isMethod(ADD_METHOD)) 
	     fFeatureMapper = new ContainerFeatureMapper() ;
	  else
	     super.initialFeatureMapper() ;
               
        
}

/**
 *
 */
protected void initialFeatureMapper(EStructuralFeature sf){
   if (sf.equals(CodeGenUtil.getComponentFeature(fbeanPart.getEObject()))) {
       fFeatureMapper = new ContainerFeatureMapper() ;
       fFeatureMapper.setFeature(sf) ;
   }
   else
       super.initialFeatureMapper(sf) ;
}

/**
 * @see org.eclipse.ve.internal.java.codegen.java.AbstractExpressionDecoder#isPriorityCacheable()
 */
protected boolean isPriorityCacheable(){
	if(isMethod(ADD_METHOD))
		return false;
	return super.isPriorityCacheable();
}

protected void initialDecoderHelper() {
	if (isMethod(ADD_METHOD)) 
	    fhelper = new ContainerAddDecoderHelper(fbeanPart, fExpr,  fFeatureMapper,this) ;
	else if (isMethod(LAYOUT_METHOD))
	    fhelper = new JFCChildRelationshipDecoderHelper(fbeanPart, fExpr,  fFeatureMapper, this);
	else 	      	
	    super.initialDecoderHelper() ;
}

/**
 *  Get the first level descendents
 */
public List getChildren(IJavaObjectInstance component) {
  List kids = super.getChildren(component) ; 
  java.util.List compList = CodeGenUtil.getChildrenComponents(component) ;
  if (compList != null && compList.size()>0) {    	
	  Iterator itr = compList.iterator() ;
	   while (itr.hasNext()) {    	   
		EObject child = CodeGenUtil.getCCcomponent((EObject)itr.next()) ;
		kids.add(child) ;
		kids.add(CodeGenUtil.getComponentFeature(component)) ;
	   }
  }

  // Consider the Layout Manager as a child as it could have attributes with it.  
  EStructuralFeature sf = component.eClass().getEStructuralFeature(LAYOUT_FEATURE) ;
  if (sf != null) {
    IJavaObjectInstance child = (IJavaObjectInstance)component.eGet(sf) ;
    if (child != null) {
	  kids.add(child) ;
	  kids.add(sf) ;
    }
  }
	
 return kids ; 
}



}
