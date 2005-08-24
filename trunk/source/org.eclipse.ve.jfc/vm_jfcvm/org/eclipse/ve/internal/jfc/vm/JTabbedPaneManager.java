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
 *  $RCSfile: JTabbedPaneManager.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:13 $ 
 */

import java.awt.Component;

import javax.swing.*;

/**
 * This is the manager for a javax.swing.JTabbedPane. It allows inserting tabs before a particular component, plus access through component. It's
 * purpose is to hide indexes and instead work on components themselves. This is because the index of the component back in JBCF may not necessarily
 * be the index within the live component. This is because of components that couldn't be instantiated due to errors, or if the container was a
 * subclass, there could be components already in the container.
 * <p>
 * <b>Note: </b> It is static. No need to have one for each tabbed pane. The tabbed pane will always be passed in.
 */
public class JTabbedPaneManager {

	/**
	 * Insert the tab before the given component. If component is null, then fluff up a component and return that component. If component is not null,
	 * then return component that was sent in. This returned component is used by the IDE to be able to find the tab in the future since indexes are
	 * not useful in VE since there could be tabs already inserted from a superclass.
	 * 
	 * @param tabbedPane
	 * @param title
	 * @param icon
	 * @param component
	 *            the component to add or <code>null</code> to fluff up a component on the fly. If null, this component is used as a placeholder.
	 * @param beforeComponent
	 *            go before this component. If this component is not found or is <code>null</code>, then insert at the end.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static Component insertTabBefore(JTabbedPane tabbedPane, String title, Icon icon, Component component, Component beforeComponent) {
		int pos = beforeComponent != null ? tabbedPane.indexOfComponent(beforeComponent) : -1;
		if (component == null) {
			// We need to fluff one up.
			component = new JPanel();
		}
		if (pos != -1)
			tabbedPane.insertTab(title, icon, component, null, pos);
		else
			tabbedPane.addTab(title, icon, component, null);
		return component;
	}
	
	/**
	 * Insert tab with component before the beforeComponent (if not null and found, else insert at end). This is used
	 * only for add(Component) and add(Component, index). Title will default to component name. Component cannot be
	 * null in this case, so no need to return a fluffed up component.
	 * 
	 * @param tabbedPane
	 * @param component
	 * @param beforeComponent go before this component. If this component is not found or is <code>null</code>, then insert at the end.
	 * 
	 * @since 1.1.0
	 */
	public static void insertTabBefore(JTabbedPane tabbedPane, Component component, Component beforeComponent) {
		int pos = beforeComponent != null ? tabbedPane.indexOfComponent(beforeComponent) : -1;
		if (pos != -1)
			tabbedPane.add(component, pos);
		else
			tabbedPane.add(component);
	}

	/**
	 * Set the icon for the given tab.
	 * 
	 * @param tabbedPane
	 * @param component
	 * @param icon
	 * 
	 * @since 1.1.0
	 */
	public static void setIconAt(JTabbedPane tabbedPane, Component component, Icon icon) {
		int pos = tabbedPane.indexOfComponent(component);
		if (pos != -1)
			tabbedPane.setIconAt(pos, icon);
	}

	/**
	 * Set the title for the given tab.
	 * 
	 * @param tabbedPane
	 * @param component
	 * @param title
	 * 
	 * @since 1.1.0
	 */
	public static void setTitleAt(JTabbedPane tabbedPane, Component component, String title) {
		int pos = tabbedPane.indexOfComponent(component);
		if (pos != -1)
			tabbedPane.setTitleAt(pos, title);
	}
	
	/**
	 * Set the title for the given tab. Use the component.getName() as the title.
	 * 
	 * @param tabbedPane
	 * @param component
	 * 
	 * @since 1.1.0
	 */
	public static void setDefaultTitle(JTabbedPane tabbedPane, Component component) {
		String title = component.getName();
		setTitleAt(tabbedPane, component, title != null ? title : ""); //$NON-NLS-1$
	}

}
