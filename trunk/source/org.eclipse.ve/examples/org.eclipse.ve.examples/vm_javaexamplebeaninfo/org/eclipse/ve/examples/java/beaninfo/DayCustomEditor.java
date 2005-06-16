package org.eclipse.ve.examples.java.beaninfo;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.awt.List;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class DayCustomEditor extends Panel {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -9100752274828605545L;
	protected int fDay;
	protected List fList;
	
public DayCustomEditor(int aDay){

	fDay = aDay;
	// Show the user a list of the available shapes with the current shape
	// selected	
	fList = new List();
	fList.setSize(100,40);
	for ( int i=0; i<DayHelper.DAY_NAMES.length ; i++){
		fList.add(DayHelper.DAY_NAMES[i]);
	}
	fList.select(fDay);
	add(fList);
	fList.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent event){
			fDay = fList.getSelectedIndex();
		}		
	});
	
}
/**
 * The shape is the selection index in the list
 */
public int getDay(){
	return fDay;
}

public void setDay(int day) {
	fList.select(day);
	fDay = day;
}
}