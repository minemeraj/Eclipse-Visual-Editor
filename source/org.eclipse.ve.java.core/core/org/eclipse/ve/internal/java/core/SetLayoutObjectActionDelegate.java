/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.ve.internal.cde.core.LayoutList;

public class SetLayoutObjectActionDelegate implements IObjectActionDelegate, IMenuCreator {
	
	private Action delegateAction;	// This is the delegate action that is wrappering this one.	
	private EditPart fEditPart;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// We don't need to know the active part
	}

	public void run(IAction action) {
		// We are never run as we drop down the menu
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (((IStructuredSelection) selection).size() != 1)
			action.setEnabled(false);	// Can only handle one composite at a time
		else {
			if (action instanceof Action) {
				if (delegateAction != action) {
					delegateAction = (Action) action;
					delegateAction.setMenuCreator(this);
				}
				setEditPart((EditPart) ((IStructuredSelection) selection).getFirstElement());				
				action.setEnabled(true);
			} else {
				action.setEnabled(false);
			}
		}
	}
	
	private void setEditPart(EditPart anEditPart){
		fEditPart = anEditPart;
	}

	public void dispose() {
	}

	public Menu getMenu(Menu parent) {
		// Create the new menu. The menu will get filled when it is about to be shown. see fillMenu(Menu).
		final Menu menu = new Menu(parent);
		/**
		 * Add listener to repopulate the menu each time
		 * it is shown because MenuManager.update(boolean, boolean) 
		 * doesn't dispose pulldown ActionContribution items for each popup menu.
		 */
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu m = (Menu)e.widget;
				MenuItem[] items = m.getItems();
				for (int i=0; i < items.length; i++) {
					items[i].dispose();
				}
				LayoutList layoutList = (LayoutList) fEditPart.getAdapter(LayoutList.class);
				if(layoutList != null){
					MenuManager menuManager = new MenuManager("Foo");
					layoutList.fillMenuManager(menuManager);
					IContributionItem[] actions = menuManager.getItems();
					for (int i = 0; i < actions.length; i++) {
						actions[i].fill(m,i);
					}
				}
			}
		});
		return menu;			
	}

	public Menu getMenu(Control parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
