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
 *  $RCSfile: JSplitPaneManagerExtension.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

/**
 * JSplitPane Manager extension, client (IDE) side.
 * 
 * @since 1.1.0
 */
public class JSplitPaneManagerExtension extends ComponentManager.ComponentManagerExtension {

	protected IProxy managerExtensionProxy;
	
	protected String getExtensionClassname() {
		return "org.eclipse.ve.internal.jfc.vm.JSplitPaneManagerExtension";	//$NON-NLS-1$
	}
	
	protected IProxy primGetExtensionProxy() {
		return managerExtensionProxy;
	}
	
	protected void primSetExtensionProxy(IProxy proxy) {
		managerExtensionProxy = proxy;
	}
	
	/**
	 * Set the divider location.
	 * @param dividerLocation
	 * @param returnOldLocation
	 * @param expression
	 * 
	 * 
	 * @return old location, if requested.
	 * @since 1.1.0
	 */
	public IProxy setDividerLocation(IProxy dividerLocation, boolean returnOldLocation, IExpression expression) {
		return BeanAwtUtilities.invoke_JSplitPane_setDividerLocation(managerExtensionProxy, dividerLocation, returnOldLocation, expression);		
	}
}
