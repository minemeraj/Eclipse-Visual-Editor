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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CustomizeLayoutWindowAction.java,v $
 *  $Revision: 1.14 $  $Date: 2005-09-13 18:56:04 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.*;

/**
 * This action on the toolbar controls whether the customize layout dialog is up or not.
 */
public class CustomizeLayoutWindowAction extends Action implements IMenuCreator {

	public static String ACTION_ID = "CustomizeLayoutWindowAction"; //$NON-NLS-1$
	protected static Point fDialogLoc;
	protected CustomizeLayoutWindow fDialog;
	protected IWorkbenchWindow workbenchWindow;
	protected IEditorPart editorPart;
	protected IEditorActionBarContributor contributor;
	protected MenuManager toolbarMenuManager;	
	
	protected final static String WINDOW_TITLE = CDEMessages.CustomizeLayoutWindow_title; 
	
	protected static final String CUSTOMIZE_LAYOUT_PAGE_KEY = "customizeLayoutPage_Key"; //$NON-NLS-1$

	public static final ImageDescriptor IMG_CUSTOMIZE_LAYOUT_BEAN_DISABLED = CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/aligndialog_obj.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMG_CUSTOMIZE_LAYOUT_BEAN = CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/aligndialog_obj.gif"); //$NON-NLS-1$
	
	
	/**
	 * Add an Layout customization page class to the viewer. The page will be brought to front on the Layout tab whenever it's 
	 * handleSelectionChanged() method returns true on a given selection.
	 * 
	 * @param layoutPage Layout tab's pageClass to add (if already there, not added twice). Must be a subclass of CustomizeLayoutPage.
	 * @param viewer EditPartViewer to add to.
	 */
	public static void addLayoutCustomizationPage(EditPartViewer viewer, Class layoutPage) {
		Object odom = viewer.getEditDomain();
		if (odom instanceof EditDomain) {
			EditDomain dom = (EditDomain) odom;
			if (!CustomizeLayoutPage.class.isAssignableFrom(layoutPage))
				throw new IllegalArgumentException(layoutPage.toString());		
			CustomizeLayoutPageController pages = (CustomizeLayoutPageController) dom.getViewerData(viewer, CUSTOMIZE_LAYOUT_PAGE_KEY);
			if (pages == null) {
				pages = new CustomizeLayoutPageController();
				dom.setViewerData(viewer, CUSTOMIZE_LAYOUT_PAGE_KEY, pages);
			}
			pages.addLayoutPage(layoutPage);
		}
	}
	
	/**
	 * Add an Component customization page class to the viewer. The page will be brought to front on the Component tab whenever it's 
	 * handleSelectionChanged() method returns true on a given selection.
	 * 
	 * @param componentPage Component tab's pageClass to add (if already there, not added twice). Must be a subclass of CustomizeLayoutPage.
	 * @param viewer EditPartViewer to add to.
	 */
	public static void addComponentCustomizationPage(EditPartViewer viewer, Class componentPage) {
		Object odom = viewer.getEditDomain();
		if (odom instanceof EditDomain) {
			EditDomain dom = (EditDomain) odom;
			if (!CustomizeLayoutPage.class.isAssignableFrom(componentPage))
				throw new IllegalArgumentException(componentPage.toString());		
			CustomizeLayoutPageController pages = (CustomizeLayoutPageController) dom.getViewerData(viewer, CUSTOMIZE_LAYOUT_PAGE_KEY);
			if (pages == null) {
				pages = new CustomizeLayoutPageController();
				dom.setViewerData(viewer, CUSTOMIZE_LAYOUT_PAGE_KEY, pages);
			}
			pages.addComponentPage(componentPage);
		}
	}
	
	protected static class CustomizeLayoutPageController {
		private CustomizeLayoutWindowAction customizeLayoutWindowAction;
		private List layoutClasses = new ArrayList(1);
		private List componentClasses = new ArrayList(1);

		public void setCustomizeLayoutWindowAction(CustomizeLayoutWindowAction alignWindowAction) {
			this.customizeLayoutWindowAction = alignWindowAction;
		}

		public CustomizeLayoutWindowAction getCustomizeLayoutWindowAction() {
			return customizeLayoutWindowAction;
		}

		public List getLayoutClasses() {
			return layoutClasses;
		}

		public List getComponentClasses() {
			return componentClasses;
		}
		
		public void addLayoutPage(Class layoutClass) {
			if (layoutClass != null && !layoutClasses.contains(layoutClass)) {
				layoutClasses.add(layoutClass);
				if (getCustomizeLayoutWindowAction() != null) {
					getCustomizeLayoutWindowAction().updateLayoutTab(layoutClass);
				}
			}
		}
		
		public void addComponentPage(Class componentClass) {
			if (componentClass != null && !componentClasses.contains(componentClass)) {
				componentClasses.add(componentClass);
				if (getCustomizeLayoutWindowAction() != null) {
					getCustomizeLayoutWindowAction().updateComponentTab(componentClass);
				}
			}
		}
	}
	
	/**
	 * Used by OpenCustomLayoutObjectActionDelegate to open the 
	 * layout window when selected from popup menu.
	 * 
	 * @since 1.0.0
	 */
	void showCustomizeLayoutWindow() {
		if (fDialog == null){
			run();
		} else {
			fDialog.getShell().forceActive();
		}
	}
	
	/*
	 * This listener is so we know when we've switched editors. It needs to know
	 * all editors because if switching to and editor on a different contributor,
	 * we want to hide the alignment window. We also need to send editor to
	 * alignment dialog when switching to one within our contributor.
	 */
	private IPartListener alignmentWindowPartListener = new IPartListener() {
		private boolean currentEPBeingClosed = false;
		public void partActivated(IWorkbenchPart part) {
			if (part instanceof IEditorPart) {
				currentEPBeingClosed = false;
				setEditorPart((IEditorPart) part);
			} else if (currentEPBeingClosed) {
				// We are switching to some non editor part, and our previous editor was closed, so
				// need to clear out the editor part from the tab pages so they don't hold onto them.
				currentEPBeingClosed = false;
				setEditorPart(null);
			}
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
			if (part == editorPart)
				currentEPBeingClosed = true; 
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};
	
	private ISelectionListener selListener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (fDialog != null && !fDialog.isHidden())
				update(selection);			
		}
	};
	
	public CustomizeLayoutWindowAction(IWorkbenchWindow workbenchWindow, IEditorActionBarContributor contributor) {
		super(CDEMessages.CustomizeLayoutWindowAction_label, IAction.AS_PUSH_BUTTON); 
		setId(ACTION_ID);
		setImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/aligndialog_obj.gif")); //$NON-NLS-1$
		setDisabledImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/aligndialog_obj.gif")); //$NON-NLS-1$
		setHoverImageDescriptor(getImageDescriptor());
			
		this.addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (IAction.CHECKED.equals(event.getProperty()))
					setTooltip();
			}
		});	
		setTooltip();		
		this.workbenchWindow = workbenchWindow;
		this.contributor = contributor;
		workbenchWindow.getSelectionService().addSelectionListener(selListener);
		workbenchWindow.getPartService().addPartListener(alignmentWindowPartListener);
	}
	
	private void setTooltip() {
		setToolTipText(isChecked() ? CDEMessages.CustomizeLayoutWindowAction_tooltip_hide : CDEMessages.CustomizeLayoutWindowAction_tooltip_show); 
	}
		
	protected void updateLayoutTab(final Class layoutPageClass) {
		// Need to farm off to ui thread because this will cause a UI update.
		workbenchWindow.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fDialog != null)
					fDialog.addLayoutCustomizationPage(layoutPageClass);
			}
		});
	}
	
	protected void updateComponentTab(final Class componentPageClass) {
		// Need to farm off to ui thread because this will cause a UI update.
		workbenchWindow.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fDialog != null)
					fDialog.addComponentCustomizationPage(componentPageClass);
			}
		});		
	}
	
	/*
	 * When editor part switches, this is the new editor part.
	 * @param anEditorPart
	 */
	protected void setEditorPart(IEditorPart anEditorPart) {
		if (editorPart == anEditorPart)
			return;

		editorPart = anEditorPart;
		if (editorPart != null && editorPart.getEditorSite().getActionBarContributor() == contributor && fDialog != null) {
			runWithoutUpdate(); // Bring it up if necessary
		} else if (fDialog != null) {
			// Switching to no editor or to an editor that is not of interest, so hide the dialog. 
			// Also set no editor into dialog so that the editor is not held onto while hidden or gone.
			fDialog.hide();
			fDialog.setEditorPart(null);
			update(StructuredSelection.EMPTY);
		}
		editorPart.getSite().getSelectionProvider().addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				fLayoutList = null;
				ISelection selection = event.getSelection();
				if(selection instanceof IStructuredSelection){
					Object firstElement = ((IStructuredSelection)selection).getFirstElement();
					if(firstElement instanceof EditPart){
						LayoutList layoutList = (LayoutList) ((EditPart)firstElement).getAdapter(LayoutList.class);
						if(layoutList != null){
							fLayoutList = layoutList;
						}
					}
				}				
			}
		});
	}
	
	private ISelectionProvider selectionProvider = new ISelectionProvider() {
		private ISelection selection = StructuredSelection.EMPTY;
		
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
		}
		
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		}

		public ISelection getSelection() {
			return selection;
		}
		
		public void setSelection(ISelection selection) {
			this.selection = selection;
		}
	};
	private LayoutList fLayoutList;

	/*
	 * This is <package-protected> because only CustomizeLayoutWindow should access it.
	 */
	ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}
	
	/**
	 * When action is going away, it is important that this
	 * dispose method is called so that it can clean up.
	 */
	public void dispose() {
		if(workbenchWindow != null){
			workbenchWindow.getSelectionService().removeSelectionListener(selListener);
			workbenchWindow.getPartService().removePartListener(alignmentWindowPartListener);
		}
		editorPart = null;
		contributor = null;
		workbenchWindow = null;
		
		if (fDialog != null) {
			fDialog.close();
			fDialog = null;
		}
	}
	public void run() {
		runWithoutUpdate();
		update(workbenchWindow.getSelectionService().getSelection());			
	}

	protected void runWithoutUpdate () {
		if (fDialog == null) {
			fDialog = new CustomizeLayoutWindow(workbenchWindow.getShell(), this);
			if (fDialogLoc == null) {
				// Get the persisted position of the dialog
				Preferences preferences = CDEPlugin.getPlugin().getPluginPreferences();
				int x = preferences.getInt(CDEPlugin.CUSTOMIZELAYOUTWINDOW_X);
				int y = preferences.getInt(CDEPlugin.CUSTOMIZELAYOUTWINDOW_Y);
				fDialog.setLocation(new Point(x, y));
				fDialogLoc = fDialog.getLocation();
			} else {
				fDialog.setLocation(fDialogLoc);
			}					
			fDialog.open();				
			fDialog.getShell().addControlListener(new ControlListener() {
				public void controlMoved(ControlEvent event) {
					fDialogLoc = fDialog.getShell().getLocation();
					fDialog.setLocation(fDialogLoc);
				}
				public void controlResized(ControlEvent event) {
				}
			});
			fDialog.getShell().addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					persistPreferences();
					fDialog = null;
					update(StructuredSelection.EMPTY);
				}
			});			
		} else {
			fDialog.open();	// Reshow it.
		}
		fDialog.setEditorPart(editorPart); // Reinitialize it
	}
	
protected void persistPreferences() {
	if (fDialogLoc != null) {
			Preferences preferences = CDEPlugin.getPlugin().getPluginPreferences();
			preferences.setValue(CDEPlugin.CUSTOMIZELAYOUTWINDOW_X, fDialogLoc.x);
			preferences.setValue(CDEPlugin.CUSTOMIZELAYOUTWINDOW_Y, fDialogLoc.y);
		}
	}
	
	protected void update(ISelection selection) {
		selectionProvider.setSelection(selection);		
		if (fDialog != null) {
			fDialog.update(selection);
		}
	}
	
	public IMenuCreator getMenuCreator() {
		return this;
	}

	public Menu getMenu(Control parent) {
		initializeToolbarMenuManager();
		return  toolbarMenuManager.createContextMenu(parent);
	}

	public Menu getMenu(Menu parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void initializeToolbarMenuManager(){
		if (toolbarMenuManager == null)
			toolbarMenuManager = new MenuManager();
		else
			toolbarMenuManager.removeAll();	// Clear it out so we can refill it.
		
		if(fLayoutList != null){
			fLayoutList.fillMenuManager(toolbarMenuManager);
		}		
	}

}
