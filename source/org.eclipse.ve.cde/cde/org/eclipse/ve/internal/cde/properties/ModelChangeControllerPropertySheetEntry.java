/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ModelChangeControllerPropertySheetEntry.java,v $
 *  $Revision: 1.1 $  $Date: 2004-04-01 21:25:25 $ 
 */
package org.eclipse.ve.internal.cde.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import org.eclipse.ve.internal.cde.core.IModelChangeController;

import org.eclipse.ve.internal.propertysheet.DefaultWrapperedValidator;
import org.eclipse.ve.internal.propertysheet.command.CommandStackPropertySheetEntry;
 

/**
 * This is a property sheet entry that uses a IModelChangeController to check for the
 * status of the change controller, and to set error message if not ready for changes.
 * 
 * @since 1.0.0
 */
public class ModelChangeControllerPropertySheetEntry extends CommandStackPropertySheetEntry {

	protected IModelChangeController changeController;

	/**
	 * Create this model change controller property sheet entry.
	 * 
	 * @param changeController
	 * @param stack
	 * @param parent
	 * @param provider
	 * 
	 * @since 1.0.0
	 */
	public ModelChangeControllerPropertySheetEntry(IModelChangeController changeController, CommandStack stack, ModelChangeControllerPropertySheetEntry parent, IPropertySourceProvider provider) {
		super(stack, parent, provider);
		this.changeController = changeController;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.AbstractPropertySheetEntry#processEditorValidator(org.eclipse.jface.viewers.CellEditor)
	 */
	protected void processEditorValidator(CellEditor cellEditor) {
		super.processEditorValidator(cellEditor);
		if (cellEditor != null) {
			cellEditor.setValidator(new DefaultWrapperedValidator(new ICellEditorValidator[] {new ICellEditorValidator() {
				public String isValid(Object value) {
					// It doesn't matter what the value is, check to get msg for model change controller on hold.
					// Since model change controller will return null when ready, that also means no error so far.
					// wrappered validator will continue to next one.
					return changeController.getHoldMsg();
				}
			}, cellEditor.getValidator()}));
		}
	}	
}
