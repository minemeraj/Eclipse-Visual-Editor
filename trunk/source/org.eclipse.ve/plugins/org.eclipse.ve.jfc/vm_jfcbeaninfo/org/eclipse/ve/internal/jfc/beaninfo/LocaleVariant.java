package org.eclipse.ve.internal.jfc.beaninfo;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: LocaleVariant.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:11 $ 
 */


public class LocaleVariant {
	private String id;
	private String name;
	
public String getID(){
	return id;
}


public String getName(){
	return name;
}

public void setID(String anID){
	id = anID;
}

public void setName(String aName){
	name = aName;
}


}