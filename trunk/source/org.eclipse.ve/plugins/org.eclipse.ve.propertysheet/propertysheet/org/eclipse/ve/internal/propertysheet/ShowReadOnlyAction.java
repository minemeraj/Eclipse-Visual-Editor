package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ShowReadOnlyAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */

import org.eclipse.ui.views.properties.IPropertySheetEntry;

/**
 * This action is used to toggle the property sheet viewer
 * to show or not show read only property values
 */

public class ShowReadOnlyAction extends EToolsPropertySheetAction {
	public ShowReadOnlyAction(EToolsPropertySheetPage page) {
		super(page);
		setText(PropertysheetMessages.getString(PropertysheetMessages.SHOW_READ_ONLY_LABEL));
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
		setToolTipText(on ? PropertysheetMessages.getString(PropertysheetMessages.SHOW_READ_ONLY_HIDE_TOOLTIP) : PropertysheetMessages.getString(PropertysheetMessages.SHOW_READ_ONLY_SHOW_TOOLTIP));
	}

}