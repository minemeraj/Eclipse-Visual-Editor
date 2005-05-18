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
/*
 * $RCSfile: WidgetProxyAdapter2.java,v $ $Revision: 1.1 $ $Date: 2005-05-18 18:39:15 $
 */
package org.eclipse.ve.internal.swt;

import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;

import org.eclipse.ve.internal.java.core.*;

/**
 * Proxy adapter for SWT Widgets.
 * 
 * @since 1.0.0
 */
public class WidgetProxyAdapter2 extends BeanProxyAdapter2 {

	public WidgetProxyAdapter2(IBeanProxyDomain domain) {
		super(domain);
	}

	/**
	 * Get the bean type proxy for the Environment.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final IBeanTypeProxy getEnvironmentBeanTypeProxy() {
		return JavaStandardSWTBeanConstants.getConstants(getBeanProxyDomain().getProxyFactoryRegistry()).getEnvironmentBeanTypeProxy();
	}

	/**
	 * Invoke sync exec on registry's UI thread.
	 * 
	 * @param runnable
	 * @return
	 * @throws ThrowableProxy
	 * @throws RunnableException
	 * 
	 * @since 1.1.0
	 */
	protected Object invokeSyncExec(DisplayManager.DisplayRunnable runnable) throws ThrowableProxy, RunnableException {
		return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}

	/**
	 * Invoke sync exec on registry's UI thread when an expression is involved.
	 * 
	 * @param runnable
	 * @return
	 * @throws ThrowableProxy
	 * @throws RunnableException
	 * 
	 * @since 1.1.0
	 */
	protected Object invokeSyncExec(DisplayManager.ExpressionDisplayRunnable runnable) throws ThrowableProxy, RunnableException {
		return JavaStandardSWTBeanConstants.invokeSyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}

	/**
	 * Invoke sync exec on registry's UI thread. Catch and log all exceptions.
	 * 
	 * @param runnable
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected Object invokeSyncExecCatchThrowable(DisplayManager.DisplayRunnable runnable) {
		return JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}

	/**
	 * Invoke sync exec on registry's UI thread when an expression is involved. Catch and log all exceptions.
	 * 
	 * @param runnable
	 * @return
	 * @throws ThrowableProxy
	 * @throws RunnableException
	 * 
	 * @since 1.1.0
	 */
	protected Object invokeSyncExecCatchThrowable(DisplayManager.ExpressionDisplayRunnable runnable) {
		return JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(getBeanProxyDomain().getProxyFactoryRegistry(), runnable);
	}

	public IProxy getBeanPropertyProxyValue(final EStructuralFeature aBeanPropertyFeature, IExpression expression, final ForExpression forExpression) {
		return (IProxy) invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

			protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
				return WidgetProxyAdapter2.super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
			}
		});
	}

	protected void applied(final EStructuralFeature feature, final Object value, final int index, final boolean isTouch, IExpression expression,
			final boolean testValidity) {
		// Do the whole setting on the UI thread -
		// Though applying the value takes place on the UI thread (WidgetProxyAdapter.applyBeanFeature()),
		// the evaluation of the arguments do not (which includes instantiation). Because of this if some argument is dependent on the UI
		// thread, it fails (ex: button1.setText(>>button2.getText()<<);) Hence wrapper the whole setting of the
		// feature to be on the UI thread.
		//
		invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

			protected Object doRun(IBeanProxy displayProxy) {
				WidgetProxyAdapter2.super.applied(feature, value, index, isTouch, expression, testValidity);
				return null;
			}
		});
	}

	protected void canceled(final EStructuralFeature feature, final Object value, final int index, IExpression expression) {
		// Do the whole canceling on the UI thread -
		// Though applying the cancel value takes place on the UI thread (WidgetProxyAdapter.applyBeanFeature()),
		// the evaluation of the arguments do not (which includes instantiation). Because of this if some argument is dependent on the UI
		// thread, it fails (ex: button1.setText(>>button2.getText()<<);) Hence wrapper the whole canceling of the
		// feature to be on the UI thread.
		//
		invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

			protected Object doRun(IBeanProxy displayProxy) {
				WidgetProxyAdapter2.super.canceled(feature, value, index, expression);
				return null;
			}
		});
	}

	protected IProxy applyBeanProperty(final PropertyDecorator propertyDecorator, final IProxy settingProxy, IExpression expression,
			final boolean getOriginalValue) throws NoSuchMethodException, NoSuchFieldException {
		// Normally we will already be on the UI thread because the applied/canceled method will make sure, but there are a few times
		// where applyBeanProperty is called outside of applied/canceled. The only overhead in that case is the runnable will be created
		// but not really needed.
		try {
			return (IProxy) invokeSyncExec(new DisplayManager.ExpressionDisplayRunnable(expression) {

				protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					try {
						return WidgetProxyAdapter2.super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
					} catch (NoSuchMethodException e) {
						throw new RunnableException(e);
					} catch (NoSuchFieldException e) {
						throw new RunnableException(e);
					}
				}
			});
		} catch (DisplayManager.DisplayRunnable.RunnableException e) {
			if (e.getCause() instanceof NoSuchMethodException)
				throw (NoSuchMethodException) e.getCause();
			else
				throw (NoSuchFieldException) e.getCause();
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
		return null;
	}

	// Style bit. Calculated once and saved. It is used repeatedly.
	private static final int NO_STYLE = -1;

	private int style = NO_STYLE;

	protected void primReleaseBeanProxy(IExpression expression) {
		style = NO_STYLE; // Uncache the style bit
		if (isOwnsProxy() && isBeanProxyInstantiated()) {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

				protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					expression.createSimpleMethodInvoke(widgetBeanProxy.getTypeProxy().getMethodProxy(expression, "dispose"), widgetBeanProxy, null,
							false);
					return null;
				}
			});
		}
		super.releaseBeanProxy(expression);
	}

	/**
	 * @return the int style value by interrogate getStyle() on the targetVM on the correct thread
	 * 
	 * @since 1.0.0
	 */
	public int getStyle() {
		if (style == NO_STYLE && isBeanProxyInstantiated()) {
			invokeSyncExecCatchThrowable(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy getStyleMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("getStyle"); //$NON-NLS-1$
					IIntegerBeanProxy styleBeanProxy = (IIntegerBeanProxy) getStyleMethodProxy.invoke(widgetBeanProxy);
					style = styleBeanProxy.intValue();
					return null;
				}
			});
		}
		return style;
	}

}
