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
/*
 * $RCSfile: WidgetProxyAdapter.java,v $ $Revision: 1.14 $ $Date: 2004-08-27 15:35:50 $
 */
package org.eclipse.ve.internal.swt;

import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * Proxy adapter for SWT Widgets.
 * 
 * @since 1.0.0
 */
public class WidgetProxyAdapter extends BeanProxyAdapter {

	public WidgetProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected IBeanProxy primReadBeanFeature(final PropertyDecorator propDecor, final IBeanProxy aSource) throws ThrowableProxy {
		return (IBeanProxy) invokeSyncExecCatchRunnable(new DisplayManager.DisplayRunnable() {

			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				return WidgetProxyAdapter.super.primReadBeanFeature(propDecor, aSource);
			}
		});
	}

	protected final IBeanTypeProxy getEnvironmentBeanTypeProxy() {
		return JavaStandardSWTBeanConstants.getConstants(getBeanProxyDomain().getProxyFactoryRegistry()).getEnvironmentBeanTypeProxy();
	}

	protected Object invokeSyncExec(DisplayManager.DisplayRunnable runnable) throws ThrowableProxy, RunnableException {
		return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}

	/**
	 * Invoke sync exec on the runnable, but catch any runnable exceptions that occurred in the runnable and just log them. This should be used for
	 * simple ones where you know you don't throw any RunnableExceptions.
	 * 
	 * @param runnable
	 * @return @throws
	 *         ThrowableProxy
	 * 
	 * @since 1.0.0
	 */
	protected Object invokeSyncExecCatchRunnable(DisplayManager.DisplayRunnable runnable) throws ThrowableProxy {
		try {
			return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
		} catch (RunnableException e) {
			SwtPlugin.getDefault().getLogger().log(e.getCause(), Level.WARNING); // Only log the runnable exceptions.
		}
		return null;
	}

	/**
	 * Invoke the runnable on the runnable and catch all RunnableExceptions and ThrowableProxy exceptions and just log them.
	 * 
	 * @param runnable
	 * @return @since 1.0.0
	 */
	protected Object invokeSyncExecCatchThrowableExceptions(DisplayManager.DisplayRunnable runnable) {
		return JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}

	protected void primApplyBeanFeature(final EStructuralFeature sf, final PropertyDecorator propDecor, final BeanFeatureDecorator featureDecor,
			final IBeanProxy settingBeanProxy) throws ThrowableProxy {
		invokeSyncExecCatchRunnable(new DisplayManager.DisplayRunnable() {

			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				WidgetProxyAdapter.super.primApplyBeanFeature(sf, propDecor, featureDecor, settingBeanProxy);
				return null;
			}
		});
	}

	public void releaseBeanProxy() {
		style = -1; // Uncache the style bit
		if (fOwnsProxy && isBeanProxyInstantiated()) {
			invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy disposeWidgetMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("dispose");
					disposeWidgetMethodProxy.invoke(widgetBeanProxy);
					return null;
				}
			});
		}
		super.releaseBeanProxy();
	}

	private int style = -1;

	/**
	 * @return the int style value by interrogate getStyle() on the targetVM on the correct thread
	 * 
	 * @since 1.0.0
	 */
	public int getStyle() {
		if (isBeanProxyInstantiated() && style == -1) {
			invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy getStyleMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("getStyle");
					IIntegerBeanProxy styleBeanProxy = (IIntegerBeanProxy) getStyleMethodProxy.invoke(widgetBeanProxy);
					style = styleBeanProxy.intValue();
					return null;
				}
			});
		}
		return style;
	}

}
