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
 *  $RCSfile: StringPropertyDescriptor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:47:33 $ 
 */


import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
/**
 * A Generic property descriptor for Strings.
 * It will use StringCellEditor.
 */
public class StringPropertyDescriptor extends EToolsPropertyDescriptor {
	public StringPropertyDescriptor(Object propertyID, String propertyDisplayname) {
		super(propertyID, propertyDisplayname);
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new StringCellEditor(parent);
		ICellEditorValidator v = getValidator();
		if (v != null)
			editor.setValidator(v);		
		return editor;
	}
}