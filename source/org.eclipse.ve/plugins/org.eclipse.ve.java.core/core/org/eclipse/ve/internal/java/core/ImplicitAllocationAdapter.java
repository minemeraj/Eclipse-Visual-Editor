/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImplicitAllocationAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-01-12 21:44:11 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
 
/**
 * This adapter processes the <code>ImplicitAllocation</code> allocation.
 * 
 * <p>This class is a singleton. It doesn't store the target and can be on more than one target.
 * 
 * @see org.eclipse.jem.internal.instantiation.ImplicitAllocation 
 * @since 1.0.0
 */
public class ImplicitAllocationAdapter implements IAllocationAdapter {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationAdapter#allocate(org.eclipse.jem.internal.instantiation.JavaAllocation, org.eclipse.ve.internal.java.core.IBeanProxyDomain)
	 */
	public IBeanProxy allocate(JavaAllocation allocation, IBeanProxyDomain domain) throws AllocationException {
		ImplicitAllocation impalloc = (ImplicitAllocation) allocation;
		EObject source = impalloc.getParent();
		IBeanProxyHost proxyhost = (IBeanProxyHost) EcoreUtil.getExistingAdapter(source, IBeanProxyHost.BEAN_PROXY_TYPE);
		return proxyhost.getBeanPropertyProxyValue(impalloc.getFeature());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;	// Since we can be on more than one.
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return type == IAllocationAdapter.class;
	}

}
