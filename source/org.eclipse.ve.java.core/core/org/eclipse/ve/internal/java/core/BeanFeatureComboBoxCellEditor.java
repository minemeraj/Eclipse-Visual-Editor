package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanFeatureComboBoxCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.swt.widgets.Composite;

public class BeanFeatureComboBoxCellEditor extends ComboBoxCellEditor implements BeanFeatureEditor.IWrappedCellEditor {

	protected String[] items;
	/**
	 * Constructor for BeanFeatureComboBoxCellEditor.
	 * @param parent
	 * @param items
	 */
	public BeanFeatureComboBoxCellEditor(Composite parent, String[] items) {
		super(parent, items);
		this.items = items;
	}

	/**
	 * @see IWrappedCellEditor#newValue(String)
	 */
	public void newValue(String text) {
		// Find where the text value occurs in the list of valid tags and use this as the value
		// This is because CellEditor's value is the Integer index of the value in the list
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(text)) {
				setValue(new Integer(i));
			}
		}
		
	}

}
