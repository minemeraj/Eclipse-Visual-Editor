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
/*
 *  $RCSfile: PaletteMenuCreator.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */



package org.eclipse.ve.internal.cde.core;

import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class PaletteMenuCreator implements IMenuCreator {

	protected MenuManager fDropdownMenuManager;
	protected MenuManager fToolbarMenuManager;
	protected Menu fMenu;

public PaletteMenuCreator(MenuManager toolbarMenuManager, MenuManager dropdownMenuManager){
	// There is a bug in Eclipse where the same menu manager can't be shared between the toolbar
	// and the drop down menu
	// The drop down menu is invokved when the toolbar action is off the right hand edge and a >>
	// appears that the user can invoke the menu from
	// To overcome the Eclipse but we need two separate menu managers, otherwise once you use the drop down
	// and then go to the menu and back again it doesn't work
	fToolbarMenuManager = toolbarMenuManager;
	fDropdownMenuManager = dropdownMenuManager;
}
public void dispose(){
	fToolbarMenuManager.dispose();
	fDropdownMenuManager.dispose();
}

public Menu getMenu(Menu aMenu){
	// Get the categories from the menu manager
	Menu fMenu = new Menu(aMenu);
	// Our menu manager has contribution items that are the menu managers
	// for the palette categories
	IContributionItem[] items = fDropdownMenuManager.getItems();
	for(int i=0;i<items.length;i++){
		MenuManager menuManager = (MenuManager)items[i];
		// For each menu manager create a new menu
		menuManager.fill(fMenu,-1);
	}
	return fMenu;
}
public Menu getMenu(Control aControl){
	return fToolbarMenuManager.createContextMenu(aControl);
}
}
