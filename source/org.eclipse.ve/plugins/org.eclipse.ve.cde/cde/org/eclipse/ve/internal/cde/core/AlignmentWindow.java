/*
 * Created on Apr 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AlignmentWindow.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IEditorPart;

/**
 * The window that parents all of the tabs for alignment controls. It works closely with
 * AlignmentWindowAction.
 * 
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed or instantiated outside of this package.
 * </p>
 */
public class AlignmentWindow extends Window {
	
	protected List tabClasses = new ArrayList(2);	// List of AlignmentTabPage classes that are on this window.
	protected List tabs = new ArrayList(2);			// List of AlignmentTabPage instances that are on this window. They correspond to tabClasses.
	
	protected TabFolder tabFolder;
	private Point location;
	protected AlignmentWindowAction windowAction;

	/**
	 * Create the AlignmentWindow. It will be parented to the given shell whenever the 
	 * window is opened.
	 * 
	 * @param parentShell
	 */
	public AlignmentWindow(Shell parentShell, AlignmentWindowAction windowAction) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE);	
		this.windowAction = windowAction;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CDEMessages.getString("AlignmentWindow.title")); //$NON-NLS-1$
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
		if (loc.x+initialSize.x > displayBounds.x+displayBounds.width)
			loc.x = displayBounds.x+displayBounds.width - initialSize.x;
		if (loc.y+initialSize.y > displayBounds.y+displayBounds.height)
			loc.y = displayBounds.y+displayBounds.height - initialSize.y;
			
		setLocation(loc);
		return loc;
	}
		
	protected Control createContents(Composite parent) {
		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));		
		for (int i = 0; i < tabs.size(); i++) {
			AlignmentTabPage page = (AlignmentTabPage) tabs.get(i);
			if (page == null) {
				try {
					page = createTabPage(i);
				} catch (CoreException e) {
					CDEPlugin.getPlugin().getLog().log(e.getStatus());
					// Remove it from the list so we don't try again.
					tabs.remove(i);
					tabClasses.remove(i);
					i--;	// Backup one because next guy has moved into this slot.
					continue;
				}
			}
			createTabItem(page);
		}
		
		tabFolder.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				tabFolder = null;
			}
		});
		
		return tabFolder;
	}

	protected void createTabItem(AlignmentTabPage page) {
		TabItem tab = new TabItem(tabFolder, SWT.NONE);
		tab.setData(page);
		tab.setText(page.getText());
		tab.setToolTipText(page.getToolTipText());
		tab.setImage(page.getImage());
		if (tabFolder.getItemCount() == 1) {
			// This is the first tab page in the folder, add listener so that any follow on pages will be created,
			// and create this tab page.
			tabFolder.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					TabItem newSelectedTab = (TabItem) e.item;
					// This is the first time selected, so create the control for it.					
					createPageControl(newSelectedTab);
				}
			});
			
			createPageControl(tab);			
		}
	}
	
	private void createPageControl(TabItem tab) {
		if (tab != null && !tab.isDisposed() && tab.getControl() == null) {
			AlignmentTabPage page = (AlignmentTabPage) tab.getData();
			if (page != null) {
				tab.setControl(page.getControl(tabFolder));
				
				// Now resize to handle the new page.
				Point size = getInitialSize();
				Point location = getInitialLocation(size);

				getShell().setBounds(location.x, location.y, size.x, size.y);
			} 
		}
	}	
	
	protected AlignmentTabPage createTabPage(int i) throws CoreException {
		try {
			AlignmentTabPage page = (AlignmentTabPage) ((Class) tabClasses.get(i)).newInstance();
			page.setSelectionProvider(windowAction.getSelectionProvider());
			page.setEditorPart(editorPart);
			page.update(selection);
			tabs.set(i, page);
			return page;			
		} catch (InstantiationException e) {
			throw new CoreException(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e)); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			throw new CoreException(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e)); //$NON-NLS-1$
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
	 * Add a tab page class to the list handled by this window.
	 * As long as this instance exists, the list will only grow. It won't shrink.
	 * 
	 * @param tabPageClass
	 */
	protected void addTabPage(Class tabPageClass) {
		if (!tabClasses.contains(tabPageClass)) {		
			tabClasses.add(tabPageClass);
			tabs.add(null);	// Put in placeholder.
			if (tabFolder != null) {
				// We have a tab folder, so create the class and page.
				int i = tabClasses.size()-1;	// Last guy added is this guy.
				try {
					AlignmentTabPage page = createTabPage(i);
					createTabItem(page); 
				} catch (CoreException e) {
					CDEPlugin.getPlugin().getLog().log(e.getStatus());
					// Remove it from the list so we don't try again.
					tabs.remove(i);
					tabClasses.remove(i);
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
		for (int i = 0; i < tabs.size(); i++) {
			AlignmentTabPage page = (AlignmentTabPage) tabs.get(i);
			if (page != null)
				page.update(selection);
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
					AlignmentWindowAction.AlignmentTabController tabs =
						(
							AlignmentWindowAction
								.AlignmentTabController) dom
								.getViewerData(
							primaryViewer,
							AlignmentWindowAction.ALIGNMENT_TAB_KEY);
					if (tabs != null) {
						tabs.setAlignWindowAction(windowAction);
						// So that it can come back and tell us of new tabs later.
						List tabClasses = tabs.getTabClasses();
						for (int i = 0; i < tabClasses.size(); i++) {
							addTabPage((Class) tabClasses.get(i));
						}
					}
				}
			}
		}
		
		for (int i = 0; i < tabs.size(); i++) {
			AlignmentTabPage page = (AlignmentTabPage) tabs.get(i);
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
