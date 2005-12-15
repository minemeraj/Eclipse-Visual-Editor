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
 *  $Revision: 1.12 $  $Date: 2005-12-15 14:55:17 $ 
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.DefaultCopyEditPolicy;

public class WidgetCopyEditPolicy extends DefaultCopyEditPolicy {
	
	public WidgetCopyEditPolicy(EditDomain anEditDomain) {
		super(anEditDomain);
	}
	
	protected boolean shouldCopyFeature(EStructuralFeature feature, Object eObject) {		
		if(feature != null){
			if ("allocation".equals(feature.getName()) //$NON-NLS-1$
			 || eObject instanceof EStructuralFeature){ // Implicit allocation points to the meta layer and we don't want to copy up into this
				return false;				
			}
		}
		return super.shouldCopyFeature(feature, eObject);
	}
	
	protected boolean shouldExpandFeature(EStructuralFeature feature, Object eObject) {
		if(eObject instanceof EStructuralFeature){
			return false;
		} 
		if(feature != null && feature.getName().equals("allocation")){
			return false;
		}
		return super.shouldExpandFeature(feature,eObject);
	}	

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
				replaceParentCompositeControlToken(javaBean,classInstanceCreation); 
			} 
		}
	}
	
	private void replaceParentCompositeControlToken(IJavaInstance javaBean, PTClassInstanceCreation classInstanceCreation) {
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
}
