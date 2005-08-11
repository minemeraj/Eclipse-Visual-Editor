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
 * $RCSfile: CompositeProxyAdapter.java,v $ $Revision: 1.36 $ $Date: 2005-08-11 19:28:01 $
 */
package org.eclipse.ve.internal.swt;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

import org.eclipse.ve.internal.cde.core.ErrorNotifier;

import org.eclipse.ve.internal.java.core.*;

/**
 * Proxy adapter for swt.Composites.
 * 
 * @since 1.1.0
 */
public class CompositeProxyAdapter extends ControlProxyAdapter {

	protected EReference sfCompositeControls, sfLayout;

	/**
	 * Construct
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public CompositeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfCompositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		sfLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
	}

	protected void primApplied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sfCompositeControls) {
			if (isTouch)
				return; // Don't want to apply if all we did was touch.
		}
		super.primApplied(feature, value, index, isTouch, expression, testValidity);
	}

	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sfCompositeControls)
			addControl((IJavaObjectInstance) value, index, expression);
		else if (feature == sfLayout)
			setLayoutDataVerifyRequired(expression);
		
		super.applySetting(feature, value, index, expression);
	}

	protected void cancelSetting(EStructuralFeature sf, Object oldValue, int position, IExpression expression) {
		if (sf == sfCompositeControls) {
			removeControl((IJavaObjectInstance) oldValue, expression);
			removeAdapters((Notifier) oldValue);
		} else if (sf == sfLayout)
			setLayoutDataVerifyRequired(expression);
		super.cancelSetting(sf, oldValue, position, expression);
	}

	protected void primMoved(EStructuralFeature feature, Object value, int oldPosition, int newPosition, IExpression expression) {
		if (feature == sfCompositeControls) {
			final IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) value);
			if (controlProxyHost == null || !controlProxyHost.isBeanProxyInstantiated())
				return; // Not instantiated, don't try to move on the jvm.

			// A move of "controls" can be done simply here by using moveAbove instead of remove and add.
			IProxy above = getProxyAt(newPosition + 1, feature);
			if (above != null)
				moveComponentBefore(controlProxyHost.getBeanProxy(), above, expression);
			else
				moveComponentToEnd(controlProxyHost.getBeanProxy(), expression);
			revalidateBeanProxy();
		} else
			super.primMoved(feature, value, oldPosition, newPosition, expression);
	}

	private IProxyMethod moveAboveMethodProxy(IExpression expression) {
		return getBeanProxy().getTypeProxy().getMethodProxy(expression, "moveAbove", new String[] { "org.eclipse.swt.widgets.Control"}); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private IProxyMethod moveBelowMethodProxy(IExpression expression) {
		return getBeanProxy().getTypeProxy().getMethodProxy(expression, "moveBelow", new String[] { "org.eclipse.swt.widgets.Control"}); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Add the control. The control actually is added through instantiation, but we need to move it to the right position.
	 * @param child
	 * @param indexOfChild
	 * 
	 * @since 1.0.2
	 */
	protected void addControl(IJavaObjectInstance child, int indexOfChild, IExpression expression) {
		IInternalBeanProxyHost controlProxyHost = getSettingBeanProxyHost(child);
		IProxy childProxy = instantiateSettingBean(controlProxyHost, expression, sfCompositeControls, child, null);
		if (childProxy == null)
			return;	// Don't go any further. It failed creation.

		// Now we need to move it above the correct guy.
		IProxy before = getProxyAt(indexOfChild+1, sfCompositeControls);
		if (before != null) {
			moveComponentBefore(childProxy, before, expression);
		}
		setLayoutDataVerifyRequired(expression);	// A new guy, need to verify him.
		if (getControlLayoutDataAdapter(child) == null) {
			// We need a layout adapter to listen for layout data changes.
			child.eAdapters().add(new ControlLayoutDataAdapter());
		}
			
	}
	
	protected void removingAdapter() {
		removeAllControlAdapters();
		super.removingAdapter();
	}
	
	private void removeAllControlAdapters() {
		// The adapter is being removed, so remove any Constraint Adapters that are pointing to us.
		try {
			if (getEObject().eIsSet(sfCompositeControls)) {
				Iterator itr = ((List) getEObject().eGet(sfCompositeControls)).iterator();
				while (itr.hasNext()) {
					removeAdapters((Notifier) itr.next());
				}
			}
		} catch (IllegalArgumentException e) {
			// This can happen if the class had gone undefined. We would get this exception when trying to do sfContainerComponents.
		}
	}
	
	private void removeAdapters(Notifier n) {
		ControlLayoutDataAdapter ccAdapter = getControlLayoutDataAdapter(n);
		if (ccAdapter != null)
			n.eAdapters().remove(ccAdapter);
	}

	/**
	 * Get the ControlLayoutDataAdapter for the child.
	 * @param n
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ControlLayoutDataAdapter getControlLayoutDataAdapter(Notifier n) {
		return (ControlLayoutDataAdapter) EcoreUtil.getExistingAdapter(n, this);
	}

	protected void primPrimReleaseBeanProxy(IExpression expression) {
		boolean releaseChildren = isBeanProxyInstantiated();
		removeAllControlAdapters();	// Remove all child adapters because we won't be listening to changes while not instantiated. When re-instantiated they will be added back in.		
		super.primPrimReleaseBeanProxy(expression);
		// Need to release all of the controls.  This is because they will be implicitly disposed anyway when super
		// gets called because the target VM will dispose them as children. 
		// If they have been implicitly disposed on the target VM but the IBeanProxyHost doesn't know about this then i
		// still thinks they are there and will try to re-dispose them and also it'll remain listening for changes 
		// and this causes stack errors - bugzilla 60017
		if (releaseChildren) {
            List controls = (List) ((IJavaObjectInstance) getTarget())
                    .eGet(sfCompositeControls);
            Iterator iter = controls.iterator();
            while (iter.hasNext()) {
                IBeanProxyHost value = (IBeanProxyHost) EcoreUtil.getExistingAdapter((IJavaInstance) iter.next(), IBeanProxyHost.BEAN_PROXY_TYPE);
                if (value != null)
                    value.releaseBeanProxy(expression);
            }
        }
		
	}
	
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		super.setupBeanProxy(beanProxy);
		// TODO Hack for now till I can talk this through with Rich properly - JRW
		// If we don't declare the fact that implicit allocation is not owned then we dispose them
		// and we shouldn't as it is the responsibility of the owner to dispose them
		JavaAllocation allocation = getJavaObject().getAllocation();
		if (allocation instanceof ImplicitAllocation) {
			setOwnsProxy(false);
		}
	}

	/**
	 * Move component before the given proxy.
	 * @param controlProxy
	 * @param before
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void moveComponentBefore(IProxy controlProxy, IProxy before, IExpression expression) {
		expression.createSimpleMethodInvoke(moveAboveMethodProxy(expression), controlProxy, new IProxy[] { before}, false);
	}

	/**
	 * Move the component to the end.
	 * @param controlProxy
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void moveComponentToEnd(IProxy controlProxy, IExpression expression) {
		expression.createSimpleMethodInvoke(moveBelowMethodProxy(expression), controlProxy, new IProxy[] {null}, false);
	}

	/**
	 * Remove the control.
	 * @param aControl
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void removeControl(IJavaObjectInstance aControl, IExpression expression) {
		// Dispose the control. Need to do this through release so that it is actually disposed on the vm.
		getSettingBeanProxyHost(aControl).releaseBeanProxy(expression);
	}
	
	private CompositeManagerExtension compositeManager;
	
	protected ControlManager createControlManager() {
		ControlManager cm = super.createControlManager();
		compositeManager = new CompositeManagerExtension(getBeanProxyDomain().getEditDomain(), this);
		cm.addControlExtension(compositeManager, null);
		return cm;
	}
	
	/**
	 * Tells composite manager extension that layout data needs to be verified
	 * before next layout.
	 * 
	 * @param expression
	 * @since 1.1.0
	 */
	protected void setLayoutDataVerifyRequired(IExpression expression) {
		compositeManager.setVerifyLayoutData(expression);
	}

	
	/**
	 * This is an adapter on the Control children that listen for "layoutData" changes.
	 * This notification is used to tell the CompositeManagerExtension that it needs to
	 * verify the children for appropriate layoutData before the layout occurs. This is
	 * done so that we can return an error (which will be in the ErrorNotifier of
	 * the adapter) for the layout data. But we will complete the layout. If we didn't
	 * do that and invalid was in the layoutData setting the entire layout would fail and
	 * we would get nothing.
	 * 
	 * @since 1.1.0
	 */
	protected class ControlLayoutDataAdapter extends AdapterImpl {
		
		/**
		 * Error notifier used to handle errors in apply the child, in this case the layout data.
		 */
		protected ErrorNotifier errorNotifier = new ErrorNotifier();
		
		/**
		 * Return the error notifier.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public ErrorNotifier getErrorNotifier() {
			return errorNotifier;
		}
		
		/**
		 * The child had a layout data error.
		 * 
		 * @param validLayoutDataType the layout data type that it should of been.
		 * @param invalidLayoutDataType the layout data type that is was set to.
		 * 
		 * @since 1.1.0
		 */
		public void processLayoutDataError(String validLayoutDataType, String invalidLayoutDataType) {
			errorNotifier.processError(new PropertyError(ERROR_WARNING, new MessageError(MessageFormat.format(SWTMessages.CompositionProxyAdapter_LayoutDataInvalid, new Object[] {validLayoutDataType, invalidLayoutDataType}), ERROR_WARNING), sfLayoutData, getTarget()));
		}
		
		/**
		 * Clear the error if it had one.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public void clearLayoutDataError() {
			errorNotifier.clearError(sfLayoutData);			
		}
		
		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
		 * 
		 * So that we find only ours, we use "CompositeProxyAdapter.this" as the key.
		 */
		public boolean isAdapterForType(Object type) {
			return type == CompositeProxyAdapter.this;
		}

		public void notifyChanged(Notification msg) {
			switch (msg.getEventType()) {
				case Notification.SET:
				case Notification.UNSET:
					if (isBeanProxyInstantiated()) {
						if (!msg.isTouch()) {
							if (msg.getFeature() == sfLayoutData) {
								// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one notification.
								IExpression expression = getBeanProxyFactory().createExpression();
								try {
									setLayoutDataVerifyRequired(expression);
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
							if (notification.isPostReinstantiation() && notification.getFeature() == sfLayout) {
								setLayoutDataVerifyRequired(notification.getExpression());
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