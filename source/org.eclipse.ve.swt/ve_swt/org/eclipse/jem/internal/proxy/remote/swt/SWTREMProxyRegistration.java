/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.*;

public class SWTREMProxyRegistration {
	
public static void initialize(ProxyFactoryRegistry registry){
	REMProxyFactoryRegistry remRegistry = (REMProxyFactoryRegistry)registry;
	new REMStandardSWTBeanTypeProxyFactory(remRegistry);
	new REMStandardSWTBeanProxyFactory(remRegistry);		
}

}
