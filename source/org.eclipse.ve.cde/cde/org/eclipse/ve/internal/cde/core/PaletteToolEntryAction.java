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
 *  $RCSfile: PaletteToolEntryAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;

public class PaletteToolEntryAction extends Action implements IPaletteContributionItem {

	protected IPaletteContributionItem parent; // Optional parent if we are on a menu and not a top level entry
	protected ToolEntry toolEntry;
	protected EditDomain editDomain;

	public PaletteToolEntryAction(IPaletteContributionItem aParent, ToolEntry aToolEntry) {
		this(aToolEntry);
		parent = aParent;
		setEnabled(getEditDomain() != null);
	}

	public PaletteToolEntryAction(ToolEntry aToolEntry) {
		setToolEntry(aToolEntry);
		setEnabled(false);
	}
	
	protected PaletteToolEntryAction(int style) {
		super("", style); //$NON-NLS-1$
		setEnabled(false);
	}
	
	/**
	 * This is used for the entries that need to be on the toolbar/menu but
	 * the palette is not yet loaded. Later the setToolEntry will be used
	 * to set appropriate items. The problem is the the Outline needs these
	 * actions before they are created for the global action handler to be set.
	 * So empty ones will be created and then when the palette is available, the
	 * entry will be set into the action.
	 */
	public PaletteToolEntryAction() {
		setEnabled(false);
	}
	
	public PaletteToolEntryAction(EditDomain ed) {
		setEditDomain(ed);
		setEnabled(false);	// Until we have a toolentry
	}
	
	public void setToolEntry(ToolEntry aToolEntry) {
		setId(aToolEntry.getLabel());		
		setHoverImageDescriptor(aToolEntry.getSmallIcon());
		setText(aToolEntry.getLabel());
		if (aToolEntry.getDescription() == null)
			setToolTipText(aToolEntry.getLabel());
		else
			setToolTipText(aToolEntry.getDescription());
		toolEntry = aToolEntry;
		setEnabled(getEditDomain() != null);		
	}
	
	public void run() {
		EditDomain ed = getEditDomain();
		if (ed != null) {
			PaletteViewer pv = ed.getPaletteViewer();
			if (pv != null)
				ed.getPaletteViewer().setActiveTool(toolEntry);
		}
	}
	
	/**
	 * Return the local edit domain or else ask the parent
	 * This means that only the top level set of config items need to explicitly store
	 * the edit domain, and the children can just cascade up
	 * This saves time when switching editors and notifying of a new edit domain
	 * as only the top level need to be informed
	 */
	public EditDomain getEditDomain() {
		if (editDomain != null) {
			return editDomain;
		} else if (parent != null) {
			return parent.getEditDomain();
		} else {
			return null;
		}
	}
	
	public void setEditDomain(EditDomain anEditDomain) {
		setEnabled(anEditDomain != null);
		editDomain = anEditDomain;
	}
}
