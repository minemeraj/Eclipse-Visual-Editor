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
 *  $RCSfile: ComboBoxCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.text.MessageFormat;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A cell editor that presents a list of items in a combo box.
 * The cell editor's value is the zero-based index of the selected
 * item.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * HACK....
 * This class is a twin of org.eclipse.jface.viewers.ComboBoxCellEditor and
 * is copied here because we need a combo box with the capability for the text 
 * field to be read-only.
 * It is used in the BeanFeatureEditor but can be used elsewhere as well.
 */
public class ComboBoxCellEditor extends CellEditor {

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	/**
	 * The zero-based index of the selected item.
	 */
	private int selection;

	/**
	 * The custom combo box control.
	 */
	private CCombo comboBox;

/**
 * Creates a new cell editor with a combo containing the given 
 * list of choices and parented under the given control. The cell
 * editor value is the zero-based index of the selected item.
 * Initially, the cell editor has no cell validator and
 * the first item in the list is selected. 
 *
 * @param parent the parent control
 * @param items the list of strings for the combo box
 */
public ComboBoxCellEditor(Composite parent, String[] items) {
	super(parent);
	Assert.isNotNull(items);
	this.items = items;
	selection = 0;
	populateComboBoxItems();
}
/* (non-Javadoc)
 * Method declared on CellEditor.
 */
protected Control createControl(Composite parent) {
	
	comboBox = new CCombo(parent, SWT.READ_ONLY);
	comboBox.setFont(parent.getFont());

	comboBox.addKeyListener(new KeyAdapter() {
		// hook key pressed - see PR 14201  
		public void keyPressed(KeyEvent e) {
			keyReleaseOccured(e);
		}
	});

	comboBox.addSelectionListener(new SelectionAdapter() {
		public void widgetDefaultSelected(SelectionEvent event) {
			// must set the selection before getting value
			selection = comboBox.getSelectionIndex();
			Object newValue = doGetValue();
			boolean newValidState = isCorrect(newValue);
			if (newValidState) {
				doSetValue(newValue);
			} else {
				// try to insert the current value into the error message.
				setErrorMessage(
					MessageFormat.format(getErrorMessage(), new Object[] {items[selection]})); 
			}
			fireApplyEditorValue();
		}
	});

	comboBox.addTraverseListener(new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
			if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
			}
		}
	});

	return comboBox;
}
/**
 * The <code>ComboBoxCellEditor</code> implementation of
 * this <code>CellEditor</code> framework method returns
 * the zero-based index of the current selection.
 *
 * @return the zero-based index of the current selection wrapped
 *  as an <code>Integer</code>
 */
protected Object doGetValue() {
	return new Integer(selection);
}
/* (non-Javadoc)
 * Method declared on CellEditor.
 */
protected void doSetFocus() {
	comboBox.setFocus();
}
/**
 * The <code>ComboBoxCellEditor</code> implementation of
 * this <code>CellEditor</code> framework method sets the 
 * minimum width of the cell to 50 pixels to make sure the
 * arrow button and some text is visible. The list of CCombo
 * will be wide enough to show its longest item.
 */
public LayoutData getLayoutData() {
	LayoutData layoutData = super.getLayoutData();
	layoutData.minimumWidth = 50;
	return layoutData;
}
/**
 * The <code>ComboBoxCellEditor</code> implementation of
 * this <code>CellEditor</code> framework method
 * accepts a zero-based index of a selection.
 *
 * @param value the zero-based index of the selection wrapped
 *   as an <code>Integer</code>
 */
protected void doSetValue(Object value) {
	Assert.isTrue(comboBox != null && (value instanceof Integer));
	selection = ((Integer) value).intValue();
	comboBox.select(selection);
}
/**
 * Add the items to the combo box.
 */
private void populateComboBoxItems() {
	if (comboBox != null && items != null) {
		for (int i = 0; i < items.length; i++)
			comboBox.add(items[i], i);

		setValueValid(true);
	}
}
}
