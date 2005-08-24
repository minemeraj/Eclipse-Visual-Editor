/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cdm.model;
/*
 *  $RCSfile: Rectangle.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */



/**
 * ViewRectangle for the OCM Model.
 */
public class Rectangle {
	public int x, y, width, height;
	
	public Rectangle() {
	}
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle(Rectangle rect) {
		this(rect.x, rect.y, rect.width, rect.height);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(20);
		s.append(x);
		s.append(',');
		s.append(y);
		s.append(',');
		s.append(width);
		s.append(',');
		s.append(height);
		return s.toString();
	}
	
	/**
	 * Returns whether this Rectangle is equal to the Object
	 * input. Checks for class type, and the values in 
	 * the input.
	 *
	 * @param o  Object being tested for equality.
	 * @return  Result of the equality test.
	 */
	public boolean equals(Object o){
		if (o instanceof Rectangle){
			Rectangle p = (Rectangle)o;
			return p.x == x && p.y == y && p.width == width && p.height == height;
		}
		return false;
	}
	
	public int hashcode() {
		return x ^ y ^ width ^ height;
	}	
	
	public Point getLocation() {
		return new Point(x,y);
	}
	
	public Dimension getSize() {
		return new Dimension(width,height);
	}
}
