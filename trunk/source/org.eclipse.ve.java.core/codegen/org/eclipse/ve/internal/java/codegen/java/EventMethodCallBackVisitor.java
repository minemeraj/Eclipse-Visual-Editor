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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.internal.compiler.ast.*;

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
public EventMethodCallBackVisitor (AbstractMethodDeclaration node, IBeanDeclModel model,CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content) {
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
	if (exp instanceof MessageSend) {
		if (((MessageSend)exp).receiver instanceof ThisReference) {
			String getter = new String (((MessageSend)exp).selector) ;
			return  fModel.getBeanReturned(getter) ;
		}
	}
	else if (exp instanceof QualifiedThisReference)
	         return fModel.getABean(BeanPart.THIS_NAME) ;
	else if (exp instanceof SingleNameReference)
	         return fModel.getABean(exp.toString()) ;
    	         
	return null ;
}


protected boolean processCondition(Expression condition, IfStatement stmt) {

    boolean result = false ;
	if (condition instanceof EqualExpression) {
		EqualExpression ee = (EqualExpression) condition;
		if (ee.left instanceof MessageSend) {
			String selector = new String(((MessageSend) ee.left).selector);
			if (selector.equals("getSource")) { //$NON-NLS-1$
				// A e.getSource() method call on an if statement, check the target
				BeanPart bp = getBeanPart(ee.right);
				if (bp != null) {
					new EventCallBackExpressionVisitor(bp, fMethod, stmt, fModel).visit();
					return true ;
				}
			}
		}
	}
	else if (condition instanceof BinaryExpression) {
		BinaryExpression be = (BinaryExpression) condition;
		if (processCondition(be.left,stmt)) return true ; // One of the if's will do ... at this point will not consider semantics
		result = processCondition(be.right,stmt);
	}
	return result ;
}

/**
 *  Use the rule base engine to parse an IF statement
 */	
protected void	processIFStatement(IfStatement stmt) throws CodeGenException{
	
	if (stmt.condition instanceof BinaryExpression) 
	   processCondition(stmt.condition,stmt) ;
}
	

}
