/*****************************************************************************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the terms
 * of the Common Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 * $RCSfile: ToolItemProxyAdapter.java,v $ $Revision: 1.4 $ $Date: 2004-11-09 17:48:35 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class ToolItemProxyAdapter extends WidgetProxyAdapter {
	private EReference sf_items;
	protected BeanProxyAdapter toolBarProxyAdapter;

	private IMethodProxy boundsMethodProxy;

	public ToolItemProxyAdapter(IBeanProxyDomain domain) {
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
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected BeanProxyAdapter getToolBarProxyAdapter() {
		if (toolBarProxyAdapter == null) {
			EObject parent = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sf_items);
			IBeanProxyHost toolBarProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) parent);
			toolBarProxyAdapter = (BeanProxyAdapter) toolBarProxyHost;
		}
		return toolBarProxyAdapter;
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
		if (getToolBarProxyAdapter() != null)
			getToolBarProxyAdapter().revalidateBeanProxy();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (!isBeanProxyInstantiated())
			return; // Nothing to cancel to yet or could not construct.
		super.canceled(sf, oldValue, position);
		if (getToolBarProxyAdapter() != null)
			getToolBarProxyAdapter().revalidateBeanProxy();
	}

	public IRectangleBeanProxy getBounds() {
		if (isBeanProxyInstantiated()) {
	 		return (IRectangleBeanProxy) invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
	
				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					// Call the layout() method
					IBeanProxy rectProxy = getBoundsMethodProxy().invoke(getBeanProxy());
					return rectProxy;
				}
			});
		} else
			return null;
	}

	protected IMethodProxy getBoundsMethodProxy() {
		if (boundsMethodProxy == null) {
			boundsMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("getBounds"); //$NON-NLS-1$
		}
		return boundsMethodProxy;
	}

	/*
	 * Use to call BeanProxyAdapter's beanProxyAllocation.
	 */
	protected IBeanProxy beanProxyAdapterBeanProxyAllocation(JavaAllocation allocation) throws AllocationException {
		return super.beanProxyAllocation(allocation);
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_TOOLBAR_ITEMS);
		}
	}

}