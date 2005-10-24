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
 *  $RCSfile: TableViewerProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-24 21:36:04 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

import org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter;

/**
 * 
 * @since 1.2.0
 */
public class TableViewerProxyAdapter extends UIThreadOnlyProxyAdapter {

	private EStructuralFeature sf_table;

	public TableViewerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_table = ((EObject) newTarget).eClass().getEStructuralFeature("table");
	}

	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, final IExpression expression, final ForExpression forExpression) {
		if (true)
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
		if (aBeanPropertyFeature == sf_table) {
			if (!onUIThread()) {
				return (IProxy) invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

					protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
						return getTableProxy(expression, forExpression);
					}
				});
			} else {
				return getTableProxy(expression, forExpression);
			}
		} else {
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
		}
	}

	protected IProxy getTableProxy(IExpression expression, ForExpression forExpression) {
		ExpressionProxy result = expression.createProxyAssignmentExpression(forExpression);
		expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, "getTable", true, 0);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getBeanProxy());
		return result;
	}

	protected void primPrimReleaseBeanProxy(IExpression expression) {
	}

}
