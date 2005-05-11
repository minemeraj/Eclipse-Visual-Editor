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
package org.eclipse.ve.examples.java;

/*
 *  $RCSfile: BasicTypesBeanProxyAdapter.java,v $
 *  $Revision: 1.6 $  $Date: 2005-05-11 19:01:23 $ 
 */

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.BeanProxyAdapter2;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * This bean proxy adapter sends a dispose method to the live bean when it is disposed. This is because the live bean has a Frame within it that holds
 * onto resources and needs custom code to dispose of its resources.
 */
public class BasicTypesBeanProxyAdapter extends BeanProxyAdapter2 {

	public BasicTypesBeanProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2#releaseBeanProxy(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public void releaseBeanProxy(IExpression expression) {
		if (isBeanProxyInstantiated() && isOwnsProxy()) {
			IProxyMethod disposeFrameMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy(expression, "disposeFrame", (IProxyBeanType[]) null); //$NON-NLS-1$
			expression.createSimpleMethodInvoke(disposeFrameMethodProxy, getBeanProxy(), null, false);
		}
		super.releaseBeanProxy(expression);

	}
}