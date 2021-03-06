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
 *  $Revision: 1.22 $  $Date: 2005-12-14 19:07:15 $ 
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

import org.eclipse.ve.internal.cde.core.ModelChangeController.ModelChangeEvent;

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
			update(selection);			
		}
	};
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
	
	public CustomizeLayoutWindowAction(IWorkbenchWindow workbenchWindow, IEditorActionBarContributor contributor) {
		super(CDEMessages.CustomizeLayoutWindowAction_label, IAction.AS_PUSH_BUTTON); 
		this.workbenchWindow = workbenchWindow;
		this.contributor = contributor;
		
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
		workbenchWindow.getSelectionService().addSelectionListener(selListener);
		workbenchWindow.getPartService().addPartListener(alignmentWindowPartListener);
		
		toolbarMenuManager = new MenuManager();
		toolbarMenuManager.setRemoveAllWhenShown(true);
		toolbarMenuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				LayoutList layoutList = null;
				ISelection selection = CustomizeLayoutWindowAction.this.workbenchWindow.getSelectionService().getSelection();	// Get current selection
				if(selection instanceof IStructuredSelection){
					Object firstElement = ((IStructuredSelection)selection).getFirstElement();
					if(firstElement instanceof EditPart){
						layoutList = (LayoutList) ((EditPart)firstElement).getAdapter(LayoutList.class);
					}
				}				
				if(layoutList != null){
					layoutList.fillMenuManager(toolbarMenuManager);
				} else {
					// Add a no layouts available label.
					Action noAvailable = new Action(CDEMessages.CustomizeLayoutWindowAction_Action_NoLayouts) {
					};
					toolbarMenuManager.add(noAvailable);
				}
			}
		
		});
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
	
	protected ModelChangeController.ModelChangeListener changeListener = new ModelChangeController.ModelChangeListener() {

		public void transactionEvent(ModelChangeEvent event) {
			if (event.getEvent() == ModelChangeEvent.TRANSACTION_COMPLETED)
				if (fDialog != null && !fDialog.isHidden())
					fDialog.refresh();	// The model has changed, refire selection so everyone can refresh.
		}
	};
	/*
	 * When editor part switches, this is the new editor part.
	 * @param anEditorPart
	 */
	protected void setEditorPart(IEditorPart anEditorPart) {
		if (editorPart == anEditorPart)
			return;

		removeModelChangeController();
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
		
		if (editorPart != null) {
			org.eclipse.gef.EditDomain domain = (org.eclipse.gef.EditDomain) editorPart.getAdapter(org.eclipse.gef.EditDomain.class);
			if (domain instanceof EditDomain) {
				ModelChangeController changeController = (ModelChangeController) ((EditDomain) domain).getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
				if (changeController != null)
					changeController.addModelChangeListener(changeListener);
			}			
		}
	}

	private void removeModelChangeController() {
		if (editorPart != null) {
			org.eclipse.gef.EditDomain domain = (org.eclipse.gef.EditDomain) editorPart.getAdapter(org.eclipse.gef.EditDomain.class);
			if (domain instanceof EditDomain) {
				ModelChangeController changeController = (ModelChangeController) ((EditDomain) domain).getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
				if (changeController != null)
					changeController.removeModelChangeListener(changeListener);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		toolbarMenuManager.dispose();
	}
	
	/**
	 * When action is going away, it is important that this
	 * dispose method is called so that it can clean up. 
	 * <p>
	 * <b>NOTE:</b> This is different than the {@link #dispose()} method. The dispose
	 * is called often, everytime the menu dropdown is called. It is for cleaning up
	 * the menu. This method is called when the action itself is no longer needed, from
	 * the editor action contributor class.
	 */
	public void disposeAction() {
		workbenchWindow.getSelectionService().removeSelectionListener(selListener);
		workbenchWindow.getPartService().removePartListener(alignmentWindowPartListener);
		removeModelChangeController();
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

	/*
	 * Set the location using the current fDialogLoc, which is relative to the upper-left corner of the workbenchWindow.
	 */
	private void setDialogLoc() {
		Point shellLoc = workbenchWindow.getShell().getLocation();
		shellLoc.x+=fDialogLoc.x;
		shellLoc.y+=fDialogLoc.y;
		fDialog.setLocation(shellLoc);		
	}
	protected void runWithoutUpdate () {
		if (fDialog == null) {
			fDialog = new CustomizeLayoutWindow(workbenchWindow.getShell(), this);
			if (fDialogLoc == null) {
				// Get the persisted position of the dialog
				Preferences preferences = CDEPlugin.getPlugin().getPluginPreferences();
				fDialogLoc = new Point(preferences.getInt(CDEPlugin.CUSTOMIZELAYOUTWINDOW_X), preferences.getInt(CDEPlugin.CUSTOMIZELAYOUTWINDOW_Y));
			}
			setDialogLoc();
			fDialog.open();				
			fDialog.getShell().addControlListener(new ControlListener() {
				public void controlMoved(ControlEvent event) {
					fDialogLoc = fDialog.getShell().getLocation();
					fDialog.setLocation(fDialogLoc);	// So if closes and reopens it will be here.
					// Calculate offset for next time.
					Point shellLoc = workbenchWindow.getShell().getLocation();
					fDialogLoc.x-=shellLoc.x;
					fDialogLoc.y-=shellLoc.y;					
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
			// Save it as relative to the workbench window.
			preferences.setValue(CDEPlugin.CUSTOMIZELAYOUTWINDOW_X, fDialogLoc.x);
			preferences.setValue(CDEPlugin.CUSTOMIZELAYOUTWINDOW_Y, fDialogLoc.y);
		}
	}
	
	protected void update(ISelection selection) {
		selectionProvider.setSelection(selection);
		if (fDialog != null && !fDialog.isHidden()) {
			fDialog.update(selection);
		}
	}
	
	public IMenuCreator getMenuCreator() {
		return this;
	}

	public Menu getMenu(Control parent) {
		return  toolbarMenuManager.createContextMenu(parent);
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}

}
