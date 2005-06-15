/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: ToolItemProxyAdapter.java,v $ $Revision: 1.10 $ $Date: 2005-06-15 20:19:21 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * swt ToolItem proxy adapter.
 * 
 * @since 1.1.0
 */
public class ToolItemProxyAdapter extends ItemProxyAdapter {

	public ToolItemProxyAdapter(IBeanProxyDomain domain) {
		super(domain, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(domain.getEditDomain()), SWTConstants.SF_TOOLBAR_ITEMS));
	}

	public IRectangleBeanProxy getBounds() {
		if (isBeanProxyInstantiated()) {
			return (IRectangleBeanProxy) invokeSyncExecCatchThrowable(new DisplayManager.DisplayRunnable() {

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
		return getBeanProxy().getTypeProxy().getMethodProxy("getBounds"); //$NON-NLS-1$
	}

}