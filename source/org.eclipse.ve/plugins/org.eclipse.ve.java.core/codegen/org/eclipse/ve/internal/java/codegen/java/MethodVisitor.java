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
 *  $RCSfile: MethodVisitor.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-04 21:36:17 $ 
 */

import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.java.rules.*;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 *  This Visitor will analyze a method or call and overrding JCMMethod Visitor
 */

public class MethodVisitor extends SourceVisitor {
	
	CodeMethodRef fMethod = null ;
	

/**
 *  A bean is declared locally in this method.
 *  At this time, no deferentiation to the variable's scope.
 */	
protected void	processLocalDecleration (VariableDeclarationStatement stmt) {
	IMethodVariableRule methodVarRule = (IMethodVariableRule) CodeGenUtil.getEditorStyle(fModel).getRule(IMethodVariableRule.RULE_ID) ;
	if (methodVarRule!=null && methodVarRule.ignoreVariable(stmt,fModel.getResolver(),fModel.getCompositionModel())) return ;
	
	BeanPart bp = new BeanPart(stmt) ;
	bp.setModel(fModel) ;
	bp.setInstanceVar(false) ;
	bp.addInitMethod(fMethod) ;
	fModel.addBean (bp) ;
	//TODO: deal with multi expression lines
    VariableDeclaration vd = (VariableDeclaration)stmt.fragments().get(0);
    Expression init = vd.getInitializer();
	if (init instanceof ClassInstanceCreation || 
	    init instanceof ArrayCreation ||
	    init instanceof CastExpression || 
	    init instanceof MethodInvocation) {
	    // Decleration and initialization
	    new ExpressionVisitor(fMethod,stmt,fModel,fReTryLater).visit();
	}		
}	
		
/**
 *   Drive processAStatement() for each element.
 */	
protected  void  processStatementArray (List statements) throws CodeGenException {
	if (statements == null || statements.size()==0) {
		// Typically a "new" allocation statement will initialize a method, so would a this.setFoo().  But
		// and empty Initialize() method need some help.
		IThisReferenceRule thisRule = (IThisReferenceRule) CodeGenUtil.getEditorStyle(fModel).getRule(IThisReferenceRule.RULE_ID) ;
        String [] mNameModifier = thisRule.getThisInitMethodName(fModel.getClassHierarchy()) ;
		if (fMethod.getMethodName().equals(mNameModifier[0])) {
			BeanPart bean = fModel.getABean(BeanPart.THIS_NAME) ;
			if (bean != null)
			   bean.addInitMethod(fMethod) ;
			
		}
		return ;
	}
	
      for (int i=0; i<statements.size(); i++)
	   processAStatement((Statement)statements.get(i)) ;	
}		
		
/**
 *   Re-drive a statement block
 */	
protected  void  processBlockStatement(Block stmt) throws CodeGenException {	
	processStatementArray(stmt.statements()) ;
} 

/**
 *   Re-drive a Sync block
 */	
protected  void  processSynchStatement(SynchronizedStatement stmt)  throws CodeGenException  {	
	processBlockStatement(stmt.getBody()) ;
} 
/**
 *   Re-drive a Sync block
 */	
protected  void  processTryStatement(TryStatement stmt)  throws CodeGenException  {	
	if(stmt.getBody()!=null)
		processBlockStatement(stmt.getBody()) ;
	if(stmt.getFinally()!=null)
		processBlockStatement(stmt.getFinally());
} 

 


		
/**
 *  Use the rule base engine to parse an IF statement
 */	
protected void	processIFStatement(IfStatement stmt) throws CodeGenException{
	IIfStatementRule ifRule = (IIfStatementRule) CodeGenUtil.getEditorStyle(fModel).getRule(IIfStatementRule.RULE_ID) ;
	if (ifRule == null) throw new CodeGenException("No IIfStatementRule") ; //$NON-NLS-1$
	
	ISourceVisitor overideIf = ifRule.overideIFVisit(fMethod.getDeclMethod(),stmt,fModel) ;
	if (overideIf != null) {
		overideIf.visit() ;
		return ;
	}
	
	
	int processPattern = ifRule.whichPartToProcess(fMethod.getDeclMethod(),stmt) ;
	if ((processPattern & IIfStatementRule.PROCESS_IF) > 0) 
         processAStatement(stmt.getThenStatement()) ;	
	
	if ((processPattern & IIfStatementRule.PROCESS_ELSE) > 0) 
         processAStatement(stmt.getElseStatement()) ;
		
}


/**
 *   Start pilling off the statements
 */	
protected void	processAStatement(Statement stmt) throws CodeGenException {
		
	// Local Variable Decleration
      if (stmt instanceof VariableDeclarationStatement) 
          processLocalDecleration((VariableDeclarationStatement)stmt) ;
      // Block Statement
      else if (stmt instanceof Block)
          processBlockStatement((Block)stmt) ;
      // Try Block
      else if (stmt instanceof TryStatement)
          processTryStatement ((TryStatement)stmt) ;
      // If Statement
      else if (stmt instanceof IfStatement) 
          processIFStatement((IfStatement)stmt) ;
      // Synchronized
      else if (stmt instanceof SynchronizedStatement) 
          processSynchStatement ((SynchronizedStatement)stmt) ;
      else if (stmt instanceof ReturnStatement)
          new ReturnStmtVisitor(fMethod,(ReturnStatement)stmt,fModel,fReTryLater).visit();

      // Handle an Expression          
      else if (stmt instanceof ExpressionStatement) 
          new ExpressionVisitor(fMethod,(ExpressionStatement)stmt,fModel,fReTryLater).visit();
      else
         JavaVEPlugin.log ("\t[JCMMethod] Visitor did not processAStatement : "+stmt, Level.FINE) ; //$NON-NLS-1$
}	
	
/**
 *
 */		
public MethodVisitor (MethodDeclaration node, IBeanDeclModel model,List reTryList,CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content) {
	super(node,model,reTryList) ;	
	fMethod = new CodeMethodRef (node,typeRef,methodHandle,range,content) ;
}

public MethodVisitor (MethodDeclaration node, IBeanDeclModel model,List reTryList,CodeMethodRef m) {
	super(node,model,reTryList) ;	
	fMethod = m ;
}

/**
 * Go for it
 */
 
public void visit() {
	
	
	// Check to see if we have a special JCMMethod Visitor to use instead this one.
	IOverideMethodVisitRule overideRule = (IOverideMethodVisitRule) CodeGenUtil.getEditorStyle(fModel).getRule(IOverideMethodVisitRule.RULE_ID) ;
	if (overideRule != null) {
		ISourceVisitor overideVisitor = overideRule.overideMethodVisit(fMethod.getDeclMethod(),fModel) ;
		if (overideVisitor != null) {
		   overideVisitor.visit() ;
		   return ;
		}
	}	
	
	// A temporary limitation so that if one add local JFrame for example,
	// and set its content pane with an instance JPanel, we will loose it as
	// the JFrame is local and would not put in the FF
	if (fMethod.getDeclMethod().isConstructor()) {
		JavaVEPlugin.log("Skiping Custructor parsing: "+fMethod.getMethodName()) ; //$NON-NLS-1$
		return ;
	}	
	
	try {
	  processStatementArray(fMethod.getDeclMethod().getBody().statements()) ;		
	}
	catch (CodeGenException  e) {
		// Will have to pass it on later on
		JavaVEPlugin.log (e, Level.WARNING) ;
	}
}

public String toString() {
	return "MethodVisitor("+fMethod.getMethodName()+")" ; //$NON-NLS-1$ //$NON-NLS-2$
}

public void setInitMethodFor(BeanPart bp) {
        bp.addInitMethod(fMethod) ;	
}

}