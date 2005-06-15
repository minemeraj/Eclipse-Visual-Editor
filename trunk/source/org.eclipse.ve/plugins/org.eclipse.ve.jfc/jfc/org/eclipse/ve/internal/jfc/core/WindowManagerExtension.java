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
 *  $RCSfile: WindowManagerExtension.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-15 20:19:27 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;
 

/**
 * Manager for awt.Window subclasses.
 * @since 1.1.0
 */
public class WindowManagerExtension extends ComponentManager.ComponentManagerExtension {

	protected IProxy windowManagerProxy;
	
	protected String getExtensionClassname() {
		return BeanAwtUtilities.WINDOWMANAGEREXTENSION_CLASSNAME;
	}
	
	protected IProxy primGetExtensionProxy() {
		return windowManagerProxy;
	}
	
	protected void primSetExtensionProxy(IProxy proxy) {
		windowManagerProxy = proxy;
	}
	
	/**
	 * Set whether the window should be packed on each validation. This is used
	 * when the window doesn't have an explicit size or bounds set. That way
	 * it will shrink and grow as needed.
	 * @param pack
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void packWindowOnValidate(boolean pack, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getWindowPackOnChange(expression), windowManagerProxy, new IProxy[] {expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(pack)}, false);		
	}
}
