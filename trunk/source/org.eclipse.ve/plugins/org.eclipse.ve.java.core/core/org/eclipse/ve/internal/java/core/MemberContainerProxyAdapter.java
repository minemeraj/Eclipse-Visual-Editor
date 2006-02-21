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
 *  $RCSfile: MemberContainerProxyAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2006-02-21 17:16:35 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;
import org.eclipse.jem.internal.proxy.remote.REMConnection;
import org.eclipse.jem.util.TimerTests;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.ModelChangeController;

import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.MemberContainer;
 

/**
 * Basic MemberContainer proxy adapter. This handles the release or removal of proxy adapters
 * from the member container. This will handle the Members and Properties features.
 * @since 1.1.0
 */
public abstract class MemberContainerProxyAdapter extends AdapterImpl {

	/**
	 * Key for adapter type for this kind of adapter.
	 * @since 1.1.0
	 */
	public static final Class MEMBER_CONTAINER_ADAPTER_TYPE = MemberContainerProxyAdapter.class;

	/**
	 * The bean proxy domain for this adapter.
	 * @since 1.1.0
	 */
	protected IBeanProxyDomain proxyDomain;

	protected final ModelChangeController controller;	
	
	/**
	 * Construct with proxy domain.
	 * @param proxyDomain
	 * 
	 * @since 1.1.0
	 */
	public MemberContainerProxyAdapter(IBeanProxyDomain proxyDomain, ModelChangeController controller) {
		this.proxyDomain = proxyDomain;
		this.controller = controller;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		adaptAllMemberContainerAdapters();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return type == MEMBER_CONTAINER_ADAPTER_TYPE;
	}
	
	/*
	 * List of pending releases. They are queued up until the
	 * end of the transaction and then handled all at once.
	 */
	private List pendingReleases = new ArrayList();
	
	/*
	 * List of pending initializations. They are queued up until the
	 * end of the transaction and then handled all at once.
	 * They are new member settings. We don't init immediately because
	 * some other part of the current transaction may initialize them.
	 * So we wait to the end and initialize any that are not yet initialized. 
	 */
	private List pendingInits = new ArrayList();
	
	/*
	 * The runnable that is used at transaction end to release.
	 */
	private Runnable releaseRunnable = new Runnable() {
		public void run() {
			/**
			 * Only those that are not contained within a member container
			 * will then released. This will allow promotions and
			 * moves of containment not causing those so moved to 
			 * be released, since not needed in that case. Only 
			 * permanent removals need to be released. 
			 */
			Object[] release;
			synchronized (pendingReleases) {
				if (pendingReleases.isEmpty())
					return;
				release = pendingReleases.toArray();
				pendingReleases.clear();
			}
			IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			try {
				for (int i = 0; i < release.length; i++) {
					try {
						IJavaInstance o = (IJavaInstance) release[i];
						if (!isMemberContained(o))
							releaseSetting(o, expression, false);
					} catch (ClassCastException e) { 
					}
				}
			} finally {
				try {
					if (expression.isValid())
						expression.invokeExpression();
					else
						expression.close();
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (ThrowableProxy e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (NoExpressionValueException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			}
		}
	};

	/*
	 * The runnable that is used at transaction end to initialize.
	 */
	private Runnable initRunnable = new Runnable() {
		public void run() {
			/**
			 * Only those that are still contained within a member container
			 * and not yet initialized will then be initialized. This will
			 * allow other settings to do the actual initialize if needed.
			 * These then are the straglers that no one else references directly.
			 * It is assumed that the other initializations have already occurred 
			 * and are not on a later pending.
			 */
			Object[] inits;
			synchronized (pendingInits) {
				if (pendingInits.isEmpty())
					return;
				inits = pendingInits.toArray();
				pendingInits.clear();
			}
			IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			try {
				for (int i = 0; i < inits.length; i++) {
					try {
						IJavaInstance o = (IJavaInstance) inits[i];
						IBeanProxyHost host = BeanProxyUtilities.getBeanProxyHost(o);
						if (host != null && !host.isBeanProxyInstantiated() && isMemberContainedForInitialization(o) )
							initSetting(o, expression, false);
					} catch (ClassCastException e) { 
					}
				}
			} finally {
				try {
					if (expression.isValid())
						expression.invokeExpression();
					else
						expression.close();
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (ThrowableProxy e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (NoExpressionValueException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			}
		}
	};

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification msg) {
		if (BeanProxyAdapter.LOG_NOTIFICATIONS) {
			BeanProxyAdapter.logNotification(msg);
		}
		switch (msg.getEventType()) {
			case Notification.REMOVING_ADAPTER:
				releaseBeanProxy(true);
				break;
			case Notification.ADD:
			case Notification.SET:
				if (!CDEUtilities.isUnset(msg)) {
					if (!msg.isTouch()) {
						if (msg.getOldValue() != null && isMemberContainmentFeature(msg)) {
							synchronized (pendingReleases) {
								pendingReleases.add(msg.getOldValue());
							}
							controller.execAtEndOfTransaction(releaseRunnable, releaseRunnable);
						}
					}
					if (msg.getNewValue() != null && isMemberContainmentFeatureForInitialization(msg)) {
						Object newValue = msg.getNewValue();
						if (newValue instanceof IJavaInstance) {
							BeanProxyUtilities.getBeanProxyHost((IJavaInstance) newValue);	// Make sure it has a bean proxy host right away, even if not yet instantiated. Many of the editparts expect this.
							synchronized (pendingInits) {
								pendingInits.add(newValue);
							}
							controller.execAtEndOfTransaction(initRunnable, initRunnable);
						}
					}
					if (isInnerMemberContainerFeature(msg)) {
						if (!msg.isTouch())
							removeMemberContainerAdapter((MemberContainer) msg.getOldValue());
						addMemberContainerAdapter((MemberContainer) msg.getNewValue());	
					}

					break;
				} // else flow into unset.
			case Notification.REMOVE:
			case Notification.UNSET:
				if (isMemberContainmentFeature(msg)) {
					if (msg.getOldValue() != null) {
						synchronized (pendingReleases) {
							pendingReleases.add(msg.getOldValue());
						}
						controller.execAtEndOfTransaction(releaseRunnable, releaseRunnable);
					}
				} else if (isInnerMemberContainerFeature(msg))
					removeMemberContainerAdapter((MemberContainer) msg.getOldValue());
				break;
				
			case Notification.ADD_MANY:
				if (isInnerMemberContainerFeature(msg)) {
					Iterator itr = ((List) msg.getNewValue()).iterator();
					while (itr.hasNext())
						addMemberContainerAdapter((MemberContainer) itr.next());
				} else if (isMemberContainmentFeature(msg)) {
					if (msg.getNewValue() != null) {
						Iterator itr = ((List) msg.getNewValue()).iterator();
						synchronized (pendingInits) {
							while (itr.hasNext()) 
								pendingInits.add(itr.next());
						}
						controller.execAtEndOfTransaction(initRunnable, initRunnable);
					}
				}
				break;
				
			case Notification.REMOVE_MANY:
				if (isMemberContainmentFeature(msg)) {
					synchronized (pendingReleases) {
						Iterator itr = ((List) msg.getOldValue()).iterator();
						while (itr.hasNext()) {
							pendingReleases.add(itr.next());
						}
					}
					controller.execAtEndOfTransaction(releaseRunnable, releaseRunnable);					
				} else if (isInnerMemberContainerFeature(msg)) {
					Iterator itr = ((List) msg.getOldValue()).iterator();
					while (itr.hasNext())
						removeMemberContainerAdapter((MemberContainer) itr.next());
				}
				break;
		}
	}
		
	/**
	 * Answer whether the value is still contained. This is used so that
	 * if a value was promoted or moved to a different MemberContainer,
	 * it is still considered contained and we won't release.
	 * 
	 * @param value
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isMemberContained(IJavaInstance value) {
		EStructuralFeature containerFeature = value.eContainingFeature();
		return containerFeature == JCMPackage.eINSTANCE.getMemberContainer_Members() || containerFeature == JCMPackage.eINSTANCE.getMemberContainer_Properties() || containerFeature == JCMPackage.eINSTANCE.getMemberContainer_Implicits();
	}
	
	/**
	 * Answer whether it is contained as a member that needs initialization.
	 * @param value
	 * @return
	 * 
	 * @since 1.2.0
	 */
	protected boolean isMemberContainedForInitialization(IJavaInstance value) {
		return value.eContainingFeature() == JCMPackage.eINSTANCE.getMemberContainer_Members();
	}
	
	/**
	 * Answers whether the feature of the msg is a that is valid for release.
	 * @param msg
	 * @return <code>true</code> if it is a feature that is member valid for release.
	 * 
	 * @since 1.1.0
	 */
	protected boolean isMemberContainmentFeature(Notification msg) {
		int featureID = msg.getFeatureID(MemberContainer.class);
		return featureID == JCMPackage.MEMBER_CONTAINER__MEMBERS || featureID == JCMPackage.MEMBER_CONTAINER__PROPERTIES || featureID == JCMPackage.MEMBER_CONTAINER__IMPLICITS;
	}
	
	/**
	 * Answers whether the feature of the msg is a member that is valid for initialization.
	 * @param msg
	 * @return <code>true</code> if it is a member valid for initialization.
	 * 
	 * @since 1.1.0
	 */
	protected boolean isMemberContainmentFeatureForInitialization(Notification msg) {
		return msg.getFeatureID(MemberContainer.class) == JCMPackage.MEMBER_CONTAINER__MEMBERS;
	}


	/**
	 * Release the given setting.
	 * @param v
	 * @param expression expression to use, or <code>null</code> if there is no registry, such as it was terminated, and we need to just clean up.
	 * @param remove remove the adapter too.
	 * @since 1.1.0
	 */
	protected void releaseSetting(Object v, final IExpression expression, final boolean remove) {
		if (v instanceof IJavaInstance) {
			// Get existing adapter, if it doesn't have one, don't create it.
			final IInternalBeanProxyHost value = (IInternalBeanProxyHost) EcoreUtil.getExistingAdapter((Notifier) v, IBeanProxyHost.BEAN_PROXY_TYPE);
			if (value != null) {
				SafeRunner.run(new ISafeRunnable() {

					public void handleException(Throwable exception) {
						JavaVEPlugin.getPlugin().getLogger().log(exception, Level.WARNING);
					}

					public void run() throws Exception {
						value.releaseBeanProxy(expression);
						if (remove)
							value.getTarget().eAdapters().remove(value);
					}
				});
			}
		}
	}
	
	/**
	 * Release all of the bean proxies.
	 * @param remove <code>true</code> will remove the  bean proxy adapters too.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public final void releaseBeanProxy(boolean remove) {
		if (proxyDomain.getProxyFactoryRegistry().isValid()) {
			IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			try {
				releaseBeanProxy(expression, remove);
			} finally {
				try {
					if (expression.isValid())
						expression.invokeExpression();
					else
						expression.close();
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (ThrowableProxy e) {
					JavaVEPlugin.log(e, Level.WARNING);
				} catch (NoExpressionValueException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			}
		} else
			releaseBeanProxy(null, remove);	// Tell everyone to clean up but no registry.
		
		
	}
	
	/**
	 * Release all of the bean proxies. This is the actual implementations. Subclasses may override but should call super to
	 * release the basics.
	 * @param expression expression to use, or <code>null</code> if there is no registry, such as it was terminated, and we need to just clean up.
	 * @param remove remove the adapter too.
	 * 
	 * @since 1.1.0
	 */
	protected void releaseBeanProxy(IExpression expression, boolean remove) {
		Iterator settings = ((MemberContainer) target).getMembers().iterator(); // Get only the attrs and composite refs.
		while (settings.hasNext()) {
			releaseSetting(settings.next(), expression, remove);
		}
		settings = ((MemberContainer) target).getImplicits().iterator(); // Get only the attrs and composite refs.
		while (settings.hasNext()) {
			releaseSetting(settings.next(), expression, remove);
		}
		settings = ((MemberContainer) target).getProperties().iterator(); // Get only the attrs and composite refs.
		while (settings.hasNext()) {
			releaseSetting(settings.next(), expression, remove);
		}
		
		// Now signal release to all of the inner ones too.
		List innerFeatures = getInnerMemberContainerFeatures();
		if (!innerFeatures.isEmpty()) {
			EObject t = (EObject) getTarget();
			for (Iterator itr = innerFeatures.iterator(); itr.hasNext();) {
				EStructuralFeature	feature = (EStructuralFeature) itr.next();
				if (t.eIsSet(feature)) {
					if (feature.isMany()) {
						for (Iterator setItr = ((List) t.eGet(feature)).iterator(); setItr.hasNext();) {
							MemberContainer mc = (MemberContainer) setItr.next();
							MemberContainerProxyAdapter mcpa = (MemberContainerProxyAdapter) EcoreUtil.getExistingAdapter(mc, MEMBER_CONTAINER_ADAPTER_TYPE);
							if (mcpa != null)
								mcpa.releaseBeanProxy(expression, remove);
						}
					} else {
						MemberContainer mc = (MemberContainer) t.eGet(feature);
						MemberContainerProxyAdapter mcpa = (MemberContainerProxyAdapter) EcoreUtil.getExistingAdapter(mc, MEMBER_CONTAINER_ADAPTER_TYPE);
						if (mcpa != null)
							mcpa.releaseBeanProxy(expression, remove);						
					}
				}
			}
		}
	}
	
	/**
	 * Return whether this feature is an inner member container feature. I.e. this feature is another MemberContainer. This
	 * is so that the adapter can propagate down to inner member containers. By default there aren't any. Subclasses need
	 * to override and provide the correct response.
	 * 
	 * @param msg
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isInnerMemberContainerFeature(Notification msg) {
		return false;
	}
	
	/**
	 * Return the list of inner member container features. These will be
	 * used in release to release all of the inner ones too.
	 * @return
	 * 
	 * @see MemberContainerProxyAdapter#isInnerMemberContainerFeature(Notification)
	 * @since 1.1.0
	 */
	protected List getInnerMemberContainerFeatures() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * Add a member container adapter to the new member container.
	 * 
	 * @param mc
	 * 
	 * @since 1.1.0
	 */
	protected void addMemberContainerAdapter(MemberContainer mc) {
		if (mc != null) {
			Adapter mca = EcoreUtil.getExistingAdapter(mc, MEMBER_CONTAINER_ADAPTER_TYPE);
			if (mca == null)
				mc.eAdapters().add(createMemberContainerProxyAdapter(mc));
		}
	}

	/**
	 * Create a member container proxy adapter for the given membercontainer. Don't add it, just
	 * create it. Default is to create a MemberContainerProxyAdapter. Subclasses should override
	 * if they need a special one.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected MemberContainerProxyAdapter createMemberContainerProxyAdapter(MemberContainer me) {
		return new MemberContainerProxyAdapter(proxyDomain, controller) {
			protected void adaptAllMemberContainerAdapters() {
				// Default is no inner members.
			}
		};
	}

	/**
	 * Remove the MemberContainer adapter from the member container.
	 * @param mc
	 * 
	 * @since 1.1.0
	 */
	protected void removeMemberContainerAdapter(MemberContainer mc) {
		if (mc != null) {
			Adapter mca = EcoreUtil.getExistingAdapter(mc, MEMBER_CONTAINER_ADAPTER_TYPE);
			if (mca != null)
				mc.eAdapters().remove(mca);
		}
	}
	
	/**
	 * Add member container adapters to all inner member containers. This is for initializations.
	 * 
	 * @since 1.1.0
	 */
	protected abstract void adaptAllMemberContainerAdapters();


	/**
	 * Initialize the setting.
	 * 
	 * @param setting
	 * @param expression
	 * @param testValidity
	 *            <code>true</code> if test for instantiation error on bean first, and if error, don't instantiate it. This is needed because if
	 *            there was an expression creation error (e.g. IllegalStateException) we want to go back and reinstantiate all of the beans that are
	 *            not yet instantiated AND don't have an error. This flag will be <code>false</code> the first time, which means always instantiate
	 *            the bean.
	 * 
	 * @since 1.1.0
	 */
	protected void initSetting(Object setting, IExpression expression, boolean testValidity) {
		if (setting instanceof IJavaInstance && expression.isValid()) {
			IBeanProxyHost settingBean = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) setting);
			// We have proxy host,and we have a valid proxy domain.
			if (settingBean != null && settingBean.getBeanProxyDomain().getProxyFactoryRegistry().isValid()) {
				IInternalBeanProxyHost ib = (IInternalBeanProxyHost) settingBean;
				if (ib.isBeanProxyInstantiated() || (!ib.inInstantiation() && (!testValidity || !ib.hasInstantiationErrors()))) {
					if (ib.isBeanProxyInstantiated())
						ib.releaseBeanProxy(expression);	// In case not already released. We will always reinstantiate. For most components you don't need to, but some require it, so we will do by default.
					expression.createTry();
					ib.instantiateBeanProxy(expression);
					expression.createTryCatchClause(IInternalBeanProxyHost.BEAN_INSTANTIATION_EXCEPTION, false);
					expression.createTryEnd();
				} 
			}
		}
	}
	
	/**
	 * Initialize all of the bean proxies. This is used to start up the member container.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void initBeanProxy() {
		boolean testValidity = false; // The first time through we don't test the validity. We try to instantiate them all.
		while (true) {
			IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			try {
				if (!subInitBeanProxy(expression, testValidity)) {
					testValidity = true;
					continue;	// Try again, but this time test validity.
				}
				// Now go through our still un-initialized members, and initialize those not yet initialized.
				if (!memberInitBeanProxy(expression, testValidity)) {
					testValidity = true;
					continue;
				}
				break; // We got through it all.
			} finally {
				try {
					TimerTests.basicTest.startStep("eval"); //$NON-NLS-1$
					TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_STEP);
					TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_METHOD_STEP);
					if (expression.isValid())
						expression.invokeExpression();
					else
						expression.close();
					TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_METHOD_STEP);
					TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_STEP);					
					TimerTests.basicTest.stopStep("eval");								 //$NON-NLS-1$
				} catch (IllegalStateException e) {
					JavaVEPlugin.log(e, Level.WARNING);
					testValidity = true; // We will try again, but this time don't instantiate those that had an error.
				} catch (ThrowableProxy e) {
					JavaVEPlugin.log(e, Level.WARNING);
					break; // This shouldn't of occured, we already should of processed it, don't try again.
				} catch (NoExpressionValueException e) {
					JavaVEPlugin.log(e, Level.WARNING);
					// This shouldn't occur. The code should never produce this, but if it does, we don't know where, so the entire composition is
					// bad.
					break;
				}
			}
		}
	}
	
	/**
	 * Called by (@link #initBeanProxy()} when it is time to initialize any non-initialized member of the container. It can also be
	 * called recursively on a contained MemberContainer.
	 * @param expression
	 * @param testValidity
	 * @return <code>true</code> to continue processing, or <code>false</code> to indicate had an error, try the loop again, this time test validity.
	 * 
	 * @since 1.2.0
	 */
	protected boolean memberInitBeanProxy(IExpression expression, boolean testValidity) {
		// Only init members. Properties and implicits are handled by their owners.
		Iterator settings = ((MemberContainer) target).getMembers().iterator(); 
		int i = 0;
		while (settings.hasNext()) {
			String step = "init#" + i++; //$NON-NLS-1$
			TimerTests.basicTest.startStep(step);
			TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_STEP);
			TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_METHOD_STEP);
			initSetting(settings.next(), expression, testValidity);
			TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_METHOD_STEP);
			TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_STEP);
			TimerTests.basicTest.stopStep(step);
			if (!expression.isValid()) {
				return false; // We will try again, but this time don't instantiate those that had an error.
			}
		}
		
		// Now signal release to all of the inner ones too.
		List innerFeatures = getInnerMemberContainerFeatures();
		if (!innerFeatures.isEmpty()) {
			EObject t = (EObject) getTarget();
			for (Iterator itr = innerFeatures.iterator(); itr.hasNext();) {
				EStructuralFeature	feature = (EStructuralFeature) itr.next();
				if (t.eIsSet(feature)) {
					if (feature.isMany()) {
						for (Iterator setItr = ((List) t.eGet(feature)).iterator(); setItr.hasNext();) {
							MemberContainer mc = (MemberContainer) setItr.next();
							MemberContainerProxyAdapter mcpa = (MemberContainerProxyAdapter) EcoreUtil.getExistingAdapter(mc, MEMBER_CONTAINER_ADAPTER_TYPE);
							if (mcpa != null) {
								if (!mcpa.memberInitBeanProxy(expression, testValidity)) {
									return false;
								}
							}
						}
					} else {
						MemberContainer mc = (MemberContainer) t.eGet(feature);
						MemberContainerProxyAdapter mcpa = (MemberContainerProxyAdapter) EcoreUtil.getExistingAdapter(mc, MEMBER_CONTAINER_ADAPTER_TYPE);
						if (mcpa != null) {
							if (!mcpa.memberInitBeanProxy(expression, testValidity)) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}	
	
	/**
	 * Called by {@link #initBeanProxy()} before doing the default member container initialization. It allows subclasses to do
	 * any initialization they want, and then the member container will then handle initializing any member (non-property) that is
	 * left over and has not initialized or started initialization.
	 * @param expression
	 * @param testValidity
	 * @return <code>true</code> to continue processing, or <code>false</code> to indicate had an error, try the loop again, this time test validity.
	 * @since 1.2.0
	 */
	protected boolean subInitBeanProxy(IExpression expression, boolean testValidity) {
		return true;
	}
}
 