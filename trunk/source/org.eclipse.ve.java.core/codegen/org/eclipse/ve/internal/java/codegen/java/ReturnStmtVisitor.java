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
 *  $Revision: 1.10 $  $Date: 2005-04-09 01:19:15 $ 
 */


import java.text.MessageFormat;
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
			BeanPart bean = CodeGenUtil.getBeanPart(fModel, value, fMethod, fReturnStmt.getStartPosition()-fMethod.getOffset());
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
	getProgressMonitor().subTask(MessageFormat.format(CodeGenJavaMessages.getString("ReturnStmtVisitor.TypeMethodExpression"), new Object[]{fMethod.getTypeRef().getSimpleName(), fMethod.getMethodName(), fReturnStmt.toString()})); //$NON-NLS-1$
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


