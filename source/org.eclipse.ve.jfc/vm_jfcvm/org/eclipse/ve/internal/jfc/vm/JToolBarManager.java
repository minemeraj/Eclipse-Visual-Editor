package org.eclipse.ve.internal.jfc.vm;
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
 *  $RCSfile: JToolBarManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:12 $ 
 */


import java.awt.Component;

import javax.swing.*;

/**
 * @author pwalker
 *
 * This is the manager for a javax.swing.JToolBar
 * It handles adding and removing actions.
 */
public abstract class JToolBarManager {

	public static int componentIndexForAction(JToolBar toolbar, Action a) {
		Component[] components = null;
		components = toolbar.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JButton) {
				JButton item = (JButton) components[i];
				if (item.getAction() == a)
					return i;
			}
		}
		return -1;
	}

	public static void removeItemWithAction(JToolBar toolbar, Action a) {
		int index = componentIndexForAction(toolbar, a);
		if (index != -1)
			toolbar.remove(index);
	}
}
