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
 *  $RCSfile: CTabFolderProxyAdapter.java,v $
 *  $Revision: 1.4 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * swt CTabFolder proxy adapter.
 * @since 1.0.0
 */
public class CTabFolderProxyAdapter extends ItemParentProxyAdapter {
	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public CTabFolderProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
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
			BeanSWTUtilities.invoke_ctabfolder_setSelection(getBeanProxy(), proxyhost.getBeanProxy());
		revalidateBeanProxy();
	}	
}