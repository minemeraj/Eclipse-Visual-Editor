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
 *  $RCSfile: ConstraintDecoderHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 23:13:34 $ 
 */


import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.ve.internal.jcm.MemberContainer;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;


public class ConstraintDecoderHelper extends ExpressionDecoderHelper {
	
	public final static String DIMENSION_CLASS = "java.awt.Dimension" ; //$NON-NLS-1$
	public final static String RECTANGLE_CLASS = "java.awt.Rectangle" ; //$NON-NLS-1$
	int  fConstraints[]=null ;		

public ConstraintDecoderHelper(BeanPart bean, Statement exp,  IJavaFeatureMapper fm, IExpressionDecoder owner){
	super (bean,exp,fm,owner) ;
}

/**
 * Add a Rectangle constraint on a target object - for none free formed objects
 * Constraints should hold x, y, width, and height, in that order.
 */
public static void addRactangleFeatureValue (MemberContainer pOwner, EObject target,int args[], EStructuralFeature sf, IDiagramModelInstance cm) throws Exception {
  if (args.length != 4) throw new RuntimeException ("Invalid Args") ;  //$NON-NLS-1$
  
  IJavaObjectInstance value = (IJavaObjectInstance) CodeGenUtil.createInstance(RECTANGLE_CLASS,cm) ;	 //$NON-NLS-1$
  value.setInitializationString("new "+RECTANGLE_CLASS+"("+ //$NON-NLS-1$ //$NON-NLS-2$
                                        Integer.toString(args[0]) +   "," + Integer.toString(args[1]) + "," + //$NON-NLS-1$ //$NON-NLS-2$
                                        Integer.toString(args[2])+"," + Integer.toString(args[3])+")") ;                                 //$NON-NLS-1$ //$NON-NLS-2$
  CodeGenUtil.propertyCleanup(target,sf) ;                                        
  // Set the scope                                
  pOwner.getProperties().add(value) ;
  // Set the property                                        
  target.eSet(sf,value);    
}

/**
 * Add a Dimension constraint on a target object - for free form objects
 * Constraints should hold with and height, in that order.
 */
public static void addDimensionFeatureValue (MemberContainer pOwner, EObject target,int args[], EStructuralFeature sf, IDiagramModelInstance cm) throws Exception {
  if (args.length != 2) throw new RuntimeException ("Invalid args") ; //$NON-NLS-1$
  
  IJavaObjectInstance value = (IJavaObjectInstance) CodeGenUtil.createInstance(DIMENSION_CLASS,cm) ;	 //$NON-NLS-1$
  value.setInitializationString("new "+DIMENSION_CLASS+"("+Integer.toString(args[0])+ //$NON-NLS-1$ //$NON-NLS-2$
                                ","+Integer.toString(args[1])+")") ;                                 //$NON-NLS-1$ //$NON-NLS-2$
                                
  CodeGenUtil.propertyCleanup(target,sf) ;
  // Set the scope                                
  pOwner.getProperties().add(value) ;
  // Set the property
  target.eSet(sf,value);    
}

/**
 *  Add a new constraint
 */
protected boolean	addConstraintFeature() throws CodeGenException {
	if (fbeanPart.getEObject() == null || fFmapper.getMethodName() == null) throw new CodeGenException ("null EObject:"+fExpr) ;       //$NON-NLS-1$
		
	try {
        if (fFmapper.getMethodName().equals(IJFCFeatureMapper.CONSTRAINT_BOUND)) {
     	    // TODO  Need to deal with Rectangle arg
    	    if (((MessageSend)fExpr).arguments.length == 4)   {
   		   // Create a dimension from the width and height of the rectangle
   		   fConstraints = new int[4] ;
   		   for (int i=0; i<4; i++) 
   		     fConstraints[i] = Integer.parseInt(((MessageSend)fExpr).arguments[i].toString()) ;
               addRactangleFeatureValue(fbeanPart.getInitMethod().getCompMethod(),
   		                       fbeanPart.getEObject(),
   		                       fConstraints,
   		                       fFmapper.getFeature(null),   		                       
   		                       fOwner.getCompositionModel()) ;
    	    }
          else {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Format: bound, number of arguments is not 4",false) ; //$NON-NLS-1$
           	  return false ;
          }   		                 
        }
        else
        	if (fFmapper.getMethodName().equals(IJFCFeatureMapper.CONSTRAINT_SIZE)) {
		       	// TODO  Need to deal with Dimension arg
		       	if (((MessageSend)fExpr).arguments.length == 2)  {
		       	   fConstraints = new int[2] ;
		   		   for (int i=0; i<2; i++) 
		   		     fConstraints[i] = Integer.parseInt(((MessageSend)fExpr).arguments[i].toString()) ;
		       	   
		       	   // Create a dimension from the width and height of the rectangle    
		       	  addDimensionFeatureValue(fbeanPart.getInitMethod().getCompMethod(),  	
		       	                       fbeanPart.getEObject(),
		       	                       fConstraints,
                                       fFmapper.getFeature(null),
		   		                       fOwner.getCompositionModel()) ;
		       	}
		   		else {
					CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Format: size, number of arguments is not 2",false) ; //$NON-NLS-1$
		           	return false ;
		   		}
          }
          else 
          		// Check if only string is inside. We assume that this decoder helper 
          		// _really_ trusts the expression it has been given to handle.
			if((((MessageSend)fExpr).arguments.length == 1) && 
			   (((MessageSend)fExpr).arguments[1] instanceof StringLiteral)){
				// String is the constraint parameter 
		            String constraint = ((MessageSend)fExpr).arguments[1].toString() ;
		            // Add the constraint to the added part
		           	CodeGenUtil.addConstraintString(fbeanPart.getInitMethod().getCompMethod(),
		           	                                fbeanPart.getEObject(), constraint,
                                                    fFmapper.getFeature(null),                                                        
		           	                                fOwner.getCompositionModel()) ;
          		}
          		else return false ;
	}
	catch (Exception e) {
		throw new CodeGenException(e) ;
	}
	return true ;   
}


/**
 *  Get the args part of the expression
 */
protected String getArgs(String exp) {
  int[] indexes = getArgsIndexes(exp) ;
  if (indexes == null) return null ;
  return exp.substring(indexes[0],indexes[1]+1) ;
}
/**
 *  Get the args part of the expression
 */
protected int[] getArgsIndexes(String exp) {
	
  int left  = exp.indexOf(Signature.C_PARAM_START) ;
  int right = exp.indexOf(Signature.C_PARAM_END) ;
  
  if (left < 0 || right < 0) return null ;  	
  left ++ ; right -- ;
  if (left>right) return null ;
  int[] indexes = new int[2] ;
  indexes[0] = left ;
  indexes[1] = right ;
  return indexes ;
}

/**
 *  Get the dimension of the constraint from the arguments
 */
protected int[] parseArgs(String exp) {
	
	if (exp == null || getArgs(exp)==null) return null ;		
	StringTokenizer st = new StringTokenizer(getArgs(exp),",") ; //$NON-NLS-1$
	int args[] = new int[st.countTokens()] ;
	for (int i=0; i<args.length; i++) {
		args[i] = Integer.parseInt(st.nextToken().trim()) ;
	}
	return args ;
}

/**
 *  Compare the arrays
 */
protected boolean sameConstraints(int[] A, int[] B) {
	if (A.length != B.length) return false ;
	for (int i=0; i<A.length; i++)
	   if (A[i] != B[i]) return false ;
	return true ;
}


/**
 *  Get the current constraints in the Composition Model
 */
protected int[]   getCompositionConstraints() throws CodeGenException {

  IJavaObjectInstance curValue = (IJavaObjectInstance)fbeanPart.
                                 getEObject().eGet(fFmapper.getFeature(fExpr)) ;  
  return parseArgs(curValue.getInitializationString()) ;	
} 
/**
 *  Create initialization arguments
 */
protected String constraintsToString(int[] constraints) {
	StringBuffer sb = new StringBuffer() ;
      for (int i=0; i<constraints.length; i++) {
     	  if (i>0) sb.append(", ") ; //$NON-NLS-1$
     	  sb.append(Integer.toString(constraints[i])) ;
      }
      return sb.toString() ; 
}

public String primRefreshFromComposition(String expSig) throws CodeGenException {
	  
  int [] curConstraints = getCompositionConstraints() ;
  if (fConstraints != null)
    if (sameConstraints(curConstraints,fConstraints)) {
  	fExprSig = expSig ; // spacing and such may have changed.
    }
    else {
      if (curConstraints.length != fConstraints.length) {
      	fExprSig = generate(null) ;
      	// Remove the generated comments etc.
      	fExprSig = fExprSig.substring(0,fExprSig.indexOf(")")+1) ; //$NON-NLS-1$
      }      
      else {  	     
        int[] curArgsIndexes = getArgsIndexes(expSig) ;
        String newArgs = constraintsToString(curConstraints) ;     
        fConstraints = curConstraints ;
        // Replace the arguments part with the current constraints
        fExprSig = expSig.substring(0,curArgsIndexes[0]) +
                newArgs +
                expSig.substring(curArgsIndexes[1]+1,expSig.length()) ;
      }
    }
  
     	
   return fExprSig ;	
}

public boolean primIsDeleted() {
  // If a constraint was changed to non-free form, remove the code
  return (fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr)) == null);
}

/**
 *   Go for it
 */
public boolean decode() throws CodeGenException {

	// TODO  Need to validate that the constraint is valid for a given layout
      return addConstraintFeature() ;
}

public void delete() {
	unadaptToCompositionModel() ;
	
	org.eclipse.emf.ecore.EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
	IJavaObjectInstance parent = (IJavaObjectInstance)fbeanPart.getEObject() ;
	EObject setting = (EObject) parent.eGet(sf) ;
	if (setting != null) {
	   cleanProperty(setting) ;
	}
	parent.eUnset(sf) ; 
}


/**
 *  Overide the default to deal with the lack of Decorator
 */
protected ExpressionTemplate getExpressionTemplate(String[] args) throws CodeGenException {
	
	if (fFmapper.getDecorator() != null) 
	   return super.getExpressionTemplate(args) ;
	   
      String mtd ;
      if (fConstraints.length == 2)
        mtd = IJFCFeatureMapper.CONSTRAINT_SIZE ;
      else if(fConstraints.length == 4)
        mtd = IJFCFeatureMapper.CONSTRAINT_BOUND ;
      else
        throw new CodeGenException("Can not resolve write method") ;                 //$NON-NLS-1$
	String sel = fbeanPart.getSimpleName() ;
	ExpressionTemplate exp = new ExpressionTemplate (sel,mtd, 
	                                                 args,
	                                                 null,
	                                                 0 ) ;
	exp.setLineSeperator(fbeanPart.getModel().getLineSeperator()) ;
	return exp ;   	
}

/**
 *  Generate from scratch
 */
public String generate(Object[] noArgs) throws CodeGenException {
	
	if (fFmapper.getFeature(null) == null) 
	    throw new CodeGenException ("null Feature") ; //$NON-NLS-1$
	try {
	   fConstraints = getCompositionConstraints() ;
	}
	catch (CodeGenException e) {
		return null ; 
	}
		
	ExpressionTemplate exp = getExpressionTemplate(new String[] {constraintsToString(fConstraints)}) ;
      fExprSig = exp.toString() ;	                                                 
      return fExprSig ;	     	
}

/**
 * Determine if the current OCM model holds a bound/size attribute constraint
 */
//public static boolean  primIsFreeFormConstraint(EObject obj) {
//
//  IJavaObjectInstance curValue = (IJavaObjectInstance) obj.eGet(CodeGenUtil.getConstraintFeature(obj)) ;  
//  if (curValue == null) return false ;
//  
//  String typeName = curValue.getJavaType().getQualifiedName() ;
//  
//  if (typeName.equals(IJavaFeatureMapper.CONSTRAINT_BOUND_CLASS) ||
//      typeName.equals(IJavaFeatureMapper.CONSTRAINT_SIZE_CLASS))
//      return true ;
//  
//  org.eclipse.ve.internal.cde.core.CDEHack.fixMe("Need to address containers");
//  return false ;
//  
//}

public boolean isImplicit(Object args[]) {
   return false ;		
}

public Object[] getArgsHandles(Statement expr) {
   // No unique arguments ;
   return null ;
}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#adaptToCompositionModel(IExpressionDecoder)
	 */
	public void adaptToCompositionModel(IExpressionDecoder decoder) {
		super.adaptToCompositionModel(decoder);
        IJavaInstance o = (IJavaInstance) fbeanPart.getEObject().eGet(fFmapper.getFeature(null)) ;
        // Add a source range adapter the bound/size constraint
        if (o != null) {
        	o.eAdapters().add(new ExpressionDecoderAdapter(decoder).getShadowSourceRangeAdapter()) ;	
        }        
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#unadaptToCompositionModel()
	 */
	public void unadaptToCompositionModel() {
		super.unadaptToCompositionModel();
		IJavaInstance o = (IJavaInstance) fbeanPart.getEObject().eGet(fFmapper.getFeature(null)) ;
		if (o != null) {
	        ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(o,ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE) ;
	        if (a != null)
	          o.eAdapters().remove(a) ;
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getSFPriority()
	 */
	protected int getSFPriority() {
		return IJavaFeatureMapper.PRIORITY_CONSTRAINT_CHANGE + super.getSFPriority();
	}

}


