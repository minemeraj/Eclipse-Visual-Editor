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
 *  $RCSfile: UIThreadOnlyProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
 
/**
 * This is a proxy adapter that sets it up so that instantiation and all property
 * settings will be done on the UI thread.
 * 
 * @since 1.1.0
 */
public abstract class UIThreadOnlyProxyAdapter extends BeanProxyAdapter2 {

	public UIThreadOnlyProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
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
		if (!onUIThread())
			return (IProxy) invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
				protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
					return UIThreadOnlyProxyAdapter.super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
				}
			});
		else
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
	}

	/**
	 * Answer whether executing within the UI thread (of the remote vm) or not.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean onUIThread() {
		ProxyFactoryRegistry proxyFactoryRegistry = getBeanProxyDomain().getProxyFactoryRegistry();
		return DisplayManager.getCurrentDisplay(proxyFactoryRegistry) == JavaStandardSWTBeanConstants.getConstants(proxyFactoryRegistry)
				.getDisplayProxy();
	}

	protected final void instantiateAndInitialize(IExpression expression) throws IllegalStateException, AllocationException {
		// Do the whole instantiation on the UI thread -
		if (onUIThread())
			primInstantiateAndInitialize(expression);
		else {
			try {
				invokeSyncExec(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
					protected Object doRun(IBeanProxy displayProxy) throws RunnableException {
						try {
							primInstantiateAndInitialize(expression);
						} catch (IllegalStateException e) {
							throw new RunnableException(e);
						} catch (AllocationException e) {
							throw new RunnableException(e);
						}
						return null;
					}
				});
			}catch (RunnableException e) {
				if (e.getCause() instanceof IllegalStateException)
					throw (IllegalStateException) e.getCause();
				else
					throw (AllocationException) e.getCause();
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
		}
	}

	/**
	 * This is instantiateAndInitialize. It will make sure that all steps of initialization will be on the UI thread. Call super.primInstantiateAndInitialize
	 * if override wants to the standard. It is guarenteed that this will be on the UI thread.
	 * @param expression
	 * @throws IllegalStateException
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	protected void primInstantiateAndInitialize(IExpression expression) throws IllegalStateException, AllocationException {
		super.instantiateAndInitialize(expression);
	}

	protected final void applied(final EStructuralFeature feature, final Object value, final int index, final boolean isTouch, IExpression expression, final boolean testValidity) {
		// Do the whole setting on the UI thread -
		// Though applying the value takes place on the UI thread (WidgetProxyAdapter.applyBeanFeature()),
		// the evaluation of the arguments do not (which includes instantiation). Because of this if some argument is dependent on the UI
		// thread, it fails (ex: button1.setText(>>button2.getText()<<);) Hence wrapper the whole setting of the
		// feature to be on the UI thread.
		//
		if (onUIThread())
			primApplied(feature, value, index, isTouch, expression, testValidity);
		else {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
				protected Object doRun(IBeanProxy displayProxy) {
					primApplied(feature, value, index, isTouch, expression, testValidity);
					return null;
				}
			});
		}
	}

	/**
	 * This is applied, but guarenteed to be on the UI thread. Override this if want to something different. Call super.primApplied if want to do the
	 * standard.
	 * 
	 * @param feature
	 * @param value
	 * @param index
	 * @param isTouch
	 * @param expression
	 * @param testValidity
	 * 
	 * @since 1.1.0
	 */
	protected void primApplied(final EStructuralFeature feature, final Object value, final int index, final boolean isTouch, IExpression expression, final boolean testValidity) {
		super.applied(feature, value, index, isTouch, expression, testValidity);
	}

	protected final void appliedList(final EStructuralFeature feature, final List values, final int index, final boolean isTouch, final IExpression expression, final boolean testValidity) {
		// Do the whole setting on the UI thread -
		// Though applying the value takes place on the UI thread (WidgetProxyAdapter.applyBeanFeature()),
		// the evaluation of the arguments do not (which includes instantiation). Because of this if some argument is dependent on the UI
		// thread, it fails (ex: button1.setText(>>button2.getText()<<);) Hence wrapper the whole setting of the
		// feature to be on the UI thread.
		//
		if (onUIThread())
			primAppliedList(feature, values, index, isTouch, expression, testValidity);
		else {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
				protected Object doRun(IBeanProxy displayProxy) {
					primAppliedList(feature, values, index, isTouch, expression, testValidity);
					return null;
				}
			});
		}
	}

	/**
	 * This is appliedList, but guarenteed to be on the UI thread. Override this if want to something different. Call super.primAppliedList if want to do the
	 * standard.
	 * 
	 * @param feature
	 * @param values
	 * @param index
	 * @param isTouch
	 * @param expression
	 * @param testValidity
	 * 
	 * @since 1.1.0
	 */
	protected void primAppliedList(EStructuralFeature feature, List values, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		super.appliedList(feature, values, index, isTouch, expression, testValidity);
	}

	protected final void canceled(final EStructuralFeature feature, final Object value, final int index, IExpression expression) {
		// Do the whole canceling on the UI thread -
		// Though applying the cancel value takes place on the UI thread (WidgetProxyAdapter.applyBeanFeature()),
		// the evaluation of the arguments do not (which includes instantiation). Because of this if some argument is dependent on the UI
		// thread, it fails (ex: button1.setText(>>button2.getText()<<);) Hence wrapper the whole canceling of the
		// feature to be on the UI thread.
		//
		if (onUIThread())
			primCanceled(feature, value, index, expression);
		else {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
				protected Object doRun(IBeanProxy displayProxy) {
					primCanceled(feature, value, index, expression);
					return null;
				}
			});
		}
	
	}

	/**
	 * This is canceled, but guarenteed to be on the UI thread. Override this if want to something different. Call super.primCanceled if want to do
	 * the standard.
	 * 
	 * @param feature
	 * @param value
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void primCanceled(final EStructuralFeature feature, final Object value, final int index, IExpression expression) {
		super.canceled(feature, value, index, expression);
	}

	protected final IProxy applyBeanProperty(final PropertyDecorator propertyDecorator, final IProxy settingProxy, IExpression expression, final boolean getOriginalValue) throws NoSuchMethodException, NoSuchFieldException {
		// Normally we will already be on the UI thread because the applied/canceled method will make sure, but there are a few times
		// where applyBeanProperty is called outside of applied/canceled.
		if (onUIThread())
			return primApplyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
		else {
			try {
				return (IProxy) invokeSyncExec(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
					protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
						try {
							return primApplyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
						} catch (NoSuchMethodException e) {
							throw new RunnableException(e);
						} catch (NoSuchFieldException e) {
							throw new RunnableException(e);
						}
					}
				});
			} catch (RunnableException e) {
				if (e.getCause() instanceof NoSuchMethodException)
					throw (NoSuchMethodException) e.getCause();
				else
					throw (NoSuchFieldException) e.getCause();
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
			return null;
		}
	}

	/**
	 * This is applyBeanProperty, but guarenteed to be on the UI thread. Override this if want to something different. Call
	 * super.primApplyBeanProperty if want to do the standard. This is special because applyBeanProperty can be called outside
	 * of normal apply/cancel settings for a direct apply bypassing the EMF.
	 * 
	 * @param propertyDecorator
	 * @param settingProxy
	 * @param expression
	 * @param getOriginalValue
	 * @return
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * 
	 * @since 1.1.0
	 */
	protected IProxy primApplyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue) throws NoSuchMethodException, NoSuchFieldException {
		return super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}

	protected final void moved(final EStructuralFeature feature, final Object value, final int oldPosition, final int newPosition, final IExpression expression) {
		// Do the whole move on the UI thread
		if (onUIThread())
			primMoved(feature, value, oldPosition, newPosition, expression);
		else {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
				protected Object doRun(IBeanProxy displayProxy) {
					primMoved(feature, value, oldPosition, newPosition, expression);
					return null;
				}
			});
		}
	}

	/**
	 * This is moved, but guarenteed to be on the UI thread. Override this if want to something different. Call
	 * super.primMoved if want to do the standard.
	 * @param feature
	 * @param value
	 * @param oldPosition
	 * @param newPosition
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void primMoved(EStructuralFeature feature, Object value, int oldPosition, int newPosition, IExpression expression) {
		super.moved(feature, value, oldPosition, newPosition, expression);
	}

	protected final void primReleaseBeanProxy(IExpression expression) {
		if (isBeanProxyInstantiated() && isOwnsProxy() && !onUIThread()) {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {
	
				protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
					primPrimReleaseBeanProxy(expression);
					UIThreadOnlyProxyAdapter.super.primReleaseBeanProxy(expression);
					return null;
				}
			});
		} else {
			primPrimReleaseBeanProxy(expression);
			super.primReleaseBeanProxy(expression);
		}
	}

	/**
	 * Process release. It is not guarenteed to be on UI thread. If the bean proxy is owned and is instantiated it will be on UI thread. Otherwise it
	 * will not. Overrides should go to the UI thread if they need to.
	 * 
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected abstract void primPrimReleaseBeanProxy(IExpression expression);

}
