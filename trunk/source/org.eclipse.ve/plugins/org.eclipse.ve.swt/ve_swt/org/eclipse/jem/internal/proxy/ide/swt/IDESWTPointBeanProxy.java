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

import org.eclipse.jem.internal.proxy.core.IBeanTypeProxy;
import org.eclipse.jem.internal.proxy.core.IPointBeanProxy;
import org.eclipse.jem.internal.proxy.ide.IDEObjectBeanProxy;
import org.eclipse.jem.internal.proxy.ide.IDEProxyFactoryRegistry;

class IDESWTPointBeanProxy extends IDEObjectBeanProxy implements IPointBeanProxy {

	IDESWTPointBeanProxy(IDEProxyFactoryRegistry aRegistry, Point aPoint, IBeanTypeProxy aBeanTypeProxy) {
		super(aRegistry, aPoint, aBeanTypeProxy);
	}

	public int getX() {
		return getPoint().x;
	}

	private Point getPoint() {
		return ((Point) getBean());
	}

	public int getY() {
		return getPoint().y;
	}

	public void setX(int x) {
		getPoint().x = x;
	}

	public void setY(int y) {
		getPoint().y = y;
	}

	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}

	public void setLocation(IPointBeanProxy pointBeanProxy) {
		setX(pointBeanProxy.getX());
		setY(pointBeanProxy.getY());
	}

}