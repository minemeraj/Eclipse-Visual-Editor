/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ShellProxyAdapter.java,v $ $Revision: 1.7 $ $Date: 2004-07-28 15:04:15 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * Proxy adapter for SWT Shell
 * 
 * @since 1.0.0
 */
public class ShellProxyAdapter extends CompositeProxyAdapter {

	public ShellProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#beanProxyAllocation(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws AllocationException {
		try {
			Object result = invokeSyncExec(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					try {
						IBeanProxy shellProxy = beanProxyAdapterBeanProxyAllocation(allocation);
						// Position the shell off screen for the moment
						// TODO this needs to be done properly so that the location can be set in the model and ignored
						// likewise for the visibility
						IIntegerBeanProxy intBeanProxy = displayProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(-5000);
						IMethodProxy setlocationMethodProxy = shellProxy.getTypeProxy().getMethodProxy("setLocation", new String[] { "int", "int"});  //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
						setlocationMethodProxy.invoke(shellProxy, new IBeanProxy[] { intBeanProxy, intBeanProxy});
						
						// Add a ShellListener that prevents the closing or minimization of the shell.
						IBeanTypeProxy listenerType = displayProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.PreventShellCloseMinimizeListener");  //$NON-NLS-1$
						if (listenerType != null) {
							IBeanProxy shellListenerBean = listenerType.newInstance();
							IMethodProxy addShellListenerMethodProxy = shellProxy.getTypeProxy().getMethodProxy("addShellListener", new String[] { "org.eclipse.swt.events.ShellListener" } ); //$NON-NLS-1$  //$NON-NLS-2$
							if (shellListenerBean != null && addShellListenerMethodProxy != null) {
								addShellListenerMethodProxy.invoke(shellProxy, new IBeanProxy[] { shellListenerBean });
							}
						}

						IMethodProxy openMethodProxy = shellProxy.getTypeProxy().getMethodProxy("open"); //$NON-NLS-1$
						openMethodProxy.invoke(shellProxy);
						return shellProxy;
					} catch (AllocationException e) {
						throw new RunnableException(e);
					}
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause(); // Because we only put allocation exceptions into the cause.
		}
	}

}