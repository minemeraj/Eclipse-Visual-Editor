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
 *  $RCSfile: ASTHelper.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ASTHelper {

public static class VariableValueVisitor 
	extends ASTVisitor{
	
	private String selector = null;
	private ASTNode value = null;
	private Hashtable varToValHash = null;
	private Stack scopeStack = null;
	private int selectorLocation = -1;
		
	public VariableValueVisitor(String selector, int selectorLocation){
		this.selector = selector;
		this.selectorLocation = selectorLocation;
	}
	
	public String getValue(){
		if (value != null) {
			// This is a workaround for AST with ArrayInitialzer bug (see 230955)
/*			if (value instanceof ArrayInitializer) {
				StringBuffer sb = new StringBuffer();
				sb.append("{"); //$NON-NLS-1$
				List expressions = ((ArrayInitializer) value).expressions();
				Expression args[] = new Expression[expressions.size()];
				expressions.toArray(args);
				for (int i = 0; i < args.length; i++) {
					if (i > 0)
						sb.append(","); //$NON-NLS-1$
					sb.append(args[i].toString());
				}
				sb.append('}'); //$NON-NLS-1$
				return sb.toString();
			}
			else
*/				return value.toString();
		}
		return selector;
	}
	private boolean isValuable(ASTNode node){
		if (node instanceof BooleanLiteral)
			return true;
		if (node instanceof CharacterLiteral)
			return true;
		if (node instanceof NullLiteral)
			return true;
		if (node instanceof NumberLiteral)
			return true;
		if (node instanceof StringLiteral)
			return true;
		if (node instanceof TypeLiteral)
			return true;
		if (node instanceof ArrayCreation)
			return true;
		return false;
	}
	private Stack getScopeStack(){
		if(scopeStack==null)
			scopeStack = new Stack();
		return scopeStack;
	}
	
	private Hashtable getHash(){
		if(varToValHash==null)
			varToValHash = new Hashtable();
		return varToValHash;
	}
	
	private void pushScope(){
		getScopeStack().push(getHash().clone());
	}
	
	private void popScope(){
		varToValHash = (Hashtable)getScopeStack().pop();
	}
	
	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(Assignment, BlockScope)
	 */
	public boolean visit(Assignment assignment) {
		if(isValuable(assignment.getRightHandSide())){
			Expression ref = assignment.getLeftHandSide();
			String varName = "?"; //$NON-NLS-1$
			if (ref instanceof SimpleName) {
					SimpleName varNameNode = (SimpleName) ref;
					varName = varNameNode.getIdentifier();
			}
			if (ref instanceof QualifiedName) {
					QualifiedName qname = (QualifiedName) ref;
					varName = qname.getName().getIdentifier();
				}
			getHash().put(varName, assignment.getRightHandSide());
		}
		return super.visit(assignment);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
	 */
	public boolean visit(VariableDeclarationFragment node) {
		if(isValuable(node.getInitializer()))
			getHash().put(node.getName().toString(), node.getInitializer());
		return super.visit(node);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(Block, BlockScope)
	 */
	public void endVisit(Block block) {
		exitingScope(block.getStartPosition(), block.getStartPosition()+block.getLength());
		super.endVisit(block);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(CompilationUnitDeclaration, CompilationUnitScope)
	 */
	public void endVisit(CompilationUnit compilationUnit) {
		exitingScope(compilationUnit.getStartPosition(), compilationUnit.getStartPosition()+compilationUnit.getLength());
		super.endVisit(compilationUnit);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(ForStatement, BlockScope)
	 */
	public void endVisit(ForStatement forStatement) {
		exitingScope(forStatement.getStartPosition(), forStatement.getStartPosition()+forStatement.getLength());
		super.endVisit(forStatement);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(MethodDeclaration, ClassScope)
	 */
	public void endVisit(MethodDeclaration methodDeclaration) {
		exitingScope(methodDeclaration.getStartPosition(), methodDeclaration.getStartPosition()+methodDeclaration.getLength());
		super.endVisit(methodDeclaration);
	}
	
	private void exitingScope(int scopeStart, int scopeEnd){
		if(scopeStart<0 || scopeEnd<0 || scopeStart>scopeEnd)
			return;
		if(selectorLocation>=scopeStart && selectorLocation <=scopeEnd && value==null)
			value = (ASTNode) getHash().get(selector);
		popScope();
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(Block, BlockScope)
	 */
	public boolean visit(Block block) {
		pushScope();
		return super.visit(block);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(CompilationUnitDeclaration, CompilationUnitScope)
	 */
	public boolean visit(CompilationUnit compilationUnit) {
		pushScope();
		return super.visit(compilationUnit);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(MethodDeclaration, ClassScope)
	 */
	public boolean visit(MethodDeclaration methodDeclaration) {
		pushScope();
		return super.visit(methodDeclaration);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(ForStatement, BlockScope)
	 */
	public boolean visit(ForStatement forStatement) {
		pushScope();
		return super.visit(forStatement);
	}

}

public static String resolveVariavleValue(SimpleName variable, int location, String entireCode){
	ASTParser parser = ASTParser.newParser(AST.JLS2);
	parser.setSource(entireCode.toCharArray());
	CompilationUnit cu = (CompilationUnit) parser.createAST(null);

	VariableValueVisitor valueVisitor = new VariableValueVisitor(variable.getIdentifier(), location);
	cu.accept(valueVisitor);
	return valueVisitor.getValue();
}

}
