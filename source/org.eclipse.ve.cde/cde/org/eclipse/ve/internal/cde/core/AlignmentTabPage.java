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
 *  $RCSfile: AlignmentTabPage.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;

/**
 * This is the base class for any AlignmentTab page that can go on the AlignmentWindow.
 */
public abstract class AlignmentTabPage {

	/*
	 * Set the selection provider. This will only be called once, right after
	 * construction. The provider is guarenteed to return the same selection
	 * as the selection sent into update(ISelection) execution time. This is
	 * for pages that are using GEF SelectionActions, which don't allow for
	 * setting specific selections. 
	 *
	 * <package-protected> so that only AlignmentWindow can call it.
	 *  
	 * @param selectionProvider
	 */
	void setSelectionProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
		handleSelectionProviderInitialization(selectionProvider);
	}
		
	/**
	 * During initialization this will be called with the selection provider.
	 * It will only be called once. If new SelectionActions are created that need
	 * it afterwards, it can use the getSelectionProvider() to get it.
	 *  
	 * @param selectionProvider
	 */
	protected abstract void handleSelectionProviderInitialization(ISelectionProvider selectionProvider);
	
	/**
	 * Return the label that goes on the tab.
	 * @return text
	 */
	public abstract String getText();
	
	/**
	 * Return the tooltip text for the tab.
	 * @return tooltip text (null if none)
	 */
	public abstract String getToolTipText();
	
	/**
	 * Return the image for the tab.
	 * @return image (null if none);
	 */
	public abstract Image getImage();
	
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
	 * 
	 * @param selection
	 */
	public final void update(ISelection selection) {
		ISelection oldSelection = this.selection;
		this.selection = selection;
		handleSelectionChanged(oldSelection);
	}
	
	/*
	 * Called whenever selection has changed.
	 * 
	 * @param oldSelection The previous selection.
	 */
	protected abstract void handleSelectionChanged(ISelection oldSelection); 
	
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
