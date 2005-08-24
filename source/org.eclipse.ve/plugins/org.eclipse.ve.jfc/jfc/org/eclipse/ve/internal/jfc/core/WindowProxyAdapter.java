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
package org.eclipse.ve.internal.jfc.core;

/*
 *  $RCSfile: WindowProxyAdapter.java,v $
 *  $Revision: 1.21 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

import org.eclipse.ve.internal.cde.core.CDEUtilities;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * awt Window proxy adapter.
 * 
 * @since 1.1.0
 */
public class WindowProxyAdapter extends ContainerProxyAdapter {

	/**
	 * Construct with domain
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public WindowProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	protected WindowManagerExtension windowManager;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ContainerProxyAdapter#createComponentManager()
	 */
	protected ComponentManager createComponentManager() {
		ComponentManager cm = super.createComponentManager();
		windowManager = new WindowManagerExtension();
		cm.addComponentExtension(windowManager, null);
		return cm;
	}

	/**
	 * We need to keep a separate indication of being on freeform from that implemented by ComponentProxyAdapter. That is because they do it
	 * completely differently. So we need to make it look like we are not on the freeform for Component, but we are for Window.
	 * 
	 * @since 1.1.0
	 */
	protected boolean onFreeForm;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#addToFreeForm(org.eclipse.ve.internal.java.core.CompositionProxyAdapter)
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
		onFreeForm = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#removeFromFreeForm()
	 */
	public void removeFromFreeForm() {
		onFreeForm = false;
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		if (onFreeForm) {
			// On freeform we want at offscreen and we want to be visible. Apply these BEFORE instantiation.
			// Actually we will always be on freeform.
			overrideLocation(BeanAwtUtilities.getOffScreenLocation(expression.getRegistry()), expression);
			overrideVisibility(true, expression);
		}
		IProxy result = super.primInstantiateBeanProxy(expression);
		
		if (onFreeForm)
			windowManager.packWindowOnValidate(!(getEObject().eIsSet(sfComponentBounds) || getEObject().eIsSet(sfComponentSize)), expression);
		
		return result;
	}
	
	protected void primReleaseBeanProxy(IExpression expression) {
		if (isOwnsProxy() && isBeanProxyInstantiated()) {
			// If we own the proxy and are instantiated we need to do an actual "dispose". We can't just release it.
			IProxyMethod dispose = BeanAwtUtilities.getWindowDisposeMethodProxy(expression);
			expression.createSimpleMethodInvoke(dispose, null, new IProxy[] {getProxy()}, false);
		}
		;
		super.primReleaseBeanProxy(expression);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		if (onFreeForm && isBeanProxyInstantiated()) {
			// May be changing useComponentSize setting. This must be done before the
			// actual apply/cancel so that it is set for when the apply occurs. Otherwise
			// timing could cause it to think it should pack to the wrong size.
			EStructuralFeature sf = (EStructuralFeature) notification.getFeature();
			if (sf == sfComponentBounds || sf == sfComponentSize) {
				switch (notification.getEventType()) {
					case Notification.SET:
						if (!CDEUtilities.isUnset(notification)) {
							if (!notification.wasSet()) {
								// TODO See if we can actually group the expression up to all notifications for this transaction instead of just
								// this one notification.
								IExpression expression = getBeanProxyFactory().createExpression();
								try {
									windowManager.packWindowOnValidate(false, expression);
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
						} // Else flow into unset.
					case Notification.UNSET:
						// Little tricker, need to see if the other setting is still set.
						if (sf == sfComponentBounds)
							if (getEObject().eIsSet(sfComponentSize))
								break; // The other is still set, so leave alone.
						if (sf == sfComponentSize)
							if (getEObject().eIsSet(sfComponentBounds))
								break; // The other is still set, so leave alone.

						// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one
						// notification.
						IExpression expression = getBeanProxyFactory().createExpression();
						try {
							windowManager.packWindowOnValidate(true, expression);
						} finally {
							try {
								if (expression.isValid())
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
						break;
				}
			}
		}
		super.notifyChanged(notification);
	}

}
