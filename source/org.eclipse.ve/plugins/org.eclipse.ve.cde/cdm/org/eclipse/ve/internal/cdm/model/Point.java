package org.eclipse.ve.internal.cdm.model;
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
 *  $RCSfile: Point.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */



/**
 * ViewPoint for the OCM Model.
 */
public class Point {
	public int x, y;
	
	public Point() {
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(20);
		s.append(x);
		s.append(',');
		s.append(y);
		return s.toString();
	}
	
	/**
	 * Returns whether this Point is equal to the Object
	 * input. Checks for class type, and the values in 
	 * the input.
	 *
	 * @param o  Object being tested for equality.
	 * @return  Result of the equality test.
	 */
	public boolean equals(Object o){
		if (o instanceof Point){
			Point p = (Point)o;
			return p.x == x && p.y == y;
		}
		return false;
	}
	
	public int hashcode() {
		return x ^ y;
	}		
}