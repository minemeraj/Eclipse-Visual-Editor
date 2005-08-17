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
 *  Created Aug 16, 2005 by Gili Mendel
 * 
 *  $RCSfile: MenuProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2005-08-17 12:30:36 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.*;

/**
 * Menu Proxy Adapter.
 * 
 * @since 1.1.0.1
 */
public class MenuProxyAdapter extends WidgetProxyAdapter {

	private EStructuralFeature sf_items;

	/**
	 * Construct with domain.
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public MenuProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_MENU_ITEMS);
	}

	private boolean inApplyItem; // Are we applying items? It is assumed that we would never recurse and apply another kind of item while
									// reapplying one kind.

	/**
	 * Call to apply an item setting (meant to be called from applySetting).
	 * 
	 * @param feature
	 * @param value
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void applyItemSetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		// We could come here again because we be in createItem and the item had already existed once,
		// so it sends out reinstantiation notice. We would then try to apply it again, and this
		// time since it is instantiated it will go reapplyItems and create it again.
		if (!inApplyItem) {
			inApplyItem = true;
			try {
				if (inInstantiation() || getProxyAt(index, feature) == null) {
					// When in instantiation, we can only create in the given order they are in the list.
					// The ctor could supply an index, but we don't know anything about that.
					// If it is in instantiation we know they will arrive here in the list order; there are no insertions.
					//
					// If not in instantiation, BUT all of the following items are not yet created, we can just instantiate
					// here. We won't have to worry about order.
					createItem(feature, value, expression);
				} else {
					// We are not in instantiation and this is an insert (because there is a following item that is instantiated).
					// However, it could be due to a re-instantiation, in which case it is already instantiated, so we just ignore this then.
					// We would get into a loop if we released and reinstantiated, because that would come through again as an apply.
					IBeanProxyHost settingHost = getSettingBeanProxyHost((IJavaInstance) value);
					if (!settingHost.isBeanProxyInstantiated())
						reapplyItems(feature, expression, false);
				}
			} finally {
				inApplyItem = false;
			}
		}
	}

	/**
	 * Called to apply list of items (meant to be called from primApplyList)
	 * 
	 * @param feature
	 * @param values
	 * @param index
	 * @param isTouch
	 * @param expression
	 * @param testValidity
	 * 
	 * @return <code>true</code> if caller should return, <code>false</code> if caller should continue and do super.primAppliedList.
	 * @since 1.1.0
	 */
	protected boolean primAppliedListItems(EStructuralFeature feature, List values, int index, boolean isTouch, IExpression expression,
			boolean testValidity) {
		if (!inInstantiation() && getProxyAt(index, feature) != null) {
			// We are not in instantiation, and we are adding multiples, and there are some after the insert point that are instantiatied.
			// We need to reapply all columns for that.
			// We use applied list to handle this instead of going to applied so that we only do the reapply once instead of one per inserted
			// column.
			reapplyItems(feature, expression, testValidity);
			revalidateBeanProxy(); // Need to explicitly revalidate because normally applied does it, but we aren't calling it.
			return true; // Don't continue normal primAppliedList.
		} else
			return false; // Continue normal primAppliedList.
	}

	/*
	 * Instantiate the item. @param feature @param value @param expression
	 * 
	 * @since 1.1.0
	 */
	private void createItem(EStructuralFeature feature, Object value, IExpression expression) {
		IInternalBeanProxyHost settingHost = getSettingBeanProxyHost((IJavaInstance) value);
		instantiateSettingBean(settingHost, expression, feature, value, null); // Don't record error here, the item as a child will have it.
	}

	private boolean inReapplyItems; // Are we reapplying items? It is assumed that we would never recurse and apply another kind of item while
									// reapplying one kind.

	/**
	 * Remove and reapply all of the declared columns.
	 * 
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	private void reapplyItems(EStructuralFeature feature, IExpression expression, boolean testValidity) {
		if (!inReapplyItems) {
			inReapplyItems = true;
			try {
				removeAllItems(feature, expression);
				List columns = (List) ((EObject) getTarget()).eGet(feature);
				Iterator iter = columns.iterator();
				while (iter.hasNext()) {
					Object value = iter.next();
					if (testApplyValidity(expression, testValidity, feature, value, true)) {
						// Need to catch the bean instantiation error because it may not instantiate. We've already logged the error, but we want to
						// go on to next item.
						expression.createTry();
						createItem(feature, value, expression);
						expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
						expression.createTryEnd();
					}
				}
			} finally {
				inReapplyItems = false;
			}
		}
	}

	/**
	 * Remove the item. (Meant to be called from cancelSetting for items features).
	 * 
	 * @param item
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void removeItem(IJavaObjectInstance item, IExpression expression) {
		// Dispose the item
		getSettingBeanProxyHost(item).releaseBeanProxy(expression);
	}

	/**
	 * Remove all items. (Meant to be called from primPrimRelease for the items features).
	 * 
	 * @param feature
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void removeAllItems(EStructuralFeature feature, IExpression expression) {
		if (!isBeanProxyInstantiated())
			return;

		List items = (List) ((EObject) getTarget()).eGet(feature);
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			removeItem((IJavaObjectInstance) iter.next(), expression);
		}
	}

	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sf_items) {
			applyItemSetting(feature, value, index, expression);
		} else
			super.applySetting(feature, value, index, expression);
	}

	protected void primAppliedList(EStructuralFeature feature, List values, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sf_items) {
			if (primAppliedListItems(feature, values, index, isTouch, expression, testValidity))
				return;
		}
		super.primAppliedList(feature, values, index, isTouch, expression, testValidity);
	}

	protected void cancelSetting(EStructuralFeature sf, Object oldValue, int position, IExpression expression) {
		if (sf == sf_items)
			removeItem((IJavaObjectInstance) oldValue, expression);
		else
			super.cancelSetting(sf, oldValue, position, expression);
	}

	public void primPrimReleaseBeanProxy(final IExpression expression) {
		boolean wasInstantiated = isBeanProxyInstantiated();
		super.primPrimReleaseBeanProxy(expression);
		if (wasInstantiated) {
			// Need to release all of the items. This is because they will be implicitly disposed anyway when super
			// gets called because the target VM will dispose them as children.
			//
			// Visit the set features, and those that are the items features will be removed.
			getJavaObject().visitSetFeatures(new FeatureValueProvider.Visitor() {

				public Object isSet(EStructuralFeature feature, Object value) {
					if (feature == sf_items)
						removeAllItems(feature, expression);
					return null;
				}

			});
		}
	}

	/**
	 * This is called by Item proxy adapters to do their reinstantiation. That is because they can't do it in the normal way. If they did, we, the
	 * parent, would be confused and would think this was an appy of new one and would try to release it and recreate it again. We must do the release
	 * and the add back.
	 * <p>
	 * It is not meant to be called by anyone else.
	 * 
	 * @param feature
	 * @param item
	 * @param itemProxy
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public final void reinstantiateItem(final EStructuralFeature feature, final IJavaObjectInstance item, final IBeanProxyHost itemProxy,
			final IExpression expression) {
		// Do the whole reinstantiate on the UI thread -
		if (onUIThread())
			primReinstantiateItem(feature, item, itemProxy, expression);
		else {
			invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {

				protected Object doRun(IBeanProxy displayProxy) {
					primReinstantiateItem(feature, item, itemProxy, expression);
					return null;
				}
			});
		}
	}

	/**
	 * Called to do actual reinstantiate, but it is guarenteed to be on the UI thread.
	 * 
	 * @param feature
	 * @param item
	 * @param itemProxy
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void primReinstantiateItem(EStructuralFeature feature, IJavaObjectInstance item, IBeanProxyHost itemProxy, IExpression expression) {
		itemProxy.releaseBeanProxy(expression);
		applyItemSetting(feature, item, ((List) getJavaObject().eGet(feature)).indexOf(item), expression);
	}
}
