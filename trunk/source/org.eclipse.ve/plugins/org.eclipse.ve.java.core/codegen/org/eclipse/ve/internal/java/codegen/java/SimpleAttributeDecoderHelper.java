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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: SimpleAttributeDecoderHelper.java,v $
 *  $Revision: 1.39 $  $Date: 2005-08-24 23:30:44 $ 
 */

import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class SimpleAttributeDecoderHelper extends ExpressionDecoderHelper {

    // true null is taken is NOT Existant.   NULL_STRING represets existant
    // but null (set)
	public static final String NULL_STRING = "null"; //$NON-NLS-1$ 

	String fInitString = null; // last value known in MOF
    String fUnresolveInitString = null ;
	IJavaInstance fPropInstance = null;

	public SimpleAttributeDecoderHelper(
		BeanPart bean,
		Statement exp,
		IJavaFeatureMapper fm,
		IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}

	public int getSFPriority() {
		if(fFmapper!=null){
			String methodName = null;
			if (fFmapper.getDecorator() != null)
				methodName = fFmapper.getMethodName();
			if (methodName == null)
				methodName = AbstractFeatureMapper.getWriteMethod(fExpr);
			if(methodName!=null)
				return fFmapper.getFeaturePriority(methodName) ;
		}
		return super.getSFPriority();
	}

	protected boolean isMethodType(String writeMethodName) {
		String method = null;
		if (fFmapper != null)
			if (fFmapper.getDecorator() != null)
				method = fFmapper.getDecorator().getWriteMethod().getName();
		if (method == null)
			method = AbstractFeatureMapper.getWriteMethod(fExpr);
		return method != null && method.equals(writeMethodName);
	}
	
    protected IJavaInstance createPropertyInstance(Expression arg, EClassifier argType) {    
    		if (arg instanceof NullLiteral) return null;
    			
            CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
            JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(
            		ConstructorDecoderHelper.getParsedTree(arg,expOfMethod,fOwner.getExprRef().getOffset(),fbeanPart.getModel(), getExpressionReferences()));
            
            EFactory fact = argType.getEPackage().getEFactoryInstance();
			IJavaInstance result = (IJavaInstance) fact.create((EClass) argType);
            
            result.setAllocation(alloc);
            
            return result;            
    }

	/**
	 *  Add a new Simple Structured Feature to a target object instance
	 *  e.g., setFoo("boo") ;
	 */
	protected boolean addFeature(boolean updateEMFModel) throws CodeGenException {

		if (fbeanPart.getEObject() == null)
			throw new CodeGenException("null EObject:" + fExpr); //$NON-NLS-1$
		

        EClassifier argType = null ;
        String newInitString = NULL_STRING;
		Expression newInitASTExpression = null;
        IJavaInstance newPropInstance = null;
        
        if (fFmapper.isFieldFeature()) {
            EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
            if (sf == null) throw new CodeGenException("Invalid SF"); //$NON-NLS-1$
            argType = sf.getEType() ;
            if (updateEMFModel)
               newPropInstance = createPropertyInstance(((Assignment)getExpression()).getRightHandSide(), argType);
            else
               newPropInstance = (IJavaInstance) fbeanPart.getEObject().eGet(sf);
            if (newPropInstance!=null){
                newInitString = ((Assignment)getExpression()).getRightHandSide().toString();
				newInitASTExpression = ((Assignment)getExpression()).getRightHandSide();
            }
        }
        else {
            // Regular setter JCMMethod
            PropertyDecorator pd = fFmapper.getDecorator();
		    if (pd == null) 
		       throw new CodeGenException("Invalid PropertyDecorator"); //$NON-NLS-1$
			argType =  pd.getPropertyType();            
		    List argExpr = ((MethodInvocation)getExpression()).arguments();
		    if (argExpr.size() != 1) throw new CodeGenException("Expression has more than one argument"); //$NON-NLS-1$
		    if (updateEMFModel)
		    	newPropInstance = createPropertyInstance((Expression)argExpr.get(0), argType);
	        else
	            newPropInstance = (IJavaInstance) fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr));
		    if (newPropInstance!=null){
                newInitString = ((Expression)argExpr.get(0)).toString();
				newInitASTExpression = ((Expression)argExpr.get(0));
		    }
        }
        	    
	    
	    if (!updateEMFModel) {
	    	fPropInstance = newPropInstance;
			fInitString = newInitString;
			return true;
	    }
	    
		EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
		EObject target = fbeanPart.getEObject() ;
		
  		// Smart decoding capability:
		// If the value has not changed - no need to re-apply it
		if(target.eIsSet(sf) && newInitASTExpression!=null){
			boolean changed = true;
			Object currentValue = target.eGet(sf);
			if(currentValue==null){
				// explicitly null value set - check if new value is null also
				if(newInitASTExpression instanceof NullLiteral)
					changed = false;
			}else{
				JavaAllocation currentAlloc = ((IJavaInstance)currentValue).getAllocation();
				if (currentAlloc instanceof InitStringAllocation) {
					InitStringAllocation currentInitStrAlloc = (InitStringAllocation) currentAlloc;
					String currentInitString = currentInitStrAlloc.getInitString();
					if(currentInitString.trim().equals(newInitString.trim()))
						changed = false;
				}else if(currentAlloc instanceof ParseTreeAllocation){
					ParseTreeAllocation currentPTAlloc = (ParseTreeAllocation) currentAlloc;
					PTExpression currentPTExpression = currentPTAlloc.getExpression();
					CodeExpressionRef exp = fOwner.getExprRef();
					PTExpression newInitPTExpression = ConstructorDecoderHelper.getParsedTree(newInitASTExpression, exp.getMethod(), exp.getOffset(), fbeanPart.getModel(),  getExpressionReferences());
					changed = !CodeGenUtil.areParseTreesEqual(currentPTExpression, newInitPTExpression);
				}
			}
			if(!changed) 
				return true; 			
		}
		
		fPropInstance = newPropInstance;
		fInitString = newInitString;

        EObject oldSetting = (EObject) target.eGet(sf);
		if (fPropInstance == null) {
			target.eSet(sf,null) ;
		}
		else {
			// Add the attribute
	      	fbeanPart.getInitMethod().getCompMethod().getProperties().add(fPropInstance) ;
			target.eSet(sf,fPropInstance) ;			            
		}
		CodeGenUtil.propertyCleanup(oldSetting);
		return true;
	}

	/**
	 *  Get the initialization string currently held in the composition model
	 */
	protected String primGetInitString() {
		if (fbeanPart.isDisposed())
			return null;
		Object currentVal = fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr));
		if (currentVal==null || currentVal instanceof IJavaInstance) {
			fPropInstance = (IJavaInstance) currentVal;
			if (currentVal != null) {
				if (currentVal instanceof IJavaObjectInstance)
					return CodeGenUtil.getInitString((IJavaObjectInstance) currentVal,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences());
				else if (currentVal instanceof IJavaDataTypeInstance)
					return CodeGenUtil.getInitString((IJavaDataTypeInstance) currentVal,fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences());

			} else { // Is it a null value ??
				EObject eobj = fbeanPart.getEObject();
				EStructuralFeature sf = fFmapper.getFeature(null); 
				if (((JavaClass)eobj.eClass()).getAllProperties().indexOf(sf) != -1)
					if (eobj.eIsSet(sf))
						return NULL_STRING;
			}
		}
		return null;
	}

	/**
	 *   Check to see if this attribute is still in the composition model
	 */
	public boolean primIsDeleted() {
		return primGetInitString() == null;
	}

	/**
	 * @return  Whether this helper can handle the set Attribute or not.
	 */
	public boolean canRefreshFromComposition(){
		EObject obj = (EObject) fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr));
		if(obj!=null &&
		   fOwner!=null && fOwner.getExprRef()!=null && 
		   fOwner.getExprRef().getBean()!=null && 
		   fOwner.getExprRef().getBean().getModel()!=null && 
		   fOwner.getExprRef().getBean().getModel().getABean(obj)!=null)
			return false;
		return super.canRefreshFromComposition();
	}
	
	/**
	 *  Reflect the current initialization string from the composition model
	 */
	public String primRefreshFromComposition(String expSig)
		throws CodeGenException {
		if (fExprSig == null)
			fExprSig = expSig;
		// TODO  Need to deal with un initialized init string
		// Isolate the initialization string int he current expression
		int[] positions =
			CodeGenUtil.indexOfIgnoringSpace(fExprSig, "("+fInitString+")");         //$NON-NLS-1$ //$NON-NLS-2$
		if (fUnresolveInitString!=null && (positions[0] < 0 || positions[1] < 0)) {
            positions = CodeGenUtil.indexOfIgnoringSpace(fExprSig, "("+fUnresolveInitString+")"); //$NON-NLS-1$ //$NON-NLS-2$
        
            if (positions[0] < 0 || positions[1] < 0)
            {
            	// Could not match delta for the existing expression
            	// At this time will not preserve existing
            	if (JavaVEPlugin.isLoggingLevel(Level.FINE))
            		JavaVEPlugin.log("SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", Level.FINE); //$NON-NLS-1$
    			return generate(null);
    		}
    		positions[0]++ ;
    		positions[1]-- ;
        }
        else {
        	if (positions[0] < 0 || positions[1] < 0)  {        		
    			JavaVEPlugin.log(
    				"SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", Level.FINE); //$NON-NLS-1$
    			return generate(null);
        		
        	}
            positions[0]++ ;
            positions[1]-- ;
        }
		fInitString = primGetInitString();
		StringBuffer sb = new StringBuffer(expSig);
		sb.replace(positions[0], positions[1], fInitString);
		fExprSig = sb.toString();
		return fExprSig;
	}
	
	protected boolean isValid() throws CodeGenException {
		if (fFmapper.getFeature(fExpr) == null || fExpr == null) {
		    CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Feature "+fFmapper.getMethodName()+" is not recognized.",false) ; //$NON-NLS-1$ //$NON-NLS-2$
			throw new CodeGenException("null Feature:" + fExpr); //$NON-NLS-1$
		}
		Expression exp = getExpression();
		if ((exp instanceof Assignment) ||
		    ((exp instanceof MethodInvocation) && ((MethodInvocation)exp).arguments().size() == 1))
			return true;
		else 			
			return false;		
	}

	public boolean decode() throws CodeGenException {
		
		if (isValid())
			return (addFeature(true));
		else {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Format",false) ; //$NON-NLS-1$
			return (false);
		}

	}
	
	public boolean restore() throws CodeGenException {
		if (isValid()) {
			return (addFeature(false));
		}
		else {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Format",false) ; //$NON-NLS-1$
		  return false;
		}
	}

	public void removeFromModel() {
		unadaptToCompositionModel();

		org.eclipse.emf.ecore.EStructuralFeature sf = fFmapper.getFeature(fExpr);
		IJavaObjectInstance parent = (IJavaObjectInstance) fbeanPart.getEObject();
		if (parent.eIsSet(sf)) {
			EObject setting = (EObject) parent.eGet(sf) ;
			parent.eUnset(sf);
			cleanProperty(setting) ;
		}
	}

	public String generate(Object[] noArgs) throws CodeGenException {
		if (fFmapper.getFeature(null) == null)
			throw new CodeGenException("null Feature"); //$NON-NLS-1$
		fInitString = primGetInitString();
		if (fInitString == null)
			fInitString = NULL_STRING; // null value
		// TODO  Should cache this in the Feature Mapper
		String mtd = fFmapper.getMethodName();
        if (mtd == null) {
        	if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
        		JavaVEPlugin.log("No Write JCMMethod found for "+fFmapper.getFeature(null)+" on "+fbeanPart.getUniqueName(),Level.WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
            return null ;
        }
		String sel = fbeanPart.getSimpleName();
		ExpressionTemplate exp =
			new ExpressionTemplate(
				sel,
				mtd,
				new String[] { fInitString },
				null,
				0);
		exp.setLineSeperator(fbeanPart.getModel().getLineSeperator());
		if (fFmapper.isFieldFeature())
		  exp.setFieldAccess(true) ;
		fExprSig = exp.toString();
		return fExprSig;

	}

	public boolean isImplicit(Object args[]) {
		return false;
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
		// Add source range adapter on the property, so that we can drive to source
		if (fPropInstance == null)
		   fPropInstance = (IJavaInstance) fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr)) ;
		if (fPropInstance != null)
	        fPropInstance.eAdapters().add(new ExpressionDecoderAdapter(decoder).getShadowSourceRangeAdapter()) ;	
	  
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#UnadaptToCompositionModel()
	 */
	public void unadaptToCompositionModel() {
		super.unadaptToCompositionModel();
		if (fPropInstance == null)
		  if (!fFmapper.getFeature(fExpr).isMany()) {
		  	// It is possible that we have a simple adapter for some feature
		  	// that we do not support, but it is not really a simple feature
		    Object o = fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr)) ;
		    if (o instanceof IJavaInstance)
		        fPropInstance = (IJavaInstance) o; 
		  }
		// Remove the source range adapter from the property
		if (fPropInstance != null) {
	        ICodeGenAdapter a = (ICodeGenAdapter) EcoreUtil.getExistingAdapter(fPropInstance,ICodeGenAdapter.JVE_CODEGEN_EXPRESSION_SOURCE_RANGE) ;
	        if (a != null)
	          fPropInstance.eAdapters().remove(a) ;
		}
	}

}
