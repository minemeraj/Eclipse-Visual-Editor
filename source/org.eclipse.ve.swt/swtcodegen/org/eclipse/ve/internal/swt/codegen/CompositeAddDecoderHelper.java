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
 *  $RCSfile: CompositeAddDecoderHelper.java,v $
 *  $Revision: 1.6 $  $Date: 2004-03-05 23:18:50 $ 
 */
package org.eclipse.ve.internal.swt.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.java.*;
import org.eclipse.ve.internal.java.codegen.model.BeanDeclModel;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.*;
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

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.AbstractContainerAddDecoderHelper#parseAddedPart(MessageSend)
	 */
	protected BeanPart parseAddedPart(MethodInvocation exp) throws CodeGenException {
		// TODO  Need to deal with multiple arguments, and nesting
		
		if (exp == null) return null ;
		
		BeanPart bp = null ;
		
		List args = exp.arguments() ;
		if (args.size() < 1) throw new CodeGenException("No Arguments !!! " + exp) ; //$NON-NLS-1$
		
		// Parse the arguments to figure out which bean to add to this container
		if (args.get(0) instanceof MethodInvocation)  {
			// Look to see of if this method returns a Bean
			String selector = ((MethodInvocation)args.get(0)).getName().getIdentifier();  
			bp = fOwner.getBeanModel().getBeanReturned(selector) ;       	
		}
		else if (args.get(0) instanceof SimpleName){
			// Simple reference to a bean
			String beanName =((SimpleName)args.get(0)).getIdentifier();
			bp = fOwner.getBeanModel().getABean(beanName) ;
			if(bp==null)
				bp = fOwner.getBeanModel().getABean(BeanDeclModel.constructUniqueName(fOwner.getExprRef().getMethod(),beanName));
			//bp = fOwner.getBeanModel().getABean(fOwner.getExprRef().getMethod().getMethodHandle()+"^"+beanName);
		}
		else if (args.get(0) instanceof ClassInstanceCreation) {
			String clazzName = CodeGenUtil.resolve(((ClassInstanceCreation)args.get(0)).getName(), fbeanPart.getModel());
			IJavaObjectInstance obj = (IJavaObjectInstance) CodeGenUtil.createInstance(clazzName,fbeanPart.getModel().getCompositionModel()) ;
			JavaClass c = (JavaClass) obj.getJavaType() ;
			if (c.isExistingType()) fAddedInstance = obj ;      
		}
		return bp ;
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
		if (fAddedPart != null) fAddedInstance = fAddedPart.getEObject() ;		
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
		if (fAddedPart != null)
			 fAddedPart.dispose();	 
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
			 obj = (IJavaObjectInstance)fAddedInstance;
		
		EObject cRef = InverseMaintenanceAdapter.getFirstReferencedBy(obj,JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		if (cRef == null) return false;
		EObject pRef = InverseMaintenanceAdapter.getFirstReferencedBy(fbeanPart.getEObject(),JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		return (!cRef.equals(pRef));
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
			fOwner.getExprRef().setNoSrcExpression();
			return null ;
		}
	}


}
