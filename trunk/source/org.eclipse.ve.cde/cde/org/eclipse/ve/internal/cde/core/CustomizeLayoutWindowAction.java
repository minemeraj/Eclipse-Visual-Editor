package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CustomizeLayoutWindowAction.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-10 18:37:20 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.*;

/**
 * This action on the toolbar controls whether the customize layout dialog is up or not.
 */
public class CustomizeLayoutWindowAction extends Action {

	public static String ACTION_ID = "CustomizeLayoutWindowAction"; //$NON-NLS-1$
	protected static Point fDialogLoc;
	protected CustomizeLayoutWindow fDialog;
	protected IWorkbenchWindow workbenchWindow;
	protected IEditorPart editorPart;
	protected IEditorActionBarContributor contributor;
	protected final static String WINDOW_TITLE = CDEMessages.getString("CustomizeLayoutWindow.title"); //$NON-NLS-1$
	
	protected static final String CUSTOMIZE_LAYOUT_PAGE_KEY = "customizeLayoutPage_Key"; //$NON-NLS-1$
	
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
		super(CDEMessages.getString("CustomizeLayoutWindowAction.label"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
		setId(ACTION_ID);
		setImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/aligndialog_obj.gif")); //$NON-NLS-1$
		setDisabledImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/dlcl16/aligndialog_obj.gif")); //$NON-NLS-1$
		setHoverImageDescriptor(
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/clcl16/aligndialog_obj.gif")); //$NON-NLS-1$
			
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
		setToolTipText(isChecked() ? CDEMessages.getString("CustomizeLayoutWindowAction.tooltip.hide") : CDEMessages.getString("CustomizeLayoutWindowAction.tooltip.show")); //$NON-NLS-1$ //$NON-NLS-2$
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
		if (editorPart != null && editorPart.getEditorSite().getActionBarContributor() == contributor) {
			run(); // Bring it up if necessary
		} else if (fDialog != null) {
			// Switching to no editor or to an editor that is not of interest, so hide the dialog. 
			// Also set no editor into dialog so that the editor is not held onto while hidden or gone.
			fDialog.hide();
			fDialog.setEditorPart(null);
			update(StructuredSelection.EMPTY);
		}
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
		workbenchWindow.getSelectionService().removeSelectionListener(selListener);
		workbenchWindow.getPartService().removePartListener(alignmentWindowPartListener);
		editorPart = null;
		contributor = null;
		workbenchWindow = null;
		
		if (fDialog != null) {
			fDialog.close();
			fDialog = null;
		}
	}
	public void run() {
		// If we are checked then close the dialog
		if (!isChecked()) {
			if (fDialog != null) {
				fDialog.close();
				fDialog = null;
			}
		} else {
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
					}
					public void controlResized(ControlEvent event) {
					}
				});
				fDialog.getShell().addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent event) {
						persistPreferences();
						setChecked(false);
						fDialog = null;
						update(StructuredSelection.EMPTY);
					}
				});			
			} else
				fDialog.open();	// Reshow it.				
			fDialog.setEditorPart(editorPart); // Reinitialize it
			update(workbenchWindow.getSelectionService().getSelection());			
		}
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
}
