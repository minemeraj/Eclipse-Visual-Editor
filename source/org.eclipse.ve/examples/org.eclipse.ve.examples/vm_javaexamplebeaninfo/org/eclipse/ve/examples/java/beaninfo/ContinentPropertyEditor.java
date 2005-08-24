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

import java.beans.*;

import org.eclipse.ve.examples.java.vm.Continent;

public class ContinentPropertyEditor extends PropertyEditorSupport {
	
	protected Continent fContinent;
	protected int fContinentIndex;
	
public String[] getTags(){
	return Continent.NAMES;
}

public void setValue(Object aContinent){
	fContinent = (Continent)aContinent;
}

public Object getValue(){
	return fContinent;
}

public String getAsText(){
	if ( fContinent == null ) {
		return null;
	} else {
		return fContinent.getName();
	}
}

public void setAsText(String aContinentName){
	for ( int i=0 ; i < Continent.CONTINENTS.length ; i++ ){
		if ( Continent.CONTINENTS[i].getName().equals(aContinentName) ){
			fContinent = Continent.CONTINENTS[i];
			return;
		}
	}
	throw new IllegalArgumentException(aContinentName + " is invalid");
}
public String getJavaInitializationString(){
	
	// For getting each continent back there is a static public field
	// on the Continent class that corresponds to each instance
	StringBuffer sb = new StringBuffer();
	sb.append(Continent.class.getName());
	sb.append('.');
	sb.append(fContinent.getName().toUpperCase());
	return sb.toString();
}
}
