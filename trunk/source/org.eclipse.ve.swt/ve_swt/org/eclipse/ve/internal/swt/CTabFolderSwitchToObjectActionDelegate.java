/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CTabFolderSwitchToObjectActionDelegate.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:52:56 $ 
 */
package org.eclipse.ve.internal.swt;
 
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author richkulp
 *
 */
public class CTabFolderSwitchToObjectActionDelegate implements IObjectActionDelegate, IMenuCreator {

	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// We don't have a need for the active part.
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		// Never called because we become a menu.
	}

	private CTabFolderGraphicalEditPart cTabFolderEditPart;
	private void setCTabFolderEditPart(CTabFolderGraphicalEditPart cTabFolderEditPart) {
		this.cTabFolderEditPart = cTabFolderEditPart;
	}

	private CTabFolderGraphicalEditPart getCTabFolderEditPart() {
		return cTabFolderEditPart;
	}

	private Action delegateAction;	// This is the delegate action that is wrappering this one.
	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (((IStructuredSelection) selection).size() != 1)
			action.setEnabled(false);	// Can only handle one tab folder at a time.
		else {
			if (action instanceof Action) {
				if (delegateAction != action) {
					delegateAction = (Action) action;
					delegateAction.setMenuCreator(this);
				}
				setCTabFolderEditPart((CTabFolderGraphicalEditPart) ((IStructuredSelection) selection).getFirstElement());
				action.setEnabled(getCTabFolderEditPart().getChildren().size() > 1);
				
			} else {
				action.setEnabled(false);
			}
		}
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(Control)
	 */
	public Menu getMenu(Control parent) {
		return null;	// Should never be on a control, should be a submenu of a popup menu
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(Menu)
	 */
	public Menu getMenu(Menu parent) {
		// Create the new menu. The menu will get filled when it is about to be shown. see fillMenu(Menu).
		Menu menu = new Menu(parent);
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
				fillMenu(m);
			}
		});
		return menu;	
	}
	/**
	 * Fill pull down menu with the pages of the CTabFolder
	 */
	protected void fillMenu(Menu menu) {
		// First add the next/previous:
		createMenuForAction(menu, new CTabFolderNextObjectAction(getCTabFolderEditPart()), Integer.MIN_VALUE);
		createMenuForAction(menu, new CTabFolderPreviousObjectAction(getCTabFolderEditPart()), Integer.MIN_VALUE);
				
		int size = getCTabFolderEditPart().getChildren().size();
		if (size > 1) {
			(new Separator()).fill(menu, -1);
			int cp = getCTabFolderEditPart().getCurrentPageIndex();	// So that we don't put it in the list.
			for (int i = 0; i < size; i++)
				if (i != cp)
					createMenuForAction(menu, new CTabFolderSelectPageObjectAction(getCTabFolderEditPart(), i), i + 1);
		}
	}
	
	private void createMenuForAction(Menu parent, IAction action, int count) {
		// If count is min_value, then no accelerator is desired.
		if (count != Integer.MIN_VALUE && count < 10) {
			StringBuffer label= new StringBuffer();
			//add the numerical accelerator to item.
			if (count < 10) {
				label.append('&');
				label.append(count);
				label.append(' ');
			}
			label.append(action.getText());
			action.setText(label.toString());
		}
		ActionContributionItem item= new ActionContributionItem(action);
		item.fill(parent, -1);
	}	

}
