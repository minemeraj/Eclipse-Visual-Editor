/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChoiceProxyAdapter.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:09 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * @author richkulp
 * 
 * This is for AWT Choice component. It is actually a Linux bug. Sun Bug Parade #188992 has been opened to fix it. The problem is that on Linux you
 * can't do a printAll of a choice that has no entries. You get a null pointer exception. So to get around it we will automatically add in one empty
 * string so that it won't be empty. Since at the moment we don't respond to add(String) as properties anyway, the live object wouldn't see any add's
 * in the code. So an empty string will be just fine.
 * <p>
 * <b>Note: </b>This was marked as "Fixed" for 1.4.1, but the fix was "Not reproducable". So we don't know if it is really fixed, so we'll just keep
 * this permanently.
 */
public class ChoiceProxyAdapter extends ComponentProxyAdapter {

	/**
	 * Construct it.
	 * 
	 * @param domain
	 */
	public ChoiceProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		IProxy bean = super.primInstantiateBeanProxy(expression);
		// See header of this class as to why we are doing what we are doing here.
		expression.createSimpleMethodInvoke(getBeanTypeProxy("java.awt.Choice", expression).getMethodProxy(expression, "add", //$NON-NLS-1$ //$NON-NLS-2$
				new String[] {"java.lang.String"}), bean, new IProxy[] {getBeanProxyFactory().createBeanProxyWith("")}, false); //$NON-NLS-1$ //$NON-NLS-2$
		return bean;
	}
}
