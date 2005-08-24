/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.swt;

import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.java.core.IBeanProxyHost;

public interface IControlProxyHost extends IBeanProxyHost{
	
void childValidated(IControlProxyHost childProxy);

IControlProxyHost getParentProxyHost();	

IBeanProxy getVisualControlBeanProxy();

void setParentProxyHost(IControlProxyHost aParentProxyHost);

}
