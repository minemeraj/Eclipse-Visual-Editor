/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabFolderProxyAdapter.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-18 21:55:55 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * swt TabFolder proxy adapter.
 * @since 1.0.0
 */
public class TabFolderProxyAdapter extends CompositeProxyAdapter {
	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public TabFolderProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		setSingleItemsFeatureName(DEFAULT_ITEMS_NAME);
	}
	
	/**
	 * Sets the selected TabItem corresponding to the TabItem for this TabFolder. This is public and is used by 
	 * the edit parts to bring a selected tab to the front so it can be viewed and edited.
	 * @param tabItem
	 * 
	 * @since 1.1.0
	 */
	public void setSelection(IJavaObjectInstance tabItem) {
		if (!isBeanProxyInstantiated())
			return;
		IBeanProxyHost proxyhost = getSettingBeanProxyHost(tabItem);
		if (proxyhost.isBeanProxyInstantiated())
			BeanSWTUtilities.invoke_tabfolder_setSelection(getBeanProxy(), proxyhost.getBeanProxy());
		revalidateBeanProxy();
	}	
}