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
 *  $Revision: 1.1 $  $Date: 2005-05-07 00:55:19 $ 
 */

package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.PTClassInstanceCreation;
import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.internal.instantiation.PTInstanceReference;
import org.eclipse.jem.internal.instantiation.PTName;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.java.codegen.core.DefaultCopyEditPolicy;

public class WidgetCopyEditPolicy extends DefaultCopyEditPolicy {
	
	protected void normalize(IJavaInstance javaBean) {
		super.normalize(javaBean);
		// The allocation may contain references to the parent Composite
		// These should be replaced with the {parentComposite} for when it is pasted into the new target
		JavaAllocation allocation = javaBean.getAllocation();
		if(allocation instanceof ParseTreeAllocation){
			// An SWT Constructor contains a constructor with two arguments, the first of which is the parent
			PTExpression expression = ((ParseTreeAllocation)allocation).getExpression();
			if(expression instanceof PTClassInstanceCreation){
				PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation)expression;
				List arguments = classInstanceCreation.getArguments();
				if(arguments.size() != 2) return;
				replaceExplicitParent(javaBean, classInstanceCreation);
			}
		}
	}

	private void replaceExplicitParent(IJavaInstance javaBean, PTClassInstanceCreation expression) {
		Object firstArgument = expression.getArguments().get(0);
		// The first argment will be a PTJavaObjectInstance that points to the parent
		if(firstArgument instanceof PTInstanceReference){
			// Create a PTName for {parentComposite}
			PTName parentCompositeName = InstantiationFactory.eINSTANCE.createPTName(SwtPlugin.PARENT_COMPOSITE_TOKEN);
			expression.getArguments().set(0,parentCompositeName);
		}
	}
}
