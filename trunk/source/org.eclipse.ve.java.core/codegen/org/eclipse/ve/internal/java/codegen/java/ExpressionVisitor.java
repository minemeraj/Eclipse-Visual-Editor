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
 *  $Revision: 1.8 $  $Date: 2004-03-12 18:26:31 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.java.impl.JavaClassImpl;

import org.eclipse.ve.internal.java.codegen.java.rules.IThisReferenceRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


public class ExpressionVisitor extends SourceVisitor {
	
CodeMethodRef		fMethod = null ;
CodeExpressionRef	fExpression = null ;
	

ExpressionVisitor(CodeMethodRef method, Statement stmt,IBeanDeclModel model,List reTryList) {
	super((ASTNode)stmt,model,reTryList) ;	
	fMethod = method ;
	try {
	fExpression = new CodeExpressionRef (stmt,fMethod) ;
	} catch (Exception e) {
		e.printStackTrace();
	}
}

/**
 *  Process a SingleNameReference, e.g., bean.setFoo()
 */
BeanPart  processSingleNameReference (MethodInvocation stmt) { 
	  // Check to see that the "bean" part is a part we are interested with
	  String name = ((SimpleName)stmt.getExpression()).getIdentifier();	  
	  BeanPart b = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod,name));
	  if(b==null)
	      b = fModel.getABean(name);	
   	  return b;
}

/**
 *  Process a Reference to a MessageSend, e.g., getFooBean().setFoo()
 */
BeanPart  processRefToMessageSend  (MethodInvocation stmt) {	   
  	  	// Check to see if the Model already knows about 
		String selector = ((MethodInvocation)stmt.getExpression()).getName().getIdentifier();
//		if (stmt.getExpression() instanceof ThisExpression) {
//			// VAJ typical pattern, we did not resolved what this method returns yet,
//			// postpone the parsing to later
//			if (fReTryLater != null) {
//				fReTryLater.add(this);
//				JavaVEPlugin.log("\t[Expression] - postponing: " + stmt, Level.FINE); //$NON-NLS-1$
//				return null;
//			}
//			selector = new String(((MethodInvocation) stmt.getExpression()).getName().getIdentifier());
//		}
//
		BeanPart b = null;
		if (selector != null) {
			b = fModel.getBeanReturned(selector);
			if (b==null && fReTryLater!=null) { 
				fReTryLater.add(this);
			    JavaVEPlugin.log("\t[Expression] - postponing: " + stmt, Level.FINE); //$NON-NLS-1$
			}
		}
		return b;
		  	      	  	  		
}

/**
 *  Process a This reference this.setFoo()
 *  if a BeanPart representing the type "this" was not created, 
 *  than create one.
 */
BeanPart  processRefToThis  (MethodInvocation stmt) {	
	BeanPart bean = fModel.getABean(BeanPart.THIS_NAME);

	// This Bean Part should already be created at this point
	if (bean != null) {
		IThisReferenceRule thisRule = (IThisReferenceRule) CodeGenUtil.getEditorStyle(fModel).getRule(IThisReferenceRule.RULE_ID);
		if (thisRule != null) {
			ISourceVisitor override = thisRule.overideThisReferenceVisit(fMethod.getDeclMethod(), stmt, fModel);
			if (override != null) {
				override.visit();
				return null;
			}
			if (thisRule.shouldProcess(fMethod.getDeclMethod(),stmt,fModel.getClassHierarchy())) {
			  bean.addInitMethod(fMethod);
			  return bean;
			}
		}
	}
	return null;
}
	
/**
 *  Figure out which BeanPart (if any) this expression is acting on.
 */
protected void processAMessageSend() {
      MethodInvocation stmt = (MethodInvocation) ((ExpressionStatement)fExpression.getExprStmt()).getExpression() ;
      if (stmt == null) return ;
    
      // This is the bean that is being updated    
  	  BeanPart bean = null ;
  	  // A simple  bean.setFoo() like statement    	
  	  if (stmt.getExpression() instanceof SimpleName)   
   	  	  bean = processSingleNameReference(stmt) ;
  	  //  getFooBean().setFoo() like statement
  	  else if (stmt.getExpression() instanceof MethodInvocation)  {
  	  	  bean = processRefToMessageSend(stmt) ;
  	  	  // Check for postponed
  	  	  if (bean == null && fReTryLater != null) return ;  
  	  }
  	  // something like this.setFoo() -- look at the rule base if we should
  	  // process.
  	  else if (stmt.getExpression()==null || stmt.getExpression() instanceof ThisExpression)  
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
	Assignment stmt = (Assignment) ((ExpressionStatement)fExpression.getExprStmt()).getExpression();
	BeanPart bean ;
	// TODO Need to deal with ArrayTypeReference etc.	
	if (stmt.getLeftHandSide() instanceof SimpleName) {
		String name = ((SimpleName)stmt.getLeftHandSide()).getIdentifier();
		bean = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod, name));//fMethod.getMethodHandle()+"^"+name);		
		if(bean==null)
			bean = fModel.getABean(name) ;
		if (bean != null) {
			fExpression.setBean(bean) ;
			boolean initExpr = false ;
			if (stmt.getRightHandSide() instanceof ClassInstanceCreation || 
			   (stmt.getRightHandSide() instanceof CastExpression && ((CastExpression)stmt.getRightHandSide()).getExpression() instanceof ClassInstanceCreation)) {
				// e.g. ivjTitledBorder = new Border()
				// e.g. ivjTitledBorder = (TitledBorder) new Border()
				initExpr = true ;
			}
			else if (stmt.getRightHandSide() instanceof MethodInvocation || 
			         (stmt.getRightHandSide() instanceof CastExpression && ((CastExpression)stmt.getRightHandSide()).getExpression() instanceof MethodInvocation)) {
				// e.g., ivjTitledBorder = javax.swing.BorderFactory.createTitledBorder(null , "Dog" , 0 , 0)
				// e.g., ivjTitledBorder = (TitledBorder) javax.swing.BorderFactory.createTitledBorder(null , "Dog" , 0 , 0)
				// Assume the MessageSend is a static that can be resolved by the target VM...
				// need more work here.
				// At this point, make sure this is a static method.
				MethodInvocation m = (MethodInvocation) (stmt.getRightHandSide() instanceof MethodInvocation ? stmt.getRightHandSide() : ((CastExpression)stmt.getRightHandSide()).getExpression());
				if (m.getExpression() instanceof Name) {
				String resolvedReciver = CodeGenUtil.resolve((Name)m.getExpression(), fModel);
			   // TODO This should be in a rule: parse methodInvocation Initialization
			   if (isStaticCall(resolvedReciver,m.getName().getIdentifier(), (m.arguments()==null?0:m.arguments().size()))) {
			   	  initExpr = true ;
			   }
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
	else if (stmt.getLeftHandSide() instanceof QualifiedName) {
		// for field access (like gridbag constraints)
		
	    //char[][] tokens = ((QualifiedNameReference)stmt.lhs).tokens ;
	    String bName =   ((QualifiedName)stmt.getLeftHandSide()).getQualifier().toString();    
	    bean = fModel.getABean(BeanDeclModel.constructUniqueName(fMethod,bName));//fMethod.getMethodHandle()+"^"+bName);	    
	    if(bean==null)
	    	bean = fModel.getABean(bName) ;
	    if (bean != null) {
	      fExpression.setBean(bean) ;
	      bean.addRefExpression(fExpression) ;
	      bean.getModel().addMethodInitializingABean(fMethod) ;
	    }
	}	
   else {   	 
   	 JavaVEPlugin.log ("\t[Expression] Visitor.processAssignmment() did not process: "+stmt, Level.FINE) ; //$NON-NLS-1$
   }	
}

/**
 * Process a Local Decleration (typically of utility beans like GridBagConstraints)
 */
protected void processLocalDeclarations() {
    
	VariableDeclarationStatement stmt = (VariableDeclarationStatement) fExpression.getExprStmt() ;
	//TODO: support multi variables per line
	VariableDeclaration dec = (VariableDeclaration)stmt.fragments().get(0);
    if (dec.getInitializer() instanceof ClassInstanceCreation ||
	    dec.getInitializer() instanceof ArrayCreation ||  
    	dec.getInitializer() instanceof CastExpression || 
    	dec.getInitializer() instanceof MethodInvocation) {
    	
    	 String name = dec.getName().getIdentifier();
         
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
	
	if (fExpression.getExprStmt() instanceof ExpressionStatement) {
			Expression exp = ((ExpressionStatement) fExpression.getExprStmt()).getExpression();
			if (exp instanceof MethodInvocation)
				processAMessageSend();
			else if (exp instanceof Assignment)
				processAssignmment();
			else
				JavaVEPlugin.log("\t[Expression] Visitor: *** did not process Expression:" + fVisitedNode, Level.FINE); //$NON-NLS-1$
	} else 
		if (fExpression.getExprStmt() instanceof VariableDeclarationStatement)
			processLocalDeclarations();
		else
			JavaVEPlugin.log("\t[Expression] Visitor: *** did not process Expression:" + fVisitedNode, Level.FINE); //$NON-NLS-1$
	   
	   
	// Need to fartehr analyze  - see Expression()
	
	
}

}