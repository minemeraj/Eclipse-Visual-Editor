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
 * $RCSfile: ResourceProxyAdapter.java,v $ $Revision: 1.12 $ $Date: 2005-05-18 16:48:00 $
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.PTExpression;
import org.eclipse.jem.internal.instantiation.PTMethodInvocation;
import org.eclipse.jem.internal.instantiation.PTName;
import org.eclipse.jem.internal.instantiation.ParseTreeAllocation;
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
public class ResourceProxyAdapter extends BeanProxyAdapter {

	public ResourceProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * The initString is evaluated using a static method on the Environment target VM class that ensures it is evaluated on the Display thread
	 */
	protected IBeanProxy basicInitializationStringAllocation(final String aString, final IBeanTypeProxy targetClass)
			throws IAllocationProcesser.AllocationException {
		try {
			Object result = JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(),
					new DisplayManager.DisplayRunnable() {

						public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
							try {
								return ResourceProxyAdapter.super.basicInitializationStringAllocation(aString, targetClass);
							} catch (AllocationException e) {
								throw new RunnableException(e);
							}
						}
					});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new IAllocationProcesser.AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (IAllocationProcesser.AllocationException) e.getCause();
		}
	}

	protected IBeanProxy beanProxyAllocation(final JavaAllocation allocation) throws IAllocationProcesser.AllocationException {
		try {
			Object result = JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(),
					new DisplayManager.DisplayRunnable() {

						public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
							try {
								return ResourceProxyAdapter.super.beanProxyAllocation(allocation);
							} catch (AllocationException e) {
								throw new RunnableException(e);
							}
						}
					});
			return (IBeanProxy) result;
		} catch (ThrowableProxy e) {
			throw new IAllocationProcesser.AllocationException(e);
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			throw (IAllocationProcesser.AllocationException) e.getCause();
		} finally {
			if(isSharedInstance(allocation)){
				fOwnsProxy = false;
			}
		}
	}
	
	public boolean isSharedInstance(JavaAllocation allocation){
		// JFace colors and fonts can come from registries, in which case we can't release them as they are shared object
		// JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
		// however some fonts can be released, e.g.
		// new Font(top.getDisplay(), data);
		// We don't have a clean way to distinguish which is which now so this has to look at the parse tree and be harded coded for
		// JFace registries.  Bugzilla 91358 tracks the fact we should re-visit this solution		
		if(allocation instanceof ParseTreeAllocation){
			PTExpression expression = ((ParseTreeAllocation)allocation).getExpression();
			if(expression instanceof PTMethodInvocation){
				PTExpression getRegistryEntry = ((PTMethodInvocation)expression).getReceiver(); // JFaceResources.getFontRegistry().getBold(...);
				if (getRegistryEntry instanceof PTMethodInvocation) {
					PTExpression registryMethod = ((PTMethodInvocation)getRegistryEntry).getReceiver(); // JFaceResources.getFontRegistry()
					if(registryMethod instanceof PTName  &&((PTName)registryMethod).getName().equals("org.eclipse.jface.resource.JFaceResources")){ //$NON-NLS-1$
							return true;
					}
				}
			}
		}
		return false;
	}

	public void releaseBeanProxy() {
		if(this.fOwnsProxy && isBeanProxyInstantiated()) {
			JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(),
				new DisplayManager.DisplayRunnable() {
					public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
						IBeanProxy resourceBeanProxy = getBeanProxy();
						IMethodProxy disposeWidgetMethodProxy = resourceBeanProxy.getTypeProxy().getMethodProxy("dispose"); //$NON-NLS-1$
						disposeWidgetMethodProxy.invoke(resourceBeanProxy);
						return null;
				}
			});
		}
		super.releaseBeanProxy();
	}
}
