package org.eclipse.ve.internal.jfc.beaninfo;
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
 *  $RCSfile: LocaleLanguage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:33 $ 
 */

import java.util.*;

public class LocaleLanguage{
	private String id = null;
	private String name = null;
	private LocaleCountry[] countries = null;
	private Vector fCountries = new Vector();
public String getID(){
	return id;
}

public String getName(){
	return name;
}

public LocaleCountry[] getCountries(){
	int fSize = fCountries.size();
	countries = new LocaleCountry[fSize];
	
	Enumeration enum = fCountries.elements();
	for (int i = 0; i < fSize && enum.hasMoreElements(); i++){
		countries[i] = (LocaleCountry)enum.nextElement();
	}
	
	//sort the countries into ascending order
	sortCountries();
	return countries;
}

public void sortCountries(){
	for (int i = 0; i < countries.length; i++){
		int min = i;
		String s = countries[min].getName();				
		if ( s != null && !s.equals(" ")){ //$NON-NLS-1$
			for (int j = i; j < countries.length; j++){
				String newString = countries[j].getName();
				if (newString.compareTo(s) < 0){
					min = j;
					s = countries[min].getName();
				}
			}
		}

		LocaleCountry swap = countries[i];
		countries[i] = countries[min];
		countries[min] = swap;
	}

}

public void setID(String anID){
	id = anID;
}

public void setName(String aName){
	name = aName;
}

//eliminate duplication while adding
public void addCountry(LocaleCountry aCountry){
	
	boolean shouldNotAddCountry = false;
	for (int i = 0; i < fCountries.size(); i++){
		LocaleCountry fCountry = (LocaleCountry)(fCountries.elementAt(i));
		if(fCountry == aCountry || aCountry.getName().equals(" ") || aCountry.getName() == null){ //$NON-NLS-1$
				shouldNotAddCountry = true;
		}
		if (fCountry.getName() == aCountry.getName() && fCountry.getVariants() != aCountry.getVariants()){
			LocaleVariant[] fVariants = aCountry.getVariants();
			for (int j = 0; j < fVariants.length; j++){
				((LocaleCountry)(fCountries.elementAt(i))).addVariant(fVariants[j]);
			}
			shouldNotAddCountry = true;
		}
	}
		
	if (shouldNotAddCountry == false){	
		fCountries.addElement(aCountry);
	}
}

}