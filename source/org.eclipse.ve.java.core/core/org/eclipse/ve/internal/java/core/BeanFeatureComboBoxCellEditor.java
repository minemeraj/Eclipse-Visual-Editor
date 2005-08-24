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
/*
 *  $RCSfile: BeanFeatureComboBoxCellEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.core;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;
/*
 * Combo box cell editor for working with BeanFeatureEditor. It uses the tags as both the text and the items.
 * The BeanFeatureEditor knows how to turn the tags back into the appropriate real object.
 * 
 * <package-protected> because it only makes sense working with the BeanFeatureEditor.
 * Too tightly tied together. 
 * 
 * @since 1.0.0
 */
class BeanFeatureComboBoxCellEditor extends ObjectComboBoxCellEditor implements BeanFeatureEditor.IWrappedCellEditor {

	/**
	 * Constructor for BeanFeatureComboBoxCellEditor.
	 * @param parent
	 * @param items
	 */
	public BeanFeatureComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items, SWT.READ_ONLY);
	}

	/* (non-Javadoc)
	 * @see IWrappedCellEditor#newValue(String)
	 */
	public void newValue(String text) {
		setValue(text);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor#doGetIndex(java.lang.Object)
	 */
	protected int doGetIndex(Object value) {
		if (value instanceof String) {
			String text = (String) value;
			String[] items = getItems();
			for (int i = 0; i < items.length; i++) {
				if (items[i].equals(text)) {
					return i;
				}
			}
		}
		return NO_SELECTION;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor#doGetObject(int)
	 */
	protected Object doGetObject(int index) {
		String[] items = getItems();
		if (index >= 0 && index < items.length)
			return items[index];
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor#isCorrectObject(java.lang.Object)
	 */
	protected String isCorrectObject(Object value) {
		return null;	// We'll accept anything here, let the Java PropertyEditor handle the validation.
	}
}
