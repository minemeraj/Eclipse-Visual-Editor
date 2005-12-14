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
 *  $RCSfile: BeanProxyAdapter.java,v $
 *  $Revision: 1.62 $  $Date: 2005-12-14 21:32:35 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

 

/**
 * Adapter for instantiating and maintaining the proxies for a Java Instance in the IDE Java model.
 * 
 * @since 1.1.0
 */
public class BeanProxyAdapter extends ErrorNotifier.ErrorNotifierAdapter implements IInternalBeanProxyHost {

	private static final MessageError NO_BEAN_DUE_TO_PREVIOUS_ERROR = new IErrorHolder.MessageError(JavaMessages.BeanProxyAdapter2_NO_BEAN_DUE_TO_PREVIOUS_ERROR_, 
										IErrorHolder.ERROR_INFO);
	
	static boolean LOG_NOTIFICATIONS;
	
	// The beanproxy being wrappered. It should be accessed only through accessors, even subclasses. During
	// instantiation it will be a ExpressionProxy.
	private IProxy beanProxy;
	private boolean inInstantiation;	// Used while in instantiation.
	
	// Does this adapter own the proxy? If it owns it, then it will release it. If it doesn't own it
	// then someone else does, and they will take care of it.
	private boolean ownsProxy;
	
	// Need a table of original setting proxies so that if a setting is canceled it can be restored to the
	// original proxy. It will be created the first time we set anything.
	// This is keyed by feature to beanproxy.
	private Map origSettingProxies;

	// Non-instantiated classes. Used to test if a feature can be set or not. This is used in conjunction with isThis.
	// It is the list of JavaClasses, starting with this class up to but not including the first non-abstract class in
	// the hierarchy. This is because we can't instantiate abstract classes.
	// TODO See if we get rid of this. We need to be able to handle abstract. This is a major kludge.
	protected List notInstantiatedClasses = null;	

	// Bean proxy domain for this adapter.
	private IBeanProxyDomain domain;
	

	// Map of feature to override proxies. These are overrides that will be applied just after instantiation but
	// before all of the settings are applied. Subclasses should use overSetting method in their primInstantiate
	// to add an override. overSetting can be called safely before instantiation too. The proxies can be either
	// ExpresionProxy or IBeanProxy. If ExpressionProxy, they should of been created in the primInstantiate so
	// that they will be available to the apply.
	private Map overrideSettings;
	
	/**
	 * Reinstantiate notification implementation used by bean proxy adapter. This will return true for isTouch() since
	 * it should be treated as a touch. It will be sent pre-reinstantiate so that any listeners can do some clean up while it
	 * is still around. It will also be sent post-reinstantiation so that they can get the new proxy.
	 * <p>
	 * It will also be sent for pre-release of the bean, even if no reinstantiate will follow.
	 * 
	 * @since 1.1.0
	 */
	public class ReinstantiateBeanProxyNotification extends NotificationImpl implements IInternalBeanProxyHost.NotificationLifeCycle {
		
		private IExpression expression;
		private EStructuralFeature feature;
		private InternalEObject notifier;
		private int featureID = NO_FEATURE_ID;
		private boolean prerelease;
		
		/**
		 * @param expression expression for listeners to use to do operations. If it <code>null</code> then this means the registry is invalid. Do not try to use it.
		 * @param prereinstantiation <code>true</code> if this is the pre notice, or else it is the post notice.
		 * 
		 * @since 1.1.0
		 */
		public ReinstantiateBeanProxyNotification(IExpression expression) {
			super(IInternalBeanProxyHost.NOTIFICATION_LIFECYCLE, BeanProxyAdapter.this.getTarget(), BeanProxyAdapter.this.getTarget(), Notification.NO_INDEX, true);
			this.expression = expression;
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2.NotificationReinstantiation#isPrenotice()
		 */
		public boolean isPrerelease() {
			return prerelease;
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2.NotificationLifeCycle#isPostReinstantiation()
		 */
		public boolean isPostReinstantiation() {
			return !prerelease;
		}
		
		/*
		 * Set the pre-relse flag.
		 * @param prerelease
		 * 
		 * @since 1.1.0
		 */
		void setPrerelease(boolean prerelease) {
			this.prerelease = prerelease;
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Notification#getFeatureID(java.lang.Class)
		 */
		public int getFeatureID(Class expectedClass) {
			if (featureID == NO_FEATURE_ID && feature != null) {
				featureID = notifier.eDerivedStructuralFeatureID(feature.getFeatureID(), feature.getContainerClass());
			}
			return notifier.eBaseStructuralFeatureID(featureID, expectedClass);
		}		
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Notification#isTouch()
		 */
		public boolean isTouch() {
			return true;
		}
		
		/*
		 * Set's the index.
		 * 
		 * @param position
		 * 
		 * @since 1.1.0
		 */
		void setIndex(int position) {
			this.position = position;
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2.NotificationReinstantiation#getExpression()
		 */
		public IExpression getExpression() {
			return expression;
		}

		/*
		 * @param feature The feature to set.
		 * 
		 * @since 1.1.0
		 */
		void setFeature(EStructuralFeature feature) {
			this.feature = feature;
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Notification#getFeature()
		 */
		public Object getFeature() {
			return feature;
		}

		/*
		 * @param notifier The notifier to set.
		 * 
		 * @since 1.1.0
		 */
		void setNotifier(InternalEObject notifier) {
			this.notifier = notifier;
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Notification#getNotifier()
		 */
		public Object getNotifier() {
			return notifier;
		}
	}

	/**
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public BeanProxyAdapter(IBeanProxyDomain domain) {
		this.domain = domain;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#getInstantiationError()
	 */
	public List getInstantiationError() {
		Object errors = getErrorsOfKey(INSTANTIATION_ERROR_KEY);
		if (errors == null)
			return Collections.EMPTY_LIST;
		else if (errors instanceof List)
			return (List) errors;
		else
			return Collections.singletonList(errors);
	}
	
	/**
	 * Helper method to get a bean type proxy by name. This is used a lot, so this helps to make it simpler.
	 * This should not be used if an IExpression in involved. Instead use the getBeanTypeProxy that takes an IExpression 
	 * as an argument.
	 * @param classname
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final IBeanTypeProxy getBeanTypeProxy(String classname) {
		return getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(classname);
	}
	
	/**
	 * Helper method to get a IProxyBeanType by name for use in expressions. This is used a lot, so this helps
	 * to make it simpler.
	 * @param classname
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final IProxyBeanType getBeanTypeProxy(String classname, IExpression expression) {
		return getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, classname);
	}

	/**
	 * Helper method to get the bean factory. This is used a lot, so this helps to make it simpler.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final IStandardBeanProxyFactory getBeanProxyFactory() {
		return getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#inInstantiation()
	 */
	public final boolean inInstantiation() {
		return inInstantiation || (beanProxy != null && beanProxy.isExpressionProxy());
	}
	
	/**
	 * Call to process the error as an instantiation error.
	 * @param error
	 * 
	 * @since 1.1.0
	 */
	protected void processInstantiationError(ErrorType error) {
		// Bit of a kludge, but we can get no bean due to previous error before the
		// actual instantiation error. So if the error is not this error (which is a static constant)
		// then we want to remove the no bean error since we now have a true instantiation error.
		Object errs = getErrorsOfKey(INSTANTIATION_ERROR_KEY);
		if (errs == NO_BEAN_DUE_TO_PREVIOUS_ERROR) {
			if (error != NO_BEAN_DUE_TO_PREVIOUS_ERROR)
				clearError(INSTANTIATION_ERROR_KEY);
			else
				return;	// Don't add it twice.
		} else if (errs != null && error == NO_BEAN_DUE_TO_PREVIOUS_ERROR)
			return;	// Don't add it in, we have other errors.
		processError(error, INSTANTIATION_ERROR_KEY);
	}
	
	public boolean hasInstantiationErrors() {
		return hasErrorsOfKey(INSTANTIATION_ERROR_KEY);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2#instantiateBeanProxy(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public final IProxy instantiateBeanProxy(IExpression expression) {

		if (!inInstantiation() && !isBeanProxyInstantiated() && expression.isValid()){
			if (LOG_NOTIFICATIONS) {
				StringBuffer r = new StringBuffer();
				r.append("Instantiation request: "); //$NON-NLS-1$
				printObject(getJavaObject(), r);
				JavaVEPlugin.log(r.toString(), Level.WARNING);
			}			
			// First create the bean
			boolean reinstantiate = isThis != null;	// If we had a isThis setting then this is not the first instantiation. We will notify in that case.
			isThis = null;
			isThisPart();	// Reset it.
			clearError(INSTANTIATION_ERROR_KEY);	// Make sure there are no instantiation errors.
			inInstantiation = true;
			int mark = expression.mark();
			try {
				instantiateAndInitialize(expression);
				if (reinstantiate) {
					// Need to notify.
					ReinstantiateBeanProxyNotification notification = new ReinstantiateBeanProxyNotification(expression);
					notification.setPrerelease(false);
					// Now walk through all references and tell of instantiation.
					InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(getTarget(),
							InverseMaintenanceAdapter.ADAPTER_KEY);
					if (ai != null)
						fireReinstantiateNotice(notification, ai);
				}
			} catch (AllocationException e) {
				// This means there was a bad error occured in instantiation and is not recoverable.
				// Log this as the instantiate error, but we make it just an info, allocation exceptions
				// are usually due to errors other than actually trying to instantiate it, such as too complicated.
				processInstantiationError(new ExceptionError(e.getCause(), ERROR_INFO));
				beanProxy = null;
			} catch (IllegalStateException e) {
				// This means there was a expression creation error occured in instantiation and so is not recoverable.
				// Log this as the instantiate error.
				processInstantiationError(new ExceptionError(e, ERROR_SEVERE));
				beanProxy = null;				
			} catch (RuntimeException e) {
				// This means there was a some other kind of error occured in instantiation and so is not recoverable.
				// Log this as the instantiate error.
				processInstantiationError(new ExceptionError(e, ERROR_SEVERE));
				beanProxy = null;							
			} finally {
				inInstantiation = false;				
				expression.endMark(mark);
			}
		}
		return beanProxy;
	}
	
	/**
	 * This is called  by instantiate to do the actual instantiation and to initialize the result with the settings. It normally should not be
	 * overridden. However, if it is, care should be taken that the same functions are performed, or super should be called.
	 * <p>
	 * <b>Note:</b>
	 * If overriding and calling super.instantiateAndInitialize() and calling your own expression methods, you will need to put
	 * your methods within expression.try/catch blocks to handle any exceptions in your calls. You will need to send them
	 * to processInstantiationError. This is because the caller of this method does not put an expression.try/catch around the
	 * call.
	 * 
	 * @param expression
	 * @throws IllegalStateException
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	protected void instantiateAndInitialize(IExpression expression) throws IllegalStateException, AllocationException {
		// Treat this in the expression as:
		// 
		//   try {
		//     instantiate
		
		expression.createTry();
		try {
			beanProxy = primInstantiateBeanProxy(expression);
			if (beanProxy != null && beanProxy.isExpressionProxy()) {
				((ExpressionProxy) beanProxy).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						setupBeanProxy(event.getProxy()); // We have it! So do setup.
					}

					/* (non-Javadoc)
					 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyAdapter#proxyNotResolved(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
					 */
					public void proxyNotResolved(ProxyEvent event) {
						if (!hasErrorsOfKey(INSTANTIATION_ERROR_KEY)) {
							if (JavaVEPlugin.isLoggingLevel(Level.INFO))
								JavaVEPlugin.log("Bean Proxy for " + getTarget() + " not resolved.", Level.INFO); //$NON-NLS-1$ //$NON-NLS-2$
							processInstantiationError(NO_BEAN_DUE_TO_PREVIOUS_ERROR);
						}
						beanProxy = null; // Reset back to not set.
					}
				});
			} else
				setupBeanProxy((IBeanProxy) beanProxy); // TODO This could happen due to this being an implicit allocation from a non-proxyhost2 source. Can collapse back when these are merged.
			//   } catch (BeanInstantiationException e) {
			//     ... ignore. This was sent because during instantiation (typically through implicit allocation) a needed bean
			//         during allocation could not be instantiated. The error is already logged with that bean.
			//   }
			expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
			
			//   } catch (Exception e) {
			//     ... send back thru ExpressionProxy to mark an instantiation error ...
			//     throw new BeanInstantiationError(); ... so that when being applied as a setting it can be seen as not valid, but rest of expression can continue.
			//   }				
			ExpressionProxy expProxy = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
			expProxy.addProxyListener(new ExpressionProxy.ProxyAdapter() {

				public void proxyResolved(ProxyEvent event) {
					ThrowableProxy throwableProxy = (ThrowableProxy) event.getProxy();
					processInstantiationError(new BeanExceptionError(throwableProxy, ERROR_SEVERE));
				}
			});
			expression.createThrow();
			expression.createClassInstanceCreation(ForExpression.THROW_OPERAND, getBeanInstantiationExceptionTypeProxy(expression), 0);
			//   } catch (LinkageError e) {
			//     ... send back thru ExpressionProxy to mark an instantiation error ...
			//     throw new BeanInstantiationError(); ... so that when being applied as a setting it can be seen as not valid, but rest of expression can continue.
			//   }				
			ExpressionProxy linkProxy = expression.createTryCatchClause(getBeanTypeProxy("java.lang.LinkageError", expression), true); //$NON-NLS-1$
			linkProxy.addProxyListener(new ExpressionProxy.ProxyAdapter() {

				public void proxyResolved(ProxyEvent event) {
					ThrowableProxy throwableProxy = (ThrowableProxy) event.getProxy();
					processInstantiationError(new BeanExceptionError(throwableProxy, ERROR_SEVERE));
				}
			});
			expression.createThrow();
			expression.createClassInstanceCreation(ForExpression.THROW_OPERAND, getBeanInstantiationExceptionTypeProxy(expression), 0);	
			//   } catch (UnresolvedCompilationError e) {
			//     ... send back thru ExpressionProxy to mark an instantiation error ...
			//     throw new BeanInstantiationError(); ... so that when being applied as a setting it can be seen as not valid, but rest of expression can continue.
			//   }				
			ExpressionProxy compErrorProxy = expression.createTryCatchClause(getBeanTypeProxy("org.eclipse.jem.internal.proxy.common.UnresolvedCompilationError", expression), true); //$NON-NLS-1$
			compErrorProxy.addProxyListener(new ExpressionProxy.ProxyAdapter() {

				public void proxyResolved(ProxyEvent event) {
					ThrowableProxy throwableProxy = (ThrowableProxy) event.getProxy();
					processInstantiationError(new BeanExceptionError(throwableProxy, ERROR_SEVERE));
				}
			});
			expression.createThrow();
			expression.createClassInstanceCreation(ForExpression.THROW_OPERAND, getBeanInstantiationExceptionTypeProxy(expression), 0);			
		} finally {
			if (expression.isValid())
				expression.createTryEnd();
		}
		
		//   apply all settings.
		applyAllSettings(expression);
	}


	/*
	 * Apply all settings. Should only be called by instantiation, so we need to test validity of the feature before applying.
	 */
	protected final void applyAllSettings(final IExpression expression) {
		// Now apply the overrides, if any.
		if (overrideSettings != null && !overrideSettings.isEmpty()) {
			Iterator overItr = overrideSettings.entrySet().iterator();
			while(expression.isValid() && overItr.hasNext()) {
				Map.Entry entry = (Entry) overItr.next();
				if (entry.getValue() != Boolean.FALSE)
					if (!applyOverrideSetting(expression, (EStructuralFeature) entry.getKey(), (IProxy) entry.getValue()))
						entry.setValue(Boolean.FALSE);	// It failed, mark it so not to try again.
			}
		}
		
		// Now apply all of the feature settings.
		IJavaObjectInstance eTarget = (IJavaObjectInstance) getTarget();

		eTarget.visitSetFeatures(new FeatureValueProvider.Visitor() {

			public Object isSet(EStructuralFeature feature, Object value) {
				if (!feature.getName().equals(JavaInstantiation.ALLOCATION)) {				
					if (expression.isValid()) {
						if (feature.isMany())
							appliedList(feature, (List) value, 0, false, expression, true);
						else {
							int mark = expression.mark();
							try {
								applied(feature, value, Notification.NO_INDEX, false, expression, true);
							} finally {
								expression.endMark(mark);
							}
						}
						return null;
					} else
						return Boolean.FALSE; // Expression is now invalid, so don't continue processing. (return false forces visit stop, but we don't care about the value).
				} else
					return null;
			}
		});
	}
	
	/* 
	 * Apply the override setting to the expression.
	 * @param expression
	 * @param entry
	 * @throws IllegalStateException
	 * @returns <code>true</code> if could be applied, or <code>false</code> if it was invalid.
	 * 
	 * @since 1.1.0
	 */
	private boolean applyOverrideSetting(final IExpression expression, EStructuralFeature feature, IProxy value) throws IllegalStateException {
		if (expression.isValid()) {
			try {
				expression.createTry();
				applyInternalBeanProperty(expression, Utilities.getPropertyDecorator(feature), value);
				expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), false); //$NON-NLS-1$
				revalidateBeanProxy();
				return true;
			} catch (IllegalArgumentException e) {
				JavaVEPlugin.log(e, Level.WARNING);
				return false; // Need to Mark this as bad so that it won't try to reapply it on the reinstantiate.
			} catch (NoSuchMethodException e) {
				JavaVEPlugin.log(e, Level.WARNING);
				return false; // Need to Mark this as bad so that it won't try to reapply it on the reinstantiate.
			} catch (NoSuchFieldException e) {
				JavaVEPlugin.log(e, Level.WARNING);
				return false; // Need to Mark this as bad so that it won't try to reapply it on the reinstantiate.
			} finally {
				if (expression.isValid())
					expression.createTryEnd();
			}
		} else
			return true;
	}


	/**
	 * Process the exception as a BeanPropertyError.
	 * 
	 * @param e
	 * @param feature
	 * @param value
	 * @param index
	 * 
	 * @since 1.1.0
	 */
	protected void processPropertyError(Throwable e, EStructuralFeature feature, Object value) {
		processPropertyError(e, feature, value, this);
	}
	
	/**
	 * Same as {@link BeanProxyAdapter#processPropertyError(Throwable, EStructuralFeature, Object)} except that
	 * a error notifier is provided. This is used for property settings errors on intermediate objects. This is
	 * rarely used. See jfc.ContainerProxyAdapter for how it could be used.
	 *  
	 * @param e
	 * @param feature
	 * @param value
	 * @param errorNotifier
	 * 
	 * @since 1.1.0
	 */
	protected void processPropertyError(Throwable e, EStructuralFeature feature, Object value, ErrorNotifier errorNotifier) {
		errorNotifier.processError(new BeanPropertyError(ERROR_WARNING, e, feature, value), null);
	}
	
	/**
	 * This is called when a notification arrives for a SET and there was an old value (and was not a TOUCH). Subclasses may of added listeners
	 * to the old value that now needs to be removed. This is the notification of the old value. Apply of the new value will be called 
	 * after this. This will not be called for UNSET or REMOVE since there is no new value and canceled will be called instead for these values.
	 * 
	 * @param feature
	 * @param oldValue
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void removeOldValue(final EStructuralFeature feature, final Object oldValue, final int index, IExpression expression) {
	}
	
	
	/**
	 * Cancel the setting.
	 * <p>Subclasses may override to handle the cancel. Normally they do not need to handle thrown exceptions from the expression because
	 * these will be handled by the caller (@link BeanProxyAdapter#canceled(EStructuralFeature, Object, int, IExpression)}. However,
	 * if they are any IDE exceptions (such as NoSuchMethodError when searching for an IProxyMethod) these should be caught and logged.
	 * 
	 * @param feature
	 * @param value
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( feature);
		if ((propertyDecorator != null && propertyDecorator.isWriteable())) {
			if ( origSettingProxies != null && origSettingProxies.containsKey(feature)){
				try {
					applyBeanProperty(propertyDecorator, (IBeanProxy) origSettingProxies.get(feature), expression, false);
				} catch (NoSuchMethodException e) {
					JavaVEPlugin.log(e, Level.INFO);
				} catch (NoSuchFieldException e) {
					JavaVEPlugin.log(e, Level.INFO);
				}
			}
		}
	}
	
	/**
	 * Cancel the feature. Normally this should not be overridden. If it is, care must be taken to either call super or provide equivalent tests.
	 * Most overrides will instead be cancelSetting instead.
	 * <p>
	 * If it is overridden, be aware of the functions of canceled in this class and do what's appropriate. Such as creating an expression 
	 * try/catch around the cancel so that if the cancel fails at evaluation time it doesn't kill anything else on the expression.
	 * <p>
	 * <b>Note:</b> Callers of canceled should wrapper the call with mark/endmark so that the expression will be valid if there was
	 * a problem in canceled. (No need to call mark/endmark if it is createExpression/call canceled/invokeexpression).
	 * @param feature
	 * @param value
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void canceled(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (expression.isValid() && (overrideSettings == null || !overrideSettings.containsKey(feature))) {
			// Only cancel if this if it is not the this part, or if it is the
			// this part, then only if it is a non-local attribute. Don;t cancel if there is a valid override for the feature.
			if (!isThisPart() || !isAttributeLocal(feature)) {
				// Treat as:
				//   try {
				//     cancel setting
				//   } catch (Exception e) {
				//     log it.
				//   }
				expression.createTry();
				try {
					cancelSetting(feature, value, index, expression);
					revalidateBeanProxy();	// TODO get rid of this. Not really appropriate.
					ExpressionProxy cancelException = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
					cancelException.addProxyListener(new ExpressionProxy.ProxyAdapter() {
	
						public void proxyResolved(ProxyEvent event) {
							JavaVEPlugin.log((ThrowableProxy)event.getProxy(), Level.INFO);
						}
					});
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.SEVERE);
				} finally {
					if (expression.isValid())
						expression.createTryEnd();	// The expression didn't go bad in instantiateSettingBean, so we need to end the try.
				}
			}
		}
	}
	
	
	/**
	 * Apply the feature. Normally should not be overridden. If it is, super should be called, or the appropriate tests made instead. Normally
	 * applySetting should be overridden instead.
	 * <p>
	 * If you override, be aware of the functions that this method does here in this class and do the same where appropriate, such
	 * as test for validity, clearing any errors for this feature, and using expression try/catch around your apply so that 
	 * it can be caught and stored as an error for the feature.
	 * <p>
	 * <b>Note:</b> Callers of applied should wrapper the call with mark/endmark so that the expression will be valid if there was
	 * a problem in applied. (No need to call mark/endmark if it is createExpression/call applied/invokeexpression).
	 * @param feature the feature being applied. The default implementation will only apply writable bean properties. Subclasses may do more if they wish.
	 * @param value the value from the IJavaInstance. Depending upon the feature this may be <code>null</code>, <code>IJavaInstance</code>, or anything else. For writable bean properties it should be either null or an IJavaInstance.
	 * @param index	the index of the setting if in a many valued feature. The current default implementation ignores this. Subclasses may use it if they wish.
	 * @param isTouch <code>true</code> if this is a touch. This means that the same object was set back into the property. This can be used by overrides to decide not to apply the live value again. By default it does apply it.
	 * @param expression the expression to build the apply into.
	 * @param testValidity <code>true</code> if true then the validity of the feature needs to be tested. Use {@link BeanProxyAdapter#testApplyValidity(IExpression, boolean, EStructuralFeature, Object, boolean) testValidity} for testing in method overrides that don't delgate to super.
	 * @since 1.1.0 
	 */
	protected void applied(final EStructuralFeature feature, final Object value, final int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (testApplyValidity(expression, testValidity, feature, value, true)) {		
			// Only apply if this if it is not the this part, or if it is the
			// this part, then only if it is a non-local attribute. Don't apply if there is a valid override setting for the feature.
			if (!isThisPart() || !isAttributeLocal(feature)) {
				// Treat as:
				//   try {
				//     apply setting
				//   } catch (BeanInstantiationException e) {
				//     ... ignore this, we've already logged it, we just don't want to try to apply it.
				//   } catch (Exception e) {
				//     ... mark feature as bad with the exception from the apply ...
				//   }
				expression.createTry();
				try {
					applySetting(feature, value, index, expression);
					revalidateBeanProxy();	// TODO get rid of this. Not really appropriate.
					if (!expression.isValid())
						return;	// We've gone bad.
					expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
					ExpressionProxy applyException = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
					applyException.addProxyListener(new ExpressionProxy.ProxyAdapter() {

						public void proxyResolved(ProxyEvent event) {
							processPropertyError((Throwable) event.getProxy(), feature, value);
						}
					});
				} catch (RuntimeException e) {
					// Something bad. So this setting is now invalid.
					processPropertyError(e, feature, value);
				} finally {
					if (expression.isValid())
						expression.createTryEnd();	// The expression didn't go bad in instantiateSettingBean, so we need to end the try.
				}
			} 
		}
		
	}
	
	/**
	 * Test validity to apply. Can be used by subclasses the override applied to do the validity test if they do applies themselves
	 * rather then pass it on up.
	 * @param expression if invalid, will return false.
	 * @param testValidity if true, will test validity, if false, it will not test validity (i.e. was there a previous error on the feature).
	 * @param feature feature to test for
	 * @param value value of the setting
	 * @param honorOverrides <code>true</code> if override should be tested for and return invalid if override exists.
	 * @return true if valid and can be applied, false if not valid and cannot be applied.
	 * 
	 * @since 1.1.0
	 */
	protected boolean testApplyValidity(IExpression expression, boolean testValidity, EStructuralFeature feature, Object value, boolean honorOverrides) {
		return expression.isValid() && (!testValidity || !hasErrorsOfKeyObject(feature, value)) && (!honorOverrides || (overrideSettings == null || !overrideSettings.containsKey(feature)));
	}
	
	/**
	 * Apply the setting.
	 * <p>Subclasses may override to handle the apply. They should call {@link BeanProxyAdapter#instantiateSettingBean(IBeanProxyHost, IExpression, EStructuralFeature, Object)}
	 * when they need to instantiate the setting value. Normally they do not need to handle thrown exceptions from the expression because
	 * these will be handled by the caller (@link BeanProxyAdapter#applied(EStructuralFeature, Object, int, IExpression, boolean)}. However,
	 * if they are any IDE exceptions (such as NoSuchMethodError when searching for an IProxyMethod) these should be caught and logged as:
	 * <pre><code>
	 *   processPropertyError(e, feature, value, index);
	 * </code></pre>
	 * <p>
	 * Often a better place to override is {@link BeanProxyAdapter#applyBeanProperty(PropertyDecorator, IProxy, IExpression, boolean)}. That is
	 * useful to override when it is still just a setting but needs to applied in a different way. This method here should be overridden if the apply
	 * is totally different from a normal apply.
	 * 
	 * @param feature
	 * @param value
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {			
		PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( feature);
		if ((propertyDecorator != null && propertyDecorator.isWriteable())) {
			try {
				IJavaInstance javaValue = (IJavaInstance)value;
				IInternalBeanProxyHost settingBean = getSettingBeanProxyHost(javaValue);						
				IProxy settingBeanProxy = null;
				if (settingBean != null) {
					settingBeanProxy = instantiateSettingBean(settingBean, expression, feature, value);
					if (settingBeanProxy == null)
						return;	// It failed creation, don't go any further. 
				}

				applyInternalBeanProperty(expression, propertyDecorator, settingBeanProxy);
			} catch (NoSuchMethodException e) {
				processPropertyError(e, feature, value);
			} catch (NoSuchFieldException e) {
				processPropertyError(e, feature, value);
			}
		}
	}

	/**
	 * This will apply a bean property proxy and get the original value if not already retrieved. This will only work on standard
	 * proxy setting. It should normally not be overridden by subclasses.
	 * 
	 * @param expression
	 * @param propertyDecorator
	 * @param settingBeanProxy
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * 
	 * @since 1.1.0
	 */
	protected void applyInternalBeanProperty(IExpression expression, final PropertyDecorator propertyDecorator, IProxy settingBeanProxy)
			throws NoSuchMethodException, NoSuchFieldException {
		// See if this is the first time for this feature, if it is then we need to get the original value to allow cancels.
		boolean getOriginalValue = !isSettingInOriginalSettingsTable((EStructuralFeature) propertyDecorator.getEModelElement());
		IProxy origValue = applyBeanProperty(propertyDecorator, settingBeanProxy, expression, getOriginalValue);
		if (getOriginalValue) {
			setOriginalValue((EStructuralFeature) propertyDecorator.getEModelElement(), origValue);
		}
	}


	/**
	 * Set the original value. Can be used to change original value if desired.
	 * @param propertyDecorator
	 * @param origValue
	 * 
	 * @since 1.1.0
	 */
	protected void setOriginalValue(final EStructuralFeature sf, IProxy origValue) {
		if (origValue == null || origValue.isBeanProxy()) {
			// No original value or it is already resolved, just put it in the original table.
			getOriginalSettingsTable().put(sf, origValue);
		} else {
			// It is an expression, so save it when resolved.
			((ExpressionProxy) origValue).addProxyListener(new ExpressionProxy.ProxyAdapter() {

				public void proxyResolved(ProxyEvent event) {
					getOriginalSettingsTable().put(sf, event.getProxy());
				}
			});
		}
	}

	/**
	 * Apply an override property. This will override the actual setting for this feature and will use the given overrideBean.
	 * However, query for default values will return the true original value. If called before instantiation or during
	 * primInstantiate, the expression will be ignored and it will not be applied until all settings are applied during
	 * instantiation. If instantiated, there must be an expression and the value will be applied through the expression.
	 * 
	 * @param feature
	 * @param overrideBean the proxy or <code>null</code> if override is null value.
	 * @param expression <code>null</code> if called before instantiation, or during primInstantiate (it will be ignored it set), else it must
	 * 	be a valid expression so that it can be applied right then.
	 * 
	 * @since 1.1.0
	 */
	protected void overrideProperty(final EStructuralFeature feature, IProxy overrideBean, IExpression expression) {
		if (overrideSettings == null)
			overrideSettings = new LinkedHashMap();
		
		if (overrideSettings.get(feature) == Boolean.FALSE)
			return;	// We can't accept it. It has been shown to be invalid.
		
		overrideSettings.put(feature, overrideBean);
		
		boolean addResolver = true;
		if (isBeanProxyInstantiated()) {
			// Need to use expression to apply the setting now.
			if (expression == null)
				throw new IllegalArgumentException(JavaMessages.BeanProxyAdapter2_OverrideProperty_ExpressionMustNotBeNull_EXC_); 
		
			if (applyOverrideSetting(expression, feature, overrideBean)) { 
				overrideSettings.put(feature, Boolean.FALSE);
				addResolver = false;
			}
		}
		
		if (addResolver && overrideBean != null && overrideBean.isExpressionProxy()) {
			((ExpressionProxy) overrideBean).addProxyListener(new ExpressionProxy.ProxyAdapter() {
				public void proxyResolved(ProxyEvent event) {
					overrideSettings.put(feature, event.getProxy());
				}
			});
		}
		
	}
	
	/**
	 * Remove the override. If the bean is instantiated, it will use the expression to apply
	 * the "original value". If not instantiated, nothing happens immediately, but next 
	 * instantiation will use the set value. It is most efficient to call when not instantiated.
	 * 
	 * @param feature
	 * @param expression ignored if bean is not instantiated, may be <code>null</code> then. If bean is instantiated, the expression must be valid not null.
	 * 
	 * @since 1.1.0
	 */
	protected void removeOverrideProperty(EStructuralFeature feature, IExpression expression) {
		boolean overridden = false;
		if (isOverridePropertySet(feature)) {
			overrideSettings.remove(feature);
			overridden = true;
		}
		
		if (overridden && isBeanProxyInstantiated()) {
			if (expression == null)
				throw new IllegalArgumentException(JavaMessages.BeanProxyAdapter2_RemoveOverrideProperty_ExpressionMustNotBeNull_EXC_); 
			IProxy oldValue = null;
			if (origSettingProxies != null)
				oldValue = (IProxy) origSettingProxies.get(feature);
			applyOverrideSetting(expression, feature, oldValue);
		}
	}
	
	/**
	 * Answer whether the feature has an override applied to it.
	 * @param feature
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isOverridePropertySet(EStructuralFeature feature) {
		return overrideSettings != null && overrideSettings.containsKey(feature);
	}

	/**
	 * Get the IBeanProxyHost (adapt it if not already adapted) for the IJavaInstance. This is 
	 * a helper method.
	 * 
	 * @param javaValue
	 * @return settingBean if javaValue not <code>null</code>, <code>null</code> if javavalue is null.
	 * 
	 * @since 1.1.0
	 */
	protected IInternalBeanProxyHost getSettingBeanProxyHost(IJavaInstance javaValue) {
		return (IInternalBeanProxyHost) BeanProxyUtilities.getBeanProxyHost(javaValue, getBeanProxyDomain().getEditDomain());
	}


	/**
	 * This will instantiate a setting bean (i.e. a bean that is being applied to a setting).
	 * If there are any errors creating the expression, then the error will be processed as an 
	 * instantiation error and <code>null</code> will be returned.
	 * <p>
	 * If there where any IExpression evaluation errors (i.e. something went wrong evaluating the expression, not in creating the expression),
	 * then a  {@link org.eclipse.ve.internal.java.remotevm.BeanInstantiationException} will of been thrown. Callers normally do not need to worry
	 * about this because the throw will pass through the caller and be caught by the apply code (e.g. applied()). But if this method
	 * is called from some other place then the caller should catch this in their expression and not do anything with the setting.
	 * <p>
	 * Any instantiation errors will be logged as settings error for this setting. Callers do not need to worry about that.
	 * <p>
	 * This is not to be used on null settings. 
	 * Callers should code as:
	 * <pre><code>
	 *     IProxy result = instantiateSettingBean(...);
	 *     if (result == null)
	 *       ... don't continue, couldn't instantiate.
	 *     exp.createApplyOfTheSetting;
	 * </code></pre>
	 * <p>
	 * If the caller is not being called from applied() in some way then it should also catch BeanInstantiationException. This
	 * way they won't go on to actually try to use the setting, and the error will be caught and allow others on the expression
	 * to continue.
	 * <pre><code>
	 *     exp.createTry();
	 *     IProxy result = instantiateSettingBean(...);
	 *     if (result == null)
	 *       ... don't continue, couldn't instantiate.
	 *     exp.createApplyOfTheSetting;
	 *     exp.createTryCatch(getBeanInstantiationExceptionTypeProxy(exp));
	 *     exp.createTryEnd();
	 * </code></pre>
	 * @param settingBean the setting bean to instantiate. It must not be null.
	 * @param expression
	 * @param feature
	 * @param value
	 * @return the setting proxy or <code>null</code> if it didn't work.
	 * 
	 * @since 1.1.0
	 */	
	protected IProxy instantiateSettingBean(IInternalBeanProxyHost settingBean, IExpression expression, EStructuralFeature feature, Object value) {
		return instantiateSettingBean(settingBean, expression, feature, value, this);
	}
	
	/**
	 * Same as {@link BeanProxyAdapter#instantiateSettingBean(IBeanProxyHost, IExpression, EStructuralFeature, Object)} except that
	 * you provide an ErrorNotifier to use. This is a bit of kludge, but it is used by subclasses that have an intermediate object for
	 * their children, and the intermediate object can have its own errors. For example the jfc.ContainerProxyAdapter does this for
	 * its children.
	 * 
	 * @param settingBean
	 * @param expression
	 * @param feature
	 * @param value
	 * @param errorNotifier	errorNotifier to add instantiation error to, or <code>null</code> if error is ignored. null would be used
	 * for children since the child would be separately displayed in the tree it would have the instantiation error there. Standard
	 * settings should not use null because there wouldn't be a child for them to show the instantiation error.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxy instantiateSettingBean(final IInternalBeanProxyHost settingBean, IExpression expression, final EStructuralFeature feature, final Object value, final ErrorNotifier errorNotifier) {
		if (settingBean == null)
			return null;	// No setting bean.
		if (settingBean.isBeanProxyInstantiated())
			return settingBean.getBeanProxy();
		if (errorNotifier != null)
			errorNotifier.clearError(feature, value);
		IProxy result = null;
		// See if being instantiated, if not then do instantiation.
		result = settingBean.getProxy();
		if (result == null) {
			// Treated as:
			// try {
			//   instantiate
			// } catch (Exception e) {
			//   .. process the bean's instantiation error as the error for this property ..
			// }
			expression.createTry();
			result = settingBean.instantiateBeanProxy(expression);
			if (result != null) {
				ExpressionProxy beanInstantiateException = expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), errorNotifier != null);
				if (errorNotifier != null) {
					beanInstantiateException.addProxyListener(new ExpressionProxy.ProxyAdapter() {

						public void proxyResolved(ProxyEvent event) {
							processSettingBeanInstantiationErrors(errorNotifier, settingBean, feature, value);
						}

						public void proxyNotResolved(ExpressionProxy.ProxyEvent event) {
							processSettingBeanInstantiationErrors(errorNotifier, settingBean, feature, value);
						}
					});
				}
				expression.createRethrow();
			}
			expression.createTryEnd();
		} else
			return result;
		
		// This will handle any immediate instantiation errors (i.e. occurred either in the IDE while creating the expression, or an error from instantiateBeanProxy without an expression).
		if (errorNotifier != null && settingBean.hasInstantiationErrors()) {
			processSettingBeanInstantiationErrors(errorNotifier, settingBean, feature, value);
			return null;
		}					
		return result;
	}
	
	/*
	 * Process the instantiation errors on the setting bean.
	 */
	private void processSettingBeanInstantiationErrors(ErrorNotifier errorNotifier, IInternalBeanProxyHost settingBean, EStructuralFeature feature, Object value) {
		List errors = settingBean.getInstantiationError();
		for (int i = 0; i < errors.size(); i++) {
			ErrorType errorType = (ErrorType) errors.get(i);
			// If the severity of the error is less than warning, (such as info), then we will
			// show that instead as the error status over here in ours.
			errorNotifier.processError(new BeanPropertyError(Math.min(ERROR_WARNING, errorType.getSeverity()), errorType, feature, value), null);
		}							

	}


	/**
	 * Get the IProxyBeanType for the BeanInstantiationError.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxyBeanType getBeanInstantiationExceptionTypeProxy(IExpression expression) {
		return getBeanTypeProxy(
				IBeanProxyHost.BEAN_INSTANTIATION_EXCEPTION, expression);
	}
	
	/**
	 * Apply the bean property directly using the property decorator and proxy. It will retrieve the original value if the flag is true.
	 * <p>
	 * If it can't correct an evaluation thrown exception in the IExpression, this method should let the exception propagate up
	 * to the caller. The caller will know how to handle the error.
	 * <p>
	 * This is also called in the default cancel to apply the original value back into the setting.
	 * <p>
	 * May be overridden to apply in a different way by subclasses. This method is usually overridden if it is a simple property that
	 * just needs to be applied by a different call then the usual. Anything more complicated should be done through overriding applySetting
	 * instead. 
	 * 
	 * @param propertyDecorator decorator describing how to apply the property.
	 * @param settingProxy the bean proxy to apply.
	 * @param expression the expression to do the apply through.
	 * @param getOriginalValue if <code>true</code> then when the apply occurs, the previous value is returned from the method.
	 *
	 * @return the IProxy for original value if getOriginalValue flag was true and an original value can be queried., else return <code>null</code>   
	 * @throws NoSuchMethodException thrown if the setter method cannot be found.
	 * @throws NoSuchFieldException thrown if the field cannot be found.
	 * @since 1.1.0
	 */
	protected IProxy applyBeanProperty(final PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue) throws NoSuchMethodException, NoSuchFieldException {
		IProxy origValue = null;
		if(propertyDecorator.getWriteMethod() != null){
			if (getOriginalValue && propertyDecorator.isReadable()) {
				try {
					IProxyMethod readMethod = BeanProxyUtilities.getMethodProxy(expression, propertyDecorator.getReadMethod());
					ExpressionProxy oldValueProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
					expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, readMethod, true, 0);
					expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getProxy());
					origValue = oldValueProxy;
				} catch (NoSuchMethodException e) {
					JavaVEPlugin.log(e, Level.INFO);	// Not as important to know if occured, so lower priority and don't flag it.
					// If we can't get the original value, we can't get it, so we don't want to store it.
				}
			}
			IProxyMethod writeMethod = BeanProxyUtilities.getMethodProxy(expression, propertyDecorator.getWriteMethod());
			expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, writeMethod, true, 1);
			expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getProxy());
			expression.createProxyExpression(ForExpression.METHOD_ARGUMENT, settingProxy);
		} else if (propertyDecorator.getField() != null && !propertyDecorator.isFieldReadOnly()){
			IProxyField field = BeanProxyUtilities.getFieldProxy(expression, propertyDecorator.getField());
			if (getOriginalValue) {
				ExpressionProxy oldValueProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
				expression.createFieldAccess(ForExpression.ASSIGNMENT_RIGHT, field, true);
				expression.createProxyExpression(ForExpression.FIELD_RECEIVER, getProxy());
				origValue = oldValueProxy;
			}
			expression.createAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createFieldAccess(ForExpression.ASSIGNMENT_LEFT, field, true);
			expression.createProxyExpression(ForExpression.FIELD_RECEIVER, getProxy());
			expression.createProxyExpression(ForExpression.ASSIGNMENT_RIGHT, settingProxy);
		}
		return origValue;
	}

	/**
	 * Apply all of the values in the list. This may be overridden to apply in a different way. The default is to
	 * just pass each entry in the list on to applied with the proper index.
	 * <p>
	 * Note: Overrides must respect the <code>testValidity</code> flag if they override and apply something themselves rather
	 * than delegating to this implementation.
	 * @param feature
	 * @param values
	 * @param index
	 * @param isTouch <code>true</code> if this is a touch. This means that the same object was set back into the property. This can be used by overrides to decide not to apply the live value again. By default it does apply it.
	 * @param expression
	 * @param testValidity <code>true</code> if true then the validity of the feature needs to be tested. Use {@link ErrorNotifier#hasErrorsOfKeyObject(EStructuralFeature, Object)} for testing.
	 * 
	 * @since 1.1.0
	 */
	protected void appliedList(EStructuralFeature feature, List values, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		Iterator iter = values.iterator();	
		while (iter.hasNext()){
			if (expression.isValid()) {
				int mark = expression.mark();
				try {
					applied(feature, iter.next(), index, isTouch, expression, testValidity);
					if (index != -1)
						index++;
				} finally {
					expression.endMark(mark);
				}
			} else
				break;	// Expression has gone invalid, so don't go any further.
		}
	}
	
	/**
	 * Cancel all of the values in the list. This may be overridden to cancel in a different way. The default is to pass each
	 * old value (starting from the end and moving backwards) to canceled with the proper index.
	 * 
	 * @param sf
	 * @param oldValues
	 * @param position
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void canceledList(EStructuralFeature sf, List oldValues, int position, IExpression expression){
		ListIterator iter = oldValues.listIterator(oldValues.size());
		if (position != -1)
			position += (oldValues.size()-1);
		while (iter.hasNext()){
			if (expression.isValid()) {
				int mark = expression.mark();
				try {
					canceled(sf, iter.next(), position, expression);
					if (position != -1)
						position--; 
				} finally {
					expression.endMark(mark);
				} 
			} else
				break;
		}
	}	
	
	// Whether this proxy is wrappering the "this" part for a BeanSubclassComposition. If it is, then
	// the bean that gets instantiated is the superclass (since we are in the process of developing
	// this bean, we can't actually instantiate it). Also, when querying for property values, we
	// won't submit properties that are defined as a local property (i.e. one that is defined at
	// this class level) since we don't have this class level instantiated.
	// null means not yet initialized. It is figured out by seeing if its container sf is thisPart.
	// TODO Need to figure out how to get "this" out of here. It throws complications in.
	// KLUDGE: This is also used to indicate in instantiate whether this is the first instantiation
	// or it is a reinstantiation. If it is null then it is the first instantiation. Need to do
	// a better way. Didn't want to have another boolean which simply said this is the initial instantiation.
	protected Boolean isThis;
	
	/**
	 * Is this a "this" part? If so then you don't instantiate it directly, you instantiate a superclass, nor do you
	 * apply properties that are local only to the class.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isThisPart() {
		if (isThis == null) {
			EStructuralFeature containersf = getEObject().eContainmentFeature();
			isThis = Boolean.valueOf(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart() == containersf);
		}
		return isThis.booleanValue();
	}
	
	/**
	 * Is this a local feature (i.e. one that is on the classes that are below the instantiated class). Takes into consideration
	 * that the class is abstract (or this is a this).
	 * <p>
	 * TODO only applicable with this part. So when thispart goes, this can go.
	 * @param attr
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isAttributeLocal(EStructuralFeature attr) {
		return notInstantiatedClasses != null ? notInstantiatedClasses.contains(attr.eContainer()) : false;
	}
	
	/**
	 * Just instantiate the bean proxy using the expression processer.
	 * <p>
	 * Subclasses should override only to provide additional function. To change the function, they should instead override
	 * primInstantiateDroppedPart and primInstantiateThisPart instead.
	 * 
	 * @param expression the expression will be valid upon return if valid upon entry. If AllocationException thrown, the expression state
	 * will be as it was upon entry.
	 * @return the bean's IProxy. <code>null</code> is not a valid return value. Subclasses must return a value, or return an AllocationException.
	 * 
	 * @throws AllocationException
	 * @since 1.1.0
	 */
	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		if (!isThisPart()) {
			return primInstantiateDroppedPart(expression);
		} else {
			ownsProxy = true; // Since we created it and we are a "this", obviously we own it
			IProxyBeanType targetClass = getValidSuperClass(expression);
			return primInstantiateThisPart(targetClass, expression);
		}
	}


	/**
	 * This is for instantiating non-this part proxies.
	 * @param expression
	 * @return
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	protected IProxy primInstantiateDroppedPart(IExpression expression) throws AllocationException {
		if (getJavaObject().isSetAllocation()) {
			JavaAllocation allocation = getJavaObject().getAllocation();
			ownsProxy = !allocation.isImplicit();	// Implicit allocations are never owned.
			return getBeanProxyDomain().getAllocationProcesser().allocate(allocation, expression);
		}
		// otherwise just create it using the default ctor.
		String qualifiedClassName = getJavaObject().getJavaType().getQualifiedNameForReflection();
		IProxyBeanType targetClass = getBeanTypeProxy(qualifiedClassName, expression);
		ownsProxy = true; // Since we created it and we aren't implict, obviously we own it.
		return BasicAllocationProcesser.instantiateWithString(null, targetClass, expression);
	}
	
	/**
	 * This is used to instantiate as a this part. Called from {@link #primInstantiateBeanProxy(IExpression)}. The default
	 * primInstantiateBeanProxy walks up the parent chain until it finds a valid superclass to instantiate and passes that
	 * into this call.
	 * @param expression
	 * @return
	 * @throws AllocationException
	 * @since 1.1.0
	 */
	protected IProxy primInstantiateThisPart(IProxyBeanType targetClass, IExpression expression) throws AllocationException {
		return BasicAllocationProcesser.instantiateWithString(null, targetClass, expression);
	}


	/**
	 * Get the valid superclass that can be instantiated for a thisPart. This is called by
	 * {@link #primInstantiateBeanProxy(IExpression)}. Subclasses may override to provide
	 * a valid super class if they don't want the default. However, they must do the same thing
	 * in putting a valid list of notInstantiatedClasses of all the abstract classes in-between
	 * up to the concrete valid super class. This is so that is known to not try to set
	 * properties on those classes since the concrete class doesn't implement those methods.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxyBeanType getValidSuperClass(IExpression expression) {
		// We are a "this" part, so instantiate the super class (go up until we reach one that is not abstract) instead.
		// Also, since the initialization string applies only to the target class, we
		// can't have one when instantiating the superclass.
		notInstantiatedClasses = new ArrayList(2);
		JavaClass thisClass = (JavaClass) ((IJavaInstance) getTarget()).getJavaType();
		notInstantiatedClasses.add(thisClass);
		JavaClass superclass = thisClass.getSupertype();
		while (superclass != null && superclass.isAbstract()) {
			notInstantiatedClasses.add(superclass);
			superclass = superclass.getSupertype();
		}
		IProxyBeanType targetClass = null;
		if (superclass != null)
			targetClass = getBeanTypeProxy(superclass.getQualifiedNameForReflection(), expression);
		else
			targetClass = getBeanTypeProxy("java.lang.Object", expression); //$NON-NLS-1$
		return targetClass;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public final void releaseBeanProxy() {
		if (isBeanProxyInstantiated()) {
			IExpression exp = getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			try {
				releaseBeanProxy(exp);
			} finally {
				try {
					if (exp.isValid())
						exp.invokeExpression();
					else
						exp.close();
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (ThrowableProxy e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (NoExpressionValueException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			}
		}
			
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost2#releaseBeanProxy(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public final void releaseBeanProxy(final IExpression expression) {
		int mark = expression != null ? expression.mark() : -1;
		try {
			if (isBeanProxyInstantiated()) {
				if (LOG_NOTIFICATIONS) {
					StringBuffer r = new StringBuffer();
					r.append("Release request: "); //$NON-NLS-1$
					printObject(getJavaObject(), r);
					JavaVEPlugin.log(r.toString(), Level.WARNING);
				}
				ReinstantiateBeanProxyNotification notification = new ReinstantiateBeanProxyNotification(expression);
				notification.setPrerelease(true);
				// Now walk through all references and tell of pre-release.
				InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(getTarget(),
						InverseMaintenanceAdapter.ADAPTER_KEY);
				if (ai != null)
					fireReinstantiateNotice(notification, ai);
			}

			primReleaseBeanProxy(expression);
		} finally {
			if (expression != null)
				expression.endMark(mark);
		}
	}


	/**
	 * Actually perform release. Subclasses must call super.primReleaseBeanProxy() when they are done to have
	 * the release performed correctly.
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void primReleaseBeanProxy(final IExpression expression) {
		overrideSettings = null;
		origSettingProxies = null;
		notInstantiatedClasses = null;
		if (isBeanProxyInstantiated() || hasErrorsOfKey(INSTANTIATION_ERROR_KEY)) {
			// Either we have a bean proxy, or there was an instantiation error, but in that case we may of had some instantiated settings that
			// need to be released.

			// Default is to release any settings that are contained as "property/implicit" settings, since these are not set in anybody else,
			// and throw the bean proxy away. 
			// Other subclasses may actually do something else first.
			getJavaObject().visitSetFeatures(new FeatureValueProvider.Visitor() {
			
				public Object isSet(EStructuralFeature feature, Object value) {
					if (value instanceof List) {
						Iterator i = ((List) value).iterator();
						while (i.hasNext()) {
							Object setting = i.next();
							releaseSetting(setting, feature, expression);
						}
					} else
						releaseSetting(value, feature, expression);
					return null;
				}
				
				/*
				 * Since we are releasing the parent bean, release the settings for it. Release only those that are not contained,
				 * properties, or implicit settings that are for this feature and bean because these are
				 * owned by this bean. Non-implicit members will not be released because someone else may own them.
				 * <p>
				 * Final control of release is done by the MemberContainer. It will release any beans when their
				 * containment is removed. That way any missed here will get released eventually.
				 * 
				 * @param value
				 * @param expression The expression will be valid on return.
				 * @since 1.1.0
				 */
				private final void releaseSetting(Object value, EStructuralFeature feature, IExpression expression) {
					if (value instanceof IJavaInstance) {
						IJavaInstance j = (IJavaInstance) value;
						JavaAllocation alloc = j.getAllocation();
						boolean ownedImplict = false;
						if (alloc != null && alloc.isImplicit()) {
							ImplicitAllocation impAlloc = (ImplicitAllocation) alloc;
							ownedImplict = impAlloc.getParent() == getTarget() && impAlloc.getFeature() == feature;
						}
						if (ownedImplict || j.eContainer() == null || j.eContainingFeature() == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
							IBeanProxyHost settingBean =  (IBeanProxyHost) EcoreUtil.getExistingAdapter((EObject) value, IBeanProxyHost.BEAN_PROXY_TYPE);
							if (settingBean != null)
								settingBean.releaseBeanProxy(expression);
						}
					}
				}
			});
			
			if (ownsProxy && isBeanProxyInstantiated()) {
				ProxyFactoryRegistry registry = getBeanProxy().getProxyFactoryRegistry();
				// Check if valid, this could of occured due to finalizer after registry has been stopped.
				if (registry.isValid()) {
					registry.releaseProxy(getBeanProxy()); // Give it a chance to clean up
				}
			}
		}
		beanProxy = null; // Now throw it away
		ownsProxy = false;
	}


	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#getBeanPropertyValue(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public IJavaInstance getBeanPropertyValue(EStructuralFeature aBeanPropertyAttribute) {
		// Only ask if we have a live proxy.
		// Don't query if this is a local attribute and we are the this part.	
		if (isBeanProxyInstantiated() && (!isThisPart() || !isAttributeLocal(aBeanPropertyAttribute))) {
			IBeanProxy valueProxy = getBeanPropertyProxyValue(aBeanPropertyAttribute);
			IJavaInstance bean = BeanProxyUtilities.wrapperBeanProxy( valueProxy , JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain()), false , InstantiationFactory.eINSTANCE.createImplicitAllocation(getEObject(), aBeanPropertyAttribute), (JavaHelpers) aBeanPropertyAttribute.getEType());
			return bean;
		}
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#getBeanPropertyProxyValue(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public final IBeanProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute){
		if (isBeanProxyInstantiated()) {
			IExpression expression = getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			IProxy result = null;
			try {
				result = getBeanPropertyProxyValue(aBeanPropertyAttribute, expression, ForExpression.ROOTEXPRESSION);
			} finally {
				if (expression.isValid()) {
					try {
						if (result != null) {
							if (result.isExpressionProxy()) {
								class GetResult extends ExpressionProxy.ProxyAdapter {
									public IBeanProxy expressionResult;
									
									public void proxyResolved(ExpressionProxy.ProxyEvent event) {
										expressionResult = event.getProxy();
									}
								}
								
								GetResult getResult = new GetResult();
								((ExpressionProxy) result).addProxyListener(getResult);
								expression.invokeExpression();
								return getResult.expressionResult;
							}
						}
						// Either null, or a proxy, so invokeexpression and return it. Need to always invoke expression because
						// we don't know what was done in getBeanPropertyProxyValue(). If it didn't do anything to the expression, the
						// invoke will be quick.
						expression.invokeExpression();
						return (IBeanProxy) result;
					} catch (IllegalStateException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (ThrowableProxy e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (NoExpressionValueException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}
				}
			}
					
		} 
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#getBeanProxy()
	 */
	public final IBeanProxy getBeanProxy() {
		return (IBeanProxy) (beanProxy != null && beanProxy.isBeanProxy() ? beanProxy : null);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#getProxy()
	 */
	public final IProxy getProxy() {
		return beanProxy;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#getOriginalSettingsTable()
	 */
	public Map getOriginalSettingsTable() {
		if (origSettingProxies == null)
			origSettingProxies = new HashMap(20);
		return origSettingProxies;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#isSettingInOriginalSettingsTable(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean isSettingInOriginalSettingsTable(EStructuralFeature feature) {
		return origSettingProxies != null && origSettingProxies.containsKey(feature);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#applyBeanPropertyProxyValue(org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.jem.internal.proxy.core.IBeanProxy)
	 */
	public void applyBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IBeanProxy aproxy) {
		PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( aBeanPropertyFeature);
		if ((propertyDecorator != null && propertyDecorator.isWriteable())) {
			IExpression exp = getBeanProxyFactory().createExpression();
			try {
				applyBeanProperty(propertyDecorator, aproxy, exp, false);
				exp.invokeExpression();
			} catch (NoSuchMethodException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalStateException e) {
				JavaVEPlugin.log(e, Level.SEVERE);
			} catch (ThrowableProxy e) {
			} catch (NoExpressionValueException e) {
			} finally {
				exp.close();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#instantiateBeanProxy()
	 */
	public final IBeanProxy instantiateBeanProxy() {
		if (!isBeanProxyInstantiated() && !inInstantiation()) {
			// We will create an expression and do normal instantiation.
			while (true) {
				IExpression expression = getBeanProxyFactory().createExpression();

				try {
					expression.createTry();
					try {
						instantiateBeanProxy(expression);
						expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
					} finally {
						if (expression.isValid()) {
							expression.createTryEnd();
							expression.invokeExpression(); // Now invoke it.
						} else
							expression.close();
					}
					break;
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.WARNING); // This shouldn't occur because we should be catching these in instantiation and setting flags correctly.
					processInstantiationError(new ExceptionError(e, ERROR_SEVERE));
					break; // So don't go on.
				} catch (ThrowableProxy e) {
					// This should only occur if an error occured evaluating this bean. If it had occured, we should of already done processError. 
					break; // But we can't continue because the error was in instantiation. Retry wouldn't help.
				} catch (NoExpressionValueException e) {
					// This shouldn't occur. The code should never produce this, but if it does, we don't know where, so the entire bean is bad.
					processInstantiationError(new ExceptionError(e, ERROR_SEVERE));
					break; // Retry won't help.
				} finally {
					expression.close();
				}
			}
		}
		return getBeanProxy();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#isBeanProxyInstantiated()
	 */
	public final boolean isBeanProxyInstantiated() {
		return beanProxy != null && beanProxy.isBeanProxy() && ((IBeanProxy) beanProxy).isValid();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#revalidateBeanProxy()
	 */
	public void revalidateBeanProxy() {
		invalidateBeanProxy();
		validateBeanProxy();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#invalidateBeanProxy()
	 */
	public void invalidateBeanProxy() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#validateBeanProxy()
	 */
	public void validateBeanProxy() {
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#setBeanProxy(org.eclipse.jem.internal.proxy.core.IBeanProxy)
	 */
	public void setBeanProxy(IBeanProxy beanProxy) {
		if (this.beanProxy == null) {
			ownsProxy = false;
			setupBeanProxy(beanProxy);
		}		
	}

	private static final Object INSTANTIATION_ERROR_KEY = new Object();
	
	/**
	 * Set a bean proxy into this adapter. It is used
	 * after to put the bean proxy in or when an external
	 * beanproxy is explicitly set as the bean proxy.
	 * At this point in time all settings have already 
	 * been applied. This can be used to initialize other
	 * pieces of data depending on the bean proxy. If possible
	 * it should not do any proxy calls because we are no longer
	 * within an IExpression. 
	 * <p> 
	 * The default process will, if necessary,
	 * change the type of the java object instance. This is
	 * so that if what was created was a subclass, then we
	 * should be that subclass. This is so that if the type
	 * of the property was say an interface LayoutManager,
	 * this doesn't help us to put up the correct property
	 * editor. The PropertyEditor for LayoutManager doesn't
	 * exist. You want to know it is a FlowLayoutManager.
	 * So when it comes back as a FlowLayoutManager.
	 * <p>
	 * Subclasses may override setupBeanProxy, but they should always
	 * call super.setupBeanProxy().

	 * @param beanProxy
	 * 
	 * @since 1.1.0
	 */
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		this.beanProxy = beanProxy;
		if (beanProxy == null) {
			// See if we already logged instantiation errors. If we haven't then log this.
			if (!hasInstantiationErrors())
				processInstantiationError(new ExceptionError(new IllegalStateException(JavaMessages.BeanProxyAdapter_NoBeanInstantiatedForSomeReason_EXC_), ERROR_SEVERE)); 
		} else if (!beanProxy.getTypeProxy().isPrimitive()) {
			// We are trying to set a non-primitive and non-null proxy. Primitives aren't valid here because
			// this proxy adapter is only valid for non-primitives.
			String realTypeClassName = beanProxy.getTypeProxy().getFormalTypeName();
			JavaClass expectedType = (JavaClass) ((IJavaInstance) getTarget()).getJavaType();
			if (!expectedType.getQualifiedNameForReflection().equals(realTypeClassName)) {
				// It will never be an interface because it is a concrete setting.
				JavaClass javaClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(realTypeClassName, expectedType.eResource()
						.getResourceSet());
				while (javaClass != null) {
					// Find the first type in the heirarchy that is public and assignable to the expected type, otherwise leave it as the expected type.
					// TODO Technically it could be package or protected and in same package as "this" part.
					// But that would be too complicated to figure out here.
					// Even worse it may be protected, but it is a private inner class, so really not visible.
					if (expectedType.isAssignableFrom(javaClass)) {
						if (javaClass.isPublic()) {
							((InternalEObject) getTarget()).eSetClass(javaClass);
							break;
						}
					} else
						break;	// We reached one that is not compatible with the expected type. So leave it as expected type.
					javaClass = javaClass.getSupertype();
				}
			}
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost#setOwnsProxy(boolean)
	 */
	public void setOwnsProxy(boolean ownsProxy) {
		this.ownsProxy = ownsProxy;
	}
	
	/**
	 * Answer whether this proxy host owns the proxy or not. Mostly used in releaseProxy overrides
	 * to determine that if it owns it, it should be more.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final boolean isOwnsProxy() {
		return ownsProxy;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#getBeanProxyDomain()
	 */
	public final IBeanProxyDomain getBeanProxyDomain() {
		return domain;
	}

	/**
	 * Reinstantiate the bean and notify all references that it has been reinstantiated.
	 * 
	 * @param expression expression to use while reinstantiating and while notifying references. It needs to be invoked after this call.
	 * @return <code>true</code> if reinstantiation was performed, <code>false</code> if reinstantiation not performed because in instantiation already.
	 * @since 1.1.0
	 */
	protected final boolean reinstantiate(IExpression expression) {
		if (inInstantiation() || !expression.isValid())
			return false;	// Can't reinstantiate while instantiating. Shouldn't happen anyway, but be safe. Or if expression is not valid. This shouldn't happen either.
		
		clearAllErrors();	// We are reinstantiating. Retry everything again.
		primReinstantiate(expression);
		return true;
	}
	
	/*
	 * Fire the reinstantiate notification.
	 * @param notification
	 * @param ai
	 * 
	 * @since 1.1.0
	 */
	private void fireReinstantiateNotice(final ReinstantiateBeanProxyNotification notification, InverseMaintenanceAdapter ai) {
		ai.visitAllReferences(new InverseMaintenanceAdapter.Visitor() {
			public Object visit(EStructuralFeature feature, EObject reference) {
				notification.setFeature(feature);
				notification.setNotifier((InternalEObject) reference);
				if (feature.isMany()) {
					List l = (List) reference.eGet(feature);
					notification.setIndex(l.indexOf(getTarget()));
				} else
					notification.setIndex(Notification.NO_INDEX);
				reference.eNotify(notification);
				return null;
			}
		});
	}


	/**
	 * Reinstantiate the bean. The default is to release the bean and do a standard instantiation.
	 * Subclasses may override to do something specific.
	 * <p>
	 * This method should not be called by anything other than {@link BeanProxyAdapter#reinstantiate(IExpression)}.
	 * It is here for subclasses to provide alternate implementations.
	 * 
	 * @param expression the expression will be valid upon return.
	 * 
	 * @since 1.1.0
	 */
	protected void primReinstantiate(IExpression expression) {
		if (LOG_NOTIFICATIONS) {
			StringBuffer r = new StringBuffer();
			r.append("Reinstantiation request: "); //$NON-NLS-1$
			printObject(getJavaObject(), r);
			JavaVEPlugin.log(r.toString(), Level.WARNING);
		}
		releaseBeanProxy(expression);
		// try {
		//   ... instantiate ...
		// } catch (BeanInstantiationException e) {
		//   ... do nothing ... we already handled that there was an error.
		// }
		expression.createTry();
		try {
			instantiateBeanProxy(expression);
		} finally {
			if (expression.isValid()) {
				expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
				expression.createTryEnd();
			}
		}
		
	}

	/**
	 * Return target as an EObject.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final EObject getEObject() {
		return (EObject) getTarget();
	}
	
	/**
	 * Return the target as a java object.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final IJavaObjectInstance getJavaObject() {
		return (IJavaObjectInstance) getTarget();
	}

	static void logNotification(Notification n) {
		StringBuffer result = new StringBuffer();
		logNotification(n, result);
	    JavaVEPlugin.log(result.toString(), Level.WARNING);
	}


	private static void logNotification(Notification n, StringBuffer result) {
		switch (n.getEventType()) {
			case Notification.SET: {
				result.append("SET"); //$NON-NLS-1$
				break;
			}
			case Notification.UNSET: {
				result.append("UNSET"); //$NON-NLS-1$
				break;
			}
			case Notification.ADD: {
				result.append("ADD"); //$NON-NLS-1$
				break;
			}
			case Notification.ADD_MANY: {
				result.append("ADD_MANY"); //$NON-NLS-1$
				break;
			}
			case Notification.REMOVE: {
				result.append("REMOVE"); //$NON-NLS-1$
				break;
			}
			case Notification.REMOVE_MANY: {
				result.append("REMOVE_MANY"); //$NON-NLS-1$
				break;
			}
			case Notification.MOVE: {
				result.append("MOVE"); //$NON-NLS-1$
				break;
			}
			case Notification.REMOVING_ADAPTER: {
				result.append("REMOVING_ADPATER"); //$NON-NLS-1$
				break;
			}
			case Notification.RESOLVE: {
				result.append("RESOLVE"); //$NON-NLS-1$
				break;
			}
			case IInternalBeanProxyHost.NOTIFICATION_LIFECYCLE: {
				if (((ReinstantiateBeanProxyNotification) n).isPrerelease())
					result.append("PRERELEASE"); //$NON-NLS-1$
				else
					result.append("POSTREINSTANTIATION"); //$NON-NLS-1$
				break;
			}
			default: {
				result.append(n.getEventType());
				break;
			}
		}
	    if (n.isTouch())
	    {
	      result.append(", touch"); //$NON-NLS-1$
	    }
	    if (n.getPosition() != -1) {
	    	result.append(", pos: "); //$NON-NLS-1$
	    	result.append(n.getPosition());
	    }
	    result.append(", notifier: "); //$NON-NLS-1$
	    printObject(n.getNotifier(), result);
	    Object feature = n.getFeature();
	    if (feature instanceof EStructuralFeature) {
	    	result.append(", feature: "); //$NON-NLS-1$
	    	result.append(((EStructuralFeature) feature).getName());
	    }
	    Object old = n.getOldValue();
	    if (old != null) {
	    	result.append(", oldValue: "); //$NON-NLS-1$
	    	if (n.getEventType() == Notification.REMOVE_MANY) {
	    		result.append('{');
	    		List ol = (List) old;
	    		boolean first = true;
	    		for (Iterator iter = ol.iterator(); iter.hasNext();) {
	    			if (first) 
	    				first = false;
	    			else
	    				result.append(", "); //$NON-NLS-1$
					Object element = iter.next();
					printObject(element, result);
				}
	    		result.append('}');
	    	} else {
	    		printObject(old, result);
	    	}
	    }
	    Object newv = n.getNewValue();
	    if (newv != null) {
	    	result.append(", newValue: "); //$NON-NLS-1$
	    	if (n.getEventType() == Notification.ADD_MANY) {
	    		result.append('{');
	    		List nl = (List) newv;
	    		boolean first = true;
	    		for (Iterator iter = nl.iterator(); iter.hasNext();) {
	    			if (first) 
	    				first = false;
	    			else
	    				result.append(", "); //$NON-NLS-1$
					Object element = iter.next();
					printObject(element, result);
				}
	    		result.append('}');
	    	} else {
	    		printObject(newv, result);	    		
	    	}
	    }

	    if (n.wasSet())
	    	result.append(", wasSet"); //$NON-NLS-1$
	}
	
	private static void printObject(Object o, StringBuffer b) {
		if (o instanceof IJavaInstance) {
			IJavaInstance j = (IJavaInstance) o;
			b.append('@');
			b.append(j.hashCode());
			b.append('(');
			b.append(j.getAllocation());
			b.append(')');
		} else
			b.append(o);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {

		if (LOG_NOTIFICATIONS) {
			StringBuffer result = new StringBuffer();
			if (isBeanProxyInstantiated())
				result.append("BPA active: "); //$NON-NLS-1$
			logNotification(notification, result);
		    JavaVEPlugin.log(result.toString(), Level.WARNING);

		}
		switch ( notification.getEventType()) {
			case Notification.SET:
				clearError((EStructuralFeature) notification.getFeature(), notification.getOldValue());
			case Notification.ADD:
				if (!CDEUtilities.isUnset(notification)) {						
					boolean isInstantiation = ((EStructuralFeature) notification.getFeature()).getName().equals(JavaInstantiation.ALLOCATION);				
					// If not instantiated, there is nothing we need to do.
					if (isInstantiation || isBeanProxyInstantiated()) {
						// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
						IExpression expression = getBeanProxyFactory().createExpression();
						try {
							if (!isInstantiation) {
								if (!notification.isTouch() && notification.getOldValue() != null)
									removeOldValue((EStructuralFeature) notification.getFeature(), notification.getOldValue(), notification
											.getPosition(), expression);								
								applied((EStructuralFeature) notification.getFeature(), notification.getNewValue(), notification.getPosition(),
										notification.isTouch(), expression, false);
							} else if (notification.getOldValue() != null && (isBeanProxyInstantiated() || hasInstantiationErrors())) {
								// Reinstantiation needed because we had an old allocation and we were instantiated or had instantiation errors. If we didn't have an old one then we don't reinstantiate.
								reinstantiate(expression);
							}
						} finally {
							try {
								if (expression.isValid())
									expression.invokeExpression();
								else
									expression.close();
							} catch (IllegalStateException e) {
								// Shouldn't occur. Should be taken care of in applied.
								JavaVEPlugin.log(e, Level.WARNING);
							} catch (ThrowableProxy e) {
								// Shouldn't occur. Should be taken care of in applied.
								JavaVEPlugin.log(e, Level.WARNING);
							} catch (NoExpressionValueException e) {
								// Shouldn't occur. Should be taken care of in applied.
								JavaVEPlugin.log(e, Level.WARNING);
							}
						}
					}				
					break;			
				}	// else flow into unset.
			case Notification.UNSET: 
			case Notification.REMOVE:
				// If not instantiated, there is nothing we need to do.
				clearError((EStructuralFeature) notification.getFeature(), notification.getOldValue());	
				boolean isInstantiation = ((EStructuralFeature) notification.getFeature()).getName().equals(JavaInstantiation.ALLOCATION);
				if (isInstantiation || isBeanProxyInstantiated()) {
					// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
					IExpression expression = getBeanProxyFactory().createExpression();
					try {
						if (!isInstantiation) {						
							canceled((EStructuralFeature)notification.getFeature(), notification.getOldValue(), notification.getPosition(), expression);
						} else {
							// Reinstantiation needed.
							reinstantiate(expression);
						}
					} finally {
						try {
							if (expression.isValid())
								expression.invokeExpression();
							else
								expression.close();
						} catch (IllegalStateException e) {
							// Shouldn't occur. Should be taken care of in canceled.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (ThrowableProxy e) {
							// Shouldn't occur. Should be taken care of in canceled.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (NoExpressionValueException e) {
							// Shouldn't occur. Should be taken care of in canceled.
							JavaVEPlugin.log(e, Level.WARNING);
						}
					}
				}				
				break;
			case Notification.ADD_MANY:
				// If not instantiated, there is nothing we need to do.
				if (isBeanProxyInstantiated()) {
					// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
					IExpression expression = getBeanProxyFactory().createExpression();
					try {
						appliedList((EStructuralFeature)notification.getFeature(), (List) notification.getNewValue(), notification.getPosition(), false, expression, false);
					} finally {
						try {
							expression.invokeExpression();
						} catch (IllegalStateException e) {
							// Shouldn't occur. Should be taken care of in applied.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (ThrowableProxy e) {
							// Shouldn't occur. Should be taken care of in applied.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (NoExpressionValueException e) {
							// Shouldn't occur. Should be taken care of in applied.
							JavaVEPlugin.log(e, Level.WARNING);
						}
					}
				}				
				break;
			case Notification.REMOVE_MANY:
				// If not instantiated, there is nothing we need to do.
				if (isBeanProxyInstantiated()) {
					// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
					IExpression expression = getBeanProxyFactory().createExpression();
					try {
						clearError((EStructuralFeature) notification.getFeature(), notification.getOldValue());
						canceledList((EStructuralFeature)notification.getFeature(), (List) notification.getOldValue(), notification.getPosition(), expression);
					} finally {
						try {
							expression.invokeExpression();
						} catch (IllegalStateException e) {
							// Shouldn't occur. Should be taken care of in canceled.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (ThrowableProxy e) {
							// Shouldn't occur. Should be taken care of in canceled.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (NoExpressionValueException e) {
							// Shouldn't occur. Should be taken care of in canceled.
							JavaVEPlugin.log(e, Level.WARNING);
						}
					}
				}				
				break;
				
			case Notification.REMOVING_ADAPTER:
				removingAdapter();	// Let subclasses hook into the remove.
				releaseBeanProxy();	// This adapter is being removed, so get rid of the proxy.
				break;
				
			case Notification.MOVE:
				// If not instantiated, there is nothing we need to do.
				if (isBeanProxyInstantiated()) {
					// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
					IExpression expression = getBeanProxyFactory().createExpression();
					try {
						clearError((EStructuralFeature) notification.getFeature(), notification.getNewValue());
						moved((EStructuralFeature)notification.getFeature(), notification.getNewValue(), ((Integer) notification.getOldValue()).intValue(), notification.getPosition(), expression);
					} finally {
						try {
							expression.invokeExpression();
						} catch (IllegalStateException e) {
							// Shouldn't occur. Should be taken care of in moved.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (ThrowableProxy e) {
							// Shouldn't occur. Should be taken care of in moved.
							JavaVEPlugin.log(e, Level.WARNING);
						} catch (NoExpressionValueException e) {
							// Shouldn't occur. Should be taken care of in moved.
							JavaVEPlugin.log(e, Level.WARNING);
						}
					}
				}				
				break;
				
			case IInternalBeanProxyHost.NOTIFICATION_LIFECYCLE:
				if (isBeanProxyInstantiated()) {
					try {
						IInternalBeanProxyHost.NotificationLifeCycle reinstantiation = (IInternalBeanProxyHost.NotificationLifeCycle) notification;
						// Treat this as a set by default. This will cause the proxy to be reapplied.
						if (reinstantiation.isPostReinstantiation()) {
							IExpression expression = reinstantiation.getExpression();
							try {
								// We don't treat reinstantiation as a touch because we have a NEW proxy, and so it should be applied.
								clearError((EStructuralFeature) notification.getFeature(), notification.getNewValue());
								applied((EStructuralFeature) notification.getFeature(), notification.getNewValue(), notification.getPosition(),
										false, expression, false);
							} catch (IllegalStateException e) {
								// Shouldn't occur. Should be taken care of in applied.
								JavaVEPlugin.log(e, Level.WARNING);
							}
						}

					} catch (ClassCastException e) {
						// Not a life cycle notification. Ignore it.
					}
				}
		}
	}
	
	/**
	 * Default implementation of move of feature has occurred. Subclasses should override if necessary. The default is to cancel
	 * old position and add at new position.
	 * @param feature
	 * @param value
	 * @param oldPosition
	 * @param newPosition
	 * 
	 * @since 1.0.2
	 */
	protected void moved(EStructuralFeature feature, Object value, int oldPosition, int newPosition, IExpression expression) {
		// The default is to do a remove followed by add.
		int mark = expression.mark();
		try {
			canceled(feature, value, oldPosition, expression);
		} finally {
			expression.endMark(mark);
		}
		mark = expression.mark();
		try {
			applied(feature, value, newPosition, false, expression, false);
		} finally {
			expression.endMark(mark);
		}
	}

	/**
	 * Removing adapter. Subclasses may add function. The default does nothing.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void removingAdapter() {
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return BEAN_PROXY_TYPE == type || super.isAdapterForType(type);
	}


	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#getBeanPropertyProxyValue(org.eclipse.emf.ecore.EStructuralFeature, org.eclipse.jem.internal.proxy.core.IExpression, null)
	 */
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IExpression expression, ForExpression forExpression) {
		// Only ask if we have a live proxy.
		// Don't query if this is a local attribute and we are the this part.	
		if (getProxy() != null && (!isThisPart() || !isAttributeLocal(aBeanPropertyFeature))) {
			if (overrideSettings == null || !overrideSettings.containsKey(aBeanPropertyFeature)) {
				return primGetBeanProperyProxyValue(getProxy(), Utilities.getPropertyDecorator(aBeanPropertyFeature), expression, forExpression);
			} else {
				// We have an existing override. Get the original setting. Don't want to see the override.
				return isSettingInOriginalSettingsTable(aBeanPropertyFeature) ? (IProxy) getOriginalSettingsTable().get(aBeanPropertyFeature) : null;
			}
		}
		return null;
	}
	
	/**
	 * A primitive direct way to request the current live value of the bean sent in. The bean is sent in only because this may be
	 * called before this proxy host has been fully instantiated. It shouldn't be called from other beanproxy hosts, only subclasses.
	 * This will only go through standard property decorators. It will not go through special overrides if they are needed.
	 * 
	 * @param bean
	 * @param property
	 * @param expression
	 * @param forExpression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxy primGetBeanProperyProxyValue(IProxy bean, PropertyDecorator propertyDecorator, IExpression expression, ForExpression forExpression) {
		// If we have a property decorator then it has a get method so just call it
		if (propertyDecorator != null && propertyDecorator.isReadable()) {
			if (propertyDecorator.getReadMethod() != null) {
				try {
					Method method = propertyDecorator.getReadMethod();
					IProxyMethod getMethodProxy = BeanProxyUtilities.getMethodProxy(expression, method);
					ExpressionProxy result = expression.createProxyAssignmentExpression(forExpression);
					expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, getMethodProxy, true, 0);
					expression.createProxyExpression(ForExpression.METHOD_RECEIVER, bean);
					return result;
				} catch (NoSuchMethodException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			} else if (propertyDecorator.getField() != null) {
				try {
					Field field = propertyDecorator.getField();
					IProxyField aField = BeanProxyUtilities.getFieldProxy(expression, field);
					ExpressionProxy result = expression.createProxyAssignmentExpression(forExpression);
					expression.createFieldAccess(ForExpression.ASSIGNMENT_RIGHT, aField, true);
					expression.createProxyExpression(ForExpression.FIELD_RECEIVER, bean);
					return result;
				} catch (NoSuchFieldException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			}
		} 
		return null;
	}

	/**
	 * Get the model change controller to allow execAtEndTransactions to be added.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ModelChangeController getModelChangeController(){
	    return (ModelChangeController)getBeanProxyDomain().getEditDomain().getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#addToFreeForm(org.eclipse.ve.internal.java.core.CompositionProxyAdapter)
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
		// For a standard bean proxy, it means nothing. Subclasses should override to do what they need to do.
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#removeFromFreeForm()
	 */
	public void removeFromFreeForm() {
		// For a standard bean proxy, it means nothing. Subclasses should override to do what they need to do.
	}
	
	public String toString() {
		return super.toString() + '(' + getTarget() +')';
	}
	
	public IJavaInstance getBean(){
		return (IJavaInstance)getTarget();
	}
}
