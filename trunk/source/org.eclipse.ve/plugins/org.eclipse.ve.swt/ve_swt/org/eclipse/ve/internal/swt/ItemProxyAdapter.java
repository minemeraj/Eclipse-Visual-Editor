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
 *  $RCSfile: ItemProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-18 21:55:55 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.logging.Level;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IExpression;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
 
/**
 * Item proxy adapter base. The override file <b>MUST</b> supply a parentItem feature or this
 * proxy adapter will not work correctly. Or the subclass must provide it through the super constructor call.
 * 
 * @since 1.1.0
 */
public class ItemProxyAdapter extends WidgetProxyAdapter {

	private EReference sf_items;
	
	private String itemsName;
	
	/**
	 * Key for parent "items" feature name for this item in parent. In other words,
	 * for this item, what is the "items" feature URL that it is stored under in its parent.
	 * For example: 
	 * <pre>
	 * 
	 * <code>parentItems='java:/org.eclipse.swt.custom#CTabFolder/items'</code>
	 * </pre>
	 * @since 1.1.0.1
	 */
	public static final String PARENT_ITEM_NAME_KEY = "parentItems";	//$NON-NLS-1$
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		itemsName = CDEPlugin.parseInitializationData(data, PARENT_ITEM_NAME_KEY);
		super.setInitializationData(config, propertyName, data);
	}
	
	/**
	 * Construct with domain and the items feature used by this item as the parent->child feature that is stored under.
	 * Subclasses would know exactly what feature it is. Else they can use the normal ctor and use the initialization data.
	 * @param domain
	 * @param items
	 * 
	 * @since 1.1.0
	 */
	public ItemProxyAdapter(IBeanProxyDomain domain, EReference items) {
		super(domain);
		sf_items = items;
	}

	/**]
	 * Ctor used when constructing for generic item. itemsName must be set by initialization data. 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public ItemProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	/**
	 * Get the items feature. Do not access sf_items directly.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected EReference getItemsFeature() {
		if (sf_items == null && itemsName != null) {
			try {
				URI uri = URI.createURI(itemsName);
				sf_items = JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain()), uri);
			} catch (IllegalArgumentException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
			itemsName = null;
		}
		return sf_items;
	}
	
	/**
	 * Get the Parent proxy adapter.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IBeanProxyHost getParentProxyAdapter() {
		IJavaInstance parent = (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), getItemsFeature());
		if (parent != null)
			return getSettingBeanProxyHost(parent);
		else
			return null;
	}
	
	public void revalidateBeanProxy() {
		// When we invalidate we need to revalidate the parent as well to cause the image to refresh.
		IBeanProxyHost parentProxy = getParentProxyAdapter();
		// If we are on the freeform then container will not be an instance of table
		if (parentProxy != null) {
			parentProxy.revalidateBeanProxy();
		}

	}
	
	protected void primReinstantiate(IExpression expression) {
		// Items can't reinstantiate themselves. They need to be done by the parent so that they can be ordered correctly.
		// Can only have one parent, so we can use the first reference by items.
		IBeanProxyHost parentProxy = getParentProxyAdapter();
		// If we are on the freeform then container will not be an instance of table
		if (parentProxy != null) {
			try {
				((WidgetProxyAdapter) parentProxy).reinstantiateItem(getItemsFeature(), getJavaObject(), this, expression);
			} catch (ClassCastException e) {
				// Means not a valid parent.
			}
		}
	}
}
