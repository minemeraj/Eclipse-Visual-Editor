/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EventMethodCallBackVisitor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.codegen.java.rules.IVisitorFactoryRule;
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
public void initialize (MethodDeclaration node, IBeanDeclModel model,CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content, IVisitorFactoryRule visitorFactory) {
	super.initialize(node,model,new ArrayList(), typeRef, methodHandle, range, content, visitorFactory) ;	
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
						EventCallBackExpressionVisitor v = visitorFactory.getEventCallBackExpressionVisitor();
						v.initialize(bp, fMethod, stmt, fModel);
						v.setProgressMonitor(getProgressMonitor());
						v.visit();
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
