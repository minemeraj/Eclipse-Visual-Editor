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
 *  $RCSfile: MethodAnalyzer.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.*;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.*;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.*;

/**
 * This is used in the pull down save approach, and should be used for testing purpuses
 * @deprecated
 */
public class MethodAnalyzer {
	
protected	IMethod           fMethod ;	
protected   IBeanDeclModel    fModel ;
protected   String            fSource  ;
protected   CodeMethodRef     fMethodRef ;
protected   String            flastFiller ;

	
public MethodAnalyzer (IMethod method, IBeanDeclModel model) {
	fModel = model ;
	fMethod = method ;
	if (fMethod != null)
	   try {
	     fSource = method.getSource() ;
	   } catch (Exception e) {
	   	 JavaVEPlugin.log("MethodAnalyzer("+method.getElementName()+"): "+e, MsgLogger.LOG_WARNING) ; //$NON-NLS-1$ //$NON-NLS-2$
	   	 JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
	   } 
	   
}


/**
 *   Find the next siebling of the method
 */
protected IMethod nextSiebling() throws JavaModelException {
	IMethod[] methods = fMethod.getDeclaringType().getMethods() ;
	for (int i=0; i<methods.length; i++) {
		if (methods[i].equals(fMethod))
		  if (i+1>=methods.length) return null ;
		  else return methods[i+1] ;
	}
	return null ;	
}
/**
 *  
 */
protected boolean hasContentChanged() {
	boolean result ;
	try {
       result = (!fMethodRef.getContent().equals(fMethod.getSource())) ;	
//       if (result) 
//		System.out.println("Content Changed:\n**Before\n"+fMethodRef.getContent()+"\n** After\n"+fMethod.getSource()) ;
	}
	catch (JavaModelException e) { return false; }
	
	return (result) ;
}

/**
 *
 */
protected void updateMethodContent(String newContent) throws CodeGenException {
	
	  // The compilation content will not be updated at this time, as to not shift
	  // offsets for follow on analyzors
      fMethodRef.setContent(newContent) ;        
}
/**
 *  Delete the Expression from the Bean Decleration Model
 */
protected void removeDeletedExpressions(Vector list) {
	for (int i=0; i<list.size(); i++) {		
		fMethodRef.removeExpressionRef((CodeExpressionRef)list.elementAt(i)) ;		
		((CodeExpressionRef)list.elementAt(i)).getBean().removeRefExpression((CodeExpressionRef)list.elementAt(i)) ;
	}	
}

/**
 *  Reflect changes to existing source and record which beans have been worked on.
 *  @return offset last expression processed
 */
protected int reflectExistingSource(StringBuffer buf,Vector impactedBeans) throws CodeGenException {
	
	int left = 0, right = -1 ;  // Keep track where are we in the old source
	// Note the expressions are ordered by their offset		
	Iterator itr = fMethodRef.getExpressions() ;	
	Vector deleteCandidate = new Vector() ;
	
	if (itr.hasNext()) {      
		while (itr.hasNext()) {
		  CodeExpressionRef exp = (CodeExpressionRef) itr.next() ;
              // Copy source which has nothing to do with  bean expression
              if (!impactedBeans.contains(exp.getBean()))
                  impactedBeans.add(exp.getBean()) ;
              right = exp.getOffset() ;
              if (right < left)  throw new CodeGenException ("Bad expression offset") ; //$NON-NLS-1$
              if (right-left>0)
                buf.append(fSource.substring(left,right));
              left=right+exp.getLen() ; 
              // Get the latest expression 
String pc = exp.getContent() ;              
              exp.refreshFromComposition() ;
              exp.setOffset(buf.length()) ;
              if ((!exp.isAnyStateSet()) || exp.isStateSet(CodeExpressionRef.STATE_NOT_EXISTANT)) { //(exp.getState() == exp.STATE_NOT_EXISTANT) {
            	// Skip the expression
            	org.eclipse.ve.internal.cde.core.CDEHack.fixMe("Need to deal with comments associated with statement") ; //$NON-NLS-1$
            	deleteCandidate.add(exp) ;
            	JavaVEPlugin.log("\tDeleting: "+exp, MsgLogger.LOG_FINE) ; //$NON-NLS-1$
            	// Advance the left marker to the start of next expression
              }
              else 
                   if ((exp.isStateSet(CodeExpressionRef.STATE_IN_SYNC)) && //((exp.getState() & exp.STATE_IN_SYNC)  >  0 && 
                       (!exp.isStateSet(CodeExpressionRef.STATE_IMPLICIT))) { //(exp.getState() & exp.STATE_IMPLICIT) == 0) {
            	// Get the latest expression
            	buf.append(exp.getContent());            	
if (pc != null && !pc.equals(exp.getContent())) {
JavaVEPlugin.log("\tUpdating: "+pc+" -> "+exp.getContent(), MsgLogger.LOG_FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
}
            	org.eclipse.ve.internal.cde.core.CDEHack.fixMe("clean this up") ; //$NON-NLS-1$
            	flastFiller = exp.getFillerContent() ;
              }            
		}
	}
	removeDeletedExpressions(deleteCandidate) ;
	
	if (flastFiller == null) flastFiller = "" ; //$NON-NLS-1$
	return left ;
}

/**
 *  Create a new Model element and a corresponding Java Source
 */
protected String GenerateAttribute(StringBuffer buf,EStructuralFeature sf,BeanPart bean,int Offset, String filler) throws CodeGenException {
	CodeExpressionRef exp = new CodeExpressionRef(fMethodRef,bean) ;
	exp.setState(CodeExpressionRef.STATE_EXIST, true); //exp.setState(exp.getState() | exp.STATE_EXIST) ;
	exp.setFillerContent(filler) ;
	exp.setOffset(Offset) ;
	return exp.getContent() ;
}

protected boolean isSourceExists(Notifier elm, EObject bean, EStructuralFeature sf) throws CodeGenException{
	boolean result = true ;
	if (elm != null) {
	   if (EcoreUtil.getExistingAdapter(elm,ICodeGenAdapter.JVE_CODE_GEN_TYPE) == null)
	     result = false ;
	}
	else {
		BeanDecoderAdapter a = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bean,ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER)  ;
		if (a == null) throw new CodeGenException("No Adapter"+bean) ; //$NON-NLS-1$
		if (a.getNullValuedAdapter(sf) == null)
		   result = false ;		
	}
    if (elm instanceof EObject && 
		MethodTextGenerator.isNotToBeCodedAttribute(sf,bean))
		result = true ;

    return result ;
}

/**
 *  Look for added attributes
 *  
 */
protected void appendNewAttributes(StringBuffer buf,BeanPart bean,String filler) throws CodeGenException {
	EObject obj = bean.getEObject() ;
	Iterator itr = ((JavaClass)obj.eClass()).getAllProperties().iterator() ;
	while (itr.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) itr.next();
		if (obj.eIsSet(sf)) {
			Object val = obj.eGet(sf);
			if (sf.isMany()) {
				Iterator vItr = ((List) val).iterator();
				while (vItr.hasNext())
					appendNewAttribute(buf, bean, filler, obj, sf, vItr.next());
			} else
				appendNewAttribute(buf, bean, filler, obj, sf, val);
		}
	}	
}


private void appendNewAttribute(StringBuffer buf, BeanPart bean, String filler, EObject obj, EStructuralFeature sf, Object val)
	throws CodeGenException {
	if (val == null || val instanceof Notifier)  {
	   Notifier elm = (Notifier) val;
	   if (!isSourceExists(elm,obj,sf)) {
	    String src = GenerateAttribute(buf,sf,bean,buf.length(),filler) ;
	    if (src == null) throw new CodeGenException ("Could not Generate Source") ;	 //$NON-NLS-1$
	    JavaVEPlugin.log ("\tAdding: "+src, MsgLogger.LOG_FINE) ;	       //$NON-NLS-1$
	    buf.append(src) ;		      	
	   }
	
	}
}

/**
 *  Create a new Model element and a corresponding Java Source
 */
protected String GenerateAddComponent(StringBuffer buf,BeanPart bean,BeanPart child,int Offset,String filler) throws CodeGenException {
	CodeExpressionRef exp = new CodeExpressionRef(fMethodRef,bean) ;
	exp.setState(CodeExpressionRef.STATE_EXIST, true); //exp.setState(exp.getState() | exp.STATE_EXIST) ;
	exp.setArguments(new Object[] {child} ) ;
	exp.setFillerContent(filler) ;
	exp.setOffset(Offset) ;
	return exp.getContent() ;
}


/**
 *  Look for new components added to bean
 *  
 */
protected void appendNewContainerSource(StringBuffer buf,BeanPart bean,String filler) throws CodeGenException, CodeGenInfoMissing {
	
	BeanDecoderAdapter bAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bean.getEObject(),ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER) ;
	if (bAdapter == null) throw new CodeGenInfoMissing("MethodAnalyzer.appendNewContainerSource: "+bean+" - Adapter") ; //$NON-NLS-1$ //$NON-NLS-2$
		
	java.util.List children = CodeGenUtil.getChildrenComponents(bean.getEObject()) ;
	if (children == null) return ;
	Iterator itr = children.iterator() ;
    while (itr.hasNext()) {
    	EObject child = (EObject)itr.next() ;
    	BeanPart bp = bean.getModel().getABean(child) ;
    	if (bp == null) throw new CodeGenInfoMissing("MethodAnalyzer.appendNewContainerSource: "+bean+" - BeanPart") ;    	 //$NON-NLS-1$ //$NON-NLS-2$
    	ICodeGenAdapter cAdapter = bAdapter.getChildAdapter(bp) ;
    	if (cAdapter == null) {
    		// A child that is not reflected in source code    	
		    String src = GenerateAddComponent(buf,bean,bp,buf.length(),filler) ;
		    if (src == null) throw new CodeGenException ("Could not Generate Source") ;	 //$NON-NLS-1$
		    JavaVEPlugin.log ("\tAdding: "+src, MsgLogger.LOG_FINE) ;	       //$NON-NLS-1$
		    buf.append(src) ;	
    	}
    }
}

/**
 *  Look for information not already searilaized
 *  
 */
protected void appendNewSource(StringBuffer buf,BeanPart bean,String filler) throws CodeGenException, CodeGenInfoMissing {
	
	appendNewAttributes(buf,bean,filler) ;
	appendNewContainerSource(buf,bean,filler) ;

}


/**
 *   This function generate the latest JCMMethod source from the composition Model 
 *   It is assumed that the source has not
 *   changed since the last parsing.   If the source has changed, a null will be returned.
 */
public String getCompositionReflectedSource() throws CodeGenException {	
	
	if (fModel == null) throw new CodeGenException (fMethod.getElementName()+" is not in the model") ;	 //$NON-NLS-1$
	fMethodRef = fModel.getMethodInitializingABean(fMethod.getHandleIdentifier()) ;
	
	if (fMethodRef == null) return null ;
	JavaVEPlugin.log("MethodAnalyzer: "+fMethod.getElementName()+"() - thinking ....", MsgLogger.LOG_FINE) ; //$NON-NLS-1$ //$NON-NLS-2$
	// At this point we expect that the source has not changed yet.
//	if (hasContentChanged()) return null ;
	// re-construct in here 
	StringBuffer buf = new StringBuffer (2*fSource.length()) ;    
	// Keep track which beans are involved
	org.eclipse.ve.internal.cde.core.CDEHack.fixMe("this may not be the init method for a bean") ; //$NON-NLS-1$
	Vector beans = new Vector() ; 
	// Update existing source code  
	int srcOffset=reflectExistingSource(buf,beans) ;		
	
	org.eclipse.ve.internal.cde.core.CDEHack.fixMe() ;
	for (int i=0; i<beans.size(); i++) {
		appendNewSource(buf,(BeanPart)beans.elementAt(i),flastFiller) ;
	}
	// Copy the rest of the original source	
	buf.append(fSource.substring(srcOffset));	
      String proposedContent = buf.toString() ;
      if (!proposedContent.equals(fSource)) {
        JavaVEPlugin.log ("                "+fMethod.getElementName()+"() - Updating Source", MsgLogger.LOG_FINE) ;         //$NON-NLS-1$ //$NON-NLS-2$
        updateMethodContent(proposedContent) ;        
      }
      else 
        JavaVEPlugin.log ("                "+fMethod.getElementName()+"() - No Change", MsgLogger.LOG_FINE) ;           //$NON-NLS-1$ //$NON-NLS-2$
    return fMethodRef.getContent() ;	
}


public String toString() {
	return super.toString() + "\n" + fSource ; //$NON-NLS-1$
}


}