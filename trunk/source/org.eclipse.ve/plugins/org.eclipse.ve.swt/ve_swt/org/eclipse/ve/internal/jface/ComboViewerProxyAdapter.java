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
 *  $RCSfile: ComboViewerProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-25 19:12:43 $ 
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
public class ComboViewerProxyAdapter extends UIThreadOnlyProxyAdapter {
	private EStructuralFeature sf_combo;

	public ComboViewerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_combo = ((EObject) newTarget).eClass().getEStructuralFeature("combo");
	}

	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, final IExpression expression, final ForExpression forExpression) {
		if (true)
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
		if (aBeanPropertyFeature == sf_combo) {
			if (!onUIThread()) {
				return (IProxy) invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

					protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
						return getComboProxy(expression, forExpression);
					}
				});
			} else {
				return getComboProxy(expression, forExpression);
			}
		} else {
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
		}
	}

	protected IProxy getComboProxy(IExpression expression, ForExpression forExpression) {
		ExpressionProxy result = expression.createProxyAssignmentExpression(forExpression);
		expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, "getCombo", true, 0);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getBeanProxy());
		return result;
	}

	protected void primPrimReleaseBeanProxy(IExpression expression) {
	}

}
