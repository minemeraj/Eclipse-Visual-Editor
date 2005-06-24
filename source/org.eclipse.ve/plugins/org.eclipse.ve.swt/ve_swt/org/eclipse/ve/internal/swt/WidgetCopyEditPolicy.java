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
 *  $RCSfile: WidgetCopyEditPolicy.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-24 14:31:24 $ 
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.DefaultCopyEditPolicy;

public class WidgetCopyEditPolicy extends DefaultCopyEditPolicy {
	
	private JavaClass formToolkitType;

	protected void preExpand(IJavaInstance javaBean) {
		super.preExpand(javaBean);
		// Only normalize our host otherwise we end up turning children to point to the {parentComposite}
		// when they should still point to use
		if(!(javaBean == getHost().getModel())) return;
		
		IJavaInstance copiedJavaBean = (IJavaInstance) copier.get(javaBean);
		// The allocation may contain references to the parent Composite
		// These should be replaced with the {parentComposite} for when it is pasted into the new target
		JavaAllocation allocation = copiedJavaBean.getAllocation();
		if(allocation instanceof ParseTreeAllocation){
			// An SWT Constructor contains a constructor with two arguments, the first of which is the parent
			PTExpression expression = ((ParseTreeAllocation)allocation).getExpression();
			if(expression instanceof PTClassInstanceCreation){
				PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation)expression;
				replaceParentCompositeToken(copiedJavaBean,classInstanceCreation.getArguments()); 
			} else if (expression instanceof PTMethodInvocation){
				// Form toolkit factories can reference the FormToolkit which is replaced with the {formToolkit} token
				PTMethodInvocation methodInvocation = (PTMethodInvocation)expression;
				replaceFormToolkitToken(methodInvocation.getArguments());
				replaceParentCompositeToken(copiedJavaBean,methodInvocation.getArguments());				
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
			if(arg instanceof PTInstanceReference && getFormToolkitType().isAssignableFrom(((PTInstanceReference)arg).getObject().getJavaType())){
				// Create a PTName for {formToolkit}
				PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.FORM_TOOLKIT_TOKEN);
				arguments.set(i,parentCompositeName);				
			}
		}
	}

	private void replaceParentCompositeToken(IJavaInstance parent, List arguments) {
		for(int i=0; i<arguments.size(); i++){
			Object arg = arguments.get(i);
			// If the argument is an instance reference that points to the parent then replace it with the token
			if(arg instanceof PTInstanceReference && ((PTInstanceReference)arg).getObject() == parent){
				// Create a PTName for {parentComposite}
				PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.PARENT_COMPOSITE_TOKEN);
				arguments.set(i,parentCompositeName);
			}
		}
	}
}
