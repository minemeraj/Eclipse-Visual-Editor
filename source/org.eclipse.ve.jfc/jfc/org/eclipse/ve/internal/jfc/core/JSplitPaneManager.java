/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: JSplitPaneManager.java,v $
 *  $Revision: 1.5 $  $Date: 2005-05-11 19:01:38 $ 
 */

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;

/**
 * JSplitPane Manager, client (IDE) side.
 * 
 * @since 1.1.0
 */
public class JSplitPaneManager extends ComponentManager {

	protected String getComponentManagerClassname() {
		return "org.eclipse.ve.internal.jfc.vm.JSplitPaneManager";
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
		return BeanAwtUtilities.invoke_JSplitPane_setDividerLocation(getComponentManagerProxy(), dividerLocation, returnOldLocation, expression);		
	}
}
