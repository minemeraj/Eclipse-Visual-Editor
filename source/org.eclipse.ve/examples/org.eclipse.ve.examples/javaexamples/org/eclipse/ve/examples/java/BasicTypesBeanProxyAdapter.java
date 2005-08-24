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
package org.eclipse.ve.examples.java;

/*
 *  $RCSfile: BasicTypesBeanProxyAdapter.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:16:43 $ 
 */

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * This bean proxy adapter sends a dispose method to the live bean when it is disposed. This is because the live bean has a Frame within it that holds
 * onto resources and needs custom code to dispose of its resources.
 */
public class BasicTypesBeanProxyAdapter extends BeanProxyAdapter {

	public BasicTypesBeanProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected void primReleaseBeanProxy(IExpression expression) {
		if (isBeanProxyInstantiated() && isOwnsProxy()) {
			IProxyMethod disposeFrameMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy(expression, "disposeFrame", (IProxyBeanType[]) null); //$NON-NLS-1$
			expression.createSimpleMethodInvoke(disposeFrameMethodProxy, getBeanProxy(), null, false);
		}
		super.primReleaseBeanProxy(expression);

	}
}
