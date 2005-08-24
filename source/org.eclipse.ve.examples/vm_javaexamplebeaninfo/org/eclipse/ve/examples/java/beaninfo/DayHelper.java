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
package org.eclipse.ve.examples.java.beaninfo;

import org.eclipse.ve.examples.java.vm.Area;

public class DayHelper {

	public final static String[] DAY_NAMES = new String[] { 
		"Sunday",		
		"Monday" , 
		"Tuesday" ,
		"Wednesday",
		"Thursday",
		"Friday",
		"Saturday",
		};
	public final static String[] INIT_STRINGS;
	static {
		String area = Area.class.getName();
		INIT_STRINGS = new String[] { 
		area+".SUNDAY" ,		
		area+".MONDAY" ,
		area+".TUESDAY" ,
		area+".WEDNESDAY" ,
		area+".THURSDAY" ,
		area+".FRIDAY" ,
		area+".SATURDAY" ,
		};	
	}

public static int getDayIndex(String text){
	loop1: for (int i = 0; i < DAY_NAMES.length ; i++){
		if ( DAY_NAMES[i].equals(text)) {
			return i;
		}
	}
	throw new IllegalArgumentException("Value must be a day of the week");	
}
}
