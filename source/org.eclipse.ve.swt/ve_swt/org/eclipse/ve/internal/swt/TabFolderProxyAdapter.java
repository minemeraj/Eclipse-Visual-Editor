/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TabFolderProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2004-09-01 16:58:58 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * 
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
	}

	/**
	 * Sets the selected TabItem corresponding to the index for this TabFolder. This is public and is used by 
	 * the edit parts to bring a selected tab to the front so it can be viewed and edited.
	 */
	public void setSelection(int index) {
		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;
		IIntegerBeanProxy intProxy = getBeanProxy().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(index);
		BeanSWTUtilities.invoke_tabfolder_setSelection(getBeanProxy(), intProxy);
		revalidateBeanProxy();
	}

}