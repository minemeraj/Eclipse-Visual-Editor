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
 *  $RCSfile: JMenuManager.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.awt.Component;
import java.awt.Container;

import javax.swing.*;

/**
 * This is the manager for a javax.swing.JMenu and JPopupMenu.
 * It handles removing actions and strings. It is static helper.
 * Doesn't have any state.
 * 
 */
public class JMenuManager {

	/**
	 * Is the container a JMenu.
	 * @param container
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected static boolean isJMenu(Container container) {
		return container instanceof JMenu;
	}
	
	
	/**
	 * Add the component before the component.
	 * @param component component to add, can be Component, Action, or String
	 * @param beforeComponent put before this component (component, action, or string) or <code>null</code> if add at end.
	 * 
	 * @since 1.1.0
	 */
	public static void addComponent(Container container, Object component, Object beforeComponent) {
		if (component instanceof Component)
			ContainerManager.addComponentBefore(container, (Component) component, componentForComponent(container, beforeComponent), false);
		else if (component instanceof String) {
			// Need to find out where it should go, add to the end (since can't add string at position) and then move it.
			int pos = ContainerManager.componentIndex(container, componentForComponent(container, beforeComponent), componentForComponent(container, component));
			JMenuItem newItem = isJMenu(container) ? ((JMenu) container).add((String) component) : ((JPopupMenu) container).add((String) component);
			if (pos != -1) {
				// Need to add back again in correct index. 
				container.add(newItem, pos);
			}
		} else if (component instanceof Action) {
			// Need to find out where it should go, add to the end (since can't add action at position) and then move it.
			int pos = ContainerManager.componentIndex(container, componentForComponent(container, beforeComponent), componentForComponent(container, component));
			JMenuItem newItem = isJMenu(container) ? ((JMenu) container).add((Action) component) : ((JPopupMenu) container).add((Action) component);
			if (pos != -1) {
				// Need to add back again in correct index. 
				container.add(newItem, pos);
			}
		}
	}
	
	/**
	 * Remove the component
	 * @param container container to remove from.
	 * @param component component, string, or action to remove. 
	 * 
	 * @since 1.1.0
	 */
	public static void removeComponent(Container container, Object component) {
		ContainerManager.removeComponent(container, componentForComponent(container, component));
	}
	
	
	/**
	 * Return the true awt.Component for the incoming component
	 * @param container contaienr to look into.
	 * @param component component to look for. Can be component, action, or string. <code>null</code> is valid and so will return null
	 * @return <code>null</code> if component not found, or awt.Component for the component.
	 * 
	 * @since 1.1.0
	 */
	protected static Component componentForComponent(Container container, Object component) {
		if (component == null)
			return null;
		if (component instanceof Component)
			return (Component) component;
		if (component instanceof String) {
			Component[] components = isJMenu(container) ? ((JMenu) container).getMenuComponents() : ((JPopupMenu) container).getComponents();
			String s = (String) component;
			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof JMenuItem) {
					JMenuItem menuitem = (JMenuItem)components[i];
					if (menuitem.getText().equals(s))
						return menuitem;
				}
			}
			return null;
		}
		if (component instanceof Action) {
			Component[] components = isJMenu(container) ? ((JMenu) container).getMenuComponents() : ((JPopupMenu) container).getComponents();
			Action a = (Action) component;
			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof JMenuItem) {
					JMenuItem menuitem = (JMenuItem)components[i];
					if (menuitem.getAction() == a)
						return menuitem;
				}
			}
			return null;			
		}
		
		return null;
	}	
}
