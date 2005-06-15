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
 *  $RCSfile: ShellManagerExtension.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

import org.eclipse.ve.internal.swt.ControlManager.ControlManagerExtension;
 

/**
 * Shell manager extension.
 * @since 1.1.0
 */
public class ShellManagerExtension extends ControlManagerExtension {


	protected IProxy shellManagerProxy;

	protected String getExtensionClassname() {
		return BeanSWTUtilities.SHELLMANAGEREXTENSION_CLASSNAME;
	}
	
	protected IProxy primGetExtensionProxy() {
		return shellManagerProxy;
	}
	
	protected void primSetExtensionProxy(IProxy proxy) {
		shellManagerProxy = proxy;
	}
	
	/**
	 * Set whether the shell should be packed on each validation. This is used
	 * when the shell doesn't have an explicit size or bounds set. That way
	 * it will shrink and grow as needed.
	 * @param pack
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void packWindowOnValidate(boolean pack, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanSWTUtilities.getWindowPackOnChange(expression), shellManagerProxy, new IProxy[] {expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(pack)}, false);		
	}

}
