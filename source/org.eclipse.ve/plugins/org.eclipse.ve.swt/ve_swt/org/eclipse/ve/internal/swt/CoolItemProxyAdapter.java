package org.eclipse.ve.internal.swt;

/*****************************************************************************************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: CoolItemProxyAdapter.java,v $ $Revision: 1.1 $ $Date: 2004-08-20 22:39:14 $
 */

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class CoolItemProxyAdapter extends WidgetProxyAdapter {

	public CoolItemProxyAdapter(IBeanProxyDomain aDomain) {
		super(aDomain);
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
						return beanProxyAdapterBeanProxyAllocation(allocation);
					} catch (AllocationException e) {
						throw new RunnableException(e);
					}
				}
			});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (AllocationException) e.getCause(); // We know it is an allocation exception because that is the only runnable exception we throw.
		}
	}

	/*
	 * Use to call BeanProxyAdapter's beanProxyAllocation.
	 */
	protected IBeanProxy beanProxyAdapterBeanProxyAllocation(JavaAllocation allocation) throws AllocationException {
		return super.beanProxyAllocation(allocation);
	}

}
