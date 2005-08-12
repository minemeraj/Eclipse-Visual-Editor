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
 *  $RCSfile: ScrollPaneProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-12 21:36:29 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
/**
 * See bug 69514 - Dropping AWT ScrollPane fails on Linux
 * Exceptions are thrown because the scroll pane doesn't have children.
 * To prevent this, we'll add a temporary child (a special Panel) when there isn't one. 
 * This is done when the scroll pane is first instanciated and when a child is removed.
 * 
 * @since 1.1.0.1
 */

public class ScrollPaneProxyAdapter extends ContainerProxyAdapter {

	public ScrollPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected void componentRemoved(EObject aConstraintComponent, IExpression expression, boolean aboutToRelease) {
		makeItRight(expression, getProxy());
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		IProxy scrollPaneProxy = super.primInstantiateBeanProxy(expression);
		makeItRight(expression, scrollPaneProxy);
		return scrollPaneProxy;
	}

	private void makeItRight(IExpression expression, IProxy scrollPaneProxy) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getScrollPaneMakeItRight(expression), null, new IProxy[] { scrollPaneProxy}, false);
	}
}
