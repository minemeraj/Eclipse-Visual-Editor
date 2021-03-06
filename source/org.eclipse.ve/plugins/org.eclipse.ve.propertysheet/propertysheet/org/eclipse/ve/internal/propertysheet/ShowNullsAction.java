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
 *  $RCSfile: ShowNullsAction.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:44:29 $ 
 */



import org.eclipse.ui.views.properties.IPropertySheetEntry;
/**
 * This action is used to toggle the property
 * to show or not show nulls as a special
 * value. The English string is "<null>".
 */
public class ShowNullsAction extends EToolsPropertySheetAction {
	public ShowNullsAction(EToolsPropertySheetPage page) {
		super(page);
		setText(PropertysheetMessages.show_nulls_label);
		setChecked(false);
	}		

	/**
	 * @see Action
	 */
	public void run() {
		IPropertySheetEntry root = getRootEntry();
		if (root instanceof IDescriptorPropertySheetEntry) {
			propertySheet.deactivateCellEditor();	// Deactivate the current cell editor because we are rebuilding everything under there feet.			
			IDescriptorPropertySheetEntry rootEntry = (IDescriptorPropertySheetEntry) root;
			rootEntry.setShowNulls(isChecked());
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
		setToolTipText(on ? PropertysheetMessages.show_nulls_hide_tooltip : PropertysheetMessages.show_nulls_show_tooltip);
	}

}
