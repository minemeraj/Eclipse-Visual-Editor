/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: EToolsPropertySheetPage.java,v $
 *  $Revision: 1.14 $  $Date: 2006-05-17 20:16:06 $ 
 */


import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.*;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.properties.*;
/**
 * This is the property sheet page to use with eTools Property Sheet.
 * It allows the interaction with actions added to the tool bar with
 * the AbstractPropertySheetEntry.
 */
public class EToolsPropertySheetPage extends PropertySheetPage implements ISelectionProvider {
	protected ShowNullsAction fShowNullsAction;
	protected ShowSetValuesAction fShowSetValuesAction;	
	protected SetToNullAction fSetToNullAction;
	protected ShowReadOnlyAction fShowReadOnlyAction;
	
	protected ListenerList fSelectionListeners = new ListenerList(ListenerList.IDENTITY);
	protected Viewer propertySheetViewer;	// They hide it so we can't do anything with it and we need it so we will get and use reflection on it.
	protected Method deactivateCellEditorMethod;
	protected IDescriptorPropertySheetEntry rootEntry;
	
	public static class EToolsPropertySheetSorter extends PropertySheetSorter {
		public void sort(IPropertySheetEntry[] entries) {
			// Don't sort here because we sort the property descriptors in the AbstractPropertySheetEntry
			// so that we could reuse entries... building new entries is slow... see https://bugs.eclipse.org/bugs/show_bug.cgi?id=97593
			return;
		}
	}
	
	/**
	 * This is an interface to know when the control has been created. It really
	 * is exposing the internals and shouldn't be visible, but the property sheet
	 * hides so much that it may be needed to be able to access the control
	 */
	public interface Listener{
		public void controlCreated(Control control);
	}
	
	protected ListenerList fListeners = new ListenerList(ListenerList.IDENTITY);
	
	public void addListener(Listener aListener){
		fListeners.add(aListener);
	}
	public void removeListener(Listener aListener){
		fListeners.remove(aListener);
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener selListener){
		fSelectionListeners.add(selListener);
	}
	
	public void removeSelectionChangedListener(ISelectionChangedListener selListener) {
		fSelectionListeners.remove(selListener);
	}
	
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.setSelectionProvider(this);
		setSorter(new EToolsPropertySheetSorter());
	}	
	
	public void handleEntrySelection(ISelection selection) {
		super.handleEntrySelection(selection);
		if (fSetToNullAction != null)
			fSetToNullAction.selectionChanged(selection);
			
		// Now notify everyone else
		if (!fSelectionListeners.isEmpty()) {
			Object[] listeners = fSelectionListeners.getListeners();
			for (int i = 0; i < listeners.length; i++)
				((ISelectionChangedListener) listeners[i]).selectionChanged(new SelectionChangedEvent(this, selection));
		}
	}
	
	public void setSelection(ISelection selection) {
		// We currently don't allow selection to be set.
	}
	
	
	public IPropertySheetEntry getRootEntry() {
		Control tree = getControl();		
		return (tree != null) ?
			(IPropertySheetEntry) tree.getData() : null;
	}
	
	public ISelection getSelection() {
		// If the idiots would just expose the PropertySheetViewer, I could ask it, but they don't. Instead
		// I have to dig in and get the control, which for now is a Tree.
		Control tree = getControl();
		if (tree == null || (((Tree) tree).getSelectionCount() == 0))
			return StructuredSelection.EMPTY;

		List entries;
		TreeItem[] sel = ((Tree) tree).getSelection();
		entries = new ArrayList(sel.length);
		for (int i = 0; i < sel.length; i++) {
			TreeItem ti = sel[i];
			Object data = ti.getData();
			if (data instanceof IPropertySheetEntry)
				entries.add(data);
		}

		return new StructuredSelection(entries);
	}	
	
	public void createControl(Composite parent) {
		super.createControl(parent);

		// Kludge: They hide the propertysheetviewer and are things that need to be done with it
		// because we are far more active then they expect. So we will need to access it reflectively.
		try {
			Field viewerField = PropertySheetPage.class.getDeclaredField("viewer"); //$NON-NLS-1$
			viewerField.setAccessible(true);
			propertySheetViewer = (Viewer) viewerField.get(this);
			deactivateCellEditorMethod = propertySheetViewer.getClass().getDeclaredMethod("deactivateCellEditor", null); //$NON-NLS-1$
			deactivateCellEditorMethod.setAccessible(true);
		} catch (NoSuchFieldException e) {
		} catch (IllegalAccessException e) {
		} catch (NoSuchMethodException e) {
		}

		Control tree = getControl();
		if (!fListeners.isEmpty()) {
			// Signal the fact that the control has been created to any
			// listeners.  This is so that people who want to listen to the control
			// know the point after which the control has been created
			Object[] listeners = fListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				((Listener) listeners[i]).controlCreated(tree);
			}
			fListeners.clear();	// Since no longer needed and we are never recreated once disposed.
		}
	}
	
	public void deactivateCellEditor() {
		try {
			deactivateCellEditorMethod.invoke(propertySheetViewer, null);
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	
	protected void makeActions() {	
		fShowNullsAction = new ShowNullsAction(this);
		fShowSetValuesAction = new ShowSetValuesAction(this, true);	// Later this will be turned into a preference
		fSetToNullAction = new SetToNullAction(this);
		fShowReadOnlyAction = new ShowReadOnlyAction(this);
		
		fShowNullsAction.run();	// If there is a root, set the setting.
		fShowSetValuesAction.run();
		fShowReadOnlyAction.run();
	}
		
	public void makeContributions(
		IMenuManager menuManager,
		IToolBarManager toolBarManager,
		IStatusLineManager statusLineManager) {
	
		super.makeContributions(menuManager, toolBarManager, statusLineManager);
		
		makeActions();
	
		menuManager.add(fShowNullsAction);
		menuManager.add(fShowSetValuesAction);	
		menuManager.add(fShowReadOnlyAction);	

		toolBarManager.add(fSetToNullAction);
	}
	
	public void setRootEntry(IPropertySheetEntry entry) {	
		super.setRootEntry(entry);
		
		rootEntry = entry instanceof IDescriptorPropertySheetEntry ? (IDescriptorPropertySheetEntry) entry : null;
		// Got a new root entry, so have the ShowNullsAction run through and update it.
		if (fShowNullsAction != null)
			fShowNullsAction.run();
		if (fShowSetValuesAction != null)
			fShowSetValuesAction.run();
		if (fShowReadOnlyAction != null)
			fShowReadOnlyAction.run();
			
	}

	private ISelection selectionQueued;
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// We are going to override this so that only one input is set (often several are sent for the same input)
		// and so that we only do one pass through the new input. If we let it go now, it would go through it
		// now, and then go through the entire sheet again later.
		if(rootEntry == null || !(selection instanceof IStructuredSelection))
			super.selectionChanged(part, selection);
		else {
			// Queue it up so that all of the selections can be handled at one time.
			// These can happen several at a time, so we will queue them up so they only happen once.
			synchronized (this) {
				if (selection.equals(selectionQueued))
					return;	// Same selection, still queued up, so don't bother doing again.
				selectionQueued = selection;
				rootEntry.markStale();	// Mark all of my children as stale.
			}

			Display d = Display.getCurrent();
			if (d == null)
				d = Display.getDefault();
			d.asyncExec(new Runnable() {
				public void run() {
					Control ctrl = getControl();
					if (ctrl != null && !ctrl.isDisposed()) {
						synchronized (EToolsPropertySheetPage.this) {
							if (!selection.equals(selectionQueued))
								return;	// We haven't processed the last queued up selection yet. When we reach we go on. We skip intermediate ones since they are not the current.
							selectionQueued = null;	// Processed the selection.
						}
						superSelectionChanged(part, selection);
					}
				}
			});
		}
	}
	
	private void superSelectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// TODO This is a total hack but it makes switching between entries snapper because there is no visible washing as the
		// old items are swapped out for the new ones.  Bugzilla 53997 is entered against the platform to have the inherited behavior changed		
		Control tree = getControl();
		if (tree != null) {
			tree.setRedraw(false);
			try {
				super.selectionChanged(part, selection);
			} finally {
				// So that even if some exception occurs we will turn tree back on.
				tree.setRedraw(true); 
			}			
		} else
			super.selectionChanged(part, selection);

	}
};
