/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.ide.swt;

import org.eclipse.jem.internal.proxy.core.BaseProxyFactoryRegistry;
import org.eclipse.jem.internal.proxy.core.IExtensionRegistration;
import org.eclipse.jem.internal.proxy.ide.IDEProxyFactoryRegistry;

/**
 * The SWT Remote Proxy registration extension. Contributed through the plugin.xml.
 * 
 * @since 1.1.0
 */
public class SWTIDEProxyRegistration implements IExtensionRegistration {

	public void register(BaseProxyFactoryRegistry baseRegistry) {
		IDEProxyFactoryRegistry remRegistry = (IDEProxyFactoryRegistry) baseRegistry;
		// Note: important that IDEStandardSWTBeanTypeProxyFactory is registered BEFORE IDEStandardSWTBeanProxyFactory.
		IDEStandardSWTBeanTypeProxyFactory.registerBeanTypeProxyFactory(remRegistry);
		IDEStandardSWTBeanProxyFactory.registerBeanTypeProxyFactory(remRegistry);
	}

}
