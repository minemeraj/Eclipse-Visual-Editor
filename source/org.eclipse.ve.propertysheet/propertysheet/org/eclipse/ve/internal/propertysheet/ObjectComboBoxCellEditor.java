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
 *  $RCSfile: ObjectComboBoxCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:32:00 $ 
 */



import java.text.MessageFormat;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A cell editor that presents a list of items in a combo box.
 * This is an Object combobox cell editor. Subclasses handle specific
 * objects for entries, not just integers.
 */
public abstract class ObjectComboBoxCellEditor extends CellEditor {
	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;
	/**
	 * The zero-based index of the selected item.
	 */
	private int selection = sNoSelection;
	/**
	 * The custom combo box control.
	 */
	private CCombo comboBox;
	
	/**
	 * The set value in the cell editor.
	 */
	private Object setValue;
	
	protected static final int sNoSelection = -1;
	
public ObjectComboBoxCellEditor(Composite parent) {
	super(parent);
}

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
public ObjectComboBoxCellEditor(Composite parent, String[] items) {
	super(parent);
	this.items = items;
	if (items != null)
		populateComboBoxItems();
}

protected Control createControl(Composite parent) {
	
	comboBox = new CCombo(parent, getCComboStyle());
	comboBox.setFont(parent.getFont());

	comboBox.addKeyListener(new KeyAdapter() {
		public void keyReleased(KeyEvent e) {
			keyReleaseOccured(e);
		}
	});

	comboBox.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			int newSelection = comboBox.getSelectionIndex();
			if (newSelection == selection)
				return;
			selection = newSelection;
			Object newValue = doGetObject(selection);
			boolean oldValidState = isValueValid();
			boolean newValidState = isCorrect(newValue);
			
			if (!newValidState) {
				// try to insert the current value into the error message.
				setErrorMessage(
					MessageFormat.format(getErrorMessage(), new Object[] {items[selection]}));
			}
			valueChanged(oldValidState, newValidState, newValue);
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
 * Override to return a different CCombo style. See the CCombo style to see
 * what is valid. The default we are using is Read Only. 
 */
protected int getCComboStyle() {
	return SWT.READ_ONLY;
}

/**
 * Need the following methods because we don't have access to
 * the super's methods from within the inner classes that are in createControl();
 */
protected void fireApplyEditorValue() {
	super.fireApplyEditorValue();
}

protected void keyReleaseOccured(KeyEvent e) {
	super.keyReleaseOccured(e);
}
protected void doSetFocus() {
	comboBox.setFocus();
}
protected void setErrorMessage(String message) {
	super.setErrorMessage(message);
}
protected void valueChanged(boolean oldValidState, boolean newValidState, Object newValue) {
	super.valueChanged(oldValidState, newValidState);
	if (newValidState)
		setValue = newValue;
}


/**
 * Return selection index that is in the combo box.
 */
protected final int getSelectionIndex() {
	return selection;
}

/**
 * Get the display string list.
 */
protected final String[] getDisplayStrings() {
	return items;
}

/**
 * Default implementation. Ask if this is the correct object, and if
 * not set the msg, else let the validators handle it.
 */
protected boolean isCorrect(Object value) {
	String eMsg = isCorrectObject(value);
	if (eMsg == null || eMsg.length() == 0)
		return super.isCorrect(value);	// Let validator give it a try.
	
	setErrorMessage(eMsg);	// This should only occur on initial setting, cboxcelleditor will make it valid boolean upon selection.
	return false;
}

/**
 * Return an error message if this is not a valid value.
 * This is a test in addition to the validators. In other
 * words there are some basic criteria of what constitutes
 * valid value. That is what isCorrectObject does. Then
 * the validators will refine this and say whether it is
 * valid in addition.
 */
protected abstract String isCorrectObject(Object value);

/**
 * Subclasses need to implement returning the object
 * that the index represents. This is called when
 * editing and a selection from the combobox is sent
 * in and we need to send the object that it represents
 * up to the validators. The index to convert will be
 * passed in.
 *
 * NOTE: This won't be called if the index is the one
 * for the current value. That way a change won't be
 * signaled if it is the set value.
 */
protected abstract Object doGetObject(int index);


/**
 * Return the value of the 
 */
protected Object doGetValue() {
	return setValue;
}

/**
 * Return the currently set value.
 */
protected final Object getSetValue() {
	return setValue;
}

/**
 * The object is being passed in, return
 * the index to be used in the editor.
 *
 * It should return sNoSelection if the value can't be
 * converted to an index. The errormsg will have
 * already been set in this case.
 */
protected abstract int doGetIndex(Object value);

/**
 * This sets the index of the selection that is to go into the
 * editor. This can be used to set a
 * select at any time. Typically this
 * isn't necessary, it is handle by doSetValue.
 */
protected final void doSetEditorSelection(int selection) {
	this.selection = selection;
	comboBox.select(selection);
}

/**
 * This is called when a doSetValue has been called.
 *
 * This is not abstract, but a default
 * implementation of doSetObject. It
 * does nothing. Implementers may 
 * do something else with it, such
 * as build list when the value changes.
 */
protected void doSetObject(Object value) {
}

/**
 * A new value is being set into the editor.
 * doSetObject will be called to allow
 * the implementers to do something
 * when a new value is sent in.
 */
protected void doSetValue(Object value) {
	setValue = value;
	doSetObject(value);	// Let implementers do something with it.
	doSetEditorSelection(doGetIndex(value));
}

/**
 * Add the items to the combo box.
 */
protected void populateComboBoxItems() {
	if (comboBox != null && items != null) {
		// if it's already populated, remove the old items first
		if (comboBox.getItemCount() > 0)
			comboBox.removeAll();
		// now add the new items
		for (int i = 0; i < items.length; i++)
			comboBox.add(items[i], i);
	}
}
/**
 * Store the array and populate the widget.
 */
protected void setItems(String [] items) {
	this.items = items;
	populateComboBoxItems();
}

/**
 * @see org.eclipse.jface.viewers.CellEditor#getLayoutData()
 */
public LayoutData getLayoutData() {
	LayoutData ld = super.getLayoutData();
	ld.minimumWidth = 50;	
	return ld;
}

}