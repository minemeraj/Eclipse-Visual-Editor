package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: SimpleAttributeDecoderHelper.java,v $
 *  $Revision: 1.6 $  $Date: 2004-02-10 23:37:11 $ 
 */

import java.util.Iterator;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.BeanUtilities;
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

	/**
	 *  Figure out what is the initialization string for an attribute
	 */
	protected String getInitString(Literal arg, EStructuralFeature sf) {
		if (arg instanceof NullLiteral)
			return null;
		else if (arg instanceof StringLiteral) {
			String val = new String(arg.source());
			return (BeanUtilities.createStringInitString(val));
		} else {
			// TODO  Need more thinking
			return new String(arg.source());
		}
	}

	public int getSFPriority() {
		if(fFmapper!=null){
			String methodName = null;
			if (fFmapper.getDecorator() != null)
				methodName = fFmapper.getDecorator().getWriteMethod().getName();
			if (methodName == null)
				methodName = CodeGenUtil.getWriteMethod(fExpr);
			if(methodName!=null)
				return fFmapper.getPriorityIncrement(methodName) + super.getSFPriority();
		}
		return super.getSFPriority();
	}

	protected boolean isMethodType(String writeMethodName) {
		String method = null;
		if (fFmapper != null)
			if (fFmapper.getDecorator() != null)
				method = fFmapper.getDecorator().getWriteMethod().getName();
		if (method == null)
			method = CodeGenUtil.getWriteMethod(fExpr);
		return method != null && method.equals(writeMethodName);
	}

	/**
	 *  Figure out what is the initialization string for an attribute
	 */
	protected String getInitString(QualifiedNameReference arg) {
		String initString = CodeGenUtil.tokensToString(arg.tokens);
		try{
			if(fbeanPart!=null && fbeanPart.getModel()!=null && 
			   fbeanPart.getModel().getCompilationUnit()!=null && 
			   fbeanPart.getModel().getCompilationUnit().getTypes().length>0){
				//String resType = CodeGenUtil.resolveTypeComplex(fbeanPart.getModel().getCompilationUnit().getTypes()[0], initString);
				String resType = fbeanPart.getModel().resolve(initString);
				if(resType!=null)	{
                    fUnresolveInitString = initString ;
					initString = resType;
                }                    
			}
		}catch(JavaModelException e){
			JavaVEPlugin.log(e);
		}
		return initString;
	}
	
	protected String getInitString(MessageSend msg){
		String initstring = msg.toString();
		if (msg.receiver instanceof QualifiedNameReference ||
			msg.receiver instanceof SingleNameReference){
			String unResolved = msg.receiver.toString();
			if(fbeanPart!=null && fbeanPart.getModel()!=null){
				String resolved = fbeanPart.getModel().resolve(unResolved);
				if(!resolved.equals(unResolved)){
					int from = initstring.indexOf(unResolved);
					int to = from+unResolved.length();
					if(from>-1 && from<initstring.length() && 
					   to>-1 && to<initstring.length() && from<=to){
					   	String newInitstring = initstring.substring(0,from)+resolved+initstring.substring(to,initstring.length());
						initstring = newInitstring;
					}
				}
			}
		}
		return initstring;
	}
	
	/**
	 *  Figure out what is the initialization string for an attribute
	 */
	protected String getInitString(AllocationExpression arg) {
		String initString = arg.toString();
		fUnresolveInitString = arg.toString();
		try{
			if(fbeanPart!=null && fbeanPart.getModel()!=null && 
			   fbeanPart.getModel().getCompilationUnit()!=null && 
			   fbeanPart.getModel().getCompilationUnit().getTypes().length>0){
			   	String type = CodeGenUtil.tokensToString(arg.type.getTypeName());
			   	String resolvedType = null;
		   		//String rt = CodeGenUtil.resolveTypeComplex(fbeanPart.getModel().getCompilationUnit().getTypes()[0], type);
		   		String rt = fbeanPart.getModel().resolve(type);
		   		if(rt != null)
		   			resolvedType = rt;
		   		else
		   			resolvedType = type;
				StringBuffer initConstruction = new StringBuffer("new "); //$NON-NLS-1$
				initConstruction.append(resolvedType);
				initConstruction.append("("); //$NON-NLS-1$
				for(int i=0;arg.arguments!=null && i<arg.arguments.length;i++){
					initConstruction.append(CodeGenUtil.expressionToString(arg.arguments[i]));
					if(i!=arg.arguments.length-1)
						initConstruction.append(", "); //$NON-NLS-1$
				}
				initConstruction.append(")"); //$NON-NLS-1$
				initString = initConstruction.toString();
			}
		}catch(JavaModelException e){
			JavaVEPlugin.log(e);
		}
		return initString;
	}
	
	protected String getInitString(SingleNameReference snr){
		int loc = snr.sourceStart;
		if(fOwner.getExprRef()!=null && fOwner.getExprRef().getMethod()!=null)
			loc = fOwner.getExprRef().getOffset()+fOwner.getExprRef().getMethod().getOffset();
		return fOwner.getBeanModel().resolveSingleNameReference(snr.toString(), loc);
	}
	/**
	 * 
	 */
	protected String parseInitString(Expression exp) {
		String initString = null;
		try{
		    if (exp instanceof Literal)
    			initString = getInitString((Literal)exp, fFmapper.getFeature(fExpr));
    		else if (exp instanceof QualifiedNameReference)
    			initString = getInitString((QualifiedNameReference)exp);
    		else if(exp instanceof AllocationExpression)
    			initString = getInitString((AllocationExpression)exp);
    		else if(exp instanceof MessageSend)
    			initString = getInitString((MessageSend)exp);
    		else if (exp instanceof SingleNameReference)
    			initString = getInitString((SingleNameReference)exp);
    		else{
    			// TODO  Will do for now?????
    			initString = exp.toString();
    		}
		}catch(Exception e){
			JavaVEPlugin.log(e, MsgLogger.LOG_WARNING) ;
			initString = exp.toString();
		}
		return initString;
    }
    
    protected boolean dealwithInternalBean(AllocationExpression exp) {
        
        try {
          String unresolved = CodeGenUtil.tokensToString(exp.type.getTypeName());
          String resolved = fbeanPart.getModel().resolve(unresolved) ;
          
          String origInitString = exp.toString() ;
          int start = origInitString.indexOf(unresolved);
          int end = start + unresolved.length();
          String newInitString = origInitString.substring(0, start)+resolved+origInitString.substring(end, origInitString.length());
          EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
        
          IJavaObjectInstance attr = (IJavaObjectInstance)CodeGenUtil.createInstance(resolved, fbeanPart.getModel().getCompositionModel()) ;
          attr.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(newInitString));
          
          EObject target = fbeanPart.getEObject() ;
          CodeGenUtil.propertyCleanup(target,sf) ;
          fbeanPart.getInitMethod().getCompMethod().getProperties().add(attr) ;          
          target.eSet(sf,attr) ; 
          fInitString = parseInitString(exp) ;       
        }
        catch (CodeGenException e) {
            return false ;
        }
        
        return true ;
    }
    
    
    protected IJavaInstance createPropertyInstance(String initString, EClassifier argType) throws CodeGenException {
    	IJavaInstance result ;
    	if (initString != null) {
			EFactory fact = argType.getEPackage().getEFactoryInstance();
			result = (IJavaInstance) fact.create((EClass) argType);
			result.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(initString));
		} else
			result = null;
			
	    return result ;
    }

	/**
	 *  Add a new Simple Structured Feature to a target object instance
	 *  e.g., setFoo("boo") ;
	 */
	protected boolean addFeature() throws CodeGenException {

		if (fbeanPart.getEObject() == null)
			throw new CodeGenException("null EObject:" + fExpr); //$NON-NLS-1$

		

        EClassifier argType = null ;
                
        if (fFmapper.isFieldFeature()) {
            EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
            if (sf == null) throw new CodeGenException("Invalid SF"); //$NON-NLS-1$
            argType = sf.getEType() ;   
            fInitString = parseInitString(((Assignment)fExpr).expression) ;
        }
        else {
            // Regular setter JCMMethod
            PropertyDecorator pd = fFmapper.getDecorator();
		    if (pd == null) 
		       throw new CodeGenException("Invalid PropertyDecorator"); //$NON-NLS-1$
			argType =  pd.getPropertyType();            
			// TODO  We really need to parse the argument for the true value
		    //As the PD type may be an abstract or interface !!!!!!!" 
		    Expression[] argExpr = ((MessageSend)fExpr).arguments;
		    if (argExpr.length != 1) throw new CodeGenException("Expression has more than one argument"); //$NON-NLS-1$
            if (argExpr[0] instanceof AllocationExpression) 
               return dealwithInternalBean((AllocationExpression)argExpr[0]) ;
    		// Determine the value of the attribute
    		fInitString = parseInitString(argExpr[0]) ;		    
        }

		try {
			fPropInstance = createPropertyInstance(fInitString, argType) ;
			if (fInitString == null)
			   fInitString = NULL_STRING ;
		}
		catch (CodeGenException e) {
			return false ;
		}
		
		EStructuralFeature sf = fFmapper.getFeature(fExpr) ;
		EObject target = fbeanPart.getEObject() ;

        CodeGenUtil.propertyCleanup(target,sf) ;
		if (fPropInstance == null) {
			target.eSet(sf,null) ;
		}
		else {
			// Add the attribute
	      	fbeanPart.getInitMethod().getCompMethod().getProperties().add(fPropInstance) ;
			target.eSet(sf,fPropInstance) ;			            
		}

		return true;
	}
	
	/**
	 * This is a temporary workaround to the fact that we need to set
	 * a constructor on a BoxLayout
	 */
	private String boxLayoutOveride(String st) {
		int index =  st.indexOf("(,") ; //$NON-NLS-1$
        if (index>=0) {            
            String toAdd = fbeanPart.getSimpleName();
            StringBuffer s = new StringBuffer (st) ;            
            s.replace(index, index+2, "("+toAdd+", ") ; //$NON-NLS-1$ //$NON-NLS-2$
          return s.toString() ;
        }
        return st ;
	}

	/**
	 *  Get the initialization string currently held in the composition model
	 */
	protected String primGetInitString() {

		Object currentVal = fbeanPart.getEObject().eGet(fFmapper.getFeature(fExpr));
		fPropInstance = (IJavaInstance) currentVal;
		if (currentVal != null) {
			if (currentVal instanceof IJavaObjectInstance)
				return boxLayoutOveride(CodeGenUtil.getInitString((IJavaObjectInstance) currentVal,fbeanPart.getModel()));
			else if (currentVal instanceof IJavaDataTypeInstance)
				return CodeGenUtil.getInitString((IJavaDataTypeInstance) currentVal,fbeanPart.getModel());

		} else { // Is it a null value ??
			EObject eobj = fbeanPart.getEObject();			
			Iterator itr = ((JavaClass)eobj.eClass()).getAllProperties().iterator();
			// TODO  
			while (itr.hasNext()) {
				EStructuralFeature sf = (EStructuralFeature) itr.next();
				if (eobj.eIsSet(sf) && sf.equals(fFmapper.getFeature(null)))
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
			CodeSnippetTranslator.indexOfIgnoringSpace(fExprSig, "("+fInitString+")");         //$NON-NLS-1$ //$NON-NLS-2$
		if (fUnresolveInitString!=null && (positions[0] < 0 || positions[1] < 0)) {
            positions = CodeSnippetTranslator.indexOfIgnoringSpace(fExprSig, "("+fUnresolveInitString+")"); //$NON-NLS-1$ //$NON-NLS-2$
        
            if (positions[0] < 0 || positions[1] < 0)
            {
            	// Could not match delta for the existing expression
            	// At this time will not preserve existing
    			JavaVEPlugin.log(
    				"SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", MsgLogger.LOG_FINE); //$NON-NLS-1$
    			return generate(null);
    		}
    		positions[0]++ ;
    		positions[1]-- ;
        }
        else {
        	if (positions[0] < 0 || positions[1] < 0)  {        		
    			JavaVEPlugin.log(
    				"SimpleAttr.DecoderHelper.primRefreshFromComposition(): Error", MsgLogger.LOG_FINE); //$NON-NLS-1$
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

	public boolean decode() throws CodeGenException {

		if (fFmapper.getFeature(fExpr) == null || fExpr == null) {
		    CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Feature "+fFmapper.getMethodName()+" is not recognized.",false) ; //$NON-NLS-1$ //$NON-NLS-2$
			throw new CodeGenException("null Feature:" + fExpr); //$NON-NLS-1$
		}

		if ((fExpr instanceof Assignment) ||
		    ((fExpr instanceof MessageSend) && ((MessageSend)fExpr).arguments.length == 1))
			return (addFeature());
		else {
			CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Invalid Format",false) ; //$NON-NLS-1$
			return (false);
		}

	}

	public void removeFromModel() {
		unadaptToCompositionModel();

		org.eclipse.emf.ecore.EStructuralFeature sf = fFmapper.getFeature(fExpr);
		IJavaObjectInstance parent = (IJavaObjectInstance) fbeanPart.getEObject();
		EObject setting = (EObject) parent.eGet(sf) ;
		if (setting != null) {
		  parent.eUnset(sf);
		  cleanProperty(setting) ;
		}
		parent.eUnset(sf);
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
            JavaVEPlugin.log("No Write JCMMethod found for "+fFmapper.getFeature(null)+" on "+fbeanPart.getUniqueName(),MsgLogger.LOG_WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
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