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
 *  $RCSfile: ShowSetValuesAction.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:47:33 $ 
 */



import org.eclipse.ui.views.properties.IPropertySheetEntry;
/**
 * This action is used to toggle the property
 * to show or not show whether an entry is the
 * default value. The standard way is to add an asterick in front of it.
 */
public class ShowSetValuesAction extends EToolsPropertySheetAction {
	public ShowSetValuesAction(EToolsPropertySheetPage page, boolean showSetValues) {
		super(page);
		setText(PropertysheetMessages.getString(PropertysheetMessages.SHOW_SET_VALUES_LABEL));
		setChecked(showSetValues);
	}		

	/**
	 * @see Action
	 */
	public void run() {
		IPropertySheetEntry root = getRootEntry();
		if (root instanceof IDescriptorPropertySheetEntry) {
			propertySheet.deactivateCellEditor();	// Deactivate the current cell editor because we are rebuilding everything under there feet.			
			IDescriptorPropertySheetEntry rootEntry = (IDescriptorPropertySheetEntry) root;
			rootEntry.setShowSetValues(isChecked());
			rootEntry.refreshFromRoot();
		}		
	}
	
	/**
	 * @see Action
	 */
	public void setChecked(boolean value) {
		super.setChecked(value);
		valueChanged(value);
	}
	
	private void valueChanged(boolean on) {
		setToolTipText(on ? PropertysheetMessages.getString(PropertysheetMessages.SHOW_NULLS_HIDE_TOOLTIP) : PropertysheetMessages.getString(PropertysheetMessages.SHOW_NULLS_SHOW_TOOLTIP));
	}

}