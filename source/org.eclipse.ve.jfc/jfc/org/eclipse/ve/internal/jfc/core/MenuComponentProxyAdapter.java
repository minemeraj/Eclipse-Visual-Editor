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
 *  $RCSfile: MenuComponentProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.jem.internal.proxy.core.*;

/**
 * Proxy Adaptor for handling AWT Menu Components.
 */
abstract public class MenuComponentProxyAdapter extends BeanProxyAdapter {
public MenuComponentProxyAdapter(IBeanProxyDomain domain) {
	super(domain);
}
/**
 * Return the bean proxy to the java.awt.MenuComponent
 * For components it is the same as the bean proxy.  Subclasses can override if they provide the component
 * from another source. 
 */
protected IBeanProxy getVisualComponentBeanProxy(){

	return getBeanProxy();
	
}
}
