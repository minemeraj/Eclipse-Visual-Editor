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
 *  $RCSfile: NumberPropertyDescriptor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:44:29 $ 
 */


import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
/**
 * A Generic property descriptor for numbers.
 * It will use the NumberCellEditor and Label provider.
 * These editors and providers display and format the numbers in locale format.
 */
public class NumberPropertyDescriptor extends EToolsPropertyDescriptor {
	protected static final NumberLabelProvider sLabelProvider = new NumberLabelProvider();	// Need only one, they are not descriptor specific.
	
	public NumberPropertyDescriptor(Object propertyID, String propertyDisplayname) {
		super(propertyID, propertyDisplayname);
		
		setLabelProvider(sLabelProvider);	// The default provider, this can be overridden by just setting in a different value after creating descriptor.
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new NumberCellEditor(parent);
		ICellEditorValidator v = getValidator();
		if (v != null)
			editor.setValidator(v);				
		return editor;
	}
}
