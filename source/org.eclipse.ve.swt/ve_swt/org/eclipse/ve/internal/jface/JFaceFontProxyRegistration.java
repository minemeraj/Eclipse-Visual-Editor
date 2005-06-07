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
 *  $RCSfile: JFaceFontProxyRegistration.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-07 20:12:15 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;

import org.eclipse.jem.internal.proxy.core.*;
 
/**
 * Initialize the JFace FontRegistry with the JFacePreferences fonts from the IDE. 
 * 
 * @since 1.1.0
 */
public class JFaceFontProxyRegistration {
	public static void initialize(ProxyFactoryRegistry registry) {
		// To handle the case in which this is being called but there is no SWT in the remote vm,
		// check to see if the SWT Font class even exists. If not, just return.
		if (registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.graphics.Font") == null) //$NON-NLS-1$
			return;
		
		// JFaceFontRegistryInitializer - remote vm helper used to update the JFace FontRegistry.
		IBeanTypeProxy jfaceFontInitBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jface.targetvm.JFaceFontRegistryInitializer"); //$NON-NLS-1$
		
		IBeanTypeProxy hashMapBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.util.HashMap"); //$NON-NLS-1$
		IBeanTypeProxy objectBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.lang.Object"); //$NON-NLS-1$
		IMethodProxy initMethodProxy = jfaceFontInitBeanTypeProxy.getMethodProxy("init", new IBeanTypeProxy[] { registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.util.Map")}); //$NON-NLS-1$ //$NON-NLS-2$
		IMethodProxy putMethodProxy = hashMapBeanTypeProxy.getMethodProxy("put", new IBeanTypeProxy[] { objectBeanTypeProxy, objectBeanTypeProxy}); //$NON-NLS-1$
		IBeanProxy hashMapBeanProxy;
		try {
			// Create a HashMap
			hashMapBeanProxy = hashMapBeanTypeProxy.newInstance();
		} catch (ThrowableProxy e) {
			return; 
		}
		Font jfaceFont = null;
		IStringBeanProxy symbolicNameBeanProxy;
		IBeanProxy fontDataBeanProxy;
		// Now populate the map with the JFace font preferences found in JFaceResources
		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFaceResources.BANNER_FONT);
		jfaceFont = JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT);
		fontDataBeanProxy = getFontDataBeanProxy(registry, jfaceFont);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, fontDataBeanProxy});

		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFaceResources.DEFAULT_FONT);
		jfaceFont = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
		fontDataBeanProxy = getFontDataBeanProxy(registry, jfaceFont);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, fontDataBeanProxy});
		
		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFaceResources.DIALOG_FONT);
		jfaceFont = JFaceResources.getFontRegistry().get(JFaceResources.DIALOG_FONT);
		fontDataBeanProxy = getFontDataBeanProxy(registry, jfaceFont);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, fontDataBeanProxy});

		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFaceResources.HEADER_FONT);
		jfaceFont = JFaceResources.getFontRegistry().get(JFaceResources.HEADER_FONT);
		fontDataBeanProxy = getFontDataBeanProxy(registry, jfaceFont);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, fontDataBeanProxy});
		
		symbolicNameBeanProxy = registry.getBeanProxyFactory().createBeanProxyWith(JFaceResources.TEXT_FONT);
		jfaceFont = JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT);
		fontDataBeanProxy = getFontDataBeanProxy(registry, jfaceFont);
		putMethodProxy.invokeCatchThrowableExceptions(hashMapBeanProxy, new IBeanProxy[] { symbolicNameBeanProxy, fontDataBeanProxy});

		// Finally... call the helper method in the remote vm to iterate throught the map and prime the FontRegistry
		initMethodProxy.invokeCatchThrowableExceptions(jfaceFontInitBeanTypeProxy, hashMapBeanProxy);
	}

	protected static IBeanProxy getFontDataBeanProxy(ProxyFactoryRegistry registry, Font font) {
		IBeanTypeProxy fontDataBeanTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.graphics.FontData"); //$NON-NLS-1$
		IBeanProxy fontDataBeanProxy = null;
		StringBuffer fontDataInitString = new StringBuffer("new org.eclipse.swt.graphics.FontData(\""); //$NON-NLS-1$
		fontDataInitString.append(font.getFontData()[0].toString());
		fontDataInitString.append('\"'); //$NON-NLS-1$
		fontDataInitString.append(')'); //$NON-NLS-1$
		try {
			fontDataBeanProxy = fontDataBeanTypeProxy.newInstance(fontDataInitString.toString());
		} catch (ThrowableProxy e) {
			return null;
		} catch (InstantiationException e) {
			return null; // shouldn't occur
		}
		return fontDataBeanProxy;
	}
}
