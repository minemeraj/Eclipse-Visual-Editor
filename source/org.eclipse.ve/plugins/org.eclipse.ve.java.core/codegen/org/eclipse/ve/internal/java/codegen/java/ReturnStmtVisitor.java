/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $Revision: 1.13 $  $Date: 2006-04-06 15:56:40 $ 
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

		String instanceName = null;
		if (exp.getNodeType() == ASTNode.SIMPLE_NAME) {
			instanceName = ((SimpleName) exp).getIdentifier();
		} else if (exp.getNodeType() == ASTNode.FIELD_ACCESS) {
			FieldAccess fa = (FieldAccess) exp;
			if (fa.getExpression() != null && fa.getExpression().getNodeType() == ASTNode.THIS_EXPRESSION) {
				// We have a this.fieldname reference on the return.
				instanceName = fa.getName().getIdentifier();
			}
		}
		if (instanceName != null) {
			BeanPart bean = CodeGenUtil.getBeanPart(fModel, instanceName, fMethod, fReturnStmt.getStartPosition() - fMethod.getOffset());
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
	getProgressMonitor().subTask(MessageFormat.format(CodeGenJavaMessages.ReturnStmtVisitor_TypeMethodExpression, new Object[]{fMethod.getTypeRef().getSimpleName(), fMethod.getMethodName(), fReturnStmt.toString()})); 
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


