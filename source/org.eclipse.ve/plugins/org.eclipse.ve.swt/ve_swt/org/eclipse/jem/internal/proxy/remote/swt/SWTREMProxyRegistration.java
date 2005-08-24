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
package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.*;

/**
 * The SWT Remote Proxy registration extension. Contributed through the plugin.xml.
 * 
 * @since 1.1.0
 */
public class SWTREMProxyRegistration implements IExtensionRegistration {

	public void register(BaseProxyFactoryRegistry baseRegistry) {
		REMProxyFactoryRegistry remRegistry = (REMProxyFactoryRegistry) baseRegistry;
		REMStandardSWTBeanTypeProxyFactory.registerBeanTypeProxyFactory(remRegistry);
		REMStandardSWTBeanProxyFactory.registerBeanTypeProxyFactory(remRegistry);
	}

}
