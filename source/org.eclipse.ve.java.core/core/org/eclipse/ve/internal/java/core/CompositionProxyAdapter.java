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
package org.eclipse.ve.internal.java.core;

/*
 *  $RCSfile: CompositionProxyAdapter.java,v $
 *  $Revision: 1.19 $  $Date: 2005-08-24 23:30:45 $ 
 */
import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;
import org.eclipse.jem.internal.proxy.remote.REMConnection;
import org.eclipse.jem.util.TimerTests;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.impl.JCMPackageImpl;

/**
 * This is a special adaptor for BeanCompositions. There really isn't a bean proxy, but we need to handle the components and the freeform.
 * 
 * @since 1.1.0
 */
public class CompositionProxyAdapter extends MemberContainerProxyAdapter {

	/**
	 * Key for this kind of adapter.
	 * @since 1.1.0
	 */
	public static final Class BEAN_COMPOSITION_PROXY = CompositionProxyAdapter.class;

	// Table of freeform hosts. key->IFreeFormHost. The adapter doesn't create the hosts, but it will maintain them and release them when done.
	private Map freeformHosts;

	/**
	 * This is the interface for a free form host controller. They will be stored by key in
	 * the CompositionProxyAdapter.
	 * 
	 * @see CompositionProxyAdapter#addFreeForm(Object, IFreeFormHost)
	 * @see CompositionProxyAdapter#getFreeForm(Object)
	 * @since 1.1.0
	 */
	public interface IFreeFormHost {
		/**
		 * Called by CompositionProxyAdapter when time to be disposed. It is going away.
		 * 
		 * @param expression expression to use, or <code>null</code> if there is no registry, such as it was terminated, and we need to just clean up.
		 * 
		 * @since 1.1.0
		 */
		public void dispose(IExpression expression);
		
		/**
		 * Get the composition proxy adapter that created this freeform host.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public CompositionProxyAdapter getCompositionAdapter();
	}
	
	/**
	 * Construct with the proxy domain.
	 * @param proxyDomain
	 * 
	 * @since 1.1.0
	 */
	public CompositionProxyAdapter(IBeanProxyDomain proxyDomain, ModelChangeController controller) {
		super(proxyDomain, controller);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return super.isAdapterForType(type) || BEAN_COMPOSITION_PROXY.equals(type);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification msg) {
		super.notifyChanged(msg);
		
		switch (msg.getEventType()) {
			case Notification.ADD:
			case Notification.SET:
				if (isCompositionFeature(msg)) {
					if (!CDEUtilities.isUnset(msg)) {
						IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
						try {
							if (!msg.isTouch() && msg.getOldValue() != null)
								releaseSetting(msg.getOldValue(), expression, false);									
							initSetting(msg.getNewValue(), expression, false);
						} catch (IllegalStateException e) {
							JavaVEPlugin.log(e, Level.WARNING);
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
						break;
					} // else flow into unset.
				} else
					break;
			case Notification.REMOVE:
			case Notification.UNSET:
				if (isCompositionFeature(msg) && msg.getOldValue() != null) {
					// We want to release these even if still considered to be a member because this means
					// its parentage will be changing and it needs to be released for this to work.
					IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
					try {
						releaseSetting(msg.getOldValue(), expression, false);
					} catch (IllegalStateException e) {
						JavaVEPlugin.log(e, Level.WARNING);
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
				break;
			case Notification.ADD_MANY:
				if (isCompositionFeature(msg)) {
					IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
					try {
						Iterator itr = ((List) msg.getNewValue()).iterator();
						while (itr.hasNext()) {
							int mark = expression.mark();
							try {
								initSetting(itr.next(), expression, false);
							} finally {
								expression.endMark(mark);
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
				break;
			case Notification.REMOVE_MANY:
				if (isCompositionFeature(msg)) {
					IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
					try {
						// We want to release these even if still considered to be a member because this means
						// its parentage will be changing and it needs to be released for this to work.
						Iterator itr = ((List) msg.getOldValue()).iterator();
						while (itr.hasNext()) {
							int mark = expression.mark();
							try {
								releaseSetting(itr.next(), expression, false);
							} finally {
								expression.endMark(mark);
							}
						}
					} catch (IllegalStateException e) {
						JavaVEPlugin.log(e, Level.WARNING);
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
				break;					
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.MemberContainerProxyAdapter#isInnerMemberContainerFeature(org.eclipse.emf.common.notify.Notification)
	 */
	protected boolean isInnerMemberContainerFeature(Notification msg) {
		// Note: If any added here, need to add in initBeanProxy() too because it is hard-coded there.
		return msg.getFeatureID(BeanSubclassComposition.class) == JCMPackage.BEAN_SUBCLASS_COMPOSITION__METHODS;
	}

	protected List getInnerMemberContainerFeatures() {
		return Collections.singletonList(JCMPackageImpl.eINSTANCE.getBeanSubclassComposition_Methods());
	}
	/**
	 * Is this a composition feature. These are features that the composition needs 
	 * @param msg
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isCompositionFeature(Notification msg) {
		int fid = msg.getFeatureID(BeanSubclassComposition.class);
		return fid == JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS || fid == JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.MemberContainerProxyAdapter#adaptAllMemberContainerAdapters()
	 */
	protected void adaptAllMemberContainerAdapters() {
		if (getTarget() instanceof BeanSubclassComposition) {
			Iterator itr = ((BeanSubclassComposition) getTarget()).getMethods().iterator();
			while (itr.hasNext()) {
				addMemberContainerAdapter((MemberContainer) itr.next());
				
			}
		}
	}
	
	/**
	 * Initialize all of the bean proxies. This is used to start up the composition.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void initBeanProxy() {		
		boolean testValidity = false; // The first time through we don't test the validity. We try to instantiate them all.
		outer: while (true) {
			IExpression expression = proxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
			try {
				if (getTarget() instanceof BeanSubclassComposition) {
					TimerTests.basicTest.startStep("Init this"); //$NON-NLS-1$
					TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_STEP);
					TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_METHOD_STEP);
					initSetting(((BeanSubclassComposition) getTarget()).getThisPart(), expression, testValidity);
					TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_METHOD_STEP);
					TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_STEP);
					TimerTests.basicTest.stopStep("Init this"); //$NON-NLS-1$
					if (!expression.isValid()) {
						testValidity = true; // We will try again, but this time don't instantiate those that had an error.
						continue outer;
					}
				}

				// Next run the components.
				List components = ((BeanComposition) getTarget()).getComponents();
				for (int i = 0; i < components.size(); i++) {
					String step = "init#" + i; //$NON-NLS-1$
					TimerTests.basicTest.startStep(step);
					TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_STEP);
					TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_METHOD_STEP);
					initSetting(components.get(i), expression, testValidity);
					TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_METHOD_STEP);
					TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_STEP);
					TimerTests.basicTest.stopStep(step);
					if (!expression.isValid()) {
						testValidity = true; // We will try again, but this time don't instantiate those that had an error.
						continue outer;
					}
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

	protected void releaseBeanProxy(IExpression expression, boolean remove) {
		// Also release the this part since that is not a member or property.
		if (target instanceof BeanSubclassComposition) {
			releaseSetting(((BeanSubclassComposition) target).getThisPart(), expression, remove);
		}
		
		super.releaseBeanProxy(expression, remove);
		
		if (freeformHosts != null && !freeformHosts.isEmpty()) {
			Iterator iter = freeformHosts.values().iterator();
			while (iter.hasNext()) {
				((IFreeFormHost) iter.next()).dispose(expression);
				iter.remove();
			}
		}
	}

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
				if (!testValidity || !ib.hasInstantiationErrors()) {
					if (ib.isBeanProxyInstantiated())
						ib.releaseBeanProxy(expression);	// In case not already released. We will always reinstantiate. For most components you don't need to, but some require it, so we will do by default.
					ib.addToFreeForm(this);
					expression.createTry();
					ib.instantiateBeanProxy(expression);
					expression.createTryCatchClause(IInternalBeanProxyHost.BEAN_INSTANTIATION_EXCEPTION, false);
					expression.createTryEnd();
				} 
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.MemberContainerProxyAdapter#releaseSetting(java.lang.Object, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void releaseSetting(Object v, IExpression expression, boolean remove) {
		super.releaseSetting(v, expression, remove);
		if (!remove && v instanceof IJavaInstance) {
			IBeanProxyHost settingBean = (IBeanProxyHost) EcoreUtil.getExistingAdapter((Notifier) v, IBeanProxyHost.BEAN_PROXY_TYPE);
			if (settingBean != null)
				((IInternalBeanProxyHost) settingBean).removeFromFreeForm();
		}		
	}
		
	/**
	 * Get the freeform host for the given key. 
	 * @param key
	 * @return the freeform host for the key or <code>null</code> if key not in set.
	 * 
	 * @since 1.1.0
	 */
	public IFreeFormHost getFreeForm(Object key) {
		if (freeformHosts == null)
			return null;
		else
			return (IFreeFormHost) freeformHosts.get(key);
	}
	
	/**
	 * Add the freeform host. This is used by callers to add one when not there.
	 * @param key
	 * @param freeform
	 * 
	 * @since 1.1.0
	 */
	public void addFreeForm(Object key, IFreeFormHost freeform) {
		if (freeformHosts == null)
			freeformHosts = new HashMap();
		freeformHosts.put(key, freeform);
	}
}
