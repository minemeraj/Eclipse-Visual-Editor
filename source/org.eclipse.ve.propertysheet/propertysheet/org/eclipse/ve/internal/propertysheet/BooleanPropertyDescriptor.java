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
 *  $RCSfile: BooleanPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */


import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
/**
 * A Generic property descriptor for Strings.
 * It will use StringCellEditor.
 */
public class BooleanPropertyDescriptor extends EToolsPropertyDescriptor {
	protected static final BooleanLabelProvider sLabelProvider = new BooleanLabelProvider();	// Need only one, they are not descriptor specific.
	
	public BooleanPropertyDescriptor(Object propertyID, String propertyDisplayname) {
		super(propertyID, propertyDisplayname);
		
		setLabelProvider(sLabelProvider);	// The default provider, this can be overridden by just setting in a different value after creating descriptor.		
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new BooleanCellEditor(parent);
		ICellEditorValidator v = getValidator();
		if (v != null)
			editor.setValidator(v);			
		return editor;
	}
}