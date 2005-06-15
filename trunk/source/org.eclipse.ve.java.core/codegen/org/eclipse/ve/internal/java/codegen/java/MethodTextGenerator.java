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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: MethodTextGenerator.java,v $
 *  $Revision: 1.23 $  $Date: 2005-06-15 18:58:22 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.jdom.IDOMMethod;

import org.eclipse.jem.internal.instantiation.base.FeatureValueProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

//*****************************************************************
// TODO:  this class should be converted to use a JavaJet template extending AbstractMethodTextGenerator
//*****************************************************************
/**
 * @deprecated
 * @author Gili Mendel
 * @since 1.0.0
 */
public class MethodTextGenerator implements IMethodTextGenerator {
	
protected   IBeanDeclModel    fModel ;
protected   IDOMMethod        fJdomMethod ;
protected   CodeMethodRef     fMethodRef ;
protected   EObject         fComponent ;
protected   String            freturnType ;
protected   String            fName ;
protected   String            fMethodName ;
protected   String[]          fComments = null ;
private     boolean           fsourceAppended = false ;

public final static String  DEFAULT_PREFIX = "get";   //$NON-NLS-1$

	
public MethodTextGenerator (EObject component, IBeanDeclModel model)  {
	fModel = model ;
	fComponent = component ;
}



/**
 *  Create a new Model element and a corresponding Java Source
 */
protected CodeExpressionRef GenerateAttribute(EStructuralFeature sf,BeanPart bean,Object [] args, boolean updateDoc) throws CodeGenException {
	
    ExpressionRefFactory egen = new ExpressionRefFactory(bean,sf) ;      	
	CodeExpressionRef exp = egen.createFromJVEModel(args) ;	
	if (updateDoc)
     if (exp.getMethod().getMethodHandle() != null)
	     exp.insertContentToDocument() ;
     else
         exp.setState(CodeExpressionRef.STATE_EXP_NOT_PERSISTED, true) ;
	return exp ;
}

public void setComments (String[] comments) {
	fComments = comments ;
}

/**
 *  This is a generic utility to note attributes that should not be reflected in the Java
 *  source code as a generic attribute.
 *  @return boolean, true to omit, false attribute shuld be reflected.
 */
public static boolean isNotToBeCodedAttribute(EStructuralFeature sf, EObject component) {		
	// Container attributes are addressed seperatly
	if (sf==null ||
        sf.isTransient() ||
//	    sf.equals(CodeGenUtil.getParentContainerFeature(component)) ||
	    sf.equals(CodeGenUtil.getComponentFeature(component)) ||
	    sf.equals(CodeGenUtil.getAllocationFeature(component))
	    )
	    return true ;	

//	// If it is a layout manager constraint of my container, let container's add() method take care of it
//	if (sf.equals(CodeGenUtil.getConstraintFeature(component)) &&
//	    !ConstraintDecoderHelper.primIsFreeFormConstraint(component) &&
//	    !CodeGenUtil.getCompositionInstance(component).isAssignableFrom((EClassifier) ((IJavaObjectInstance)component).getJavaType()))
//	    return true ;	
	    
	return false ;
}

/**
 *  Determine if a SF impact java source
 */
protected boolean ignoreSF(EStructuralFeature sf) {
	// TODO  Need to set up a hash somewere
	return isNotToBeCodedAttribute(sf,fComponent) ;	
}


/**
 * This method will look/generate Expression meta objects for all settings for a given
 * bean.
 * 
 * 
 */
public void generateExpressionsContent() throws CodeGenException {
	
	if (fsourceAppended) return ;
	
	BeanPart bp = fModel.getABean(fComponent) ;
	BeanDecoderAdapter bda = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(fComponent, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
	
	Iterator itr = ((JavaClass)fComponent.eClass()).getAllProperties().iterator();
	while (itr.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) itr.next();
		if (fComponent.eIsSet(sf)) {            
			if (bda == null || sf.isMany() || ignoreSF(sf))
				continue;
		    if (bda.getSettingAdapters(sf) != null) // Expression already exists
		        continue ;
			CodeExpressionRef newExpr = GenerateAttribute(sf, bp, null, true);
			String src = newExpr.getContent();
			if (src == null)
				throw new CodeGenException("Could not Generate Source"); //$NON-NLS-1$
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("\tAdding: " + src, Level.FINE); //$NON-NLS-1$	
		}
	}
	
	
}



/**
 *  Look for added attributes
 * @deprecated
 *  
 */
protected void appendNewSource(final StringBuffer buf, final BeanPart bean, final List kids, final boolean updateDoc) throws CodeGenException {

	EObject obj = bean.getEObject();
	final BeanDecoderAdapter a = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bean.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;
	CodeGenException exp = (CodeGenException) FeatureValueProvider.FeatureValueProviderHelper.visitSetFeatures(obj, new FeatureValueProvider.Visitor() {
	
		public Object isSet(EStructuralFeature feature, Object value) {
			try {
				if (feature.isMany() || ignoreSF(feature) || kids.contains(feature))
					return null;
				if (a.getSettingAdapters(feature) != null && a.getSettingAdapters(feature).length > 0)
					return null;
				CodeExpressionRef newExpr = GenerateAttribute(feature, bean, null, updateDoc);
				String src = newExpr.getContent();
				if (src == null)
					throw new CodeGenException("Could not Generate Source"); //$NON-NLS-1$
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log("\tAdding: " + src, Level.FINE); //$NON-NLS-1$
				if (!updateDoc)
					newExpr.setOffset(buf.length());
				buf.append(src);
				return null;
			} catch (CodeGenException e) {
				return e;
			}
		}
	
	});
	if (exp != null)
		throw exp;
	fsourceAppended=true ;
}


CodeExpressionRef parseInitExpression(BeanPart bean) {
	ExpressionRefFactory eg = new ExpressionRefFactory(bean,null) ;
	return eg.parseInitExpression() ;
}

CodeExpressionRef createInitExpression(BeanPart bean) {
	ExpressionRefFactory eg = new ExpressionRefFactory(bean,null) ;
	return eg.createInitExpression() ;
}


/**
 * Parent Child relationship may be aggregated by a middle object (e.g., Container Constraint)
 * @deprecated
 */
protected Object getChildRoot(BeanPart bean, BeanPart child,EStructuralFeature sf) {
    if (bean == null || child == null) return null ;
    if (child.getEObject().eContainer().equals(bean.getEObject()))
       return child.getEObject() ;
    else
       return child.getEObject().eContainer() ;
}

/**
 *  Look for added attributes
 *  @deprecated
 */
protected void GenerateAChild(StringBuffer buf,BeanPart bean, BeanPart child,EStructuralFeature sf) throws CodeGenException {
	
	Object actualChild = getChildRoot(bean,child,sf) ;
    CodeExpressionRef newExpr = GenerateAttribute(sf,bean,new Object[] { actualChild },false) ; ;
	String src =  newExpr.getContent() ;
	if (src == null) {
		if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
			JavaVEPlugin.log ("No Source Generated for "+bean.getUniqueName()+"("+sf+")", Level.WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return ;
	}	
    
    // The generator assume that offsets will be update once we insert the exp. to the doc.
    // We are going to set the offset explicitly
    CodeMethodRef mref = newExpr.getMethod() ;
    mref.removeExpressionRef(newExpr) ;
    newExpr.setOffset(buf.length()) ;
    mref.addExpressionRef(newExpr) ;
	buf.append(src) ;		      	
	JavaVEPlugin.log ("\tAttribute source added: "+src, Level.FINE) ;	       //$NON-NLS-1$
	
}

protected void setConstructorString (BeanMethodTemplate tp, Object component, List imports) {
    if (component instanceof IJavaObjectInstance) {
     // use the init string rather than default template string
        IJavaObjectInstance obj = (IJavaObjectInstance) component ;
        String st = CodeGenUtil.getInitString(obj,fModel, imports, null) ;  
        tp.setBeanConstructorString(st) ;
    }
}



public void generateInLine(CodeMethodRef method,String beanName, List kids) throws CodeGenException {
    
    fMethodRef = method ;    
    freturnType = ((IJavaObjectInstance)fComponent).getJavaType().getQualifiedName() ;
    fName=beanName ;
    

    // Set up a new BeanPart in the decleration Model
    BeanPart bp = fModel.getABean(fName) ;
    // TODO:GM need to be able to generate code that reuses beans.    
    if(bp==null)
    	bp = fModel.getABean(BeanPartDecleration.createDeclerationHandle(method,fName));//method.getMethodHandle()+"^"+fName);
    
   StringBuffer sb = new StringBuffer () ;
    
   CodeExpressionRef initExp = createInitExpression(bp);
   // Allow the expression sorted to find a nice spot for this one
   initExp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, false); // initExp.setState(initExp.getState()&~initExp.STATE_SRC_LOC_FIXED) ;
   initExp.setOffset(-1) ;
   try {   
       method.updateExpressionOrder() ;
   }
   catch (Throwable e) {
        JavaVEPlugin.log(e, Level.SEVERE) ;
        return  ;
   }
   // We may be processing a nested child, 
   // and the method is not in the source yet
   if (method.getMethodHandle() != null)
      initExp.insertContentToDocument() ;
   else
      initExp.setState(CodeExpressionRef.STATE_EXP_NOT_PERSISTED, true) ;
            
    appendNewSource(sb,bp,kids,true) ;
    
    
    
}

public String generateMethod(CodeMethodRef method,String methodName,String beanName, List imports) throws CodeGenException {
	  
    fMethodRef = method ;    
    freturnType = ((IJavaObjectInstance)fComponent).getJavaType().getQualifiedName() ;
    fName=beanName ;
    fMethodName = methodName ;
                   
    // Set up a new BeanPart in the decleration Model
    BeanPart bp = fModel.getABean(fName) ;
 	
    BeanMethodTemplate template = new BeanMethodTemplate(freturnType,fName,fMethodName,fComments) ;    
    setConstructorString (template, fComponent, imports) ;
    template.setSeperator(fModel.getLineSeperator()) ;
//    if (((IJavaObjectInstance)fComponent).isImplicit()) {
//    	template.setImplicit(true) ;
//    }
    StringBuffer sb = new StringBuffer () ;
    sb.append (template.getPrefix()) ;
    

     if (fMethodRef.getExpressions() != null && fMethodRef.getExpressions().hasNext())
        throw new CodeGenException("Init JCMMethod already has expressions in it") ;     //$NON-NLS-1$
//    // Some Expressions may have generated by nested, internal children of this bean.
//
//    ArrayList shadowExprList = new ArrayList() ;
//    Iterator itr = fMethodRef.getExpressions() ;
//    while (itr.hasNext()) 
//        shadowExprList.add(itr.next()) ;
//    for (int i=0; i<shadowExprList.size(); i++) 
//        fMethodRef.removeExpressionRef((CodeExpressionRef)shadowExprList.get(i)) ;

    // Init Expression will use the method's content
    method.setContent(template.getPrefix()+template.getPostfix()) ;   
//    
//    // Add back the internal expressions, if any 
//    for (int i=0; i<shadowExprList.size(); i++) {
//        CodeExpressionRef exp = (CodeExpressionRef)shadowExprList.get(i) ;
//        exp.setOffset(sb.length()) ;
//        sb.append(exp.getContent()) ;
//        // How else will we have a shadow expression ?
//        if (!exp.isStateSet(exp.STATE_EXP_NOT_PERSISTED)) throw new CodeGenException ("STATE_EXP_NOT_PERSISTED is not set for existing expression") ; //$NON-NLS-1$
//        exp.setState(exp.STATE_EXP_NOT_PERSISTED, false) ;
//        fMethodRef.addExpressionRef(exp) ;        
//    }
       
    appendNewSource(sb,bp,new ArrayList(),true) ;     
    sb.append(template.getPostfix()) ;
    
    Map options = fModel.getCompilationUnit().getJavaProject().getOptions(true);		
	return DefaultClassGenerator.format(sb.toString(), CodeFormatter.K_CLASS_BODY_DECLARATIONS, options, fModel.getLineSeperator() );              
}

public String toString() {
	return super.toString() + fMethodName;
}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#setBeanInitArgs(java.lang.String[])
	 */
	public void setBeanInitString(String str) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IMethodTextGenerator#setMethodArguments(java.lang.String[])
	 */
	public void setMethodArguments(String[] fmethodArguments) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodTextGenerator#getMethodPrefix()
	 */
	public String getMethodPrefix() {
		return DEFAULT_PREFIX;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IMethodTextGenerator#generateMain()
	 */
	public String generateMain(String className) {		
		return null;
	}
}


