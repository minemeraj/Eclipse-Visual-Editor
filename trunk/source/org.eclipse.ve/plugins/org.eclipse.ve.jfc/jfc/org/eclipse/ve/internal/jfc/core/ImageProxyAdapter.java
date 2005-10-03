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
/*
 *  $RCSfile: ImageProxyAdapter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-03 19:21:01 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.ParseTreeAllocationInstantiationVisitor.ProcessingException;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.InfixOperator;

import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
 
/**
 * Proxy adapter for AWT Images.
 * <p>
 * We need special function on creation because of an AWT bug. This bug
 * causes a infinite wait if the image was created from a null URL. So
 * this adapter will try to figure this out and prevent the wait.
 * <p>
 * Image is an interface, so this will only work if the IJavaObject is
 * of type "java.awt.Image" before the proxy adapter has been created. If
 * it is not of Image then there is no way to add this adapter because
 * the adapter factory only looks at the superclass heirarchy, not the
 * interface heirarchy. 
 * <p>
 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=97924 .
 * @since 1.1.0
 */
public class ImageProxyAdapter extends BeanProxyAdapter {

	public ImageProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	protected IProxy primInstantiateDroppedPart(IExpression expression) throws AllocationException {
		// Check if we have somewhere a getImage(). If we do, put on to the expression
		// a separate test for the result of the argument to getImage(), and if null, throw an NPE.
		if (getJavaObject().isParseTreeAllocation()) {
			try {
				ParseTreeAllocation ptAlloc = (ParseTreeAllocation) getJavaObject().getAllocation();
				try {
					ptAlloc.getExpression().accept(new ImageParseVisitor(expression));
				} catch (ProcessingException e) {
					if (e.getCause() instanceof AllocationException)
						throw (AllocationException) e.getCause();
					// Anything else should actually be "null" and it means we found a getImage or there was no getImage(). It means go on.
				}					
			} catch(ClassCastException e) {
				// This is ok. just ignore.
			} 
		}
		return super.primInstantiateDroppedPart(expression);
	}
	
	private class ImageParseVisitor extends ParseVisitor {
		private IExpression expression;
		
		public ImageParseVisitor(IExpression expression) {
			this.expression = expression;
		}
		public boolean visit(PTMethodInvocation node) {
			if ("getImage".equals(node.getName()) && node.getArguments().size() == 1) {
				// Create a verification expression. It will simply take what it was, do the allocation, and test the result. If null, it will throw
				// an NPE with a message. This will stop the rest of the allocation from taking place.
				// If it worked, it will be reevaluated again in the full expression, but that is ok. This is not the frequent an operation.
				try {
					IProxy image = getBeanProxyDomain().getAllocationProcesser().allocate((PTExpression) node.getArguments().get(0), expression);
					expression.createIfElse(false);
					expression.createInfixExpression(ForExpression.IF_CONDITION, InfixOperator.IN_EQUALS, 0);
					expression.createProxyExpression(ForExpression.INFIX_LEFT, image);
					expression.createNull(ForExpression.INFIX_RIGHT);
					expression.createBlockBegin();
					expression.createThrow();
					expression.createClassInstanceCreation(ForExpression.THROW_OPERAND, expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, "java.lang.NullPointerException"), 1);
					expression.createStringLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, JFCMessages.ImageProxyAdapter_imageurlnotfound);
					expression.createBlockEnd();
					throw new ProcessingException(null);	// This just stops the walking of the rest of the expression.
				} catch (AllocationException e) {
					throw new ProcessingException(e);
				}
			}
			return super.visit(node);
		}
	}

}
