/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: JTabbedPaneProxyAdapter.java,v $
 *  $Revision: 1.10 $  $Date: 2005-06-22 21:05:25 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.ErrorNotifier;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;

/**
 * Create the bean proxy. We override because we use addtab(String title, Icon icon, Component component, String tip, int index) or insertTab(String
 * title, Icon icon, Component component, String tip, int index) to add components as tabs (pages).
 * <p>
 * This also keeps a List of those who are interested in the page selection of the JTabbedPane. The listener must implement IJTabbedPanePageListener
 * and notifies the listeners when a page is selected.
 * 
 * @since 1.1.0
 */
public class JTabbedPaneProxyAdapter extends ComponentProxyAdapter {

	protected EReference sfTabs, sfJTabComponent, sfTitle, sfIcon, sfName;

	/**
	 * Construct with domain.
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public JTabbedPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfTabs = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABBEDPANE_TABS);
		sfJTabComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_COMPONENT);
		sfTitle = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_TITLE);
		sfIcon = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_ICON);
		sfName = JavaInstantiation.getReference(rset, JFCConstants.SF_COMPONENT_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean,
	 *      org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void applied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sfTabs) {
			if (isTouch)
				return; // Don't want to apply if all we did was touch.
		}
		super.applied(feature, value, index, isTouch, expression, testValidity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applySetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sfTabs)
			addTab((EObject) value, index, expression);
		else
			super.applySetting(feature, value, index, expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#cancelSetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		if (feature == sfTabs) {
			removeTab((EObject) oldValue, expression);
			removeAdapters((Notifier) oldValue);
		} else
			super.cancelSetting(feature, oldValue, index, expression);
	}

	/**
	 * Return the true component added for the tab at the requested position. It may not be the one associated with the tab because it may not of been
	 * instantiated, in which case we would of created a dummy one to use.
	 * 
	 * @param position
	 * @return the component proxy for the first instantiated component after the given index, or -1 if not found.
	 * 
	 * @since 1.1.0
	 */
	protected IProxy getComponentProxyAt(int position) {
		List tabs = (List) ((EObject) getTarget()).eGet(sfTabs);
		for (int i = position; i < tabs.size(); i++) {
			IProxy component = getComponentProxyFor((EObject) tabs.get(i));
			if (component != null)
				return component;
		}

		return null;
	}

	/**
	 * Return the bean proxy component that was used for the specified tab. It may be different if the specified component for the tab could not be
	 * instantiated.
	 * 
	 * @param jtab
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IBeanProxy getComponentProxyFor(EObject jtab) {
		JTabAdapter adapter = getJTabAdapter(jtab);
		if (adapter != null)
			return adapter.getComponentProxy();

		// The adapter doesn't have a component, so some problem, so we go to the end instead.
		return null;
	}

	/*
	 * Add the tab at the right place @param jtab @param position
	 * 
	 * @since 1.1.0
	 */
	private void addTab(EObject jtab, int position, IExpression expression) {
		// Add a listener to the tab component for when the tab fields changes, if not already there.
		JTabAdapter tabAdapter = getJTabAdapter(jtab);
		if (tabAdapter == null) {
			tabAdapter = new JTabAdapter();
			jtab.eAdapters().add(tabAdapter);
		}

		tabAdapter.clearError(sfTabs); 
		tabAdapter.clearError(sfTitle);
		tabAdapter.clearError(sfIcon);
		tabAdapter.clearError(sfJTabComponent);

		expression.createTry();
		try {
			IProxy titleProxy = null;
			boolean titleIsSet = false;
			// Get the value of the constraint attribute of the component.
			if (jtab.eIsSet(sfTitle)) {
				IJavaInstance title = (IJavaInstance) jtab.eGet(sfTitle);
				titleIsSet = true;
				if (title != null) {
					expression.createTry();
					titleProxy = instantiateSettingBean(getSettingBeanProxyHost(title), expression, sfTitle, title, tabAdapter);
					if (titleProxy != null && titleProxy.isExpressionProxy()) {
						// We don't want to just terminate here, we will just change the assignment to be null title.
						expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
						expression.createProxyReassignmentExpression(ForExpression.ROOTEXPRESSION, (ExpressionProxy) titleProxy);
						expression.createNull(ForExpression.ASSIGNMENT_RIGHT);
					}
					expression.createTryEnd();
				}
			}

			IProxy iconProxy = null;
			if (jtab.eIsSet(sfIcon)) {
				IJavaInstance icon = (IJavaInstance) jtab.eGet(sfIcon);
				expression.createTry();
				iconProxy = instantiateSettingBean(getSettingBeanProxyHost(icon), expression, sfIcon, icon, tabAdapter);
				if (iconProxy != null && iconProxy.isExpressionProxy()) {
					// We don't want to just terminate here, we will just change the assignment to be null icon.
					expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
					expression.createProxyReassignmentExpression(ForExpression.ROOTEXPRESSION, (ExpressionProxy) iconProxy);
					expression.createNull(ForExpression.ASSIGNMENT_RIGHT);
				}
				expression.createTryEnd();
			}

			// The component to actually add is within the jtab component too.
			IJavaInstance component = null;
			IInternalBeanProxyHost componentProxyHost = null;
			IProxy componentProxy = null;
			if (jtab.eIsSet(sfJTabComponent)) {
				component = (IJavaInstance) jtab.eGet(sfJTabComponent);
				componentProxyHost = getSettingBeanProxyHost(component);
				expression.createTry();
				componentProxy = instantiateSettingBean(componentProxyHost, expression, sfJTabComponent, component, null);
				if (componentProxy != null && componentProxy.isExpressionProxy()) {
					// We don't want to just terminate here, we will just change the assignment to be null component.
					expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
					expression.createProxyReassignmentExpression(ForExpression.ROOTEXPRESSION, (ExpressionProxy) componentProxy);
					expression.createNull(ForExpression.ASSIGNMENT_RIGHT);
				}
				expression.createTryEnd();
			}

			if (!titleIsSet && (iconProxy != null || componentProxy == null))
				titleIsSet = true; // invalid to have no title and an icon, or no title and no componentproxy. Either way we say we have a title, but the
			// title is null.

			if (!titleIsSet) {
				// Title is not set, we need to listen for component names changes so we can refresh the tab with the new name.
				if (cnameAdapter == null)
					cnameAdapter = new ComponentNameAdapter();
				Adapter a = EcoreUtil.getExistingAdapter(component, cnameAdapter);
				if (a == null)
					component.eAdapters().add(cnameAdapter);
			} else {
				// No longer a default title (or maybe never was), so remove the component name adapter.
				if (cnameAdapter != null)
					component.eAdapters().remove(cnameAdapter);
			}

			IProxy beforeBeanProxy; // The beanproxy to go before, if any.
			if (position != Notification.NO_INDEX)
				beforeBeanProxy = getComponentProxyAt(position + 1); // Need to do +1 because we (componentBeanProxy) are already at that position in the
			// EMF list. So we want to go before next guy.
			else
				beforeBeanProxy = null;

			boolean poofed = componentProxy == null;
			if (titleIsSet) {
				componentProxy = BeanAwtUtilities.invoke_JTabbedPane_insertTab(getProxy(), titleProxy, iconProxy, componentProxy, beforeBeanProxy,
						expression);
			} else {
				BeanAwtUtilities.invoke_JTabbedPane_insertTab_Default(getProxy(), componentProxy, beforeBeanProxy, expression);
			}

			expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
			ExpressionProxy applyException = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
			final JTabAdapter ftabadapter = tabAdapter;
			applyException.addProxyListener(new ExpressionProxy.ProxyAdapter() {

				public void proxyResolved(ProxyEvent event) {
					processPropertyError((Throwable) event.getProxy(), sfTabs, null, ftabadapter);
				}
			});
			tabAdapter.setComponentProxy(componentProxy, poofed);
			revalidateBeanProxy();
		} finally {
			expression.createTryEnd();
		}
	}

	/**
	 * Returns the currently selected index for this tabbed pane as an int. It will be only for ones we've defined. Any others (such as from a
	 * superclass) will not be selectable and -1 will be returned in that case. If one we have not defined is selected, it will return -1.
	 * 
	 * @return the index of the selected tab, or -1 if none selected or if none of our defined ones are selected.
	 * 
	 * @since 1.1.0
	 */
	public int getSelectedIndex() {
		if (isBeanProxyInstantiated()) {
			IBeanProxy selectedComponentProxy = BeanAwtUtilities.invoke_tab_getSelectedComponent(getBeanProxy());
			if (selectedComponentProxy != null) {
				List tabs = (List) ((EObject) getTarget()).eGet(sfTabs);
				for (int i = 0; i < tabs.size(); i++)
					if (selectedComponentProxy == getComponentProxyFor((EObject) tabs.get(i)))
						return i;
			}
		}

		return -1;
	}

	/**
	 * Sets the icon for the page at the tab with the given component
	 * 
	 * @param componentProxy
	 * @param icon
	 * @param jtab
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void setIconAt(IProxy componentProxy, IJavaObjectInstance icon, final EObject jtab, IExpression expression) {
		JTabAdapter jtabadapter = getJTabAdapter(jtab);
		jtabadapter.clearError(sfIcon);
		expression.createTry();
		IProxy iconProxy = instantiateSettingBean(getSettingBeanProxyHost(icon), expression, sfIcon, icon, jtabadapter);
		BeanAwtUtilities.invoke_JTabbedPane_setIconAt(getProxy(), componentProxy, iconProxy, expression);
		expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
		expression.createTryEnd();
		revalidateBeanProxy();
	}

	/**
	 * Sets the selected component for this tabbedpane. This is public and is used by the edit parts to bring a selected page to the front so it can
	 * be viewed and edited. PageModel is the actual component, not the tab.
	 * 
	 * @param componentOnTab
	 * 
	 * @since 1.1.0
	 */
	public void setSelectedComponent(IJavaObjectInstance componentOnTab) {
		if (componentOnTab != null && isBeanProxyInstantiated()) {
			JTabAdapter a = getJTabAdapter(getJTab(componentOnTab));
			BeanAwtUtilities.invoke_tab_setSelectedComponent(getBeanProxy(), a.getComponentProxy());
			revalidateBeanProxy();
		}
	}

	/**
	 * Get the jtab adapter for the given tab.
	 * @param jtab
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected JTabAdapter getJTabAdapter(EObject jtab) {
		return (JTabAdapter) EcoreUtil.getExistingAdapter(jtab, this);
	}

	/**
	 * Set the title text for the given tab.
	 * 
	 * @param componentProxy
	 * @param title
	 * @param jtab
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void setTitleAt(IProxy componentProxy, IJavaObjectInstance title, EObject jtab, IExpression expression) {
		JTabAdapter jtabadapter = getJTabAdapter(jtab);
		jtabadapter.clearError(sfTitle);
		expression.createTry();
		IProxy titleProxy = instantiateSettingBean(getSettingBeanProxyHost(title), expression, sfTabs, title, jtabadapter);
		BeanAwtUtilities.invoke_JTabbedPane_setTitle(getProxy(), componentProxy, titleProxy, expression);
		expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
		expression.createTryEnd();
		revalidateBeanProxy();
	}

	/*
	 * Remove tab component @param jtab @param expression
	 * 
	 * @since 1.1.0
	 */
	private void removeTab(EObject jtab, IExpression expression) {
		JTabAdapter a = getJTabAdapter(jtab);
		if (a != null) {
			a.clearError(sfTabs);
			a.clearError(sfTitle);
			a.clearError(sfIcon);
			a.clearError(sfJTabComponent);
			BeanAwtUtilities.invoke_removeComponent(getProxy(), a.getComponentProxy(), expression);
		}
		revalidateBeanProxy();
	}

	protected void primReleaseBeanProxy(IExpression expression) {
		// Also remove the adapters on any of the jtab. This is because if changes are
		// made during the non-instantiated time, we won't see them because the notifications only come through
		// while bean is instantiated.
		removeAllJTabAdapters();
		super.primReleaseBeanProxy(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#removingAdapter()
	 */
	protected void removingAdapter() {
		removeAllJTabAdapters();
		super.removingAdapter();
	}

	/*
	 * Remove all of the tab adapters.
	 * 
	 * @since 1.1.0
	 */
	private void removeAllJTabAdapters() {
		try {
			if (getEObject().eIsSet(sfTabs)) {
				Iterator itr = ((List) getEObject().eGet(sfTabs)).iterator();
				while (itr.hasNext()) {
					removeAdapters((Notifier) itr.next());
				}
			}
		} catch (IllegalArgumentException e) {
			// This can occur because the class has gone undefined and so the feature is
			// no longer valid.
		}
	}

	/*
	 * Remove the JTab adapter and the ComponentNameAdapter.
	 */
	private void removeAdapters(Notifier jtab) {
		Adapter jtAdapter = EcoreUtil.getExistingAdapter(jtab, this);
		if (jtAdapter != null)
			jtab.eAdapters().remove(jtAdapter);
		if (cnameAdapter != null) {
			jtab = ((EObject) jtab).eIsSet(sfJTabComponent) ? (Notifier) ((EObject) jtab).eGet(sfJTabComponent) : null;
			if (jtab != null) {
				jtab.eAdapters().remove(cnameAdapter);
			}
		}
	}

	/**
	 * We have a default title (i.e. we are using the add that takes no title, so it uses component.getName()).
	 * 
	 * @param componentProxy
	 * @param jtab
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void setDefaultTitle(IProxy componentProxy, EObject jtab, IExpression expression) {
		if (componentProxy != null) {
			getJTabAdapter(jtab).clearError(sfTitle);
			BeanAwtUtilities.invoke_JTabbedPane_setDefaultTitle(getProxy(), componentProxy, expression);
			revalidateBeanProxy();
		}
	}

	/*
	 * Get the jtab that is referencing this component.
	 */
	private EObject getJTab(IJavaInstance component) {
		return InverseMaintenanceAdapter.getIntermediateReference(getEObject(), sfTabs, sfJTabComponent, component);
	}

	private ComponentNameAdapter cnameAdapter;

	/*
	 * An internal adapter added to the JTabComponent to listen for changes to JTabComponent's features so that we can reapply if necessary. It also
	 * holds the actual component that was added. This is necessary so that when we add an empty entry, we have unique component in it.
	 */
	private class JTabAdapter extends ErrorNotifier.ErrorNotifierAdapter {

		protected IBeanProxy componentProxy; // The proxy for the component in the tab.

		protected boolean poofedUpProxy; // Was the above proxy a poofed up, if it was, then it can released when this adapter is removed.

		/**
		 * Construct the adapter.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public JTabAdapter() {
		}

		/**
		 * Get the component proxy.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public IBeanProxy getComponentProxy() {
			return componentProxy;
		}

		/**
		 * Set the component proxy.
		 * 
		 * @param componentProxy
		 * @param poofed
		 * 
		 * @since 1.1.0
		 */
		public void setComponentProxy(IProxy componentProxy, boolean poofed) {
			if (this.componentProxy != null && poofedUpProxy) {
				getBeanProxyFactory().releaseProxy(this.componentProxy);
				this.componentProxy = null;
			}

			if (componentProxy.isBeanProxy())
				this.componentProxy = (IBeanProxy) componentProxy;
			else {
				((ExpressionProxy) componentProxy).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						JTabAdapter.this.componentProxy = event.getProxy();
					}
				});
			}
			this.poofedUpProxy = poofed;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
		 */
		public boolean isAdapterForType(Object type) {
			return type == JTabbedPaneProxyAdapter.this || super.isAdapterForType(type);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
		 */
		public void notifyChanged(Notification msg) {
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				if (poofedUpProxy)
					componentProxy.getProxyFactoryRegistry().releaseProxy(componentProxy);
				return;
			}

			EStructuralFeature feature = (EStructuralFeature) msg.getFeature();
			if (feature != sfTitle && feature != sfIcon)
				return; // Not one of interest. (TabTitle we don't care about, and we never get a changed component, that would be a remove/add
						// instead).
			switch (msg.getEventType()) {
				case Notification.SET:
					if (isBeanProxyInstantiated()) {
						if (!msg.isTouch()) {
							if (!CDEUtilities.isUnset(msg)) {
								// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this
								// one notification.
								IExpression expression = getBeanProxyFactory().createExpression();
								try {
									if (msg.getOldValue() != null)
										releaseSetting(msg.getOldValue(), expression);
									if (feature == sfIcon)
										setIconAt(getComponentProxy(), (IJavaObjectInstance) msg.getNewValue(), (EObject) getTarget(), expression);
									else if (feature == sfTitle)
										setTitleAt(getComponentProxy(), (IJavaObjectInstance) msg.getNewValue(), (EObject) getTarget(), expression);
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
										processPropertyError(e, feature, getTarget());
									} catch (NoExpressionValueException e) {
										// Shouldn't occur.
										JavaVEPlugin.log(e, Level.WARNING);
									}
								}
								break;
							}
						} else
							break;
					} else
						break;
				// Else flow into unset
				case Notification.UNSET:
					if (isBeanProxyInstantiated()) {
						// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one
						// notification.
						IExpression expression = getBeanProxyFactory().createExpression();
						try {
							if (msg.getOldValue() != null)
								releaseSetting(msg.getOldValue(), expression);
							if (feature == sfIcon)
								setIconAt(getComponentProxy(), null, (EObject) getTarget(), expression);
							else if (feature == sfTitle)
								setDefaultTitle(getComponentProxy(), (EObject) getTarget(), expression); // Unsetting the title is same as default
																										 // title.
						} finally {
							try {
								if (expression.isValid())
									expression.invokeExpression();
							} catch (IllegalStateException e) {
								// Shouldn't occur. Should be taken care of in applied.
								JavaVEPlugin.log(e, Level.WARNING);
							} catch (ThrowableProxy e) {
								processPropertyError(e, feature, getTarget());
							} catch (NoExpressionValueException e) {
								// Shouldn't occur. Should be taken care of in applied.
								JavaVEPlugin.log(e, Level.WARNING);
							}
						}
					}
					break;

				case IInternalBeanProxyHost.NOTIFICATION_LIFECYCLE:
					if (isBeanProxyInstantiated()) {
						try {
							IInternalBeanProxyHost.NotificationLifeCycle notification = (IInternalBeanProxyHost.NotificationLifeCycle) msg;
							EStructuralFeature sf = (EStructuralFeature) notification.getFeature();
							if (sf == sfTitle) {
								if (notification.isPostReinstantiation())
									setTitleAt(getComponentProxy(), (IJavaObjectInstance) notification.getNewValue(), (EObject) notification
											.getNotifier(), notification.getExpression()); // Constraint was re-instantiated.
							} else if (sf == sfIcon) {
								if (notification.isPostReinstantiation())
									setIconAt(getComponentProxy(), (IJavaObjectInstance) notification.getNewValue(), (EObject) notification
											.getNotifier(), notification.getExpression()); // Constraint was re-instantiated.
							} else if (sf == sfJTabComponent) {
								// It is a component life cycle change.
								if (notification.isPrerelease()) {
									// Remove the bean from the container because it is about to go away. We need to do this prerelease because after
									// release
									// we no longer have the old component proxy available to remove. It would stay in the container and we couldn't
									// touch it.
									removeTab((EObject) getTarget(), notification.getExpression());
								} else {
									// Add the new bean back into the container. (This adds the entire tabComponent back.
									addTab((EObject) getTarget(), getJTabPosition((EObject) getTarget()), notification.getExpression());
								}
							}
						} catch (ClassCastException e) {
							// Ignore this. It means someone sent their own kind of notification but used out notification type.
						}
					}
					break;
			}
		}

	}

	/*
	 * Get the JTab position. This is used when we have the JTab component but we don't know its position.
	 */
	private int getJTabPosition(EObject jtab) {
		return ((List) getEObject().eGet(sfTabs)).indexOf(jtab);
	}

	/*
	 * An internal adapter added to the Component to listen for changes to the <name> property so that we can reapply if necessary to the constraint
	 * field in the live bean.
	 * 
	 * We have one instance per JTabbedPaneProxyAdapter instance. Because of this the target must be ingored because it could be on any one of the
	 * tabbedPane components. Use getNotifier instead to determine which constraint component. We will never have an null component when we are using
	 * a ComponentNameAdapter (since the adapter was added to the component!).
	 */
	private class ComponentNameAdapter extends AdapterImpl {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
		 * 
		 * So that we find only ours, we use "this" as the key.
		 */
		public boolean isAdapterForType(Object type) {
			return type == this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
		 * 
		 * We return null to just enforce the fact that we are on more than one target and so the target should be ignored.
		 */
		public Notifier getTarget() {
			return null;
		}

		public void notifyChanged(Notification msg) {
			switch (msg.getEventType()) {
				case Notification.SET:
				case Notification.UNSET:
					if (msg.getFeature() == sfName && isBeanProxyInstantiated()) {
						if (!msg.isTouch()) {
							IJavaInstance component = (IJavaInstance) msg.getNotifier();
							// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one
							// notification.
							IExpression expression = getBeanProxyFactory().createExpression();
							try {
								setDefaultTitle(BeanProxyUtilities.getBeanProxy(component), getJTab(component), expression);
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
					}
					break;

				case IInternalBeanProxyHost.NOTIFICATION_LIFECYCLE:
					if (msg.getFeature() == sfName && isBeanProxyInstantiated()) {
						try {
							ReinstantiateBeanProxyNotification notification = (ReinstantiateBeanProxyNotification) msg;
							if (notification.isPostReinstantiation()) {
								IJavaInstance component = (IJavaInstance) msg.getNotifier();
								setDefaultTitle(BeanProxyUtilities.getBeanProxy(component), getJTab(component), notification.getExpression());
							}
						} catch (ClassCastException e) {
							// Ignore this. It means someone sent their own kind of notification but used out notification type.
						}
					}
					break;

			}
		}
	}

}