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
 *  $RCSfile: ContainerProxyAdapter.java,v $
 *  $Revision: 1.24 $  $Date: 2005-11-01 20:41:23 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

import org.eclipse.ve.internal.cde.core.ErrorNotifier;
import org.eclipse.ve.internal.cde.core.ModelChangeController;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
 

/**
 * Proxy Adapter for java.awt.Container.
 * @since 1.1.0
 */
public class ContainerProxyAdapter extends ComponentProxyAdapter {

	protected EReference sfConstraintConstraint, sfContainerComponents, sfConstraintComponent, sfName, sfLayout;

	/**
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public ContainerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		sfContainerComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
		sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sfLayout = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_LAYOUT);
		sfName = JavaInstantiation.getReference(rset, JFCConstants.SF_COMPONENT_NAME);		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void applied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sfContainerComponents) { 
			if (isTouch)
				return;	// Don't want to apply if all we did was touch.
		}
		super.applied(feature, value, index, isTouch, expression, testValidity);
	}
	
	/*
	 * An internal adapter added to the ConstraintComponent to listen for changes to the 
	 * constraints so that we can reapply if necessary.
	 * 
	 * It is also an error notifier so that the EditPart parent can retrieve this notifier
	 * and give it to the child editpart. That way any errors on the component constraint
	 * will be merged into any child errors and will show on the children in the viewers.
	 * This makes more sense from a user standpoint.
	 */
	private class ComponentConstraintAdapter extends ErrorNotifier.ErrorNotifierAdapter {

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
		 * 
		 * So that we find only ours, we use "ContainerProxyAdapter.this" as the key.
		 */
		public boolean isAdapterForType(Object type) {
			return type == ContainerProxyAdapter.this || super.isAdapterForType(type);
		}
		
		public void notifyChanged(Notification msg) {
			switch (msg.getEventType()) {
				case Notification.SET:
				case Notification.UNSET:
					if (isBeanProxyInstantiated()) {
						if (msg.getFeature() == sfConstraintConstraint) {
							// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
							IExpression expression = getBeanProxyFactory().createExpression();
							try {
								if (!msg.isTouch() && msg.getOldValue() != null)
									releaseSetting(msg.getOldValue(), expression);
								layoutChanged(expression);	// This could be coming in from snippet parsing and so we may have incomplete state, so farm off to the end of transaction.
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
					if (isBeanProxyInstantiated()) {
						try {
							IInternalBeanProxyHost.NotificationLifeCycle notification = (IInternalBeanProxyHost.NotificationLifeCycle) msg;
							EStructuralFeature sf = (EStructuralFeature) notification.getFeature();
							if (sf == sfConstraintConstraint) {
								if (notification.isPostReinstantiation()) {
									// Since this is a reinstantiate we can sure it is ok to do now.
									changeConstraint((EObject) notification.getNotifier(), notification.getExpression());	// Constraint was re-instantiated.
								}
							} else if (sf == sfConstraintComponent) {
								// It is a component life cycle change.
								if (notification.isPrerelease()) {
									// Remove the bean from the container because it is about to go away. We need to do this prerelease because after release 
									// we no longer have the old component proxy available to remove. It would stay in the container and we couldn't touch it.
									removeComponent((EObject) notification.getNotifier(), true, notification.getExpression());
								} else {
									// Add the new bean back into the container.
									addComponentWithConstraint((EObject) notification.getNotifier(), getConstraintComponentPosition((EObject) notification.getNotifier()), notification
											.getExpression());
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
	 * Get the constraint component position. This is used when we have
	 * the constraint component but we don't know its position.
	 */
	private int getConstraintComponentPosition(EObject cc) {
		return ((List) getEObject().eGet(sfContainerComponents)).indexOf(cc);
	}

	/*
	 * An internal adapter added to the Component to listen for changes to the 
	 * <name> property so that we can reapply if necessary to the constraint field in the live bean.
	 * 
	 * We have one instance per ContainerProxyAdapter instance. Because of this the target must be ingored because
	 * it could be on any one of the constraint components. Use getNotifier instead to determine which constraint component.	  
	 */
	private class ComponentNameAdapter extends AdapterImpl {
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
		 * 
		 * So that we find only ours, we use "this" as the key.
		 */
		public boolean isAdapterForType(Object type) {
			return type == this;
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#getTarget()
		 * 
		 * We return null to just enforce the fact that we are on more than one target
		 * and so the target should be ignored.
		 */
		public Notifier getTarget() {
			return null;
		}

		public void notifyChanged(Notification msg) {
			switch (msg.getEventType()) {
				case Notification.SET:
				case Notification.UNSET:
					if (isBeanProxyInstantiated()) {
						if (!msg.isTouch()) {
							if (msg.getFeature() == sfName) {
								// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
								IExpression expression = getBeanProxyFactory().createExpression();
								try {
									layoutChanged(expression);	// This could be coming in from snippet parsing and so we may have incomplete state, so farm off to the end of transaction.
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
					}
					break;
					
				case IInternalBeanProxyHost.NOTIFICATION_LIFECYCLE:
					if (isBeanProxyInstantiated()) {
						try {
							ReinstantiateBeanProxyNotification notification = (ReinstantiateBeanProxyNotification) msg;
							if (notification.isPostReinstantiation() && notification.getFeature() == sfName) {
								EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference(ContainerProxyAdapter.this
										.getEObject(), sfContainerComponents, sfConstraintComponent, (Notifier) msg.getNotifier());
								changeConstraint(constraintComponent, notification.getExpression());	// Since this is a reinstantiate we can sure it is ok to do now.
							}
						} catch (ClassCastException e) {
							// Ignore this. It means someone sent their own kind of notification but used out notification type.
						}
					}
					break;
					
			}
		}
	}

	private ComponentNameAdapter cnameAdapter;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applySetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sfContainerComponents)
			addComponentWithConstraint((EObject) value, index, expression);
		else if (!inInstantiation() && feature == sfLayout) {
			layoutChanged(expression);
			super.applySetting(feature, value, index, expression);
		} else
			super.applySetting(feature, value, index, expression);
	}
	
	protected boolean layoutChangePending;	// Is there a layout change in progress.
	
	/**
	 * We are changing layout. This isn't handled very well by the live container because if we change layout, all of the components
	 * now have invalid constraints. We have to assume that by the end of the transaction the constraints will have been changed
	 * to the correct values. So we will at this point simply remove all of the components from the bean so that we get no errors.
	 * And we will queue up an exec for the end of the transaction to add all of the components back in with their new constraints. 
	 *
	 * <p>
	 * <b>Note:</b> This will also be called if any constraints are changed. Sometimes, especially on undo, the constraints may be changed to an
	 * invalid value before being changed to a valid value. Or they may be changed to new valid value for a new layout manager, but that new
	 * layout manager has not yet been applied. So to handle these various cases we will treat it as a layout change. 

	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void layoutChanged(IExpression expression) {

		if (!layoutChangePending) {
			// Remove and then do a restoreVisibility on all the components. They will be immediately removed so that should be ok.
			// They will then be added back if they are still around at the end and the appropriate layout visibility will be reapplied.
			Iterator components = ((List) getJavaObject().eGet(sfContainerComponents)).iterator();
			while (components.hasNext()) {
				EObject cc = (EObject) components.next();
				IJavaInstance component = (IJavaInstance) cc.eGet(sfConstraintComponent);
				try {
					ComponentProxyAdapter componentProxyHost = (ComponentProxyAdapter) getSettingBeanProxyHost(component);
					if (componentProxyHost != null && componentProxyHost.isBeanProxyInstantiated()) {
						BeanAwtUtilities.invoke_removeComponent(getProxy(), componentProxyHost.getProxy(), expression);
						componentProxyHost.restoreVisibility(expression);
					}
				} catch (ClassCastException e) {
					// Slight possibility it wasn't a ComponentProxyAdapter due to some error like undefined class.
				}
			}
			// Should never be excluding because we shouldn't be here during initialization.
			layoutChangePending = getModelChangeController().execAtEndOfTransaction(new Runnable() {

				public void run() {
					processPendingLayoutChange();
				}
			}, ModelChangeController.createHashKey(this, "LAYOUT CHANGE"), //$NON-NLS-1$
					new Object[] { ModelChangeController.SETUP_PHASE, ModelChangeController.INIT_VIEWERS_PHASE});
		}
	}
	
	/**
	 * Process the pending layout change ahead of time. It is called
	 * at the end of transaction if layout change is pending.
	 * <p> It may be called by subclasses, but this is tricky to call because it may
	 * cause extra work if called outside of end of 
	 * transaction. If called while transaction is still in progress,
	 * we could get inconsistent results. So it should be
	 * called only at end of transaction.
	 * <p>
	 * It is used by subclasses (JLayeredPane actually) to get the
	 * layout to occur ahead of time. But it will still be from within
	 * the end of transaction processing. 
	 * 
	 * @since 1.1.0
	 */
	protected void processPendingLayoutChange() {
    	if (layoutChangePending) {
			layoutChangePending = false;
			if (isBeanProxyInstantiated()) {
				// Now we walk through them all and put them back in.
				IExpression expression = getBeanProxyFactory().createExpression();
				try {
					List components = (List) getEObject().eGet(sfContainerComponents);
					int l = components.size();
					for (int i = 0; i < l && expression.isValid(); i++) {
						EObject cc = (EObject) components.get(i);
						if (!hasErrorsOfKeyObject(sfContainerComponents, cc)) {
							int mark = expression.mark();
							try {
								addComponentWithConstraint(cc, i, expression);
							} finally {
								expression.endMark(mark);
							}
						}
					}
				} finally {
					try {
						if (expression.isValid())
							expression.invokeExpression();
						else
							expression.close();
					} catch (IllegalStateException e) {
						// Shouldn't occur. 
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (ThrowableProxy e) {
						// Shouldn't occur. 
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (NoExpressionValueException e) {
						// Shouldn't occur. 
						JavaVEPlugin.log(e, Level.WARNING);
					}
				}
			} 
		}		            
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#cancelSetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		if (feature == sfContainerComponents) {
			removeComponent((EObject) oldValue, false, expression);
			removeAdapters((Notifier) oldValue);	// Going away, so remove the adapter too.
		} else if (!inInstantiation() && feature == sfLayout) {
			layoutChanged(expression);
			super.cancelSetting(feature, oldValue, index, expression);
		} else
			super.cancelSetting(feature, oldValue, index, expression);
	}

	/*
	 * Change the constraint. This should only be called on reinstantiation of a constraint. Any other change
	 * constraint should go through layout change pending.
	 * @param constraintComponent
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	private void changeConstraint(EObject constraintComponent, IExpression expression) {
		if (!layoutChangePending) {
			clearError(sfConstraintConstraint, constraintComponent);	// We are doing special for CC. The feature in error is the constraint, but the value is the CC so that it is easier to know a CC error.
			IJavaInstance constraintAttributeValue = null;
			boolean constraintIsSet = false;
			// Get the value of the constraint attribute of the component.
			if (constraintComponent.eIsSet(sfConstraintConstraint)) {
				constraintAttributeValue = (IJavaInstance) constraintComponent.eGet(sfConstraintConstraint);
				constraintIsSet = true;
			}

			// The component to change is within the ConstraintComponent too.
			IJavaInstance component = (IJavaInstance) constraintComponent.eGet(sfConstraintComponent);
			IInternalBeanProxyHost componentProxyHost = getSettingBeanProxyHost(component);
			IProxy componentProxy = componentProxyHost.getProxy(); // It is assumed to already be instantiated or there was an error for a change constraint request.
			if (componentProxy == null)
				return; // It failed creation, don't go any further. 

			expression.createTry();
			try {
				IProxy constraintBeanProxy = null;
				if (!constraintIsSet) {
					// Changed to no constraint. Make sure we have a component name adapter.
					if (cnameAdapter == null)
						cnameAdapter = new ComponentNameAdapter();
					Adapter a = EcoreUtil.getExistingAdapter(component, cnameAdapter);
					if (a == null)
						component.eAdapters().add(cnameAdapter);
				} else {
					// No longer a default constraint (or maybe never), so remove the component name adapter, if it has one.
					if (cnameAdapter != null)
						component.eAdapters().remove(cnameAdapter);

					if (constraintAttributeValue != null) {
						IInternalBeanProxyHost constraintHost = getSettingBeanProxyHost(constraintAttributeValue);
						constraintBeanProxy = instantiateSettingBean(constraintHost, expression, sfConstraintConstraint, constraintComponent); // Note: Using feature constraint with value CC.
						if (constraintHost == null)
							return; // It failed creation, don't go any further.
					}
				}

				BeanAwtUtilities.invoke_changeConstraint(getProxy(), componentProxy, constraintBeanProxy, !constraintIsSet, expression);
				expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
			} finally {
				expression.createTryEnd();
			}
			
			revalidateBeanProxy();
		}
	}

	/*
	 * Add constraint component with a given constraint value. This can be called multiple times for the constraintComponent, it
	 * will simply add it back in at the right place. This is used from the original add and from reinstanitation of the component itself.
	 * 
	 * @param constraintComponent
	 * @param constraintAttributeValue
	 * @param index
	 * @param constraintIsSet
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	private void addComponentWithConstraint(EObject constraintComponent, int index, IExpression expression) {
		if (!layoutChangePending) {
			// Add a listener to the constraint component for when the constraint changes if not already there.

			ComponentConstraintAdapter ccAdapter = (ComponentConstraintAdapter) EcoreUtil.getExistingAdapter(constraintComponent, this);
			if (ccAdapter == null) {
				ccAdapter = new ComponentConstraintAdapter();
				constraintComponent.eAdapters().add(ccAdapter);
			}
			ccAdapter.clearError(sfConstraintComponent);
			ccAdapter.clearError(sfConstraintConstraint);
			ccAdapter.clearError(sfContainerComponents);


			IJavaInstance constraintAttributeValue = null;
			boolean constraintIsSet = false;
			// Get the value of the constraint attribute of the component.
			if (constraintComponent.eIsSet(sfConstraintConstraint)) {
				constraintAttributeValue = (IJavaInstance) constraintComponent.eGet(sfConstraintConstraint);
				constraintIsSet = true;
			}
			
			expression.createTry();

			try {
				// The component to actually add is within the ConstraintComponent too.
				IJavaInstance component = (IJavaInstance) constraintComponent.eGet(sfConstraintComponent);
				IInternalBeanProxyHost componentProxyHost = getSettingBeanProxyHost(component);
				IProxy componentProxy = instantiateSettingBean(componentProxyHost, expression, sfConstraintComponent, component, null);
				if (componentProxy == null)
					return; // It failed creation, don't go any further. 

				IProxy constraintBeanProxy = null;
				if (!constraintIsSet) {
					if (cnameAdapter == null)
						cnameAdapter = new ComponentNameAdapter();
					if (EcoreUtil.getExistingAdapter(component, cnameAdapter) == null)
						component.eAdapters().add(cnameAdapter);
				} else {
					// No longer a default constraint (or maybe never), so remove the component name adapter.
					if (cnameAdapter != null)
						component.eAdapters().remove(cnameAdapter);

					if (constraintAttributeValue != null) {
						IInternalBeanProxyHost constraintHost = getSettingBeanProxyHost(constraintAttributeValue);
						constraintBeanProxy = instantiateSettingBean(constraintHost, expression, sfConstraintConstraint, constraintAttributeValue,
								ccAdapter);
						if (constraintHost == null)
							return; // It failed creation, don't go any further.
					}
				}

				IProxy beforeBeanProxy; // The beanproxy to go before, if any.
				if (index != Notification.NO_INDEX)
					beforeBeanProxy = getComponentProxyAt(index + 1); // Need to do +1 because we (componentBeanProxy) are already at that position in the EMF list. So we want to go before next guy.
				else
					beforeBeanProxy = null;

				if (constraintIsSet) {
					BeanAwtUtilities.invoke_addComponent(getProxy(), componentProxy, beforeBeanProxy, constraintBeanProxy, expression);
				} else {
					BeanAwtUtilities.invoke_addComponent(getProxy(), componentProxy, beforeBeanProxy, true, expression);
				}

				expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
				ExpressionProxy applyException = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
				final ComponentConstraintAdapter fccadapter = ccAdapter;
				applyException.addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						processPropertyError((Throwable) event.getProxy(), sfContainerComponents, null, fccadapter);
					}
				});
				
			} finally {
				expression.createTryEnd();
			}
			revalidateBeanProxy();
			componentAdded(constraintComponent, index, expression);
		}
	}
	
	/**
	 * Notification that a component was added.  Subclasses may override to do something in addition to the add.
	 * @param constraintComponent
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void componentAdded(EObject constraintComponent, int index, IExpression expression) {
		
	}

	/**
	 * Return the first instantiated (or in instantiation) component proxy from the component constraints at or after the given index.
	 * Return null if no components after the given position are instantiated or are in instantiation.
	 * @param position
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxy getComponentProxyAt(int position) {
		List components = (List) ((EObject) getTarget()).eGet(sfContainerComponents);
		for (int i=position; i<components.size(); i++) {
			EObject componentConstraint = (EObject) components.get(i);
			IInternalBeanProxyHost componentProxyHost = getSettingBeanProxyHost((IJavaInstance) componentConstraint.eGet(sfConstraintComponent));
			if (componentProxyHost != null && (componentProxyHost.isBeanProxyInstantiated() || componentProxyHost.inInstantiation()))
				return componentProxyHost.getProxy();
		}
		
		return null;
	}
	
	/*
	 * Remove the component from the container on the vm.
	 * @param aConstraintComponent
	 * @param aboutToRelease <code>true </code> if we are about to release and dispose of the control, not just remove.
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	private void removeComponent(EObject aConstraintComponent, boolean aboutToRelease, IExpression expression) {
		ComponentConstraintAdapter ccAdapter = (ComponentConstraintAdapter) EcoreUtil.getExistingAdapter(aConstraintComponent, this);
		if (ccAdapter != null)
			clearError(sfContainerComponents); 
		IJavaInstance component = (IJavaInstance) aConstraintComponent.eGet(sfConstraintComponent);
		try {
			ComponentProxyAdapter componentProxyHost = (ComponentProxyAdapter) EcoreUtil
					.getExistingAdapter(component, IBeanProxyHost.BEAN_PROXY_TYPE);
			// Note: We shouldn't be called during an instantiation of any kind, so we should have a straight instantiated bean.
			if (componentProxyHost != null && componentProxyHost.isBeanProxyInstantiated()) {
				if (!layoutChangePending) {
					BeanAwtUtilities.invoke_removeComponent(getProxy(), componentProxyHost.getBeanProxy(), expression);
				}
				// Always want to restore, even when in layout change pending. Except if about to release then we don't need to because it will reinstantiate if needed.
				if (!aboutToRelease)
					componentProxyHost.restoreVisibility(expression); // Always want to restore, even when in layout change pending.
				componentRemoved(aConstraintComponent, expression, aboutToRelease);
			}
		} catch (ClassCastException e) {
			// Ok, possible that component wasn't a valid child.
		}
	}
	
	/**
	 * Notification that a component was removed. Subclasses may override to do something.
	 * @param aConstraintComponent
	 * @param expression
	 * @param aboutToRelease <code>true</code> if the component is about to be released and disposed. <code>false</code> if just a cancel (a release may follow but we don't know that).
	 * @since 1.1.0
	 */
	protected void componentRemoved(EObject aConstraintComponent, IExpression expression, boolean aboutToRelease) {
		
	}

	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#removingAdapter()
	 */
	protected void removingAdapter() {
		removeAllComponentAdapters();
		super.removingAdapter();
	}
	
	/*
	 *  Remove all of the constraint component adapters.
	 * 
	 * @since 1.1.0
	 */
	private void removeAllComponentAdapters() {
		// The adapter is being removed, so remove any Constraint Adapters that are pointing to us.
		try {
			if (getEObject().eIsSet(sfContainerComponents)) {
				Iterator itr = ((List) getEObject().eGet(sfContainerComponents)).iterator();
				while (itr.hasNext()) {
					removeAdapters((Notifier) itr.next());
				}
			}
		} catch (IllegalArgumentException e) {
			// This can happen if the class had gone undefined. We would get this exception when trying to do sfContainerComponents.
		}
	}

	/*
	 * Remove the ComponentConstraint adapter and the ComponentNameAdapter.
	 */
	private void removeAdapters(Notifier n) {
		ComponentConstraintAdapter ccAdapter = (ComponentConstraintAdapter) EcoreUtil.getExistingAdapter(n, this);
		if (ccAdapter != null)
			n.eAdapters().remove(ccAdapter);
		if (cnameAdapter != null) {
			n = ((EObject) n).eIsSet(sfConstraintComponent) ? (Notifier) ((EObject) n).eGet(sfConstraintComponent) : null;
			if (n != null) {
				n.eAdapters().remove(cnameAdapter);
			}
		}
	}
	

	protected void primReleaseBeanProxy(IExpression expression) {
		// We need to release the Layout because some layouts can't be shared (e.g. BoxLayout) and when we are reinstantiated
		// we will use the old layout. This will fail and throw exceptions.
		// See: https://bugs.eclipse.org/bugs/show_bug.cgi?id=59351
		try {
			IJavaInstance layoutValue = (IJavaInstance) getEObject().eGet(sfLayout);
			if (layoutValue != null) {
				IBeanProxyHost layoutProxyHost = getSettingBeanProxyHost(layoutValue);
				layoutProxyHost.releaseBeanProxy(expression);
			} 
		} catch (IllegalArgumentException e) {
			// This can happen if the class has gone undefined. sfLayout eGet would throw this then.
		} 
		
		// Also remove the adapters on any of the component constraints. This is because if changes are
		// made during the non-instantiated time, we won't see them because the notifications only come through
		// while bean is instantiated.
		removeAllComponentAdapters();

		super.primReleaseBeanProxy(expression);
	}
}