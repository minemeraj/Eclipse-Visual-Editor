/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;

/*
 *  $RCSfile: FrameConstructorProxyAdapter.java,v $
 *  $Revision: 1.13 $  $Date: 2005-06-22 21:05:25 $ 
 */

import java.util.List;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * A Proxy adapter for classes that require a Frame as a parent to be able to be constructed. E.G. awt.Dialog. Will check the allocation to see if
 * there is a "new Frame()" in it. If so, it will dispose that frame on release. It assumes that there will never be an invalid allocation (e.g. a new
 * Dialog()). Since this is invalid as code, and we shouldn't create such code.
 * 
 * @since 1.0.0
 */
public class FrameConstructorProxyAdapter extends WindowProxyAdapter {

	protected boolean disposeParentOnRelease;

	/**
	 * Construct FrameConstructor proxy adapter.
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public FrameConstructorProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected IProxy primInstantiateDroppedPart(IExpression expression) throws AllocationException {
		// Override to see if the allocation has a "new java.awt.Frame" in it. If it does, we need to
		// grab that frame so that we can dispose it later.
		// TODO This is really a bad way to do this. We should never have a temporary like this.
		// The code shouldn't even generate something like this.
		disposeParentOnRelease = false;
		if (getJavaObject().isSetAllocation()) {
			JavaAllocation allocation = getJavaObject().getAllocation();
			if (allocation instanceof ParseTreeAllocation) {
				// Can only handle parse tree, and only if Frame is first argument.
				PTExpression allocExp = ((ParseTreeAllocation) allocation).getExpression();
				if (allocExp instanceof PTClassInstanceCreation) {
					PTClassInstanceCreation newClass = (PTClassInstanceCreation) allocExp;
					List args = newClass.getArguments();
					if (args.size() == 1) {
						PTExpression arg1 = (PTExpression) args.get(0);
						disposeParentOnRelease = arg1 instanceof PTClassInstanceCreation
								&& "java.awt.Frame".equals(((PTClassInstanceCreation) arg1).getType()); //$NON-NLS-1$
					}
				}
			}
		}

		return super.primInstantiateDroppedPart(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#primInstantiateThisPart(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected IProxy primInstantiateThisPart(IExpression expression) {
		// We need to override so that we can generate the fake Frame that is required for Dialogs.
		// We are assuming that we find a class that has a constructor with only Frame for a parent.
		IProxyBeanType targetClass = getValidSuperClass(expression);

		disposeParentOnRelease = true;
		IProxy result = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
		expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, targetClass, 1);
		expression.createClassInstanceCreation(ForExpression.CLASSINSTANCECREATION_ARGUMENT, "java.awt.Frame", 0); //$NON-NLS-1$
		return result;
	}

	protected void primReleaseBeanProxy(IExpression expression) {
		if (disposeParentOnRelease && isBeanProxyInstantiated()) {
			// Execute: WindowManager.disposeWindow(dialog.getParent());
			expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, BeanAwtUtilities.getWindowDisposeMethodProxy(expression), false, 0);
			expression.createMethodInvocation(ForExpression.METHOD_ARGUMENT, BeanAwtUtilities.getParentMethodProxy(expression), true, 0);
			expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getProxy());
		}
		;
		disposeParentOnRelease = false;
		super.primReleaseBeanProxy(expression);
	}
}