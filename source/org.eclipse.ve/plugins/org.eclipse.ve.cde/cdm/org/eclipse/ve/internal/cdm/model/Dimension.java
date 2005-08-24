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
 *  $RCSfile: Dimension.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */



/**
 * ViewDimension for the OCM Model.
 */
public class Dimension {
	public int width, height;
	
	public Dimension() {
	}
	
	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(20);
		s.append(width);
		s.append(',');
		s.append(height);
		return s.toString();
	}
	
	/**
	 * Returns whether this Dimension is equal to the Object
	 * input. Checks for class type, and the values in 
	 * the input.
	 *
	 * @param o  Object being tested for equality.
	 * @return  Result of the equality test.
	 */
	public boolean equals(Object o){
		if (o instanceof Dimension){
			Dimension p = (Dimension)o;
			return p.width == width && p.height == height;
		}
		return false;
	}
	
	public int hashcode() {
		return width ^ height;
	}		
}
