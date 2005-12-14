/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $Revision: 1.19 $  $Date: 2005-12-14 21:37:04 $ 
 */

import java.util.List;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
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

	protected IProxy primPrimInstantiateDroppedPart(IExpression expression) throws AllocationException {
		// Override to see if the allocation has a "new java.awt.Frame" in it. If it does, we need to
		// grab that frame so that we can dispose it later.
		// TODO This is really a bad way to do this. We should never have a temporary like this.
		// The code shouldn't even generate something like this.
		disposeParentOnRelease = false;
		if (getJavaObject().isParseTreeAllocation()) {
			// Can only handle parse tree, and only if Frame is first argument.
			ParseTreeAllocation allocation = (ParseTreeAllocation) getJavaObject().getAllocation();
			PTExpression allocExp = allocation.getExpression();
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

		return super.primPrimInstantiateDroppedPart(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#primInstantiateThisPart(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected IProxy primPrimInstantiatedThisPart(IProxyBeanType targetClass, IExpression expression) {
		// We need to override so that we can generate the fake Frame that is required for Dialogs.
		// We are assuming that we find a class that has a constructor with only Frame for a parent.
		// If we can't find a ctor with Frame as the parameter, try the null ctor.

		disposeParentOnRelease = true;
		expression.createTry();
		ExpressionProxy result = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
		expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, targetClass, 1);
		expression.createClassInstanceCreation(ForExpression.CLASSINSTANCECREATION_ARGUMENT, "java.awt.Frame", 0); //$NON-NLS-1$
		ExpressionProxy exception = expression.createTryCatchClause(getBeanTypeProxy("java.lang.NoSuchMethodException", expression), true); //$NON-NLS-1$
		// Reassign the expression and create with a null ctor.
		expression.createProxyReassignmentExpression(ForExpression.ROOTEXPRESSION, result);
		expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, targetClass, 0);
		exception.addProxyListener(new ExpressionProxy.ProxyAdapter() {
			public void proxyResolved(ProxyEvent event) {
				// Set flag to false so we don't try to dispose the shared internal frame during the release
				disposeParentOnRelease = false;
			}
		});
		expression.createTryEnd();
		return result;
	}

	protected void primReleaseBeanProxy(IExpression expression) {
		if (disposeParentOnRelease && isBeanProxyInstantiated()) {
			// Execute: WindowManager.disposeWindow(dialog.getParent());
			expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, BeanAwtUtilities.getWindowDisposeMethodProxy(expression), false, 1);
			expression.createMethodInvocation(ForExpression.METHOD_ARGUMENT, BeanAwtUtilities.getParentMethodProxy(expression), true, 0);
			expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getProxy());
		}
		;
		disposeParentOnRelease = false;
		super.primReleaseBeanProxy(expression);
	}
}
