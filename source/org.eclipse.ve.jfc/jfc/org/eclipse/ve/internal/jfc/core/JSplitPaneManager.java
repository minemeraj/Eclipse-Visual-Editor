package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JSplitPaneManager.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import java.io.InputStream;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.jfc.common.Common;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ICallback;

/**
 * This is the IDE class that interfaces to the JSplitPaneManager on the remote vm
 * @author richkulp
 */
public class JSplitPaneManager implements ICallback {

	protected IBeanProxy splitpaneManager;
	protected JSplitPaneProxyAdapter proxyAdapter;

	/**
	 * Set the bean proxy of the splitpane that we are manageing 
	 */
	public void setJSplitPaneBeanProxy(JSplitPaneProxyAdapter proxyAdapter, IBeanProxy splitpaneProxy) {
		this.proxyAdapter = proxyAdapter;
		try {
			if (splitpaneProxy != null && splitpaneManager == null) {
				IBeanTypeProxy splitpaneManagerType = splitpaneProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JSplitPaneManager"); //$NON-NLS-1$
				splitpaneManager = splitpaneManagerType.newInstance();
				splitpaneManager.getProxyFactoryRegistry().getCallbackRegistry().registerCallback(splitpaneManager, this);
			}

			if (splitpaneManager != null)
				BeanAwtUtilities.invoke_set_JSplitPaneBean_Manager(splitpaneManager, splitpaneProxy);
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e);
		}
	}

	public void dispose() {
		if (splitpaneManager != null && splitpaneManager.isValid()) {
			BeanAwtUtilities.invoke_set_JSplitPaneBean_Manager(splitpaneManager, null);
			splitpaneManager.getProxyFactoryRegistry().getCallbackRegistry().deregisterCallback(splitpaneManager);
			splitpaneManager.getProxyFactoryRegistry().releaseProxy(splitpaneManager);
		}
		splitpaneManager = null;
		proxyAdapter = null;		
	}
	
	/**
	 * Set the divider location.
	 */
	public void setDividerLocation(IBeanProxy dividerLocation) {
		BeanAwtUtilities.invoke_set_JSplitPane_DividerLocation_Manager(splitpaneManager, dividerLocation);		
	}
	
	/**
	 * Reset to preferred sizes
	 */
	public void resetToPreferredSizes() {
		BeanAwtUtilities.invoke_reset_JSplitPane_PreferredSizes_Manager(splitpaneManager);			
	}
	
	/**
	 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, IBeanProxy)
	 */
	public Object calledBack(int msgID, IBeanProxy parm) {
		if (msgID == Common.JSP_INVALIDATE) {
			proxyAdapter.revalidateBeanProxy();	// Cause it to get a new image.
		}
		return null;
	}

	/**
	 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, Object[])
	 */
	public Object calledBack(int msgID, Object[] parms) {
		throw new RuntimeException("A jsplitpane manager has been called back incorrectly"); //$NON-NLS-1$	
	}

	/**
	 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBackStream(int, InputStream)
	 */
	public void calledBackStream(int msgID, InputStream is) {
		throw new RuntimeException("A jsplitpane manager has been called back incorrectly"); //$NON-NLS-1$		
	}

}
