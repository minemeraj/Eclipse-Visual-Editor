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
 *  $RCSfile: ASTHelper.java,v $
 *  $Revision: 1.3 $  $Date: 2004-02-11 16:03:22 $ 
 */

import java.util.*;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ASTHelper {

public static class SingleNameReferenceValueSyntaxTreeVisitor 
	extends ASTVisitor{
	
	private String selector = null;
	private ASTNode value = null;
	private Hashtable varToValHash = null;
	private Stack scopeStack = null;
	private int selectorLocation = -1;
		
	public SingleNameReferenceValueSyntaxTreeVisitor(String selector, int selectorLocation){
		this.selector = selector;
		this.selectorLocation = selectorLocation;
	}
	
	public String getValue(){
		if (value != null) {
			// This is a workaround for AST with ArrayInitialzer bug (see 230955)
			if (value instanceof ArrayInitializer) {
				StringBuffer sb = new StringBuffer();
				sb.append("{"); //$NON-NLS-1$
				Expression args[] = ((ArrayInitializer) value).expressions;
				for (int i = 0; i < args.length; i++) {
					if (i > 0)
						sb.append(","); //$NON-NLS-1$
					sb.append(args[i].toString());
				}
				sb.append('}'); //$NON-NLS-1$
				return sb.toString();
			}
			else
				return value.toString();
		}
		return selector;
	}
	private boolean isValuable(ASTNode node){
		if (node instanceof Literal)
			return true;
		if (node instanceof ArrayInitializer)
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
	public boolean visit(Assignment assignment, BlockScope scope) {
		if(isValuable(assignment.expression)){
			Expression ref = assignment.lhs;
			String varName = "?"; //$NON-NLS-1$
			
			if (ref instanceof ArrayReference) {
				ArrayReference arRef = (ArrayReference) ref;
				varName = CodeGenUtil.expressionToString(arRef) ;							
			}else if (ref instanceof FieldReference) {
				FieldReference fieldRef = (FieldReference) ref;
				varName = CodeGenUtil.expressionToString(fieldRef) ;
			}else if (ref instanceof NameReference) {
				NameReference nameRef = (NameReference) ref;
				varName = CodeGenUtil.expressionToString(nameRef) ;
			}else if (ref instanceof ThisReference) {
				ThisReference thisRef = (ThisReference) ref;
				varName = CodeGenUtil.expressionToString(thisRef) ;
			}
			getHash().put(varName, assignment.expression);
		}
		return super.visit(assignment, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(FieldDeclaration, MethodScope)
	 */
	public boolean visit(
		FieldDeclaration fieldDeclaration,
		MethodScope scope) {
		if(isValuable(fieldDeclaration.initialization))
			getHash().put(String.valueOf(fieldDeclaration.name), fieldDeclaration.initialization);
		return super.visit(fieldDeclaration, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(LocalDeclaration, BlockScope)
	 */
	public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {
		if(isValuable(localDeclaration.initialization))
			getHash().put(String.valueOf(localDeclaration.name), localDeclaration.initialization);
		return super.visit(localDeclaration, scope);
	}
	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(Block, BlockScope)
	 */
	public void endVisit(Block block, BlockScope scope) {
		exitingScope(block.sourceStart, block.sourceEnd);
		super.endVisit(block, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(CompilationUnitDeclaration, CompilationUnitScope)
	 */
	public void endVisit(
		CompilationUnitDeclaration compilationUnitDeclaration,
		CompilationUnitScope scope) {
			exitingScope(compilationUnitDeclaration.sourceStart, compilationUnitDeclaration.sourceEnd);
		super.endVisit(compilationUnitDeclaration, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(ConstructorDeclaration, ClassScope)
	 */
	public void endVisit(
		ConstructorDeclaration constructorDeclaration,
		ClassScope scope) {
			exitingScope(constructorDeclaration.sourceStart, constructorDeclaration.declarationSourceEnd);
		super.endVisit(constructorDeclaration, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(ForStatement, BlockScope)
	 */
	public void endVisit(ForStatement forStatement, BlockScope scope) {
		exitingScope(forStatement.sourceStart, forStatement.sourceEnd);
		super.endVisit(forStatement, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#endVisit(MethodDeclaration, ClassScope)
	 */
	public void endVisit(
		MethodDeclaration methodDeclaration,
		ClassScope scope) {
			exitingScope(methodDeclaration.sourceStart, methodDeclaration.declarationSourceEnd);
		super.endVisit(methodDeclaration, scope);
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
	public boolean visit(Block block, BlockScope scope) {
		pushScope();
		return super.visit(block, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(CompilationUnitDeclaration, CompilationUnitScope)
	 */
	public boolean visit(
		CompilationUnitDeclaration compilationUnitDeclaration,
		CompilationUnitScope scope) {
		pushScope();
		return super.visit(compilationUnitDeclaration, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(ConstructorDeclaration, ClassScope)
	 */
	public boolean visit(
		ConstructorDeclaration constructorDeclaration,
		ClassScope scope) {
		pushScope();
		return super.visit(constructorDeclaration, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(MethodDeclaration, ClassScope)
	 */
	public boolean visit(
		MethodDeclaration methodDeclaration,
		ClassScope scope) {
		pushScope();
		return super.visit(methodDeclaration, scope);
	}

	/**
	 * @see org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor#visit(ForStatement, BlockScope)
	 */
	public boolean visit(ForStatement forStatement, BlockScope scope) {
		pushScope();
		return super.visit(forStatement, scope);
	}

}

public static String resolveSingleNameReference(String selector, int location, String entireCode){
	List parserOutput = CodeSnippetTranslatorHelper.parseSyntacticallyCodeSnippet(entireCode);
	if(parserOutput==null)
		return selector;
	if(parserOutput.size()<1)
		return selector;
	if (parserOutput.get(0) instanceof CompilationUnitDeclaration) {
		CompilationUnitDeclaration decl = (CompilationUnitDeclaration) parserOutput.get(0);
		SingleNameReferenceValueSyntaxTreeVisitor valueVisitor = 
			new SingleNameReferenceValueSyntaxTreeVisitor(selector,location);
		decl.traverse(valueVisitor,decl.scope);
		return valueVisitor.getValue();
	}
	return selector;
}

}