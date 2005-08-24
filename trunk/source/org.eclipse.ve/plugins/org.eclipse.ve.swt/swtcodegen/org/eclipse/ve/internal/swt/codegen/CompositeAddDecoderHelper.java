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
 *  $RCSfile: CompositeAddDecoderHelper.java,v $
 *  $Revision: 1.26 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.CodeExpressionRef;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class CompositeAddDecoderHelper extends AbstractContainerAddDecoderHelper {

	/**
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 * 
	 * @since 1.0.0
	 */
	
	
	String [][]  NoConstraintSF = {
				CompositeDecoder.structuralFeatures,             // control feature
	} ;
	
	public CompositeAddDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}

	protected EObject add(EObject toAdd, BeanPart target, int index) {
		int i = index ;
		if (i<0) {
			i = findIndex(target) ;
			// fAddedIndex may inforce an insert mathod, vs. and add method
			//  fAddedIndex = Integer.toString(i) ;
		}
		
		if (toAdd.eContainer()==null){
	    	//  not in the model yet ... no Bean Part
	       fbeanPart.getInitMethod().getCompMethod().getProperties().add(toAdd);
		}

		CodeGenUtil.eSet(target.getEObject(), 
				fFmapper.getFeature(null), 
				toAdd, i) ;
		
		return toAdd ;
	}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#add(BeanPart, BeanPart, int)
	 */
	protected EObject add(BeanPart toAdd, BeanPart target, int index) {
		toAdd.addBackRef(target, (EReference)fFmapper.getFeature(null)) ;
		target.addChild(toAdd) ;
		
		return add(toAdd.getEObject(), target, index) ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getComponent(EObject)
	 */
	protected IJavaObjectInstance getComponent(EObject root) {
		return (IJavaObjectInstance) root ;
	}

	protected BeanPart parseAddedPart(MethodInvocation exp) throws CodeGenException {
		// TODO  Need to deal with multiple arguments, and nesting
		
		if (exp == null) return null ;
		
		BeanPart bp = null ;
		
		List args = exp.arguments() ;
		if (args.size() < 1)
			return parseNoArgAddedPart(exp) ;
			
		
		// Parse the arguments to figure out which bean to add to this container
		if (args.get(getAddedPartArgIndex(args.size())) instanceof MethodInvocation)  {
			// Look to see of if this method returns a Bean
			String selector = ((MethodInvocation)args.get(getAddedPartArgIndex(args.size()))).getName().getIdentifier();  
			bp = fOwner.getBeanModel().getBeanReturned(selector) ;       	
		}
		else if (args.get(getAddedPartArgIndex(args.size())) instanceof SimpleName){
			// Simple reference to a bean
			String beanName =((SimpleName)args.get(getAddedPartArgIndex(args.size()))).getIdentifier();
			bp = CodeGenUtil.getBeanPart(fbeanPart.getModel(), beanName, fOwner.getExprRef().getMethod(), fOwner.getExprRef().getOffset());
		}
		else if (args.get(getAddedPartArgIndex(args.size())) instanceof ClassInstanceCreation) {
			if (fAddedInstance==null) {
				Resolved resolved = fbeanPart.getModel().getResolver().resolveType(((ClassInstanceCreation)args.get(getAddedPartArgIndex(args.size()))).getName());
				if (resolved == null)
					return null;
				String clazzName = resolved.getName();			
				IJavaObjectInstance obj = (IJavaObjectInstance) CodeGenUtil.createInstance(clazzName,fbeanPart.getModel().getCompositionModel()) ;
				JavaClass c = (JavaClass) obj.getJavaType() ;
				if (c.isExistingType()) {
					fAddedInstance = obj ;
					obj.setAllocation(getAllocation((Expression)args.get(getAddedPartArgIndex(args.size()))));
				}
			}
		}
		return bp ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#shouldCommit(org.eclipse.ve.internal.java.codegen.model.BeanPart, org.eclipse.ve.internal.java.codegen.model.BeanPart, org.eclipse.emf.ecore.EObject, java.util.List)
	 */
	protected boolean shouldCommit(BeanPart oldAddedPart, BeanPart newAddedPart, EObject newAddedInstance, List args) {
		boolean shouldCommit = super.shouldCommit(oldAddedPart, newAddedPart, newAddedInstance, args);
		// Affected by offset changes?
		if(!shouldCommit){
			if (args.size() == 2 && args.get(1) instanceof NumberLiteral) {}
			else shouldCommit = !canAddingBeSkippedByOffsetChanges();
		}
		if(!shouldCommit){
			if(fAddedPart!=null){
		      	  boolean backRefAdded = false;
			      BeanPart[] bRefs = fAddedPart.getBackRefs();
			      int bRefCount = 0;
			      for (; bRefCount < bRefs.length; bRefCount++) 
					if(fbeanPart.equals(bRefs[bRefCount])){
						backRefAdded = true;
						break;
					}
				    
			       boolean childAdded = false;
				   Iterator childItr = fbeanPart.getChildren();
				   while (childItr.hasNext()) {
						BeanPart child = (BeanPart) childItr.next();
						if(child.equals(fAddedPart)){
							childAdded = true;
							break;
						}
				   }
					shouldCommit = !backRefAdded || !childAdded;
			}
			if(!shouldCommit){
				int index = findIndex(fbeanPart);
				EStructuralFeature sf = fFmapper.getFeature(null);
				EObject targetEObject = fbeanPart.getEObject();
				EObject addedOne = getRootObject(false);
				if (sf.isMany()) {
					List elements = getRootComponentList();
					if((!elements.contains(addedOne)) || (index>-1 && elements.indexOf(addedOne)!=index))
						shouldCommit = true;
				}else
				   shouldCommit =  (!targetEObject.eIsSet(sf)) || (!targetEObject.eGet(sf).equals(addedOne));
			}
		}
		return shouldCommit;
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#parseAndAddArguments(Expression[])
	 */
	protected boolean parseAndAddArguments(List args) throws CodeGenException {
		if (fAddedPart!= null || fAddedInstance != null) {
			int index = -1 ;
			if (args.size() == 2 && args.get(1) instanceof NumberLiteral) {
				fAddedIndex = args.get(1).toString(); //CodeGenUtil.expressionToString(args[1]);
				index = Integer.parseInt(fAddedIndex) ;
			}          
			if (fAddedPart!=null)
				add(fAddedPart, fbeanPart, index) ;
			else
				add(fAddedInstance, fbeanPart, index) ;
			return true ;
		}	
		CodeGenUtil.logParsingError(fExpr.toString(), fbeanPart.getInitMethod().getMethodName(), "Could not resolve added component",false) ; //$NON-NLS-1$	
		return false ;
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#isMySigniture()
	 */
	protected boolean isMySigniture() {
		if (fbeanPart.getEObject() == null) return false ;
		// TODO  Need to make a generic no conttraint component feature
		for (int i = 0; i < NoConstraintSF.length; i++) {
			String[] elements = NoConstraintSF[i];
			for (int j = 0; j < elements.length; j++) {		
				EStructuralFeature sf = fbeanPart.getEObject().eClass().getEStructuralFeature(elements[j]) ; 
				if (sf != null && fFmapper.getFeature(fExpr).equals(sf)) return true ;	
			}      
		}   
		return false ;
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getExpressionTemplate()
	 */
	protected ExpressionTemplate getExpressionTemplate() throws CodeGenException {
		
		String[] args = getSourceCodeArgs() ;
		
		String sel = fbeanPart.getSimpleName() ;
		String mtd ;
		if (fAddedIndex != null)
			mtd = fFmapper.getIndexMethodName() ;
		else
			mtd = fFmapper.getMethodName() ;
		ExpressionTemplate exp = new ExpressionTemplate (sel,mtd, args,	                                                
				null,
				0 ) ;
		exp.setLineSeperator(fbeanPart.getModel().getLineSeperator()) ;	
		return exp ;   	
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#primRefreshArguments()
	 */
	protected void primRefreshArguments() {
		
		// indexValueFound will designate if to use and index APi or not
		if(indexValueFound || fAddedIndex!=null){ 	
			// Add an index position, as it was found.
			List parentsChildren = getRootComponentList() ; 
			int indexOfAddedPart = parentsChildren.indexOf(getRootObject(false));
			fAddedIndex = Integer.toString(indexOfAddedPart);	
		}
		else 
			fAddedIndex = null;

	}


	private String[] getSourceCodeArgs() {
		String AddedArg ;
		if (fAddedPart.getInitMethod().equals(fbeanPart.getInitMethod()))   // Added part is defined in the same method as the container
			AddedArg = fAddedPart.getSimpleName() ;
		else	
			AddedArg= fAddedPart.getReturnedMethod().getMethodName()+
			ExpressionTemplate.LPAREN+ExpressionTemplate.RPAREN ;   
		List finalArgs = new ArrayList();
		
		finalArgs.add(AddedArg);
		
		if(fAddedIndex!=null){
			finalArgs.add(fAddedIndex);
		}
		String[] args = new String[finalArgs.size()];
		for(int i=0;i<finalArgs.size();i++)
			args[i] = (String)finalArgs.get(i);
		return args ;
	}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#primRefreshFromComposition(String)
	 */
	public String primRefreshFromComposition(String expSig) throws CodeGenException {


		// Isolate the constraint argument using the last used constraint
		String[] args = getSourceCodeArgs() ;
		
		int start = expSig.indexOf(args[0]) ;	
		// TODO  Need to deal with identical 
		int end = start<0 ? -1 : 	          
						  expSig.lastIndexOf(")"); //$NON-NLS-1$
		if (start<0 || end<0) {
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING))
				JavaVEPlugin.log ("NoConstraintAdDecoderHelper.primRefreshFromComposition(): Error", Level.WARNING) ; //$NON-NLS-1$
			return expSig ;
		}
		
		// Get the latest constraint		
		primRefreshArguments();
		
		args = getSourceCodeArgs() ;
		// Regenerate the arguments, only
		StringBuffer sb = new StringBuffer() ;
		for (int i=0; i<args.length; i++) {
			if (i>0) sb.append(", ") ; //$NON-NLS-1$
			sb.append(args[i]) ;
		}
		// Replace the arguments part	
		StringBuffer newExp = new StringBuffer(expSig) ;
		newExp.replace(start,end,sb.toString()) ;
		fExprSig = newExp.toString() ;
		return fExprSig ;

	}

	

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getComponentArguments(EObject)
	 */
	protected IJavaObjectInstance[] getComponentArguments(EObject root) {
		return new IJavaObjectInstance[0];
	}


	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getRootComponentSF()
	 */
	protected EStructuralFeature getRootComponentSF() {
		return fFmapper.getFeature(null);
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#getRootObject(boolean)
	 */
	protected EObject getRootObject(boolean cache) {
		if (fRootObj != null && cache)
			return fRootObj;
		if (fAddedPart != null) fAddedInstance = (IJavaObjectInstance) fAddedPart.getEObject() ;		
		// No actual MetaRoot ... so return null, if are not added to our parent
		EObject targetRoot = fbeanPart.getEObject();
		if (fAddedInstance != null &&
				((List)targetRoot.eGet(fFmapper.getFeature(fExpr))).contains(fAddedInstance)){
			targetRoot = fAddedInstance; 
			fRootObj = targetRoot;
		}else{
			targetRoot = null ;
		}
		return targetRoot;
	}
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IExpressionDecoderHelper#getAddedInstance()
	 */
	public Object[] getAddedInstance() {
		if (fAddedPart != null)
			return new Object[] { fAddedPart.getEObject(), null } ;
		else if (fAddedInstance != null)
			return new Object[] { fAddedInstance, null } ;
		
		return new Object[0] ;
	}	

	/**
	 * 
	 * In the case that the expression associated with this helper is disposed, the
	 * decode will require this helper to remove the child from the model completely 
	 * as to not to leave an orphaned child in the model
	 * 
	 * @since 1.0.0
	 */
	public void removeChildFromModel() {
		if (fAddedPart != null) {
			List ctrls = getRootComponentList();
			if (ctrls!=null && ctrls.contains(fAddedPart.getEObject())) // It is possible that the child was hijacked by another parent by the time we get here.			
			    fAddedPart.dispose();
		}
		else if (fAddedInstance != null) {
			if (fAddedInstance.eContainer() != null) {
				if (fAddedInstance.eContainingFeature().isMany())
				    ((EList)fAddedInstance.eContainer().eGet(fAddedInstance.eContainingFeature())).remove(fAddedInstance) ;
				else
					fAddedInstance.eContainer().eUnset(fAddedInstance.eContainingFeature());
			}
		}
	}
	/**
	 * If the child is declared in the same method as the parent, no
	 * need to call the child's creation method
	 * 
	 * @return true if an explicit call is required
	 * 
	 * @since 1.0.0
	 */
	protected boolean isInvocationCallNeeded() {
		IJavaObjectInstance obj ;
		if (fAddedPart!=null)
			 obj = (IJavaObjectInstance)fAddedPart.getEObject();
		else
			 obj = fAddedInstance;
		
		EObject cRef = InverseMaintenanceAdapter.getFirstReferencedBy(obj,JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		if (cRef == null) return false;
		EObject pRef = InverseMaintenanceAdapter.getFirstReferencedBy(fbeanPart.getEObject(),JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		return (!cRef.equals(pRef));
	}
	
	
	protected CodeExpressionRef generateInitExpression() {
		ExpressionRefFactory eGen = new ExpressionRefFactory(fAddedPart, JavaInstantiation.getAllocationFeature((IJavaInstance)fAddedPart.getEObject()));		
		try {
			CodeExpressionRef newExpr = eGen.createFromJVEModel(new Object[] { fbeanPart.getEObject() });
			newExpr.setState(CodeExpressionRef.STATE_INIT_EXPR, true);
			newExpr.insertContentToDocument();
			if (newExpr.isStateSet(CodeExpressionRef.STATE_INIT_EXPR)) {
				  if (!newExpr.getBean().getDecleration().isSingleDecleration())
					  newExpr.getBean().getDecleration().refreshDeclerationSource();
			}
			return newExpr;

		} catch (CodeGenException e) {
			JavaVEPlugin.log(e);
		}		
		return null;
	}
	
	/**
	 * In the case that noSRC expression is needed, the Constructor becomes the master
	 * of this expression ... for z ordering
	 * @return
	 * @todo Generated comment
	 */
	protected CodeExpressionRef getInitExpression() {
		CodeExpressionRef init =  fAddedPart.getInitExpression();
		if (init==null) {			
			Iterator itr = fAddedPart.getNoSrcExpressions().iterator();
			while (itr.hasNext()) {
				CodeExpressionRef e = (CodeExpressionRef) itr.next();
				if (e.isStateSet(CodeExpressionRef.STATE_INIT_EXPR))
					return e;
			}
			return generateInitExpression();
		}
		return init;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#generateSrc()
	 */
	protected String generateSrc() {

		if (isInvocationCallNeeded()) {
			// Generate method call
		  IMethodInvocationGenerator iGen = new DefaultMethodInvocationGenerator() ;				
		  return iGen.generateMethodInvocation(fAddedPart.getInitMethod().getMethodName()) ;
		}
		else {
			// This feature has no src.
			fOwner.getExprRef().setNoSrcExpression(true);
			CodeExpressionRef mastered = getInitExpression() ;
			fOwner.getExprRef().setMasteredExpression(mastered);
			if (mastered.isStateSet(CodeExpressionRef.STATE_NO_SRC)) {
				// master was not generated yet, .. snooze it
				mastered.setNoSrcExpression(false);
				try {
					mastered.generateSource(null);
					mastered.getMethod().updateExpressionOrder();
					mastered.insertContentToDocument();
				} catch (CodeGenException e) {
					JavaVEPlugin.log(e);
				}
			}
			return null ;
		}
	}


	protected BeanPart parseNoArgAddedPart(MethodInvocation exp) throws CodeGenException {
		// This decoder assume that this expression is adding a child to its bean
		// with a createFoo()... During the resolution, the child must be put
		// as an argumet
		if (fOwner.getExprRef().getArgs() == null || fOwner.getExprRef().getArgs().length<1)
		   throw new CodeGenException("No Arguments !!! " + exp) ; //$NON-NLS-1$
		return fOwner.getBeanModel().getABean((EObject)fOwner.getExprRef().getArgs()[0]);
	}
	
	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getSFPriority()
	 */
	protected int getSFPriority() {
		// The add priority is tied to a constructor's priority
		return IJavaFeatureMapper.PRIORITY_CONSTRUCTOR ;
	}	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getIndexPriority()
	 */
	protected IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex getIndexPriority() {
		
		EStructuralFeature sf =fFmapper.getFeature(null);
		int index = SWTConstructorDecoderHelper.getIndexPriority(fAddedPart,getIndexedEntries());
		EObject parent = fbeanPart.getEObject();
		return new IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex(sf, index, parent);
	}
	protected int getAddedPartArgIndex(int argsSize) {		
		return 0;
	}

	public boolean primIsDeleted() {
		EObject rootObj = getRootObject(false);
		if (rootObj == null) {
			if (fAddedPart != null && fbeanPart!=null){
				fbeanPart.removeChild(fAddedPart);
			}
		}
		return super.primIsDeleted();
	}

}
