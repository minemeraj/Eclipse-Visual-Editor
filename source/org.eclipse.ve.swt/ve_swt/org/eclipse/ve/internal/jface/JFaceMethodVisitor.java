/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JFaceMethodVisitor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-04-05 20:11:46 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import org.eclipse.ve.internal.java.codegen.java.JavaBeanModelBuilder;
import org.eclipse.ve.internal.java.codegen.java.MethodVisitor;
import org.eclipse.ve.internal.java.codegen.java.rules.IVisitorFactoryRule;
import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver;
 

public class JFaceMethodVisitor extends MethodVisitor {

	public void initialize(MethodDeclaration node, IBeanDeclModel model, List reTryList, CodeMethodRef m, IVisitorFactoryRule visitorFactory) {
		super.initialize(node, model, reTryList, m, visitorFactory);
		processJFaceContributions(node, model);
	}
	
	public void initialize(MethodDeclaration node, IBeanDeclModel model, List reTryList, CodeTypeRef typeRef, String methodHandle, ISourceRange range, String content, IVisitorFactoryRule visitorFactory) {
		super.initialize(node, model, reTryList, typeRef, methodHandle, range, content, visitorFactory);
		processJFaceContributions(node, model);
	}
	
	public void processJFaceContributions(MethodDeclaration method, IBeanDeclModel model) {
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

	protected void createParamBeanPart(SingleVariableDeclaration svd, IBeanDeclModel model, MethodDeclaration method) {
		// If bean has been created by earlier visits to the source - ignore further creation
		if(model.getABean(svd.getName().getFullyQualifiedName())!=null)
			return;
		
		// Determine init method
		CodeMethodRef initMethod = null;
		Iterator methodItr = model.getAllMethods();
		while (methodItr.hasNext()) {
			CodeMethodRef methodRef = (CodeMethodRef) methodItr.next();
			if(methodRef.getDeclMethod()!=null && methodRef.getDeclMethod().equals(method)){
				initMethod = methodRef;
				break;
			}
		}
		
		// Create the bean part
		BeanPart bp = new BeanPart(svd.getName().getFullyQualifiedName(), "org.eclipse.swt.widgets.Composite") ; //$NON-NLS-1$
		model.addBean(bp) ;
		if(initMethod!=null)
			bp.addInitMethod(initMethod);
		
		// Find out the 'this' BeanPart
		BeanPart thisPart = model.getABean("this"); //$NON-NLS-1$
		
		// Create the expression 
		CodeExpressionRef exp = new CodeExpressionRef(initMethod, bp);
		exp.setBean(bp);
		exp.setNoSrcExpression(true);
		exp.setDecoder(new ImplicitAllocationDecoder(thisPart, bp, "delegate_control")); //$NON-NLS-1$
		exp.setOffset(svd.getStartPosition()-method.getStartPosition());
		exp.setState(CodeExpressionRef.STATE_SRC_LOC_FIXED, true);
		updateContentParser(svd, model, exp);
		bp.addRefExpression(exp);
		exp.setState(CodeExpressionRef.STATE_INIT_EXPR, true);
	}

	/**
	 * The content parser is necessary for the snippet update mechanism to work properly,
	 * in determining of expressions are equivalent etc.
	 * 
	 * @param svd
	 * @param model
	 * @param exp
	 * 
	 * @since 1.0.2
	 */
	protected void updateContentParser(SingleVariableDeclaration svd, IBeanDeclModel model, CodeExpressionRef exp) {
		int start = svd.getStartPosition();
		int len = svd.getLength();
		String source = svd.toString();
		if(svd.getRoot()==null){
			start = 0;
			len = source.length();
		}else{
			String rootSource = (String) svd.getRoot().getProperty(JavaBeanModelBuilder.ASTNODE_SOURCE_PROPERTY);
			if(rootSource==null || rootSource.length()<1){
				start = 0;
				len = source.length();
			}else{
				source = rootSource;
			}
		}
		exp.setContent(
				new FixedContentExpressionParser(
						source,
						start,
						len,
						model
				));
	}

}
