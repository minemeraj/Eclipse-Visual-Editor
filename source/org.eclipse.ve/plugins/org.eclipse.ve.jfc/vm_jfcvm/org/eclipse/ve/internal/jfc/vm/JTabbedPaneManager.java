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
 *  $RCSfile: JTabbedPaneManager.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:35 $ 
 */

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

/**
 * This is the manager for a javax.swing.JTabbedPane. It allows
 * inserting tabs before a particular component, plus access through component. 
 * It's purpose is to hide indexes and instead
 * work on components themselves. This is because the index of 
 * the component back in JBCF may not necessarily be the index
 * within the live component. This is because of components that
 * couldn't be instantiated due to errors, or if the container was
 * a subclass, there could be components already in the container.
 * 
 * Note: It is static. No need to have one for each tabbed pane. The tabbed pane
 * will always be passed in.
 */
public class JTabbedPaneManager {
	
	
	/**
	 * Insert a tab before the specified component with the given tab info. If the
	 * component is not found, then add at end.
	 */
	public static void insertTabBefore(JTabbedPane tabbedPane, String title, Icon icon, Component component , String tooltip, Component beforeComponent) {
		int pos = beforeComponent != null ? tabbedPane.indexOfComponent(beforeComponent) : -1;
		if (pos != -1)
			tabbedPane.insertTab(title, icon, component, tooltip, pos);
		else
			tabbedPane.addTab(title, icon, component, tooltip);
	}
	
	/**
	 * Set icon at the component specified.
	 */
	public static void setIconAt(JTabbedPane tabbedPane, Component component, Icon icon) {
		int pos = tabbedPane.indexOfComponent(component);
		if (pos != -1)
			tabbedPane.setIconAt(pos, icon);
	}
	
	/**
	 * Set title at the component specified.
	 */
	public static void setTitleAt(JTabbedPane tabbedPane, Component component, String title) {
		int pos = tabbedPane.indexOfComponent(component);
		if (pos != -1)
			tabbedPane.setTitleAt(pos, title);
	}	
	
	/**
	 * Set tooltiptext at the component specified.
	 */
	public static void setTooltipTextAt(JTabbedPane tabbedPane, Component component, String tooltip) {
		int pos = tabbedPane.indexOfComponent(component);
		if (pos != -1)
			tabbedPane.setToolTipTextAt(pos, tooltip);
	}	
	
}
