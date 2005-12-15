/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.emf.ecore.*;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

public class TabItemCopyEditPolicy extends WidgetCopyEditPolicy {

	public TabItemCopyEditPolicy(EditDomain anEditDomain) {
		super(anEditDomain);
	}
	
	protected IJavaInstance getRootBean() {
		// The Control is on a TabItem.  Start from the TabItem rather than the control to ensure properties
		// like the tabText, tabIcon, on the Item are copied.  The control will be picked up because it is the
		// "control" feature value of the TabItem
		IJavaInstance controlBean = super.getRootBean();
		IJavaInstance tabItemInstance = (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(controlBean,getTabItemControlSF());
		return tabItemInstance;
		
	}
	
	private EReference getTabItemControlSF(){
		JavaClass tabItemClass = Utilities.getJavaClass("org.eclipse.swt.widgets.TabItem", ((EObject)getHost().getModel()).eResource().getResourceSet());
		return (EReference) tabItemClass.getEStructuralFeature("control");		
	}

	protected void preExpand(IJavaInstance javaBean) {
		super.preExpand(javaBean);
		// See whether or not we are pre-expanding the TabItem itself
		IJavaInstance control = (IJavaInstance) getHost().getModel();
		IJavaInstance tabItemInstance = (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(control,getTabItemControlSF());		
		if(tabItemInstance == javaBean){
			// The allocation may contain references to the parent Composite
			// These should be replaced with the {parentComposite} for when it is pasted into the new target
			JavaAllocation allocation = javaBean.getAllocation();
			if(allocation.isParseTree()){
			// An SWT Constructor contains a constructor with two arguments, the first of which is the parent
				PTExpression expression = ((ParseTreeAllocation)allocation).getExpression();
				if(expression instanceof PTClassInstanceCreation){
					PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation)expression;
					replaceParentCompositeItemToken(javaBean,classInstanceCreation);
				}
			} 
		}
	}	
	private void replaceParentCompositeItemToken(IJavaInstance javaBean, PTClassInstanceCreation classInstanceCreation) {
		List arguments = classInstanceCreation.getArguments();
		for(int i=0; i<arguments.size(); i++){
			Object arg = arguments.get(i);
			// If the argument is an instance reference that points to the parent then replace it with the token
			if(arg instanceof PTInstanceReference){
				IJavaInstance potentialParent = ((PTInstanceReference)arg).getReference();
				List controls = (List) potentialParent.eGet(potentialParent.eClass().getEStructuralFeature("items"));
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
