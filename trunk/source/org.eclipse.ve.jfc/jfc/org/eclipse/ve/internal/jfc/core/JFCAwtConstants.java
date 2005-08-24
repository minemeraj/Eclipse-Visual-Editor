/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JFCAwtConstants.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:10 $ 
 */


import org.eclipse.jem.internal.proxy.core.IMethodProxy;
import org.eclipse.jem.internal.proxy.core.ProxyFactoryRegistry;

/**
 * Standard AWT Constants for the JCF.
 *
 * Use the static accessor method (getConstants()) to get the appropriate 
 * constants instance for the registry. This is done so that until the
 * the constants are needed, they aren't registered.
 *
 * NOTE: Since everything in here just proxies, there is
 *       no need to have them separated by VM. 
 *
 * This is final because this can't be extended. This specific one
 * will be registered with the factory for this key. Any extensions
 * must be done in their own constants and registry key.
 *
 * Creation date: (4/7/00 4:47:48 PM)
 * @author: Administrator
 */
public final class JFCAwtConstants {
		
	public static final Object REGISTRY_KEY = new Object();
	
		
	private ProxyFactoryRegistry fRegistry;
				
	private IMethodProxy 
		fDataCollectorAbort,
		fDataCollectorBusy,
		fDataCollectorWait,
		fDataCollectorStartImage,
		fDataCollectorStartComponent,
		fDataCollectorStartProducer;
	
	/**
	 * Get the constants instance for the specified registry.
	 */
	public static JFCAwtConstants getConstants(ProxyFactoryRegistry registry) {
		JFCAwtConstants constants = (JFCAwtConstants) registry.getConstants(REGISTRY_KEY);
		if (constants == null)
			registry.registerConstants(REGISTRY_KEY, constants = new JFCAwtConstants(registry));
		return constants;
	}
			
	
	protected JFCAwtConstants(ProxyFactoryRegistry registry) {
		fRegistry = registry;
	}
	
	public IMethodProxy getDataCollectorAbort() {
		if (fDataCollectorAbort == null) {
			fDataCollectorAbort = fRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").getMethodProxy("abort"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return fDataCollectorAbort;
	}
	
	public IMethodProxy getDataCollectorStartImage() {
		if (fDataCollectorStartImage == null) {
			fDataCollectorStartImage = fRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").getMethodProxy("start", new String[] {"java.awt.Image"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return fDataCollectorStartImage;
	}

	public IMethodProxy getDataCollectorStartComponent() {
		if (fDataCollectorStartComponent == null) {
			fDataCollectorStartComponent = fRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").getMethodProxy("start", new String[] {"java.awt.Component", "int", "int"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		return fDataCollectorStartComponent;
	}
	
	public IMethodProxy getDataCollectorStartProducer() {
		if (fDataCollectorStartProducer == null) {
			fDataCollectorStartProducer = fRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").getMethodProxy("start", new String[] {"java.awt.image.ImageProducer"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return fDataCollectorStartProducer;
	}
	
	public IMethodProxy getDataCollectorWait() {
		if (fDataCollectorWait == null) {
			fDataCollectorWait = fRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").getMethodProxy("waitForCompletion"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return fDataCollectorWait;
	}
	
	public IMethodProxy getDataCollectorBusy() {
		if (fDataCollectorBusy == null) {
			fDataCollectorBusy = fRegistry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ImageDataCollector").getMethodProxy("isCollecting"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return fDataCollectorBusy;
	}		
	
}
