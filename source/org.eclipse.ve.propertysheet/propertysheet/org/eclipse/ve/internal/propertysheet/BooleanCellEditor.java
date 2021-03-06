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
 *  $RCSfile: BooleanCellEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:44:29 $ 
 */


import org.eclipse.swt.widgets.Composite;
/**
 * Celleditor for handling booleans. It can get a non-boolean as input.
 * This is an error, but it doesn't throw an exception.
 */

public class BooleanCellEditor extends StandardComboBoxCellEditor {
	protected static final int
		TRUE_INDEX = 0,
		FALSE_INDEX = 1;
			
	public BooleanCellEditor(Composite parent){
		super(parent, 
			new String[] {
				PropertysheetMessages.display_true,
				PropertysheetMessages.display_false
			},
			new Object[] {
				Boolean.TRUE,
				Boolean.FALSE
			});
	}	
	
	/**
	 * Return an error message if this is not a valid boolean
	 */
	protected String isCorrectObject(Object value) {
		if (value == null || value instanceof Boolean)
			return null;
			
		return PropertysheetMessages.bad_bool_WARN_;
	}		
	
}


