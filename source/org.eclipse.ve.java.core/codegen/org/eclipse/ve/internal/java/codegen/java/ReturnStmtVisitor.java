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
 *  $RCSfile: ReturnStmtVisitor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */


import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.eval.CodeSnippetReturnStatement;
import org.eclipse.jem.internal.core.MsgLogger;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.codegen.java.rules.IReturnStmtRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;



public class ReturnStmtVisitor extends SourceVisitor {
	
CodeMethodRef             fMethod = null ;
ReturnStatement		  fReturnStmt = null ;
	

ReturnStmtVisitor(CodeMethodRef method,ReturnStatement stmt,IBeanDeclModel model,List reTryList) {
	super((AstNode)stmt,model,reTryList) ;	
	fMethod = method ;
	fReturnStmt = stmt ;
}
	
/**
 *  Process a Return Statement, 
 */
void processAReturnStatement() {
	
	Expression exp = fReturnStmt.expression ;
	
	if (exp instanceof SingleNameReference) {
		String value = new String (((SingleNameReference)exp).token) ;
		String localValue = BeanDeclModel.constructUniqueName(fMethod,value) ;		
		BeanPart bean  ;
		// It is possible that we have a local decleration with the same name of an 
		// instance variable.  If a local is defined, the return method is for it.
		bean = fModel.getABean(localValue) ;
		if (bean == null)
		   bean = fModel.getABean(value) ;
		if (bean != null) {
			bean.addReturnMethod(fMethod) ;
			try {
			  fModel.addMethodReturningABean(new String(fMethod.getDeclMethod().selector),bean.getUniqueName()) ;
			}
			catch (CodeGenException e) { // Should not be here
		      }
		}
	}
	
	
	
} 
 
	
/**
 *  Go for it
 */
public void visit(){
	
	IReturnStmtRule returnRule = (IReturnStmtRule) CodeGenUtil.getEditorStyle(fModel).getRule(IReturnStmtRule.RULE_ID) ;
	if (returnRule != null) {
		ISourceVisitor override = returnRule.overideReturnVisit(fMethod.getDeclMethod(),fReturnStmt,fModel) ;
		if (override != null) {
			override.visit() ;
			return ;
		}
	}
		
	if (!(fReturnStmt instanceof CodeSnippetReturnStatement) && 
		fReturnStmt instanceof ReturnStatement) 
	              processAReturnStatement () ;	
      else
        JavaVEPlugin.log ("... did not process Expression:"+fReturnStmt, MsgLogger.LOG_FINE) ; //$NON-NLS-1$
	
	
}

}


