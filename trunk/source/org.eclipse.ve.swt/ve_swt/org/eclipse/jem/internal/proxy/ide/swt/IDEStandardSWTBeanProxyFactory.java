/*******************************************************************************
 * Copyright (c) 2005IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.ide.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.jem.internal.proxy.core.IPointBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.ide.IDEProxyFactoryRegistry;
import org.eclipse.jem.internal.proxy.swt.IStandardSWTBeanProxyFactory;

/**
 * A standard SWT Bean proxy factory extension. Used for SWT bean proxies.
 * 
 * @since 1.1.0
 */
public class IDEStandardSWTBeanProxyFactory implements IStandardSWTBeanProxyFactory {

	final IDEStandardSWTBeanTypeProxyFactory fBeanTypeFactory;

	public static void registerBeanTypeProxyFactory(IDEProxyFactoryRegistry aRegistry) {
		if (aRegistry.getBeanTypeProxyFactoryExtension(IStandardSWTBeanProxyFactory.REGISTRY_KEY) == null)
			new IDEStandardSWTBeanProxyFactory(aRegistry);
	}

	private IDEStandardSWTBeanProxyFactory(IDEProxyFactoryRegistry factory) {
		factory.registerBeanProxyFactory(IStandardSWTBeanProxyFactory.REGISTRY_KEY, this);
		fBeanTypeFactory = (IDEStandardSWTBeanTypeProxyFactory) factory
				.getBeanTypeProxyFactoryExtension(IDEStandardSWTBeanTypeProxyFactory.BEAN_TYPE_FACTORY_KEY);
	}

	public IPointBeanProxy createPointBeanProxyWith(int x, int y) {
		return (IPointBeanProxy) fBeanTypeFactory.pointBeanType.newBeanProxy(new Point(x, y));
	}

	public IRectangleBeanProxy createBeanProxyWith(int x, int y, int width, int height) {
		return (IRectangleBeanProxy) fBeanTypeFactory.rectangleBeanType.newBeanProxy(new Rectangle(x, y, width, height));
	}

	public void terminateFactory(boolean wait) {
	}

}
