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
 *  $RCSfile: CustomizeLayoutPage.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:12:49 $ 
 */

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;

/**
 * This is the base class for any CustomizeLayout page that can go on the CustomizeLayoutWindow.
 */
public abstract class CustomizeLayoutPage {
	
	public final static String LAYOUT_POLICY_KEY = "LAYOUTPOLICY"; //$NON-NLS-1$
	public final static String LAYOUT_FILTER_KEY = CDEActionFilter.EDITPOLICY_STRING + LAYOUT_POLICY_KEY;

	/*
	 * Set the selection provider. This will only be called once, right after
	 * construction. The provider is guarenteed to return the same selection
	 * as the selection sent into update(ISelection) execution time. This is
	 * for pages that are using GEF SelectionActions, which don't allow for
	 * setting specific selections. 
	 *
	 * <package-protected> so that only CustomizeLayoutWindow can call it.
	 *  
	 * @param selectionProvider
	 */
	void setSelectionProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
		handleSelectionProviderInitialization(selectionProvider);
	}
		
	/*
	 * During initialization this will be called with the selection provider.
	 * It will only be called once. If new SelectionActions are created that need
	 * it afterwards, it can use the getSelectionProvider() to get it.
	 *  
	 * @param selectionProvider
	 */
	protected abstract void handleSelectionProviderInitialization(ISelectionProvider selectionProvider);
		
	/**
	 * Return the control for this page, parent on the given parent (which will be a TabFolder).
	 * 
	 * Note: The control that is returned may be disposed and then this call made again to create a new control.
	 * 
	 * @param parent
	 * @return control for this page.
	 */
	public abstract Control getControl(Composite parent);
	
	private ISelection selection = StructuredSelection.EMPTY;
	
	/**
	 * The selection has changed. The page should respond by enabling/disabling
	 * the actions, and or controls on the control (if created), depending upon
	 * the selection. These selections can come from any selection provider, so
	 * test the selection to make sure it is compatible to the expectations of
	 * page. It may not even necessarily be an IStructuredSelection.
	 * 
	 * Note: When first created, it is guarenteed that setEditorPart and setSelection
	 * will be called immediately in that order. However, editorpart may be null at that time.
	 * This method may also be called before the getControl() is invoked.
	 * 
	 * @param selection
	 * @return whether this page is relevant to the current selection
	 */
	public final boolean update(ISelection selection) {
		ISelection oldSelection = this.selection;
		this.selection = selection;
		return handleSelectionChanged(oldSelection);
	}
	
	/*
	 * Called whenever selection has changed.
	 * This may be called before getControl() is invoked.
	 * 
	 * @param oldSelection The previous selection.
	 */
	protected abstract boolean handleSelectionChanged(ISelection oldSelection);
	
	protected abstract String getLabelForSelection(ISelection newSelection);
	
	/*
	 * Called to determine whether a Layout page applies to the selection or
	 * a component's parent container.  This is used to raise the Layout tab
	 * to the front when the selection is a container and has a customizer page. 
	 * 
	 * This returns false by default, so Component pages do not have to override.  Layout
	 * pages should override this method if they want to be able to be raised.
	 */
	protected boolean selectionIsContainer(ISelection newSelection) {
		return false;
	}
	
	private ISelectionProvider selectionProvider;
	
	/*
	 * Return the ISelectionProvider. This selection provider
	 * is guarenteed to return the same selection as the
	 * selection sent in at update(ISelection) execution. If null,
	 * then the workbench selection service is the provider, and it
	 * will fulfill this contract also. This is a special selection
	 * provider that really only handles returning selection. It does
	 * not fire selections. It is here simply for GEF SelectionActions
	 * so that we can clear out the selection when an editor is closed
	 * or switching to a non Alignment editor.
	 * 
	 * @return The selection provider.
	 */
	protected final ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}
	
	/*
	 * Return the current selection.
	 * 
	 * @return current selection
	 */
	protected final ISelection getSelection() {
		return selection;
	}

	private IEditorPart editorPart;
	/**
	 * Set the current editor part being handled.
	 * 
	 * Note: When first created, it is guarenteed that setEditorPart and setSelection
	 * will be called immediately in that order. However, editorpart may be null at that time.
	 * 
	 * @param editorPart (null if no current editor part).
	 */
	public final void setEditorPart(IEditorPart editorPart) {
		IEditorPart oldEP = this.editorPart;
		this.editorPart = editorPart;
		handleEditorPartChanged(oldEP);
	}
	
	/*
	 * Called whenever editor part has changed.
	 * 
	 * @param oldEditorPart The previous editor part.
	 */
	protected abstract void handleEditorPartChanged(IEditorPart oldEditorPart); 
	
	/*
	 * Return the current editor part.
	 * 
	 * @return current editor part
	 */
	protected final IEditorPart getEditorPart() {
		return editorPart;
	}	
}
