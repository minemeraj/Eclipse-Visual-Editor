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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: PaletteToolbarDropDownAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

import java.util.Iterator;

import org.eclipse.gef.Disposable;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * This class is used to create the drop-down action on the tool bar.
 * Typically the Choose selection item will be the ToolEntryAction that
 * is directly exposed on the toolbar.
 */
public class PaletteToolbarDropDownAction extends PaletteToolEntryAction implements Disposable, IMenuCreator {
	
	protected PaletteRoot paletteRoot;
	protected MenuManager toolbarMenuManager;

	/**
	 * 
	 */
	public PaletteToolbarDropDownAction() {
		super(IAction.AS_DROP_DOWN_MENU);
		setEnabled(false);
	}

	public PaletteToolbarDropDownAction(EditDomain editDomain) {
		super(IAction.AS_DROP_DOWN_MENU);
		setEditDomain(editDomain);
		setEnabled(false);	// Until we have a tool entry
	}
	
	public void setPaletteRoot(PaletteRoot paletteRoot) {
		this.paletteRoot = paletteRoot;
		if (toolbarMenuManager != null)
			initializeToolbarMenuManager();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		if (toolbarMenuManager != null) {
			disposeToolbarMenuManager();
		}
	}

	protected void disposeToolbarMenuManager() {
		toolbarMenuManager.dispose();
		toolbarMenuManager = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		if (toolbarMenuManager == null)
			initializeToolbarMenuManager();
		return toolbarMenuManager.createContextMenu(parent);
	}
	
	protected void initializeToolbarMenuManager() {
		if (toolbarMenuManager == null)
			toolbarMenuManager = new MenuManager();
		else
			toolbarMenuManager.removeAll();	// Clear it out so we can refill it.
			
		Iterator iter = paletteRoot.getChildren().iterator();
		// Groups at the root level will not be added to the categories. This is typically the "control" group and will be right on the toolbar/menu.
		while (iter.hasNext()) {
			Object paletteChild = iter.next();
			if (paletteChild instanceof PaletteGroup)
				continue;
			else if (paletteChild instanceof PaletteDrawer) {
				// Recurse through the items on the container
				addContainerToMenu((PaletteContainer) paletteChild, toolbarMenuManager);
			}
		}
	}
	
	protected void addToolEntryToMenu(ToolEntry toolEntry, IMenuManager aMenuManager) {
		PaletteToolEntryAction entryAction = new PaletteToolEntryAction(this, toolEntry);
		aMenuManager.add(entryAction);
	}
	protected void addContainerToMenu(PaletteContainer container, IMenuManager menuManager) {
		if (container instanceof PaletteDrawer) {
			// Create a new MenuManager which will appear as a sub menu
			IMenuManager parentMenuManager = menuManager;	
			menuManager = new MenuManager(container.getLabel());			
			parentMenuManager.add(menuManager);
		} else
			menuManager.add(new Separator());	// It's a group, so just add separator, and rest will be on same menu.
		Iterator entries = container.getChildren().iterator();
		while (entries.hasNext()) {
			Object entry = entries.next();
			if (entry instanceof ToolEntry) {
				addToolEntryToMenu((ToolEntry) entry, menuManager);
			} else if (entry instanceof PaletteContainer) {
				addContainerToMenu((PaletteContainer) entry, menuManager);
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#getMenuCreator()
	 */
	public IMenuCreator getMenuCreator() {
		return this;
	}

}
