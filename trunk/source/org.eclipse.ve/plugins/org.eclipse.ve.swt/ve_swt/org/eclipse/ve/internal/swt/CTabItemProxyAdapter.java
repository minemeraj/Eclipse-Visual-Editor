/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabItemProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-05 19:42:25 $ 
 */
package org.eclipse.ve.internal.swt;

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


public class CTabItemProxyAdapter extends WidgetProxyAdapter {

	private EStructuralFeature sf_text,sf_image,sf_font;

	private EReference sf_items;

	protected CTabFolderProxyAdapter cTabFolderProxyAdapter;

	public CTabItemProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		if (!isBeanProxyInstantiated()  && !isInstantiationFeature(sf))
			return; // Nothing to apply to yet or could not construct.
		super.applied(sf, newValue, position); // We letting the settings go through
		if (((sf == sf_image)||(sf == sf_text)||(sf == sf_font)) && getCTabFolderProxyAdapter() != null)
			getCTabFolderProxyAdapter().validateBeanProxy();
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
	protected CTabFolderProxyAdapter getCTabFolderProxyAdapter() {
		if (cTabFolderProxyAdapter == null) {
			EObject parent = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sf_items);
			IBeanProxyHost cTabFolderProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) parent);
			cTabFolderProxyAdapter = (CTabFolderProxyAdapter) cTabFolderProxyHost;
		}
		return cTabFolderProxyAdapter;
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sf_text = ((IJavaObjectInstance) newTarget).eClass().getEStructuralFeature("text"); //$NON-NLS-1$
			sf_image = ((IJavaObjectInstance) newTarget).eClass().getEStructuralFeature("image"); //$NON-NLS-1$
			sf_font = ((IJavaObjectInstance) newTarget).eClass().getEStructuralFeature("font"); //$NON-NLS-1$
			sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_CTABFOLDER_ITEMS);
		}
	}

	/*
	 * Use to call BeanProxyAdapter's beanProxyAllocation.
	 */
	protected IBeanProxy beanProxyAdapterBeanProxyAllocation(JavaAllocation allocation) throws AllocationException {
		return super.beanProxyAllocation(allocation);
	}
}
