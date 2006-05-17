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
 *  $RCSfile: PointDecoderHelper.java,v $
 *  $Revision: 1.16 $  $Date: 2006-05-17 20:14:59 $ 
 */


import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IPointBeanProxy;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

import com.ibm.icu.util.StringTokenizer;


public class PointDecoderHelper extends ExpressionDecoderHelper {
	
	public static final String POINT_CLASS = "java.awt.Point" ; //$NON-NLS-1$
	int  fpointArgs[]=null ;		

public PointDecoderHelper(BeanPart bean, Statement exp,  IJavaFeatureMapper fm, IExpressionDecoder owner){
	super (bean,exp,fm,owner) ;
}


/**
 *  Add a new constraint
 */
protected boolean	addPointArg(boolean updateEMFmodel) throws CodeGenException {
	if (fbeanPart.getEObject() == null || fFmapper.getMethodName() == null) throw new CodeGenException ("null EObject:"+fExpr) ;       //$NON-NLS-1$
		
	try {
		if(!shouldCommit(((MethodInvocation)getExpression()).arguments()))
			return true;
		if (((MethodInvocation) getExpression()).arguments().size() == 2) {
			fpointArgs = new int[2];
			for (int i = 0; i < 2; i++)
				fpointArgs[i] = Integer.parseInt(((MethodInvocation) getExpression()).arguments().get(i).toString());

			if (!updateEMFmodel)
				return true; 
			
            IJavaInstance value = CodeGenUtil.createInstance(POINT_CLASS,fOwner.getCompositionModel()) ;	 //$NON-NLS-1$
            value.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation("new "+POINT_CLASS+"("+Integer.toString(fpointArgs[0])+    //$NON-NLS-1$ //$NON-NLS-2$
                                                            ","+Integer.toString(fpointArgs[1])+")")) ;                                 //$NON-NLS-1$ //$NON-NLS-2$
            EObject target = fbeanPart.getEObject() ;
            EStructuralFeature sf = fFmapper.getFeature(null) ;
            
            EObject oldPoint = (EObject) target.eGet(sf);
            fbeanPart.getInitMethod().getCompMethod().getProperties().add(value) ;
            target.eSet(sf,value);
            CodeGenUtil.propertyCleanup(oldPoint);
		}
		else {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Format: number of arguments is not 2",false) ; //$NON-NLS-1$
			return false;
		}

	}
	catch (Exception e) {
		throw new CodeGenException(e);
	}
	return true;
}


/**
 * @param list
 * @return
 * 
 * @since 1.0.0
 */
private boolean shouldCommit(List list) {
	boolean shouldCommit = true;
	if (list.size() == 2) {
		int[] newPointArgs = new int[2];
		for (int i = 0; i < 2; i++)
			newPointArgs[i] = Integer.parseInt(list.get(i).toString());
		
        EObject target = fbeanPart.getEObject() ;
        EStructuralFeature sf = fFmapper.getFeature(null) ;
        if(target.eIsSet(sf)){
        	Object currentValue = target.eGet(sf);
        	if (currentValue instanceof IJavaObjectInstance) {
				IJavaObjectInstance joi = (IJavaObjectInstance) currentValue;
				IBeanProxy bProxy = BeanProxyUtilities.getBeanProxy(joi);
				if (bProxy instanceof IPointBeanProxy) {
					IPointBeanProxy pbProxy = (IPointBeanProxy) bProxy;
					if(pbProxy.getX()==newPointArgs[0] && pbProxy.getY()==newPointArgs[1])
						shouldCommit = false;
				}
			}
        }
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
protected boolean sameArgs(int[] A, int[] B) {
	if (A.length != B.length) return false ;
	for (int i=0; i<A.length; i++)
	   if (A[i] != B[i]) return false ;
	return true ;
}


/**
 *  Get the current constraints in the Composition Model
 */
protected int[]   getCompositionArgs() throws CodeGenException {

  IJavaObjectInstance curValue = (IJavaObjectInstance)fbeanPart.
                                 getEObject().eGet(fFmapper.getFeature(fExpr)) ;  
  return parseArgs(CodeGenUtil.getInitString(curValue,fbeanPart.getModel(),fOwner.getExprRef().getReqImports(),null)) ;	
} 
/**
 *  Create initialization arguments
 */
protected String argsToString(int[] constraints) {
	StringBuffer sb = new StringBuffer() ;
      for (int i=0; i<constraints.length; i++) {
     	  if (i>0) sb.append(", ") ; //$NON-NLS-1$
     	  sb.append(Integer.toString(constraints[i])) ;
      }
      return sb.toString() ; 
}

public String primRefreshFromComposition(String expSig) throws CodeGenException {
	  
  int [] curConstraints = getCompositionArgs() ;
  if (fpointArgs != null)
    if (sameArgs(curConstraints,fpointArgs)) {
  	fExprSig = expSig ; // spacing and such may have changed.
    }
    else {
      if (curConstraints.length != fpointArgs.length) {
      	fExprSig = generate(null) ;
      	// Remove the generated comments etc.
      	fExprSig = fExprSig.substring(0,fExprSig.indexOf(")")+1) ; //$NON-NLS-1$
      }      
      else {  	     
        int[] curArgsIndexes = getArgsIndexes(expSig) ;
        String newArgs = argsToString(curConstraints) ;     
        fpointArgs = curConstraints ;
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
    return addPointArg(true) ;
}

public boolean restore() throws CodeGenException {
	return addPointArg(false);
}

public void removeFromModel() {
	unadaptToCompositionModel() ;
	
	org.eclipse.emf.ecore.EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
	IJavaObjectInstance parent = (IJavaObjectInstance)fbeanPart.getEObject() ;
	EObject setting = (EObject) parent.eGet(sf) ;
	cleanProperty(setting) ;
	parent.eUnset(sf) ; 
}


/**
 *  Overide the default to deal with the lack of Decorator
 */
protected ExpressionTemplate getExpressionTemplate(String[] args) throws CodeGenException {
	
	if (fFmapper.getDecorator() != null) 
	   return super.getExpressionTemplate(args) ;
	   
      String mtd  ;
      if (fpointArgs.length == 2)
        mtd = IJFCFeatureMapper.LOCATION_NAME ;
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
	   fpointArgs = getCompositionArgs() ;
	}
	catch (CodeGenException e) {
		return null ; 
	}
		
	ExpressionTemplate exp = getExpressionTemplate(new String[] {argsToString(fpointArgs)}) ;
      fExprSig = exp.toString() ;	                                                 
      return fExprSig ;	     	
}



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


