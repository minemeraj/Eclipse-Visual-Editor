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
 *  $RCSfile: IntegerPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */


import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
/**
 * This is a property descriptor for integers, and not
 * just numbers in general.
 */
public class IntegerPropertyDescriptor extends NumberPropertyDescriptor {
	public IntegerPropertyDescriptor(Object propertyID, String propertyDisplayname) {
		super(propertyID, propertyDisplayname);
	}	
	
	public CellEditor createPropertyEditor(Composite parent) {
		NumberCellEditor editor = (NumberCellEditor) super.createPropertyEditor(parent);
		editor.setType(NumberCellEditor.INTEGER);
		return editor;
	}	

}