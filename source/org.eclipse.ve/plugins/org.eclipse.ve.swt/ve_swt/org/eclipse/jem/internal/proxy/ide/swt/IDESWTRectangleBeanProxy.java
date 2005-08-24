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
package org.eclipse.jem.internal.proxy.ide.swt;

import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.ide.IDEObjectBeanProxy;
import org.eclipse.jem.internal.proxy.ide.IDEProxyFactoryRegistry;

public class IDESWTRectangleBeanProxy extends IDEObjectBeanProxy implements IRectangleBeanProxy {

	IDESWTRectangleBeanProxy(IDEProxyFactoryRegistry aRegistry, Rectangle aPoint, IBeanTypeProxy aBeanTypeProxy) {
		super(aRegistry, aPoint, aBeanTypeProxy);
	}

	public int getX() {
		return getRectangle().x;
	}

	private Rectangle getRectangle() {
		return ((Rectangle) getBean());
	}

	public int getY() {
		return getRectangle().y;
	}

	public void setX(int x) {
		getRectangle().x = x;
	}

	public void setY(int y) {
		getRectangle().y = y;
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
		return getRectangle().height;
	}

	public int getWidth() {
		return getRectangle().width;
	}

	public void setHeight(int height) {
		getRectangle().height = height;
	}

	public void setWidth(int width) {
		getRectangle().width = width;
	}

	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public void setSize(IPointBeanProxy size) {
		setSize(size.getX(), size.getY());
	}

	public void setBounds(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
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
