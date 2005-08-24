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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JTabbedPaneSwitchToObjectActionDelegate.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */


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
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JTabbedPaneSwitchToObjectActionDelegate implements IObjectActionDelegate, IMenuCreator {

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

	private JTabbedPaneGraphicalEditPart tabPaneEditPart;
	private void setTabPaneEditPart(JTabbedPaneGraphicalEditPart tabPaneEditPart) {
		this.tabPaneEditPart = tabPaneEditPart;
	}

	private JTabbedPaneGraphicalEditPart getTabPaneEditPart() {
		return tabPaneEditPart;
	}

	private Action delegateAction;	// This is the delegate action that is wrappering this one.
	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (((IStructuredSelection) selection).size() != 1)
			action.setEnabled(false);	// Can only handle one tab pane at a time.
		else {
			if (action instanceof Action) {
				if (delegateAction != action) {
					delegateAction = (Action) action;
					delegateAction.setMenuCreator(this);
				}
				setTabPaneEditPart((JTabbedPaneGraphicalEditPart) ((IStructuredSelection) selection).getFirstElement());
				action.setEnabled(getTabPaneEditPart().getChildren().size() > 1);
				
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
	 * Fill pull down menu with the pages of the JTabbedPane
	 */
	protected void fillMenu(Menu menu) {
		// First add the next/previous:
		createMenuForAction(menu, new JTabbedPaneNextObjectAction(getTabPaneEditPart()), Integer.MIN_VALUE);
		createMenuForAction(menu, new JTabbedPanePreviousObjectAction(getTabPaneEditPart()), Integer.MIN_VALUE);
				
		int size = getTabPaneEditPart().getChildren().size();
		if (size > 1) {
			(new Separator()).fill(menu, -1);
			int cp = getTabPaneEditPart().getCurrentPageIndex();	// So that we don't put it in the list.
			for (int i = 0; i < size; i++)
				if (i != cp)
					createMenuForAction(menu, new JTabbedPaneSelectPageObjectAction(getTabPaneEditPart(), i), i + 1);
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
