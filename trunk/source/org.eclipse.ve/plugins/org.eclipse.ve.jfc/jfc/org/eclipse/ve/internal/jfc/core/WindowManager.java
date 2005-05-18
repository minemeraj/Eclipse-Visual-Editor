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
 *  $RCSfile: WindowManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-18 16:36:07 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;
 

/**
 * Manager for awt.Window subclasses.
 * @since 1.1.0
 */
public class WindowManager extends ComponentManager {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentManager#getComponentManagerClassname()
	 */
	protected String getComponentManagerClassname() {
		return "org.eclipse.ve.internal.jfc.vm.WindowManager"; //$NON-NLS-1$
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
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getWindowPackOnChange(expression), fComponentManagerProxy, new IProxy[] {expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(pack)}, false);		
	}
}
