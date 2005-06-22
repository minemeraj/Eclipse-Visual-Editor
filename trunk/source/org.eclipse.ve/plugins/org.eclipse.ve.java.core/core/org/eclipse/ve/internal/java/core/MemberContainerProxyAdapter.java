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
 *  $Revision: 1.4 $  $Date: 2005-06-22 21:05:23 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

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
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification msg) {
		switch (msg.getEventType()) {
			case Notification.REMOVING_ADAPTER:
				releaseBeanProxy(true);
				break;
			case Notification.ADD:
			case Notification.SET:
				if (!CDEUtilities.isUnset(msg)) {
					if (isMemberContainmentFeature(msg)) {
						if (!msg.isTouch() && msg.getOldValue() != null) {
							synchronized (pendingReleases) {
								pendingReleases.add(msg.getOldValue());
							}
							controller.execAtEndOfTransaction(releaseRunnable, releaseRunnable);
						}
					} else if (isInnerMemberContainerFeature(msg)) {
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
	 * it is still considered contained and we won't release the setting.
	 * 
	 * @param value
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isMemberContained(IJavaInstance value) {
		EStructuralFeature containerFeature = value.eContainingFeature();
		return containerFeature == JCMPackage.eINSTANCE.getMemberContainer_Members() || containerFeature == JCMPackage.eINSTANCE.getMemberContainer_Properties();
	}
	
	/**
	 * Answers whether the feature of the msg is a Member or Properties feature.
	 * @param msg
	 * @return <code>true</code> if either Members or Properties feature.
	 * 
	 * @since 1.1.0
	 */
	protected boolean isMemberContainmentFeature(Notification msg) {
		return msg.getFeatureID(MemberContainer.class) == JCMPackage.MEMBER_CONTAINER__MEMBERS || msg.getFeatureID(MemberContainer.class) == JCMPackage.MEMBER_CONTAINER__PROPERTIES;
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
				Platform.run(new ISafeRunnable() {

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
}
