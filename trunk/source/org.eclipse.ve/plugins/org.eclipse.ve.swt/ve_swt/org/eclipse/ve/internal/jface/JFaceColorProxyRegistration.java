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
 *  $RCSfile: JFaceColorProxyRegistration.java,v $
 *  $Revision: 1.3 $  $Date: 2005-04-13 21:14:10 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.RGB;

import org.eclipse.jem.internal.proxy.core.*;

/**
 * Initialize the JFace ColorRegistry with the JFacePreferences colors from the IDE. 
 * The ColorRegistry is normally primed from the WorkBench themes but because the WorkBench 
 * isn't loaded in the remote VM, it never gets set and when you set the color of a SWT control
 * using the JFace preferences colors, it doesn't show correctly in the remote VM. 
 * For RCP applications, the workbench will be loaded in the running application so the
 * ColorRegistry should be primed as well and show up correctly.
 * 
 * @since 1.1.0
 */
public class JFaceColorProxyRegistration {

	public static void initialize(ProxyFactoryRegistry registry) {
		// To handle the case in which this is being called but there is no SWT in the remote vm,
		// check to see if the SWT RGB class even exists. If not, just return.
		if (registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.graphics.RGB") == null) //$NON-NLS-1$
			return;
		
		RGB errorColor_RGB = JFaceResources.getColorRegistry().getRGB(JFacePreferences.ERROR_COLOR);
		RGB hyperLinkColor_RGB = JFaceResources.getColorRegistry().getRGB(JFacePreferences.HYPERLINK_COLOR);
		RGB activeHyperLinkColor_RGB = JFaceResources.getColorRegistry().getRGB(JFacePreferences.ACTIVE_HYPERLINK_COLOR);

		// JFaceColorRegistryInitializer - remote vm helper used to prime the JFace ColorRegistry.
		IBeanTypeProxy jfaceColorInitBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jface.targetvm.JFaceColorRegistryInitializer"); //$NON-NLS-1$
		
		IBeanTypeProxy hashMapBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.util.HashMap"); //$NON-NLS-1$
		IBeanTypeProxy objectBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.lang.Object"); //$NON-NLS-1$
		IMethodProxy initMethodProxy = jfaceColorInitBeanTypeProxy.getMethodProxy("init", new IBeanTypeProxy[] { registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.util.Map")}); //$NON-NLS-1$ //$NON-NLS-2$
		IMethodProxy putMethodProxy = hashMapBeanTypeProxy.getMethodProxy("put", new IBeanTypeProxy[] { objectBeanTypeProxy, objectBeanTypeProxy}); //$NON-NLS-1$
		IBeanProxy hashMapBeanProxy;
		try {
			// Create a HashMap
			hashMapBeanProxy = hashMapBeanTypeProxy.newInstance();
		} catch (ThrowableProxy e) {
			return; 
		}
		// Now populate the map with the JFace color preferences found in JFacePreferences
		IStringBeanProxy symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFacePreferences.ERROR_COLOR);
		IBeanProxy rgbBeanProxy = getRGBBeanProxy(registry, errorColor_RGB);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, rgbBeanProxy});

		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFacePreferences.HYPERLINK_COLOR);
		rgbBeanProxy = getRGBBeanProxy(registry, hyperLinkColor_RGB);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, rgbBeanProxy});

		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFacePreferences.ACTIVE_HYPERLINK_COLOR);
		rgbBeanProxy = getRGBBeanProxy(registry, activeHyperLinkColor_RGB);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, rgbBeanProxy});
		
		// Finally... call the helper method in the remote vm to iterate throught the map and prime the ColorRegistry
		initMethodProxy.invokeCatchThrowableExceptions(jfaceColorInitBeanTypeProxy, hashMapBeanProxy);
	}

	protected static IBeanProxy getRGBBeanProxy(ProxyFactoryRegistry registry, RGB rgb) {
		IBeanTypeProxy rgbBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.graphics.RGB"); //$NON-NLS-1$
		IBeanProxy rgbBeanProxy = null;
		StringBuffer rgbInitString = new StringBuffer("new org.eclipse.swt.graphics.RGB("); //$NON-NLS-1$
		rgbInitString.append(rgb.red);
		rgbInitString.append(','); //$NON-NLS-1$
		rgbInitString.append(rgb.green);
		rgbInitString.append(','); //$NON-NLS-1$
		rgbInitString.append(rgb.blue);
		rgbInitString.append(')'); //$NON-NLS-1$
		try {
			rgbBeanProxy = rgbBeanTypeProxy.newInstance(rgbInitString.toString());
		} catch (ThrowableProxy e) {
			return null;
		} catch (InstantiationException e) {
			return null; // shouldn't occur
		}
		return rgbBeanProxy;
	}

}
