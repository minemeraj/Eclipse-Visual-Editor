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
/*
 * $RCSfile: WidgetProxyAdapter.java,v $ $Revision: 1.22 $ $Date: 2005-08-18 21:55:55 $
 */
package org.eclipse.ve.internal.swt;



import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.*;

/**
 * Proxy adapter for SWT Widgets.
 * 
 * @since 1.0.0
 */
public class WidgetProxyAdapter extends UIThreadOnlyProxyAdapter implements IExecutableExtension {

	public WidgetProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	// Style bit. Calculated once and saved. It is used repeatedly.
	private static final int NO_STYLE = -1;

	private int style = NO_STYLE;

	private EStructuralFeature sf_items;

	private String itemsName;

	private boolean inApplyItem;

	private boolean inReapplyItems;
	
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget instanceof IJavaObjectInstance) {
			sf_items = getSingleItemsFeature((IJavaObjectInstance) newTarget); 
		}
	}
	
	/**
	 * Subclasses can use this if their items name is simply "items", the default.
	 * They would use it in {@link #setSingleItemsFeatureName(String)}.
	 * @since 1.1.0.1
	 */
	public static final String DEFAULT_ITEMS_NAME = "items"; //$NON-NLS-1$
	
	/**
	 * A subclass should call this if they want to supply the single items feature name, rather than
	 * using the initialization data. 
	 * <p>
	 * <b>Note:</b> This must be called before there is a target. If there is already a target or if the
	 * name was set through the initialization data, then this setting will be ignored. That is because
	 * it is too late at that point.
	 * @param itemsName
	 * 
	 * @since 1.1.0.1
	 */
	protected void setSingleItemsFeatureName(String itemsName) {
		if (this.itemsName == null && getTarget() == null) {
			this.itemsName = itemsName;
		}
	}

	/**
	 * Answer whether this feature is an "items" feature. By default, there are no items. The setinitialization data
	 * will be used to set the items feature name. 
	 * However, if a subclass can have more than one items feature (e.g. Tree
	 * has columns and items), the subclass should test for both. If it returns true then the standard apply/cancel/release
	 * will be done.
	 *  
	 * @param feature
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isItemFeature(EStructuralFeature feature) {
		return feature == sf_items;
	}
	
	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (isItemFeature(feature)) {
			applyItemSetting(feature, value, index, expression);
		} else
			super.applySetting(feature, value, index, expression);
	}	

	protected void primPrimReleaseBeanProxy(final IExpression expression) {
		boolean wasInstantiated = isBeanProxyInstantiated();
		style = NO_STYLE; // Uncache the style bit
		if (isOwnsProxy() && isBeanProxyInstantiated()) {
			BeanSWTUtilities.invoke_WidgetDispose(getProxy(), expression);
		}
		if (wasInstantiated) {
			// Need to release all of the items. This is because they where implicitly disposed anyway when the
			// widget dispose was called because the target VM will dispose them as children.
			//
			// Visit the set features, and those that are the items features will be removed.
			getJavaObject().visitSetFeatures(new FeatureValueProvider.Visitor(){
			
				public Object isSet(EStructuralFeature feature, Object value) {
					if (isItemFeature(feature))
						removeAllItems(feature, expression);
					return null;
				}
			
			});
		}		
	}

	/**
	 * Supply if there is only one items feature. The default uses the set initialization data.
	 * Subclasses should override and return null if there are more than one and should use 
	 * instead the isItemFeature override. This is used to just supply a single one through the
	 * set initialization data.
	 * @param target
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected EStructuralFeature getSingleItemsFeature(IJavaObjectInstance target) {
		return target.eClass().getEStructuralFeature(itemsName);	
	}

	/**
	 * @return the int style value by interrogate getStyle() on the targetVM on the correct thread
	 * 
	 * @since 1.0.0
	 */
	public int getStyle() {
		if (style == NO_STYLE && isBeanProxyInstantiated()) {
			invokeSyncExecCatchThrowable(new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
					IBeanProxy widgetBeanProxy = getBeanProxy();
					IMethodProxy getStyleMethodProxy = widgetBeanProxy.getTypeProxy().getMethodProxy("getStyle"); //$NON-NLS-1$
					IIntegerBeanProxy styleBeanProxy = (IIntegerBeanProxy) getStyleMethodProxy.invoke(widgetBeanProxy);
					style = styleBeanProxy.intValue();
					return null;
				}
			});
		}
		return style;
	}

	/**
	 * Call to apply an item setting (meant to be called from applySetting).
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

	protected void primAppliedList(EStructuralFeature feature, List values, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (isItemFeature(feature)) {
			if (primAppliedListItems(feature, values, index, isTouch, expression, testValidity))
				return;
		}
		super.primAppliedList(feature, values, index, isTouch, expression, testValidity);
	}
	
	/**
	 * Called to apply list of items (meant to be called from primApplyList)
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
	protected boolean primAppliedListItems(EStructuralFeature feature, List values, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (!inInstantiation() && getProxyAt(index, feature) != null) {
			// We are not in instantiation, and we are adding multiples, and there are some after the insert point that are instantiatied.
			// We need to reapply all columns for that.
			// We use applied list to handle this instead of going to applied so that we only do the reapply once instead of one per inserted
			// column.
			reapplyItems(feature, expression, testValidity);
			revalidateBeanProxy(); // Need to explicitly revalidate because normally applied does it, but we aren't calling it.
			return true;	// Don't continue normal primAppliedList.
		} else
			return false;	// Continue normal primAppliedList.
	}

	private void createItem(EStructuralFeature feature, Object value, IExpression expression) {
		IInternalBeanProxyHost settingHost = getSettingBeanProxyHost((IJavaInstance) value);
		instantiateSettingBean(settingHost, expression, feature, value, null); // Don't record error here, the item as a child will have it.
	}
	
	protected void cancelSetting(EStructuralFeature sf, Object oldValue, int position, IExpression expression) {
		if (isItemFeature(sf))
			removeItem((IJavaObjectInstance) oldValue, expression);
		else
			super.cancelSetting(sf, oldValue, position, expression);
	}
	

	/**
	 * Return the first instantiated (or in instantiation) proxy at or after the given index.
	 * Return null if no setting after the given position are instantiated or are in instantiation.
	 * <p>
	 * This is a useful method for working with isMany features for doing the add() on the
	 * target vm. It is usually used so that we put it in the right place (since the index
	 * may not actually coorespond to the correct index on the target vm due to superclasses
	 * may of added their own settings that we don't see).
	 * 
	 * @param position position to start looking at
	 * @param feature feature to look into (must be an isMany() feature).
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxy getProxyAt(int position, EStructuralFeature feature) {
		List settings = (List) getEObject().eGet(feature);
		for (int i=position; i<settings.size(); i++) {
			IJavaInstance setting = (IJavaInstance) settings.get(i);
			IInternalBeanProxyHost settingProxyHost = getSettingBeanProxyHost(setting);
			if (settingProxyHost.isBeanProxyInstantiated() || settingProxyHost.inInstantiation())
				return settingProxyHost.getProxy();
		}
		
		return null;
	}
	
	/**
	 * This is called by Item proxy adapters to do their reinstantiation. That is because they can't do it in
	 * the normal way. If they did, we, the parent, would be confused and would think this was an appy of new
	 * one and would try to release it and recreate it again. We must do the release and the add back.
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
	public final void reinstantiateItem(final EStructuralFeature feature, final IJavaObjectInstance item, final IBeanProxyHost itemProxy, final IExpression expression) {
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
	 * Remove and reapply all of the declared items.
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
						// Need to catch the bean instantiation error because it may not instantiate. We've already logged the error, but we want to go on to next item.
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

	/**
	 * Key for "items" feature name for this item parent. If the key is not in the data, then this is not an items parent proxy.
	 * @since 1.1.0.1
	 */
	public static final String ITEMS_NAME_KEY = "items";	//$NON-NLS-1$
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		// The initdata must be in the form of key/values pairs as recognized by the initialization data parser. 
		// If the key for items name is in it, then it will the value.
		//
		// This is needed this way because subclasses may need their own initialization data and this is the only
		// way to allow both of us to work together.
		itemsName = CDEPlugin.parseInitializationData(data, ITEMS_NAME_KEY);
	}

	/**
	 * Called to do actual reinstantiate, but it is guarenteed to be on the UI thread.
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
