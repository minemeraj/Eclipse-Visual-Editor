/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventMethodCallBackVisitor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-05 23:18:38 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @author Gili Mendel
 *
 */
public class EventMethodCallBackVisitor extends MethodVisitor {


/**
 *
 */		
public EventMethodCallBackVisitor (MethodDeclaration node, IBeanDeclModel model,CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content) {
	super(node,model,new ArrayList(), typeRef, methodHandle, range, content) ;	
}

/**
 *   Drive processAStatement() for each element.
 */	
protected  void  processStatementArray (Statement[] statements) throws CodeGenException {
	  if (statements == null) return ;
	  for (int i=0; i<statements.length; i++)
	     processAStatement(statements[i]) ;	
}	

/**
 *   Start pilling off the statements
 */	
protected void	processAStatement(Statement stmt) throws CodeGenException {
		
      if (stmt instanceof IfStatement) 
          processIFStatement((IfStatement)stmt) ;
}	


protected BeanPart getBeanPart(Expression exp) {
	if (exp instanceof MethodInvocation) {
		if (((MethodInvocation)exp).getExpression() instanceof ThisExpression) {
			String getter = ((MethodInvocation)exp).getName().getIdentifier();
			return  fModel.getBeanReturned(getter) ;
		}
	}
	else if (exp instanceof ThisExpression)
	         return fModel.getABean(BeanPart.THIS_NAME) ;
	else if (exp instanceof SimpleName)
	         return fModel.getABean(((SimpleName)exp).getIdentifier()) ;
    	         
	return null ;
}


protected boolean processCondition(Expression condition, IfStatement stmt) {

	boolean result = false;
	if (condition instanceof InfixExpression) {
		InfixExpression ee = (InfixExpression) condition;
		if (ee.getOperator().equals(InfixExpression.Operator.EQUALS)) {
			if (ee.getLeftOperand() instanceof MethodInvocation) {
				String selector = ((MethodInvocation) ee.getLeftOperand()).getName().getIdentifier();
				if (selector.equals("getSource")) { //$NON-NLS-1$
					// A e.getSource() method call on an if statement, check the target
					BeanPart bp = getBeanPart(ee.getRightOperand());
					if (bp != null) {
						new EventCallBackExpressionVisitor(bp, fMethod, stmt, fModel).visit();
						return true;
					}
				}
			}
		} else if (ee.getLeftOperand() instanceof InfixExpression) {
			//evt.getSource()==Empty.this.getCompoundInterest1()&&(evt.getPropertyName().equals("principalAmount"))
			return processCondition(ee.getLeftOperand(), stmt);
		}
	}
	return result;
}

/**
 *  Use the rule base engine to parse an IF statement
 */	
protected void	processIFStatement(IfStatement stmt) throws CodeGenException{
	
	if (stmt.getExpression() instanceof InfixExpression) 
	   processCondition(stmt.getExpression(),stmt) ;
}
	

}
