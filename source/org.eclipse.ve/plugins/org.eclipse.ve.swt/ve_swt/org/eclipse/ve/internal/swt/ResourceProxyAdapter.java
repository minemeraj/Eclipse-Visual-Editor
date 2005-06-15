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
 * $RCSfile: ResourceProxyAdapter.java,v $ $Revision: 1.15 $ $Date: 2005-06-15 20:19:21 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * Proxy adapter for resources (not Controls) that need to be allocated on the display thread, and disposed when released. For example, Font, Cursor,
 * Color, or Image.
 * 
 * @since 1.0.0
 */
public class ResourceProxyAdapter extends UIThreadOnlyProxyAdapter {

	public ResourceProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected void setupBeanProxy(IBeanProxy beanProxy) {
		super.setupBeanProxy(beanProxy);
		if (isSharedInstance(getJavaObject().getAllocation()))
			setOwnsProxy(false);
	}
	
	protected boolean isSharedInstance(JavaAllocation allocation){
		// JFace colors and fonts can come from registries, in which case we can't release them as they are shared object
		// JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
		// however some fonts can be released, e.g.
		// new Font(top.getDisplay(), data);
		// We don't have a clean way to distinguish which is which now so this has to look at the parse tree and be harded coded for
		// JFace registries.  Bugzilla 91358 tracks the fact we should re-visit this solution		
		if(allocation != null){
			try {
				PTExpression expression = ((ParseTreeAllocation) allocation).getExpression();
				if (expression instanceof PTMethodInvocation) {
					PTExpression getRegistryEntry = ((PTMethodInvocation) expression).getReceiver(); // JFaceResources.getFontRegistry().getBold(...);
					if (getRegistryEntry instanceof PTMethodInvocation) {
						PTExpression registryMethod = ((PTMethodInvocation) getRegistryEntry).getReceiver(); // JFaceResources.getFontRegistry()
						if (registryMethod instanceof PTName
								&& ((PTName) registryMethod).getName().equals("org.eclipse.jface.resource.JFaceResources")) {	//$NON-NLS-1$
							return true;
						}
					}
				}
			} catch (ClassCastException e) {
				// It wasn't a ParseTree. This is rare.
			}
		}
		return false;
	}

	protected void primPrimReleaseBeanProxy(IExpression expression) {
		if(isOwnsProxy() && isBeanProxyInstantiated()) {
			IBeanProxy resourceBeanProxy = getBeanProxy();
			expression.createSimpleMethodInvoke(resourceBeanProxy.getTypeProxy().getMethodProxy(expression, "dispose"), resourceBeanProxy, //$NON-NLS-1$
				null, false);
		}
	}
}
