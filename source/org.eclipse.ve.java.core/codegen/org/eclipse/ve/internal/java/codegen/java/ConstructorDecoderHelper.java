/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ConstructorDecoderHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-05 16:13:50 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaObjectInstance;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class ConstructorDecoderHelper extends ExpressionDecoderHelper {
	
	protected List fReferences = new ArrayList() ;
	

	/**
	 * 
	 * This decoder deals with simple constructors
	 * 
	 * @since 1.0.0
	 */
	public ConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);		
	}

	
	
	/**
	 * This is temporary untill we move to the new AST, convert
	 * @return  new AST
	 * 
	 * @since 1.0.0
	 * @deprecated
	 */
	protected Expression  getAST() {
		StringBuffer sb = new StringBuffer();
		sb.append("class Foo {\n void method() {\n") ;
		sb.append(fExpr.toString()) ;
		sb.append(";\n}\n}") ;
				
		CompilationUnit cu = AST.parseCompilationUnit(sb.toString().toCharArray());
		Assignment e = (Assignment)((ExpressionStatement)((TypeDeclaration)cu.types().get(0)).getMethods()[0].getBody().statements().get(0)).getExpression();
		
		return e.getRightHandSide();
		
	}
	
	protected PTExpression getParsedTree(Expression ast) {
		class Resolver extends ParseTreeCreationFromAST.Resolver{
			public PTExpression resolveName(Name name) {
				String n=null;
				if (name instanceof QualifiedName)
					n = ((QualifiedName)name).toString();
				else if (name instanceof SimpleName)
					n = ((SimpleName)name).getIdentifier();
				if (n!=null) {
					BeanPart bp = fbeanPart.getModel().getABean(n);
					if (bp!=null) {
						PTInstanceReference ptref = InstantiationFactory.eINSTANCE.createPTInstanceReference();
						IJavaObjectInstance o = (IJavaObjectInstance)bp.getEObject();
						fReferences.add(o);
						ptref.setObject(o);
						return ptref;
					}
					else {
						PTName ptname = InstantiationFactory.eINSTANCE.createPTName();
						ptname.setName(n);
						return ptname;
					}
				}				
				return null ;
			}
			public String resolveType(Type type) {
				return fbeanPart.getModel().resolve(type.toString());
			}
		}		
		Resolver r = new Resolver() ;
		ParseTreeCreationFromAST parser = new ParseTreeCreationFromAST(r);
		return parser.createExpression(ast);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {
		// TODO This is a temporary until we move to the new AST and use the converter
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(getParsedTree(getAST()));
		IJavaObjectInstance obj = (IJavaObjectInstance)fbeanPart.getEObject();
		obj.setAllocation(alloc) ;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#generate(java.lang.Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		IJavaObjectInstance obj = (JavaObjectInstance)fbeanPart.getEObject();
		StringBuffer sb = new StringBuffer();		
		if (obj.getAllocation() instanceof ParseTreeAllocation) {
			// ivjFoo = <allocation>;			
			sb.append(fbeanPart.getSimpleName());
			sb.append(" = ");
			PTExpression exp = ((ParseTreeAllocation) obj.getAllocation()).getExpression();
			CodeGenExpFlattener ef = new CodeGenExpFlattener(fbeanPart.getModel()) ;
			// Visit the parsed tree
			exp.accept(ef) ;
			sb.append(ef.getResult());
			sb.append(";");
			sb.append(fbeanPart.getModel().getLineSeperator());
			return sb.toString();
		}
		else
			 return CodeGenUtil.getInitString(obj) ;
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#removeFromModel()
	 */
	public void removeFromModel() {
		fbeanPart.getEObject().eUnset(fFmapper.getFeature(null)) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primRefreshFromComposition(java.lang.String)
	 */
	public String primRefreshFromComposition(String expSig) throws CodeGenException {
		// TODO Want to do smarter here, and compar PTExpressions (generate one from expSig)
		return generate(null) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primIsDeleted()
	 */
	public boolean primIsDeleted() {
		return fbeanPart.getEObject().eIsSet(fFmapper.getFeature(null)) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getArgsHandles(org.eclipse.jdt.internal.compiler.ast.Statement)
	 */
	public Object[] getArgsHandles(Statement expr) {		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#isImplicit(java.lang.Object[])
	 */
	public boolean isImplicit(Object[] args) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getSFPriority()
	 */
	protected int getSFPriority() {		
		return IJavaFeatureMapper.PRIORITY_CONSTRUCTOR;
	}

}
