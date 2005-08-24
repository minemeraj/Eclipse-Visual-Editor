/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: PTExpressionComparator.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.*;

import org.eclipse.jem.internal.instantiation.*;
 

/**
 * Visitor to compare two {@link org.eclipse.jem.internal.instantiation.PTExpression}.
 * 
 * @since 1.1
 */
public class PTExpressionComparator extends ParseVisitor {

	private boolean equal = true;
	
	/*
	 * Private non-synchronized version of a Stack. The java Stack is synchronized, and
	 * we don't need the overhead of that here.
	 */
	private static class Stack {
		private List list = new ArrayList();
		
		public void push(Object o) {
			list.add(o);
		}
		
		public Object pop() {
			return list.remove(list.size()-1);
		}
	}
	
	protected Stack otherPTExpressionStack = new Stack();
	
	/**
	 * Construct the comparator.
	 * @param otherExpression
	 * 
	 * @since 1.1.0.1
	 */
	public PTExpressionComparator(PTExpression otherExpression){
		otherPTExpressionStack.push(otherExpression);
	}
	
	/**
	 * Is the expression equal? Call this after running visitor ({@link PTExpression#accept(ParseVisitor)}.
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public boolean isEqual(){
		return equal;
	}

	/**
	 * Call in this visitor to indicate the expression is not equal and stop all visiting.
	 * 
	 * @throws StopVisiting
	 * 
	 * @since 1.1.0.1
	 */
	protected void notEqual() throws StopVisiting {
		equal = false;
		throw new StopVisiting();
	}
	
	protected void pushInReverse(List list){
		if(list!=null){
			for(int i=list.size()-1;i>=0;i--){
				otherPTExpressionStack.push(list.get(i));
			}
		}
	}
	
	protected void push(Object object){
		otherPTExpressionStack.push(object);
	}
	
	protected Object pop(){
		return otherPTExpressionStack.pop();
	}
	
	public boolean visit(PTArrayAccess node) {
		Object ast = pop();
		if(ast instanceof PTArrayAccess){
			PTArrayAccess otherArrayAccess = (PTArrayAccess) ast;
			List otherIndexes = otherArrayAccess.getIndexes();
			List nodeIndexes = node.getIndexes();
			if(otherIndexes==null && nodeIndexes==null){
				push(otherArrayAccess.getArray());
				return super.visit(node);
			}else if(otherIndexes!=null && nodeIndexes!=null && otherIndexes.size()==nodeIndexes.size()) {
				pushInReverse(otherIndexes);
				push(otherArrayAccess.getArray());
				return super.visit(node);
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTArrayCreation node) {
		Object ast = pop();
		if(ast instanceof PTArrayCreation){
			PTArrayCreation otherArrayCreation = (PTArrayCreation) ast;
			if(node.getType().equals(otherArrayCreation.getType())){
				push(otherArrayCreation.getInitializer());
				List otherArrayDimentions = otherArrayCreation.getDimensions();
				List nodeDimensions = node.getDimensions();
				if(otherArrayDimentions==null && nodeDimensions==null)
					return super.visit(node);
				else if(otherArrayDimentions!=null && nodeDimensions!=null && otherArrayDimentions.size()==nodeDimensions.size()){
					pushInReverse(otherArrayDimentions);
					return super.visit(node);
				}
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTArrayInitializer node) {
		Object ast = pop();
		if(ast instanceof PTArrayInitializer){
			PTArrayInitializer otherArrayInitializer = (PTArrayInitializer) ast;
			List otherExpressions = otherArrayInitializer.getExpressions();
			List nodeExpressions = node.getExpressions();
			if(otherExpressions==null && nodeExpressions==null)
				return super.visit(node);
			else if(otherExpressions!=null && nodeExpressions!=null && otherExpressions.size()==nodeExpressions.size()){
				pushInReverse(otherExpressions);
				return super.visit(node);
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTBooleanLiteral node) {
		Object ast = pop();
		if(ast instanceof PTBooleanLiteral){
			PTBooleanLiteral otherBooleanLiteral = (PTBooleanLiteral) ast;
			if(node.isBooleanValue()==otherBooleanLiteral.isBooleanValue())
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTCastExpression node) {
		Object ast = pop();
		if(ast instanceof PTCastExpression){
			PTCastExpression otherCastExpression = (PTCastExpression) ast;
			if(node.getType().equals(otherCastExpression.getType())){
				push(otherCastExpression.getExpression());
				return super.visit(node);
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTCharacterLiteral node) {
		Object ast = pop();
		if(ast instanceof PTCharacterLiteral){
			PTCharacterLiteral otherCharacterLiteral = (PTCharacterLiteral) ast;
			if(node.getEscapedValue().equals(otherCharacterLiteral.getEscapedValue()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTClassInstanceCreation node) {
		Object otherAST = pop();
		if (otherAST instanceof PTClassInstanceCreation) {
			PTClassInstanceCreation otherClassInstanceCreation = (PTClassInstanceCreation) otherAST;
			if(node.getType().equals(otherClassInstanceCreation.getType())){
				List otherArguments = otherClassInstanceCreation.getArguments();
				List nodeArguments = node.getArguments();
				if(nodeArguments==null && otherArguments==null) {
					return super.visit(node);
				}else if(nodeArguments!=null && otherArguments!=null && nodeArguments.size()==otherArguments.size()){
					pushInReverse(otherArguments);
					return super.visit(node);
				}
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTConditionalExpression node) {
		Object otherAST = pop();
		if (otherAST instanceof PTConditionalExpression) {
			PTConditionalExpression otherConditionalExpression = (PTConditionalExpression) otherAST;
			push(otherConditionalExpression.getFalse());
			push(otherConditionalExpression.getTrue());
			push(otherConditionalExpression.getCondition());
			return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTFieldAccess node) {
		Object otherAST = pop();
		if (otherAST instanceof PTFieldAccess) {
			PTFieldAccess otherFieldAccess = (PTFieldAccess) otherAST;
			if(node.getField().equals(otherFieldAccess.getField())){
				push(otherFieldAccess.getReceiver());
				return super.visit(node);
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTInfixExpression node) {
		Object otherAST = pop();
		if (otherAST instanceof PTInfixExpression) {
			PTInfixExpression otherInfixExpression = (PTInfixExpression) otherAST;
			PTInfixOperator nodeOperator = node.getOperator();
			PTInfixOperator otherOperator = otherInfixExpression.getOperator();
			String nodeOperatorString = nodeOperator==null?null:nodeOperator.getOperator();
			String otherOperatorString = otherOperator==null?null:otherOperator.getOperator();
			if(	nodeOperatorString!=null && otherOperatorString!=null && 
				nodeOperatorString.equals(otherOperatorString)){
				List otherExtendedOperands =otherInfixExpression.getExtendedOperands();
				List nodeExtendedOperands = node.getExtendedOperands();
				if(otherExtendedOperands==null && nodeExtendedOperands==null){
					push(otherInfixExpression.getLeftOperand());
					push(otherInfixExpression.getRightOperand());
					return super.visit(node);
				}else if(otherExtendedOperands!=null && nodeExtendedOperands!=null && otherExtendedOperands.size()==nodeExtendedOperands.size()){
					pushInReverse(otherExtendedOperands);
					push(otherInfixExpression.getLeftOperand());
					push(otherInfixExpression.getRightOperand());
					return super.visit(node);
				}
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTInstanceof node) {
		Object otherAST = pop();
		if (otherAST instanceof PTInstanceof) {
			PTInstanceof otherInstanceof = (PTInstanceof) otherAST;
			if(node.getType().equals(otherInstanceof.getType())){
				push(otherInstanceof.getOperand());
				return super.visit(node);
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTInstanceReference node) {
		Object otherAST = pop();
		if (otherAST instanceof PTInstanceReference) {
			PTInstanceReference otherInstanceReference = (PTInstanceReference) otherAST;
			if(node.getObject()!=null && otherInstanceReference.getObject()!=null && node.getObject().equals(otherInstanceReference.getObject()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTInvalidExpression node) {
		Object otherAST = pop();
		if (otherAST instanceof PTInvalidExpression) {
			PTInvalidExpression otherInvalidExpression = (PTInvalidExpression) otherAST;
			if(node.getMessage().equals(otherInvalidExpression.getMessage()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTMethodInvocation node) {
		Object otherAST = pop();
		if (otherAST instanceof PTMethodInvocation) {
			PTMethodInvocation otherMethodInvocation = (PTMethodInvocation) otherAST;
			if(node.getName().equals(otherMethodInvocation.getName())){
				List otherArguments = otherMethodInvocation.getArguments();
				List nodeArguments = node.getArguments();
				if(nodeArguments==null && otherArguments==null){
					push(otherMethodInvocation.getReceiver());
					return super.visit(node);
				}else if(nodeArguments!=null && otherArguments!=null && otherArguments.size()==nodeArguments.size()){
					pushInReverse(otherArguments);
					push(otherMethodInvocation.getReceiver());
					return super.visit(node);
				}
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTName node) {
		Object otherAST = pop();
		if (otherAST instanceof PTName) {
			PTName otherName = (PTName) otherAST;
			if(node.getName().equals(otherName.getName()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTNullLiteral node) {
		Object otherAST = pop();
		if (otherAST instanceof PTNullLiteral) {
			return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTNumberLiteral node) {
		Object otherAST = pop();
		if (otherAST instanceof PTNumberLiteral) {
			PTNumberLiteral otherNumLiteral = (PTNumberLiteral) otherAST;
			if(node.getToken().equals(otherNumLiteral.getToken()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTParenthesizedExpression node) {
		Object otherAST = pop();
		if (otherAST instanceof PTParenthesizedExpression) {
			PTParenthesizedExpression otherParenthesizedExpression = (PTParenthesizedExpression) otherAST;
			push(otherParenthesizedExpression.getExpression());
			return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTPrefixExpression node) {
		Object otherAST = pop();
		if (otherAST instanceof PTPrefixExpression) {
			PTPrefixExpression otherPrefixExpression = (PTPrefixExpression) otherAST;
			PTPrefixOperator nodeOperator = node.getOperator();
			PTPrefixOperator otherOperator = otherPrefixExpression.getOperator();
			String nodeOperatorString = nodeOperator==null?null:nodeOperator.getOperator();
			String otherOperatorString = otherOperator==null?null:otherOperator.getOperator();
			if(	nodeOperatorString!=null && otherOperatorString!=null && 
				nodeOperatorString.equals(otherOperatorString)){
				push(otherPrefixExpression.getExpression());
				return super.visit(node);
			}
		}
		notEqual();
		return false;
	}

	public boolean visit(PTStringLiteral node) {
		Object otherAST = pop();
		if (otherAST instanceof PTStringLiteral) {
			PTStringLiteral otherStringLiteral = (PTStringLiteral) otherAST;
			if(node.getEscapedValue().equals(otherStringLiteral.getEscapedValue()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTThisLiteral node) {
		Object otherAST = pop();
		if (otherAST instanceof PTThisLiteral) {
			return super.visit(node);
		}
		notEqual();
		return false;
	}

	public boolean visit(PTTypeLiteral node) {
		Object otherAST = pop();
		if (otherAST instanceof PTTypeLiteral) {
			PTTypeLiteral otherTypeLiteral = (PTTypeLiteral) otherAST;
			if(node.getType().equals(otherTypeLiteral.getType()))
				return super.visit(node);
		}
		notEqual();
		return false;
	}
}
