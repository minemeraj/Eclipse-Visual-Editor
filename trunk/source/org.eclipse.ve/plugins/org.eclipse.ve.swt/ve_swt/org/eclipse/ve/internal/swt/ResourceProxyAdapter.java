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
 * $RCSfile: ResourceProxyAdapter.java,v $ $Revision: 1.13 $ $Date: 2005-05-18 18:39:15 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.*;
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
public class ResourceProxyAdapter extends BeanProxyAdapter2 {

	public ResourceProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		JavaAllocation allocation = getJavaObject().isSetAllocation() ? getJavaObject().getAllocation() : null;
		try {
			return (IProxy) JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(),
					new DisplayManager.ExpressionDisplayRunnable(expression) {

						protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
							try {
								return ResourceProxyAdapter.super.primInstantiateBeanProxy(expression);
							} catch (AllocationException e) {
								throw new RunnableException(e);	// Wrapper it so we know an AllocationException was thrown.
							}
						}
					});
		} catch (ThrowableProxy e) {
			throw new IAllocationProcesser.AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (IAllocationProcesser.AllocationException) e.getCause();	// Now throw the allocation exception.
		} finally {
			if(isSharedInstance(allocation)){
				setOwnsProxy(false);	// It is a shared instance, so we don't own the proxy.
			}
		}
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

	protected void primReleaseBeanProxy(IExpression expression) {
		if(isOwnsProxy() && isBeanProxyInstantiated()) {
			JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(),
				new DisplayManager.ExpressionDisplayRunnable(expression) {
					protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
						IBeanProxy resourceBeanProxy = getBeanProxy();
						expression.createSimpleMethodInvoke(resourceBeanProxy.getTypeProxy().getMethodProxy(expression, "dispose"), resourceBeanProxy,
								null, false);
						return null;
					}
				}
			);
		}
		super.primReleaseBeanProxy(expression);
	}
}
