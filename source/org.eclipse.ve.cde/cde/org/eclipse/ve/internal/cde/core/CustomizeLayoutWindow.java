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
/*
 * Created on Apr 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CustomizeLayoutWindow.java,v $
 *  $Revision: 1.15 $  $Date: 2005-12-14 21:27:11 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IEditorPart;

/**
 * The window that parents all of the tabs for alignment controls. It works closely with
 * CustomizeLayoutWindowAction.
 * 
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed or instantiated outside of this package.
 * </p>
 */
public class CustomizeLayoutWindow extends Window {
	
	protected List layoutClasses = new ArrayList(2);
	protected List layoutPages = new ArrayList(2);
	protected List layoutControls = new ArrayList(2);
	protected List componentClasses = new ArrayList(2);	// List of CustomizeLayoutPage classes that are on this window.
	protected List componentPages = new ArrayList(2);			// List of CustomizeLayoutPage instances that are on this window. They correspond to componentClasses.
	protected List componentControls = new ArrayList(2);
	
	protected TabFolder tabFolder;
	
	protected Composite layoutPage;
	protected StackLayout layoutPageLayout;
	protected Composite componentPage;
	protected StackLayout componentPageLayout;
	
	protected Composite noLayoutPage;
	protected Composite noComponentPage;
	
	private Point location;
	protected CustomizeLayoutWindowAction windowAction;
	
	protected Shell parentShell;
	protected CustomizeLayoutPage selectedPage = null;

	/**
	 * Create the CustomizeLayoutWindow. It will be parented to the given shell whenever the 
	 * window is opened.
	 * 
	 * @param parentShell
	 */
	public CustomizeLayoutWindow(Shell parentShell, CustomizeLayoutWindowAction windowAction) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE);	
		this.windowAction = windowAction;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		setTitle(null);
	}
	
	protected void setTitle(String description) {
		Shell shell = getShell();
		if (shell == null || shell.isDisposed()) {
			return;
		}
		if (description == null) {
			shell.setText(CDEMessages.CustomizeLayoutWindow_title); 
		} else {
			shell.setText(CDEMessages.CustomizeLayoutWindow_title + " - " + description);  //$NON-NLS-1$
		}
	}

	protected Point getInitialSize() {
		Point initSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		// Set it to a minimum. If above size is bigger, great.
		if (initSize.x < 100)
			initSize.x = 100;
		if (initSize.x < 90)
			initSize.y = 90;
		return initSize;
	}
	
	protected Point getInitialLocation(Point initialSize) {
		if (getLocation() == null)
			return super.getInitialLocation(initialSize);
		
		Rectangle displayBounds = getShell().getDisplay().getClientArea();
		Point loc = getLocation();
		if (loc.x < displayBounds.x)
			loc.x = displayBounds.x;
		if (loc.y < displayBounds.y)
			loc.y = displayBounds.y;		
		if (loc.x+initialSize.x > displayBounds.x+displayBounds.width)
			loc.x = displayBounds.x+displayBounds.width - initialSize.x;
		if (loc.y+initialSize.y > displayBounds.y+displayBounds.height)
			loc.y = displayBounds.y+displayBounds.height - initialSize.y;
		// Check again in case the above moved off-screen to the left.
		if (loc.x < displayBounds.x)
			loc.x = displayBounds.x;
		if (loc.y < displayBounds.y)
			loc.y = displayBounds.y;		
		setLocation(loc);
		return loc;
	}
		
	protected Control createContents(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));	
		
		// Create the Layout tab
		TabItem layoutTab = new TabItem(tabFolder, SWT.NONE);
		layoutTab.setText(CDEMessages.CustomizeLayoutWindow_layoutTabTitle); 
		layoutTab.setToolTipText(CDEMessages.CustomizeLayoutWindow_layoutTabToolTip); 
		
		layoutPage = new Composite(tabFolder, SWT.NONE);
		layoutPageLayout = new StackLayout();
		layoutPage.setLayout(layoutPageLayout);
		layoutTab.setControl(layoutPage);
		
		// Create the Component tab
		TabItem componentTab = new TabItem(tabFolder, SWT.NONE);
		componentTab.setText(CDEMessages.CustomizeLayoutWindow_componentTabTitle); 
		componentTab.setToolTipText(CDEMessages.CustomizeLayoutWindow_componentTabToolTip); 
		
		componentPage = new Composite(tabFolder, SWT.NONE);
		componentPageLayout = new StackLayout();
		componentPage.setLayout(componentPageLayout);
		componentTab.setControl(componentPage);
		
		// Create the no Layout page
		noLayoutPage = new Composite(layoutPage, SWT.NONE);
		noLayoutPage.setLayout(new GridLayout());
		Label noLayoutLabel = new Label(noLayoutPage, SWT.WRAP);
		noLayoutLabel.setText(CDEMessages.CustomizeLayoutWindow_noLayoutText); 
		GridData gd = new GridData();
		gd.widthHint = 200;
		noLayoutLabel.setLayoutData(gd);
		layoutPageLayout.topControl = noLayoutPage;
		layoutPage.layout();
		
		// Create the no Component page
		noComponentPage = new Composite(componentPage, SWT.NONE);
		noComponentPage.setLayout(new GridLayout());
		Label noComponentLabel = new Label(noComponentPage, SWT.WRAP);
		noComponentLabel.setText(CDEMessages.CustomizeLayoutWindow_noComponentText); 
		gd = new GridData();
		gd.widthHint = 200;
		noComponentLabel.setLayoutData(gd);
		componentPageLayout.topControl = noComponentPage;
		componentPage.layout();
		
		// Add the layout pages
		for (int i = 0; i < layoutPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) layoutPages.get(i);
			if (page == null) {
				try {
					Class pageClass = (Class)layoutClasses.get(i);
					page = createPage(pageClass);
					layoutPages.set(i, page);
				} catch (CoreException e) {
					CDEPlugin.getPlugin().getLog().log(e.getStatus());
					// Remove it from the list so we don't try again.
					layoutPages.remove(i);
					layoutClasses.remove(i);
					i--;	// Backup one because next guy has moved into this slot.
					continue;
				}
			}
		}
		
		// Add the component pages
		for (int i = 0; i < componentPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) componentPages.get(i);
			if (page == null) {
				try {
					Class pageClass = (Class)componentClasses.get(i);
					page = createPage(pageClass);
					componentPages.set(i, page);
				} catch (CoreException e) {
					CDEPlugin.getPlugin().getLog().log(e.getStatus());
					// Remove it from the list so we don't try again.
					layoutPages.remove(i);
					layoutClasses.remove(i);
					i--;	// Backup one because next guy has moved into this slot.
					continue;
				}
			}
		}
		
		tabFolder.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				tabFolder = null;
			}
		});
		Dialog.applyDialogFont(tabFolder);
		return tabFolder;
	}
	
	protected Control createPageControl(Composite parent, CustomizeLayoutPage page) {
		Control control = null;
		if (parent != null && !parent.isDisposed()) {
			control = page.getControl(parent);
			Dialog.applyDialogFont(control);
			parent.layout();	// re-layout to adjust for font changes
			
			// Now resize to handle the new page.
			Point size = getInitialSize();
			Point location = getInitialLocation(size);

			getShell().setBounds(location.x, location.y, size.x, size.y);
		}
		return control;
	}
	
	protected CustomizeLayoutPage createPage(Class pageClass) throws CoreException {
		try {
			CustomizeLayoutPage page = (CustomizeLayoutPage) pageClass.newInstance();
			page.setSelectionProvider(windowAction.getSelectionProvider());
			page.setEditorPart(editorPart);
			page.update(selection);
			return page;			
		} catch (InstantiationException e) {
			throw new CoreException(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			throw new CoreException(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e)); //$NON-NLS-1$
		}
	}
	
	/**
	 * Hide the shell, if active.
	 */
	public void hide() {
		Shell shell = getShell();
		if (shell != null)
			shell.setVisible(false); 
	}
	
	public boolean isHidden() {
		Shell shell = getShell();
		return (shell != null) ? !shell.isVisible() : true; 		
	}
	
	/*
	 * Add page customiztaion classes to the list handled by this window.
	 * As long as this instance exists, the list will only grow. It won't shrink.
	 * 
	 * @param tabPageClass
	 */
	protected void addCustomizationPages(Class layoutPageClass, Class componentPageClass) {
		addLayoutCustomizationPage(layoutPageClass);
		addComponentCustomizationPage(componentPageClass);
	}
	
	protected void addLayoutCustomizationPage(Class layoutPageClass) {
		if (layoutPageClass != null && !layoutClasses.contains(layoutPageClass)) {		
			layoutClasses.add(layoutPageClass);
			if (layoutPage != null) {
				// We have a tab folder, so create the class and page.
				int i = layoutPages.size();	// Last guy added is this guy.
				try {
					CustomizeLayoutPage page = createPage(layoutPageClass);
					layoutPages.add(i, page);
					layoutControls.add(i, null);
				} catch (CoreException e) {
					CDEPlugin.getPlugin().getLog().log(e.getStatus());
					// Remove it from the list so we don't try again.
					layoutPages.remove(i);
					layoutClasses.remove(i);
				} 
			}
		}
	}
	
	protected void addComponentCustomizationPage(Class componentPageClass) {
		if (componentPageClass != null && !componentClasses.contains(componentPageClass)) {		
			componentClasses.add(componentPageClass);
			if (componentPage != null) {
				// We have a tab folder, so create the class and page.
				int i = componentPages.size();	// Last guy added is this guy.
				try {
					CustomizeLayoutPage page = createPage(componentPageClass);
					componentPages.add(i, page);
					componentControls.add(i, null); 
				} catch (CoreException e) {
					CDEPlugin.getPlugin().getLog().log(e.getStatus());
					// Remove it from the list so we don't try again.
					componentPages.remove(i);
					componentClasses.remove(i);
				} 
			}
		}
	}
	
	protected ISelection selection = StructuredSelection.EMPTY;
	/**
	 * Set a new selection into the window and any current tab pages.
	 * @param selection
	 */
	public void update(ISelection selection) {
		this.selection = selection;
		boolean found = false;
		boolean isLayout = false;
		CustomizeLayoutPage lPage = null, cPage = null;
		for (int i = 0; i < layoutPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) layoutPages.get(i);
			if (page != null)
				found = page.update(selection);
			if (found) {
				Control control = (Control)layoutControls.get(i);
				if (control == null) {
					control = createPageControl(layoutPage, page);
					layoutControls.set(i, control);
				}
				layoutPageLayout.topControl = control;
				layoutPage.layout();
				lPage = page;
				// If the selection is a container and this is the container's
				// layout, stop looking through the available pages.
				if (page.selectionIsContainer(selection)) {
					isLayout = true;
					break;
				}
			}
		}
		if (!found) {
			lPage = null;
			layoutPageLayout.topControl = noLayoutPage;
			layoutPage.layout();
		}
		
		found = false;
		for (int i = 0; i < componentPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) componentPages.get(i);
			if (page != null)
				found = page.update(selection);
			if (found) {
				Control control = (Control)componentControls.get(i);
				if (control == null) {
					control = createPageControl(componentPage, page);
					componentControls.set(i, control);
				}
				componentPageLayout.topControl = control;
				componentPage.layout();
				cPage = page;
				break;
			}
		}
		if (!found) {
			cPage = null;
			componentPageLayout.topControl = noComponentPage;
			componentPage.layout();
		}
		
		// if the layout page is a container or there isn't a component page
		if (isLayout || !found) {
			// Set the layout tab to the top
			if (tabFolder.getSelectionIndex() != 0)
				tabFolder.setSelection(0);
			if (lPage != null) {
				setTitle(lPage.getLabelForSelection(selection));
			}
		} else {
			// else set the component tab to the top
			if (tabFolder.getSelectionIndex() != 1)
				tabFolder.setSelection(1);
			if (cPage != null) {
				setTitle(cPage.getLabelForSelection(selection));
			}
		}
		
		if (lPage == null && lPage == null) {
			setTitle(null);
		}
	}
	
	/**
	 * Called when model has changed and all pages should refresh themselves.
	 * 
	 * 
	 * @since 1.2.0
	 */
	public void refresh() {
		for (int i = 0; i < layoutPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) layoutPages.get(i);
			if (page != null)
				page.refresh();
		}
	}
	
	protected IEditorPart editorPart;
	/**
	 * Set a new editor part into the window and any current tab pages.
	 * 
	 * @param editorPart new editor part (null if no editor)
	 */
	public void setEditorPart(IEditorPart editorPart) {
		this.editorPart = editorPart;

		if (editorPart != null) {
			// Need to get all of the tabs for this editor so that we can add if necessary.
			EditPartViewer primaryViewer =
				(EditPartViewer) editorPart.getAdapter(EditPartViewer.class);
			if (primaryViewer != null) {
				Object odom = primaryViewer.getEditDomain();
				if (odom instanceof EditDomain) {
					EditDomain dom = (EditDomain) odom;
					CustomizeLayoutWindowAction.CustomizeLayoutPageController pages =
						(
							CustomizeLayoutWindowAction
								.CustomizeLayoutPageController) dom
								.getViewerData(
							primaryViewer,
							CustomizeLayoutWindowAction.CUSTOMIZE_LAYOUT_PAGE_KEY);
					if (pages != null) {
						pages.setCustomizeLayoutWindowAction(windowAction);
						// So that it can come back and tell us of new tabs later.
						List layoutClasses = pages.getLayoutClasses();
						for (int i = 0; i < layoutClasses.size(); i++) {
							addCustomizationPages((Class) layoutClasses.get(i), null);
						}
						List componentClasses = pages.getComponentClasses();
						for (int i = 0; i < componentClasses.size(); i++) {
							addCustomizationPages(null, (Class) componentClasses.get(i));
						}
					}
				}
			}
		}
		
		for (int i = 0; i < layoutPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) layoutPages.get(i);
			if (page != null)
				page.setEditorPart(editorPart);
		}
		for (int i = 0; i < componentPages.size(); i++) {
			CustomizeLayoutPage page = (CustomizeLayoutPage) componentPages.get(i);
			if (page != null)
				page.setEditorPart(editorPart);
		}
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}


}
