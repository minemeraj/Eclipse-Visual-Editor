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
 *  $Revision: 1.26 $  $Date: 2004-10-13 18:49:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaObjectInstance;
import org.eclipse.jem.internal.instantiation.impl.NaiveExpressionFlattener;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.FieldResolvedType;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
 
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
		if (fExpr instanceof ExpressionStatement) {
			Expression e = ((ExpressionStatement)fExpr).getExpression();
			if (e instanceof Assignment)
			   return ((Assignment)e).getRightHandSide();
			else
				return null ;
		}
		else if (fExpr instanceof VariableDeclarationStatement)
			return ((VariableDeclarationFragment)((VariableDeclarationStatement)fExpr).fragments().get(0)).getInitializer();
		else
			return null;
		
	}
	
	/**
	 * Convert an AST to resolved Parsed Tree
	 * @param ast  (AST expression)
	 * 
	 * @param expMethodRef  The methodref in which the ast parameter is to be evaluated in. Strictly 
	 * used for finding instance references which happen to be local variable. If <code>null</code>
	 * is passed in, no local variable resolution will take place. 
	 *  
	 * @param bdm
	 * @param ref will update the list with the referenced instances not null
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public  static PTExpression getParsedTree(Expression ast, final CodeMethodRef expMethodRef, final IBeanDeclModel bdm, final List ref) {
		class CGResolver extends ParseTreeCreationFromAST.Resolver{
			/*
			 *  (non-Javadoc)
			 * @see org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST.Resolver#resolveName(org.eclipse.jdt.core.dom.Name)
			 */
			public PTExpression resolveName(Name name) {
				if (name instanceof QualifiedName) {
					// This could be a type, or a field access.
					// First check if first qualifier is a known bean part.
					Name firstq = name;
					while (firstq.isQualifiedName()) {
						firstq = ((QualifiedName) firstq).getQualifier();
					}
					
					PTExpression exp = resolveToBean((SimpleName) firstq);	// See if we can resolve it.
					if (exp != null) {
						// It was an access to a bean part, so the rest must be a field access to it.
						// Now we will walk back up creating field accesses.
						while (firstq.getParent() instanceof Name) {
							firstq = (Name) firstq.getParent();
							exp = InstantiationFactory.eINSTANCE.createPTFieldAccess(exp, ((QualifiedName) firstq).getName().getIdentifier());
						}
						return exp;
					}
					
					// First was not a bean part, so it must be type/field access.
					FieldResolvedType r = bdm.getResolver().resolveWithPossibleField(name);					
					if (r != null) {
						PTName ptname = InstantiationFactory.eINSTANCE.createPTName(r.resolvedType.getName());					
						if (r.fieldAccessors.length == 0) {
							// Just a type.
							return ptname;
						}
						// It is a field access. Put the resolved PTName as the receiver of the field access.
						// Now we will walk back up creating field accesses.
						exp = ptname;
						for (int i = 0; i < r.fieldAccessors.length; i++) {
							exp = InstantiationFactory.eINSTANCE.createPTFieldAccess(exp, r.fieldAccessors[i]);
						}
						return exp;
					}
				}
				else if (name instanceof SimpleName) {
					// Possibly a reference to a known beanpart.
					PTExpression exp = resolveToBean((SimpleName) name);	// See if we can resolve it.
					if (exp != null)
						return exp;	// It is a known BeanPart.
					
					// See if it is a type.
					Resolved r = bdm.getResolver().resolveType(name);
					if (r != null)
						return InstantiationFactory.eINSTANCE.createPTName(r.getName());
				}
				return null;
			}
			
			/*
			 *  (non-Javadoc)
			 * @see org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST.Resolver#resolveType(org.eclipse.jdt.core.dom.Type)
			 */
			public String resolveType(Type type) {
				Resolved r = bdm.getResolver().resolveType(type);
				return r != null ? r.getName() : null;
			}
			
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST.Resolver#resolveType(org.eclipse.jdt.core.dom.Name)
			 */
			public String resolveType(Name name) {
				Resolved r = bdm.getResolver().resolveType(name);
				return r != null ? r.getName() : null;
			}
			
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST.Resolver#resolveThis()
			 */
			public PTExpression resolveThis() {
				BeanPart bp = bdm.getABean(BeanPart.THIS_NAME);
				if (bp != null)
					return createBeanPartExpression(ref, bp);
				else 
					return InstantiationFactory.eINSTANCE.createPTThisLiteral();
			}
			
			/*
			 * See if the simple name can be resolved down to a bean, either a local
			 * variable or global. Return null if not.
			 */
			private PTExpression resolveToBean(SimpleName name) {
				BeanPart bp = bdm.getABean(name.getIdentifier());
				if (bp!=null)
					return createBeanPartExpression(ref, bp);
				
				// possibly a ref. to a local variable - check bean with unique name
				if(expMethodRef!=null){
					String uniqueName = BeanDeclModel.constructUniqueName(expMethodRef, name.getIdentifier());
					bp = bdm.getABean(uniqueName);
					if (bp!=null) 
						return createBeanPartExpression(ref, bp);
				}
				return null;
			}
			
			/**
			 * Create the bean part expression for this bean part.
			 * @param ref
			 * @param bp
			 * @return
			 * 
			 * @since 1.0.2
			 */
			private PTInstanceReference createBeanPartExpression(final List ref, BeanPart bp) {
				PTInstanceReference ptref = InstantiationFactory.eINSTANCE.createPTInstanceReference();
				IJavaObjectInstance o = (IJavaObjectInstance)bp.getEObject();
				if (ref!=null && !ref.contains(o))
				    ref.add(o);
				ptref.setObject(o);
				return ptref;
			}

			/**
			 * Called locally in codegen parser to resolve the method name. If the 
			 * method name resolves to a bean's getter, then return a PTExpression for it,
			 * else return null if it can't be handled.
			 * 
			 * @param methodName
			 * @return an Expression to replace the invocation, or <code>null</code> if not resolvable.
			 * 
			 * @since 1.0.2
			 */
			public PTExpression resolveMethodInvocation(SimpleName methodName) {
				BeanPart bp = bdm.getBeanReturned(methodName.getIdentifier());
				if (bp != null)
					return createBeanPartExpression(ref, bp);
				else
					return null;
			}
		}		
		CGResolver r = new CGResolver() ;
		ParseTreeCreationFromAST parser = new ParseTreeCreationFromAST(r) {
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST#visit(org.eclipse.jdt.core.dom.MethodInvocation)
			 */
			public boolean visit(MethodInvocation node) {
				// Override to try to handle special resolution of methods to local EObject instances.
				// Doing it here instead of in superclass because general parse tree creation doesn't have a concept of
				// resolving method invocations in any special way.
				Expression receiver = node.getExpression();
				if ((receiver == null || receiver.getNodeType() == ASTNode.THIS_EXPRESSION) && node.arguments().isEmpty()) {
					PTExpression exp = ((CGResolver) resolver).resolveMethodInvocation(node.getName());
					if (exp != null) {
						expression = exp;
						return false;
					}
				}
				return super.visit(node);
			} 
		};
		return parser.createExpression(ast);
	}
	
	public static String convertToString(JavaAllocation alloc){
		if (alloc instanceof InitStringAllocation) {
			InitStringAllocation isAlloc = (InitStringAllocation) alloc;
			return isAlloc.getInitString();
		}else if (alloc instanceof ParseTreeAllocation) {
			ParseTreeAllocation ptAlloc = (ParseTreeAllocation) alloc;
			NaiveExpressionFlattener flattener = new NaiveExpressionFlattener();
			ptAlloc.getExpression().accept(flattener);
			return flattener.getResult();
		}// Ignoring ImplicitAlloction for now
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {
		// TODO This is a temporary until we move to the new AST and use the converter
		CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(getParsedTree(getAST(),expOfMethod,fbeanPart.getModel(),fReferences));
		IJavaObjectInstance obj = (IJavaObjectInstance)fbeanPart.getEObject();
		
		// SMART UPDATE
		boolean allocationChanged = true;
		JavaAllocation currentAllocation = obj.getAllocation();
		if(currentAllocation!=null && alloc!=null){
			String currentAllocationString = convertToString(currentAllocation);
			String newAllocationString = convertToString(alloc);
			if(currentAllocationString!=null)
				allocationChanged = !currentAllocationString.equals(newAllocationString);
		}else if(currentAllocation==null && alloc==null)
			allocationChanged = false;
		if(allocationChanged)
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
		if (!fbeanPart.isInstanceVar()) {
			String type = fbeanPart.getType();
			fOwner.getExprRef().getReqImports().add(type);
			int idx = type.lastIndexOf('.');
			if (idx>=0)
				type = type.substring(idx+1);
			sb.append(type+" ");
		}
		sb.append(fbeanPart.getSimpleName());
		sb.append(" = ");
		sb.append(CodeGenUtil.getInitString(obj, fbeanPart.getModel(), fOwner.getExprRef().getReqImports()));
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
	
	/**
	 * 
	 * @return 3 for GlobalBlobal, 2 for Global local, and 1 for local local.
	 */
	public static int getDefaultBeanPriority(BeanPart b) {
		int p = 10000000;
		if (b.isInstanceVar()) {
			if (b.getReturnedMethod() != null)
				p+= 3;
			else
				p+= 2;			
		}
		else
			p+= 1;
		// Parent should come before its child.
		//TODO: we actyally need to get the parent priority here
		return p-100*getParentCount(b);
	}
	
	protected static int getParentCount (BeanPart b) {
		int count=-1 ;
		while(b!=null){
		  count++;
		  b = CodeGenUtil.determineParentBeanpart(b);
		}
		return count;
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
//			// TODO Warning: Hierarchy of beans is assumed to be known at this point, 
//			// to figure out constructor expression index priority.
//			BeanPart obj = fbeanPart;
//			int parentCount = 0;
//			if (fbeanPart.getReturnedMethod() != fbeanPart.getInitMethod()) {
//				// Let the constructor of the returned bean have a higher priority
//				parentCount++;
//			}
//			while(obj!=null){
//				parentCount++;
//				obj = CodeGenUtil.determineParentBeanpart(obj);
//			}
//			return Integer.MAX_VALUE - parentCount;
			return getDefaultBeanPriority(fbeanPart);
		}
		return super.getIndexPriority();
	}
}
