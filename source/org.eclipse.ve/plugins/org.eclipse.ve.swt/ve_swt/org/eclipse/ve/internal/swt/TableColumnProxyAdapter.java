/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

/*
 * $RCSfile: TableColumnProxyAdapter.java,v $ $Revision: 1.7 $ $Date: 2005-02-15 23:51:47 $
 */

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class TableColumnProxyAdapter extends WidgetProxyAdapter {
	private IMethodProxy widthMethodProxy;
	private EReference sf_columns;
	protected BeanProxyAdapter tableProxyAdapter;

	public TableColumnProxyAdapter(IBeanProxyDomain aDomain) {
		super(aDomain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (!isBeanProxyInstantiated())
			return; // Nothing to cancel to yet or could not construct.
		super.canceled(sf, oldValue, position);
		if (getTableProxyAdapter() != null)
			getTableProxyAdapter().revalidateBeanProxy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		if (!isBeanProxyInstantiated())
			return; // Nothing to apply to yet or could not construct.
		super.applied(sf, newValue, position); // We letting the settings go through
		if (getTableProxyAdapter() != null)
			getTableProxyAdapter().revalidateBeanProxy();
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

	protected IMethodProxy getWidthMethodProxy() {
		if (widthMethodProxy == null) {
			widthMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("getWidth"); //$NON-NLS-1$
		}
		return widthMethodProxy;
	}

	public IIntegerBeanProxy getWidth() {
		if (isBeanProxyInstantiated()) {
			return (IIntegerBeanProxy) invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy intProxy = getWidthMethodProxy().invoke(getBeanProxy());
					return intProxy;
				}
			});
		} else
			return null;
	}

	/*
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected BeanProxyAdapter getTableProxyAdapter() {
		if (tableProxyAdapter == null) {
			EObject parent = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sf_columns);
			IBeanProxyHost tableProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) parent);
			tableProxyAdapter = (BeanProxyAdapter) tableProxyHost;
		}
		return tableProxyAdapter;
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sf_columns = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_TABLE_COLUMNS);
		}
	}
}