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
 *  $Revision: 1.13 $  $Date: 2004-04-20 20:49:16 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaObjectInstance;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
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
		Statement s = (Statement)((TypeDeclaration)cu.types().get(0)).getMethods()[0].getBody().statements().get(0);
		if (s instanceof ExpressionStatement) {
			Expression e = ((ExpressionStatement)s).getExpression();
			if (e instanceof Assignment)
			   return ((Assignment)e).getRightHandSide();
			else
				return null ;
		}
		else if (s instanceof VariableDeclarationStatement)
			return ((VariableDeclarationFragment)((VariableDeclarationStatement)s).fragments().get(0)).getInitializer();
		else
			return null;
		
	}
	
	/**
	 * Convert an AST to resolved Parsed Tree
	 * @param ast  (AST expression)
	 * @param bdm
	 * @param ref will update the list with the referenced instances not null
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public  static PTExpression getParsedTree(Expression ast, final IBeanDeclModel bdm, final List ref) {
		class Resolver extends ParseTreeCreationFromAST.Resolver{
			public PTExpression resolveName(Name name) {
				String n=null;
				if (name instanceof QualifiedName) {
					n = ((QualifiedName)name).toString();
					// This could be a type, or a field access
					// TODO:  this is a temporary fix, and will not deal with
					//        factories call etc.
					String r = 	bdm.resolveType(n);
					if (r != null && !r.equals(n)) {
						PTName ptname = InstantiationFactory.eINSTANCE.createPTName();
						ptname.setName(r) ;
						PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess() ;
						fa.setReceiver(ptname) ;
						fa.setField(n.substring(r.length()+1)) ;
						return fa;
					}
				}
				else if (name instanceof SimpleName) {
					// possibly a ref. to an instance variable
					n = ((SimpleName)name).getIdentifier();
					BeanPart bp = bdm.getABean(n);
					if (bp!=null) {
						PTInstanceReference ptref = InstantiationFactory.eINSTANCE.createPTInstanceReference();
						IJavaObjectInstance o = (IJavaObjectInstance)bp.getEObject();
						if (ref!=null)
						   ref.add(o);
						ptref.setObject(o);
						return ptref;
					}
					n = bdm.resolve(n);
				}
				if (n!=null) {
					PTName ptname = InstantiationFactory.eINSTANCE.createPTName();
					ptname.setName(n);
					return ptname;				
				}				
				return null ;
			}
			public String resolveType(Type type) {
				if (type instanceof SimpleType)
				   return CodeGenUtil.resolve(((SimpleType)type).getName(), bdm);  //fbeanPart.getModel().resolve(type.toString());
				else
					//TBD
					return null;
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
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(getParsedTree(getAST(),fbeanPart.getModel(),fReferences));
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
			// ivjFoo = <allocation>;			
		sb.append(fbeanPart.getSimpleName());
		sb.append(" = ");
		sb.append(CodeGenUtil.getInitString(obj, fbeanPart.getModel()));
		sb.append(";");
		sb.append(fbeanPart.getModel().getLineSeperator());
		return sb.toString();
		
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
		return !fbeanPart.getEObject().eIsSet(fFmapper.getFeature(null)) ;
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
	
	/* 
	 * Determines the index priority for the constructor expressions.
	 * NOTE: Since the hierarchy of the beans should be known for the 
	 * index values to turn out correctly, it is assumed that the decoding
	 * process has been completed when this code gets called.
	 * 
	 * 
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getIndexPriority()
	 */
	protected int getIndexPriority() {
		if(fbeanPart!=null){
			// TODO Warning: Hierarchy of beans is assumed to be known at this point, 
			// to figure out constructor expression index priority.
			BeanPart obj = fbeanPart;
			int parentCount = 0;
			while(obj!=null){
				parentCount++;
				obj = CodeGenUtil.determineParentBeanpart(obj);
			}
			return Integer.MAX_VALUE - parentCount;
		}
		return super.getIndexPriority();
	}
}
