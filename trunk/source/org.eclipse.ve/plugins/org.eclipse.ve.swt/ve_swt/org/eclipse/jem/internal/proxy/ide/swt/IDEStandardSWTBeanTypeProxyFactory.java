/*****************************************************************************************************************************************************
 * Copyright (c) 2005 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
package org.eclipse.jem.internal.proxy.ide.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.ide.*;

/**
 * The standard REM SWT BeanType proxy factory extension. Used for SWT bean type proxies.
 * 
 * @since 1.1.0
 */
public class IDEStandardSWTBeanTypeProxyFactory implements IDEExtensionBeanTypeProxyFactory {

	static final String BEAN_TYPE_FACTORY_KEY = "org.eclipse.swt.graphics"; //$NON-NLS-1$

	protected final IDEProxyFactoryRegistry fFactoryRegistry;

	protected IDESWTRectangleBeanTypeProxy rectangleBeanType;

	protected IDESWTPointBeanTypeProxy pointBeanType;

	public static void registerBeanTypeProxyFactory(IDEProxyFactoryRegistry aRegistry) {
		if (aRegistry.getBeanTypeProxyFactoryExtension(BEAN_TYPE_FACTORY_KEY) == null)
			new IDEStandardSWTBeanTypeProxyFactory(aRegistry);
	}

	private IDEStandardSWTBeanTypeProxyFactory(IDEProxyFactoryRegistry aRegistry) {
		fFactoryRegistry = aRegistry;
		fFactoryRegistry.registerBeanTypeProxyFactory(BEAN_TYPE_FACTORY_KEY, this);
		rectangleBeanType = new IDESWTRectangleBeanTypeProxy(aRegistry);
		pointBeanType = new IDESWTPointBeanTypeProxy(aRegistry);
	}

	public IDEBeanTypeProxy getExtensionBeanTypeProxy(String typeName) {
		return null;
	}

	public IDEBeanTypeProxy getExtensionBeanTypeProxy(String className, Integer classID, IBeanTypeProxy superType) {

		if ("org.eclipse.swt.graphics.Rectangle".equals(className)) //$NON-NLS-1$
			return rectangleBeanType;
		else if ("org.eclipse.swt.graphics.Point".equals(className)) //$NON-NLS-1$
			return pointBeanType;
		else
			return null;
	}

	public void terminateFactory(boolean wait) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jem.internal.proxy.remote.IREMBeanTypeProxyFactory#getExtensionBeanTypeProxy(java.lang.String,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public IProxyBeanType getExtensionBeanTypeProxy(String typeName, IExpression expression) {
		return getExtensionBeanTypeProxy(typeName);
	}

	public IDEBeanTypeProxy getExtensionBeanTypeProxy(String typeName, IBeanTypeProxy superType) {
		return getExtensionBeanTypeProxy(typeName);
	}
}