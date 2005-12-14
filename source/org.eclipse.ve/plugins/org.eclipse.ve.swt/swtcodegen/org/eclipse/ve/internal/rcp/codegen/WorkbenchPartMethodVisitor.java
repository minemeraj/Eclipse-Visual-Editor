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
 *  $RCSfile: WorkbenchPartMethodVisitor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-12-14 21:38:13 $ 
 */
package org.eclipse.ve.internal.rcp.codegen;

import java.util.List;

import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.codegen.java.MethodVisitor;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.IMethodArgumentCodegenHelper;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver;

 

public class WorkbenchPartMethodVisitor extends MethodVisitor {

	
	public void processJFaceContributions(MethodDeclaration method, IBeanDeclModel model) {
		// This visitor is for WorkbenchPart class only
		if (method.getName().getIdentifier().equals("createPartControl")) { //$NON-NLS-1$
			List parameters = method.parameters();
			if(parameters!=null && parameters.size()==1){
				Object param = parameters.get(0);
				if (param instanceof SingleVariableDeclaration) {
					SingleVariableDeclaration svd = (SingleVariableDeclaration) param;
					String fqn = svd.getType().toString();
					TypeResolver.Resolved resolved = model.getResolver().resolveType(svd.getType());
					if(resolved!=null)
						fqn = resolved.getName();
					if("org.eclipse.swt.widgets.Composite".equals(fqn)){ //$NON-NLS-1$
						createParamBeanPart(svd, model, method);
					}
				}
			}
		}
	}

	protected void createParamBeanPart(SingleVariableDeclaration svd, IBeanDeclModel model, MethodDeclaration method) {
		// If bean has been created by earlier visits to the source - ignore further creation
		String beanHandle = BeanPartDecleration.createDeclerationHandle(fMethod, svd.getName().getFullyQualifiedName());
		BeanPartDecleration bpDecl = model.getModelDecleration(beanHandle);
		
		// Create the 'Statement' AST node as we cannot handle non-statement expressions
		AST ast = svd.getAST();
		VariableDeclarationFragment varDeclFragment = ast.newVariableDeclarationFragment();
		varDeclFragment.setFlags(svd.getFlags());
		varDeclFragment.setExtraDimensions(svd.getExtraDimensions());
		if(svd.getInitializer()!=null)
			varDeclFragment.setInitializer((Expression) ASTNode.copySubtree(ast, svd.getInitializer()));
		varDeclFragment.setName((SimpleName) ASTNode.copySubtree(ast, svd.getName()));
		VariableDeclarationStatement varDeclStatement = ast.newVariableDeclarationStatement(varDeclFragment);
		varDeclStatement.setType((Type) ASTNode.copySubtree(ast, svd.getType()));
		varDeclStatement.setSourceRange(svd.getStartPosition(), svd.getLength());
		
		//TODO : Remove the AST node param - no need for it
		WorkbenchPartArgumentCodegenHelper helper = new WorkbenchPartArgumentCodegenHelper("delegate_control", /*svd*/ null); // no need of passing in of node. It caches it and leaks memory. //$NON-NLS-1$
		varDeclStatement.setProperty(IMethodArgumentCodegenHelper.KEY_METHODARGUMENT_CODEGENHELPER, helper);
		
		
		// Determine init method
		CodeMethodRef initMethod = fMethod;
		
		// Create the bean part
		if(bpDecl==null)
			bpDecl = new BeanPartDecleration(varDeclStatement) ; //$NON-NLS-1$
		bpDecl.setDeclaringMethod(initMethod);
		BeanPart bp = new BeanPart(bpDecl);			
		model.addBean(bp) ;

		bp.addInitMethod(initMethod);
		
		// Create the expression 
		CodeExpressionRef exp = new CodeExpressionRef(varDeclStatement, initMethod);
		exp.setBean(bp);
		exp.setNoSrcExpression(true);
		exp.setOffset(svd.getStartPosition()-method.getStartPosition());
		exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
		bp.addRefExpression(exp);
		exp.setState(CodeExpressionRef.STATE_INIT_EXPR, true);
	}

	public void visit() {
		processJFaceContributions((MethodDeclaration) fVisitedNode, fModel);
		super.visit();
	}

}
