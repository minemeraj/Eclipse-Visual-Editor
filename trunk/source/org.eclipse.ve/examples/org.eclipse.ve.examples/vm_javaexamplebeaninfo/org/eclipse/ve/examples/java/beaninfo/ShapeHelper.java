/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.beaninfo;

import org.eclipse.ve.examples.java.vm.Area;


public class ShapeHelper {
	
	public final static String[] fShapeNames = new String[] { "None" , "Oval" , "Diamond" };
	public final static Integer[] fShapeValues = new Integer[] { new Integer(0) , new Integer(1) , new Integer(2) };
	public final static String[] fInitStrings;
	static {
		String area = Area.class.getName();
		fInitStrings = new String[] { 
		area+".NO_SHAPE" ,
		area+".OVAL" , 
		area+".DIAMOND" };
	}
	

public static int getShapeIndex(String text){
	for (int i = 0; i < fShapeNames.length ; i++){
		if ( fShapeNames[i].equals(text)) {
			return i;
		}
	}
	throw new IllegalArgumentException("Value must be None, Oval or Diamond");	
}
}
