package org.eclipse.ve.internal.jfc.vm;
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
 *  $RCSfile: JMenuManager.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:35 $ 
 */

import java.awt.Component;
import java.awt.Container;

import javax.swing.*;
import javax.swing.Action;
import javax.swing.JMenuItem;

/**
 * This is the manager for a javax.swing.JMenu and JPopupMenu.
 * It handles removing actions and strings.
 * 
 * Note: It is static. No need to have one for each menu... it will always be passed in.
 */
public class JMenuManager extends Object {

	public static int componentIndexForString(Container menu, String s) {
		Component[] components = null;
		if (menu instanceof JMenu)
			components = ((JMenu)menu).getMenuComponents();
		else
			components = menu.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JMenuItem) {
				JMenuItem menuitem = (JMenuItem)components[i];
				if (menuitem.getText().equals(s))
					return i;
			}
		}
		return -1;
	}

	public static void removeMenuItemWithString(Container menu, String s) {
		int index = componentIndexForString(menu, s);
		if (index != -1)
			menu.remove(index);
	}

	public static int componentIndexForAction(Container menu, Action a) {
		Component[] components = null;
		if (menu instanceof JMenu)
			components = ((JMenu)menu).getMenuComponents();
		else
			components = menu.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JMenuItem) {
				JMenuItem menuitem = (JMenuItem)components[i];
				if (menuitem.getAction() == a)
					return i;
			}
		}
		return -1;
	}

	public static void removeMenuItemWithAction(Container menu, Action a) {
		int index = componentIndexForAction(menu, a);
		if (index != -1)
			menu.remove(index);
	}
}
