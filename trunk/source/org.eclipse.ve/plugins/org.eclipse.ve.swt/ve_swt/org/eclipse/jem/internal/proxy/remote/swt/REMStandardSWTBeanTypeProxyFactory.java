/*****************************************************************************************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.*;

/**
 * The standard REM SWT BeanType proxy factory extension. Used for SWT bean type proxies.
 * 
 * @since 1.1.0
 */
public class REMStandardSWTBeanTypeProxyFactory implements IREMBeanTypeProxyFactory {

	static final String BEAN_TYPE_FACTORY_KEY = "org.eclipse.swt.graphics"; //$NON-NLS-1$

	protected final REMProxyFactoryRegistry fFactoryRegistry;

	public static void registerBeanTypeProxyFactory(REMProxyFactoryRegistry aRegistry) {
		if (aRegistry.getBeanTypeProxyFactoryExtension(BEAN_TYPE_FACTORY_KEY) == null)
			new REMStandardSWTBeanTypeProxyFactory(aRegistry);
	}
	
	private REMStandardSWTBeanTypeProxyFactory(REMProxyFactoryRegistry aRegistry) {
		fFactoryRegistry = aRegistry;
		fFactoryRegistry.registerBeanTypeProxyFactory(BEAN_TYPE_FACTORY_KEY, this);
	}

	public IREMBeanTypeProxy getExtensionBeanTypeProxy(String typeName) {
		return null;
	}

	public IREMBeanTypeProxy getExtensionBeanTypeProxy(String className, Integer classID, IBeanTypeProxy superType) {

		if ("org.eclipse.swt.graphics.Rectangle".equals(className)) //$NON-NLS-1$
			return new REMSWTRectangleBeanTypeProxy(fFactoryRegistry, classID, className, superType);
		else if ("org.eclipse.swt.graphics.Point".equals(className)) //$NON-NLS-1$
			return new REMSWTPointBeanTypeProxy(fFactoryRegistry, classID, className, superType);
		else
			return null;
	}

	public void terminateFactory(boolean wait) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jem.internal.proxy.remote.IREMBeanTypeProxyFactory#getExtensionBeanTypeProxy(java.lang.String, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public IProxyBeanType getExtensionBeanTypeProxy(String typeName, IExpression expression) {
		return getExtensionBeanTypeProxy(typeName);
	}
}