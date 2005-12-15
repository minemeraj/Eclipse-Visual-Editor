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
 *  $RCSfile: WidgetCopyEditPolicy.java,v $
 *  $Revision: 1.11 $  $Date: 2005-12-15 01:03:51 $ 
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.DefaultCopyEditPolicy;

public class WidgetCopyEditPolicy extends DefaultCopyEditPolicy {
	
	public WidgetCopyEditPolicy(EditDomain anEditDomain) {
		super(anEditDomain);
	}

	private JavaClass formToolkitType;

	protected void preExpand(IJavaInstance javaBean) {
		super.preExpand(javaBean);
		// Only normalize our host otherwise we end up turning children to point to the {parentComposite}
		// when they should still point to use
		if(!(javaBean == getHost().getModel())) return;
		
		// The allocation may contain references to the parent Composite
		// These should be replaced with the {parentComposite} for when it is pasted into the new target
		JavaAllocation allocation = javaBean.getAllocation();
		if(allocation.isParseTree()){
			// An SWT Constructor contains a constructor with two arguments, the first of which is the parent
			PTExpression expression = ((ParseTreeAllocation)allocation).getExpression();
			if(expression instanceof PTClassInstanceCreation){
				PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation)expression;
				replaceParentCompositeToken(javaBean,classInstanceCreation); 
			} else if (expression instanceof PTMethodInvocation){
				// Form toolkit factories can reference the FormToolkit which is replaced with the {formToolkit} token
				PTMethodInvocation methodInvocation = (PTMethodInvocation)expression;
				replaceFormToolkitToken(methodInvocation.getArguments());
//				replaceParentCompositeToken(copiedJavaBean,methodInvocation.getArguments());				
			}
		}
	}
	
	private void replaceParentCompositeToken(IJavaInstance javaBean, PTClassInstanceCreation classInstanceCreation) {
		List arguments = classInstanceCreation.getArguments();
		for(int i=0; i<arguments.size(); i++){
			Object arg = arguments.get(i);
			// If the argument is an instance reference that points to the parent then replace it with the token
			if(arg instanceof PTInstanceReference){
				IJavaInstance potentialParent = ((PTInstanceReference)arg).getReference();
				List controls = (List) potentialParent.eGet(potentialParent.eClass().getEStructuralFeature("controls"));
				if(controls.indexOf(javaBean) != -1){
					// Manipulate the allocation of the JavaBean in the copy set
					IJavaInstance copiedJavaBean = (IJavaInstance) copier.get(javaBean);
					PTExpression expression = ((ParseTreeAllocation)copiedJavaBean.getAllocation()).getExpression();
					List copiedArgs = ((PTClassInstanceCreation)expression).getArguments();
					// Create a PTName for {parentComposite}
					PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.PARENT_COMPOSITE_TOKEN);
					copiedArgs.set(i,parentCompositeName);
				}
			}
		}
	}

	private JavaClass getFormToolkitType(){
		if(formToolkitType == null){
			ResourceSet resourceSet = ((EObject)getHost().getModel()).eResource().getResourceSet();
			formToolkitType = Utilities.getJavaClass(SwtPlugin.FORM_TOOLKIT_CLASSNAME,resourceSet);
		}
		return formToolkitType;
	}
	
	private void replaceFormToolkitToken(List arguments) {
		for(int i=0; i<arguments.size(); i++){
			Object arg = arguments.get(i);
			if(arg instanceof PTInstanceReference && getFormToolkitType().isAssignableFrom(((PTInstanceReference)arg).getReference().getJavaType())){
				// Create a PTName for {formToolkit}
				PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.FORM_TOOLKIT_TOKEN);
				arguments.set(i,parentCompositeName);				
			}
		}
	}
/**
	private void replaceParentCompositeToken(IJavaInstance controlBean, List arguments) {
		for(int i=0; i<arguments.size(); i++){
			Object arg = arguments.get(i);
			// If the argument is an instance reference that points to the parent then replace it with the token
			if(arg instanceof PTInstanceReference){
				IJavaInstance potentialParent = ((PTInstanceReference)arg).getReference();
				List controls = (List) potentialParent.eGet(potentialParent.eClass().getEStructuralFeature("controls"));
				if(controls.indexOf(controlBean) != -1){
					// Create a PTName for {parentComposite}
					PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.PARENT_COMPOSITE_TOKEN);
					arguments.set(i,parentCompositeName);
				}
			}
		}
	}
 **/	
}
