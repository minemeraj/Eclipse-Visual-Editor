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
 *  $RCSfile: MethodTextGenerator.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.jdom.IDOMMethod;

import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;



public class MethodTextGenerator {
	
protected   IBeanDeclModel    fModel ;
protected   IDOMMethod        fJdomMethod ;
protected   CodeMethodRef     fMethodRef ;
protected   EObject         fComponent ;
protected   String            freturnType ;
protected   String            fName ;
protected   String            fMethodName ;
protected   String[]          fComments = null ;

	
public MethodTextGenerator (EObject component, IBeanDeclModel model) {
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
	     exp.insertContentToDocument(true) ;
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
       ((sf instanceof EStructuralFeature) && ((EStructuralFeature)sf).isTransient()) ||
//	    sf.equals(CodeGenUtil.getParentContainerFeature(component)) ||
	    sf.equals(CodeGenUtil.getComponentFeature(component)) ||
	    sf.equals(CodeGenUtil.getImplicitFeature(component)) ||
	    sf.equals(CodeGenUtil.getInitStringFeature(component))
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
 * @deprecated
 */

protected void appendNewSource(StringBuffer buf,BeanPart bean, List kids) throws CodeGenException {
    appendNewSource(buf,bean,kids,false) ;
}

/**
 * This method will look/generate Expression meta objects for all settings for a given
 * bean.
 * 
 * 
 */
public void generateExpressionsContent() throws CodeGenException {
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
			JavaVEPlugin.log("\tAdding: " + src, MsgLogger.LOG_FINE); //$NON-NLS-1$	
		}
	}
	
	
}



/**
 *  Look for added attributes
 * @deprecated
 *  
 */
protected void appendNewSource(StringBuffer buf, BeanPart bean, List kids, boolean updateDoc) throws CodeGenException {

	EObject obj = bean.getEObject();
	BeanDecoderAdapter a = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bean.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;
	Iterator itr = ((JavaClass)obj.eClass()).getEAllStructuralFeatures().iterator();
	while (itr.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) itr.next();
		if (obj.eIsSet(sf)) {
			if (sf.isMany() || ignoreSF(sf) || kids.contains(sf))
				continue;
			if (a.getSettingAdapters(sf) != null && a.getSettingAdapters(sf).length > 0)
				continue;
			CodeExpressionRef newExpr = GenerateAttribute(sf, bean, null, updateDoc);
			String src = newExpr.getContent();
			if (src == null)
				throw new CodeGenException("Could not Generate Source"); //$NON-NLS-1$
			JavaVEPlugin.log("\tAdding: " + src, MsgLogger.LOG_FINE); //$NON-NLS-1$
			if (!updateDoc)
				newExpr.setOffset(buf.length());
			buf.append(src);
		}
	}
}

/**
 *  Create a NOop expression reference for the initializationn expression..
 */
CodeExpressionRef createInitExpression(BeanMethodTemplate mt,BeanPart bean) {
	ExpressionRefFactory eg = new ExpressionRefFactory(bean,null) ;
	return eg.createInitExpression(mt) ;
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
		JavaVEPlugin.log ("No Source Generated for "+bean.getUniqueName()+"("+sf+")", MsgLogger.LOG_WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return ;
	}	
    
    // The generator assume that offsets will be update once we insert the exp. to the doc.
    // We are going to set the offset explicitly
    CodeMethodRef mref = newExpr.getMethod() ;
    mref.removeExpressionRef(newExpr) ;
    newExpr.setOffset(buf.length()) ;
    mref.addExpressionRef(newExpr) ;
	buf.append(src) ;		      	
	JavaVEPlugin.log ("\tAttribute source added: "+src, MsgLogger.LOG_FINE) ;	       //$NON-NLS-1$
	
}

protected void setConstractorString (BeanMethodTemplate tp, Object component) {
    if (component instanceof IJavaObjectInstance) {
     // use the init string rather than default template string
        IJavaObjectInstance obj = (IJavaObjectInstance) component ;
        String st = obj.getInitializationString() ;  
// With the new model, a parent is not eContainer() anymore
// Also, this code path will not get executed, as we drop a layout
// as a propert.  This hack is moved to the SimpleAttributeDecoderHelper        
//        CDEHack.fixMe("Temp. until we have a constructor based init support") ; //$NON-NLS-1$
//        int index =  st.indexOf("(,") ; //$NON-NLS-1$
//        if (index>=0) {            
//            EObject parent = (EObject) obj.eContainer() ;
//            BeanPart pBP = fModel.getABean(parent) ;
//            String toAdd = pBP.getSimpleName();
//            StringBuffer s = new StringBuffer (st) ;            
//            s.replace(index, index+2, "("+toAdd+", ") ; //$NON-NLS-1$ //$NON-NLS-2$
//            st = s.toString() ;
//        }
        tp.setBeanConstructorString(st) ;
    }
}

public String generateInLine(CodeMethodRef method,String beanName, List kids) throws CodeGenException {
    
    fMethodRef = method ;    
    freturnType = ((IJavaObjectInstance)fComponent).getJavaType().getQualifiedName() ;
    fName=beanName ;
    
                   
    // Set up a new BeanPart in the decleration Model
    BeanPart bp = fModel.getABean(fName) ;
    if(bp==null)
    	bp = fModel.getABean(BeanDeclModel.constructUniqueName(method,fName));//method.getMethodHandle()+"^"+fName);
 	BeanMethodTemplate template = new BeanMethodTemplate(freturnType,fName,"",fComments) ; //$NON-NLS-1$
    setConstractorString (template, fComponent) ;
 	template.setSeperator(fModel.getLineSeperator());
 	template.setInLineMethod(true) ;
    
   StringBuffer sb = new StringBuffer () ;
    
   CodeExpressionRef initExp = createInitExpression(template,bp) ;
   // Allow the expression sorted to find a nice spot for this one
   initExp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, false); // initExp.setState(initExp.getState()&~initExp.STATE_SRC_LOC_FIXED) ;
   initExp.setOffset(-1) ;
   try {   
       method.updateExpressionOrder() ;
   }
   catch (Throwable e) {
        JavaVEPlugin.log(e, MsgLogger.LOG_SEVERE) ;
        return null ;
   }
   // We may be processing a nested child, 
   // and the method is not in the source yet
   if (method.getMethodHandle() != null)
      initExp.insertContentToDocument(true) ;
   else
      initExp.setState(CodeExpressionRef.STATE_EXP_NOT_PERSISTED, true) ;
            
    appendNewSource(sb,bp,kids,true) ;
//    if (kids != null) {
//    	Iterator itr = kids.iterator() ;
//    	while (itr.hasNext()) {
//    	  BeanPart child = (BeanPart)itr.next() ;
//    	  child.addBackRef(bp) ;    	      	      	  
//    	  GenerateAChild(sb,bp,child,(EStructuralFeature)itr.next()) ;    	  
//      }
//    }    
    sb.append(template.getPostfix()) ;
    
    return (sb.toString()) ;
}

/**
 * @deprecated
 */
public String generate(CodeMethodRef method,String methodName,String beanName, List kids) throws CodeGenException {
	  
    fMethodRef = method ;    
    freturnType = ((IJavaObjectInstance)fComponent).getJavaType().getQualifiedName() ;
    fName=beanName ;
    fMethodName = methodName ;
                   
    // Set up a new BeanPart in the decleration Model
    BeanPart bp = fModel.getABean(fName) ;
 	
    BeanMethodTemplate template = new BeanMethodTemplate(freturnType,fName,fMethodName,fComments) ;
    setConstractorString (template, fComponent) ;
    template.setSeperator(fModel.getLineSeperator()) ;
//    if (((IJavaObjectInstance)fComponent).isImplicit()) {
//    	template.setImplicit(true) ;
//    }
    StringBuffer sb = new StringBuffer () ;
    sb.append (template.getPrefix()) ;
    
    // Some Expressions may have generated by nested, internal children of this bean.
    ArrayList shadowExprList = new ArrayList() ;
    Iterator itr = fMethodRef.getExpressions() ;
    while (itr.hasNext()) 
        shadowExprList.add(itr.next()) ;
    for (int i=0; i<shadowExprList.size(); i++) 
        fMethodRef.removeExpressionRef((CodeExpressionRef)shadowExprList.get(i)) ;
        
    createInitExpression(template,bp) ;
    
    // Add back the internal expressions, if any 
    for (int i=0; i<shadowExprList.size(); i++) {
        CodeExpressionRef exp = (CodeExpressionRef)shadowExprList.get(i) ;
        exp.setOffset(sb.length()) ;
        sb.append(exp.getContent()) ;
        // How else will we have a shadow expression ?
        if (!exp.isStateSet(CodeExpressionRef.STATE_EXP_NOT_PERSISTED)) throw new CodeGenException ("STATE_EXP_NOT_PERSISTED is not set for existing expression") ; //$NON-NLS-1$
        exp.setState(CodeExpressionRef.STATE_EXP_NOT_PERSISTED, false) ;
        fMethodRef.addExpressionRef(exp) ;        
    }
            
    appendNewSource(sb,bp,kids) ;
    if (kids != null) {
    	itr = kids.iterator() ;
    	while (itr.hasNext()) {
    	  BeanPart child = (BeanPart)itr.next() ;
		  EStructuralFeature sf = (EStructuralFeature)itr.next() ;
    	  child.addBackRef(bp,(EReference) sf) ;    	      	      	  
    	  GenerateAChild(sb,bp,child,sf) ;    	  
      }
    }    
    sb.append(template.getPostfix()) ;
    
    return (sb.toString()) ;
        
}

public String generateMethod(CodeMethodRef method,String methodName,String beanName) throws CodeGenException {
	  
    fMethodRef = method ;    
    freturnType = ((IJavaObjectInstance)fComponent).getJavaType().getQualifiedName() ;
    fName=beanName ;
    fMethodName = methodName ;
                   
    // Set up a new BeanPart in the decleration Model
    BeanPart bp = fModel.getABean(fName) ;
 	
    BeanMethodTemplate template = new BeanMethodTemplate(freturnType,fName,fMethodName,fComments) ;
    setConstractorString (template, fComponent) ;
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
        
    createInitExpression(template,bp) ;
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
    
    return (sb.toString()) ;
        
}

public String toString() {
	return super.toString() + fMethodName;
}


}


