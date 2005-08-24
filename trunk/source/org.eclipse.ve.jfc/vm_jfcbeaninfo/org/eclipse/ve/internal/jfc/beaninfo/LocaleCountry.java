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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: LocaleCountry.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.util.*;

public class LocaleCountry {
	private String id = null;
	private String name = null;
	private LocaleVariant[] variants = null;
	private Vector fVariants = new Vector();
public String getID(){
	return id;
}

public String getName(){
	return name;
}

public LocaleVariant[] getVariants(){
	int fSize = fVariants.size();
	variants = new LocaleVariant[fSize];
	
	Enumeration enumer = fVariants.elements();
	for (int i = 0; i < fSize && enumer.hasMoreElements(); i++){
		variants[i] = (LocaleVariant)enumer.nextElement();
	}
	
	//sort variants into ascending order
	sortVariants();
	return variants;
}


public void sortVariants(){
	for (int i = 0; i < variants.length; i++){
		String s = variants[i].getID();
		int min = i;		
		if ( s != null && !s.equals(" ")){ //$NON-NLS-1$
			for (int j = i; j < variants.length; j++){
				String newString = variants[j].getName();
				if (newString.compareTo(s) < 0){
					min = j;
					s = variants[min].getName();
				}
			}
		}

		LocaleVariant swap = variants[i];
		variants[i] = variants[min];
		variants[min] = swap;
	}

}


public void setID(String anID){
	id = anID;
}

public void setName(String aName){
	name = aName;
}

//we need to insert the variant in a right position to keep the ascending 
//order of array
public void addVariant(LocaleVariant aVariant){
	boolean shouldNotAddVariant = false;
	for (int i = 0; i < fVariants.size(); i++){
		if((LocaleVariant)(fVariants.elementAt(i)) == aVariant 
		|| aVariant.getName() == null || aVariant.getName().equals(" ")){ //$NON-NLS-1$
				shouldNotAddVariant = true;
			}
	}
		
	if (shouldNotAddVariant == false){	
		fVariants.addElement(aVariant);
	}
	
}

}
