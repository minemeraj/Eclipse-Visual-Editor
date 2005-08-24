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

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.remote.REMBeanProxy;
import org.eclipse.jem.internal.proxy.remote.REMProxyFactoryRegistry;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;

public class REMSWTRectangleBeanProxy extends REMBeanProxy implements IRectangleBeanProxy {

protected REMSWTRectangleBeanProxy(REMProxyFactoryRegistry aRegistry, Integer anID, IBeanTypeProxy aType) {
	super(aRegistry, anID, aType);
}

public int getX() {
	try {
		IIntegerBeanProxy x = (IIntegerBeanProxy) JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleXFieldProxy().get(this);
		return x.intValue();
	} catch (ThrowableProxy e) {
		return 0;
	}
}
public int getY() {
	try {
		IIntegerBeanProxy y = (IIntegerBeanProxy) JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleYFieldProxy().get(this);
		return y.intValue();
	} catch (ThrowableProxy e) {
		return 0;
	}
}
public void setX(int x) {
	try {
		IBeanProxy xProxy = fFactory.getBeanProxyFactory().createBeanProxyWith(x);
		JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleXFieldProxy().set(this,xProxy);
	} catch (ThrowableProxy e) {
	}
}
public void setY(int y) {
	try {
		IBeanProxy yProxy = fFactory.getBeanProxyFactory().createBeanProxyWith(y);
		JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleXFieldProxy().set(this,yProxy);
	} catch (ThrowableProxy e) {
	}
}
public void setLocation(int x, int y) {
	setX(x);
	setY(y);
}
public void setLocation(IPointBeanProxy pointBeanProxy) {
	setX(pointBeanProxy.getX());
	setY(pointBeanProxy.getY());
}
public int getHeight() {
	try {
		IIntegerBeanProxy height = (IIntegerBeanProxy) JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleHeightFieldProxy().get(this);
		return height.intValue();
	} catch (ThrowableProxy e) {
		return 0;
	}
}
public int getWidth() {
	try {
		IIntegerBeanProxy width = (IIntegerBeanProxy) JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleWidthFieldProxy().get(this);
		return width.intValue();
	} catch (ThrowableProxy e) {
		return 0;
	}
}
public void setHeight(int height) {
	try {
		IBeanProxy heightProxy = fFactory.getBeanProxyFactory().createBeanProxyWith(height);
		JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleXFieldProxy().set(this,heightProxy);
	} catch (ThrowableProxy e) {
	}
}
public void setWidth(int width) {
	try {
		IBeanProxy widthProxy = fFactory.getBeanProxyFactory().createBeanProxyWith(width);
		JavaStandardSWTBeanConstants.getConstants(fFactory).getRectangleXFieldProxy().set(this,widthProxy);
	} catch (ThrowableProxy e) {
	}
}
public void setSize(int width, int height) {
	setWidth(width);
	setHeight(height);	
}
public void setSize(IPointBeanProxy size) {
	setSize(size.getX(),size.getY());
}
public void setBounds(int x, int y, int width, int height) {
	setX(x);
	setY(y);
	setWidth(width);
	setHeight(height);
}
public ProxyFactoryRegistry getProxyFactoryRegistry() {
	return fFactory;
}
public void setSize(IDimensionBeanProxy dim) {
	setWidth(dim.getWidth());
	setHeight(dim.getHeight());	
}
public void setBounds(IRectangleBeanProxy rect) {
	setX(rect.getX());
	setY(rect.getY());
	setWidth(rect.getWidth());
	setHeight(rect.getHeight());
}
}
