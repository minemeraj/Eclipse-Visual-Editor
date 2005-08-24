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
package org.eclipse.ve.internal.jfc.vm;
/*
 *  $RCSfile: JToolBarManager.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.awt.Component;

import javax.swing.*;

/**
 * This is the manager for a javax.swing.JToolBar.
 * It handles removing actions. It is a static helper
 * class. It doesn't have any state that needs to be saved.
 * 
 */
public class JToolBarManager {
		
	/**
	 * Add the component before the component.
	 * @param toolbar toolbar to add to.
	 * @param component component to add, can be Component, or Action.
	 * @param beforeComponent put before this component (component or action) or <code>null</code> if add at end.
	 * 
	 * @since 1.1.0
	 */
	public static void addComponent(JToolBar toolbar, Object component, Object beforeComponent) {
		if (component instanceof Component)
			ContainerManager.addComponentBefore(toolbar, (Component) component, componentForComponent(toolbar, beforeComponent), false);
		else if (component instanceof Action) {
			// Need to find out where it should go, add to the end (since can't add action at position) and then move it.
			int pos = ContainerManager.componentIndex(toolbar, componentForComponent(toolbar, beforeComponent), componentForComponent(toolbar, component));
			JButton newItem = toolbar.add((Action) component);
			if (pos != -1) {
				// Need to add back again in correct index. 
				toolbar.add(newItem, pos);
			}
		}
	}
	
	/**
	 * Remove the component
	 * @param toolbar toolbar to remove from
	 * @param component component, string, or action to remove. 
	 * 
	 * @since 1.1.0
	 */
	public static void removeComponent(JToolBar toolbar, Object component) {
		ContainerManager.removeComponent(toolbar, componentForComponent(toolbar, component));
	}
	
	
	/**
	 * Return the true awt.Component for the incoming component
	 * @param toolbar toolbar to look into
	 * @param component component to look for. Can be component, action, or string. <code>null</code> is valid and so will return null
	 * @return <code>null</code> if component not found, or awt.Component for the component.
	 * 
	 * @since 1.1.0
	 */
	protected static Component componentForComponent(JToolBar toolbar, Object component) {
		if (component == null)
			return null;
		if (component instanceof Component)
			return (Component) component;
		if (component instanceof Action) {
			Component[] components = toolbar.getComponents();
			Action a = (Action) component;
			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof JButton) {
					JButton toolbarItem = (JButton)components[i];
					if (toolbarItem.getAction() == a)
						return toolbarItem;
				}
			}
			return null;			
		}
		
		return null;
	}	
}
