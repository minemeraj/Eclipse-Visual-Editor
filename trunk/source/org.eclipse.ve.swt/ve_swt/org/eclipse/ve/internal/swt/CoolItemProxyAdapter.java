/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

/*
 * $RCSfile: CoolItemProxyAdapter.java,v $ $Revision: 1.3 $ $Date: 2004-08-27 15:35:50 $
 */

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

public class CoolItemProxyAdapter extends WidgetProxyAdapter {

	private EStructuralFeature sf_text;

	private EStructuralFeature sf_size;

	private EStructuralFeature sf_preferredSize;

	private EReference sf_items;

	protected BeanProxyAdapter coolBarProxyAdapter;

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
	 * Return the proxy adapter associated with this TabFolder.
	 */
	protected BeanProxyAdapter getCoolBarProxyAdapter() {
		if (coolBarProxyAdapter == null) {
			EObject parent = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sf_items);
			IBeanProxyHost coolBarProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) parent);
			coolBarProxyAdapter = (BeanProxyAdapter) coolBarProxyHost;
		}
		return coolBarProxyAdapter;
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
		if ((sf == sf_text || sf == sf_size || sf == sf_preferredSize) && getCoolBarProxyAdapter() != null)
			getCoolBarProxyAdapter().revalidateBeanProxy();
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
			sf_text = ((IJavaObjectInstance) newTarget).eClass().getEStructuralFeature("text"); //$NON-NLS-1$
			sf_size = ((IJavaObjectInstance) newTarget).eClass().getEStructuralFeature("size"); //$NON-NLS-1$
			sf_preferredSize = ((IJavaObjectInstance) newTarget).eClass().getEStructuralFeature("preferredSize"); //$NON-NLS-1$
			sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_COOLBAR_ITEMS);
		}
	}

}
