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
 *  $RCSfile: ExpressionVisitor.java,v $
 *  $Revision: 1.6 $  $Date: 2004-02-20 00:44:29 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jdt.internal.compiler.ast.*;

import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class ExpressionVisitor extends SourceVisitor {
	
CodeMethodRef             fMethod = null ;
CodeExpressionRef	        fExpression = null ;
	

ExpressionVisitor(CodeMethodRef method,Statement stmt,IBeanDeclModel model,List reTryList) {
	super((ASTNode)stmt,model,reTryList) ;	
	fMethod = method ;
	fExpression = new CodeExpressionRef (stmt,fMethod) ;
//try {	
//System.out.println("---"+fModel.getDocument().get().substring(fMethod.getOffset()+fExpression.getOffset(),fMethod.getOffset()+fExpression.getOffset()+fExpression.getLen())) ;	
//}
//catch (Throwable t) {
//	System.out.println("--- Error on"+this) ;
//}
}

/**
 *  Process a SingleNameReference, e.g., bean.setFoo()
 */
BeanPart  processSingleNameReference (MessageSend stmt) { 
	  // Check to see that the "bean" part is a part we are interested with
	  String name = new String(((SingleNameReference)stmt.receiver).token);
	  
	  BeanPart b = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod,name));//fMethod.getMethodHandle()+"^"+name);
	  if(b==null)
	      b = fModel.getABean(name);	
   	  return b;
}

/**
 *  Process a Reference to a MessageSend, e.g., getFooBean().setFoo()
 */
BeanPart  processRefToMessageSend  (MessageSend stmt) {	   
  	  	// Check to see if the Model already knows about 
  	String selector=null ;
    if (((MessageSend)stmt.receiver).receiver instanceof ThisReference) {
     // VAJ typical pattern, we did not resolved what this method returns yet,
     // postpone the parsing to later
     if (fReTryLater != null) {
     		fReTryLater.add(this) ;
            JavaVEPlugin.log ("\t[Expression] - postponing: "+stmt, Level.FINE) ;        		 //$NON-NLS-1$
       		return null ;
     }
     selector = new String (((MessageSend)stmt.receiver).selector) ;  	  	
    }
    
    if (selector != null)
  	  	   return fModel.getBeanReturned(selector) ;  	  	
  	else
  	       return null ;  	      	  	  		
}

/**
 *  Process a This reference this.setFoo()
 *  if a BeanPart representing the type "this" was not created, 
 *  than create one.
 */
BeanPart  processRefToThis  (MessageSend stmt) {	   
    if (fModel.getABean(BeanPart.THIS_NAME)==null) return null ;
    
	IThisReferenceRule thisRule = (IThisReferenceRule) CodeGenUtil.getEditorStyle(fModel).getRule(IThisReferenceRule.RULE_ID) ;
	if (thisRule != null) {
	   ISourceVisitor override = thisRule.overideThisReferenceVisit(fMethod.getDeclMethod(),stmt,fModel) ;
	   if (override != null) {
			override.visit() ;
			return null ;
	   } 
	   if (thisRule.shouldProcess(fMethod.getDeclMethod(),stmt)) {
	   	  BeanPart bean = null ;
	   	  
	   	  BeanPartFactory bpg = new BeanPartFactory(fModel,null) ;
	   	  bean = bpg.createThisBeanPartIfNeeded(fMethod) ;	   	  
	   	  return bean ;
	   }
	}
	return null ;
}
	
/**
 *  Figure out which BeanPart (if any) this expression is acting on.
 */
protected void processAMessageSend() {
      MessageSend stmt = (MessageSend) fExpression.getExpression() ;
      if (stmt == null) return ;
    
      // This is the bean that is being updated    
  	  BeanPart bean = null ;
  	  // A simple  bean.setFoo() like statement    	
  	  if (stmt.receiver instanceof SingleNameReference)   
   	  	  bean = processSingleNameReference(stmt) ;
  	  //  getFooBean().setFoo() like statement
  	  else if (stmt.receiver instanceof MessageSend)  {
  	  	  bean = processRefToMessageSend(stmt) ;
  	  	  // Check for postponed
  	  	  if (bean == null && fReTryLater != null) return ;  
  	  }
  	  // something like this.setFoo() -- look at the rule base if we should
  	  // process.
  	  else if (stmt.receiver instanceof ThisReference)  
  	  	bean = processRefToThis(stmt) ;
	  	  	  	  	  	  
  	
  	  if (bean != null) {
  	     	fExpression.setBean(bean) ;
   	 		bean.addRefExpression(fExpression) ;
   	 		// NOTE: 1. The expression should not be added to the bean unless is
   	 		//          belongs to the init method (i.e. hit the "new" statement).
   	 		//       2. The method should NOT be made an Init method just because
   	 		//          it hit a MessageSend.
   	 		// bean.getModel().addMethodInitializingABean(fMethod) ;
  	  }
      else {  	     
        JavaVEPlugin.log("\t[Expression] Visitor.processAMessageSend() did not process: "+stmt, Level.FINE) ; //$NON-NLS-1$
      }
}

/**
 * Check to see if a method call, is static.
 */
protected boolean isStaticCall (String resolvedReciever, String selector, int argc)  {
		EClassifier rClass = CodeGenUtil.getMetaClass(resolvedReciever,fModel.getCompositionModel()) ;
		if (rClass != null  && rClass instanceof JavaClassImpl) {
			org.eclipse.jem.java.impl.JavaClassImpl Clazz = (JavaClassImpl) rClass ;
			Iterator itr = Clazz.getPublicMethods().iterator() ;
			while (itr.hasNext()) {
				org.eclipse.jem.java.Method element = (org.eclipse.jem.java.Method) itr.next();
				if (element.getName().equals(selector)) 
				  if (element.isStatic()) 
					if (element.getReturnType()==null) return false ; // must have a return 
					else
	                   // Return type, is part of the parameter lists				
					   if (element.getParameters().size() == argc) 
					      return true  ;
			}
		}
		return false ;
}

/**
 *  Process a Send Message,
 */
protected void processAssignmment() {
	Assignment stmt = (Assignment) fExpression.getExpression();
	BeanPart bean ;
	// TODO Need to deal with ArrayTypeReference etc.	
	if (stmt.lhs instanceof SingleNameReference) {
		String name = new String(((SingleNameReference)stmt.lhs).token);
		bean = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod, name));//fMethod.getMethodHandle()+"^"+name);		
		if(bean==null)
			bean = fModel.getABean(name) ;
		if (bean != null) {
			fExpression.setBean(bean) ;
			boolean initExpr = false ;
			if (stmt.expression instanceof AllocationExpression || 
			   (stmt.expression instanceof CastExpression && ((CastExpression)stmt.expression).expression instanceof AllocationExpression)) {
				// e.g. ivjTitledBorder = new Border()
				// e.g. ivjTitledBorder = (TitledBorder) new Border()
				initExpr = true ;
			}
			else if (stmt.expression instanceof MessageSend || 
			           (stmt.expression instanceof CastExpression && ((CastExpression)stmt.expression).expression instanceof MessageSend)) {
				// e.g., ivjTitledBorder = javax.swing.BorderFactory.createTitledBorder(null , "Dog" , 0 , 0)
				// e.g., ivjTitledBorder = (TitledBorder) javax.swing.BorderFactory.createTitledBorder(null , "Dog" , 0 , 0)
				// Assume the MessageSend is a static that can be resolved by the target VM...
				// need more work here.
				// At this point, make sure this is a static method.
				MessageSend m = (MessageSend) (stmt.expression instanceof MessageSend ? stmt.expression : ((CastExpression)stmt.expression).expression);
				String resolvedReciver = null ;
				if (m.receiver instanceof QualifiedNameReference)
				   resolvedReciver = m.receiver.toString() ;
				else
				   resolvedReciver = fModel.resolve(m.receiver.toString()) ;
			   // TODO This should be in a rule
			   if (isStaticCall(resolvedReciver,new String(m.selector), (m.arguments==null?0:m.arguments.length))) {
			   	  initExpr = true ;
			   }
			}
			if (initExpr) {
			   bean.addInitMethod(fMethod) ;
			   fExpression.setState(CodeExpressionRef.STATE_IN_SYNC, true);
			   fExpression.setState(CodeExpressionRef.STATE_INIT_EXPR, true);
			}
			else {
			  JavaVEPlugin.log ("\t[Expression] Visitor.processAssignmment() did not process: "+stmt, Level.FINE) ; //$NON-NLS-1$
			}
		}		
	}
	else if (stmt.lhs instanceof QualifiedNameReference) {
		// for field access (like gridbag constraints)
		// TODO consider things like this.ivjFoo = new ()"
	    char[][] tokens = ((QualifiedNameReference)stmt.lhs).tokens ;
	    String bName = new String(tokens[tokens.length-2]) ;
	    bean = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod,bName));//fMethod.getMethodHandle()+"^"+bName);	    
	    if(bean==null)
	    	bean = fModel.getABean(bName) ;
	    if (bean != null) {
	      fExpression.setBean(bean) ;
	      bean.addRefExpression(fExpression) ;
//	      bean.getModel().addMethodInitializingABean(fMethod) ;
	    }
	}	
   else {   	 
   	 JavaVEPlugin.log ("\t[Expression] Visitor.processAssignmment() did not process: "+stmt, Level.FINE) ; //$NON-NLS-1$
   }	
}

/**
 * Process a Local Decleration (typically of utility beans like GridBagConstraints)
 */
protected void processDeclarations() {
    
    LocalDeclaration stmt = (LocalDeclaration) fExpression.getExpression() ;   
    if (stmt.initialization instanceof AllocationExpression ||
	    stmt.initialization instanceof ArrayAllocationExpression ||  
    	stmt.initialization instanceof CastExpression || 
    	stmt.initialization instanceof MessageSend) {
    	 String name = new String(stmt.name);
         
         BeanPart bean = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod, name));  //(fMethod.getMethodHandle()+"^"+name)
         if(bean==null)
         	bean = fModel.getABean(name) ; 
         fExpression.setBean(bean) ;
         bean.addInitMethod(fMethod) ;
         //fExpression.setState(fExpression.getState() | fExpression.STATE_IN_SYNC | fExpression.STATE_NO_OP) ;
         fExpression.setState(CodeExpressionRef.STATE_IN_SYNC, true);
         fExpression.setState(CodeExpressionRef.STATE_NO_MODEL, true);
         fExpression.setState(CodeExpressionRef.STATE_INIT_EXPR, true);         
         bean.addRefExpression(fExpression) ;
		 bean.getModel().addMethodInitializingABean(fMethod) ;			
    }
}	
	
/**
 *  Go for it
 */
public void visit(){
	
	if (fExpression.getExpression() instanceof MessageSend)
	   processAMessageSend () ;
	else if (fExpression.getExpression() instanceof Assignment)
	   processAssignmment () ;
    else if (fExpression.getExpression() instanceof LocalDeclaration)
       processDeclarations() ;
    else
       JavaVEPlugin.log ("\t[Expression] Visitor: *** did not process Expression:"+fVisitedNode, Level.FINE) ; //$NON-NLS-1$
	   
	   
	// Need to fartehr analyze  - see Expression()
	
	
}

}