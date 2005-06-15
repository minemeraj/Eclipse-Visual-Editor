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
/*
 * $RCSfile: ShellProxyAdapter.java,v $ $Revision: 1.18 $ $Date: 2005-06-15 20:19:21 $
 */
package org.eclipse.ve.internal.swt;

import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * Proxy adapter for SWT Shell
 * 
 * @since 1.0.0
 */
public class ShellProxyAdapter extends CompositeProxyAdapter {

	protected EStructuralFeature sfText;
	
	public ShellProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		sfText = JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(domain.getEditDomain()), SWTConstants.SF_DECORATIONS_TEXT);

	}
	
	/**
	 * Handle the shell title. It is used when instantiating and no title set, or when apply and applying
	 * 
	 * @param shell
	 * @param title
	 * @param replaceOld
	 * @param wantOld	<code>true</code> if want the old value.
	 * @param expression
	 * 
	 * @return old value proxy if wantOld is <code>true</code> else return null.
	 * @since 1.1.0
	 */
	protected IProxy handleShellTitle(IProxy shell, IProxy title, boolean replaceOld, boolean wantOld, IExpression expression) {
		return expression.createSimpleMethodInvoke(BeanSWTUtilities.getShellApplyTitleMethodProxy(expression),
				null, new IProxy[] {shell, title, expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(replaceOld)}, wantOld);
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		if (onFreeForm) {
			// On freeform we want at offscreen and we want to be visible. Apply these BEFORE instantiation.
			// Actually we will always be on freeform.
			overrideLocation(BeanSWTUtilities.getOffScreenLocation(), expression);
			overrideVisibility(true, expression);
		}
		
		IProxy result = super.primInstantiateBeanProxy(expression);
		if (!getJavaObject().eIsSet(sfText)) {
			// Handle applying a default title, and get original value to be used later if title is explicitly set.
			IProxy origValue = handleShellTitle(result, null, false, true, expression);
			if (origValue == null || origValue.isBeanProxy()) {
				// No original value or it is already resolved, just put it in the original table.
				getOriginalSettingsTable().put(sfText, origValue);
			} else {
				// It is an expression, so save it when resolved.
				((ExpressionProxy) origValue).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						getOriginalSettingsTable().put(sfText, event.getProxy());
					}
				});
			}
		}
		
		if (onFreeForm)
			shellManager.packWindowOnValidate(!(getEObject().eIsSet(sfControlBounds) || getEObject().eIsSet(sfControlSize)), expression);

		return result;
	}

	protected IProxy primApplyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue) throws NoSuchMethodException, NoSuchFieldException {
		if (propertyDecorator.getEModelElement() == sfText) {
			return handleShellTitle(getProxy(), settingProxy, true, getOriginalValue, expression);
		} else
			return super.primApplyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}
	
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute, IExpression exp, ForExpression forExpression) {
		if (aBeanPropertyAttribute == sfText && isSettingInOriginalSettingsTable(aBeanPropertyAttribute)) {
			// This is title, it was not explicitly set, and we have an original settings, so return that instead of
			// default string we use as a title.
			return (IProxy) getOriginalSettingsTable().get(aBeanPropertyAttribute);
		}
		return super.getBeanPropertyProxyValue(aBeanPropertyAttribute, exp, forExpression);
	}
	
	protected ShellManagerExtension shellManager;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ContainerProxyAdapter#createComponentManager()
	 */
	protected ControlManager createControlManager() {
		ControlManager cm = super.createControlManager();
		shellManager = new ShellManagerExtension();
		cm.addControlExtension(shellManager, null);
		return cm;
	}

	/**
	 * We need to keep a separate indication of being on freeform from that implemented by ControlProxyAdapter. That is because they do it
	 * completely differently. So we need to make it look like we are not on the freeform for Control, but we are for Shell.
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
			if (sf == sfControlBounds || sf == sfControlSize) {
				switch (notification.getEventType()) {
					case Notification.SET:
						if (!CDEUtilities.isUnset(notification)) {
							if (!notification.wasSet()) {
								// TODO See if we can actually group the expression up to all notifications for this transaction instead of just
								// this one notification.
								IExpression expression = getBeanProxyFactory().createExpression();
								try {
									shellManager.packWindowOnValidate(false, expression);
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
						if (sf == sfControlBounds)
							if (getEObject().eIsSet(sfControlSize))
								break; // The other is still set, so leave alone.
						if (sf == sfControlSize)
							if (getEObject().eIsSet(sfControlBounds))
								break; // The other is still set, so leave alone.

						// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one
						// notification.
						IExpression expression = getBeanProxyFactory().createExpression();
						try {
							shellManager.packWindowOnValidate(true, expression);
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
