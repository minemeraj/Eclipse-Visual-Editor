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
 *  $RCSfile: AppletProxyAdapter.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * Applet proxy adapter
 * 
 * @since 1.1.0
 */
public class AppletProxyAdapter extends ContainerProxyAdapter {

	/**
	 * Construct Applet proxy adapter
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public AppletProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		// We need to initialize the applet after creation. Applets should actually be instantiated using java.lang.reflect.Beans.instantiate()
		// but we don't recognize this. So we do normal instantiation and then initialize with our dummy applet stub that we provide.
		IProxy result = super.primInstantiateBeanProxy(expression);

		IProxyBeanType dummyStubType = getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.DummyAppletStub", expression); //$NON-NLS-1$
		IProxyMethod initializeMethod = dummyStubType.getMethodProxy(expression, "initializeApplet", new String[] { "java.applet.Applet"}); //$NON-NLS-1$ //$NON-NLS-2$
		expression.createSimpleMethodInvoke(initializeMethod, null, new IProxy[] { result}, false);
		return result;
	}


	protected void primReleaseBeanProxy(IExpression expression) {
		// When an applet is removed from the system it should be called with destroy so it
		// can free up any resources it allocated

		if (isBeanProxyInstantiated()) {
			IProxyMethod destroyMethod = getBeanProxy().getTypeProxy().getMethodProxy(expression, "destroy");	//$NON-NLS-1$
			expression.createSimpleMethodInvoke(destroyMethod, getProxy(), null, false);
		}
		super.primReleaseBeanProxy(expression);
	}

}

