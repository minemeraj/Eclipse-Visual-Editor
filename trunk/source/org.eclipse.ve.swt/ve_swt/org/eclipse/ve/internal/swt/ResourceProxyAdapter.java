/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ResourceProxyAdapter.java,v $ $Revision: 1.9 $ $Date: 2005-02-15 23:51:49 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * Proxy adapter for resources (not Controls) that need to be allocated on the display thread, and disposed when released. For example, Font, Cursor,
 * Color, or Image.
 * 
 * @since 1.0.0
 */
public class ResourceProxyAdapter extends BeanProxyAdapter {

	public ResourceProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * The initString is evaluated using a static method on the Environment target VM class that ensures it is evaluated on the Display thread
	 */
	protected IBeanProxy basicInitializationStringAllocation(final String aString, final IBeanTypeProxy targetClass)
			throws IAllocationProcesser.AllocationException {
		try {
			Object result = JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(),
					new DisplayManager.DisplayRunnable() {

						public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
							try {
								return ResourceProxyAdapter.super.basicInitializationStringAllocation(aString, targetClass);
							} catch (AllocationException e) {
								throw new RunnableException(e);
							}
						}
					});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new IAllocationProcesser.AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (IAllocationProcesser.AllocationException) e.getCause();
		}
	}

	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws IAllocationProcesser.AllocationException {
		try {
			Object result = JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(),
					new DisplayManager.DisplayRunnable() {

						public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
							try {
								return ResourceProxyAdapter.super.beanProxyAllocation(allocation);
							} catch (AllocationException e) {
								throw new RunnableException(e);
							}
						}
					});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new IAllocationProcesser.AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (IAllocationProcesser.AllocationException) e.getCause();
		}
	}

	public void releaseBeanProxy() {
		if(this.fOwnsProxy) {
			if (isBeanProxyInstantiated()) {
				JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(),
					new DisplayManager.DisplayRunnable() {
						public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
							IBeanProxy resourceBeanProxy = getBeanProxy();
							IMethodProxy disposeWidgetMethodProxy = resourceBeanProxy.getTypeProxy().getMethodProxy("dispose");
							disposeWidgetMethodProxy.invoke(resourceBeanProxy);
							return null;
						}
				});
			}
		}
		super.releaseBeanProxy();
	}
}
