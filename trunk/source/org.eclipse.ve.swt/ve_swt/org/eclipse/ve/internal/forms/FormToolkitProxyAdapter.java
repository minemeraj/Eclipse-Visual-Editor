/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: FormToolkitProxyAdapter.java,v $ $Revision: 1.2 $ $Date: 2006-05-17 20:15:54 $
 */
package org.eclipse.ve.internal.forms;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

import org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter;


public class FormToolkitProxyAdapter extends UIThreadOnlyProxyAdapter {

	public FormToolkitProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected void primPrimReleaseBeanProxy(IExpression expression) {
		if(isOwnsProxy() && isBeanProxyInstantiated()) {
			IBeanProxy resourceBeanProxy = getBeanProxy();
			expression.createSimpleMethodInvoke(resourceBeanProxy.getTypeProxy().getMethodProxy(expression, "dispose"), resourceBeanProxy, //$NON-NLS-1$
				null, false);
		}		
	}

}
