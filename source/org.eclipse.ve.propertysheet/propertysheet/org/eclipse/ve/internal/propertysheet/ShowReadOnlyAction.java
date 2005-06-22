package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ShowReadOnlyAction.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-22 15:21:41 $ 
 */

import org.eclipse.ui.views.properties.IPropertySheetEntry;

/**
 * This action is used to toggle the property sheet viewer
 * to show or not show read only property values
 */

public class ShowReadOnlyAction extends EToolsPropertySheetAction {
	public ShowReadOnlyAction(EToolsPropertySheetPage page) {
		super(page);
		setText(PropertysheetMessages.getString("show_read_only.label"));
		setChecked(false);
	}		

	public void run() {
		IPropertySheetEntry root = getRootEntry();
		if (root instanceof IDescriptorPropertySheetEntry) {
			propertySheet.deactivateCellEditor();	// Deactivate the current cell editor because we are rebuilding everything under there feet.
			IDescriptorPropertySheetEntry rootEntry = (IDescriptorPropertySheetEntry) root;
			rootEntry.setShowReadOnly(isChecked());
			rootEntry.refreshFromRoot();
		}		
	}
	
	public void setChecked(boolean value) {
		super.setChecked(value);
		valueChanged(value);
	}
	
	private void valueChanged(boolean on) {
		setToolTipText(on ? PropertysheetMessages.getString("show_read_only_hide.tooltip") : PropertysheetMessages.getString("show_read_only_show.tooltip"));
	}

}