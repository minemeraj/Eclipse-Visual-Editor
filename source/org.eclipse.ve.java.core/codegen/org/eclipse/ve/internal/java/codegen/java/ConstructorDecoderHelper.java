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
 *  $RCSfile: ConstructorDecoderHelper.java,v $
 *  $Revision: 1.68 $  $Date: 2005-12-09 21:12:42 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;
import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.model.*;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.FieldResolvedType;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class ConstructorDecoderHelper extends ExpressionDecoderHelper {
		
	private PropertyFeatureMapper implicitFeatureMapper = null;
	
	/**
	 * 
	 * This decoder deals with simple constructors
	 * 
	 * @since 1.0.0
	 */
	public ConstructorDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);		
	}
	
	protected PropertyFeatureMapper getImplicitFeatureMapper(IJavaObjectInstance parent) {
		if (implicitFeatureMapper!=null) return implicitFeatureMapper;
		
		implicitFeatureMapper = new PropertyFeatureMapper();
		implicitFeatureMapper.setRefObject(parent);	
		return implicitFeatureMapper;
	}

	/**
	 * This is temporary untill we move to the new AST, convert
	 * @return  new AST
	 * 
	 * @since 1.0.0
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

	public static class CGParseTreeCreationFromAST extends ParseTreeCreationFromAST {

		public CGParseTreeCreationFromAST(ParseTreeCreationFromAST.Resolver resolver) {
			super(resolver);
		}

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
		
		/* (non-Javadoc)
		 * @see org.eclipse.jem.workbench.utility.ParseTreeCreationFromAST#visit(org.eclipse.jdt.core.dom.FieldAccess)
		 */
		public boolean visit(FieldAccess node) {
			Expression receiver = node.getExpression();
			// expression can never be null in a FieldAccess
			if(receiver.getNodeType()==ASTNode.THIS_EXPRESSION){
				// It could be this.myComponent
				PTExpression exp = ((CGResolver)resolver).resolveName(node.getName());
				if(exp!=null){
					expression=exp;
					return false;
				}
			}
			return super.visit(node);
		}
	}

	public static class CGResolver extends ParseTreeCreationFromAST.Resolver{
		
		private IBeanDeclModel bdm;
		private List ref;
		private CodeMethodRef expMethodRef;
		private int	offset;
		
		public CGResolver(CodeMethodRef expMethodRef, int off, IBeanDeclModel bdm, List ref) {
			this.expMethodRef = expMethodRef;
			this.bdm = bdm;
			this.ref = ref;
			offset = off;
			
		}
		
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
				else
					return createImmutablePTExpression((SimpleName)name); // simple name not a bean - might be a primitive variable
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
			BeanPart bp = CodeGenUtil.getBeanPart(bdm, name.getIdentifier(), expMethodRef, offset);
			if (bp!=null)
				return createBeanPartExpression(ref, bp);
			return null;
		}
		
		/**
		 * Create the bean part expression for this bean part.
		 * @param ref
		 * @param bp
		 * @return PT expression referencing bp
		 * 
		 * @since 1.0.2
		 */
		private PTInstanceReference createBeanPartExpression(final List ref, BeanPart bp) {
			PTInstanceReference ptref = InstantiationFactory.eINSTANCE.createPTInstanceReference();
			IJavaInstance o = (IJavaInstance)bp.getEObject();
			if (ref!=null && !ref.contains(o))
			    ref.add(o);
			ptref.setReference(o);
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
		
		/**
		 * @param name
		 * @return
		 * 
		 * @since 1.0.2
		 */
		private PTExpression createImmutablePTExpression(final SimpleName name) {
			PTExpression ptExpression = null;
			ASTNode parent = name.getParent();
			
			final List values = new ArrayList();
			Expression value = null;
			
			final List declaration = new ArrayList();
			Type type = null;
			
			final int offset = name.getStartPosition();
			
			final List parents = new ArrayList();
			final Collection visitedMap = new ArrayList();
			while(parent!=null){
				parents.add(parent);
				parent.accept(new ASTVisitor(){
					public boolean visit(Assignment node) {
						if(!visitedMap.contains(node) && !parents.contains(node) && node.getStartPosition()<offset){
							visitedMap.add(node);
							if(node.getLeftHandSide() instanceof SimpleName){
								SimpleName lhs = (SimpleName) node.getLeftHandSide();
								if(name.getIdentifier().equals(lhs.getIdentifier())){
									values.add(node.getRightHandSide());
									return false;
								}
							}
						} 
						else 
							return false;
						return super.visit(node);
					}
					public boolean visit(VariableDeclarationFragment node) {
						if(!visitedMap.contains(node) && !parents.contains(node) && node.getStartPosition()<offset){
							visitedMap.add(node);
							if(name.getIdentifier().equals(node.getName().getIdentifier())){
									values.add(node.getInitializer());
									return false;
							}
						}
						else
							return false;
						return super.visit(node);
					}
					public boolean visit(VariableDeclarationExpression node) {
						if(!visitedMap.contains(node) && !parents.contains(node) && node.getStartPosition()<offset){
							List fragments = node.fragments();
							for(int i=0;i<fragments.size();i++){
								VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments.get(i);
								handleVariableDeclaration(node.getType(), frag.getName(), node);
								if(name.getIdentifier().equals(frag.getName().getIdentifier())){
									values.add(frag.getInitializer());
									return false;
								}
							}
						}
						else
							return false;
						return super.visit(node);
					}
					public boolean visit(VariableDeclarationStatement node) {
						List fragments = node.fragments();
						for(int i=0;i<fragments.size();i++){
							VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments.get(i);
							handleVariableDeclaration(node.getType(), frag.getName(), node);
						}
						return super.visit(node);
					}
					public boolean visit(SingleVariableDeclaration node) {						
						if(!visitedMap.contains(node) && !parents.contains(node) && node.getStartPosition()<offset){
							handleVariableDeclaration(node.getType(), node.getName(), node);
							if (name.getIdentifier().equals(node.getName().getIdentifier())) {
								values.add(node.getInitializer());
								return false;
							}
						}
						else
							return false;
						return super.visit(node);
					}
					public boolean visit(FieldDeclaration node) {
						List fragments = node.fragments();
						for(int i=0;i<fragments.size();i++){
							VariableDeclarationFragment frag = (VariableDeclarationFragment) fragments.get(i);
							handleVariableDeclaration(node.getType(), frag.getName(), node);
						}
						return super.visit(node);
					}
					protected void handleVariableDeclaration(Type type, SimpleName varName, ASTNode node){
						if(name.getIdentifier().equals(varName.getIdentifier())){
							declaration.add(type);
						}
					}
				});
				if(value==null && values.size()>0)
					value = (Expression) values.get(values.size()-1);
				if(type==null && declaration.size()>0)
					type = (Type) declaration.get(declaration.size()-1);
				if(type!=null && value!=null)
					break; // A variable declaration has been reached - no need to keep visiting parents
				else
					parent = parent.getParent();
			}
			if(type!=null && value!=null){
				// We have a type - check to see its an immutable
				if(isTypeImmutable(type)) {
					// Immutable - try to get its value
					// The immutable could have other immutables in it - so call this resolver on that again
					ptExpression = getParsedTree(value, expMethodRef, offset, bdm, ref);
				}
			}
			return ptExpression;
		}

		/**
		 * @param type
		 * @return true if Immutable
		 * 
		 * @since 1.0.2
		 */
		private boolean isTypeImmutable(Type type) {
			if(type.isPrimitiveType())
				return true;
			String typeName = resolveType(type);
			if(		"java.lang.String".equals(typeName) || //$NON-NLS-1$
					"java.lang.Character".equals(typeName) || //$NON-NLS-1$
					"java.lang.Byte".equals(typeName) || //$NON-NLS-1$
					"java.lang.Short".equals(typeName) || //$NON-NLS-1$
					"java.lang.Integer".equals(typeName) || //$NON-NLS-1$
					"java.lang.Long".equals(typeName) || //$NON-NLS-1$
					"java.lang.Boolean".equals(typeName) || //$NON-NLS-1$
					"java.lang.Float".equals(typeName) || //$NON-NLS-1$
					"java.lang.Double".equals(typeName)) //$NON-NLS-1$
				return true;
			return false;
		}

	}		

	
	/**
	 * Convert an AST to resolved Parsed Tree
	 * @param ast  (AST expression)
	 * 
	 * @param expMethodRef  The methodref in which the ast parameter is to be evaluated in. Strictly 
	 * used for finding instance references which happen to be local variable. If <code>null</code>
	 * is passed in, no local variable resolution will take place. 
	 *  
	 * @param off offset within the method (to resolve proper model instance for a given expression)
	 * @param bdm
	 * @param ref will update the list with the referenced instances not null
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public  static PTExpression getParsedTree(Expression ast, CodeMethodRef expMethodRef, int off, IBeanDeclModel bdm, List ref) {
		ParseTreeCreationFromAST parser = new CGParseTreeCreationFromAST(new CGResolver(expMethodRef, off, bdm, ref));
		return parser.createExpression(ast);
	}
	
	
	protected ParseTreeAllocation createParseTreeAllocation(Expression ast, CodeMethodRef expOfMethod) {
		PTExpression exp = getParsedTree(ast,expOfMethod,fOwner.getExprRef().getOffset(), fbeanPart.getModel(), getExpressionReferences()); 
		return InstantiationFactory.eINSTANCE.createParseTreeAllocation(exp); 
	}
	
	protected ImplicitAllocation createImplicitAllocation (EObject parent, Expression ast) {
		EStructuralFeature sf = getImplicitFeatureMapper((IJavaObjectInstance)parent).getImplicitFeature(ast);
		if (sf!=null)
			return InstantiationFactory.eINSTANCE.createImplicitAllocation(parent, sf);
		return null;
	}
	protected JavaAllocation createAllocation(Expression ast, CodeMethodRef expOfMethod){
		if (ast!=null) {
			if (ast instanceof MethodInvocation) {
				// Check to see if it could be an implicit construction (e.g., Foo foo = bar.getFoo())
				Expression receiver = ((MethodInvocation)ast).getExpression();
				if (receiver != null) {
					if (receiver instanceof SimpleName) {
						String parentName = ((SimpleName)receiver).getIdentifier();
						BeanPart parent = CodeGenUtil.getBeanPart(
								fbeanPart.getModel(), 
								parentName, 
								fOwner.getExprRef().getMethod(), 
								fOwner.getExprRef().getOffset());
						if(parent==null)
							parent = fbeanPart.getModel().getABean(parentName);
						if (parent!=null) {							
							return createImplicitAllocation (parent.getEObject(), ast);
					    }
					}
					else if (receiver instanceof MethodInvocation) {
						MethodInvocation lastMi = (MethodInvocation) receiver;;
						while(lastMi.getExpression() instanceof MethodInvocation)
							lastMi = (MethodInvocation) lastMi.getExpression();
						if (lastMi.getExpression() instanceof SimpleName) {
							SimpleName sn = (SimpleName) lastMi.getExpression();
							String beanName = sn.getIdentifier();
							BeanPart bean = CodeGenUtil.getBeanPart(
									fbeanPart.getModel(), 
									beanName, 
									fOwner.getExprRef().getMethod(), 
									fOwner.getExprRef().getOffset());
							if(bean==null)
								bean = fbeanPart.getModel().getABean(beanName);
							if(bean!=null){
								// ultimately there is a bean getting called - 
								// so it could be an implicit allocation
								return createAllocation (receiver, expOfMethod);
							}
						}else{
							return createAllocation (receiver, expOfMethod);
						}
					}
				}
			}		
			return createParseTreeAllocation(ast, expOfMethod);
		}
		else if (fbeanPart.isImplicit()) {			
			IJavaObjectInstance obj = (IJavaObjectInstance)fbeanPart.getEObject();
			return obj.getAllocation();							
		}
		return null;
	}
	
	
	public static EStructuralFeature getRequiredImplicitFeature (IJavaObjectInstance obj) {
		// TODO: this may return null, String or String[]
		FeatureAttributeValue val = BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(obj.getJavaType(), IBaseBeanInfoConstants.REQUIRED_IMPLICIT_PROPERTIES);
		if (val!=null) {
		   return obj.getJavaType().getEStructuralFeature((String)val.getValue());
		}
		return null;
	}
	
	public static void primCreateImplicitInstanceIfNeeded(BeanPart bp, EStructuralFeature sf) {
		if (sf==null) 
			sf = getRequiredImplicitFeature((IJavaObjectInstance)bp.getEObject());
		
		if (sf!=null) {			
			BeanPartFactory bpf = new BeanPartFactory(bp.getModel(),bp.getModel().getCompositionModel());
			bpf.createImplicitBeanPart(bp,sf);			
		}
	}
	
	public static void primRemoveImplicitInstanceIfNeeded(BeanPart bp, EStructuralFeature sf) {
		if (sf==null) 
			sf = getRequiredImplicitFeature((IJavaObjectInstance)bp.getEObject());
		
		if (sf!=null) {			
			BeanPartFactory bpf = new BeanPartFactory(bp.getModel(),bp.getModel().getCompositionModel());
			bpf.removeImplicitBeanPart(bp,sf);			
		}
	}
	
	protected void createImplicitInstancesIfNeeded() throws CodeGenException {
		primCreateImplicitInstanceIfNeeded(fbeanPart, null);
	}
	
	protected void removeImplicitInstancesIfNeeded() throws CodeGenException {
		primRemoveImplicitInstanceIfNeeded(fbeanPart, null);
	}
	
	
	protected void designateAsImplicit (boolean updateModel) throws CodeGenException {
		
		ImplicitAllocation ia = (ImplicitAllocation) ((IJavaObjectInstance) fbeanPart.getEObject()).getAllocation();
		EStructuralFeature sf = ia.getFeature();
		BeanPart parent = fbeanPart.getModel().getABean(ia.getParent());
		// Add reference to the visual parent, It is required here that the parent's init
		// expression has beed decoded already!!
		getExpressionReferences().addAll(CodeGenUtil.getReferences(ia.getParent(), false));
		// we also have a dependency on the implicitParent
		// TODO: FreeForm issues
		getExpressionReferences().add(parent.getEObject());

		fbeanPart.setImplicitParent(parent, sf);

		// It is possible that during decode, and implicit BeanPart was already fluffed up.
		// Now is the time to replace it with this bean.
		String name = fbeanPart.getImplicitName();
		BeanPart old = CodeGenUtil.getBeanPart(fbeanPart.getModel(), name, fOwner.getExprRef().getMethod(), fOwner.getExprRef().getOffset());
		if (old == null)
			old = fbeanPart.getModel().getABean(name);
		if (old != null && old != fbeanPart) {
			old.dispose();
		}
		if (updateModel) {
			BeanPartFactory.setBeanPartAsImplicit(fbeanPart, parent, sf);
		}
	}
			
	protected void restoreImplicitInstancesIfNeeded() {
	    EStructuralFeature sf = getRequiredImplicitFeature((IJavaObjectInstance)fbeanPart.getEObject());		
		if (sf!=null) {
			EObject implicit = (EObject) fbeanPart.getEObject().eGet(sf);
			EStructuralFeature containingSF = implicit.eContainingFeature();
			// Use the BeanPartFactory to create a new BeanPart only when
			// the implicit is in the implicits of the container. Else, some 
			// other expression will create a BeanPart, and we dont want two
			// BeanParts with the same eObject instance.
			if(containingSF==JCMPackage.eINSTANCE.getMemberContainer_Implicits()){
				BeanPartFactory bpf = new BeanPartFactory(fbeanPart.getModel(),fbeanPart.getModel().getCompositionModel());
				bpf.restoreImplicitBeanPart(fbeanPart,sf, true);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#decode()
	 */
	public boolean decode() throws CodeGenException {
		// Set the EMF object with a proper PT allocation
		CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
		getExpressionReferences().clear();
		
		JavaAllocation alloc = createAllocation(getAST(), expOfMethod); 			
		IJavaObjectInstance obj = (IJavaObjectInstance)fbeanPart.getEObject();
		
		
		// SMART UPDATE
		if(!CodeGenUtil.areAllocationsEqual(obj.getAllocation(), alloc)) {
			obj.setAllocation(alloc) ;
			if (alloc.isImplicit()) {
				designateAsImplicit(true);
			}
			createImplicitInstancesIfNeeded();
		}
		else if (alloc instanceof ImplicitAllocation) {
			designateAsImplicit(false);
		}
		
		return true;
	}
	
	public boolean restore() throws CodeGenException {
		JavaAllocation alloc = ((IJavaObjectInstance)fbeanPart.getEObject()).getAllocation(); 
		if (!(alloc instanceof ImplicitAllocation)) {
		// Update the references (fReferences) from the allocation PT. 
		CodeMethodRef expOfMethod = (fOwner!=null && fOwner.getExprRef()!=null) ? fOwner.getExprRef().getMethod():null;
			getParsedTree(getAST(),expOfMethod, fOwner.getExprRef().getOffset(), fbeanPart.getModel(), getExpressionReferences());
			restoreImplicitInstancesIfNeeded();
		}
		else 
			designateAsImplicit(false);
		
		return true;
	}	
	/**
	 * If a BeanPart is a reUsed local variable, a decleration
	 * should not be generated
	 * @return true, if it is the first seclared local variable
	 * 
	 * @since 1.1.0
	 */
	protected boolean isDeclerationNeeded() {
		boolean result = false;
		BeanPartDecleration d = fbeanPart.getDecleration();
		if (!d.isInstanceVar() || !d.isSingleDecleration()) {
			// Reused Variable
			result = fbeanPart.getDecleration().getBeanPartIndex(fbeanPart)==0;
		}else if(fOwner.getExprRef().isStateSet(CodeExpressionRef.STATE_FIELD_EXP)){
			result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#generate(java.lang.Object[])
	 */
	public String generate(Object[] args) throws CodeGenException {
		IJavaObjectInstance obj = (IJavaObjectInstance)fbeanPart.getEObject();
		StringBuffer sb = new StringBuffer();				
			// ivjFoo = <allocation>;					
		if (!fbeanPart.getDecleration().isInstanceVar() || fOwner.getExprRef().isStateSet(CodeExpressionRef.STATE_FIELD_EXP)) {
			if(fOwner.getExprRef().isStateSet(CodeExpressionRef.STATE_FIELD_EXP))
				contributeModifiers(sb);
			contributeType(sb);
		}
		sb.append(fbeanPart.getSimpleName());
		sb.append(" = "); //$NON-NLS-1$
		sb.append(CodeGenUtil.getInitString(obj, fbeanPart.getModel(), fOwner.getExprRef().getReqImports(), getExpressionReferences()));
		sb.append(";"); //$NON-NLS-1$
		sb.append(fbeanPart.getModel().getLineSeperator());
		fOwner.getExprRef().setState(CodeExpressionRef.STATE_INIT_EXPR,true);
		return sb.toString();
		
	}

	/**
	 * Contributes Modifiers to the generated string
	 * 
	 * @param sb
	 * 
	 * @since 1.1
	 */
	private void contributeModifiers(StringBuffer sb) {
		if(isDeclerationNeeded()){
			if (fExpr instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement vds = (VariableDeclarationStatement) fExpr;
				int modifiers = vds.getModifiers();
				if(Modifier.isFinal(modifiers))
					sb.append("final "); //$NON-NLS-1$
				if(Modifier.isPrivate(modifiers))
					sb.append("private "); //$NON-NLS-1$
				if(Modifier.isProtected(modifiers))
					sb.append("protected "); //$NON-NLS-1$
				if(Modifier.isPublic(modifiers))
					sb.append("public "); //$NON-NLS-1$
				if(Modifier.isStatic(modifiers))
					sb.append("static "); //$NON-NLS-1$
				if(Modifier.isStrictfp(modifiers))
					sb.append("strictfp "); //$NON-NLS-1$
				if(Modifier.isSynchronized(modifiers))
					sb.append("synchronized "); //$NON-NLS-1$
				if(Modifier.isTransient(modifiers))
					sb.append("transient "); //$NON-NLS-1$
				if(Modifier.isVolatile(modifiers))
					sb.append("volatile "); //$NON-NLS-1$
				if(Modifier.isNative(modifiers))
					sb.append("native "); //$NON-NLS-1$
				if(Modifier.isAbstract(modifiers))
					sb.append("abstract "); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @param sb
	 * 
	 * @since 1.1.0.1
	 */
	private void contributeType(StringBuffer sb) {
		String type = fbeanPart.getType();
		fOwner.getExprRef().getReqImports().add(type);
		if (isDeclerationNeeded()) {
		   int idx = type.lastIndexOf('.');
		   if (idx>=0)
			  type = type.substring(idx+1);
		      type = type.replace('$','.');	// Change for qualified for reflection to formal qualified form.
		      sb.append(type+" "); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#removeFromModel()
	 */
	public void removeFromModel() {
		try {
			removeImplicitInstancesIfNeeded();
		} catch (CodeGenException e) {
			JavaVEPlugin.log(e);
		}
		EStructuralFeature f = fFmapper.getFeature(null);
		// We do not want to remove the allocation... as if the EObject is 
		// still contained int he model (membership)... bean proxy will try
		// to uset this... casuing instantiations warning
		if (!f.getName().equals(AllocationFeatureMapper.ALLOCATION_FEATURE))		
		   fbeanPart.getEObject().eUnset(f) ;
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
		return !(fbeanPart.getEObject()!=null && fbeanPart.getEObject().eIsSet(fFmapper.getFeature(null))) ;
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
		return 0;
	}
	
	protected static int getParentCount (BeanPart b) {
		int count=-1 ;
		while(b!=null){
		  count++;
		  b = CodeGenUtil.determineParentBeanpart(b);
		}
		return count;
	}
	
	public Object[] getReferencedInstances() {		
		return CodeGenUtil.getReferences(fbeanPart.getEObject(),false).toArray();
	}
}
