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
package org.eclipse.jem.internal.proxy.remote.swt;

import org.eclipse.jem.internal.proxy.remote.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.*;

public class REMSWTPointBeanProxy extends REMBeanProxy implements IPointBeanProxy {

protected REMSWTPointBeanProxy(REMProxyFactoryRegistry aRegistry, Integer anID, IBeanTypeProxy aType) {
	super(aRegistry, anID, aType);
}

public int getX() {
	try {
		IIntegerBeanProxy x = (IIntegerBeanProxy) JavaStandardSWTBeanConstants.getConstants(fFactory).getPointXFieldProxy().get(this);
		return x.intValue();
	} catch (ThrowableProxy e) {
		return 0;
	}
}
public int getY() {
	try {
		IIntegerBeanProxy y = (IIntegerBeanProxy) JavaStandardSWTBeanConstants.getConstants(fFactory).getPointYFieldProxy().get(this);
		return y.intValue();
	} catch (ThrowableProxy e) {
		return 0;
	}
}
public void setX(int x) {
	try {
		IBeanProxy xProxy = fFactory.getBeanProxyFactory().createBeanProxyWith(x);
		JavaStandardSWTBeanConstants.getConstants(fFactory).getPointXFieldProxy().set(this,xProxy);
	} catch (ThrowableProxy e) {
	}
}
public void setY(int y) {
	try {
		IBeanProxy yProxy = fFactory.getBeanProxyFactory().createBeanProxyWith(y);
		JavaStandardSWTBeanConstants.getConstants(fFactory).getPointXFieldProxy().set(this,yProxy);
	} catch (ThrowableProxy e) {
	}
}
public void setLocation(int x,int y){
	setX(x);
	setY(y);
}
public void setLocation(IPointBeanProxy aPointBeanProxy){
	setX(aPointBeanProxy.getX());
	setY(aPointBeanProxy.getY());
}
public ProxyFactoryRegistry getProxyFactoryRegistry() {
	return fFactory;
}
}
