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

import org.eclipse.jem.internal.proxy.remote.REMProxyFactoryRegistry;
import org.eclipse.jem.internal.proxy.swt.*;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * A standard SWT Bean proxy factory extension. Used for SWT bean proxies.
 * 
 * @since 1.1.0
 */
public class REMStandardSWTBeanProxyFactory implements IStandardSWTBeanProxyFactory {

	final IStandardBeanTypeProxyFactory fBeanTypeFactory;

	public static void registerBeanTypeProxyFactory(REMProxyFactoryRegistry aRegistry) {
		if (aRegistry.getBeanTypeProxyFactoryExtension(IStandardSWTBeanProxyFactory.REGISTRY_KEY) == null)
			new REMStandardSWTBeanProxyFactory(aRegistry);
	}

	private REMStandardSWTBeanProxyFactory(REMProxyFactoryRegistry factory) {
		factory.registerBeanProxyFactory(IStandardSWTBeanProxyFactory.REGISTRY_KEY, this);
		fBeanTypeFactory = factory.getBeanTypeProxyFactory();
	}

	public IPointBeanProxy createPointBeanProxyWith(int x, int y) {
		try {
			return (IPointBeanProxy) fBeanTypeFactory
					.getBeanTypeProxy("org.eclipse.swt.graphics.Point").newInstance("new org.eclipse.swt.graphics.Point(" + x + "," + y + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} catch (ThrowableProxy e) {
			return null;
		} catch (InstantiationException e) {
			return null; // Shouldn't occur
		}
	}

	public IRectangleBeanProxy createBeanProxyWith(int x, int y, int width, int height) {
		try {
			return (IRectangleBeanProxy) fBeanTypeFactory
					.getBeanTypeProxy("org.eclipse.swt.graphics.Rectangle").newInstance("new org.eclipse.swt.graphics.Rectangle(" + x + "," + y + "," + width + "," + height + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		} catch (ThrowableProxy e) {
			return null;
		} catch (InstantiationException e) {
			return null; // Shouldn't occur
		}
	}

	public void terminateFactory(boolean wait) {
	}

}
