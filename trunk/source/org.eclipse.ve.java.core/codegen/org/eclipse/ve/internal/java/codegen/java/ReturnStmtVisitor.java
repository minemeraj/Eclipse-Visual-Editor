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
 *  $RCSfile: ReturnStmtVisitor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-03-30 17:34:23 $ 
 */


import java.util.List;

import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.codegen.java.rules.IReturnStmtRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;



public class ReturnStmtVisitor extends SourceVisitor {
	
CodeMethodRef		fMethod = null ;
ReturnStatement		fReturnStmt = null ;
	

public void initialize(CodeMethodRef method,ReturnStatement stmt,IBeanDeclModel model,List reTryList) {
	super.initialize(stmt,model,reTryList) ;	
	fMethod = method ;
	fReturnStmt = stmt ;
}
	
/**
 *  Process a Return Statement, 
 */
void processAReturnStatement() {
	
	Expression exp = fReturnStmt.getExpression();

		if (exp instanceof SimpleName) {
			String value = ((SimpleName) exp).getIdentifier();
			String localValue = BeanDeclModel.constructUniqueName(fMethod, value);
			BeanPart bean;
			// It is possible that we have a local decleration with the same name of an
			// instance variable. If a local is defined, the return method is for it.
			bean = fModel.getABean(localValue);
			if (bean == null)
				bean = fModel.getABean(value);
			if (bean != null) {
				bean.addReturnMethod(fMethod);
				try {
					fModel.addMethodReturningABean(fMethod.getDeclMethod().getName().getIdentifier(), bean.getUniqueName());
				} catch (CodeGenException e) { // Should not be here
				}
			}
		}
} 
 
	
/**
 *  Go for it
 */
public void visit(){
	getProgressMonitor().subTask(fMethod.getTypeRef().getSimpleName()+" : "+fMethod.getMethodName()+"()"+" : "+fReturnStmt.toString());
	IReturnStmtRule returnRule = (IReturnStmtRule) CodeGenUtil.getEditorStyle(fModel).getRule(IReturnStmtRule.RULE_ID) ;
	if (returnRule != null) {
		ISourceVisitor override = returnRule.overideReturnVisit(fMethod.getDeclMethod(),fReturnStmt,fModel) ;
		if (override != null) {
			override.visit() ;
			return ;
		}
	}
		
	processAReturnStatement () ;	
    
	
}

}


