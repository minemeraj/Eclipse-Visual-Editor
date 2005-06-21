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
 *  $RCSfile: MethodVisitor.java,v $
 *  $Revision: 1.17 $  $Date: 2005-06-21 22:15:43 $ 
 */

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.java.rules.*;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 *  This Visitor will analyze a method or call and overrding JCMMethod Visitor
 */

public class MethodVisitor extends SourceVisitor {
	
	protected CodeMethodRef fMethod = null ;
	IVisitorFactoryRule visitorFactory = null;
	
	/**
	 *
	 */		
	public void initialize (MethodDeclaration node, IBeanDeclModel model,List reTryList,CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content, IVisitorFactoryRule visitorFactory) {
		super.initialize(node,model,reTryList) ;	
		fMethod = new CodeMethodRef (node,typeRef,methodHandle,range,content) ;
		this.visitorFactory = visitorFactory;
	}

	public void initialize (MethodDeclaration node, IBeanDeclModel model,List reTryList,CodeMethodRef m, IVisitorFactoryRule visitorFactory) {
		super.initialize(node,model,reTryList) ;	
		fMethod = m ;
		this.visitorFactory = visitorFactory;
	}


/**
 *  A bean is declared locally in this method.
 *  At this time, no deferentiation to the variable's scope.
 */	
protected void	processLocalDecleration (VariableDeclarationStatement stmt) {
	IMethodVariableRule methodVarRule = (IMethodVariableRule) CodeGenUtil.getEditorStyle(fModel).getRule(IMethodVariableRule.RULE_ID) ;
	if (methodVarRule!=null && methodVarRule.ignoreVariable(stmt,fModel.getResolver(),fModel.getCompositionModel())) return ;
	
	BeanPartDecleration decl = new BeanPartDecleration(stmt);
	decl.setDeclaringMethod(fMethod);
	if (fModel.getModelDecleration(decl)!=null)
		decl = fModel.getModelDecleration(decl); // reuse the existing mone
	BeanPart bp = new BeanPart(decl) ;
	bp.setModel(fModel) ;
	decl.setDeclaringMethod(fMethod); 
	bp.addInitMethod(fMethod) ;
	fModel.addBean (bp) ;
	//TODO: deal with multi expression lines
    VariableDeclaration vd = (VariableDeclaration)stmt.fragments().get(0);
    Expression init = vd.getInitializer();
	if (init instanceof ClassInstanceCreation || 
	    init instanceof ArrayCreation ||
	    init instanceof CastExpression || 
	    init instanceof MethodInvocation || 
	    init instanceof StringLiteral) {
	    // Decleration and initialization
		ExpressionVisitor v = visitorFactory.getExpressionVisitor();
		v.initialize(fMethod,stmt,fModel,fReTryLater);
		v.setProgressMonitor(getProgressMonitor());
		v.visit();
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
      else if (stmt instanceof ReturnStatement){
      	ReturnStmtVisitor visitor = visitorFactory.getReturnStmtVisitor();
		visitor.initialize(fMethod,(ReturnStatement)stmt,fModel,fReTryLater);
      	visitor.setProgressMonitor(getProgressMonitor());
      	visitor.visit();
      }

      // Handle an Expression          
      else if (stmt instanceof ExpressionStatement) {
      	ExpressionVisitor v = visitorFactory.getExpressionVisitor();
		v.initialize(fMethod,stmt,fModel,fReTryLater);
      	v.setProgressMonitor(getProgressMonitor());
      	v.visit();
      }else if (JavaVEPlugin.isLoggingLevel(Level.FINE))
         JavaVEPlugin.log ("\t[JCMMethod] Visitor did not processAStatement : "+stmt, Level.FINE) ; //$NON-NLS-1$
}	
/**
 * Go for it
 */
 
public void visit() {
	
	getProgressMonitor().subTask(MessageFormat.format(CodeGenJavaMessages.MethodVisitor_TypeMethod, new Object[]{fMethod.getTypeRef().getSimpleName(),fMethod.getMethodName()})); 

	// A temporary limitation so that if one add local JFrame for example,
	// and set its content pane with an instance JPanel, we will loose it as
	// the JFrame is local and would not put in the FF
	if (fMethod.getDeclMethod().isConstructor()) {
		if (JavaVEPlugin.isLoggingLevel(Level.FINEST))
			JavaVEPlugin.log("Skiping Custructor parsing: "+fMethod.getMethodName()) ; //$NON-NLS-1$
		return ;
	}	
	
	try {
	  Block body = fMethod.getDeclMethod().getBody();
	  if (body!=null && body.statements()!=null && body.statements().size()>0)
	       processStatementArray(fMethod.getDeclMethod().getBody().statements()) ;		
	}
	catch (CodeGenException  e) {
		// Will have to pass it on later on
		JavaVEPlugin.log (e, Level.WARNING) ;
	}
	getProgressMonitor().worked(100);
}

public String toString() {
	return "MethodVisitor("+fMethod.getMethodName()+")" ; //$NON-NLS-1$ //$NON-NLS-2$
}

public void setInitMethodFor(BeanPart bp) {
        bp.addInitMethod(fMethod) ;	
}

}
