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
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: ShowReadOnlyAction.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:44:29 $ 
 */

import org.eclipse.ui.views.properties.IPropertySheetEntry;

/**
 * This action is used to toggle the property sheet viewer
 * to show or not show read only property values
 */

public class ShowReadOnlyAction extends EToolsPropertySheetAction {
	public ShowReadOnlyAction(EToolsPropertySheetPage page) {
		super(page);
		setText(PropertysheetMessages.show_read_only_label);
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
		setToolTipText(on ? PropertysheetMessages.show_read_only_hide_tooltip : PropertysheetMessages.show_read_only_show_tooltip);
	}

}
