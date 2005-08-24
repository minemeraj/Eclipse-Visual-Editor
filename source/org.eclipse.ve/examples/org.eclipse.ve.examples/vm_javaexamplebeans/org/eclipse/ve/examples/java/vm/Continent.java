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
package org.eclipse.ve.examples.java.vm;


public class Continent {
	
	public static String[] NAMES = new String[] {"Europe","Asia","Africa","America","Australasia"};
	public static Continent[] CONTINENTS = new Continent[NAMES.length];
	static {
		for ( int i=0 ; i< NAMES.length ; i++ ) {
			CONTINENTS[i] = new Continent(NAMES[i]);
		}
	}
	public static Continent EUROPE = CONTINENTS[0];
	public static Continent ASIA = CONTINENTS[1];
	public static Continent AFRICA = CONTINENTS[2];
	public static Continent AMERICA = CONTINENTS[3];
	public static Continent AUSTRALASIA = CONTINENTS[4];
	
	protected String fName;
	
public Continent(String name){
	fName = name;
}
public String getName(){
	return fName;
}
public void setName(String aName){
	fName = aName;
}
}
