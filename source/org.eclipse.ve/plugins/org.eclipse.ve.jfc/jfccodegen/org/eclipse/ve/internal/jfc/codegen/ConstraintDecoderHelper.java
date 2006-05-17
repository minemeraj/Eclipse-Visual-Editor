/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
 *  $RCSfile: ConstraintDecoderHelper.java,v $
 *  $Revision: 1.22 $  $Date: 2006-05-17 20:14:59 $ 
 */


import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.jcm.MemberContainer;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import com.ibm.icu.util.StringTokenizer;


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
public static void addRectangleFeatureValue (MemberContainer pOwner, EObject target,int args[], EStructuralFeature sf, IVEModelInstance cm) throws Exception {
  if (args.length != 4) throw new RuntimeException ("Invalid Args") ;  //$NON-NLS-1$
  
  IJavaInstance value = CodeGenUtil.createInstance(RECTANGLE_CLASS,cm) ;	 //$NON-NLS-1$
  value.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation("new "+RECTANGLE_CLASS+"("+ //$NON-NLS-1$ //$NON-NLS-2$
                                        Integer.toString(args[0]) +   "," + Integer.toString(args[1]) + "," + //$NON-NLS-1$ //$NON-NLS-2$
                                        Integer.toString(args[2])+"," + Integer.toString(args[3])+")"));	//$NON-NLS-1$ //$NON-NLS-2$
  EObject oldRect = (EObject) target.eGet(sf);                                        
  // Set the scope                                
  pOwner.getProperties().add(value) ;
  // Set the property                                        
  target.eSet(sf,value);
  CodeGenUtil.propertyCleanup(oldRect);
}

/**
 * Add a Dimension constraint on a target object - for free form objects
 * Constraints should hold with and height, in that order.
 */
public static void addDimensionFeatureValue (MemberContainer pOwner, EObject target,int args[], EStructuralFeature sf, IVEModelInstance cm) throws Exception {
  if (args.length != 2) throw new RuntimeException ("Invalid args") ; //$NON-NLS-1$
  
  IJavaInstance value = CodeGenUtil.createInstance(DIMENSION_CLASS,cm) ;	 //$NON-NLS-1$
  value.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation("new "+DIMENSION_CLASS+"("+Integer.toString(args[0])+ //$NON-NLS-1$ //$NON-NLS-2$
                                ","+Integer.toString(args[1])+")"));	//$NON-NLS-1$ //$NON-NLS-2$
                                
  EObject oldDim = (EObject) target.eGet(sf);
  // Set the scope                                
  pOwner.getProperties().add(value) ;
  // Set the property
  target.eSet(sf,value);
  CodeGenUtil.propertyCleanup(oldDim);
}

/**
 *  Add a new constraint
 */
protected boolean	addConstraintFeature(boolean addToEMFmodel) throws CodeGenException {
	if (fbeanPart.getEObject() == null || fFmapper.getMethodName() == null)
			throw new CodeGenException("null EObject:" + fExpr); //$NON-NLS-1$

		MethodInvocation exp = (MethodInvocation) getExpression();
		try {
			// If we restore... go ahead, and update fConstraints
			if (addToEMFmodel && !shouldCommit(exp))
				return true;
			if (fFmapper.getMethodName().equals(IJFCFeatureMapper.CONSTRAINT_BOUND)) {
				// TODO Need to deal with Rectangle arg
				if (exp.arguments().size() == 4) {
					// Create a dimension from the width and height of the rectangle
					fConstraints = new int[4];
					for (int i = 0; i < 4; i++)
						fConstraints[i] = Integer.parseInt(exp.arguments().get(i).toString());
					if (addToEMFmodel)
						addRectangleFeatureValue(fbeanPart.getInitMethod().getCompMethod(), fbeanPart.getEObject(), fConstraints, fFmapper
								.getFeature(null), fOwner.getCompositionModel());
				}
				else {
					CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(),
							"Invalid Format: bound, number of arguments is not 4", false); //$NON-NLS-1$
					return false;
				}
			}
			else if (fFmapper.getMethodName().equals(IJFCFeatureMapper.CONSTRAINT_SIZE)) {
				// TODO Need to deal with Dimension arg
				if (exp.arguments().size() == 2) {
					fConstraints = new int[2];
					for (int i = 0; i < 2; i++)
						fConstraints[i] = Integer.parseInt(exp.arguments().get(i).toString());

					// Create a dimension from the width and height of the rectangle
					if (addToEMFmodel)
						addDimensionFeatureValue(fbeanPart.getInitMethod().getCompMethod(), fbeanPart.getEObject(), fConstraints, fFmapper
								.getFeature(null), fOwner.getCompositionModel());
				}
				else {
					CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(),
							"Invalid Format: size, number of arguments is not 2", false); //$NON-NLS-1$
					return false;
				}
			}
			else
				// Check if only string is inside. We assume that this decoder helper
				// _really_ trusts the expression it has been given to handle.
				if ((exp.arguments().size() == 1) && (exp.arguments().get(0) instanceof StringLiteral)) {
					if (addToEMFmodel) {
						// String is the constraint parameter
						String constraint = exp.arguments().get(0).toString();
						// Add the constraint to the added part
						CodeGenUtil.addConstraintString(fbeanPart.getInitMethod().getCompMethod(), fbeanPart.getEObject(), constraint, fFmapper
								.getFeature(null), fOwner.getCompositionModel());
					}
				}
				else
					return false;
		} catch (Exception e) {
			throw new CodeGenException(e);
		}
		return true;   
}


/**
 * @param exp
 * @return
 * 
 * @since 1.0.0
 */
	private boolean shouldCommit(MethodInvocation exp) {
		boolean shouldCommit = true;
		try{
			if (fFmapper.getMethodName().equals(IJFCFeatureMapper.CONSTRAINT_BOUND)) {
				if (exp.arguments().size() == 4) {
					int[] newConstraints = new int[4];
					for (int i = 0; i < 4; i++)
						newConstraints[i] = Integer.parseInt(exp.arguments().get(i).toString());
					EObject target = fbeanPart.getEObject();
					EStructuralFeature sf = fFmapper.getFeature(null);
					if(target.eIsSet(sf)){
						Object currentValue = target.eGet(sf);
						if (currentValue instanceof IJavaObjectInstance) {
							IJavaObjectInstance joi = (IJavaObjectInstance) currentValue;
							IBeanProxy bProxy = BeanProxyUtilities.getBeanProxy(joi);
							if (bProxy instanceof IRectangleBeanProxy) {
								IRectangleBeanProxy rectBeanProxy = (IRectangleBeanProxy) bProxy;
								if(		newConstraints[0]==rectBeanProxy.getX() && 
										newConstraints[1]==rectBeanProxy.getY() && 
										newConstraints[2]==rectBeanProxy.getWidth() && 
										newConstraints[3]==rectBeanProxy.getHeight())
									shouldCommit=false;
							}
						}
					}
				}
			} else if (fFmapper.getMethodName().equals(IJFCFeatureMapper.CONSTRAINT_SIZE)) {
				if (exp.arguments().size() == 2) {
					int[] newConstraints = new int[2];
					for (int i = 0; i < 2; i++)
						newConstraints[i] = Integer.parseInt(exp.arguments().get(i).toString());
					EObject target = fbeanPart.getEObject();
					EStructuralFeature sf = fFmapper.getFeature(null);
					if(target.eIsSet(sf)){
						Object currentValue = target.eGet(sf);
						if (currentValue instanceof IJavaObjectInstance) {
							IJavaObjectInstance joi = (IJavaObjectInstance) currentValue;
							IBeanProxy bProxy = BeanProxyUtilities.getBeanProxy(joi);
							if (bProxy instanceof IDimensionBeanProxy) {
								IDimensionBeanProxy sizeBeanProxy = (IDimensionBeanProxy) bProxy;
								if(		newConstraints[0]==sizeBeanProxy.getWidth() && 
										newConstraints[1]==sizeBeanProxy.getHeight()) 
									shouldCommit=false;
							}
						}
					}
				}
			} else
			if ((exp.arguments().size() == 1) && (exp.arguments().get(0) instanceof StringLiteral)) {
				// TODO Havent found a case where this is true - but leaving it here for the time being
				String newConstraint = "\""+exp.arguments().get(0).toString()+"\""; //$NON-NLS-1$ //$NON-NLS-2$
				EObject target = fbeanPart.getEObject();
				EStructuralFeature sf = fFmapper.getFeature(null);
				if(target.eIsSet(sf)){
					Object currentValue = target.eGet(sf);
					if(currentValue!=null && currentValue instanceof IJavaObjectInstance){
						IJavaObjectInstance joi = (IJavaObjectInstance) currentValue;
						String currentConstraint = CodeGenUtil.getInitString(joi, fbeanPart.getModel(), null, getExpressionReferences());
						if(newConstraint.equals(currentConstraint))
							shouldCommit = false;
					}
				}
			}
		}catch(Exception e){
			JavaVEPlugin.log(e, Level.FINER);
		}
		return shouldCommit;
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
  return parseArgs(CodeGenUtil.getInitString(curValue,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences()));	
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
      return addConstraintFeature(true) ;
}

public boolean restore() throws CodeGenException {	
	return addConstraintFeature(false);
}

public void removeFromModel() {
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
}


