/*
 * Created on Jun 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChoiceProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;

/**
 * @author richkulp
 *
 * TODO
 * This is for AWT Choice component. It is actually a Linux bug.
 * Sun Bug Parade #188992 has been opened to fix it. The problem
 * is that on Linux you can't do a printAll of a choice that has
 * no entries. You get a null pointer exception. So to get around
 * it we will automatically add in one empty string so that it won't
 * be empty. Since at the moment we don't respond to add(String) 
 * as properties anyway, the live object wouldn't see any add's
 * in the code. So an empty string will be just fine.
 */
public class ChoiceProxyAdapter extends ComponentProxyAdapter {

	/**
	 * @param domain
	 */
	public ChoiceProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#primInstantiateBeanProxy()
	 */
	protected void primInstantiateBeanProxy() {
		super.primInstantiateBeanProxy();
		
		if (getErrorStatus() != ERROR_SEVERE) {
			// No errors so do the add.
			getBeanProxy().getTypeProxy().getMethodProxy("add", "java.lang.String").invokeCatchThrowableExceptions(getBeanProxy(), getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith("")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}		
	}

}
