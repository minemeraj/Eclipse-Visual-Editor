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
package org.eclipse.jem.internal.proxy.ide.swt;

import org.eclipse.swt.graphics.Point;

import org.eclipse.jem.internal.proxy.ide.*;
/**
 * Beantype proxy for org.eclipse.swt.graphics.Point
 */
public class IDESWTPointBeanTypeProxy extends IDEBeanTypeProxy {


	protected IDESWTPointBeanTypeProxy(IDEProxyFactoryRegistry aRegistry) {
		super(aRegistry, Point.class);
	}
	
	protected IIDEBeanProxy newBeanProxy(Object anObject) {
		return new IDESWTPointBeanProxy((IDEProxyFactoryRegistry) getProxyFactoryRegistry(), (Point) anObject, this);
	}


}